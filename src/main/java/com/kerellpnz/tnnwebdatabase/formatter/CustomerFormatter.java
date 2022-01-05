package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CustomerFormatter implements Formatter<Customer> {

    private final BaseEntityDAO<Customer> customerDAO;

    @Autowired
    public CustomerFormatter(BaseEntityDAO<Customer> customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Customer parse(String text, Locale locale) throws ParseException {
        return customerDAO.get(Customer.class, Integer.parseInt(text));
    }

    @Override
    public String print(Customer object, Locale locale) {
        return object.toString();
    }
}
