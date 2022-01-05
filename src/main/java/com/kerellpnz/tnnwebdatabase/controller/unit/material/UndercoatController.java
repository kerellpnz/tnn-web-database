package com.kerellpnz.tnnwebdatabase.controller.unit.material;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.material.UndercoatDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.UndercoatJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.AnticorrosiveCoatingTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.Undercoat;
import com.kerellpnz.tnnwebdatabase.exception.EntityNotFoundException;
import com.kerellpnz.tnnwebdatabase.model.UserModel;
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
@RequestMapping("/entity/Undercoats")
public class UndercoatController extends BaseEntityController {

    private final UndercoatDAO entityDAO;
    private final BaseEntityDAO<SheetGateValve> sheetGateValveDAO;
    private final BaseEntityDAO<UndercoatJournal> baseJournalDAO;
    private final BaseEntityDAO<AnticorrosiveCoatingTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> names;
    private List<String> factories;
    private List<AnticorrosiveCoatingTCP> entityTCPs;

    private List<Undercoat> entities;
    private String parameter;

    public UndercoatController(HttpSession session,
                                 JournalNumberDAO journalNumberDAO,
                                 UndercoatDAO entityDAO,
                                 BaseEntityDAO<SheetGateValve> sheetGateValveDAO,
                                 BaseEntityDAO<UndercoatJournal> baseJournalDAO,
                                 BaseEntityDAO<AnticorrosiveCoatingTCP> TCPDAO,
                                 InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.sheetGateValveDAO = sheetGateValveDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }
    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("title", "Грунт");
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            if ("duplicate".equals(parameter)) {
                mv.addObject("message", "ВНИМАНИЕ: Покрытие с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новое покрытие (Грунт)");
        Undercoat entity = new Undercoat();
        entityTCPs = TCPDAO.getAll(AnticorrosiveCoatingTCP.class);
        List<UndercoatJournal> entityJournals = new ArrayList<>();
        for (AnticorrosiveCoatingTCP tcp : entityTCPs) {
            UndercoatJournal entityJournal = new UndercoatJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }


    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики покрытия (Грунт)");
        Undercoat entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (UndercoatJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(AnticorrosiveCoatingTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("coatingClass", "Undercoats");
        mv.addObject("userClickCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/material/coating-editView");
        mv.addObject("title", title);
        mv.addObject("coatingClass", "Undercoats");
        mv.addObject("userClickEditCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, Undercoat entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        factories = entityDAO.getDistinctFactory(Undercoat.class);
        names = entityDAO.getDistinctName(Undercoat.class);
        mv.addObject("factories", factories);
        mv.addObject("names", names);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSheetGateValve")
    public String openSheetGateValve(@RequestParam("valveId") int valveId) {
        return "redirect:/entity/SheetGateValves/showFormForUpdate/" + valveId;
    }


    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") Undercoat entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(AnticorrosiveCoatingTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                UndercoatJournal entityJournal = new UndercoatJournal(tempTCP);
                entity.getEntityJournals().add(entityJournal);
            }
            else {
                List<UndercoatJournal> entityJournals = new ArrayList<>();
                UndercoatJournal entityJournal = new UndercoatJournal(tempTCP);
                entity.getEntityJournals().add(entityJournal);
                entityJournals.add(entityJournal);
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/material/coating-editView";
    }


    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") Undercoat entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        UndercoatJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(UndercoatJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/material/coating-editView";
    }


    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") Undercoat entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/material/coating-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entities == null)
            entities = getAll();
        for(Undercoat tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Покрытие с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (UndercoatJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null) {
                    isEmpty = false;
                    entity.setInputControlDate(journal.getDate());
                }
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
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
        return "redirect:/entity/Undercoats/showAll";
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/material/coating-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditCoatings", true);
        model.addAttribute("coatingClass", "Undercoats");
        model.addAttribute("title", "Характеристики покрытия (Грунт)");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("factories", factories);
        model.addAttribute("names", names);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        Undercoat entity = entityDAO.get(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/Undercoats/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(Undercoat entity, String newNumber) {
        Undercoat entityNew = new Undercoat(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (UndercoatJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new UndercoatJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new UndercoatJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Undercoat entity = entityDAO.get(Undercoat.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(Undercoat.class, id);
        return "redirect:/entity/Undercoats/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Undercoat> getAll() {
        entities = entityDAO.getAll(Undercoat.class);
        return entities;
    }
}
