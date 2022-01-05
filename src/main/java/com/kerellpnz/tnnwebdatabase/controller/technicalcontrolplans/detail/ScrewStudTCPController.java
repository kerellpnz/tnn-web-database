package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.ScrewStudTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/ScrewStudTCPs")
public class ScrewStudTCPController extends BaseTCPController<ScrewStudTCP> {

    @Autowired
    private BaseEntityDAO<ScrewStudTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Шпилька");
        mv.addObject("tcpClass", "ScrewStudTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") ScrewStudTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/ScrewStudTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(ScrewStudTCP.class, id);
        return "redirect:/tcp/ScrewStudTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ScrewStudTCP> getTCPs() {
        return TCPDAO.getAll(ScrewStudTCP.class);
    }

    @ModelAttribute("tcp")
    public ScrewStudTCP getTCPModel() {
        return new ScrewStudTCP();
    }
}
