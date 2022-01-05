package com.kerellpnz.tnnwebdatabase.controller.unit;

import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;


public class BaseEntityController {

    private final HttpSession session;
    private final JournalNumberDAO journalNumberDAO;

    public BaseEntityController(HttpSession session, JournalNumberDAO journalNumberDAO) {
        this.session = session;
        this.journalNumberDAO = journalNumberDAO;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    public UserModel getUserModel() {
        UserModel userModel = (UserModel) session.getAttribute("userModel");
        if (userModel.getJournalNumber() == null) {
            userModel.setJournalNumber(journalNumberDAO.getLastOpenJournalNumber());
            session.setAttribute("userModel", userModel);
        }
        return userModel;
    }

    public String checkJournal(BaseJournal journal, UserModel userModel) {
        String status = "Соответствует";
        if (journal.getDate() != null && journal.getInspectorId() == null) {
            if (!journal.getRemarkIssued().isBlank() && journal.getRemarkClosed().isBlank()) {
                if (!journal.getRemarkIssued().matches("^(\\d+)$")) {
                    if (!journal.getRemarkIssued().matches("^(\\d{2}/П/ЗА/ПТПА/\\d{2})$"))
                        return "ОШИБКА: Введите только номер замечания (1256) или предписания (01/П/ЗА/ПТПА/21)!";
                }
                status = "Не соответствует";
                journal.setStatus(status);
                journal.setRemarkInspector(userModel.getName());
                journal.setDateOfRemark(journal.getDate());
            }
            else {
                journal.setStatus(status);
            }
            journal.setInspectorId(userModel.getId());
            journal.setJournalNumber(userModel.getJournalNumber());
            return status;
        }
        else if (journal.getStatus().equals("Не соответствует")) {
            if (!journal.getRemarkIssued().isBlank() && !journal.getRemarkClosed().isBlank()) {
                if (journal.getClosingDate() == null) {
                    return "ОШИБКА: Укажите дату закрытия замечания/предписания!";
                }
                journal.setInspectorId(userModel.getId());
                journal.setDate(journal.getClosingDate());
                journal.setStatus(status);
                return status;
            }
            else if(journal.getClosingDate() != null && journal.getRemarkClosed().isBlank()) {
                return "ОШИБКА: Укажите номер закрываемого замечания/предписания!";
            }
            else {
                return "Не соответствует";
            }
        }
        else if (journal.getDate() == null && (!journal.getRemarkIssued().isBlank() || !journal.getComment().isBlank())) {
            return "ОШИБКА: Имеются заполненные поля \"Замечание\" или \"Примечание\" при отсутствующей Дате!";
        }
        return status;
    }

    @ModelAttribute("entityId")
    public BaseEntity getBaseEntityModel() {
        return new BaseEntity();
    }
}
