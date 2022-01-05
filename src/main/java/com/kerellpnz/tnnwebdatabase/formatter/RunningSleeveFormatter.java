package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.RunningSleeve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class RunningSleeveFormatter implements Formatter<RunningSleeve> {

    private final BaseEntityDAO<RunningSleeve> RunningSleeveDAO;

    @Autowired
    public RunningSleeveFormatter(BaseEntityDAO<RunningSleeve> RunningSleeveDAO) {
        this.RunningSleeveDAO = RunningSleeveDAO;
    }

    @Override
    public RunningSleeve parse(String text, Locale locale) throws ParseException {
        return RunningSleeveDAO.get(RunningSleeve.class, Integer.parseInt(text));
    }

    @Override
    public String print(RunningSleeve object, Locale locale) {
        return object.toString();
    }
}
