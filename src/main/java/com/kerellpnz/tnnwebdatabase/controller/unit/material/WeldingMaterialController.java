package com.kerellpnz.tnnwebdatabase.controller.unit.material;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.material.WeldingMaterialDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.WeldingMaterialJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.material.WeldingMaterialTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.WeldingMaterial;
import com.kerellpnz.tnnwebdatabase.exception.EntityNotFoundException;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
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
@RequestMapping("/entity/WeldingMaterials")
public class WeldingMaterialController extends BaseEntityController {

    private final WeldingMaterialDAO entityDAO;
    private final BaseEntityDAO<WeldingMaterialJournal> baseJournalDAO;
    private final BaseEntityDAO<WeldingMaterialTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<WeldingMaterialTCP> entityTCPs;
    private List<String> names;

    private List<WeldingMaterial> entities;
    private String parameter;

    public WeldingMaterialController(HttpSession session,
                                 JournalNumberDAO journalNumberDAO,
                                 WeldingMaterialDAO entityDAO,
                                 BaseEntityDAO<WeldingMaterialJournal> baseJournalDAO,
                                 BaseEntityDAO<WeldingMaterialTCP> TCPDAO,
                                 InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            if ("duplicate".equals(parameter)) {
                mv.addObject("message", "ВНИМАНИЕ: Материалы с такими характеристиками уже существуют!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новый сварочный материал","userClickEditWeldingMaterials");
        WeldingMaterial entity = new WeldingMaterial();
        entityTCPs = TCPDAO.getAll(WeldingMaterialTCP.class);
        List<WeldingMaterialJournal> entityJournals = new ArrayList<>();
        for (WeldingMaterialTCP tcp : entityTCPs) {
            WeldingMaterialJournal entityJournal = new WeldingMaterialJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Редактирование сварочного материала","userClickEditWeldingMaterials");
        WeldingMaterial entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (WeldingMaterialJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(WeldingMaterialTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Сварочные материалы");
        mv.addObject("userClickWeldingMaterials", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title, String pagePart) {
        ModelAndView mv = new ModelAndView("./unit/material/welding-material-editView");
        mv.addObject("title", title);
        mv.addObject(pagePart, true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, WeldingMaterial entity) {
        names = entityDAO.getDistinctName();
        mv.addObject("names", names);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") WeldingMaterial entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(WeldingMaterialTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new WeldingMaterialJournal(tempTCP));
            }
            else {
                List<WeldingMaterialJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new WeldingMaterialJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/material/welding-material-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") WeldingMaterial entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        WeldingMaterialJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(WeldingMaterialJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/material/welding-material-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") WeldingMaterial entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/material/welding-material-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

//        if (entities == null)
//            entities = getAll();
//        for(WeldingMaterial tempEntity : entities) {
//            if (entity.equals(tempEntity)) {
//                if (entity.getId() != tempEntity.getId())
//                    return sendMessage("ОШИБКА: Материалы с таким характеристиками уже существуют!", model);
//            }
//        }

        if (entity.getEntityJournals() != null) {
            for (WeldingMaterialJournal journal: entity.getEntityJournals()) {
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
        return "redirect:/entity/WeldingMaterials/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        WeldingMaterial entity = entityDAO.get(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/WeldingMaterials/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(WeldingMaterial entity, String newNumber) {
        WeldingMaterial entityNew = new WeldingMaterial(entity, newNumber);
//        if (entities.contains(entityNew)) {
//            parameter = "duplicate";
//            return;
//        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (WeldingMaterialJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new WeldingMaterialJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new WeldingMaterialJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/material/welding-material-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditWeldingMaterials", true);
        model.addAttribute("title", "Редактирование материала");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("names", names);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        WeldingMaterial entity = entityDAO.get(WeldingMaterial.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(WeldingMaterial.class, id);
        return "redirect:/entity/WeldingMaterials/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<WeldingMaterial> getAll() {
        entities = entityDAO.getAll(WeldingMaterial.class);
        return entities;
    }
}
