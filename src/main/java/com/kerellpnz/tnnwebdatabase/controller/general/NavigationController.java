package com.kerellpnz.tnnwebdatabase.controller.general;

import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.model.ChangePassword;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import com.kerellpnz.tnnwebdatabase.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NavigationController {

    @Autowired
    private HttpSession session;
    @Autowired
    private InspectorDAO inspectorDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserModel getUserModel() {
        return (UserModel) session.getAttribute("userModel");
    }


    @RequestMapping(value = {"/", "/menu", "/index"})
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Главное Меню");
        mv.addObject("userClickMainMenu", true);
        return mv;
    }

    @RequestMapping(value = "/gate-valve-menu")
    public ModelAndView gateValveMenu() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Меню ЗШ");
        mv.addObject("userClickGateValveMenu", true);
        return mv;
    }

    @RequestMapping("/showMyProfile")
    public ModelAndView showMyProfile() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Профиль");
        mv.addObject("userClickProfile", true);
        UserProfile userProfile = new UserProfile(this.getUserModel());
        mv.addObject("userProfile", userProfile);
        return mv;
    }

    @PostMapping("/processProfileForm")
    public String processProfileForm(
            @Valid @ModelAttribute("userProfile") UserProfile userProfile,
            BindingResult theBindingResult,
            Model theModel) {
        if (theBindingResult.hasErrors()){
            theModel.addAttribute("userClickProfile", true);
            theModel.addAttribute("title", "Профиль");
            return "index";
        }
        Inspector inspector = inspectorDAO.get(Inspector.class, userProfile.getId());
        UserModel userModel = this.getUserModel();
        if (!inspector.getLogin().equalsIgnoreCase(userProfile.getLogin())) {
            Inspector existing = inspectorDAO.findByInspectorLogin(userProfile.getLogin());
            if (existing != null){
                theModel.addAttribute("registrationError", "Пользователь с таким логином уже существует!");
                theModel.addAttribute("userClickProfile", true);
                theModel.addAttribute("title", "Профиль");
                return "index";
            }
            inspector.setLogin(userProfile.getLogin().toLowerCase());
            userModel.setLogin(userProfile.getLogin().toLowerCase());
        }
        inspector.setApointment(userProfile.getApointment());
        userModel.setApointment(userProfile.getApointment());
        inspector.setName(userProfile.getName());
        userModel.setName(userProfile.getName());
        session.setAttribute("userModel", userModel);
        inspectorDAO.saveOrUpdate(inspector);
        return "redirect:/";
    }

    @RequestMapping("/changePassword")
    public ModelAndView changePassword() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Профиль");
        mv.addObject("userClickChangePassword", true);
        ChangePassword changePassword = new ChangePassword(this.getUserModel());
        mv.addObject("changePassword", changePassword);
        return mv;
    }

    @PostMapping("/processChangePassword")
    public String processChangePassword(
            @Valid @ModelAttribute("changePassword") ChangePassword changePassword,
            BindingResult theBindingResult,
            Model theModel) {
        if (theBindingResult.hasErrors()){
            theModel.addAttribute("userClickChangePassword", true);
            theModel.addAttribute("title", "Профиль");
            return "index";
        }
        Inspector inspector = inspectorDAO.get(Inspector.class, changePassword.getId());
        inspector.setPassword(passwordEncoder.encode(changePassword.getPassword()));
        inspectorDAO.saveOrUpdate(inspector);
        return "redirect:/";
    }


    @RequestMapping(value = "/login")
    public ModelAndView login(@RequestParam(name="error", required = false)String error, @RequestParam(name="logout", required = false) String logout) {
        ModelAndView mv = new ModelAndView("login");
        if (error != null) {
            mv.addObject("message", "Неверный логин/пароль!");
        }
        if (logout != null) {
            mv.addObject("logout", "Пользователь вышел из учетной записи.");
        }
        mv.addObject("title", "Вход");
        return mv;
    }

    @RequestMapping(value = "/access-denied")
    public ModelAndView accessDenied() {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("title", "Доступ запрещен!");
        mv.addObject("errorTitle", "Доступ запрещен!");
        mv.addObject("errorDescription", "У Вас нет доступа к этой странице!");
        return mv;
    }

    @RequestMapping(value = "/perform-logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }
}
