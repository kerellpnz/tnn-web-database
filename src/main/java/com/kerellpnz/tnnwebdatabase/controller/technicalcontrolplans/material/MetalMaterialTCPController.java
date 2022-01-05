package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.material;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.material.MetalMaterialTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

@Controller
@RequestMapping("/tcp/MetalMaterialTCPs")
public class MetalMaterialTCPController extends BaseTCPController<MetalMaterialTCP> {
    @Autowired
    private BaseEntityDAO<MetalMaterialTCP> metalMaterialTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Металл");
        mv.addObject("tcpClass", "MetalMaterialTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") MetalMaterialTCP tcp) {
        metalMaterialTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/MetalMaterialTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        metalMaterialTCPDAO.delete(MetalMaterialTCP.class, id);
        return "redirect:/tcp/MetalMaterialTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<MetalMaterialTCP> getTCPs() {
        return metalMaterialTCPDAO.getAll(MetalMaterialTCP.class);
    }

    @ModelAttribute("tcp")
    public MetalMaterialTCP getTCPModel() {
        return new MetalMaterialTCP();
    }
}
