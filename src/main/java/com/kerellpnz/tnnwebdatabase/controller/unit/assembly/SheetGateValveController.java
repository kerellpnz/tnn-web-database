package com.kerellpnz.tnnwebdatabase.controller.unit.assembly;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.assemblyunit.GateValveDAO;
import com.kerellpnz.tnnwebdatabase.dao.detail.*;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.PIDDAO;
import com.kerellpnz.tnnwebdatabase.dao.periodical.WeldingProcedureDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.CoatingJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.WeldingProcedureJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.CoatingTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.SheetGateValveTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.*;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.*;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.AbovegroundCoating;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.BaseAnticorrosiveCoating;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.Undercoat;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.UndergroundCoating;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.WeldingProcedure;
import com.kerellpnz.tnnwebdatabase.exception.EntityNotFoundException;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/entity/SheetGateValves")
public class SheetGateValveController extends BaseEntityController {

    private final GateValveDAO entityDAO;
    private final BaseEntityDAO<SheetGateValveJournal> baseJournalDAO;
    private final BaseEntityDAO<CoatingJournal> coatingJournalDAO;
    private final WeldingProcedureDAO weldingProcedureDAO;
    private final BaseEntityDAO<BaseValveWithShearPin> withShearPinDAO;
    private final BaseEntityDAO<BaseValveWithScrewNut> withScrewNutDAO;
    private final BaseEntityDAO<BaseValveWithScrewStud> withScrewStudDAO;
    private final BaseEntityDAO<BaseValveWithSpring> withSpringDAO;
    private final BaseTCPDAO<SheetGateValveTCP> TCPDAO;
    private final BaseTCPDAO<CoatingTCP> coatingTCPDAO;
    private final PIDDAO pidDAO;
    private final BaseEntityDAO<Gate> gateDAO;
    private final SaddleDAO saddleDAO;
    private final NozzleDAO nozzleDAO;
    private final BaseEntityDAO<SheetGateValveCase> sheetGateValveCaseDAO;
    private final BaseEntityDAO<SheetGateValveCover> sheetGateValveCoverDAO;
    private final BaseEntityDAO<MainFlangeSealing> mainFlangeSealingDAO;
    private final BaseEntityDAO<BaseAnticorrosiveCoating> baseAnticorrosiveCoatingDAO;
    private final ShearPinDAO shearPinDAO;
    private final ScrewNutDAO  screwNutDAO;
    private final ScrewStudDAO screwStudDAO;
    private final SpringDAO springDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> drawings;
    private List<CoatingTCP> entityTCPs;
    private List<SheetGateValveTCP> valveTCPs;
    private List<PID> pids;
    private List<Gate> gates;
    private List<Saddle> saddles;
    private List<Nozzle> nozzles;
    private List<SheetGateValveCase> cases;
    private List<SheetGateValveCover> covers;
    private List<MainFlangeSealing> mainFlangeSeals;
    private List<BaseAnticorrosiveCoating> baseAnticorrosiveCoatings;
    private List<ShearPin> shearPins;
    private List<ScrewNut> screwNuts;
    private List<ScrewStud> screwStuds;
    private List<Spring> springs;
    private List<SheetGateValveTCP> inputControlTCP;
    private List<SheetGateValveTCP> weldTCP;
    private List<SheetGateValveTCP> mechanicalTCP;
    private List<SheetGateValveTCP> documentTCP;
    private List<SheetGateValveTCP> assemblyTCP;

    private List<SheetGateValve> entities;

    @Value("/resources/files/BaseBlankPassport.xlsx")
    private Resource baseBlankPassport;
    private SheetGateValve tempValve;
    private String[] designationSplit;
    private String[] specificationSplit;

    @Autowired
    public SheetGateValveController(HttpSession session,
                                    JournalNumberDAO journalNumberDAO,
                                    GateValveDAO entityDAO,
                                    BaseEntityDAO<SheetGateValveJournal> baseJournalDAO,
                                    BaseEntityDAO<CoatingJournal> coatingJournalDAO,
                                    WeldingProcedureDAO weldingProcedureDAO,
                                    BaseEntityDAO<BaseValveWithShearPin> withShearPinDAO,
                                    BaseEntityDAO<BaseValveWithScrewNut> withScrewNutDAO,
                                    BaseEntityDAO<BaseValveWithScrewStud> withScrewStudDAO,
                                    BaseEntityDAO<BaseValveWithSpring> withSpringDAO,
                                    BaseTCPDAO<SheetGateValveTCP> TCPDAO,
                                    BaseTCPDAO<CoatingTCP> coatingTCPDAO,
                                    PIDDAO pidDAO,
                                    BaseEntityDAO<Gate> gateDAO,
                                    SaddleDAO saddleDAO,
                                    NozzleDAO nozzleDAO,
                                    ShearPinDAO shearPinDAO,
                                    ScrewNutDAO screwNutDAO,
                                    ScrewStudDAO screwStudDAO,
                                    SpringDAO springDAO,
                                    BaseEntityDAO<SheetGateValveCase> sheetGateValveCaseDAO,
                                    BaseEntityDAO<SheetGateValveCover> sheetGateValveCoverDAO,
                                    BaseEntityDAO<MainFlangeSealing> mainFlangeSealingDAO,
                                    BaseEntityDAO<BaseAnticorrosiveCoating> baseAnticorrosiveCoatingDAO,
                                    InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.coatingJournalDAO = coatingJournalDAO;
        this.weldingProcedureDAO = weldingProcedureDAO;
        this.withShearPinDAO = withShearPinDAO;
        this.withScrewNutDAO = withScrewNutDAO;
        this.withScrewStudDAO = withScrewStudDAO;
        this.withSpringDAO = withSpringDAO;
        this.TCPDAO = TCPDAO;
        this.coatingTCPDAO = coatingTCPDAO;
        this.pidDAO = pidDAO;
        this.gateDAO = gateDAO;
        this.saddleDAO = saddleDAO;
        this.nozzleDAO = nozzleDAO;
        this.sheetGateValveCaseDAO = sheetGateValveCaseDAO;
        this.sheetGateValveCoverDAO = sheetGateValveCoverDAO;
        this.mainFlangeSealingDAO = mainFlangeSealingDAO;
        this.baseAnticorrosiveCoatingDAO = baseAnticorrosiveCoatingDAO;
        this.shearPinDAO = shearPinDAO;
        this.screwStudDAO = screwStudDAO;
        this.screwNutDAO = screwNutDAO;
        this.springDAO = springDAO;
        this.inspectorDAO = inspectorDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            switch (parameter) {
                case "badValveCover" -> mv.addObject("message", "ВНИМАНИЕ: Выбранная крышка имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к ЗШ.");
                case "badValveCase" -> mv.addObject("message", "ВНИМАНИЕ: Выбранный корпус имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к ЗШ.");
                case "badGate" -> mv.addObject("message", "ВНИМАНИЕ: Шибер имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к ЗШ.");
                case "badNozzle" -> mv.addObject("message", "ВНИМАНИЕ: Катушка имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к ЗШ.");
                case "badSaddle" -> mv.addObject("message", "ВНИМАНИЕ: Обойма имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к ЗШ.");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новая ЗШ");
        SheetGateValve entity = new SheetGateValve();
        valveTCPs = TCPDAO.getAll(SheetGateValveTCP.class);
        entityTCPs = coatingTCPDAO.getAll(CoatingTCP.class);
        List<SheetGateValveJournal> valveJournals = new ArrayList<>(20);
        List<CoatingJournal> entityJournals = new ArrayList<>();
        for (SheetGateValveTCP tcp : valveTCPs) {
            SheetGateValveJournal entityJournal = new SheetGateValveJournal(tcp);
            valveJournals.add(entityJournal);
        }
        for (CoatingTCP tcp : entityTCPs) {
            CoatingJournal entityJournal = new CoatingJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        entity.setValveJournals(valveJournals);
        TCPInitialize(valveTCPs);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Редактирование ЗШ");
        SheetGateValve entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (SheetGateValveJournal entityJournal: entity.getValveJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        for (CoatingJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        if (entity.getPid() != null)
            entity.setSpecNumber(entity.getPid().getSpecification().getNumber());
        valveTCPs = TCPDAO.getAll(SheetGateValveTCP.class);
        entityTCPs = coatingTCPDAO.getAll(CoatingTCP.class);
        TCPInitialize(valveTCPs);
        return modelInitializeObject(mv, entity);
    }

    private void TCPInitialize(List<SheetGateValveTCP> valveTCPs) {
        inputControlTCP = new ArrayList<>();
        weldTCP = new ArrayList<>();
        mechanicalTCP = new ArrayList<>();
        documentTCP = new ArrayList<>();
        assemblyTCP = new ArrayList<>();
        if (valveTCPs != null) {
            for(SheetGateValveTCP tcp : valveTCPs) {
                switch (tcp.getOperationType()) {
                    case "Входной контроль" -> inputControlTCP.add(tcp);
                    case "Сварка" -> weldTCP.add(tcp);
                    case "Обработка" -> mechanicalTCP.add(tcp);
                    case "Документация" -> documentTCP.add(tcp);
                    case "Сборка" -> assemblyTCP.add(tcp);
                }
            }
        }
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ЗШ");
        mv.addObject("userClickSheetGateValves", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/assembly/gate-valve-editView");
        mv.addObject("title", title);
        mv.addObject("userClickEditSheetGateValves", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, SheetGateValve entity) {
        drawings = entityDAO.getDistinctDrawing();
        gates = gateDAO.getAll(Gate.class);
        saddles = saddleDAO.getUnusedSaddles();
        nozzles = nozzleDAO.getUnusedNozzles();
        cases = sheetGateValveCaseDAO.getAll(SheetGateValveCase.class);
        covers = sheetGateValveCoverDAO.getAll(SheetGateValveCover.class);
        mainFlangeSeals = mainFlangeSealingDAO.getAll(MainFlangeSealing.class);
        baseAnticorrosiveCoatings = baseAnticorrosiveCoatingDAO.getAll(BaseAnticorrosiveCoating.class);
        shearPins = shearPinDAO.getAll(ShearPin.class);
        screwNuts = screwNutDAO.getAll(ScrewNut.class);
        screwStuds = screwStudDAO.getAll(ScrewStud.class);
        springs = springDAO.getAll(Spring.class);
        pids = pidDAO.getAll(PID.class);
        mv.addObject("gates", gates);
        mv.addObject("saddles", saddles);
        mv.addObject("nozzles", nozzles);
        mv.addObject("cases", cases);
        mv.addObject("covers", covers);
        mv.addObject("mainFlangeSeals", mainFlangeSeals);
        mv.addObject("baseAnticorrosiveCoatings", baseAnticorrosiveCoatings);
        mv.addObject("shearPins", shearPins);
        mv.addObject("screwNuts", screwNuts);
        mv.addObject("screwStuds", screwStuds);
        mv.addObject("springs", springs);
        mv.addObject("drawings", drawings);
        mv.addObject("pids", pids);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        mv.addObject("inputControlTCP", inputControlTCP);
        mv.addObject("weldTCP", weldTCP);
        mv.addObject("mechanicalTCP", mechanicalTCP);
        mv.addObject("documentTCP", documentTCP);
        mv.addObject("assemblyTCP", assemblyTCP);
        mv.addObject("entityTCPs", entityTCPs);

        if (entity.getEntityJournals() != null) {
            for(SheetGateValveJournal journal : entity.getValveJournals()) {
                String operationType = TCPDAO.getOperationTypeById(SheetGateValveTCP.class, journal.getPointId());
                switch (operationType) {
                    case "Входной контроль" -> entity.getInputControlJournals().add(journal);
                    case "Сварка" -> entity.getWeldJournals().add(journal);
                    case "Обработка" -> entity.getMechanicalJournals().add(journal);
                    case "Документация" -> entity.getDocumentJournals().add(journal);
                    case "Сборка" -> entity.getAssemblyJournals().add(journal);
                }
            }
            if (entity.getInputControlJournals() != null) entity.getInputControlJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
            if (entity.getWeldJournals() != null) entity.getWeldJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
            if (entity.getMechanicalJournals() != null) entity.getMechanicalJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
            if (entity.getDocumentJournals() != null) entity.getDocumentJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
            if (entity.getAssemblyJournals() != null) entity.getAssemblyJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
        }
        return mv;
    }

    @GetMapping("/download/passportBlank.xlsx")
    public void passportBlank(HttpServletResponse response) throws IOException {
        Workbook wb = new XSSFWorkbook(baseBlankPassport.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        CellStyle rowHead = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Franklin Gothic Book");
        font.setFontHeightInPoints((short) 12);
        Font rowHeadFont = wb.createFont();
        rowHeadFont.setFontName("Franklin Gothic Book");
        rowHeadFont.setFontHeightInPoints((short) 12);
        rowHeadFont.setBold(true);
        style.setFont(font);
        rowHead.setFont(rowHeadFont);
        sheet.getRow(1).createCell(1).setCellValue(designationSplit[1]);
        sheet.getRow(1).getCell(1).setCellStyle(style);
        sheet.getRow(1).createCell(3).setCellValue(tempValve.getPid().getNumber());
        sheet.getRow(1).getCell(3).setCellStyle(style);
        sheet.getRow(1).createCell(9).setCellValue(specificationSplit[2]);
        sheet.getRow(1).getCell(9).setCellStyle(style);
        sheet.getRow(1).createCell(12).setCellValue("О" + tempValve.getNumber());
        sheet.getRow(1).getCell(12).setCellStyle(style);
        sheet.getRow(37).createCell(1).setCellValue(designationSplit[1]);
        sheet.getRow(37).getCell(1).setCellStyle(style);
        sheet.getRow(37).createCell(2).setCellValue(designationSplit[2]);
        sheet.getRow(37).getCell(2).setCellStyle(style);
        sheet.getRow(37).createCell(4).setCellValue(designationSplit[3]);
        sheet.getRow(37).getCell(4).setCellStyle(style);
        sheet.getRow(37).createCell(5).setCellValue(designationSplit[4]);
        sheet.getRow(37).getCell(5).setCellStyle(style);
        sheet.getRow(37).createCell(6).setCellValue(designationSplit[5]);
        sheet.getRow(37).getCell(6).setCellStyle(style);
        sheet.getRow(37).createCell(7).setCellValue(designationSplit[6]);
        sheet.getRow(37).getCell(7).setCellStyle(style);
        sheet.getRow(37).createCell(8).setCellValue(designationSplit[7]);
        sheet.getRow(37).getCell(8).setCellStyle(style);
        sheet.getRow(37).createCell(10).setCellValue("О" + tempValve.getNumber());
        sheet.getRow(37).getCell(10).setCellStyle(style);
        sheet.getRow(37).createCell(13).setCellValue(tempValve.getPid().getNumber());
        sheet.getRow(37).getCell(13).setCellStyle(style);

        int recordIndex = 41;

        sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover())
                .map(SheetGateValveCover::getNumber).orElse(""));
        sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
        recordIndex++;
        if (tempValve.getValveCover() != null) {
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCaseBottom())
                    .map(CaseBottom::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCaseBottom())
                    .map(CaseBottom::getMelt).orElse(""));
            sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
            recordIndex++;
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover().getFlange())
                    .map(Flange::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCover().getFlange())
                    .map(Flange::getMelt).orElse(""));
            sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
            recordIndex++;
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCoverSleeve())
                    .map(CoverSleeve::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCoverSleeve())
                    .map(CoverSleeve::getZk).orElse(""));
            sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCoverSleeve())
                    .map(CoverSleeve::getMelt).orElse(""));
            sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
            recordIndex++;
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCoverSleeve008())
                    .map(CoverSleeve008::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCoverSleeve008())
                    .map(CoverSleeve008::getZk).orElse(""));
            sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCover().getCoverSleeve008())
                    .map(CoverSleeve008::getMelt).orElse(""));
            sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
            recordIndex++;
        }
        sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCase())
                .map(SheetGateValveCase::getNumber).orElse(""));
        sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
        recordIndex++;
        sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCase())
                .map(SheetGateValveCase::getNumber).orElse(""));
        sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCase())
                .map(SheetGateValveCase::getMelt).orElse(""));
        sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
        recordIndex++;
        if (tempValve.getValveCase() != null) {
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCase().getCaseBottom())
                    .map(CaseBottom::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCase().getCaseBottom())
                    .map(CaseBottom::getMelt).orElse(""));
            sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
            recordIndex++;
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCase().getFlange())
                    .map(Flange::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCase().getFlange())
                    .map(Flange::getMelt).orElse(""));
            sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
            recordIndex++;
            if (tempValve.getValveCase().getRings() != null) {
                for (int i = 0; i < tempValve.getValveCase().getRings().size(); i++) {
                    sheet.createRow(recordIndex).createCell(0).setCellValue("КОЛЬЦО №");
                    sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                    sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCase().getRings().get(i))
                            .map(Ring::getNumber).orElse(""));
                    sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                    sheet.getRow(recordIndex).createCell(7).setCellValue("ЗК");
                    sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                    sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getValveCase().getRings().get(i))
                            .map(Ring::getZk).orElse(""));
                    sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                    sheet.getRow(recordIndex).createCell(12).setCellValue("пл.");
                    sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                    sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCase().getRings().get(i))
                            .map(Ring::getMelt).orElse(""));
                    sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
                    recordIndex++;
                }
            }
        }
        sheet.createRow(recordIndex).createCell(0).setCellValue("ШИБЕР №");
        sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
        sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getGate())
                .map(Gate::getNumber).orElse(""));
        sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(7).setCellValue("ЗК");
        sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getGate())
                .map(Gate::getZk).orElse(""));
        sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(12).setCellValue("пл.");
        sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getGate())
                .map(Gate::getMelt).orElse(""));
        sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
        recordIndex++;
        if (tempValve.getValveCover() != null) {
            sheet.createRow(recordIndex).createCell(0).setCellValue("ШПИНДЕЛЬ №");
            sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover().getSpindle())
                    .map(Spindle::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(7).setCellValue("ЗК");
            sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getValveCover().getSpindle())
                    .map(Spindle::getZk).orElse(""));
            sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(12).setCellValue("пл.");
            sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
            sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCover().getSpindle())
                    .map(Spindle::getMelt).orElse(""));
            sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
            recordIndex++;
            sheet.createRow(recordIndex).createCell(0).setCellValue("СТОЙКА №");
            sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
            sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover().getColumn())
                    .map(Column::getNumber).orElse(""));
            sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
            recordIndex++;
        }
        if (tempValve.getSaddles() != null) {
            for (int i = 0; i < tempValve.getSaddles().size(); i++) {
                sheet.createRow(recordIndex).createCell(0).setCellValue("ОБОЙМА №");
                sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getSaddles().get(i))
                        .map(Saddle::getNumber).orElse(""));
                sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(7).setCellValue("ЗК");
                sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getSaddles().get(i))
                        .map(Saddle::getZk).orElse(""));
                sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(12).setCellValue("пл.");
                sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getSaddles().get(i))
                        .map(Saddle::getMelt).orElse(""));
                sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
                recordIndex++;
                if (tempValve.getSaddles().get(i).getFrontalSaddleSeals() != null) {
                    for (int k = 0; k < tempValve.getSaddles().get(i).getFrontalSaddleSeals().size(); k++) {
                        sheet.createRow(recordIndex).createCell(0).setCellValue("Уплотнение №" + (k+1));
                        sheet.getRow(recordIndex).getCell(0).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getSaddles().get(i).getFrontalSaddleSeals().get(k))
                                .map(FrontalSaddleSealing::getCertificate).orElse(""));
                        sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(7).setCellValue("парт.");
                        sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getSaddles().get(i).getFrontalSaddleSeals().get(k))
                                .map(FrontalSaddleSealing::getBatch).orElse(""));
                        sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                        sheet.getRow(recordIndex).createCell(12).setCellValue(Optional.ofNullable(tempValve.getSaddles().get(i).getFrontalSaddleSeals().get(k))
                                .map(FrontalSaddleSealing::getDrawing).orElse(""));
                        sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                        recordIndex++;
                    }
                }
            }
        }
        if (tempValve.getNozzles() != null) {
            for (int i = 0; i < tempValve.getNozzles().size(); i++) {
                sheet.createRow(recordIndex).createCell(0).setCellValue("КАТУШКА №");
                sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getNozzles().get(i))
                        .map(Nozzle::getNumber).orElse(""));
                sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(7).setCellValue("ЗК");
                sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getNozzles().get(i))
                        .map(Nozzle::getZk).orElse(""));
                sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(12).setCellValue("пл.");
                sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getNozzles().get(i))
                        .map(Nozzle::getMelt).orElse(""));
                sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
                sheet.getRow(recordIndex + 1).createCell(4).setCellValue(Optional.ofNullable(tempValve.getNozzles().get(i))
                        .map(Nozzle::getGrooving).orElse(""));
                sheet.getRow(recordIndex + 1).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex + 1).createCell(8).setCellValue(Optional.ofNullable(tempValve.getNozzles().get(i))
                        .map(Nozzle::getTensileStrength).orElse(""));
                sheet.getRow(recordIndex + 1).getCell(8).setCellStyle(style);
                recordIndex += 2;
            }
        }
        sheet.createRow(recordIndex).createCell(0).setCellValue("КРАН ШАРОВЫЙ");
        sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
        sheet.getRow(recordIndex).createCell(7).setCellValue("Др");
        sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getValveCover())
                .map(SheetGateValveCover::getBallValveDrainage).orElse(""));
        sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(12).setCellValue("Сп");
        sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getValveCover())
                .map(SheetGateValveCover::getBallValveDraining).orElse(""));
        sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
        recordIndex++;
        if (tempValve.getBaseValveWithShearPins() != null) {
            for (int i = 0; i < tempValve.getBaseValveWithShearPins().size(); i++) {
                sheet.createRow(recordIndex).createCell(0).setCellValue("ШТИФТ №");
                sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                sheet.getRow(recordIndex).createCell(4).setCellValue("ЗК");
                sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(5).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithShearPins().get(i).getShearPin())
                        .map(ShearPin::getNumber).orElse(""));
                sheet.getRow(recordIndex).getCell(5).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(7).setCellValue("Ф");
                sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithShearPins().get(i).getShearPin())
                        .map(ShearPin::getDiameter).orElse(""));
                sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(9).setCellValue("тяга:");
                sheet.getRow(recordIndex).getCell(9).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(10).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithShearPins().get(i).getShearPin())
                        .map(ShearPin::getPull).orElse(""));
                sheet.getRow(recordIndex).getCell(10).setCellStyle(style);
                recordIndex++;
            }
        }
        if (tempValve.getBaseValveWithScrewStuds() != null) {
            for (int i = 0; i < tempValve.getBaseValveWithScrewStuds().size(); i++) {
                sheet.createRow(recordIndex).createCell(0).setCellValue("ШПИЛЬКА №");
                sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithScrewStuds().get(i).getScrewStud())
                        .map(ScrewStud::getCertificate).orElse(""));
                sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(7).setCellValue("парт.");
                sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithScrewStuds().get(i).getScrewStud())
                        .map(ScrewStud::getNumber).orElse(""));
                sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(12).setCellValue("пл.");
                sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithScrewStuds().get(i).getScrewStud())
                        .map(ScrewStud::getMelt).orElse(""));
                sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
                recordIndex++;
            }
        }
        if (tempValve.getBaseValveWithScrewNuts() != null) {
            for (int i = 0; i < tempValve.getBaseValveWithScrewNuts().size(); i++) {
                sheet.createRow(recordIndex).createCell(0).setCellValue("ГАЙКА №");
                sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithScrewNuts().get(i).getScrewNut())
                        .map(ScrewNut::getCertificate).orElse(""));
                sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(7).setCellValue("парт.");
                sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithScrewNuts().get(i).getScrewNut())
                        .map(ScrewNut::getNumber).orElse(""));
                sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(12).setCellValue("пл.");
                sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(13).setCellValue(Optional.ofNullable(tempValve.getBaseValveWithScrewNuts().get(i).getScrewNut())
                        .map(ScrewNut::getMelt).orElse(""));
                sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
                recordIndex++;
            }
        }
        if (tempValve.getMainFlangeSeals() != null) {
            for (int i = 0; i < tempValve.getMainFlangeSeals().size(); i++) {
                sheet.createRow(recordIndex).createCell(0).setCellValue("УПЛОТНИТЕЛЬ:");
                sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getMainFlangeSeals().get(i))
                        .map(MainFlangeSealing::getCertificate).orElse(""));
                sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(7).setCellValue("парт.");
                sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getMainFlangeSeals().get(i))
                        .map(MainFlangeSealing::getBatch).orElse(""));
                sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(12).setCellValue(Optional.ofNullable(tempValve.getMainFlangeSeals().get(i))
                        .map(MainFlangeSealing::getDrawing).orElse(""));
                sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                recordIndex++;
            }
        }
        if (tempValve.getValveCover() != null) {
            if (tempValve.getValveCover().getFrontalSaddleSeals() != null) {
                for (int i = 0; i < tempValve.getValveCover().getFrontalSaddleSeals().size(); i++) {
                    sheet.createRow(recordIndex).createCell(0).setCellValue("УПЛОТНИТЕЛЬ:");
                    sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                    sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getValveCover().getFrontalSaddleSeals().get(i))
                            .map(FrontalSaddleSealing::getCertificate).orElse(""));
                    sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                    sheet.getRow(recordIndex).createCell(7).setCellValue("парт.");
                    sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                    sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getValveCover().getFrontalSaddleSeals().get(i))
                            .map(FrontalSaddleSealing::getBatch).orElse(""));
                    sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                    sheet.getRow(recordIndex).createCell(12).setCellValue(Optional.ofNullable(tempValve.getValveCover().getFrontalSaddleSeals().get(i))
                            .map(FrontalSaddleSealing::getDrawing).orElse(""));
                    sheet.getRow(recordIndex).getCell(12).setCellStyle(style);
                    recordIndex++;
                }
            }
        }
        if (tempValve.getBaseAnticorrosiveCoatings() != null) {
            for (int i = 0; i < tempValve.getBaseAnticorrosiveCoatings().size(); i++) {
                if (tempValve.getBaseAnticorrosiveCoatings().get(i) instanceof UndergroundCoating) {
                    sheet.createRow(recordIndex).createCell(0).setCellValue("ПОДЗЕМНОЕ АКП:");
                    sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                }
                else {
                    sheet.createRow(recordIndex).createCell(0).setCellValue("НАДЗЕМНОЕ АКП:");
                    sheet.getRow(recordIndex).getCell(0).setCellStyle(rowHead);
                }
                sheet.getRow(recordIndex).createCell(4).setCellValue(Optional.ofNullable(tempValve.getBaseAnticorrosiveCoatings().get(i))
                        .map(BaseAnticorrosiveCoating::getCertificate).orElse(""));
                sheet.getRow(recordIndex).getCell(4).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(7).setCellValue("парт.");
                sheet.getRow(recordIndex).getCell(7).setCellStyle(style);
                sheet.getRow(recordIndex).createCell(8).setCellValue(Optional.ofNullable(tempValve.getBaseAnticorrosiveCoatings().get(i))
                        .map(BaseAnticorrosiveCoating::getBatch).orElse(""));
                sheet.getRow(recordIndex).getCell(8).setCellStyle(style);
                recordIndex++;
            }
        }
        sheet.createRow(recordIndex).createCell(6).setCellValue("ИСПЫТАНИЯ:");
        sheet.getRow(recordIndex).getCell(6).setCellStyle(rowHead);
        recordIndex++;
        sheet.createRow(recordIndex).createCell(0).setCellValue("СБОРКА ЗШ:");
        sheet.getRow(recordIndex).getCell(0).setCellStyle(style);
        sheet.getRow(recordIndex).createCell(9).setCellValue("ПСИ:");
        sheet.getRow(recordIndex).getCell(9).setCellStyle(style);
        sheet.createRow(recordIndex + 2).createCell(9).setCellValue("ВИК после ПСИ:");
        sheet.getRow(recordIndex + 2).getCell(9).setCellStyle(style);
        sheet.createRow(recordIndex + 4).createCell(0).setCellValue("АКП:");
        sheet.getRow(recordIndex + 4).getCell(0).setCellStyle(style);
        sheet.createRow(recordIndex + 5).createCell(0).setCellValue("Фото дроби:");
        sheet.getRow(recordIndex + 5).getCell(0).setCellStyle(style);
        sheet.createRow(recordIndex + 3).createCell(9).setCellValue("Адгезия:");
        sheet.getRow(recordIndex + 3).getCell(9).setCellStyle(style);
        sheet.createRow(recordIndex + 1).createCell(0).setCellValue("Мкр");
        sheet.getRow(recordIndex + 1).getCell(0).setCellStyle(style);
        sheet.getRow(recordIndex + 2).createCell(0).setCellValue("Ход шибера");
        sheet.getRow(recordIndex + 2).getCell(0).setCellStyle(style);
        sheet.getRow(recordIndex + 3).createCell(0).setCellValue("Положение шибера");
        sheet.getRow(recordIndex + 3).getCell(0).setCellStyle(style);
        sheet.getRow(recordIndex + 1).createCell(9).setCellValue("Авт.Сброс");
        sheet.getRow(recordIndex + 1).getCell(9).setCellStyle(style);
        sheet.createRow(recordIndex + 6).createCell(0).setCellValue("Фото промывки корпуса");
        sheet.getRow(recordIndex + 6).getCell(0).setCellStyle(style);
        sheet.getRow(recordIndex + 4).createCell(9).setCellValue("ЗИП");
        sheet.getRow(recordIndex + 4).getCell(9).setCellStyle(style);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (tempValve.getValveJournals() != null) {
            for (SheetGateValveJournal journal : tempValve.getValveJournals()) {
                if (journal.getPointId() == 113 && journal.getDate() != null) {
                    sheet.getRow(recordIndex).createCell(5).setCellValue(dateFormat.format(journal.getDate()));
                    sheet.getRow(recordIndex).getCell(5).setCellStyle(style);
                }
                if (journal.getPointId() == 111 && journal.getDate() != null) {
                    sheet.getRow(recordIndex).createCell(13).setCellValue(dateFormat.format(journal.getDate()));
                    sheet.getRow(recordIndex).getCell(13).setCellStyle(style);
                }
                if (journal.getPointId() == 112 && journal.getDate() != null) {
                    sheet.getRow(recordIndex + 2).createCell(13).setCellValue(dateFormat.format(journal.getDate()));
                    sheet.getRow(recordIndex + 2).getCell(13).setCellStyle(style);
                }
            }
        }
        if (tempValve.getEntityJournals() != null) {
            for (CoatingJournal journal : tempValve.getEntityJournals()) {
                if (journal.getPointId() == 6 && journal.getDate() != null) {
                    sheet.getRow(recordIndex + 4).createCell(5).setCellValue(dateFormat.format(journal.getDate()));
                    sheet.getRow(recordIndex + 4).getCell(5).setCellStyle(style);
                }
                if (journal.getPointId() == 4 && journal.getDate() != null) {
                    sheet.getRow(recordIndex + 5).createCell(5).setCellValue(dateFormat.format(journal.getDate()));
                    sheet.getRow(recordIndex + 5).getCell(5).setCellStyle(style);
                }
                if (journal.getPointId() == 7 && journal.getDate() != null) {
                    sheet.getRow(recordIndex + 3).createCell(13).setCellValue(dateFormat.format(journal.getDate()));
                    sheet.getRow(recordIndex + 3).getCell(13).setCellStyle(style);
                }
            }
        }
        sheet.getRow(recordIndex + 1).createCell(5).setCellValue(tempValve.getMoment() + " Н*м");
        sheet.getRow(recordIndex + 1).getCell(5).setCellStyle(style);
        sheet.getRow(recordIndex + 2).createCell(5).setCellValue(tempValve.getTime() + " с");
        sheet.getRow(recordIndex + 2).getCell(5).setCellStyle(style);
        sheet.getRow(recordIndex + 3).createCell(5).setCellValue(tempValve.getGatePlace() + " мм");
        sheet.getRow(recordIndex + 3).getCell(5).setCellStyle(style);
        if (tempValve.getValveCase() != null) {
            if (tempValve.getValveCase().getDateOfWashing() != null) {
                sheet.getRow(recordIndex + 6).createCell(5).setCellValue(dateFormat.format(tempValve.getValveCase().getDateOfWashing()));
                sheet.getRow(recordIndex + 6).getCell(5).setCellStyle(style);
            }
        }
        if (tempValve.getZip() != null) {
            sheet.getRow(recordIndex + 4).createCell(13).setCellValue(dateFormat.format(tempValve.getZip()));
            sheet.getRow(recordIndex + 4).getCell(13).setCellStyle(style);
        }
        sheet.getRow(recordIndex + 1).createCell(13).setCellValue(tempValve.getAutomaticReset() + " МПа");
        sheet.getRow(recordIndex + 1).getCell(13).setCellStyle(style);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wb.write(outputStream);
        ByteArrayInputStream stream = new ByteArrayInputStream(outputStream.toByteArray());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=passportBlank.xlsx");
        IOUtils.copy(stream, response.getOutputStream());
    }

    @RequestMapping(value = "/action", params = "checkBeforeCreateExcel")
    public String checkBeforeCreateExcel(@ModelAttribute("entity") SheetGateValve entity,  Model model) {
        if (entity.getId() != 0)
            tempValve = entityDAO.getForCheck(entity.getId());
        if (tempValve != null) {
            if (tempValve.getPid() != null) {
                if (tempValve.getPid().getSpecification() != null) {
                    if (tempValve.getPid().getDesignation() != null && tempValve.getPid().getSpecification().getNumber() != null) {
                        designationSplit = tempValve.getPid().getDesignation().split("-");
                        specificationSplit = tempValve.getPid().getSpecification().getNumber().split("-");
                        if (designationSplit.length == 8) {
                            if (specificationSplit.length == 4) {
                                tempValve = entityDAO.getForBlank(entity.getId());
                                return "redirect:/entity/SheetGateValves/download/passportBlank.xlsx";
                            }
                            else return sendMessage("В формате номера спецификации есть ошибка. Номер должен содержать 4 параметра и разделители в виде \"-\". Пример: 00007936-ТПР-ТСИБ-2020", model);
                        }
                        else return sendMessage("В формате обозначения ЗШ есть ошибка. Обозначение должно содержать 8 параметров и разделители в виде \"-\". Пример: ЗШ-700-1,6-1,6-Св-ЭП-С0-ХЛ1", model);
                    }
                    else return sendMessage("Отсутствует обозначение ЗШ или номер спецификации!", model);
                }
                else return sendMessage("Спецификация отсутствует!", model);
            }
            else return sendMessage("PID не выбран! Выберите PID и пересохраните ЗШ!", model);
        }
        else {
            modelInitialize(model);
            return "./unit/assembly/gate-valve-editView";
        }
    }

    @PostMapping(value = "/action", params = "openPID")
    public String openPID(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getPid() != null) {
            if (entity.getId() != 0)
                return "redirect:/Specifications/showFormForUpdate/" + entity.getPid().getId()+ "/" + entity.getId() + "/valve";
            else
                return "redirect:/Specifications/showFormForUpdate/" + entity.getPid().getId();
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "openCover")
    public String openSheetGateValveCover(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getValveCover() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/SheetGateValveCovers/showFormForUpdate/" + entity.getValveCover().getId()+ "/" + entity.getId();
            else
                return "redirect:/entity/SheetGateValveCovers/showFormForUpdate/" + entity.getValveCover().getId();
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "openCase")
    public String openSheetGateValveCase(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getValveCase() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/SheetGateValveCases/showFormForUpdate/" + entity.getValveCase().getId()+ "/" + entity.getId();
            else
                return "redirect:/entity/SheetGateValveCases/showFormForUpdate/" + entity.getValveCase().getId();
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "openGate")
    public String openGate(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getGate() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/Gates/showFormForUpdate/" + entity.getGate().getId() + "/" + entity.getId();
            else
                return "redirect:/entity/Gates/showFormForUpdate/" + entity.getGate().getId();
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openNozzle")
    public String openNozzle(@RequestParam("nozzleId") int nozzleId, @RequestParam("valveId") int valveId) {
        if (valveId != 0)
            return "redirect:/entity/Nozzles/showFormForUpdate/" + nozzleId + "/" + valveId;
        else
            return "redirect:/entity/Nozzles/showFormForUpdate/" + nozzleId;
    }

    @PostMapping(value = "/action", params = "addNozzle")
    public String addNozzle(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempNozzleId() != null) {
            Nozzle nozzle = nozzleDAO.get(Nozzle.class, entity.getTempNozzleId());
            if (entity.getNozzles() != null) {
                if (entity.getNozzles().size() < 2)
                    if (entity.getNozzles().get(0).getId() != nozzle.getId())
                        entity.getNozzles().add(nozzle);
                    else
                        return sendMessage("ОШИБКА: Попытка добавить два одинаковых объекта!", model);
                else
                    return sendMessage("ОШИБКА: Невозможно привязать больше двух катушек!", model);
            }
            else {
                List<Nozzle> Nozzles = new ArrayList<>();
                Nozzles.add(nozzle);
                entity.setNozzles(Nozzles);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteNozzle")
    public String deleteNozzle (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteNozzle) {
        Nozzle nozzle = nozzleDAO.get(Nozzle.class, entity.getNozzles().get(deleteNozzle).getId());
        nozzle.setSheetGateValve(null);
        nozzleDAO.saveOrUpdate(nozzle);
        entity.getNozzles().remove(deleteNozzle);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openSaddle")
    public String openSaddle(@RequestParam("saddleId") int saddleId, @RequestParam("valveId") int valveId) {
        if (valveId != 0)
            return "redirect:/entity/Saddles/showFormForUpdate/" + saddleId + "/" + valveId;
        else
            return "redirect:/entity/Saddles/showFormForUpdate/" + saddleId;
    }

    @PostMapping(value = "/action", params = "addSaddle")
    public String addSaddle(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempSaddleId() != null) {
            Saddle saddle = saddleDAO.get(Saddle.class, entity.getTempSaddleId());
            if (entity.getSaddles() != null) {
                if (entity.getSaddles().size() < 2)
                    if (entity.getSaddles().get(0).getId() != saddle.getId())
                        entity.getSaddles().add(saddle);
                    else
                        return sendMessage("ОШИБКА: Попытка добавить два одинаковых объекта!", model);
                else
                    return sendMessage("ОШИБКА: Невозможно привязать больше двух обойм!", model);
            }
            else {
                List<Saddle> Saddles = new ArrayList<>();
                Saddles.add(saddle);
                entity.setSaddles(Saddles);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteSaddle")
    public String deleteSaddle (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteSaddle) {
        Saddle saddle = saddleDAO.get(Saddle.class, entity.getSaddles().get(deleteSaddle).getId());
        saddle.setSheetGateValve(null);
        saddleDAO.saveOrUpdate(saddle);
        entity.getSaddles().remove(deleteSaddle);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openCoating")
    public String openCoating(@RequestParam("coatingId") int coatingId) {
        BaseAnticorrosiveCoating coating = baseAnticorrosiveCoatingDAO.get(BaseAnticorrosiveCoating.class, coatingId);
        if (coating instanceof AbovegroundCoating)
            return "redirect:/entity/AbovegroundCoatings/showFormForUpdate/" + coatingId;
        if (coating instanceof Undercoat)
            return "redirect:/entity/Undercoats/showFormForUpdate/" + coatingId;
        if (coating instanceof UndergroundCoating)
            return "redirect:/entity/UndergroundCoatings/showFormForUpdate/" + coatingId;
        else
            return "redirect:/entity/AbrasiveMaterials/showFormForUpdate/" + coatingId;
    }

    @PostMapping(value = "/action", params = "addCoating")
    public String addCoating(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempCoatingId() != null) {
            BaseAnticorrosiveCoating coating = baseAnticorrosiveCoatingDAO.get(BaseAnticorrosiveCoating.class, entity.getTempCoatingId());
            if (entity.getBaseAnticorrosiveCoatings() != null) {
                entity.getBaseAnticorrosiveCoatings().add(coating);
            }
            else {
                List<BaseAnticorrosiveCoating> BaseAnticorrosiveCoatings = new ArrayList<>();
                BaseAnticorrosiveCoatings.add(coating);
                entity.setBaseAnticorrosiveCoatings(BaseAnticorrosiveCoatings);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteCoating")
    public String deleteCoating (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteCoating) {
        entity.getBaseAnticorrosiveCoatings().remove(deleteCoating);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openSeal")
    public String openSeal(@RequestParam("sealId") int sealId) {
        return "redirect:/entity/MainFlangeSealings/showFormForUpdate/" + sealId;
    }

    @PostMapping(value = "/action", params = "addSeal")
    public String addSeal(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempSealId() != null) {
            MainFlangeSealing sealing = mainFlangeSealingDAO.get(MainFlangeSealing.class, entity.getTempSealId());
            if (entity.getMainFlangeSeals() != null) {
                if (entity.getMainFlangeSeals().size() < 2)
                    entity.getMainFlangeSeals().add(sealing);
                else return sendMessage("Невозможно привязать больше 2-х уплотнений!", model);
            }
            else {
                List<MainFlangeSealing> MainFlangeSeals = new ArrayList<>();
                MainFlangeSeals.add(sealing);
                entity.setMainFlangeSeals(MainFlangeSeals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteSeal")
    public String deleteSeal (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteSeal) {
        entity.getMainFlangeSeals().remove(deleteSeal);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openShearPin")
    public String openShearPin(@RequestParam("shearPinId") int shearPinId) {
        return "redirect:/entity/ShearPins/showFormForUpdate/" + shearPinId;
    }

    @PostMapping(value = "/action", params = "addShearPin")
    public String addShearPin(@RequestParam("amountShear") Integer amountShear, @ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempShearPinId() != null) {
            ShearPin shearPin = shearPinDAO.getForAdd(entity.getTempShearPinId());
            Integer amountUsed = 0;
            if (shearPin.getBaseValveWithShearPins() != null) {
                for(BaseValveWithShearPin withShearPin : shearPin.getBaseValveWithShearPins()) {
                    amountUsed += withShearPin.getShearPinAmount();
                }
            }
            if (shearPin.getAmount() - amountUsed >= amountShear) {
                if (entity.getBaseValveWithShearPins() != null) {
                    for(BaseValveWithShearPin withShearPin : entity.getBaseValveWithShearPins()) {
                        if (entity.getTempShearPinId() == withShearPin.getShearPin().getId())
                            return sendMessage("Штифты данной ЗК/тяги уже добавлены, для изменения кол-ва удалите и добавьте вновь!", model);
                    }
                    entity.getBaseValveWithShearPins().add(new BaseValveWithShearPin(amountShear, shearPin));
                }
                else {
                    List<BaseValveWithShearPin> withShearPin = new ArrayList<>();
                    withShearPin.add(new BaseValveWithShearPin(amountShear, shearPin));
                    entity.setBaseValveWithShearPins(withShearPin);
                }
                model.addAttribute("entity", entity);
            }
            else return sendMessage("Штифты данной ЗК/тяги закончились или введено количество, превышающее остаток! Применение можно посмотреть внутри штифтов.", model);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteShearPin")
    public String deleteShearPin (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteShearPin) {
        BaseValveWithShearPin withShearPin = entity.getBaseValveWithShearPins().get(deleteShearPin);
        if (withShearPin.getId() != 0) {
            withShearPinDAO.delete(BaseValveWithShearPin.class, withShearPin.getId());
        }
        entity.getBaseValveWithShearPins().remove(deleteShearPin);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openScrewStud")
    public String openScrewStud(@RequestParam("screwStudId") int screwStudId) {
        return "redirect:/entity/ScrewStuds/showFormForUpdate/" + screwStudId;
    }

    @PostMapping(value = "/action", params = "addScrewStud")
    public String addScrewStud(@RequestParam("amountStud") Integer amountStud, @ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempScrewStudId() != null) {
            ScrewStud screwStud = screwStudDAO.getForAdd(entity.getTempScrewStudId());
            Integer amountUsed = 0;
            if (screwStud.getBaseValveWithScrewStuds() != null) {
                for(BaseValveWithScrewStud withScrewStud : screwStud.getBaseValveWithScrewStuds()) {
                    amountUsed += withScrewStud.getScrewStudAmount();
                }
            }
            if (screwStud.getAmount() - amountUsed >= amountStud) {
                if (entity.getBaseValveWithScrewStuds() != null) {
                    for(BaseValveWithScrewStud withScrewStud : entity.getBaseValveWithScrewStuds()) {
                        if (entity.getTempScrewStudId() == withScrewStud.getScrewStud().getId())
                            return sendMessage("Шпильки данной партии уже добавлены, для изменения кол-ва удалите и добавьте вновь!", model);
                    }
                    entity.getBaseValveWithScrewStuds().add(new BaseValveWithScrewStud(amountStud, screwStud));
                }
                else {
                    List<BaseValveWithScrewStud> withScrewStud = new ArrayList<>();
                    withScrewStud.add(new BaseValveWithScrewStud(amountStud, screwStud));
                    entity.setBaseValveWithScrewStuds(withScrewStud);
                }
                model.addAttribute("entity", entity);
            }
            else return sendMessage("Шпильки данной партии закончились или введено количество, превышающее остаток! Применение можно посмотреть внутри шпилек.", model);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteScrewStud")
    public String deleteScrewStud (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteScrewStud) {
        BaseValveWithScrewStud withScrewStud = entity.getBaseValveWithScrewStuds().get(deleteScrewStud);
        if (withScrewStud.getId() != 0) {
            withScrewStudDAO.delete(BaseValveWithScrewStud.class, withScrewStud.getId());
        }
        entity.getBaseValveWithScrewStuds().remove(deleteScrewStud);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openScrewNut")
    public String openScrewNut(@RequestParam("screwNutId") int screwNutId) {
        return "redirect:/entity/ScrewNuts/showFormForUpdate/" + screwNutId;
    }

    @PostMapping(value = "/action", params = "addScrewNut")
    public String addScrewNut(@RequestParam("amountNut") Integer amountNut, @ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempScrewNutId() != null) {
            ScrewNut screwNut = screwNutDAO.getForAdd(entity.getTempScrewNutId());
            Integer amountUsed = 0;
            if (screwNut.getBaseValveWithScrewNuts() != null) {
                for(BaseValveWithScrewNut withScrewNut : screwNut.getBaseValveWithScrewNuts()) {
                    amountUsed += withScrewNut.getScrewNutAmount();
                }
            }
            if (screwNut.getAmount() - amountUsed >= amountNut) {
                if (entity.getBaseValveWithScrewNuts() != null) {
                    for(BaseValveWithScrewNut withScrewNut : entity.getBaseValveWithScrewNuts()) {
                        if (entity.getTempScrewNutId() == withScrewNut.getScrewNut().getId())
                            return sendMessage("Гайки данной партии уже добавлены, для изменения кол-ва удалите и добавьте вновь!", model);
                    }
                    entity.getBaseValveWithScrewNuts().add(new BaseValveWithScrewNut(amountNut, screwNut));
                }
                else {
                    List<BaseValveWithScrewNut> withScrewNut = new ArrayList<>();
                    withScrewNut.add(new BaseValveWithScrewNut(amountNut, screwNut));
                    entity.setBaseValveWithScrewNuts(withScrewNut);
                }
                model.addAttribute("entity", entity);
            }
            else return sendMessage("Гайки данной партии закончились или введено количество, превышающее остаток! Применение можно посмотреть внутри гаек.", model);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteScrewNut")
    public String deleteScrewNut (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteScrewNut) {
        BaseValveWithScrewNut withScrewNut = entity.getBaseValveWithScrewNuts().get(deleteScrewNut);
        if (withScrewNut.getId() != 0) {
            withScrewNutDAO.delete(BaseValveWithScrewNut.class, withScrewNut.getId());
        }
        entity.getBaseValveWithScrewNuts().remove(deleteScrewNut);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @GetMapping("/openSpring")
    public String openSpring(@RequestParam("springId") int springId) {
        return "redirect:/entity/Springs/showFormForUpdate/" + springId;
    }

    @PostMapping(value = "/action", params = "addSpring")
    public String addSpring(@RequestParam("amountSpring") Integer amountSpring, @ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempSpringId() != null) {
            Spring spring = springDAO.getForAdd(entity.getTempSpringId());
            Integer amountUsed = 0;
            if (spring.getBaseValveWithSprings() != null) {
                for(BaseValveWithSpring withSpring : spring.getBaseValveWithSprings()) {
                    amountUsed += withSpring.getSpringAmount();
                }
            }
            if (spring.getAmount() - amountUsed >= amountSpring) {
                if (entity.getBaseValveWithSprings() != null) {
                    for(BaseValveWithSpring withSpring : entity.getBaseValveWithSprings()) {
                        if (entity.getTempSpringId() == withSpring.getSpring().getId())
                            return sendMessage("Пружины данной партии уже добавлены, для изменения кол-ва удалите и добавьте вновь!", model);
                    }
                    entity.getBaseValveWithSprings().add(new BaseValveWithSpring(amountSpring, spring));
                }
                else {
                    List<BaseValveWithSpring> withSpring = new ArrayList<>();
                    withSpring.add(new BaseValveWithSpring(amountSpring, spring));
                    entity.setBaseValveWithSprings(withSpring);
                }
                model.addAttribute("entity", entity);
            }
            else return sendMessage("Пружины данной партии закончились или введено количество, превышающее остаток! Применение можно посмотреть внутри пружин.", model);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteSpring")
    public String deleteSpring (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam int deleteSpring) {
        BaseValveWithSpring withSpring = entity.getBaseValveWithSprings().get(deleteSpring);
        if (withSpring.getId() != 0) {
            withSpringDAO.delete(BaseValveWithSpring.class, withSpring.getId());
        }
        entity.getBaseValveWithSprings().remove(deleteSpring);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    private void addOperation(Integer tempTCPid, List<SheetGateValveJournal> targetJournals) {
        BaseTCP tempTCP = TCPDAO.get(SheetGateValveTCP.class, tempTCPid);
        targetJournals.add(new SheetGateValveJournal(tempTCP));
        targetJournals.sort(Comparator.comparingInt(BaseJournal::getPointId));
    }

    @PostMapping(value = "/action", params = "addOperationInput")
    public String addOperationInput(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getInputTCPId() != null) {
            if (entity.getInputControlJournals() == null)
                entity.setInputControlJournals(new ArrayList<>());
            addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationInput")
    public String deleteOperationInput (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam String deleteOperationInput) {
        int i = Integer.parseInt(deleteOperationInput);
        SheetGateValveJournal entityJournal = entity.getInputControlJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveJournal.class, entityJournal.getId());
        }
        entity.getInputControlJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "addOperationWeld")
    public String addOperationWeld(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getWeldTCPId() != null) {
            if (entity.getWeldJournals() == null)
                entity.setWeldJournals(new ArrayList<>());
            addOperation(entity.getWeldTCPId(), entity.getWeldJournals());
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationWeld")
    public String deleteOperationWeld (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam String deleteOperationWeld) {
        int i = Integer.parseInt(deleteOperationWeld);
        SheetGateValveJournal entityJournal = entity.getWeldJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveJournal.class, entityJournal.getId());
        }
        entity.getWeldJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "addOperationMech")
    public String addOperationMech(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getMechTCPId() != null) {
            if (entity.getMechanicalJournals() == null)
                entity.setMechanicalJournals(new ArrayList<>());
            addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationMech")
    public String deleteOperationMech (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam String deleteOperationMech) {
        int i = Integer.parseInt(deleteOperationMech);
        SheetGateValveJournal entityJournal = entity.getMechanicalJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveJournal.class, entityJournal.getId());
        }
        entity.getMechanicalJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "addOperationDoc")
    public String addOperationDoc(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getDocTCPId() != null) {
            if (entity.getDocumentJournals() == null)
                entity.setDocumentJournals(new ArrayList<>());
            for(SheetGateValveJournal journal: entity.getDocumentJournals()) {
                if(Objects.equals(journal.getPointId(), entity.getDocTCPId()) && entity.getDocTCPId() == 114) {
                    return sendMessage("Операция проверки паспорта может быть только одна!", model);
                }
            }
            addOperation(entity.getDocTCPId(), entity.getDocumentJournals());
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationDoc")
    public String deleteOperationDoc (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam String deleteOperationDoc) {
        int i = Integer.parseInt(deleteOperationDoc);
        SheetGateValveJournal entityJournal = entity.getDocumentJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveJournal.class, entityJournal.getId());
        }
        entity.getDocumentJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "addOperationAssembly")
    public String addOperationAssembly(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getAssemblyTCPId() != null) {
            if (entity.getAssemblyJournals() == null)
                entity.setAssemblyJournals(new ArrayList<>());
            addOperation(entity.getAssemblyTCPId(), entity.getAssemblyJournals());
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationAssembly")
    public String deleteOperationAssembly (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam String deleteOperationAssembly) {
        int i = Integer.parseInt(deleteOperationAssembly);
        SheetGateValveJournal entityJournal = entity.getAssemblyJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveJournal.class, entityJournal.getId());
        }
        entity.getAssemblyJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") SheetGateValve entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = coatingTCPDAO.get(CoatingTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() == null) {
                List<CoatingJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new CoatingJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            entity.getEntityJournals().add(new CoatingJournal(tempTCP));
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") SheetGateValve entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        CoatingJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            coatingJournalDAO.delete(CoatingJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/assembly/gate-valve-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") SheetGateValve entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/assembly/gate-valve-editView";
        }

        boolean flag = true;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean isEmpty = true;
        String parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entity.getValveCover() != null) {
            if (entity.getValveCover().getSheetGateValve() != null) {
                if (entity.getValveCover().getSheetGateValve().getId() != entity.getId()) {
                    return sendMessage("ОШИБКА: Выбранная крышка уже применена в " +
                            entity.getValveCover().getSheetGateValve().getName() + " №О" +
                            entity.getValveCover().getSheetGateValve().getNumber(), model);
                }
                if (entity.getValveCover().getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badValveCover";
                }
            }
            if (entity.getValveCover().getStatus().equals("НЕ СООТВ.")) {
                flag = false;
                parameter = "badValveCover";
            }
        }
        if (entity.getValveCase() != null) {
            if (entity.getValveCase().getSheetGateValve() != null) {
                if (entity.getValveCase().getSheetGateValve().getId() != entity.getId()) {
                    return sendMessage("ОШИБКА: Выбранный корпус уже применен в " +
                            entity.getValveCase().getSheetGateValve().getName() + " №О" +
                            entity.getValveCase().getSheetGateValve().getNumber(), model);
                }
                if (entity.getValveCase().getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badValveCase";
                }
            }
            if (entity.getValveCase().getStatus().equals("НЕ СООТВ.")) {
                flag = false;
                parameter = "badValveCase";
            }
        }
        if (entity.getGate() != null) {
            if (entity.getGate().getSheetGateValve() != null) {
                if (entity.getGate().getSheetGateValve().getId() != entity.getId()) {
                    return sendMessage("ОШИБКА: Выбранный шибер уже применен в " +
                            entity.getGate().getSheetGateValve().getName() + " №О" +
                            entity.getGate().getSheetGateValve().getNumber(), model);
                }
                if (entity.getGate().getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badGate";
                }
            }
            if (entity.getGate().getStatus().equals("НЕ СООТВ.")) {
                flag = false;
                parameter = "badGate";
            }
        }
        if (entity.getSaddles() != null) {
            for(Saddle saddle : entity.getSaddles()) {
                if (saddle.getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badSaddle";
                }
            }
        }
        if (entity.getNozzles() != null) {
            for(Nozzle nozzle : entity.getNozzles()) {
                if (nozzle.getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badNozzle";
                }
            }
        }

        if (entities == null)
            entities = getAll();
        for(SheetGateValve tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: ЗШ с таким номером уже существует!", model);
            }
        }
        entity.setValveJournals(
                Stream.of(entity.getInputControlJournals(), entity.getWeldJournals(),
                        entity.getMechanicalJournals(), entity.getDocumentJournals(), entity.getAssemblyJournals())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));

        if (entity.getValveJournals() != null) {
            for (SheetGateValveJournal journal: entity.getValveJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 114 && journal.getStatus().equals("Соответствует")) {
                    flag1 = true;
                }
                if (journal.getPointId() == 115 && journal.getStatus().equals("Соответствует")) {
                    flag2 = true;
                    if (entity.getAutoNumber() == null)
                        return sendMessage("Введите номер авто или ж/д платформы!", model);
                    else {
                        if (!entity.getAutoNumber().matches("^([А-Я]\\s\\d{3}\\s[А-Я]{2}\\s\\d{2,3})$")) {
                            if (!entity.getAutoNumber().matches("^(Ж/д вагон №\\d{7,8})$"))
                                return sendMessage("Номер авто/вагона не соответствует требованиям!", model);
                        }
                    }
                }
                if (journal.getPointId() == 98 && journal.getStatus().equals("Соответствует")) {
                    if (entity.getGatePlace().isBlank()) {
                        return sendMessage("Не указано положение шибера!", model);
                    }
                }
                if (journal.getPointId() == 102 && journal.getStatus().equals("Соответствует")) {
                    if (entity.getTime().isBlank()) {
                        return sendMessage("Не указано время открытия/закрытия ЗШ!", model);
                    }
                    if (entity.getMoment().isBlank()) {
                        return sendMessage("Не указаны моменты!", model);
                    }
                }
                if (journal.getPointId() == 103 && journal.getStatus().equals("Соответствует")) {
                    if (entity.getAutomaticReset().isBlank()) {
                        return sendMessage("Не указано давление сброса!", model);
                    }
                }
                if (journal.getPointId() == 116 && journal.getDate() != null) {
                    if (entity.getNozzles() == null) {
                        return sendMessage("Катушки не выбраны!", model);
                    }
                }
                if (journal.getPointId() == 126 && journal.getDate() != null) {
                    WeldingProcedure WP = weldingProcedureDAO.get(3);
                    if(WP.getLastControl() != null) {
                        if (journal.getDate().after(WP.getLastControl())) {
                            WP.getPeriodicalJournals().add(new WeldingProcedureJournal(WP, journal));
                            WP.setLastControl(journal.getDate());
                            WP.setNextControl(new Date((long)(journal.getDate().getTime() + 7*864*Math.pow(10,5))));
                            weldingProcedureDAO.saveOrUpdate(WP);
                        }
                    }
                }
                if (journal.getPointId() == 187 && journal.getDate() != null) {
                    WeldingProcedure WP = weldingProcedureDAO.get(5);
                    if(WP.getLastControl() != null) {
                        if (journal.getDate().after(WP.getLastControl())) {
                            WP.getPeriodicalJournals().add(new WeldingProcedureJournal(WP, journal));
                            WP.setLastControl(journal.getDate());
                            WP.setNextControl(new Date((long)(journal.getDate().getTime() + 7*864*Math.pow(10,5))));
                            weldingProcedureDAO.saveOrUpdate(WP);
                        }
                    }
                }
            }
        }
        boolean coatingFlag1 = false;
        boolean coatingFlag2 = false;
        if (entity.getEntityJournals() != null) {
            for (CoatingJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 3 && journal.getDate() != null) {
                    coatingFlag1 = true;
                }
                if (journal.getPointId() == 4 && journal.getDate() != null) {
                    coatingFlag2 = true;
                }
                if (journal.getPointId() == 6 && journal.getDate() != null) {
                    if (entity.getBaseAnticorrosiveCoatings() == null) {
                        return sendMessage("Не добавлены материалы АКП!", model);
                    }
                }
            }
        }
        if ((coatingFlag1 && !coatingFlag2) || (!coatingFlag1 && coatingFlag2)) {
            return sendMessage("П.6(АКП) должен быть закрыт вместе с п.7(АКП)", model);
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if(entity.getSaddles() != null) {
            List<Saddle> tempSaddlesList = new ArrayList<>();
            for(Saddle saddle : entity.getSaddles()) {
                Saddle tempSaddle = saddleDAO.get(Saddle.class, saddle.getId());
                tempSaddle.setSheetGateValve(entity);
                tempSaddlesList.add(tempSaddle);
            }
            entity.setSaddles(tempSaddlesList);
        }
        if(entity.getNozzles() != null) {
            List<Nozzle> tempNozzlesList = new ArrayList<>();
            for(Nozzle nozzle : entity.getNozzles()) {
                Nozzle tempNozzle = nozzleDAO.get(Nozzle.class, nozzle.getId());
                tempNozzle.setSheetGateValve(entity);
                tempNozzlesList.add(tempNozzle);
            }
            entity.setNozzles(tempNozzlesList);
        }
        if(entity.getMainFlangeSeals() != null) {
            List<MainFlangeSealing> tempSealsList = new ArrayList<>();
            for(MainFlangeSealing mainFlangeSealing : entity.getMainFlangeSeals()) {
                MainFlangeSealing sealing = mainFlangeSealingDAO.get(MainFlangeSealing.class, mainFlangeSealing.getId());
                tempSealsList.add(sealing);
            }
            entity.setMainFlangeSeals(tempSealsList);
        }
        if(entity.getBaseAnticorrosiveCoatings() != null) {
            List<BaseAnticorrosiveCoating> tempCoatingList = new ArrayList<>();
            for(BaseAnticorrosiveCoating coating : entity.getBaseAnticorrosiveCoatings()) {
                BaseAnticorrosiveCoating tempCoating = baseAnticorrosiveCoatingDAO.get(BaseAnticorrosiveCoating.class, coating.getId());
                tempCoatingList.add(tempCoating);
            }
            entity.setBaseAnticorrosiveCoatings(tempCoatingList);
        }
        if(entity.getBaseValveWithShearPins() != null) {
            for(BaseValveWithShearPin withShearPin : entity.getBaseValveWithShearPins()) {
                ShearPin shearPin = shearPinDAO.get(ShearPin.class, withShearPin.getShearPin().getId());
                withShearPin.setShearPin(shearPin);
                withShearPin.setBaseValve(entity);
            }
        }
        if(entity.getBaseValveWithScrewStuds() != null) {
            for(BaseValveWithScrewStud withScrewStud : entity.getBaseValveWithScrewStuds()) {
                ScrewStud screwStud = screwStudDAO.get(ScrewStud.class, withScrewStud.getScrewStud().getId());
                withScrewStud.setScrewStud(screwStud);
                withScrewStud.setBaseValve(entity);
            }
        }
        if(entity.getBaseValveWithScrewNuts() != null) {
            for(BaseValveWithScrewNut withScrewNut : entity.getBaseValveWithScrewNuts()) {
                ScrewNut screwNut = screwNutDAO.get(ScrewNut.class, withScrewNut.getScrewNut().getId());
                withScrewNut.setScrewNut(screwNut);
                withScrewNut.setBaseValve(entity);
            }
        }
        if(entity.getBaseValveWithSprings() != null) {
            for(BaseValveWithSpring withSpring : entity.getBaseValveWithSprings()) {
                Spring spring = springDAO.get(Spring.class, withSpring.getSpring().getId());
                withSpring.setSpring(spring);
                withSpring.setBaseValve(entity);
            }
        }
        if (flag) {
            entity.setStatus("Cоотв.");
            if (flag1 && flag2) {
                entity.setStatus("Отгружен");
            }
        }
        else
            entity.setStatus("НЕ СООТВ.");
        if (entityDAO.saveOrUpdate(entity))
            return "redirect:/entity/SheetGateValves/showAll?parameter=" + parameter;
        else
            return sendMessage("Не удается сохранить объект! Что-то пошло не так, сообщите об ошибке!", model);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/assembly/gate-valve-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditSheetGateValves", true);
        model.addAttribute("title", "Редактирование ЗШ");
        model.addAttribute("editMode", true);
        model.addAttribute("gates", gates);
        model.addAttribute("saddles", saddles);
        model.addAttribute("nozzles", nozzles);
        model.addAttribute("cases", cases);
        model.addAttribute("covers", covers);
        model.addAttribute("mainFlangeSeals", mainFlangeSeals);
        model.addAttribute("baseAnticorrosiveCoatings", baseAnticorrosiveCoatings);
        model.addAttribute("shearPins", shearPins);
        model.addAttribute("screwNuts", screwNuts);
        model.addAttribute("screwStuds", screwStuds);
        model.addAttribute("springs", springs);
        model.addAttribute("drawings", drawings);
        model.addAttribute("pids", pids);
        model.addAttribute("inputControlTCP", inputControlTCP);
        model.addAttribute("weldTCP", weldTCP);
        model.addAttribute("mechanicalTCP", mechanicalTCP);
        model.addAttribute("documentTCP", documentTCP);
        model.addAttribute("assemblyTCP", assemblyTCP);
        model.addAttribute("entityTCPs", entityTCPs);

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        SheetGateValve entity = entityDAO.get(SheetGateValve.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(SheetGateValve.class, id);
        return "redirect:/entity/SheetGateValves/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SheetGateValve> getAll() {
        entities = entityDAO.getAll(SheetGateValve.class);
        return entities;
    }
}
