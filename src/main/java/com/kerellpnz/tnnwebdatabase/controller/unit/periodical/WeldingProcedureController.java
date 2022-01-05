package com.kerellpnz.tnnwebdatabase.controller.unit.periodical;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.periodical.WeldingProcedureDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.WeldingProcedureJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.periodical.WeldingProceduresTCP;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/entity/WeldingProcedures")
public class WeldingProcedureController extends BaseEntityController {

    private final WeldingProcedureDAO weldingProcedureDAO;
    private final BaseEntityDAO<WeldingProcedureJournal> baseJournalDAO;
    private final BaseEntityDAO<WeldingProceduresTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<WeldingProceduresTCP> weldingProcedureTCPs;

    @Autowired
    public WeldingProcedureController(HttpSession session,
                                   JournalNumberDAO journalNumberDAO,
                                   WeldingProcedureDAO weldingProcedureDAO,
                                   BaseEntityDAO<WeldingProcedureJournal> baseJournalDAO,
                                   BaseEntityDAO<WeldingProceduresTCP> TCPDAO,
                                   InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.weldingProcedureDAO = weldingProcedureDAO;
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
    

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit();
        WeldingProcedure weldingProcedure = weldingProcedureDAO.get(id);
        if (weldingProcedure == null)
            throw new EntityNotFoundException();
        for (WeldingProcedureJournal weldingProcedureJournal: weldingProcedure.getPeriodicalJournals()) {
            if (weldingProcedureJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, weldingProcedureJournal.getInspectorId());
                weldingProcedureJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        weldingProcedureTCPs = TCPDAO.getAll(WeldingProceduresTCP.class);
        return modelInitializeObject(mv, weldingProcedure, weldingProcedureTCPs);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Режимы сварки");
        mv.addObject("periodicalClass", "WeldingProcedures");
        mv.addObject("userClickPeriodicalControl", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit() {
        ModelAndView mv = new ModelAndView("./unit/periodical/periodical-operView");
        mv.addObject("title", "Операции");
        mv.addObject("periodicalClass", "WeldingProcedures");
        mv.addObject("userClickEditPeriodicalControl", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, WeldingProcedure weldingProcedure, List<WeldingProceduresTCP> weldingProcedureTCPs) {
        mv.addObject("weldingProcedureTCPs", weldingProcedureTCPs);
        mv.addObject("editMode", true);
        mv.addObject("periodicalControl", weldingProcedure);
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("periodicalControl") WeldingProcedure weldingProcedure, Model model) {
        if (weldingProcedure.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(WeldingProceduresTCP.class, weldingProcedure.getTempTCPId());
            if (weldingProcedure.getPeriodicalJournals() != null) {
                weldingProcedure.getPeriodicalJournals().add(new WeldingProcedureJournal(tempTCP));
            }
            else {
                List<WeldingProcedureJournal> weldingProcedureJournals = new ArrayList<>();
                weldingProcedureJournals.add(new WeldingProcedureJournal(tempTCP));
                weldingProcedure.setPeriodicalJournals(weldingProcedureJournals);
            }
        }
        modelInitialize(model);
        model.addAttribute("periodicalControl", weldingProcedure);
        return "./unit/periodical/periodical-operView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("periodicalControl") WeldingProcedure weldingProcedure, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        WeldingProcedureJournal weldingProcedureJournal = weldingProcedure.getPeriodicalJournals().get(i);
        if (weldingProcedureJournal.getId() != 0) {
            baseJournalDAO.delete(WeldingProcedureJournal.class, weldingProcedureJournal.getId());
        }
        weldingProcedure.getPeriodicalJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("periodicalControl", weldingProcedure);
        return "./unit/periodical/periodical-operView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("periodicalControl") WeldingProcedure weldingProcedure, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/periodical/periodical-operView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        Date maxDate = new Date(0);
        UserModel userModel = this.getUserModel();
        if (weldingProcedure.getPeriodicalJournals() != null) {
            for (WeldingProcedureJournal journal: weldingProcedure.getPeriodicalJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(weldingProcedure);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null) {
                    isEmpty = false;
                    if (journal.getDate().after(maxDate))
                        maxDate = journal.getDate();
                }
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        weldingProcedure.setLastControl(maxDate);
        weldingProcedure.setNextControl(new Date((long)(maxDate.getTime() + 7*864*Math.pow(10,5))));
        if (flag)
            weldingProcedure.setStatus("Cоотв.");
        else
            weldingProcedure.setStatus("НЕ СООТВ.");
        weldingProcedureDAO.saveOrUpdate(weldingProcedure);
        return "redirect:/entity/WeldingProcedures/showAll";
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/periodical/periodical-operView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditPeriodicalControl", true);
        model.addAttribute("periodicalClass", "WeldingProcedures");
        model.addAttribute("title", "Операции");
        model.addAttribute("editMode", true);
        model.addAttribute("weldingProcedureTCPs", weldingProcedureTCPs);
    }

//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable int id) throws EntityNotFoundException {
//        WeldingProcedure weldingProcedure = weldingProcedureDAO.get(WeldingProcedure.class, id);
//        if (weldingProcedure == null)
//            throw new EntityNotFoundException();
//        weldingProcedureDAO.delete(WeldingProcedure.class, id);
//        return "redirect:/entity/WeldingProcedures/showAll";
//    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<WeldingProcedure> getAll() {
        return weldingProcedureDAO.getAll(WeldingProcedure.class);
    }
}
