package com.kerellpnz.tnnwebdatabase.controller.report;

import com.kerellpnz.tnnwebdatabase.dao.report.FOMReportDAO;
import com.kerellpnz.tnnwebdatabase.entity.report.FOMReport;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequestMapping("/FOMReport")
public class FOMReportController {

    private final FOMReportDAO fomReportDAO;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private List<FOMReport> report;
    private String start;
    private String end;
    private Date newPer;

    @Autowired
    public FOMReportController(FOMReportDAO fomReportDAO) {
        this.fomReportDAO = fomReportDAO;
    }

    @RequestMapping("/showMenu")
    public ModelAndView showMenu() {
        ModelAndView mv = new ModelAndView("./report/fom-report");
        mv.addObject("title", "Отчет по отгрузке");
        return mv;
    }

    @RequestMapping("/getReport")
    public String getReport(@RequestParam("start") String start, @RequestParam("end") String end, @RequestParam("newPer") String newPer, Model model) {
        if(!start.isBlank() && !end.isBlank()) {
            this.start = start;
            this.end = end;
            try {
                if (!newPer.isBlank()) this.newPer = formatter.parse(newPer);
                else this.newPer = null;
                report = fomReportDAO.getShippingValves(formatter.parse(start), formatter.parse(end));
                if (!report.isEmpty()) {
                    model.addAttribute("showButton", true);
                    model.addAttribute("report", report);
                }
                else
                    model.addAttribute("message", "Отгрузки не найдены!");
            }
            catch (ParseException e) {
                model.addAttribute("message", "Неверный формат даты!");
            }
            catch (Exception e) {
                model.addAttribute("message", "Обнаружена ошибка при обработке запроса! Сообщите об ошибке!");
            }
        }
        model.addAttribute("title", "Отчет по отгрузке");
        return "./report/fom-report";
    }

    @GetMapping("/createExcelReport")
    public void createExcelReport(HttpServletResponse response) throws IOException {
        if (report != null) {
            if (!report.isEmpty()) {

                Map<FOMReport.SheetIdentity, List<FOMReport>> reportsByCustomersAndProg = report.stream()
                        .collect(groupingBy(fom -> new FOMReport.SheetIdentity(fom.getCustomerName(), fom.getProgram())));

                Workbook wb = new XSSFWorkbook();
                CellStyle style1 = wb.createCellStyle();
                Font font1 = wb.createFont();
                font1.setFontName("Franklin Gothic Book");
                font1.setFontHeightInPoints((short) 10);
                font1.setBold(true);
                Font font2 = wb.createFont();
                font2.setFontName("Franklin Gothic Book");
                font2.setFontHeightInPoints((short) 8);
                font2.setBold(true);
                style1.setFont(font1);
                style1.setWrapText(true);
                style1.setAlignment(HorizontalAlignment.CENTER);
                style1.setVerticalAlignment(VerticalAlignment.CENTER);
                CellStyle style2 = wb.createCellStyle();
                style2.cloneStyleFrom(style1);
                style2.setBorderTop(BorderStyle.THIN);
                style2.setBorderBottom(BorderStyle.THIN);
                style2.setBorderLeft(BorderStyle.THIN);
                style2.setBorderRight(BorderStyle.THIN);
                CellStyle style3 = wb.createCellStyle();
                style3.cloneStyleFrom(style2);
                style3.setFont(font2);
                CellStyle style4 = wb.createCellStyle();
                style4.cloneStyleFrom(style3);
                style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style4.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                CellRangeAddress cellMerge1 = new CellRangeAddress(0, 0, 0, 12);
                CellRangeAddress cellMerge2 = new CellRangeAddress(1, 1, 0, 12);
                int sheetIndex = 0;
                for (var entry : reportsByCustomersAndProg.entrySet()) {
                    Sheet sheet = wb.createSheet(entry.getValue().get(0).getCustomerName() + " " + entry.getValue().get(0).getProgram());
                    sheet.getPrintSetup().setPaperSize((short) 9);
                    sheet.getPrintSetup().setLandscape(true);
                    sheet.getPrintSetup().setFitWidth((short) 1);
                    sheet.getPrintSetup().setFitHeight((short) 1);
                    sheet.setAutobreaks(true);
                    sheet.addMergedRegion(cellMerge1);
                    sheet.addMergedRegion(cellMerge2);
                    sheet.createRow(0).createCell(0).setCellValue("Отчет");
                    sheet.getRow(0).getCell(0).setCellStyle(style1);
                    sheet.createRow(1).createCell(0).setCellValue("\n" +
                            "по запорной арматуре Ду 300 и выше для нефтепроводов и систем пожаротушения, прошедших технический надзор на \n" +
                            "АО \"Пензтяжпромарматура\" и отгруженных в адрес  " + entry.getValue().get(0).getCustomerFullName() + " за " + start + " - " + end + " \nпрограмма " + entry.getValue().get(0).getProgram());
                    sheet.getRow(1).getCell(0).setCellStyle(style1);
                    sheet.getRow(1).setHeight((short) 1400);
                    int headIndex = 0;
                    sheet.createRow(2).createCell(headIndex).setCellValue("");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(35));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Номер транспортного средства");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(110));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Дата погрузки");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(78));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Станция назначения, Грузополучатель");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(157));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Тип продукции");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(86));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Наименование изделий (элементов) продукции");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(158));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Номер Спецификации");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style3);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(91));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Идентификационный номер (PID)");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style3);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(71));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Монтажная маркировка (номер заводского заказа) условно");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(93));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("ТУ, ГОСТ");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(64));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("№ паспорта на задвижку");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(78));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Кол-во, шт.");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(56));
                    headIndex++;
                    sheet.getRow(2).createCell(headIndex).setCellValue("Общий вес (кг)");
                    sheet.getRow(2).getCell(headIndex).setCellStyle(style2);
                    sheet.setColumnWidth(headIndex, PixelUtil.pixel2WidthUnits(62));
                    sheet.createRow(3);
                    for (int i = 0; i < 13; i++) {
                        sheet.getRow(3).createCell(i).setCellValue(i+1);
                        sheet.getRow(3).getCell(i).setCellStyle(style2);
                    }
                    int rowIndex = 4;
                    for (FOMReport row: entry.getValue()) {
                        int colIndex = 0;
                        sheet.createRow(rowIndex).createCell(colIndex).setCellValue(rowIndex-3);
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getAutoNumber());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(formatter.format(row.getShippingDate()));
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getConsignee());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getProductType());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getProductDescription());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getSpecificationNumber());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getPidNumber());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getDesignationNumber());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getStd());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getCertificateNumber());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getAmount());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        colIndex++;
                        sheet.getRow(rowIndex).createCell(colIndex).setCellValue(row.getWeight());
                        sheet.getRow(rowIndex).getCell(colIndex).setCellStyle(style3);
                        if (newPer != null) {
                            if (row.getShippingDate().before(newPer)) {
                                for (int i = 0; i < 13; i++) {
                                    sheet.getRow(rowIndex).getCell(i).setCellStyle(style4);
                                }
                            }
                        }
                        rowIndex++;
                    }
                    sheet.createRow(rowIndex).createCell(10).setCellValue("Итого");
                    sheet.getRow(rowIndex).getCell(10).setCellStyle(style2);
                    sheet.getRow(rowIndex).createCell(11).setCellFormula("SUM(" + "L5" + ":" + "L" + (rowIndex) + ")");
                    sheet.getRow(rowIndex).getCell(11).setCellStyle(style3);
                    sheet.getRow(rowIndex).createCell(12).setCellFormula("SUM(" + "M5" + ":" + "M" + (rowIndex) + ")");
                    sheet.getRow(rowIndex).getCell(12).setCellStyle(style3);
                    wb.setPrintArea(
                            sheetIndex, //sheet index
                            0, //start column
                            12, //end column
                            0, //start row
                            rowIndex  //end row
                    );
                    sheetIndex++;
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                wb.write(outputStream);
                ByteArrayInputStream stream = new ByteArrayInputStream(outputStream.toByteArray());

                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=FOM.xlsx");
                IOUtils.copy(stream, response.getOutputStream());
            }
        }
    }

    static public class PixelUtil {

        public static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
        public static final short EXCEL_ROW_HEIGHT_FACTOR = 20;
        public static final int UNIT_OFFSET_LENGTH = 7;
        public static final int[] UNIT_OFFSET_MAP = new int[] { 0, 36, 73, 109, 146, 182, 219 };

        public static short pixel2WidthUnits(int pxs) {
            short widthUnits = (short) (EXCEL_COLUMN_WIDTH_FACTOR * (pxs / UNIT_OFFSET_LENGTH));
            widthUnits += UNIT_OFFSET_MAP[(pxs % UNIT_OFFSET_LENGTH)];
            return widthUnits;
        }

//        public static int widthUnits2Pixel(short widthUnits) {
//            int pixels = (widthUnits / EXCEL_COLUMN_WIDTH_FACTOR) * UNIT_OFFSET_LENGTH;
//            int offsetWidthUnits = widthUnits % EXCEL_COLUMN_WIDTH_FACTOR;
//            pixels += Math.floor((float) offsetWidthUnits / ((float) EXCEL_COLUMN_WIDTH_FACTOR / UNIT_OFFSET_LENGTH));
//            return pixels;
//        }
//
//        public static int heightUnits2Pixel(short heightUnits) {
//            int pixels = (heightUnits / EXCEL_ROW_HEIGHT_FACTOR);
//            int offsetWidthUnits = heightUnits % EXCEL_ROW_HEIGHT_FACTOR;
//            pixels += Math.floor((float) offsetWidthUnits / ((float) EXCEL_ROW_HEIGHT_FACTOR / UNIT_OFFSET_LENGTH));
//            return pixels;
//        }
    }
}
