package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.RunningSleeveDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.RunningSleeveJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.RunningSleeveTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.RunningSleeve;
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
@RequestMapping("/entity/RunningSleeves")
public class RunningSleeveController extends BaseUnitControllerWithZk<RunningSleeve> {

    private final RunningSleeveDAO entityDAO;
    private final BaseEntityDAO<RunningSleeveJournal> baseJournalDAO;
    private final BaseTCPDAO<RunningSleeveTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<RunningSleeveTCP> entityTCPs;
    private List<String> dns;

    private List<RunningSleeve> entities;
    private String parameter;

    @Autowired
    public RunningSleeveController(HttpSession session,
                                 JournalNumberDAO journalNumberDAO,
                                 RunningSleeveDAO entityDAO,
                                 BaseEntityDAO<RunningSleeveJournal> baseJournalDAO,
                                 BaseTCPDAO<RunningSleeveTCP> TCPDAO,
                                 InspectorDAO inspectorDAO,
                                 ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            if ("duplicate".equals(parameter)) {
                mv.addObject("message", "ВНИМАНИЕ: Втулка с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новая ходовая втулка");
        RunningSleeve entity = new RunningSleeve();
        entityTCPs = TCPDAO.getAll(RunningSleeveTCP.class);
        List<RunningSleeveJournal> entityJournals = new ArrayList<>();
        for (RunningSleeveTCP tcp : entityTCPs) {
            RunningSleeveJournal entityJournal = new RunningSleeveJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики ходовой втулки");
        RunningSleeve entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (RunningSleeveJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(RunningSleeveTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Втулка ходовая");
        mv.addObject("bugelClass", "RunningSleeves");
        mv.addObject("userClickBugels", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/bugel-editView");
        mv.addObject("title", title);
        mv.addObject("bugelClass", "RunningSleeves");
        mv.addObject("userClickEditBugels", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, RunningSleeve entity) {
        drawings = entityDAO.getDistinctDrawing();
        dns = serviceClass.getDns();
        mv.addObject("drawings", drawings);
        mv.addObject("dns", dns);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") RunningSleeve entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(RunningSleeveTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new RunningSleeveJournal(tempTCP));
            }
            else {
                List<RunningSleeveJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new RunningSleeveJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/bugel-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") RunningSleeve entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        RunningSleeveJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(RunningSleeveJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/bugel-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") RunningSleeve entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/bugel-editView";
        }

        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entities == null)
            entities = getAll();
        String resultDup = checkDuplicatesWithZK(entity, entities);
        if (resultDup.startsWith("ОШИБКА"))
            return sendMessage(resultDup, model);

        if (entity.getEntityJournals() != null) {
            for (RunningSleeveJournal journal: entity.getEntityJournals()) {
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
        return "redirect:/entity/RunningSleeves/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/bugel-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditBugels", true);
        model.addAttribute("title", "Характеристики ходовой втулки");
        model.addAttribute("bugelClass", "RunningSleeves");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("drawings", drawings);
        model.addAttribute("dns", dns);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        RunningSleeve entity = entityDAO.get(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/RunningSleeves/showAll?parameter=" + parameter;
    }

    @PostMapping("/multi-copy")
    public ModelAndView multiCopyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("quantity") String quantity){
        RunningSleeve entity = entityDAO.get(entityId.getId());
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

    private void newEntityInitialize(RunningSleeve entity, String newNumber) {
        RunningSleeve entityNew = new RunningSleeve(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (RunningSleeveJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new RunningSleeveJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new RunningSleeveJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        RunningSleeve entity = entityDAO.get(RunningSleeve.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(RunningSleeve.class, id);
        return "redirect:/entity/RunningSleeves/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<RunningSleeve> getAll() {
        entities = entityDAO.getAll(RunningSleeve.class);
        return entities;
    }
}
