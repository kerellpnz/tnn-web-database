package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.general;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.general.PIDTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/PIDTCPs")
public class PIDTCPController extends BaseTCPController<PIDTCP> {
    @Autowired
    private BaseEntityDAO<PIDTCP> pidTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-PID");
        mv.addObject("tcpClass", "PIDTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") PIDTCP tcp) {
        pidTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/PIDTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        pidTCPDAO.delete(PIDTCP.class, id);
        return "redirect:/tcp/PIDTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<PIDTCP> getTCPs() {
        return pidTCPDAO.getAll(PIDTCP.class);
    }

    @ModelAttribute("tcp")
    public PIDTCP getTCPModel() {
        return new PIDTCP();
    }

}
