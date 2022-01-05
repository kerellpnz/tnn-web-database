package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.assembly;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.SheetGateValveTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/SheetGateValveTCPs")
public class SheetGateValveTCPController extends BaseTCPController<SheetGateValveTCP> {

    @Autowired
    private BaseEntityDAO<SheetGateValveTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-ЗШ");
        mv.addObject("tcpClass", "SheetGateValveTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") SheetGateValveTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/SheetGateValveTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(SheetGateValveTCP.class, id);
        return "redirect:/tcp/SheetGateValveTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SheetGateValveTCP> getTCPs() {
        return TCPDAO.getAll(SheetGateValveTCP.class);
    }

    @ModelAttribute("tcp")
    public SheetGateValveTCP getTCPModel() {
        return new SheetGateValveTCP();
    }
}
