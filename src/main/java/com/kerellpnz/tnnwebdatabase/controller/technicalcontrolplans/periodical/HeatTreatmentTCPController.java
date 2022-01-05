package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.periodical;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.periodical.NDTControlTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/HeatTreatmentTCPs")
public class HeatTreatmentTCPController extends BaseTCPController<NDTControlTCP> {

    @Autowired
    private BaseEntityDAO<NDTControlTCP> heatTreatmentTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Контроль ТО");
        mv.addObject("tcpClass", "HeatTreatmentTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") NDTControlTCP tcp) {
        heatTreatmentTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/HeatTreatmentTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        heatTreatmentTCPDAO.delete(NDTControlTCP.class, id);
        return "redirect:/tcp/HeatTreatmentTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<NDTControlTCP> getTCPs() {
        return heatTreatmentTCPDAO.getAll(NDTControlTCP.class);
    }

    @ModelAttribute("tcp")
    public NDTControlTCP getTCPModel() {
        return new NDTControlTCP();
    }
}
