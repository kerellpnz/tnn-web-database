package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve008;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CoverSleeve008Formatter implements Formatter<CoverSleeve008> {

    private final BaseEntityDAO<CoverSleeve008> CoverSleeve008DAO;

    @Autowired
    public CoverSleeve008Formatter(BaseEntityDAO<CoverSleeve008> CoverSleeve008DAO) {
        this.CoverSleeve008DAO = CoverSleeve008DAO;
    }

    @Override
    public CoverSleeve008 parse(String text, Locale locale) throws ParseException {
        return CoverSleeve008DAO.get(CoverSleeve008.class, Integer.parseInt(text));
    }

    @Override
    public String print(CoverSleeve008 object, Locale locale) {
        return object.toString();
    }
}