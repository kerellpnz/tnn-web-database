package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class GateValveCoverFormatter implements Formatter<SheetGateValveCover> {

    private final BaseEntityDAO<SheetGateValveCover> SheetGateValveCoverDAO;

    @Autowired
    public GateValveCoverFormatter(BaseEntityDAO<SheetGateValveCover> SheetGateValveCoverDAO) {
        this.SheetGateValveCoverDAO = SheetGateValveCoverDAO;
    }

    @Override
    public SheetGateValveCover parse(String text, Locale locale) throws ParseException {
        return SheetGateValveCoverDAO.get(SheetGateValveCover.class, Integer.parseInt(text));
    }

    @Override
    public String print(SheetGateValveCover object, Locale locale) {
        return object.toString();
    } 
}
