package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.SheetGateValveCoverTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/SheetGateValveCoverTCPs")
public class SheetGateValveCoverTCPController extends BaseTCPController<SheetGateValveCoverTCP> {

    @Autowired
    private BaseEntityDAO<SheetGateValveCoverTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Крышка ЗШ");
        mv.addObject("tcpClass", "SheetGateValveCoverTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") SheetGateValveCoverTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/SheetGateValveCoverTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(SheetGateValveCoverTCP.class, id);
        return "redirect:/tcp/SheetGateValveCoverTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SheetGateValveCoverTCP> getTCPs() {
        return TCPDAO.getAll(SheetGateValveCoverTCP.class);
    }

    @ModelAttribute("tcp")
    public SheetGateValveCoverTCP getTCPModel() {
        return new SheetGateValveCoverTCP();
    }
}
