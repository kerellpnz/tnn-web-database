package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.ShearPinTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/ShearPinTCPs")
public class ShearPinTCPController extends BaseTCPController<ShearPinTCP> {

    @Autowired
    private BaseEntityDAO<ShearPinTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Штифты");
        mv.addObject("tcpClass", "ShearPinTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") ShearPinTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/ShearPinTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(ShearPinTCP.class, id);
        return "redirect:/tcp/ShearPinTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ShearPinTCP> getTCPs() {
        return TCPDAO.getAll(ShearPinTCP.class);
    }

    @ModelAttribute("tcp")
    public ShearPinTCP getTCPModel() {
        return new ShearPinTCP();
    }
}
