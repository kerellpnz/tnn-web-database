package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spindle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class SpindleFormatter implements Formatter<Spindle> {

    private final BaseEntityDAO<Spindle> SpindleDAO;

    @Autowired
    public SpindleFormatter(BaseEntityDAO<Spindle> SpindleDAO) {
        this.SpindleDAO = SpindleDAO;
    }

    @Override
    public Spindle parse(String text, Locale locale) throws ParseException {
        return SpindleDAO.get(Spindle.class, Integer.parseInt(text));
    }

    @Override
    public String print(Spindle object, Locale locale) {
        return object.toString();
    }
}
