package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.SpringDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.SpringJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.SpringTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseValveWithSpring;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spring;
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
@RequestMapping("/entity/Springs")
public class SpringController extends BaseEntityController {

    private final SpringDAO entityDAO;
    private final BaseEntityDAO<SpringJournal> baseJournalDAO;
    private final BaseEntityDAO<SpringTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> drawings;
    private List<SpringTCP> entityTCPs;

    private List<Spring> entities;
    private String parameter;

    @Autowired
    public SpringController(HttpSession session,
                                          JournalNumberDAO journalNumberDAO,
                                          SpringDAO entityDAO,
                                          BaseEntityDAO<SpringJournal> baseJournalDAO,
                                          BaseEntityDAO<SpringTCP> TCPDAO,
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
                mv.addObject("message", "ВНИМАНИЕ: Пружины с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новые пружины");
        Spring entity = new Spring();
        entityTCPs = TCPDAO.getAll(SpringTCP.class);
        List<SpringJournal> entityJournals = new ArrayList<>();
        for (SpringTCP tcp : entityTCPs) {
            SpringJournal entityJournal = new SpringJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики пружин");
        Spring entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (SpringJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        int amountUsed = 0;
        if (entity.getBaseValveWithSprings() != null) {
            for(BaseValveWithSpring withSpring : entity.getBaseValveWithSprings()) {
                amountUsed += withSpring.getSpringAmount();
            }
        }
        entity.setAmountRemaining(entity.getAmount() - amountUsed);
        entityTCPs = TCPDAO.getAll(SpringTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Пружины");
        mv.addObject("sealingClass", "Springs");
        mv.addObject("userClickSealings", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/sealing-editView");
        mv.addObject("title", title);
        mv.addObject("sealingClass", "Springs");
        mv.addObject("userClickEditSealings", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, Spring entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        drawings = entityDAO.getDistinctDrawing();
        mv.addObject("drawings", drawings);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSheetGateValve")
    public String openSheetGateValve(@RequestParam("valveId") int valveId) {
        return "redirect:/entity/SheetGateValves/showFormForUpdate/" + valveId;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") Spring entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(SpringTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new SpringJournal(tempTCP));
            }
            else {
                List<SpringJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new SpringJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/sealing-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") Spring entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        SpringJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SpringJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/sealing-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") Spring entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/sealing-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entities == null)
            entities = getAll();
        for(Spring tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Пружины с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (SpringJournal journal: entity.getEntityJournals()) {
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
        else
            entity.setAmountRemaining(entity.getAmount());
        if (flag)
            entity.setStatus("Cоотв.");
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        return "redirect:/entity/Springs/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        Spring entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/Springs/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(Spring entity, String newNumber) {
        Spring entityNew = new Spring(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (SpringJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new SpringJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new SpringJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/sealing-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditSealings", true);
        model.addAttribute("sealingClass", "Springs");
        model.addAttribute("title", "Характеристики пружин");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("drawings", drawings);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Spring entity = entityDAO.get(Spring.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(Spring.class, id);
        return "redirect:/entity/Springs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Spring> getAll() {
        entities = entityDAO.getAll(Spring.class);
        return entities;
    }
}
