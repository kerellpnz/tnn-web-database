package com.kerellpnz.tnnwebdatabase.controller.general;

import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private HttpSession session;

    @Autowired
    private InspectorDAO inspectorDAO;

    private UserModel userModel = null;

    @ModelAttribute("userModel")
    public UserModel getUserModel() {
        if (session.getAttribute("userModel")== null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();
            if (!login.equals("anonymousUser")) {
                Inspector inspector = inspectorDAO.getByLogin(login);
                if (inspector != null) {
                    userModel = new UserModel();
                    userModel.setName(inspector.getName());
                    userModel.setApointment(inspector.getApointment());
                    userModel.setId(inspector.getId());
                    userModel.setLogin(inspector.getLogin());
                    userModel.setRole(inspector.getRole());
                    session.setAttribute("userModel", userModel);
                    return userModel;
                }
            }
        }
        return (UserModel) session.getAttribute("userModel");
    }
}
