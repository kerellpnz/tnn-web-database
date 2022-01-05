package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.periodical;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.periodical.StoresControlTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/StoresControlTCPs")
public class StoreControlTCPController extends BaseTCPController<StoresControlTCP> {
    @Autowired
    private BaseEntityDAO<StoresControlTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Режимы сварки");
        mv.addObject("tcpClass", "StoresControlTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") StoresControlTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/StoresControlTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(StoresControlTCP.class, id);
        return "redirect:/tcp/StoresControlTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<StoresControlTCP> getTCPs() {
        return TCPDAO.getAll(StoresControlTCP.class);
    }

    @ModelAttribute("tcp")
    public StoresControlTCP getTCPModel() {
        return new StoresControlTCP();
    }
}
