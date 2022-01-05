package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.material;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.material.WeldingMaterialTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/WeldingMaterialTCPs")
public class WeldingMaterialTCPController extends BaseTCPController<WeldingMaterialTCP> {

    @Autowired
    private BaseEntityDAO<WeldingMaterialTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Сварочные материалы");
        mv.addObject("tcpClass", "WeldingMaterialTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") WeldingMaterialTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/WeldingMaterialTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(WeldingMaterialTCP.class, id);
        return "redirect:/tcp/WeldingMaterialTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<WeldingMaterialTCP> getTCPs() {
        return TCPDAO.getAll(WeldingMaterialTCP.class);
    }

    @ModelAttribute("tcp")
    public WeldingMaterialTCP getTCPModel() {
        return new WeldingMaterialTCP();
    }
}
