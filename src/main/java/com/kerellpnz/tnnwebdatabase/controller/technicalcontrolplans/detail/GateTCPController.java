package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.GateTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/GateTCPs")
public class GateTCPController extends BaseTCPController<GateTCP> {

    @Autowired
    private BaseEntityDAO<GateTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Шибер");
        mv.addObject("tcpClass", "GateTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") GateTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/GateTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(GateTCP.class, id);
        return "redirect:/tcp/GateTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<GateTCP> getTCPs() {
        return TCPDAO.getAll(GateTCP.class);
    }

    @ModelAttribute("tcp")
    public GateTCP getTCPModel() {
        return new GateTCP();
    }
}
