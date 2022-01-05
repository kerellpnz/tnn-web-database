package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.detail;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.FrontalSaddleSealingTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/FrontalSaddleSealingTCPs")
public class FrontalSaddleSealingTCPController extends BaseTCPController<FrontalSaddleSealingTCP> {

    @Autowired
    private BaseEntityDAO<FrontalSaddleSealingTCP> TCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Уплотнения ЗШ");
        mv.addObject("tcpClass", "FrontalSaddleSealingTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") FrontalSaddleSealingTCP tcp) {
        TCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/FrontalSaddleSealingTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        TCPDAO.delete(FrontalSaddleSealingTCP.class, id);
        return "redirect:/tcp/FrontalSaddleSealingTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<FrontalSaddleSealingTCP> getTCPs() {
        return TCPDAO.getAll(FrontalSaddleSealingTCP.class);
    }

    @ModelAttribute("tcp")
    public FrontalSaddleSealingTCP getTCPModel() {
        return new FrontalSaddleSealingTCP();
    }
}
