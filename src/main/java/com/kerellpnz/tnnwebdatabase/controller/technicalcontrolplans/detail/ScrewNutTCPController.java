package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.ScrewNutTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/ScrewNutTCPs")
public class ScrewNutTCPController extends BaseTCPController<ScrewNutTCP> {

    @Autowired
    private BaseEntityDAO<ScrewNutTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Шпилька");
        mv.addObject("tcpClass", "ScrewNutTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") ScrewNutTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/ScrewNutTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(ScrewNutTCP.class, id);
        return "redirect:/tcp/ScrewNutTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ScrewNutTCP> getTCPs() {
        return TCPDAO.getAll(ScrewNutTCP.class);
    }

    @ModelAttribute("tcp")
    public ScrewNutTCP getTCPModel() {
        return new ScrewNutTCP();
    }
}
