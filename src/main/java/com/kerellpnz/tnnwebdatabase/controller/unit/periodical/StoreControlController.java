package com.kerellpnz.tnnwebdatabase.controller.unit.periodical;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.StoreControlJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.periodical.StoresControlTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.TempPeriodicalUnit;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/entity/StoreControl")
public class StoreControlController extends BaseEntityController {

    private final BaseEntityDAO<StoreControlJournal> entityDAO;
    private final BaseEntityDAO<StoresControlTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<StoresControlTCP> entityTCPs;
    private Date lastDate;
    private Date nextDate;

    @Autowired
    public StoreControlController(HttpSession session,
                                   JournalNumberDAO journalNumberDAO,
                                   BaseEntityDAO<StoreControlJournal> entityDAO,
                                   BaseEntityDAO<StoresControlTCP> TCPDAO,
                                   InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll() {
        TempPeriodicalUnit<StoreControlJournal> entity = new TempPeriodicalUnit<>(entityDAO.getAll(StoreControlJournal.class));
        Date maxDate = new Date(0);
        for (StoreControlJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
            if (entityJournal.getDate() != null) {
                if (entityJournal.getDate().after(maxDate))
                    maxDate = entityJournal.getDate();
            }
        }
        lastDate = maxDate;
        nextDate = new Date((long)(maxDate.getTime() + 2628*Math.pow(10,6)));
        entityTCPs = TCPDAO.getAll(StoresControlTCP.class);
        ModelAndView mv = modelInitializeGeneral();
        mv.addObject("lastDate", lastDate);
        mv.addObject("nextDate", nextDate);
        mv.addObject("entity", entity);
        return mv;
    }

    private ModelAndView modelInitializeGeneral() {
        ModelAndView mv = new ModelAndView("./unit/periodical/non-entity-periodical-operView");
        mv.addObject("title", "Контроль складирования");
        mv.addObject("type", "Контроль складирования");
        mv.addObject("userClickEditNonEntityPeriodical", true);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("periodicalClass", "StoreControl");
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") TempPeriodicalUnit<StoreControlJournal> entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(StoresControlTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new StoreControlJournal(tempTCP));
            }
            else {
                List<StoreControlJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new StoreControlJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/periodical/non-entity-periodical-operView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") TempPeriodicalUnit<StoreControlJournal> entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        BaseJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            entityDAO.delete(StoreControlJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/periodical/non-entity-periodical-operView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") TempPeriodicalUnit<StoreControlJournal> entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/periodical/non-entity-periodical-operView";
        }
        boolean flag = true;
        UserModel userModel = this.getUserModel();
        if (entity.getEntityJournals() != null) {
            for (BaseJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                if (result.equals("Не соответствует"))
                    flag = false;
            }
            for (BaseJournal journal: entity.getEntityJournals()) {
                StoreControlJournal journalCast = new StoreControlJournal(journal);
                entityDAO.saveOrUpdate(journalCast);
            }
        }
        return "redirect:/";
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/periodical/non-entity-periodical-operView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditNonEntityPeriodical", true);
        model.addAttribute("title", "Контроль складирования");
        model.addAttribute("type", "Контроль складирования");
        model.addAttribute("lastDate", lastDate);
        model.addAttribute("nextDate", nextDate);
        model.addAttribute("editMode", true);
        model.addAttribute("periodicalClass", "StoreControl");
        model.addAttribute("entityTCPs", entityTCPs);
    }

}
