package com.kerellpnz.tnnwebdatabase.controller.unit.material;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.material.AbovegroundCoatingDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.AbovegroundCoatingJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.AnticorrosiveCoatingTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.AbovegroundCoating;
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
@RequestMapping("/entity/AbovegroundCoatings")
public class AbovegroundCoatingController extends BaseEntityController {

    private final AbovegroundCoatingDAO entityDAO;
    private final BaseEntityDAO<SheetGateValve> sheetGateValveDAO;
    private final BaseEntityDAO<AbovegroundCoatingJournal> baseJournalDAO;
    private final BaseEntityDAO<AnticorrosiveCoatingTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> names;
    private List<String> factories;
    private List<String> colors;
    private List<AnticorrosiveCoatingTCP> entityTCPs;

    private List<AbovegroundCoating> entities;
    private String parameter;


    public AbovegroundCoatingController(HttpSession session,
                               JournalNumberDAO journalNumberDAO,
                               AbovegroundCoatingDAO entityDAO,
                               BaseEntityDAO<SheetGateValve> sheetGateValveDAO,
                               BaseEntityDAO<AbovegroundCoatingJournal> baseJournalDAO,
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
        mv.addObject("title", "Эмаль");
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
        ModelAndView mv = modelInitializeEdit("Новое покрытие (Эмаль)");
        AbovegroundCoating entity = new AbovegroundCoating();
        entityTCPs = TCPDAO.getAll(AnticorrosiveCoatingTCP.class);
        List<AbovegroundCoatingJournal> entityJournals = new ArrayList<>();
        for (AnticorrosiveCoatingTCP tcp : entityTCPs) {
            AbovegroundCoatingJournal entityJournal = new AbovegroundCoatingJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }


    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики покрытия (Эмаль)");
        AbovegroundCoating entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (AbovegroundCoatingJournal entityJournal: entity.getEntityJournals()) {
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
        mv.addObject("coatingClass", "AbovegroundCoatings");
        mv.addObject("userClickCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/material/coating-editView");
        mv.addObject("title", title);
        mv.addObject("coatingClass", "AbovegroundCoatings");
        mv.addObject("userClickEditCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, AbovegroundCoating entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        factories = entityDAO.getDistinctFactory(AbovegroundCoating.class);
        names = entityDAO.getDistinctName(AbovegroundCoating.class);
        colors = entityDAO.getDistinctColor(AbovegroundCoating.class);
        mv.addObject("factories", factories);
        mv.addObject("names", names);
        mv.addObject("colors", colors);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSheetGateValve")
    public String openSheetGateValve(@RequestParam("valveId") int valveId) {
        return "redirect:/entity/SheetGateValves/showFormForUpdate/" + valveId;
    }


    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") AbovegroundCoating entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(AnticorrosiveCoatingTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                AbovegroundCoatingJournal entityJournal = new AbovegroundCoatingJournal(tempTCP);
                entity.getEntityJournals().add(entityJournal);
            }
            else {
                List<AbovegroundCoatingJournal> entityJournals = new ArrayList<>();
                AbovegroundCoatingJournal entityJournal = new AbovegroundCoatingJournal(tempTCP);
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
    public String deleteOperation (@ModelAttribute("entity") AbovegroundCoating entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        AbovegroundCoatingJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(AbovegroundCoatingJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/material/coating-editView";
    }


    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") AbovegroundCoating entity, BindingResult results, Model model) {
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
        for(AbovegroundCoating tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Покрытие с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (AbovegroundCoatingJournal journal: entity.getEntityJournals()) {
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
        return "redirect:/entity/AbovegroundCoatings/showAll";
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/material/coating-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditCoatings", true);
        model.addAttribute("coatingClass", "AbovegroundCoatings");
        model.addAttribute("title", "Характеристики покрытия (Эмаль)");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("factories", factories);
        model.addAttribute("names", names);
        model.addAttribute("colors", colors);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        AbovegroundCoating entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/AbovegroundCoatings/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(AbovegroundCoating entity, String newNumber) {
        AbovegroundCoating entityNew = new AbovegroundCoating(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (AbovegroundCoatingJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new AbovegroundCoatingJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new AbovegroundCoatingJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        AbovegroundCoating entity = entityDAO.get(AbovegroundCoating.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(AbovegroundCoating.class, id);
        return "redirect:/entity/AbovegroundCoatings/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<AbovegroundCoating> getAll() {
        entities = entityDAO.getAll(AbovegroundCoating.class);
        return entities;
    }
}
