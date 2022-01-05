package com.kerellpnz.tnnwebdatabase.controller.unit.material;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.material.ControlWeldDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.ControlWeldJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.material.ControlWeldTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.ControlWeld;
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
@RequestMapping("/entity/ControlWelds")
public class ControlWeldController extends BaseEntityController {

    private final ControlWeldDAO entityDAO;
    private final BaseEntityDAO<ControlWeldJournal> baseJournalDAO;
    private final BaseEntityDAO<ControlWeldTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> weldingMethods;
    private List<String> welders;
    private List<String> firstMaterials;
    private List<String> secondMaterials;
    private List<String> stamps;

    private List<ControlWeldTCP> entityTCPs;


    public ControlWeldController(HttpSession session,
                                JournalNumberDAO journalNumberDAO,
                                 ControlWeldDAO entityDAO,
                                BaseEntityDAO<ControlWeldJournal> baseJournalDAO,
                                BaseEntityDAO<ControlWeldTCP> TCPDAO,
                                InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll() {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новый КСС");
        ControlWeld entity = new ControlWeld();
        entityTCPs = TCPDAO.getAll(ControlWeldTCP.class);
        List<ControlWeldJournal> entityJournals = new ArrayList<>();
        for (ControlWeldTCP tcp : entityTCPs) {
            ControlWeldJournal entityJournal = new ControlWeldJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Редактирование КСС");
        ControlWeld entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (ControlWeldJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(ControlWeldTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "КСС");
        mv.addObject("userClickControlWelds", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/material/control-weld-editView");
        mv.addObject("title", title);
        mv.addObject("userClickEditControlWelds", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, ControlWeld entity) {
        weldingMethods = entityDAO.getDistinctWeldingMethod();
        welders = entityDAO.getDistinctWelder();
        firstMaterials = entityDAO.getDistinctFirstMaterial();
        secondMaterials = entityDAO.getDistinctSecondMaterial();
        stamps = entityDAO.getDistinctStamp();
        mv.addObject("weldingMethods", weldingMethods);
        mv.addObject("welders", welders);
        mv.addObject("firstMaterials", firstMaterials);
        mv.addObject("secondMaterials", secondMaterials);
        mv.addObject("stamps", stamps);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") ControlWeld entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(ControlWeldTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new ControlWeldJournal(tempTCP));
            }
            else {
                List<ControlWeldJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new ControlWeldJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/material/control-weld-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") ControlWeld entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        ControlWeldJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(ControlWeldJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/material/control-weld-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") ControlWeld entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/material/control-weld-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        UserModel userModel = this.getUserModel();

        if (entity.getEntityJournals() != null) {
            for (ControlWeldJournal journal: entity.getEntityJournals()) {
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
        return "redirect:/entity/ControlWelds/showAll";
    }

//    @PostMapping("/single-copy")
//    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
//        ControlWeld entity = entityDAO.get(entityId.getId());
//        newEntityInitialize(entity, number);
//        return "redirect:/entity/ControlWelds/showAll";
//    }
//
//    private void newEntityInitialize(ControlWeld entity, String newNumber) {
//        ControlWeld entityNew = new ControlWeld(entity, newNumber);
//        entityNew.setEntityJournals(new ArrayList<>());
//        UserModel userModel = getUserModel();
//        if (entity.getEntityJournals() != null) {
//            for (ControlWeldJournal journal: entity.getEntityJournals()) {
//                if (journal.getDate() != null)
//                    entityNew.getEntityJournals().add(new ControlWeldJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
//                else
//                    entityNew.getEntityJournals().add(new ControlWeldJournal(journal, entityNew));
//            }
//        }
//        entityDAO.saveOrUpdate(entityNew);
//    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/material/control-weld-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditControlWelds", true);
        model.addAttribute("title", "Редактирование КСС");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("weldingMethods", weldingMethods);
        model.addAttribute("welders", welders);
        model.addAttribute("firstMaterials", firstMaterials);
        model.addAttribute("secondMaterials", secondMaterials);
        model.addAttribute("stamps", stamps);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        ControlWeld entity = entityDAO.get(ControlWeld.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(ControlWeld.class, id);
        return "redirect:/entity/ControlWelds/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ControlWeld> getAll() {
        return entityDAO.getAll(ControlWeld.class);
    }
}
