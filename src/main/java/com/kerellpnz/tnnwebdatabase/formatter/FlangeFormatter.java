package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Flange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class FlangeFormatter implements Formatter<Flange> {

    private final BaseEntityDAO<Flange> flangeDAO;

    @Autowired
    public FlangeFormatter(BaseEntityDAO<Flange> flangeDAO) {
        this.flangeDAO = flangeDAO;
    }

    @Override
    public Flange parse(String text, Locale locale) throws ParseException {
        return flangeDAO.get(Flange.class, Integer.parseInt(text));
    }

    @Override
    public String print(Flange object, Locale locale) {
        return object.toString();
    }
}
