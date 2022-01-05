package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class GateValveCaseFormatter implements Formatter<SheetGateValveCase> {

    private final BaseEntityDAO<SheetGateValveCase> SheetGateValveCaseDAO;

    @Autowired
    public GateValveCaseFormatter(BaseEntityDAO<SheetGateValveCase> SheetGateValveCaseDAO) {
        this.SheetGateValveCaseDAO = SheetGateValveCaseDAO;
    }

    @Override
    public SheetGateValveCase parse(String text, Locale locale) throws ParseException {
        return SheetGateValveCaseDAO.get(SheetGateValveCase.class, Integer.parseInt(text));
    }

    @Override
    public String print(SheetGateValveCase object, Locale locale) {
        return object.toString();
    } 
}
