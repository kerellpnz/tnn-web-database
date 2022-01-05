package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CoverSleeveFormatter implements Formatter<CoverSleeve> {

    private final BaseEntityDAO<CoverSleeve> CoverSleeveDAO;

    @Autowired
    public CoverSleeveFormatter(BaseEntityDAO<CoverSleeve> CoverSleeveDAO) {
        this.CoverSleeveDAO = CoverSleeveDAO;
    }

    @Override
    public CoverSleeve parse(String text, Locale locale) throws ParseException {
        return CoverSleeveDAO.get(CoverSleeve.class, Integer.parseInt(text));
    }

    @Override
    public String print(CoverSleeve object, Locale locale) {
        return object.toString();
    }
}
