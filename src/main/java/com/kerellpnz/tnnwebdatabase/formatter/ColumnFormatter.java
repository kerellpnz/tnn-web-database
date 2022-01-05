package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class ColumnFormatter implements Formatter<Column> {

    private final BaseEntityDAO<Column> ColumnDAO;

    @Autowired
    public ColumnFormatter(BaseEntityDAO<Column> ColumnDAO) {
        this.ColumnDAO = ColumnDAO;
    }

    @Override
    public Column parse(String text, Locale locale) throws ParseException {
        return ColumnDAO.get(Column.class, Integer.parseInt(text));
    }

    @Override
    public String print(Column object, Locale locale) {
        return object.toString();
    }
}
