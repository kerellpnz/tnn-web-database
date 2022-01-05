package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.RunningSleeveTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/RunningSleeveTCPs")
public class RunningSleeveTCPController extends BaseTCPController<RunningSleeveTCP> {

    @Autowired
    private BaseEntityDAO<RunningSleeveTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Втулка центральная");
        mv.addObject("tcpClass", "RunningSleeveTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") RunningSleeveTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/RunningSleeveTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(RunningSleeveTCP.class, id);
        return "redirect:/tcp/RunningSleeveTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<RunningSleeveTCP> getTCPs() {
        return TCPDAO.getAll(RunningSleeveTCP.class);
    }

    @ModelAttribute("tcp")
    public RunningSleeveTCP getTCPModel() {
        return new RunningSleeveTCP();
    }
}
