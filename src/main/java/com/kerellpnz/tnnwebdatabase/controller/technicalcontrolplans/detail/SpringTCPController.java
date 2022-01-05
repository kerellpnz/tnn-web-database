package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.SpringTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/SpringTCPs")
public class SpringTCPController extends BaseTCPController<SpringTCP> {

    @Autowired
    private BaseEntityDAO<SpringTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Пружина");
        mv.addObject("tcpClass", "SpringTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") SpringTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/SpringTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(SpringTCP.class, id);
        return "redirect:/tcp/SpringTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<SpringTCP> getTCPs() {
        return TCPDAO.getAll(SpringTCP.class);
    }

    @ModelAttribute("tcp")
    public SpringTCP getTCPModel() {
        return new SpringTCP();
    }
}
