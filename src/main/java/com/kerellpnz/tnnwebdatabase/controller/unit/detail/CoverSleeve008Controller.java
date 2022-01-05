package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.CoverSleeve008DAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.CoverSleeve008Journal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.CoverSleeve008TCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve008;
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
@RequestMapping("/entity/CoverSleeves008")
public class CoverSleeve008Controller extends BaseUnitControllerWithZk<CoverSleeve008> {

    private final CoverSleeve008DAO entityDAO;
    private final BaseEntityDAO<CoverSleeve008Journal> baseJournalDAO;
    private final BaseTCPDAO<CoverSleeve008TCP> TCPDAO;
    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<CoverSleeve008TCP> entityTCPs;
    private List<MetalMaterial> metalMaterials;
    private List<String> dns;

    private List<CoverSleeve008> entities;
    private String parameter;

    @Autowired
    public CoverSleeve008Controller(HttpSession session,
                                 JournalNumberDAO journalNumberDAO,
                                 CoverSleeve008DAO entityDAO,
                                 BaseEntityDAO<CoverSleeve008Journal> baseJournalDAO,
                                 BaseTCPDAO<CoverSleeve008TCP> TCPDAO,
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
                        " поэтому этот же статус применен к втулке.");
                case "error" -> mv.addObject("message", "Номер втулки не является числом!");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Втулка с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новая дренажная втулка");
        CoverSleeve008 entity = new CoverSleeve008();
        entityTCPs = TCPDAO.getAll(CoverSleeve008TCP.class);
        List<CoverSleeve008Journal> entityJournals = new ArrayList<>();
        for (CoverSleeve008TCP tcp : entityTCPs) {
            CoverSleeve008Journal entityJournal = new CoverSleeve008Journal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        CoverSleeve008 entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}/{name}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId, @PathVariable String name) throws EntityNotFoundException {
        CoverSleeve008 entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        entity.setReqName(name);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(CoverSleeve008 entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики дренажной втулки");
        for (CoverSleeve008Journal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(CoverSleeve008TCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Втулка дренажная");
        mv.addObject("commonDetailClass", "CoverSleeves008");
        mv.addObject("userClickCommonDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/common-details-editView");
        mv.addObject("title", title);
        mv.addObject("commonDetailClass", "CoverSleeves008");
        mv.addObject("userClickEditCommonDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, CoverSleeve008 entity) {
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
    public String openMaterial(@ModelAttribute("entity") CoverSleeve008 entity, Model model) {
        if (entity.getMetalMaterial() != null) {
            if (entity.getMetalMaterial() instanceof SheetMaterial)
                return "redirect:/entity/SheetMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
            else
                return "redirect:/entity/RolledMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
        }
        modelInitialize(model);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") CoverSleeve008 entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(CoverSleeve008TCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new CoverSleeve008Journal(tempTCP));
            }
            else {
                List<CoverSleeve008Journal> entityJournals = new ArrayList<>();
                entityJournals.add(new CoverSleeve008Journal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") CoverSleeve008 entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        CoverSleeve008Journal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(CoverSleeve008Journal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") CoverSleeve008 entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/common-details-editView";
        }

        boolean flag = true;
        boolean isEmpty = true;
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
            for (CoverSleeve008Journal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if (flag)
            entity.setStatus("Cоотв.");
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        if (entity.getReqId() != null) {
            if (entity.getReqName().equals("case"))
                return "redirect:/entity/SheetGateValveCases/showFormForUpdate/" + entity.getReqId();
            else
                return "redirect:/entity/SheetGateValveCovers/showFormForUpdate/" + entity.getReqId();
        }
        else
            return "redirect:/entity/CoverSleeves008/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/common-details-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditCommonDetails", true);
        model.addAttribute("title", "Характеристики дренажной втулки");
        model.addAttribute("commonDetailClass", "CoverSleeves008");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("metalMaterials", metalMaterials);
        model.addAttribute("drawings", drawings);
        model.addAttribute("dns", dns);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        CoverSleeve008 entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/CoverSleeves008/showAll?parameter=" + parameter;
    }

    @PostMapping("/multi-copy")
    public ModelAndView multiCopyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("quantity") String quantity){
        CoverSleeve008 entity = entityDAO.getForCopy(entityId.getId());
        if (!entity.getNumber().matches("\\d+")) {
            return showAll("error");
        }
        Integer number = Integer.parseInt(entity.getNumber());
        parameter = "success";
        for (int i = 0; i < Integer.parseInt(quantity); i++) {
            String newNumber = (++number).toString();
            newEntityInitialize(entity, newNumber);
        }
        return showAll(parameter);
    }

    private void newEntityInitialize(CoverSleeve008 entity, String newNumber) {
        CoverSleeve008 entityNew = new CoverSleeve008(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (CoverSleeve008Journal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new CoverSleeve008Journal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new CoverSleeve008Journal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        CoverSleeve008 entity = entityDAO.get(CoverSleeve008.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(CoverSleeve008.class, id);
        return "redirect:/entity/CoverSleeves008/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<CoverSleeve008> getAll() {
        entities = entityDAO.getAll(CoverSleeve008.class);
        return entities;
    }
}
