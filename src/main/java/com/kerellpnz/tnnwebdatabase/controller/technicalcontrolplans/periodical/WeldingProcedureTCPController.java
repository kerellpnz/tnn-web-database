package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.periodical;

import com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans.BaseTCPController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.periodical.WeldingProceduresTCP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/WeldingProcedureTCPs")
public class WeldingProcedureTCPController extends BaseTCPController<WeldingProceduresTCP> {

    @Autowired
    private BaseEntityDAO<WeldingProceduresTCP> weldingProcedureTCPDAO;

    @GetMapping("/showAll")
    public ModelAndView showTCPs(@RequestParam(name="operation", required = false) String operation) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "ПТК-Режимы сварки");
        mv.addObject("tcpClass", "WeldingProcedureTCPs");
        mv.addObject("userClickTCP", true);
        if(operation!=null) {
            if(operation.equals("tcp")) {
                mv.addObject("message", "Пункт успешно добавлен!");
            }
        }
        return mv;
    }

    @PostMapping("/save")
    public String saveTCP(@ModelAttribute("tcp") WeldingProceduresTCP tcp) {
        weldingProcedureTCPDAO.saveOrUpdate(tcp);
        return "redirect:/tcp/WeldingProcedureTCPs/showAll?operation=tcp";
    }

    @GetMapping("/delete/{id}")
    public String deleteTCP(@PathVariable int id) {
        weldingProcedureTCPDAO.delete(WeldingProceduresTCP.class, id);
        return "redirect:/tcp/WeldingProcedureTCPs/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<WeldingProceduresTCP> getTCPs() {
        return weldingProcedureTCPDAO.getAll(WeldingProceduresTCP.class);
    }

    @ModelAttribute("tcp")
    public WeldingProceduresTCP getTCPModel() {
        return new WeldingProceduresTCP();
    }
}
