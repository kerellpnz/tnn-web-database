package com.kerellpnz.tnnwebdatabase.controller.unit.periodical;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.periodical.HeatTreatmentDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.HeatTreatmentJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.periodical.NDTControlTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.HeatTreatment;
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
@RequestMapping("/entity/HeatTreatment")
public class HeatTreatmentController extends BaseEntityController {

    private final HeatTreatmentDAO heatTreatmentDAO;
    private final BaseEntityDAO<HeatTreatmentJournal> baseJournalDAO;
    private final BaseEntityDAO<NDTControlTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<NDTControlTCP> heatTreatmentTCPs;

    @Autowired
    public HeatTreatmentController(HttpSession session,
                                      JournalNumberDAO journalNumberDAO,
                                      HeatTreatmentDAO heatTreatmentDAO,
                                      BaseEntityDAO<HeatTreatmentJournal> baseJournalDAO,
                                      BaseEntityDAO<NDTControlTCP> TCPDAO,
                                      InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.heatTreatmentDAO = heatTreatmentDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }

//    @GetMapping("/showAll")
//    public ModelAndView showAll() {
//        ModelAndView mv = modelInitializeIndex("Контроль ТО","userClickPeriodicalControl");
//        mv.addObject("userClickEntityView", true);
//        return mv;
//    }

//    private ModelAndView modelInitializeIndex(String title, String pagePart) {
//        ModelAndView mv = new ModelAndView("index");
//        mv.addObject("title", title);
//        mv.addObject("periodicalClass", "HeatTreatment");
//        mv.addObject(pagePart, true);
//        return mv;
//    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit();
        HeatTreatment heatTreatment = heatTreatmentDAO.get(id);
        if (heatTreatment == null)
            throw new EntityNotFoundException();
        for (HeatTreatmentJournal heatTreatmentJournal: heatTreatment.getPeriodicalJournals()) {
            if (heatTreatmentJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, heatTreatmentJournal.getInspectorId());
                heatTreatmentJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        heatTreatmentTCPs = TCPDAO.getAll(NDTControlTCP.class);
        return modelInitializeObject(mv, heatTreatment, heatTreatmentTCPs);
    }



    private ModelAndView modelInitializeEdit() {
        ModelAndView mv = new ModelAndView("./unit/periodical/periodical-operView");
        mv.addObject("title", "Операции");
        mv.addObject("periodicalClass", "HeatTreatment");
        mv.addObject("userClickEditPeriodicalControl", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, HeatTreatment heatTreatment, List<NDTControlTCP> heatTreatmentTCPs) {
        mv.addObject("heatTreatmentTCPs", heatTreatmentTCPs);
        mv.addObject("editMode", true);
        mv.addObject("periodicalControl", heatTreatment);
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("periodicalControl") HeatTreatment heatTreatment, Model model) {
        if (heatTreatment.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(NDTControlTCP.class, heatTreatment.getTempTCPId());
            if (heatTreatment.getPeriodicalJournals() != null) {
                heatTreatment.getPeriodicalJournals().add(new HeatTreatmentJournal(tempTCP));
            }
            else {
                List<HeatTreatmentJournal> heatTreatmentJournals = new ArrayList<>();
                heatTreatmentJournals.add(new HeatTreatmentJournal(tempTCP));
                heatTreatment.setPeriodicalJournals(heatTreatmentJournals);
            }
        }
        modelInitialize(model);
        model.addAttribute("periodicalControl", heatTreatment);
        return "./unit/periodical/periodical-operView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("periodicalControl") HeatTreatment heatTreatment, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        HeatTreatmentJournal heatTreatmentJournal = heatTreatment.getPeriodicalJournals().get(i);
        if (heatTreatmentJournal.getId() != 0) {
            baseJournalDAO.delete(HeatTreatmentJournal.class, heatTreatmentJournal.getId());
        }
        heatTreatment.getPeriodicalJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("periodicalControl", heatTreatment);
        return "./unit/periodical/periodical-operView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("periodicalControl") HeatTreatment heatTreatment, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/periodical/periodical-operView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        Date maxDate = new Date(0);
        UserModel userModel = this.getUserModel();
        if (heatTreatment.getPeriodicalJournals() != null) {
            for (HeatTreatmentJournal journal: heatTreatment.getPeriodicalJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(heatTreatment);
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
        heatTreatment.setLastControl(maxDate);
        heatTreatment.setNextControl(new Date((long)(maxDate.getTime() + 2628*Math.pow(10,6))));
        if (flag)
            heatTreatment.setStatus("Cоотв.");
        else
            heatTreatment.setStatus("НЕ СООТВ.");
        heatTreatmentDAO.saveOrUpdate(heatTreatment);
        return "redirect:/";
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/periodical/periodical-operView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditPeriodicalControl", true);
        model.addAttribute("periodicalClass", "HeatTreatment");
        model.addAttribute("title", "Операции");
        model.addAttribute("editMode", true);
        model.addAttribute("heatTreatmentTCPs", heatTreatmentTCPs);
    }

//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable int id) throws EntityNotFoundException {
//        HeatTreatment heatTreatment = heatTreatmentDAO.get(HeatTreatment.class, id);
//        if (heatTreatment == null)
//            throw new EntityNotFoundException();
//        heatTreatmentDAO.delete(HeatTreatment.class, id);
//        return "redirect:/entity/heatTreatments/showAll";
//    }

//    @GetMapping("json/getAll")
//    @ResponseBody
//    public List<HeatTreatment> getAll() {
//        return heatTreatmentDAO.getAll(HeatTreatment.class);
//    }
}
