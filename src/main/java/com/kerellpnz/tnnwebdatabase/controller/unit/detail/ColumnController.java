package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.ColumnDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.ColumnJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.ColumnTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Column;
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
@RequestMapping("/entity/Columns")
public class ColumnController extends BaseEntityController {

    private final ColumnDAO entityDAO;
    private final BaseEntityDAO<ColumnJournal> baseJournalDAO;
    private final BaseTCPDAO<ColumnTCP> TCPDAO;
    private final BaseEntityDAO<RunningSleeve> runningSleeveDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<ColumnTCP> entityTCPs;
    private List<RunningSleeve> runningSleeves;
    private List<String> dns;

    private List<Column> entities;
    private String parameter;

    @Autowired
    public ColumnController(HttpSession session,
                                 JournalNumberDAO journalNumberDAO,
                                 ColumnDAO entityDAO,
                                 BaseEntityDAO<ColumnJournal> baseJournalDAO,
                                 BaseTCPDAO<ColumnTCP> TCPDAO,
                                 BaseEntityDAO<RunningSleeve> runningSleeveDAO,
                                 InspectorDAO inspectorDAO,
                                 ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.runningSleeveDAO = runningSleeveDAO;
        this.inspectorDAO = inspectorDAO;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            switch (parameter) {
                case "badSleeve" -> mv.addObject("message", "ВНИМАНИЕ: Выбранная втулка имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к стойке.");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Стойка с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новая стойка","userClickEditBugels");
        Column entity = new Column();
        entityTCPs = TCPDAO.getAll(ColumnTCP.class);
        List<ColumnJournal> entityJournals = new ArrayList<>();
        for (ColumnTCP tcp : entityTCPs) {
            ColumnJournal entityJournal = new ColumnJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        Column entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId) throws EntityNotFoundException {
        Column entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(Column entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики стойки","userClickEditDetails");
        for (ColumnJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(ColumnTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Стойка");
        mv.addObject("bugelClass", "Columns");
        mv.addObject("userClickBugels", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title, String pagePart) {
        ModelAndView mv = new ModelAndView("./unit/detail/bugel-editView");
        mv.addObject("title", title);
        mv.addObject("bugelClass", "Columns");
        mv.addObject(pagePart, true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, Column entity) {
        runningSleeves = runningSleeveDAO.getAll(RunningSleeve.class);
        drawings = entityDAO.getDistinctDrawing();
        dns = serviceClass.getDns();
        mv.addObject("runningSleeves", runningSleeves);
        mv.addObject("drawings", drawings);
        mv.addObject("dns", dns);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        return mv;
    }

    @PostMapping(value = "/action", params = "openRunningSleeve")
    public String openRunningSleeve(@ModelAttribute("entity") Column entity, Model model) {
        if (entity.getRunningSleeve() != null) {
                return "redirect:/entity/RunningSleeves/showFormForUpdate/" + entity.getRunningSleeve().getId();
        }
        modelInitialize(model);
        return "./unit/detail/bugel-editView";
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") Column entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(ColumnTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new ColumnJournal(tempTCP));
            }
            else {
                List<ColumnJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new ColumnJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/bugel-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") Column entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        ColumnJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(ColumnJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/bugel-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") Column entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/bugel-editView";
        }

        boolean flag = true;
        boolean isEmpty = true;
        boolean assemblyFlag = false;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entity.getRunningSleeve() != null) {
            if (entity.getRunningSleeve().getStatus().equals("НЕ СООТВ.")) {
                flag = false;
                parameter = "badSleeve";
            }
        }

        if (entities == null)
            entities = getAll();
        for(Column tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Стойка с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (ColumnJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 90 && journal.getDate() != null && journal.getStatus().equals("Соответствует"))
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
            if (assemblyFlag && entity.getRunningSleeve() != null)
                entity.setStatus("Готово к сборке");
        }
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        if (entity.getReqId() != null) {
            return "redirect:/entity/SheetGateValveCovers/showFormForUpdate/" + entity.getReqId();
        }
        else
            return "redirect:/entity/Columns/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/bugel-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditBugels", true);
        model.addAttribute("title", "Характеристики стойки");
        model.addAttribute("bugelClass", "Columns");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("runningSleeves", runningSleeves);
        model.addAttribute("drawings", drawings);
        model.addAttribute("dns", dns);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        Column entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/Columns/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(Column entity, String newNumber) {
        Column entityNew = new Column(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (ColumnJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new ColumnJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new ColumnJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Column entity = entityDAO.get(Column.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(Column.class, id);
        return "redirect:/entity/Columns/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Column> getAll() {
        entities = entityDAO.getAll(Column.class);
        return entities;
    }
}
