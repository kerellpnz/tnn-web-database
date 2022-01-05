package com.kerellpnz.tnnwebdatabase.controller.unit.assembly;

import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.assemblyunit.GateValveCoverDAO;
import com.kerellpnz.tnnwebdatabase.dao.detail.FrontalSaddleSealingDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.PIDDAO;
import com.kerellpnz.tnnwebdatabase.dao.periodical.WeldingProcedureDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveCoverJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.WeldingProcedureJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.SheetGateValveCoverTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.*;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.SheetMaterial;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.WeldingProcedure;
import com.kerellpnz.tnnwebdatabase.exception.EntityNotFoundException;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/entity/SheetGateValveCovers")
public class SheetGateValveCoverController extends BaseAssemblyController {

    private final GateValveCoverDAO entityDAO;
    private final BaseEntityDAO<SheetGateValveCoverJournal> baseJournalDAO;
    private final WeldingProcedureDAO weldingProcedureDAO;
    private final BaseTCPDAO<SheetGateValveCoverTCP> TCPDAO;
    private final PIDDAO pidDAO;
    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;
    private final BaseEntityDAO<CaseBottom> caseBottomDAO;
    private final BaseEntityDAO<Flange> flangeDAO;
    private final BaseEntityDAO<Column> columnDAO;
    private final BaseEntityDAO<CoverSleeve> coverSleeveDAO;
    private final BaseEntityDAO<CoverSleeve008> coverSleeve008DAO;
    private final BaseEntityDAO<Spindle> spindleDAO;
    private final FrontalSaddleSealingDAO frontalSaddleSealingDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<SheetGateValveCoverTCP> entityTCPs;
    private List<PID> pids;
    private List<MetalMaterial> metalMaterials;
    private List<CaseBottom> caseBottoms;
    private List<Flange> flanges;
    private List<Column> columns;
    private List<CoverSleeve> coverSleeves;
    private List<CoverSleeve008> coverSleeves008;
    private List<Spindle> spindles;
    private List<FrontalSaddleSealing> frontalSaddleSeals;
    private List<String> dns;
    private List<String> pns;
    private List<SheetGateValveCoverTCP> inputControlTCP;
    private List<SheetGateValveCoverTCP> weldTCP;
    private List<SheetGateValveCoverTCP> mechanicalTCP;
    private List<SheetGateValveCoverTCP> documentTCP;
    private List<SheetGateValveCoverTCP> assemblyTCP;

    private List<SheetGateValveCover> entities;
    private String parameter;

    @Autowired
    public SheetGateValveCoverController(HttpSession session, 
                                         JournalNumberDAO journalNumberDAO,
                                         GateValveCoverDAO entityDAO,
                                         BaseEntityDAO<SheetGateValveCoverJournal> baseJournalDAO,
                                         WeldingProcedureDAO weldingProcedureDAO,
                                         BaseTCPDAO<SheetGateValveCoverTCP> TCPDAO, 
                                         PIDDAO pidDAO, 
                                         BaseEntityDAO<MetalMaterial> metalMaterialDAO, 
                                         BaseEntityDAO<CaseBottom> caseBottomDAO, 
                                         BaseEntityDAO<Flange> flangeDAO, 
                                         BaseEntityDAO<Column> columnDAO, 
                                         BaseEntityDAO<CoverSleeve> coverSleeveDAO, 
                                         BaseEntityDAO<CoverSleeve008> coverSleeve008DAO, 
                                         BaseEntityDAO<Spindle> spindleDAO,
                                         FrontalSaddleSealingDAO frontalSaddleSealingDAO,
                                         InspectorDAO inspectorDAO, 
                                         ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.weldingProcedureDAO = weldingProcedureDAO;
        this.TCPDAO = TCPDAO;
        this.pidDAO = pidDAO;
        this.metalMaterialDAO = metalMaterialDAO;
        this.caseBottomDAO = caseBottomDAO;
        this.flangeDAO = flangeDAO;
        this.columnDAO = columnDAO;
        this.coverSleeveDAO = coverSleeveDAO;
        this.coverSleeve008DAO = coverSleeve008DAO;
        this.spindleDAO = spindleDAO;
        this.frontalSaddleSealingDAO = frontalSaddleSealingDAO;
        this.inspectorDAO = inspectorDAO;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            switch (parameter) {
                case "badBottom" -> mv.addObject("message", "ВНИМАНИЕ: Выбранное днище имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к крышке.");
                case "badFlange" -> mv.addObject("message", "ВНИМАНИЕ: Выбранный фланец имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к крышке.");
                case "badColumn" -> mv.addObject("message", "ВНИМАНИЕ: Стойка имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к крышке.");
                case "badCoverSleeve" -> mv.addObject("message", "ВНИМАНИЕ: Центральная втулка имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к крышке.");
                case "badCoverSleeve008" -> mv.addObject("message", "ВНИМАНИЕ: Дренажная втулка имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к крышке.");
                case "badSpindle" -> mv.addObject("message", "ВНИМАНИЕ: Шпиндель имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к крышке.");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Крышка с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новая крышка");
        SheetGateValveCover entity = new SheetGateValveCover();
        entityTCPs = TCPDAO.getAll(SheetGateValveCoverTCP.class);
        List<SheetGateValveCoverJournal> entityJournals = new ArrayList<>(20);
        for (SheetGateValveCoverTCP tcp : entityTCPs) {
            SheetGateValveCoverJournal entityJournal = new SheetGateValveCoverJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        SheetGateValveCover entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId) throws EntityNotFoundException {
        SheetGateValveCover entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(SheetGateValveCover entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики крышки");
        for (SheetGateValveCoverJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(SheetGateValveCoverTCP.class);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    private void TCPInitialize(List<SheetGateValveCoverTCP> entityTCPs) {
        inputControlTCP = new ArrayList<>();
        weldTCP = new ArrayList<>();
        mechanicalTCP = new ArrayList<>();
        documentTCP = new ArrayList<>();
        assemblyTCP = new ArrayList<>();
        if (entityTCPs != null) {
            for(SheetGateValveCoverTCP tcp : entityTCPs) {
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
        mv.addObject("title", "Крышка ЗШ");
        mv.addObject("assemblyClass", "SheetGateValveCovers");
        mv.addObject("userClickAssemblies", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("unit/assembly/gate-valve-cover-editView");
        mv.addObject("title", title);
        mv.addObject("assemblyClass", "SheetGateValveCovers");
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, SheetGateValveCover entity) {
        drawings = entityDAO.getDistinctDrawing();
        caseBottoms = caseBottomDAO.getAll(CaseBottom.class);
        flanges = flangeDAO.getAll(Flange.class);
        coverSleeves = coverSleeveDAO.getAll(CoverSleeve.class);
        coverSleeves008 = coverSleeve008DAO.getAll(CoverSleeve008.class);
        spindles = spindleDAO.getAll(Spindle.class);
        frontalSaddleSeals = frontalSaddleSealingDAO.getAll(FrontalSaddleSealing.class);
        columns = columnDAO.getAll(Column.class);
        metalMaterials = metalMaterialDAO.getAll(MetalMaterial.class);
        pids = pidDAO.getAll(PID.class);
        dns = serviceClass.getDns();
        pns = serviceClass.getPns();
        mv.addObject("coverSleeves", coverSleeves);
        mv.addObject("coverSleeves008", coverSleeves008);
        mv.addObject("spindles", spindles);
        mv.addObject("frontalSaddleSeals", frontalSaddleSeals);
        mv.addObject("columns", columns);
        mv.addObject("metalMaterials", metalMaterials);
        mv.addObject("drawings", drawings);
        mv.addObject("caseBottoms", caseBottoms);
        mv.addObject("flanges", flanges);
        mv.addObject("pids", pids);
        mv.addObject("dns", dns);
        mv.addObject("pns", pns);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        mv.addObject("inputControlTCP", inputControlTCP);
        mv.addObject("weldTCP", weldTCP);
        mv.addObject("mechanicalTCP", mechanicalTCP);
        mv.addObject("documentTCP", documentTCP);

        if (entity.getEntityJournals() != null) {
            for(SheetGateValveCoverJournal journal : entity.getEntityJournals()) {
                String operationType = TCPDAO.getOperationTypeById(SheetGateValveCoverTCP.class, journal.getPointId());
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

    @PostMapping(value = "/action", params = "openFlange")
    public String openFlange(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getFlange() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/Flanges/showFormForUpdate/" + entity.getFlange().getId() + "/" + entity.getId() + "/cover";
            else
                return "redirect:/entity/Flanges/showFormForUpdate/" + entity.getFlange().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "openCaseBottom")
    public String openCaseBottom(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getCaseBottom() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/CaseBottoms/showFormForUpdate/" + entity.getCaseBottom().getId() + "/" + entity.getId() + "/cover";
            else
                return "redirect:/entity/CaseBottoms/showFormForUpdate/" + entity.getCaseBottom().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "openCoverSleeve")
    public String openCoverSleeve(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getCoverSleeve() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/CoverSleeves/showFormForUpdate/" + entity.getCoverSleeve().getId() + "/" + entity.getId();
            else
                return "redirect:/entity/CoverSleeves/showFormForUpdate/" + entity.getCoverSleeve().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "openCoverSleeve008")
    public String openCoverSleeve008(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getCoverSleeve008() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/CoverSleeves008/showFormForUpdate/" + entity.getCoverSleeve008().getId() + "/" + entity.getId() + "/cover";
            else
                return "redirect:/entity/CoverSleeves008/showFormForUpdate/" + entity.getCoverSleeve008().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "openSpindle")
    public String openSpindle(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getSpindle() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/Spindles/showFormForUpdate/" + entity.getSpindle().getId() + "/" + entity.getId();
            else
                return "redirect:/entity/Spindles/showFormForUpdate/" + entity.getSpindle().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "openColumn")
    public String openColumn(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getColumn() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/Columns/showFormForUpdate/" + entity.getColumn().getId() + "/" + entity.getId();
            else
                return "redirect:/entity/Columns/showFormForUpdate/" + entity.getColumn().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "openMaterial")
    public String openMaterial(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getMetalMaterial() != null) {
            if (entity.getMetalMaterial() instanceof SheetMaterial)
                return "redirect:/entity/SheetMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
            else
                return "redirect:/entity/RolledMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @RequestMapping("/openSeal")
    public String openSeal(@RequestParam("sealId") int sealId) {
        return "redirect:/entity/FrontalSaddleSealings/showFormForUpdate/" + sealId;
    }

    @PostMapping(value = "/action", params = "addSeal")
    public String addSeal(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getTempSealId() != null) {
            FrontalSaddleSealing sealing = frontalSaddleSealingDAO.getForCover(entity.getTempSealId());
            if (sealing.getAmount() - sealing.getSheetGateValveCovers().size() > 0) {
                if (entity.getFrontalSaddleSeals() != null) {
                    if (entity.getFrontalSaddleSeals().size() < 4)
                        entity.getFrontalSaddleSeals().add(sealing);
                    else return sendMessage("Невозможно привязать больше 4-х уплотнений!", model);
                }
                else {
                    List<FrontalSaddleSealing> frontalSaddleSeals = new ArrayList<>();
                    frontalSaddleSeals.add(sealing);
                    entity.setFrontalSaddleSeals(frontalSaddleSeals);
                }
                model.addAttribute("entity", entity);
            }
            else return sendMessage("Уплотнения данной партии закончились! Применение можно посмотреть внутри самого уплотнения.", model);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "deleteSeal")
    public String deleteSeal (@ModelAttribute("entity") SheetGateValveCover entity, Model model, @RequestParam int deleteSeal) {
        entity.getFrontalSaddleSeals().remove(deleteSeal);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-cover-editView";
    }

    private void addOperation(Integer tempTCPid, List<SheetGateValveCoverJournal> targetJournals) {
        BaseTCP tempTCP = TCPDAO.get(SheetGateValveCoverTCP.class, tempTCPid);
        targetJournals.add(new SheetGateValveCoverJournal(tempTCP));
        targetJournals.sort(Comparator.comparingInt(BaseJournal::getPointId));
    }

    @PostMapping(value = "/action", params = "addOperationInput")
    public String addOperationInput(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getInputTCPId() != null) {
            if (entity.getInputControlJournals() != null) {
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            else {
                List<SheetGateValveCoverJournal> entityJournals = new ArrayList<>();
                entity.setInputControlJournals(entityJournals);
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationInput")
    public String deleteOperationInput (@ModelAttribute("entity") SheetGateValveCover entity, Model model, @RequestParam String deleteOperationInput) {
        int i = Integer.parseInt(deleteOperationInput);
        SheetGateValveCoverJournal entityJournal = entity.getInputControlJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCoverJournal.class, entityJournal.getId());
        }
        entity.getInputControlJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "addOperationWeld")
    public String addOperationWeld(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getWeldTCPId() != null) {
            if (entity.getWeldJournals() != null) {
                addOperation(entity.getWeldTCPId(), entity.getWeldJournals());
            }
            else {
                List<SheetGateValveCoverJournal> entityJournals = new ArrayList<>();
                entity.setWeldJournals(entityJournals);
                addOperation(entity.getWeldTCPId(), entity.getWeldJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationWeld")
    public String deleteOperationWeld (@ModelAttribute("entity") SheetGateValveCover entity, Model model, @RequestParam String deleteOperationWeld) {
        int i = Integer.parseInt(deleteOperationWeld);
        SheetGateValveCoverJournal entityJournal = entity.getWeldJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCoverJournal.class, entityJournal.getId());
        }
        entity.getWeldJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "addOperationMech")
    public String addOperationMech(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getMechTCPId() != null) {
            if (entity.getMechanicalJournals() != null) {
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            else {
                List<SheetGateValveCoverJournal> entityJournals = new ArrayList<>();
                entity.setMechanicalJournals(entityJournals);
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationMech")
    public String deleteOperationMech (@ModelAttribute("entity") SheetGateValveCover entity, Model model, @RequestParam String deleteOperationMech) {
        int i = Integer.parseInt(deleteOperationMech);
        SheetGateValveCoverJournal entityJournal = entity.getMechanicalJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCoverJournal.class, entityJournal.getId());
        }
        entity.getMechanicalJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "addOperationDoc")
    public String addOperationDoc(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getDocTCPId() != null) {
            if (entity.getDocumentJournals() != null) {
                addOperation(entity.getDocTCPId(), entity.getDocumentJournals());
            }
            else {
                List<SheetGateValveCoverJournal> entityJournals = new ArrayList<>();
                entity.setDocumentJournals(entityJournals);
                addOperation(entity.getDocTCPId(), entity.getDocumentJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationDoc")
    public String deleteOperationDoc (@ModelAttribute("entity") SheetGateValveCover entity, Model model, @RequestParam String deleteOperationDoc) {
        int i = Integer.parseInt(deleteOperationDoc);
        SheetGateValveCoverJournal entityJournal = entity.getDocumentJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCoverJournal.class, entityJournal.getId());
        }
        entity.getDocumentJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "addOperationAssembly")
    public String addOperationAssembly(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getAssemblyTCPId() != null) {
            if (entity.getAssemblyJournals() != null) {
                addOperation(entity.getAssemblyTCPId(), entity.getAssemblyJournals());
            }
            else {
                List<SheetGateValveCoverJournal> entityJournals = new ArrayList<>();
                entity.setAssemblyJournals(entityJournals);
                addOperation(entity.getAssemblyTCPId(), entity.getAssemblyJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationAssembly")
    public String deleteOperationAssembly (@ModelAttribute("entity") SheetGateValveCover entity, Model model, @RequestParam String deleteOperationAssembly) {
        int i = Integer.parseInt(deleteOperationAssembly);
        SheetGateValveCoverJournal entityJournal = entity.getAssemblyJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCoverJournal.class, entityJournal.getId());
        }
        entity.getAssemblyJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-cover-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") SheetGateValveCover entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "unit/assembly/gate-valve-cover-editView";
        }

        boolean flag = true;
        boolean isEmpty = true;
        boolean assemblyFlag1 = false;
        boolean assemblyFlag2 = false;
        boolean assemblyFlag3 = false;
        boolean assemblyFlag4 = false;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entity.getCaseBottom() != null) {
            String result = checkCaseBottom(entity);
            if (result.startsWith("ОШИБКА"))
                return sendMessage(result, model);
            else if (result.equals("badBottom")) {
                flag = false;
                parameter = result;
            }
        }
        if (entity.getFlange() != null) {
            String result = checkFlange(entity);
            if (result.startsWith("ОШИБКА"))
                return sendMessage(result, model);
            else if (result.equals("badFlange")) {
                flag = false;
                parameter = result;
            }
        }
        if (entity.getCoverSleeve008() != null) {
            String result = checkCoverSleeve008(entity);
            if (result.startsWith("ОШИБКА"))
                return sendMessage(result, model);
            else if (result.equals("badCoverSleeve008")) {
                flag = false;
                parameter = result;
            }
        }
        if (entity.getCoverSleeve() != null) {
            if (entity.getCoverSleeve().getSheetGateValveCover() != null) {
                if (entity.getCoverSleeve().getSheetGateValveCover().getId() != entity.getId()) {
                    return sendMessage("ОШИБКА: Выбранная центральная втулка уже применена в " +
                            entity.getCoverSleeve().getSheetGateValveCover().getName() + " №" +
                            entity.getCoverSleeve().getSheetGateValveCover().getNumber(), model);
                }
                if (entity.getCoverSleeve().getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badCoverSleeve";
                }
            }
            if (entity.getCoverSleeve().getStatus().equals("НЕ СООТВ.")) {
                flag = false;
                parameter = "badCoverSleeve";
            }  
        }
        if (entity.getSpindle() != null) {
            if (entity.getSpindle().getSheetGateValveCover() != null) {
                if (entity.getSpindle().getSheetGateValveCover().getId() != entity.getId()) {
                    return sendMessage("ОШИБКА: Выбранный шпиндель уже применен в " +
                            entity.getSpindle().getSheetGateValveCover().getName() + " №" +
                            entity.getSpindle().getSheetGateValveCover().getNumber(), model);
                }
                if (entity.getSpindle().getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badSpindle";
                }
            }
            if (entity.getSpindle().getStatus().equals("НЕ СООТВ.")) {
                flag = false;
                parameter = "badSpindle";
            }
        }
        if (entity.getColumn() != null) {
            if (entity.getColumn().getSheetGateValveCover() != null) {
                if (entity.getColumn().getSheetGateValveCover().getId() != entity.getId()) {
                    return sendMessage("ОШИБКА: Выбранная стойка уже применена в " +
                            entity.getColumn().getSheetGateValveCover().getName() + " №" +
                            entity.getColumn().getSheetGateValveCover().getNumber(), model);
                }
                if (entity.getColumn().getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badColumn";
                }
            }
            if (entity.getColumn().getStatus().equals("НЕ СООТВ.")) {
                flag = false;
                parameter = "badColumn";
            }
        }
        if (entity.getMetalMaterial() != null) {
            if (!checkMaterial(entity, entity.getMetalMaterial())) {
                flag = false;
                parameter = "badMetal";
            }
        }
        if(entity.getMaterial().isBlank() || entity.getMelt().isBlank()) {
            return sendMessage("Введите плавку/материал!", model);
        }

        if (entities == null)
            entities = getAll();
        for(SheetGateValveCover tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Крышка с таким характеристиками уже существует!", model);
            }
        }
        if (entity.getSpindle() != null && entity.getColumn() != null && entity.getFrontalSaddleSeals() != null) {
            assemblyFlag4 = true;
        }
        entity.setEntityJournals(
                Stream.of(entity.getInputControlJournals(), entity.getWeldJournals(),
                                entity.getMechanicalJournals(), entity.getDocumentJournals(), entity.getAssemblyJournals())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        if (entity.getEntityJournals() != null) {
            for (SheetGateValveCoverJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 63 && journal.getDate() != null) {
                    if (entity.getFlange() == null) {
                        return sendMessage("Не выбран фланец!", model);
                    }
                }
                if (journal.getPointId() == 64 && journal.getDate() != null) {
                    if (entity.getCoverSleeve() == null) {
                        return sendMessage("Не выбрана центральная втулка!", model);
                    }
                }
                if (journal.getPointId() == 65 && journal.getDate() != null) {
                    if (entity.getCoverSleeve008() == null) {
                        return sendMessage("Не выбрана дренажная втулка!", model);
                    }
                }
                if (journal.getPointId() == 73 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag1 = true;
                }
                if (journal.getPointId() == 76 && journal.getDate() != null) {
                    if (entity.getPn().isBlank()) {
                        return sendMessage("Не выбрано давление!", model);
                    }
                    if (journal.getStatus().equals("Соответствует"))
                        assemblyFlag2 = true;
                }
                if (journal.getPointId() == 154 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag3 = true;
                }
                if (journal.getPointId() == 66 && journal.getDate() != null) {
                    WeldingProcedure WP = weldingProcedureDAO.get(1);
                    if(WP.getLastControl() != null) {
                        if (journal.getDate().after(WP.getLastControl())) {
                            WP.getPeriodicalJournals().add(new WeldingProcedureJournal(WP, journal));
                            WP.setLastControl(journal.getDate());
                            WP.setNextControl(new Date((long)(journal.getDate().getTime() + 7*864*Math.pow(10,5))));
                            weldingProcedureDAO.saveOrUpdate(WP);
                        }
                    }
                }
                if (journal.getPointId() == 67 && journal.getDate() != null) {
                    WeldingProcedure WP = weldingProcedureDAO.get(2);
                    if(WP.getLastControl() != null) {
                        if (journal.getDate().after(WP.getLastControl())) {
                            WP.getPeriodicalJournals().add(new WeldingProcedureJournal(WP, journal));
                            WP.setLastControl(journal.getDate());
                            WP.setNextControl(new Date((long)(journal.getDate().getTime() + 7*864*Math.pow(10,5))));
                            weldingProcedureDAO.saveOrUpdate(WP);
                        }
                    }
                }
                if (journal.getPointId() == 68 && journal.getDate() != null) {
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
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if(entity.getFrontalSaddleSeals() != null) {
            List<FrontalSaddleSealing> tempSealsList = new ArrayList<>();
            for(FrontalSaddleSealing frontalSaddleSealing : entity.getFrontalSaddleSeals()) {
                FrontalSaddleSealing sealing = frontalSaddleSealingDAO.get(FrontalSaddleSealing.class, frontalSaddleSealing.getId());
                tempSealsList.add(sealing);
            }
            entity.setFrontalSaddleSeals(tempSealsList);
        }
        if (flag) {
            entity.setStatus("Cоотв.");
            if (assemblyFlag1 && assemblyFlag2 && assemblyFlag3 && assemblyFlag4)
                entity.setStatus("Готово к сборке");
        }
        else
            entity.setStatus("НЕ СООТВ.");
        if (entityDAO.saveOrUpdate(entity)) {
            if (entity.getReqId() != null)
                return "redirect:/entity/SheetGateValves/showFormForUpdate/" + entity.getReqId();
            else
                return "redirect:/entity/SheetGateValveCovers/showAll?parameter=" + parameter;
        }
        else
            return sendMessage("Не удается сохранить объект! Что-то пошло не так, сообщите об ошибке!", model);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "unit/assembly/gate-valve-cover-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("title", "Характеристики крышки");
        model.addAttribute("editMode", true);
        model.addAttribute("assemblyClass", "SheetGateValveCovers");
        model.addAttribute("drawings", drawings);
        model.addAttribute("caseBottoms", caseBottoms);
        model.addAttribute("flanges", flanges);
        model.addAttribute("pids", pids);
        model.addAttribute("coverSleeves", coverSleeves);
        model.addAttribute("coverSleeves008", coverSleeves008);
        model.addAttribute("spindles", spindles);
        model.addAttribute("frontalSaddleSeals", frontalSaddleSeals);
        model.addAttribute("columns", columns);
        model.addAttribute("metalMaterials", metalMaterials);
        model.addAttribute("dns", dns);
        model.addAttribute("pns", pns);
        model.addAttribute("inputControlTCP", inputControlTCP);
        model.addAttribute("weldTCP", weldTCP);
        model.addAttribute("mechanicalTCP", mechanicalTCP);
        model.addAttribute("documentTCP", documentTCP);
        model.addAttribute("assemblyTCP", assemblyTCP);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number) {
        SheetGateValveCover entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/SheetGateValveCovers/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(SheetGateValveCover entity, String newNumber) {
        SheetGateValveCover entityNew = new SheetGateValveCover(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (SheetGateValveCoverJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new SheetGateValveCoverJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new SheetGateValveCoverJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        SheetGateValveCover entity = entityDAO.get(SheetGateValveCover.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(SheetGateValveCover.class, id);
        return "redirect:/entity/SheetGateValveCovers/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SheetGateValveCover> getAll() {
        entities = entityDAO.getAll(SheetGateValveCover.class);
        return entities;
    }
}
