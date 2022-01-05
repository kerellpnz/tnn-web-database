package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.CoverSleeve008TCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/CoverSleeve008TCPs")
public class CoverSleeve008TCPController extends BaseTCPController<CoverSleeve008TCP> {

    @Autowired
    private BaseEntityDAO<CoverSleeve008TCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Втулка дренажная");
        mv.addObject("tcpClass", "CoverSleeve008TCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") CoverSleeve008TCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/CoverSleeve008TCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(CoverSleeve008TCP.class, id);
        return "redirect:/tcp/CoverSleeve008TCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<CoverSleeve008TCP> getTCPs() {
        return TCPDAO.getAll(CoverSleeve008TCP.class);
    }

    @ModelAttribute("tcp")
    public CoverSleeve008TCP getTCPModel() {
        return new CoverSleeve008TCP();
    }
}
