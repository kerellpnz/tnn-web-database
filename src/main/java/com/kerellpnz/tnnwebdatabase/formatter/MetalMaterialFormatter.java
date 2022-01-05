package com.kerellpnz.tnnwebdatabase.formatter;

import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class MetalMaterialFormatter implements Formatter<MetalMaterial> {

    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;

    @Autowired
    public MetalMaterialFormatter(BaseEntityDAO<MetalMaterial> metalMaterialDAO) {
        this.metalMaterialDAO = metalMaterialDAO;
    }

    @Override
    public String print(MetalMaterial object, Locale locale) {
        return object.toString();
    }

    @Override
    public MetalMaterial parse(String text, Locale locale) throws ParseException {
        return metalMaterialDAO.get(MetalMaterial.class, Integer.parseInt(text));
    }
}
