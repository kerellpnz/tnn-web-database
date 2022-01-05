package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;


@Component
public class InspectorFormatter implements Formatter<Inspector> {

    @Autowired
    private InspectorDAO inspectorDAO;

    @Override
    public String print(Inspector object, Locale locale) {
        return object.toString();
    }

    @Override
    public Inspector parse(String text, Locale locale) throws ParseException {
        return inspectorDAO.get(Inspector.class, Integer.parseInt(text));
    }
}
