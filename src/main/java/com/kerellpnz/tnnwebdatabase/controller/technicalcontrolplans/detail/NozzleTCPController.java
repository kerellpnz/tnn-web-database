package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.NozzleTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/NozzleTCPs")
public class NozzleTCPController extends BaseTCPController<NozzleTCP> {

    @Autowired
    private BaseEntityDAO<NozzleTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Фланец");
        mv.addObject("tcpClass", "NozzleTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") NozzleTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/NozzleTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(NozzleTCP.class, id);
        return "redirect:/tcp/NozzleTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<NozzleTCP> getTCPs() {
        return TCPDAO.getAll(NozzleTCP.class);
    }

    @ModelAttribute("tcp")
    public NozzleTCP getTCPModel() {
        return new NozzleTCP();
    }
}
