package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.AnticorrosiveCoatingTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/AnticorrosiveCoatingTCPs")
public class AnticorrosiveCoatingTCPController extends BaseTCPController<AnticorrosiveCoatingTCP> {

    @Autowired
    private BaseEntityDAO<AnticorrosiveCoatingTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-АКП Входной");
        mv.addObject("tcpClass", "AnticorrosiveCoatingTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") AnticorrosiveCoatingTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/AnticorrosiveCoatingTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(AnticorrosiveCoatingTCP.class, id);
        return "redirect:/tcp/AnticorrosiveCoatingTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<AnticorrosiveCoatingTCP> getTCPs() {
        return TCPDAO.getAll(AnticorrosiveCoatingTCP.class);
    }

    @ModelAttribute("tcp")
    public AnticorrosiveCoatingTCP getTCPModel() {
        return new AnticorrosiveCoatingTCP();
    }
}
