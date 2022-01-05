package com.kerellpnz.tnnwebdatabase.controller.general;

import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.journal.general.JournalNumber;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/entity/JournalNumbers")
public class JournalNumberController {
    @Autowired
    private JournalNumberDAO journalNumberDAO;
    @Autowired
    private HttpSession session;

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Журналы ТН");
        mv.addObject("userClickJournalNumbers", true);
        if(operation!=null) {
            if(operation.equals("journalNumber")) {
                mv.addObject("message", "Журнал успешно добавлен!");
            }
            if(operation.equals("success"))
                mv.addObject("message", "Активный журнал заменен!");
        }
        return mv;
    }

    @ModelAttribute("journals")
    public List<String> getOpenJournalNumbers() {
        return journalNumberDAO.getOpenJournalNumbers();
    }

    @GetMapping("/changeJournal")
    public ModelAndView changeJournal(@RequestParam("number") String number) {
        UserModel userModel = (UserModel) session.getAttribute("userModel");
        userModel.setJournalNumber(number);
        session.setAttribute("userModel", userModel);
        return this.showAll("success");
    }

    @GetMapping("/journal/{id}/activation")
    @ResponseBody
    public String handleJournalActivation(@PathVariable int id) {
        JournalNumber journalNumber = journalNumberDAO.get(JournalNumber.class, id);
        boolean isClosed = journalNumber.isClosed();
        journalNumber.setClosed(!isClosed);
        journalNumberDAO.saveOrUpdate(journalNumber);
        return (isClosed)? "Журнал закрыт!": "Журнал открыт!";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("journalNumber") JournalNumber journalNumber) {
        journalNumberDAO.saveOrUpdate(journalNumber);
        return "redirect:/entity/JournalNumbers/showAll?operation=journalNumber";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        journalNumberDAO.delete(JournalNumber.class, id);
        return "redirect:/entity/JournalNumbers/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<JournalNumber> getAll() {
        return journalNumberDAO.getAll(JournalNumber.class);
    }

    @ModelAttribute("journalNumber")
    public JournalNumber getJournalNumberModel() {
        return new JournalNumber();
    }
}
