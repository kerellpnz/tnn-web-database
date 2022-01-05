package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.RingTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/RingTCPs")
public class RingTCPController extends BaseTCPController<RingTCP> {

    @Autowired
    private BaseEntityDAO<RingTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Кольцо");
        mv.addObject("tcpClass", "RingTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") RingTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/RingTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(RingTCP.class, id);
        return "redirect:/tcp/RingTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<RingTCP> getTCPs() {
        return TCPDAO.getAll(RingTCP.class);
    }

    @ModelAttribute("tcp")
    public RingTCP getTCPModel() {
        return new RingTCP();
    }
}
