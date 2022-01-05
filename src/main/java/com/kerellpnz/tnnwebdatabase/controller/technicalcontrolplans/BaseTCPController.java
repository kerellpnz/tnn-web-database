package com.kerellpnz.tnnwebdatabase.controller.technicalcontrolplans;

import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public abstract class BaseTCPController<T extends BaseTCP> {
    @Autowired
    private ServiceClass serviceClass;


    public abstract ModelAndView showTCPs(String operation);

    public abstract String saveTCP(T tcp) ;

    public abstract String deleteTCP(int id) ;

    public abstract List<T> getTCPs();

    public abstract T getTCPModel() ;

    @ModelAttribute("productTypes")
    public List<String> getProductTypes() {
        return serviceClass.getProductTypes();
    }

    @ModelAttribute("operationTypes")
    public List<String> getOperationTypes() {
        return serviceClass.getOperationTypes();
    }

    @ModelAttribute("placesOfControl")
    public List<String> getPlacesOfControl() {
        return serviceClass.getPlacesOfControlOption();
    }

    @ModelAttribute("documents")
    public List<String> getDocuments() {
        return serviceClass.getDocumentsOption();
    }
}
