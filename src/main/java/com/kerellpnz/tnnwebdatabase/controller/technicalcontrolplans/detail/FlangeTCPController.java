package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.FlangeTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/FlangeTCPs")
public class FlangeTCPController extends BaseTCPController<FlangeTCP> {

    @Autowired
    private BaseEntityDAO<FlangeTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Фланец");
        mv.addObject("tcpClass", "FlangeTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") FlangeTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/FlangeTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(FlangeTCP.class, id);
        return "redirect:/tcp/FlangeTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<FlangeTCP> getTCPs() {
        return TCPDAO.getAll(FlangeTCP.class);
    }

    @ModelAttribute("tcp")
    public FlangeTCP getTCPModel() {
        return new FlangeTCP();
    }
}
