package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class PidFormatter implements Formatter<PID> {

    private final BaseEntityDAO<PID> pidDAO;

    @Autowired
    public PidFormatter(BaseEntityDAO<PID> pidDAO) {
        this.pidDAO = pidDAO;
    }

    @Override
    public PID parse(String text, Locale locale) throws ParseException {
        return pidDAO.get(PID.class, Integer.parseInt(text));
    }

    @Override
    public String print(PID object, Locale locale) {
        return object.toString();
    }
}
