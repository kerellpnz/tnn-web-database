package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.NozzleDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.PIDDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.NozzleJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.NozzleTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Nozzle;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.SheetMaterial;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/entity/Nozzles")
public class NozzleController extends BaseUnitControllerWithZk<Nozzle> {

    private final NozzleDAO entityDAO;
    private final BaseEntityDAO<SheetGateValve> sheetGateValveDAO;
    private final BaseEntityDAO<NozzleJournal> baseJournalDAO;
    private final BaseTCPDAO<NozzleTCP> TCPDAO;
    private final PIDDAO pidDAO;
    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<String> tensileStrengths;
    private List<String> groovings;
    private List<NozzleTCP> entityTCPs;
    private List<MetalMaterial> metalMaterials;
    private List<PID> pids;
    private List<String> dns;
    private List<String> pns;
    private List<NozzleTCP> inputControlTCP;
    private List<NozzleTCP> mechanicalTCP;

    private List<Nozzle> entities;
    private String parameter;

    @Autowired
    public NozzleController(HttpSession session,
                            JournalNumberDAO journalNumberDAO,
                            NozzleDAO entityDAO,
                            BaseEntityDAO<SheetGateValve> sheetGateValveDAO,
                            BaseEntityDAO<NozzleJournal> baseJournalDAO,
                            BaseTCPDAO<NozzleTCP> TCPDAO,
                            PIDDAO pidDAO,
                            BaseEntityDAO<MetalMaterial> metalMaterialDAO,
                            InspectorDAO inspectorDAO,
                            ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.sheetGateValveDAO = sheetGateValveDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.pidDAO = pidDAO;
        this.metalMaterialDAO = metalMaterialDAO;
        this.inspectorDAO = inspectorDAO;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            switch (parameter) {
                case "badMetal" -> mv.addObject("message", "ВНИМАНИЕ: Выбранный прокат имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к катушке.");
                case "error" -> mv.addObject("message", "Номер катушки не является числом!");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Катушка с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новая катушка");
        Nozzle entity = new Nozzle();
        entityTCPs = TCPDAO.getAll(NozzleTCP.class);
        List<NozzleJournal> entityJournals = new ArrayList<>();
        for (NozzleTCP tcp : entityTCPs) {
            NozzleJournal entityJournal = new NozzleJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        Nozzle entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId) throws EntityNotFoundException {
        Nozzle entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(Nozzle entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики катушки");
        for (NozzleJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(NozzleTCP.class);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    private void TCPInitialize(List<NozzleTCP> entityTCPs) {
        inputControlTCP = new ArrayList<>();
        mechanicalTCP = new ArrayList<>();
        if (entityTCPs != null) {
            for(NozzleTCP tcp : entityTCPs) {
                switch (tcp.getOperationType()) {
                    case "Входной контроль" -> inputControlTCP.add(tcp);
                    case "Обработка" -> mechanicalTCP.add(tcp);
                }
            }
        }
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("assemblyDetail", "Nozzles");
        mv.addObject("title", "Катушка");
        mv.addObject("userClickAssemblyDetail", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/assembly-detail-editView");
        mv.addObject("assemblyDetail", "Nozzles");
        mv.addObject("title", title);
        mv.addObject("userClickEditAssemblyDetail", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, Nozzle entity) {
        metalMaterials = metalMaterialDAO.getAll(MetalMaterial.class);
        drawings = entityDAO.getDistinctDrawing();
        tensileStrengths = entityDAO.getDistinctTensileStrength();
        groovings = entityDAO.getDistinctGrooving();
        pids = pidDAO.getAll(PID.class);
        dns = serviceClass.getDns();
        pns = serviceClass.getPns();
        mv.addObject("metalMaterials", metalMaterials);
        mv.addObject("drawings", drawings);
        mv.addObject("tensileStrengths", tensileStrengths);
        mv.addObject("groovings", groovings);
        mv.addObject("pids", pids);
        mv.addObject("dns", dns);
        mv.addObject("pns", pns);
        mv.addObject("inputControlTCP", inputControlTCP);
        mv.addObject("mechanicalTCP", mechanicalTCP);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);

        if (entity.getEntityJournals() != null) {
            for(NozzleJournal journal : entity.getEntityJournals()) {
                String operationType = TCPDAO.getOperationTypeById(NozzleTCP.class, journal.getPointId());
                switch (operationType) {
                    case "Входной контроль" -> entity.getInputControlJournals().add(journal);
                    case "Обработка" -> entity.getMechanicalJournals().add(journal);
                }
            }
            if (entity.getInputControlJournals() != null) entity.getInputControlJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
            if (entity.getMechanicalJournals() != null) entity.getMechanicalJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
        }

        return mv;
    }

    @PostMapping(value = "/action", params = "openPID")
    public String openPID(@ModelAttribute("entity") Nozzle entity, Model model) {
        if (entity.getPid() != null) {
            if (entity.getId() != 0)
                return "redirect:/Specifications/showFormForUpdate/" + entity.getPid().getId()+ "/" + entity.getId() + "/nozzle";
            else
                return "redirect:/Specifications/showFormForUpdate/" + entity.getPid().getId();
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "openMaterial")
    public String openMaterial(@ModelAttribute("entity") Nozzle entity, Model model) {
        if (entity.getMetalMaterial() != null) {
            if (entity.getMetalMaterial() instanceof SheetMaterial)
                return "redirect:/entity/SheetMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
            else
                return "redirect:/entity/RolledMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    private void addOperation(Integer tempTCPid, List<NozzleJournal> targetJournals) {
        BaseTCP tempTCP = TCPDAO.get(NozzleTCP.class, tempTCPid);
        targetJournals.add(new NozzleJournal(tempTCP));
        targetJournals.sort(Comparator.comparingInt(BaseJournal::getPointId));
    }

    @PostMapping(value = "/action", params = "addOperationInput")
    public String addOperationInput(@ModelAttribute("entity") Nozzle entity, Model model) {
        if (entity.getInputTCPId() != null) {
            if (entity.getInputControlJournals() != null) {
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            else {
                List<NozzleJournal> entityJournals = new ArrayList<>();
                entity.setInputControlJournals(entityJournals);
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationInput")
    public String deleteOperationInput (@ModelAttribute("entity") Nozzle entity, Model model, @RequestParam String deleteOperationInput) {
        int i = Integer.parseInt(deleteOperationInput);
        NozzleJournal entityJournal = entity.getInputControlJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(NozzleJournal.class, entityJournal.getId());
        }
        entity.getInputControlJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "addOperationMech")
    public String addOperationMech(@ModelAttribute("entity") Nozzle entity, Model model) {
        if (entity.getMechTCPId() != null) {
            if (entity.getMechanicalJournals() != null) {
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            else {
                List<NozzleJournal> entityJournals = new ArrayList<>();
                entity.setMechanicalJournals(entityJournals);
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationMech")
    public String deleteOperationMech (@ModelAttribute("entity") Nozzle entity, Model model, @RequestParam String deleteOperationMech) {
        int i = Integer.parseInt(deleteOperationMech);
        NozzleJournal entityJournal = entity.getMechanicalJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(NozzleJournal.class, entityJournal.getId());
        }
        entity.getMechanicalJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") Nozzle entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/assembly-detail-editView";
        }

        boolean flag = true;
        boolean isEmpty = true;
        boolean assemblyFlag = false;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entity.getMetalMaterial() != null) {
            if (!checkMaterial(entity, entity.getMetalMaterial())) {
                flag = false;
                parameter = "badMetal";
            }
        }
        else {
            if(entity.getMaterial().isBlank() || entity.getMelt().isBlank()) {
                return sendMessage("Введите плавку/материал!", model);
            }
        }

        if (entities == null)
            entities = getAll();
        String resultDup = checkDuplicatesWithZK(entity, entities);
        if (resultDup.startsWith("ОШИБКА"))
            return sendMessage(resultDup, model);

        entity.setEntityJournals(
                Stream.of(entity.getInputControlJournals(),
                                entity.getMechanicalJournals())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));

        if (entity.getEntityJournals() != null) {
            for (NozzleJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 95 && journal.getDate() != null) {
                    if (entity.getPn().isBlank()) {
                        return sendMessage("Не выбрано давление!", model);
                    }
                    if (entity.getTensileStrength().isBlank()) {
                        return sendMessage("Не введено временное сопротивление!", model);
                    }
                    if (entity.getGrooving().isBlank()) {
                        return sendMessage("Не введена разделка!", model);
                    }
                }
                if (journal.getPointId() == 95 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag = true;
                }
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if(entity.getSheetGateValve() != null) {
            SheetGateValve sheetGateValve = sheetGateValveDAO.get(SheetGateValve.class,
                    entity.getSheetGateValve().getId());
            entity.setSheetGateValve(sheetGateValve);
        }
        if (flag) {
            entity.setStatus("Cоотв.");
            if (assemblyFlag)
                entity.setStatus("Готово к сборке");
        }
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        if (entity.getReqId() != null)
            return "redirect:/entity/SheetGateValves/showFormForUpdate/" + entity.getReqId();
        else
            return "redirect:/entity/Nozzles/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/assembly-detail-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditAssemblyDetail", true);
        model.addAttribute("title", "Характеристики катушки");
        model.addAttribute("editMode", true);
        model.addAttribute("assemblyDetail", "Nozzles");
        model.addAttribute("metalMaterials", metalMaterials);
        model.addAttribute("drawings", drawings);
        model.addAttribute("tensileStrengths", tensileStrengths);
        model.addAttribute("groovings", groovings);
        model.addAttribute("pids", pids);
        model.addAttribute("dns", dns);
        model.addAttribute("pns", pns);
        model.addAttribute("inputControlTCP", inputControlTCP);
        model.addAttribute("mechanicalTCP", mechanicalTCP);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        Nozzle entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/Nozzles/showAll?parameter=" + parameter;
    }

    @PostMapping("/multi-copy")
    public ModelAndView multiCopyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("quantity") Integer quantity){
        Nozzle entity = entityDAO.getForCopy(entityId.getId());
        if (!entity.getNumber().matches("\\d+")) {
            return showAll("error");
        }
        parameter = "success";
        Integer number = Integer.parseInt(entity.getNumber());
        for (int i = 0; i < quantity; i++) {
            String newNumber = (++number).toString();
            newEntityInitialize(entity, newNumber);
        }
        return showAll(parameter);
    }

    private void newEntityInitialize(Nozzle entity, String newNumber) {
        Nozzle entityNew = new Nozzle(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (NozzleJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new NozzleJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new NozzleJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Nozzle entity = entityDAO.get(Nozzle.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(Nozzle.class, id);
        return "redirect:/entity/Nozzles/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Nozzle> getAll() {
        entities = entityDAO.getAll(Nozzle.class);
        return entities;
    }
}
