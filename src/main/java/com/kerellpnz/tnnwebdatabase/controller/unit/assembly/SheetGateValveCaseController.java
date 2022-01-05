package com.kerellpnz.tnnwebdatabase.controller.unit.assembly;

import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.assemblyunit.GateValveCaseDAO;
import com.kerellpnz.tnnwebdatabase.dao.detail.RingDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.PIDDAO;
import com.kerellpnz.tnnwebdatabase.dao.periodical.WeldingProcedureDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveCaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.WeldingProcedureJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.SheetGateValveCaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCase;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.*;
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
@RequestMapping("/entity/SheetGateValveCases")
public class SheetGateValveCaseController extends BaseAssemblyController {

    private final GateValveCaseDAO entityDAO;
    private final BaseEntityDAO<SheetGateValveCaseJournal> baseJournalDAO;
    private final WeldingProcedureDAO weldingProcedureDAO;
    private final BaseTCPDAO<SheetGateValveCaseTCP> TCPDAO;
    private final PIDDAO pidDAO;
    private final BaseEntityDAO<CaseBottom> caseBottomDAO;
    private final BaseEntityDAO<Flange> flangeDAO;
    private final BaseEntityDAO<CoverSleeve008> coverSleeve008DAO;
    private final RingDAO ringDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<SheetGateValveCaseTCP> entityTCPs;
    private List<PID> pids;
    private List<CoverSleeve008> coverSleeves008;
    private List<CaseBottom> caseBottoms;
    private List<Flange> flanges;
    private List<Ring> rings;
    private List<String> dns;
    private List<String> pns;
    private List<SheetGateValveCaseTCP> inputControlTCP;
    private List<SheetGateValveCaseTCP> weldTCP;
    private List<SheetGateValveCaseTCP> mechanicalTCP;
    private List<SheetGateValveCaseTCP> documentTCP;

    private List<SheetGateValveCase> entities;
    private String parameter;

    @Autowired
    public SheetGateValveCaseController(HttpSession session,
                                JournalNumberDAO journalNumberDAO,
                                GateValveCaseDAO entityDAO,
                                BaseEntityDAO<SheetGateValveCaseJournal> baseJournalDAO,
                                WeldingProcedureDAO weldingProcedureDAO,
                                BaseEntityDAO<CoverSleeve008> coverSleeve008DAO,
                                BaseTCPDAO<SheetGateValveCaseTCP> TCPDAO,
                                PIDDAO pidDAO,
                                BaseEntityDAO<CaseBottom> caseBottomDAO,
                                BaseEntityDAO<Flange> flangeDAO,
                                RingDAO ringDAO,
                                InspectorDAO inspectorDAO,
                                ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.weldingProcedureDAO = weldingProcedureDAO;
        this.TCPDAO = TCPDAO;
        this.pidDAO = pidDAO;
        this.coverSleeve008DAO = coverSleeve008DAO;
        this.caseBottomDAO = caseBottomDAO;
        this.flangeDAO = flangeDAO;
        this.ringDAO = ringDAO;
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
                        " поэтому этот же статус применен к корпусу.");
                case "badFlange" -> mv.addObject("message", "ВНИМАНИЕ: Выбранный фланец имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к корпусу.");
                case "badCoverSleeve008" -> mv.addObject("message", "ВНИМАНИЕ: Выбранная дренажная втулка имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к корпусу.");
                case "badRing" -> mv.addObject("message", "ВНИМАНИЕ: Кольцо имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к корпусу.");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Корпус с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новый корпус");
        SheetGateValveCase entity = new SheetGateValveCase();
        entityTCPs = TCPDAO.getAll(SheetGateValveCaseTCP.class);
        List<SheetGateValveCaseJournal> entityJournals = new ArrayList<>(20);
        for (SheetGateValveCaseTCP tcp : entityTCPs) {
            SheetGateValveCaseJournal entityJournal = new SheetGateValveCaseJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        SheetGateValveCase entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId) throws EntityNotFoundException {
        SheetGateValveCase entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(SheetGateValveCase entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики корпуса");
        for (SheetGateValveCaseJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(SheetGateValveCaseTCP.class);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    private void TCPInitialize(List<SheetGateValveCaseTCP> entityTCPs) {
        inputControlTCP = new ArrayList<>();
        weldTCP = new ArrayList<>();
        mechanicalTCP = new ArrayList<>();
        documentTCP = new ArrayList<>();
        if (entityTCPs != null) {
            for(SheetGateValveCaseTCP tcp : entityTCPs) {
                switch (tcp.getOperationType()) {
                    case "Входной контроль" -> inputControlTCP.add(tcp);
                    case "Сварка" -> weldTCP.add(tcp);
                    case "Обработка" -> mechanicalTCP.add(tcp);
                    case "Документация" -> documentTCP.add(tcp);
                }
            }
        }
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Корпус ЗШ");
        mv.addObject("assemblyClass", "SheetGateValveCases");
        mv.addObject("userClickAssemblies", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("unit/assembly/gate-valve-case-editView");
        mv.addObject("title", title);
        mv.addObject("assemblyClass", "SheetGateValveCases");
        mv.addObject("userClickEditSheetGateValveCases", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, SheetGateValveCase entity) {
        rings = ringDAO.getUnusedRings();
        drawings = entityDAO.getDistinctDrawing();
        coverSleeves008 = coverSleeve008DAO.getAll(CoverSleeve008.class);
        caseBottoms = caseBottomDAO.getAll(CaseBottom.class);
        flanges = flangeDAO.getAll(Flange.class);
        pids = pidDAO.getAll(PID.class);
        dns = serviceClass.getDns();
        pns = serviceClass.getPns();
        mv.addObject("rings", rings);
        mv.addObject("drawings", drawings);
        mv.addObject("coverSleeves008", coverSleeves008);
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
            for(SheetGateValveCaseJournal journal : entity.getEntityJournals()) {
                String operationType = TCPDAO.getOperationTypeById(SheetGateValveCaseTCP.class, journal.getPointId());
                switch (operationType) {
                    case "Входной контроль" -> entity.getInputControlJournals().add(journal);
                    case "Сварка" -> entity.getWeldJournals().add(journal);
                    case "Обработка" -> entity.getMechanicalJournals().add(journal);
                    case "Документация" -> entity.getDocumentJournals().add(journal);
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
        }
        return mv;
    }

    @PostMapping(value = "/action", params = "openFlange")
    public String openFlange(@ModelAttribute("entity") SheetGateValveCase entity, Model model) {
        if (entity.getFlange() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/Flanges/showFormForUpdate/" + entity.getFlange().getId() + "/" + entity.getId() + "/case";
            else
                return "redirect:/entity/Flanges/showFormForUpdate/" + entity.getFlange().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "openCaseBottom")
    public String openCaseBottom(@ModelAttribute("entity") SheetGateValveCase entity, Model model) {
        if (entity.getCaseBottom() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/CaseBottoms/showFormForUpdate/" + entity.getCaseBottom().getId() + "/" + entity.getId() + "/case";
            else
                return "redirect:/entity/CaseBottoms/showFormForUpdate/" + entity.getCaseBottom().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "openCoverSleeve008")
    public String openCoverSleeve008(@ModelAttribute("entity") SheetGateValveCover entity, Model model) {
        if (entity.getCoverSleeve008() != null) {
            if (entity.getId() != 0)
                return "redirect:/entity/CoverSleeves008/showFormForUpdate/" + entity.getCoverSleeve008().getId() + "/" + entity.getId() + "/case";
            else
                return "redirect:/entity/CoverSleeves008/showFormForUpdate/" + entity.getCoverSleeve008().getId();
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @GetMapping("/openRing")
    public String openRing(@RequestParam("ringId") int ringId, @RequestParam("caseId") int caseId) {
        if (caseId != 0)
            return "redirect:/entity/Rings/showFormForUpdate/" + ringId + "/" + caseId;
        else
            return "redirect:/entity/Rings/showFormForUpdate/" + ringId;
    }

    @PostMapping(value = "/action", params = "addRing")
    public String addRing(@ModelAttribute("entity") SheetGateValveCase entity, Model model) {
        if (entity.getTempRingId() != null) {
            Ring ring = ringDAO.get(Ring.class, entity.getTempRingId());
            if (entity.getRings() != null) {
                if (entity.getRings().size() < 2)
                    if (entity.getRings().get(0).getId() != ring.getId())
                        entity.getRings().add(ring);
                    else
                        return sendMessage("ОШИБКА: Попытка добавить два одинаковых объекта!", model);
                else
                    return sendMessage("ОШИБКА: Невозможно привязать больше двух колец!", model);
            }
            else {
                List<Ring> rings = new ArrayList<>();
                rings.add(ring);
                entity.setRings(rings);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "deleteRing")
    public String deleteRing (@ModelAttribute("entity") SheetGateValveCase entity, Model model, @RequestParam int deleteRing) {
        Ring ring = ringDAO.get(Ring.class, entity.getRings().get(deleteRing).getId());
        ring.setSheetGateValveCase(null);
        ringDAO.saveOrUpdate(ring);
        entity.getRings().remove(deleteRing);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-case-editView";
    }

    private void addOperation(Integer tempTCPid, List<SheetGateValveCaseJournal> targetJournals) {
        BaseTCP tempTCP = TCPDAO.get(SheetGateValveCaseTCP.class, tempTCPid);
        targetJournals.add(new SheetGateValveCaseJournal(tempTCP));
        targetJournals.sort(Comparator.comparingInt(BaseJournal::getPointId));
    }

    @PostMapping(value = "/action", params = "addOperationInput")
    public String addOperationInput(@ModelAttribute("entity") SheetGateValveCase entity, Model model) {
        if (entity.getInputTCPId() != null) {
            if (entity.getInputControlJournals() != null) {
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            else {
                List<SheetGateValveCaseJournal> entityJournals = new ArrayList<>();
                entity.setInputControlJournals(entityJournals);
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationInput")
    public String deleteOperationInput (@ModelAttribute("entity") SheetGateValveCase entity, Model model, @RequestParam String deleteOperationInput) {
        int i = Integer.parseInt(deleteOperationInput);
        SheetGateValveCaseJournal entityJournal = entity.getInputControlJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCaseJournal.class, entityJournal.getId());
        }
        entity.getInputControlJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "addOperationWeld")
    public String addOperationWeld(@ModelAttribute("entity") SheetGateValveCase entity, Model model) {
        if (entity.getWeldTCPId() != null) {
            if (entity.getWeldJournals() != null) {
                addOperation(entity.getWeldTCPId(), entity.getWeldJournals());
            }
            else {
                List<SheetGateValveCaseJournal> entityJournals = new ArrayList<>();
                entity.setWeldJournals(entityJournals);
                addOperation(entity.getWeldTCPId(), entity.getWeldJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationWeld")
    public String deleteOperationWeld (@ModelAttribute("entity") SheetGateValveCase entity, Model model, @RequestParam String deleteOperationWeld) {
        int i = Integer.parseInt(deleteOperationWeld);
        SheetGateValveCaseJournal entityJournal = entity.getWeldJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCaseJournal.class, entityJournal.getId());
        }
        entity.getWeldJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "addOperationMech")
    public String addOperationMech(@ModelAttribute("entity") SheetGateValveCase entity, Model model) {
        if (entity.getMechTCPId() != null) {
            if (entity.getMechanicalJournals() != null) {
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            else {
                List<SheetGateValveCaseJournal> entityJournals = new ArrayList<>();
                entity.setMechanicalJournals(entityJournals);
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationMech")
    public String deleteOperationMech (@ModelAttribute("entity") SheetGateValveCase entity, Model model, @RequestParam String deleteOperationMech) {
        int i = Integer.parseInt(deleteOperationMech);
        SheetGateValveCaseJournal entityJournal = entity.getMechanicalJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCaseJournal.class, entityJournal.getId());
        }
        entity.getMechanicalJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "addOperationDoc")
    public String addOperationDoc(@ModelAttribute("entity") SheetGateValveCase entity, Model model) {
        if (entity.getDocTCPId() != null) {
            if (entity.getDocumentJournals() != null) {
                addOperation(entity.getDocTCPId(), entity.getDocumentJournals());
            }
            else {
                List<SheetGateValveCaseJournal> entityJournals = new ArrayList<>();
                entity.setDocumentJournals(entityJournals);
                addOperation(entity.getDocTCPId(), entity.getDocumentJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationDoc")
    public String deleteOperationDoc (@ModelAttribute("entity") SheetGateValveCase entity, Model model, @RequestParam String deleteOperationDoc) {
        int i = Integer.parseInt(deleteOperationDoc);
        SheetGateValveCaseJournal entityJournal = entity.getDocumentJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SheetGateValveCaseJournal.class, entityJournal.getId());
        }
        entity.getDocumentJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "unit/assembly/gate-valve-case-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") SheetGateValveCase entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "unit/assembly/gate-valve-case-editView";
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
        if (entity.getRings() != null) {
            for(Ring ring : entity.getRings()) {
                if (ring.getStatus().equals("НЕ СООТВ.")) {
                    flag = false;
                    parameter = "badRing";
                }
            }
        }
        if (entities == null)
            entities = getAll();
        for(SheetGateValveCase tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Корпус с таким характеристиками уже существует!", model);
            }
        }
        entity.setEntityJournals(
                Stream.of(entity.getInputControlJournals(), entity.getWeldJournals(),
                                entity.getMechanicalJournals(), entity.getDocumentJournals())
                                                                                    .flatMap(Collection::stream)
                                                                                        .collect(Collectors.toList()));
        if (entity.getEntityJournals() != null) {
            for (SheetGateValveCaseJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 53 && journal.getDate() != null) {
                    if (entity.getCaseBottom() == null) {
                        return sendMessage("Не выбрано днище!", model);
                    }
                }
                if (journal.getPointId() == 52 && journal.getDate() != null) {
                    if (entity.getFlange() == null) {
                        return sendMessage("Не выбран фланец!", model);
                    }
                }
                if (journal.getPointId() == 171 && journal.getDate() != null) {
                    if (entity.getCoverSleeve008() == null) {
                        return sendMessage("Не выбрана дренажная втулка!", model);
                    }
                }
                if ((journal.getPointId() == 150 || journal.getPointId() == 151) && journal.getDate() != null) {
                    if (entity.getRings() == null) {
                        return sendMessage("Не выбраны кольца!", model);
                    }
                }
                if (journal.getPointId() == 62 && journal.getDate() != null) {
                    if (entity.getDrawing().isBlank()) {
                        return sendMessage("Не введен чертеж!", model);
                    }
                    if (entity.getPn().isBlank()) {
                        return sendMessage("Не выбрано давление!", model);
                    }
                }
                if (journal.getPointId() == 161 && journal.getDate() != null) {
                    entity.setDateOfWashing(journal.getDate());
                }
                if (journal.getPointId() == 59 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag1 = true;
                }
                if (journal.getPointId() == 62 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag2 = true;
                }
                if (journal.getPointId() == 61 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag3 = true;
                }
                if (journal.getPointId() == 161 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag4 = true;
                }
                if (journal.getPointId() == 54 && journal.getDate() != null) {
                    WeldingProcedure AF = weldingProcedureDAO.get(1);
                    if(AF.getLastControl() != null) {
                        if (journal.getDate().after(AF.getLastControl())) {
                            AF.getPeriodicalJournals().add(new WeldingProcedureJournal(AF, journal));
                            AF.setLastControl(journal.getDate());
                            AF.setNextControl(new Date((long)(journal.getDate().getTime() + 7*864*Math.pow(10,5))));
                            weldingProcedureDAO.saveOrUpdate(AF);
                        }
                    }
                }
                if (journal.getPointId() == 55 && journal.getDate() != null) {
                    WeldingProcedure AF = weldingProcedureDAO.get(1);
                    if(AF.getLastControl() != null) {
                        if (journal.getDate().after(AF.getLastControl())) {
                            AF.getPeriodicalJournals().add(new WeldingProcedureJournal(AF, journal));
                            AF.setLastControl(journal.getDate());
                            AF.setNextControl(new Date((long)(journal.getDate().getTime() + 7*864*Math.pow(10,5))));
                            weldingProcedureDAO.saveOrUpdate(AF);
                        }
                    }
                }
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if(entity.getRings() != null) {
            List<Ring> tempRingsList = new ArrayList<>();
            for(Ring ring : entity.getRings()) {
                Ring tempRing = ringDAO.get(Ring.class, ring.getId());
                tempRing.setSheetGateValveCase(entity);
                tempRingsList.add(tempRing);
            }
            entity.setRings(tempRingsList);
        }
        if (flag) {
            entity.setStatus("Cоотв.");
            if (assemblyFlag1 && assemblyFlag2 && assemblyFlag3 && assemblyFlag4)
                entity.setStatus("Готово к сборке");
        }
        else
            entity.setStatus("НЕ СООТВ.");
        if(entityDAO.saveOrUpdate(entity)) {
            if (entity.getReqId() != null)
                return "redirect:/entity/SheetGateValves/showFormForUpdate/" + entity.getReqId();
            else
                return "redirect:/entity/SheetGateValveCases/showAll?parameter=" + parameter;
        }
        else
            return sendMessage("Не удается сохранить объект! Что-то пошло не так, сообщите об ошибке!", model);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "unit/assembly/gate-valve-case-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditSheetGateValveCases", true);
        model.addAttribute("assemblyClass", "SheetGateValveCases");
        model.addAttribute("title", "Характеристики корпуса");
        model.addAttribute("editMode", true);
        model.addAttribute("drawings", drawings);
        model.addAttribute("coverSleeves008", coverSleeves008);
        model.addAttribute("caseBottoms", caseBottoms);
        model.addAttribute("flanges", flanges);
        model.addAttribute("pids", pids);
        model.addAttribute("rings", rings);
        model.addAttribute("dns", dns);
        model.addAttribute("pns", pns);
        model.addAttribute("inputControlTCP", inputControlTCP);
        model.addAttribute("weldTCP", weldTCP);
        model.addAttribute("mechanicalTCP", mechanicalTCP);
        model.addAttribute("documentTCP", documentTCP);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number) {
        SheetGateValveCase entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/SheetGateValveCases/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(SheetGateValveCase entity, String newNumber) {
        SheetGateValveCase entityNew = new SheetGateValveCase(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (SheetGateValveCaseJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new SheetGateValveCaseJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new SheetGateValveCaseJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        SheetGateValveCase entity = entityDAO.get(SheetGateValveCase.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(SheetGateValveCase.class, id);
        return "redirect:/entity/SheetGateValveCases/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SheetGateValveCase> getAll() {
        entities = entityDAO.getAll(SheetGateValveCase.class);
        return entities;
    }
}
