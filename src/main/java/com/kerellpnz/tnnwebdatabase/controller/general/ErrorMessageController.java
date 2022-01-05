package com.kerellpnz.tnnwebdatabase.controller.general;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.ErrorMessage;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/errorMessage")
public class ErrorMessageController {

    private final BaseEntityDAO<ErrorMessage> messageDAO;
    private final HttpSession session;

    @Autowired
    public ErrorMessageController(BaseEntityDAO<ErrorMessage> messageDAO, HttpSession session) {
        this.messageDAO = messageDAO;
        this.session = session;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Сообщения об ошибках");
        mv.addObject("userClickShowErrors", true);
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = new ModelAndView("index");
        ErrorMessage em = new ErrorMessage();
        mv.addObject("title", "Сообщение об ошибке");
        mv.addObject("errorMessage", em);
        mv.addObject("userClickErrorMessage", true);
        return mv;
    }

    @PostMapping("/send")
    public String send(@Valid @ModelAttribute("errorMessage") ErrorMessage em, BindingResult results, Model model) {
        if(results.hasErrors()) {
            model.addAttribute("title", "Сообщение об ошибке");
            model.addAttribute("errorMessage", em);
            model.addAttribute("userClickErrorMessage", true);
            return "index";
        }
        UserModel userModel = (UserModel) session.getAttribute("userModel");
        em.setInspectorName(userModel.getName());
        messageDAO.saveOrUpdate(em);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        messageDAO.delete(ErrorMessage.class, id);
        return "redirect:/errorMessage/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ErrorMessage> get() {
        return messageDAO.getAll(ErrorMessage.class);
    }
}
