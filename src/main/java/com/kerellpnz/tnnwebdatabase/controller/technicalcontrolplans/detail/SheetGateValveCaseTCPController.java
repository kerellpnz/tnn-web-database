package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.SheetGateValveCaseTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/SheetGateValveCaseTCPs")
public class SheetGateValveCaseTCPController extends BaseTCPController<SheetGateValveCaseTCP> {

    @Autowired
    private BaseEntityDAO<SheetGateValveCaseTCP> sheetGateValveCaseTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Днище");
        mv.addObject("tcpClass", "SheetGateValveCaseTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") SheetGateValveCaseTCP tcp) {
        sheetGateValveCaseTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/SheetGateValveCaseTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        sheetGateValveCaseTCPDAO.delete(SheetGateValveCaseTCP.class, id);
        return "redirect:/tcp/SheetGateValveCaseTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SheetGateValveCaseTCP> getTCPs() {
        return sheetGateValveCaseTCPDAO.getAll(SheetGateValveCaseTCP.class);
    }

    @ModelAttribute("tcp")
    public SheetGateValveCaseTCP getTCPModel() {
        return new SheetGateValveCaseTCP();
    }
}
