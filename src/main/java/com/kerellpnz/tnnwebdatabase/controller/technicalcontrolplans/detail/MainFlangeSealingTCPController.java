package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.MainFlangeSealingTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/MainFlangeSealingTCPs")
public class MainFlangeSealingTCPController extends BaseTCPController<MainFlangeSealingTCP> {

    @Autowired
    private BaseEntityDAO<MainFlangeSealingTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Уплотнения ЗШ");
        mv.addObject("tcpClass", "MainFlangeSealingTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") MainFlangeSealingTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/MainFlangeSealingTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(MainFlangeSealingTCP.class, id);
        return "redirect:/tcp/MainFlangeSealingTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<MainFlangeSealingTCP> getTCPs() {
        return TCPDAO.getAll(MainFlangeSealingTCP.class);
    }

    @ModelAttribute("tcp")
    public MainFlangeSealingTCP getTCPModel() {
        return new MainFlangeSealingTCP();
    }
}
