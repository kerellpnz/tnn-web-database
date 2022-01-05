package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.SaddleTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/SaddleTCPs")
public class SaddleTCPController extends BaseTCPController<SaddleTCP> {

    @Autowired
    private BaseEntityDAO<SaddleTCP> saddleTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Седло");
        mv.addObject("tcpClass", "SaddleTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") SaddleTCP tcp) {
        saddleTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/SaddleTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        saddleTCPDAO.delete(SaddleTCP.class, id);
        return "redirect:/tcp/SaddleTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SaddleTCP> getTCPs() {
        return saddleTCPDAO.getAll(SaddleTCP.class);
    }

    @ModelAttribute("tcp")
    public SaddleTCP getTCPModel() {
        return new SaddleTCP();
    }
}
