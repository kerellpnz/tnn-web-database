package com.kerellpnz.tnnwebdatabase.controller.general;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/Inspectors")
public class InspectorController {
    @Autowired
    private BaseEntityDAO<Inspector> inspectorDAO;

    @GetMapping("/showAll")
    public ModelAndView showAll() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Персонал");
        mv.addObject("userClickInspectors", true);
        return mv;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("inspector") Inspector inspector) {
        inspectorDAO.saveOrUpdate(inspector);
        return "redirect:/Inspectors/showAll";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        inspectorDAO.delete(Inspector.class, id);
        return "redirect:/Inspectors/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Inspector> getAll() {
        return inspectorDAO.getAll(Inspector.class);
    }
}
