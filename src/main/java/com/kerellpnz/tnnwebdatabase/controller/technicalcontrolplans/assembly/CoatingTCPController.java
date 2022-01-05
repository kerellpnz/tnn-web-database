package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.assembly;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.assemblyunit.CoatingTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/CoatingTCPs")
public class CoatingTCPController extends BaseTCPController<CoatingTCP> {

    @Autowired
    private BaseEntityDAO<CoatingTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-ЗШ");
        mv.addObject("tcpClass", "CoatingTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") CoatingTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/CoatingTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(CoatingTCP.class, id);
        return "redirect:/tcp/CoatingTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<CoatingTCP> getTCPs() {
        return TCPDAO.getAll(CoatingTCP.class);
    }

    @ModelAttribute("tcp")
    public CoatingTCP getTCPModel() {
        return new CoatingTCP();
    }
}
