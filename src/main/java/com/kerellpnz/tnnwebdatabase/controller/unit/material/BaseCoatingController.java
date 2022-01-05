package com.kerellpnz.tnnwebdatabase.controller.unit.material;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.material.CoatingDAO;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.AnticorrosiveCoatingTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.BaseAnticorrosiveCoating;
import com.kerellpnz.tnnwebdatabase.exception.EntityNotFoundException;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.hibernate.Hibernate;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class BaseCoatingController<K extends BaseJournal, T extends BaseAnticorrosiveCoating> extends BaseEntityController {

    private Supplier<K> supplierJournal;
    private Supplier<T> supplierCoating;
    private String title;

    private final CoatingDAO<T> entityDAO;
    private final BaseEntityDAO<SheetGateValve> sheetGateValveDAO;
    private final BaseEntityDAO<K> baseJournalDAO;
    private final BaseEntityDAO<AnticorrosiveCoatingTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> names;
    private List<String> factories;
    private List<AnticorrosiveCoatingTCP> entityTCPs;

    private List<T> entities;


    public BaseCoatingController(HttpSession session,
                                 JournalNumberDAO journalNumberDAO,
                                 CoatingDAO<T> entityDAO,
                                 BaseEntityDAO<SheetGateValve> sheetGateValveDAO,
                                 BaseEntityDAO<K> baseJournalDAO,
                                 BaseEntityDAO<AnticorrosiveCoatingTCP> TCPDAO,
                                 InspectorDAO inspectorDAO,
                                 Supplier<K> supplierJournal,
                                 Supplier<T> supplierCoating,
                                 String title) {
        super(session, journalNumberDAO);
        this.supplierJournal = supplierJournal;
        this.supplierCoating = supplierCoating;
        this.entityDAO = entityDAO;
        this.sheetGateValveDAO = sheetGateValveDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
        this.title = title;
    }

    private T createCoating() {
        return supplierCoating.get();
    }

    private K createJournal() {
        return supplierJournal.get();
    }

    public ModelAndView showAll(Class<T> coatingType) {
        ModelAndView mv = modelInitializeIndex(coatingType);
        mv.addObject("title", title);
        mv.addObject("userClickEntityView", true);
        return mv;
    }


    public ModelAndView showFormForAdd(Class<T> coatingType) {
        ModelAndView mv = modelInitializeEdit("Новое покрытие (" + title + ")", coatingType);
        T entity = createCoating();
        entityTCPs = TCPDAO.getAll(AnticorrosiveCoatingTCP.class);
        List<K> entityJournals = new ArrayList<>();
        for (AnticorrosiveCoatingTCP tcp : entityTCPs) {
            K entityJournal = createJournal();
            entityJournal.setPointId(tcp.getId());
            entityJournal.setPoint(tcp.getPoint());
            entityJournal.setDescription(tcp.getDescription());
            entityJournal.setPlaceOfControl(tcp.getPlaceOfControl());
            entityJournal.setDocuments(tcp.getDocument());
            entityJournals.add(entityJournal);
        }
//        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity, coatingType);
    }


    public ModelAndView showFormForUpdate(Class<T> coatingType, int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики покрытия (" + title + ")", coatingType);
        T entity = entityDAO.get(coatingType, id);
        if (entity == null)
            throw new EntityNotFoundException();
//        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSheetGateValves());
//        for (K entityJournal: entity.getEntityJournals()) {
//            if (entityJournal.getInspectorId() != null) {
//                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
//                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
//            }
//        }
        entityTCPs = TCPDAO.getAll(AnticorrosiveCoatingTCP.class);
        return modelInitializeObject(mv, entity, coatingType);
    }

    private ModelAndView modelInitializeIndex(Class<T> coatingType) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("coatingClass", coatingType.getSimpleName() +"s");
        mv.addObject("userClickCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title, Class<T> coatingType) {
        ModelAndView mv = new ModelAndView("./unit/material/coating-editView");
        mv.addObject("title", title);
        mv.addObject("coatingClass", coatingType.getSimpleName() +"s");
        mv.addObject("userClickEditCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, T entity, Class<T> CoatingType) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        factories = entityDAO.getDistinctFactory(CoatingType);
        names = entityDAO.getDistinctName(CoatingType);
        mv.addObject("factories", factories);
        mv.addObject("names", names);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSheetGateValve")
    public String openSheetGateValve(@RequestParam("valveId") int valveId) {
        return "redirect:/entity/SheetGateValves/showFormForUpdate/" + valveId;
    }


    public String addOperation(Class<T> coatingType, T entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(AnticorrosiveCoatingTCP.class, entity.getTempTCPId());
//            if (entity.getEntityJournals() != null) {
//                K entityJournal = createJournal();
//                entityJournal.setPointId(tempTCP.getId());
//                entityJournal.setPoint(tempTCP.getPoint());
//                entityJournal.setDescription(tempTCP.getDescription());
//                entityJournal.setPlaceOfControl(tempTCP.getPlaceOfControl());
//                entityJournal.setDocuments(tempTCP.getDocuments());
//                entity.getEntityJournals().add(entityJournal);
//            }
//            else {
//                List<K> entityJournals = new ArrayList<>();
//                K entityJournal = createJournal();
//                entityJournal.setPointId(tempTCP.getId());
//                entityJournal.setPoint(tempTCP.getPoint());
//                entityJournal.setDescription(tempTCP.getDescription());
//                entityJournal.setPlaceOfControl(tempTCP.getPlaceOfControl());
//                entityJournal.setDocuments(tempTCP.getDocuments());
//                entity.getEntityJournals().add(entityJournal);
//                entityJournals.add(entityJournal);
//                entity.setEntityJournals(entityJournals);
//            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(coatingType, model);
        return "./unit/material/coating-editView";
    }


    public String deleteOperation (Class<T> coatingType, Class<K> journalType, T entity, Model model, String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
//        K entityJournal = entity.getEntityJournals().get(i);
//        if (entityJournal.getId() != 0) {
//            baseJournalDAO.delete(journalType, entityJournal.getId());
//        }
//        entity.getEntityJournals().remove(i);
        modelInitialize(coatingType, model);
        model.addAttribute("entity", entity);
        return "./unit/material/coating-editView";
    }


    public String save(T entity, Class<T> coatingType, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(coatingType, model);
            return "./unit/material/coating-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        UserModel userModel = this.getUserModel();

        if (entities == null)
            entities = getAll(coatingType);
        int count = 0;
        for(T tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                count++;
                if (count == 1) {
                    if (entity.getId() == 0 || entity.getId() != tempEntity.getId())
                        return sendMessage(coatingType,  "ОШИБКА: АКП с такими сертификатными данными уже существует!", model);
                }
                else return sendMessage(coatingType, "ОШИБКА: АКП с такими сертификатными данными уже существует!", model);
            }
        }

//        if (entity.getEntityJournals() != null) {
//            for (K journal: entity.getEntityJournals()) {
//                String result = checkJournal(journal, userModel);
//                if (result.startsWith("ОШИБКА:")) {
//                    return sendMessage(coatingType, result, model);
//                }
//                if (result.equals("Не соответствует"))
//                    flag = false;
//                if (journal.getDate() != null) {
//                    isEmpty = false;
//                    entity.setInputControlDate(journal.getDate());
//                }
//            }
//        }
        if (isEmpty) {
            return sendMessage(coatingType, "ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if(entity.getSheetGateValves() != null) {
            List<SheetGateValve> tempValvesList = new ArrayList<>();
            for(SheetGateValve valve : entity.getSheetGateValves()) {
                tempValvesList.add(sheetGateValveDAO.get(SheetGateValve.class, valve.getId()));
            }
            entity.setSheetGateValves(tempValvesList);
        }
        if (flag)
            entity.setStatus("Cоотв.");
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        return "redirect:/entity/" + coatingType.getSimpleName() + "s/showAll";
    }

    private String sendMessage(Class<T> coatingType, String message, Model model) {
        modelInitialize(coatingType, model);
        model.addAttribute("message", message);
        return "./unit/material/coating-editView";
    }

    private void modelInitialize(Class<T> coatingType, Model model) {
        model.addAttribute("userClickEditCoatings", true);
        model.addAttribute("coatingClass", coatingType.getSimpleName() + "s");
        model.addAttribute("title", "Характеристики покрытия (" + title + ")");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("factories", factories);
        model.addAttribute("names", names);
    }


    public String delete(Class<T> coatingType, int id) throws EntityNotFoundException {
        T entity = entityDAO.get(coatingType, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(coatingType, id);
        return "redirect:/entity/" + coatingType.getSimpleName() + "s/showAll";
    }


    public List<T> getAll(Class<T> coatingType) {
        entities = entityDAO.getAll(coatingType);
        return entities;
    }
}
