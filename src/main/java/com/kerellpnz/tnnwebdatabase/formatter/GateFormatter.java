package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Gate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class GateFormatter implements Formatter<Gate> {

    private final BaseEntityDAO<Gate> GateDAO;

    @Autowired
    public GateFormatter(BaseEntityDAO<Gate> GateDAO) {
        this.GateDAO = GateDAO;
    }

    @Override
    public Gate parse(String text, Locale locale) throws ParseException {
        return GateDAO.get(Gate.class, Integer.parseInt(text));
    }

    @Override
    public String print(Gate object, Locale locale) {
        return object.toString();
    } 
}
