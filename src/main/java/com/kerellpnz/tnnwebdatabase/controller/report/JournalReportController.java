package com.kerellpnz.tnnwebdatabase.controller.report;

import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.report.JournalReportDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.report.JournalReport;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequestMapping("/journalReport")
public class JournalReportController {

    private final JournalReportDAO journalReportDAO;
    private final InspectorDAO inspectorDAO;
    private final HttpSession session;

    @Value("/resources/files/BaseJournalReport.xlsx")
    private Resource baseJournalReport;
    private List<JournalReport> report;
    private List<Inspector> inspectors;

    @Autowired
    public JournalReportController(JournalReportDAO journalReportDAO, InspectorDAO inspectorDAO, HttpSession session) {
        this.journalReportDAO = journalReportDAO;
        this.inspectorDAO = inspectorDAO;
        this.session = session;
    }


    @RequestMapping("/showMenu")
    public ModelAndView showMenu() {
        ModelAndView mv = new ModelAndView("./report/journal-report");
        inspectors = inspectorDAO.getAll(Inspector.class);
        UserModel userModel = (UserModel) session.getAttribute("userModel");
        TempObject tempObject = new TempObject();
        tempObject.setInspId(userModel.getId());
        tempObject.setDate(new Date());
        mv.addObject("title", "Отчет ЖТН");
        mv.addObject("inspectors", inspectors);
        mv.addObject("tempObject", tempObject);
        return mv;
    }

    @RequestMapping("/getReport")
    public String getReport(@Valid @ModelAttribute("tempObject") TempObject tempObject, BindingResult results, Model model) {
        if(!results.hasErrors()) {
            report = journalReportDAO.getJournalReportByInspectorAndDate(tempObject.getDate(), tempObject.getInspId());
            if (!report.isEmpty()) {
                model.addAttribute("showButton", true);
                model.addAttribute("report", report);
            }
            else
                model.addAttribute("message", "Операции не найдены!");
        }
        model.addAttribute("title", "Отчет ЖТН");
        model.addAttribute("inspectors", inspectors);
        return "./report/journal-report";
    }

    @GetMapping("/createExcelReport")
    public void createExcelReport(HttpServletResponse response) throws IOException {
        if (report != null) {
            if (!report.isEmpty()) {
                Map<String, Map<JournalReport.RowIdentity, List<JournalReport>>> reportsByJourNumAndDetNum = report.stream()
                        .collect(groupingBy(JournalReport::getJournalNumber, groupingBy(jR -> new JournalReport.RowIdentity(jR.getDate(),
                                jR.getPoint(), jR.getName(), jR.getDescription(), jR.getStatus(), jR.getRemark(), jR.getRemarkClosed(),
                                jR.getPlaceOfControl(), jR.getDocuments(), jR.getComment()))));

                Workbook wb = new XSSFWorkbook(baseJournalReport.getInputStream());
                CellStyle styleDes = wb.createCellStyle();
                CellStyle style = wb.createCellStyle();
                Font font = wb.createFont();
                font.setFontName("Franklin Gothic Book");
                font.setFontHeightInPoints((short) 12);
                styleDes.setFont(font);
                styleDes.setBorderTop(BorderStyle.THIN);
                styleDes.setBorderBottom(BorderStyle.THIN);
                styleDes.setBorderLeft(BorderStyle.THIN);
                styleDes.setBorderRight(BorderStyle.THIN);
                styleDes.setWrapText(true);
                styleDes.setAlignment(HorizontalAlignment.JUSTIFY);
                styleDes.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setFont(font);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setWrapText(true);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                int sheetIndex = 1;
                for (var entry : reportsByJourNumAndDetNum.entrySet()) {
                    Sheet sheet = wb.cloneSheet(0);
                    wb.setSheetName(sheetIndex, WorkbookUtil.createSafeSheetName(entry.getKey()));
                    int recordIndex = 3;
                    sheet.getRow(0).getCell(10).setCellValue(entry.getKey());
                    for(var entry1 : entry.getValue().entrySet()) {
                        sheet.createRow(recordIndex).createCell(0).setCellValue(recordIndex-2);
                        sheet.getRow(recordIndex).getCell(0).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(1).setCellValue(entry1.getValue().get(0).getDate());
                        sheet.getRow(recordIndex).getCell(1).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(2).setCellValue(entry1.getValue().get(0).getPoint());
                        sheet.getRow(recordIndex).getCell(2).setCellStyle(style);
                        StringBuilder numbers = new StringBuilder();
                        for(JournalReport row: entry1.getValue()) {
                            numbers.append(row.getNumber()).append(";\n");
                        }
                        sheet.getRow(recordIndex).createCell(3).setCellValue(entry1.getValue().get(0).getName() + ": " + numbers);
                        sheet.getRow(recordIndex).getCell(3).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(4).setCellValue(entry1.getValue().get(0).getDescription());
                        sheet.getRow(recordIndex).getCell(4).setCellStyle(styleDes);
                        sheet.getRow(recordIndex).createCell(5).setCellValue(entry1.getValue().get(0).getPlaceOfControl());
                        sheet.getRow(recordIndex).getCell(5).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(6).setCellValue(entry1.getValue().get(0).getDocuments());
                        sheet.getRow(recordIndex).getCell(6).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(7).setCellValue(entry1.getValue().get(0).getStatus());
                        sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(8).setCellValue(entry1.getValue().get(0).getRemark());
                        sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(9).setCellValue(entry1.getValue().get(0).getRemarkClosed());
                        sheet.getRow(recordIndex).getCell(9).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(10).setCellValue(entry1.getValue().get(0).getEngineer());
                        sheet.getRow(recordIndex).getCell(10).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(11).setCellValue(entry1.getValue().get(0).getComment());
                        sheet.getRow(recordIndex).getCell(11).setCellStyle(style);
                        recordIndex++;
                    }
                    wb.setPrintArea(
                            sheetIndex, //sheet index
                            0, //start column
                            11, //end column
                            0, //start row
                            recordIndex-1  //end row
                    );
                    sheetIndex++;
                }
                wb.removeSheetAt(0);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                wb.write(outputStream);
                ByteArrayInputStream stream = new ByteArrayInputStream(outputStream.toByteArray());

                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=JournalReport.xlsx");
                IOUtils.copy(stream, response.getOutputStream());
            }
        }
    }

    public class TempObject {
        @NotNull(message = "Введите дату!")
        @DateTimeFormat(pattern = "dd.MM.yyyy")
        private Date date;
        @NotNull(message = "Выберите инженера!")
        @Min(value=1, message="Выберите инженера!")
        private Integer inspId;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Integer getInspId() {
            return inspId;
        }

        public void setInspId(Integer inspId) {
            this.inspId = inspId;
        }
    }

}
