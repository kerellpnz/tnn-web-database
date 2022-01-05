package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.periodical;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.periodical.FactoryInspectionTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/FactoryInspectionTCPs")
public class FactoryInspectionTCPController extends BaseTCPController<FactoryInspectionTCP> {

    @Autowired
    private BaseEntityDAO<FactoryInspectionTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Режимы сварки");
        mv.addObject("tcpClass", "FactoryInspectionTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") FactoryInspectionTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/FactoryInspectionTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(FactoryInspectionTCP.class, id);
        return "redirect:/tcp/FactoryInspectionTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<FactoryInspectionTCP> getTCPs() {
        return TCPDAO.getAll(FactoryInspectionTCP.class);
    }

    @ModelAttribute("tcp")
    public FactoryInspectionTCP getTCPModel() {
        return new FactoryInspectionTCP();
    }
}
