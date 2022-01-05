package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.GateDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.GateJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.GateTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Gate;
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
import java.util.List;

@Controller
@RequestMapping("/entity/Gates")
public class GateController extends BaseUnitControllerWithZk<Gate> {

    private final GateDAO entityDAO;
    private final BaseEntityDAO<GateJournal> baseJournalDAO;
    private final BaseTCPDAO<GateTCP> TCPDAO;
    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<GateTCP> entityTCPs;
    private List<MetalMaterial> metalMaterials;
    private List<String> dns;

    private List<Gate> entities;
    private String parameter;

    @Autowired
    public GateController(HttpSession session,
                                 JournalNumberDAO journalNumberDAO,
                                 GateDAO entityDAO,
                                 BaseEntityDAO<GateJournal> baseJournalDAO,
                                 BaseTCPDAO<GateTCP> TCPDAO,
                                 BaseEntityDAO<MetalMaterial> metalMaterialDAO,
                                 InspectorDAO inspectorDAO,
                                 ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
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
                        " поэтому этот же статус применен к шиберу.");
                case "error" -> mv.addObject("message", "Номер шибера не является числом!");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Шибер с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новый шибер");
        Gate entity = new Gate();
        entityTCPs = TCPDAO.getTCPbyOperationType(GateTCP.class, "Обработка");
        List<GateJournal> entityJournals = new ArrayList<>();
        for (GateTCP tcp : entityTCPs) {
            GateJournal entityJournal = new GateJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        Gate entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId) throws EntityNotFoundException {
        Gate entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(Gate entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики шибера");
        List<GateJournal> newEntityJournals = new ArrayList<>();
        for(GateJournal entityJournal: entity.getEntityJournals()) {
            String operationType = TCPDAO.getOperationTypeById(GateTCP.class, entityJournal.getPointId());
            if (operationType.equals("Обработка")) {
                newEntityJournals.add(entityJournal);
            }
        }
        entity.setEntityJournals(newEntityJournals);
        for (GateJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getTCPbyOperationType(GateTCP.class, "Обработка");
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Шибер");
        mv.addObject("detailClass", "Gates");
        mv.addObject("userClickDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/detail-editView");
        mv.addObject("title", title);
        mv.addObject("detailClass", "Gates");
        mv.addObject("userClickEditDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, Gate entity) {
        metalMaterials = metalMaterialDAO.getAll(MetalMaterial.class);
        drawings = entityDAO.getDistinctDrawing();
        dns = serviceClass.getDns();
        mv.addObject("metalMaterials", metalMaterials);
        mv.addObject("drawings", drawings);
        mv.addObject("dns", dns);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        return mv;
    }

    @PostMapping(value = "/action", params = "openMaterial")
    public String openMaterial(@ModelAttribute("entity") Gate entity, Model model) {
        if (entity.getMetalMaterial() != null) {
            if (entity.getMetalMaterial() instanceof SheetMaterial)
                return "redirect:/entity/SheetMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
            else
                return "redirect:/entity/RolledMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
        }
        modelInitialize(model);
        return "./unit/detail/detail-editView";
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") Gate entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(GateTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new GateJournal(tempTCP));
            }
            else {
                List<GateJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new GateJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/detail-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") Gate entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        GateJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(GateJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/detail-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") Gate entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/detail-editView";
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

        if (entity.getEntityJournals() != null) {
            for (GateJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 42 && journal.getDate() != null && journal.getStatus().equals("Соответствует"))
                {
                    assemblyFlag = true;
                }
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
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
            return "redirect:/entity/Gates/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/detail-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditDetails", true);
        model.addAttribute("title", "Характеристики шибера");
        model.addAttribute("detailClass", "Gates");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("metalMaterials", metalMaterials);
        model.addAttribute("drawings", drawings);
        model.addAttribute("dns", dns);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        Gate entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/Gates/showAll?parameter=" + parameter;
    }

    @PostMapping("/multi-copy")
    public ModelAndView multiCopyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("quantity") String quantity){
        Gate entity = entityDAO.getForCopy(entityId.getId());
        if (!entity.getNumber().matches("\\d+")) {
            return showAll("error");
        }
        Integer number = Integer.parseInt(entity.getNumber());
        for (int i = 0; i < Integer.parseInt(quantity); i++) {
            String newNumber = (++number).toString();
            newEntityInitialize(entity, newNumber);
        }
        return showAll(parameter);
    }

    private void newEntityInitialize(Gate entity, String newNumber) {
        Gate entityNew = new Gate(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (GateJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new GateJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new GateJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Gate entity = entityDAO.get(Gate.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(Gate.class, id);
        return "redirect:/entity/Gates/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Gate> getAll() {
        entities = entityDAO.getAll(Gate.class);
        return entities;
    }
}
