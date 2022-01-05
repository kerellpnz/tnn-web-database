package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class CaseBottomFormatter implements Formatter<CaseBottom> {

    private final BaseEntityDAO<CaseBottom> caseBottomDAO;

    @Autowired
    public CaseBottomFormatter(BaseEntityDAO<CaseBottom> caseBottomDAO) {
        this.caseBottomDAO = caseBottomDAO;
    }

    @Override
    public CaseBottom parse(String text, Locale locale) throws ParseException {
        return caseBottomDAO.get(CaseBottom.class, Integer.parseInt(text));
    }

    @Override
    public String print(CaseBottom object, Locale locale) {
        return object.toString();
    }
}
