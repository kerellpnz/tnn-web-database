package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.CoverSleeveTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/CoverSleeveTCPs")
public class CoverSleeveTCPController extends BaseTCPController<CoverSleeveTCP> {

    @Autowired
    private BaseEntityDAO<CoverSleeveTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Втулка центральная");
        mv.addObject("tcpClass", "CoverSleeveTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") CoverSleeveTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/CoverSleeveTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(CoverSleeveTCP.class, id);
        return "redirect:/tcp/CoverSleeveTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<CoverSleeveTCP> getTCPs() {
        return TCPDAO.getAll(CoverSleeveTCP.class);
    }

    @ModelAttribute("tcp")
    public CoverSleeveTCP getTCPModel() {
        return new CoverSleeveTCP();
    }
}
