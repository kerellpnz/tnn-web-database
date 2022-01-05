package com.kerellpnz.tnnwebdatabase.controller.general;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tcp/Customers")
public class CustomerController {

    private final BaseEntityDAO<Customer> customerDAO;

    @Autowired
    public CustomerController(BaseEntityDAO<Customer> customerDAO) {
        this.customerDAO = customerDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Заказчик");
        mv.addObject("userClickCustomers", true);
        return mv;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("customer") Customer customer) {
        customerDAO.saveOrUpdate(customer);
        return "redirect:/tcp/Customers/showAll";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        customerDAO.delete(Customer.class, id);
        return "redirect:/tcp/Customers/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Customer> getAll() {
        return customerDAO.getAll(Customer.class);
    }

    @ModelAttribute("customer")
    public Customer getCustomer() {
        return new Customer();
    }
}

