package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.CaseBottomTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/CaseBottomTCPs")
public class CaseBottomTCPController extends BaseTCPController<CaseBottomTCP> {
    @Autowired
    private BaseEntityDAO<CaseBottomTCP> caseBottomTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Днище");
        mv.addObject("tcpClass", "CaseBottomTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") CaseBottomTCP tcp) {
        caseBottomTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/CaseBottomTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        caseBottomTCPDAO.delete(CaseBottomTCP.class, id);
        return "redirect:/tcp/CaseBottomTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<CaseBottomTCP> getTCPs() {
        return caseBottomTCPDAO.getAll(CaseBottomTCP.class);
    }

    @ModelAttribute("tcp")
    public CaseBottomTCP getTCPModel() {
        return new CaseBottomTCP();
    }
}
