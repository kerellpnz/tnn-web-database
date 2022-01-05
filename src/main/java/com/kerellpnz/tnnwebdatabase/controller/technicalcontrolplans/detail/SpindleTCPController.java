package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.SpindleTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/SpindleTCPs")
public class SpindleTCPController extends BaseTCPController<SpindleTCP> {
    @Autowired
    private BaseEntityDAO<SpindleTCP> spindleTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Шпиндель");
        mv.addObject("tcpClass", "SpindleTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") SpindleTCP tcp) {
        spindleTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/SpindleTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        spindleTCPDAO.delete(SpindleTCP.class, id);
        return "redirect:/tcp/SpindleTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SpindleTCP> getTCPs() {
        return spindleTCPDAO.getAll(SpindleTCP.class);
    }

    @ModelAttribute("tcp")
    public SpindleTCP getTCPModel() {
        return new SpindleTCP();
    }
}
