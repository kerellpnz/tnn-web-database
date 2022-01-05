package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.detail.MainFlangeSealingDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.MainFlangeSealingJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.MainFlangeSealingTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.MainFlangeSealing;
import com.kerellpnz.tnnwebdatabase.exception.EntityNotFoundException;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/entity/MainFlangeSealings")
public class MainFlangeSealingController extends BaseEntityController {

    private final MainFlangeSealingDAO entityDAO;
    private final BaseEntityDAO<SheetGateValve> sheetGateValveDAO;
    private final BaseEntityDAO<MainFlangeSealingJournal> baseJournalDAO;
    private final BaseEntityDAO<MainFlangeSealingTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> materials;
    private List<String> drawings;
    private List<String> names;
    private List<MainFlangeSealingTCP> entityTCPs;

    private List<MainFlangeSealing> entities;
    private String parameter;

    @Autowired
    public MainFlangeSealingController(HttpSession session,
                                          JournalNumberDAO journalNumberDAO,
                                          MainFlangeSealingDAO entityDAO,
                                          BaseEntityDAO<SheetGateValve> sheetGateValveDAO,
                                          BaseEntityDAO<MainFlangeSealingJournal> baseJournalDAO,
                                          BaseEntityDAO<MainFlangeSealingTCP> TCPDAO,
                                          InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.sheetGateValveDAO = sheetGateValveDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll() {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            if ("duplicate".equals(parameter)) {
                mv.addObject("message", "ВНИМАНИЕ: Уплотнение с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новое уплотнение основного разъема");
        MainFlangeSealing entity = new MainFlangeSealing();
        entityTCPs = TCPDAO.getAll(MainFlangeSealingTCP.class);
        List<MainFlangeSealingJournal> entityJournals = new ArrayList<>();
        for (MainFlangeSealingTCP tcp : entityTCPs) {
            MainFlangeSealingJournal entityJournal = new MainFlangeSealingJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики уплотнения основного разъема");
        MainFlangeSealing entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (MainFlangeSealingJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(MainFlangeSealingTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Уплотнения основного разъема");
        mv.addObject("sealingClass", "MainFlangeSealings");
        mv.addObject("userClickSealings", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/sealing-editView");
        mv.addObject("title", title);
        mv.addObject("sealingClass", "MainFlangeSealings");
        mv.addObject("userClickEditSealings", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, MainFlangeSealing entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        materials = entityDAO.getDistinctMaterial(MainFlangeSealing.class);
        drawings = entityDAO.getDistinctDrawing(MainFlangeSealing.class);
        names = entityDAO.getDistinctName(MainFlangeSealing.class);
        mv.addObject("materials", materials);
        mv.addObject("drawings", drawings);
        mv.addObject("names", names);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSheetGateValve")
    public String openSheetGateValve(@RequestParam("valveId") int valveId) {
        return "redirect:/entity/SheetGateValves/showFormForUpdate/" + valveId;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") MainFlangeSealing entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(MainFlangeSealingTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new MainFlangeSealingJournal(tempTCP));
            }
            else {
                List<MainFlangeSealingJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new MainFlangeSealingJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/sealing-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") MainFlangeSealing entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        MainFlangeSealingJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(MainFlangeSealingJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/sealing-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") MainFlangeSealing entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/sealing-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entities == null)
            entities = getAll();
        for(MainFlangeSealing tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Уплотнение с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (MainFlangeSealingJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if(entity.getSheetGateValves() != null) {
            List<SheetGateValve> tempSheetGateValveList = new ArrayList<>();
            for(SheetGateValve sheetGateValve : entity.getSheetGateValves()) {
                tempSheetGateValveList.add(sheetGateValveDAO.get(SheetGateValve.class, sheetGateValve.getId()));
            }
            entity.setSheetGateValves(tempSheetGateValveList);
        }
//        else
//            entity.setAmountRemaining(entity.getAmount());
        if (flag)
            entity.setStatus("Cоотв.");
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        return "redirect:/entity/MainFlangeSealings/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        MainFlangeSealing entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/MainFlangeSealings/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(MainFlangeSealing entity, String newNumber) {
        MainFlangeSealing entityNew = new MainFlangeSealing(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (MainFlangeSealingJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new MainFlangeSealingJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new MainFlangeSealingJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/sealing-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditSealings", true);
        model.addAttribute("sealingClass", "MainFlangeSealings");
        model.addAttribute("title", "Характеристики уплотнения основного разъема");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("materials", materials);
        model.addAttribute("drawings", drawings);
        model.addAttribute("names", names);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        MainFlangeSealing entity = entityDAO.get(MainFlangeSealing.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(MainFlangeSealing.class, id);
        return "redirect:/entity/MainFlangeSealings/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<MainFlangeSealing> getAll() {
        entities = entityDAO.getAll(MainFlangeSealing.class);
        return entities;
    }
}
