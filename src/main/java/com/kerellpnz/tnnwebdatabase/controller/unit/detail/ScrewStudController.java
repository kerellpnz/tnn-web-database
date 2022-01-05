package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.ScrewStudDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.ScrewStudJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.ScrewStudTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseValveWithScrewStud;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ScrewStud;
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
@RequestMapping("/entity/ScrewStuds")
public class ScrewStudController extends BaseEntityController {

    private final ScrewStudDAO entityDAO;
    private final BaseEntityDAO<ScrewStudJournal> baseJournalDAO;
    private final BaseEntityDAO<ScrewStudTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<String> dns;
    private List<ScrewStudTCP> entityTCPs;

    private List<ScrewStud> entities;
    private String parameter;

    @Autowired
    public ScrewStudController(HttpSession session,
                                          JournalNumberDAO journalNumberDAO,
                                          ScrewStudDAO entityDAO,
                                          BaseEntityDAO<ScrewStudJournal> baseJournalDAO,
                                          BaseEntityDAO<ScrewStudTCP> TCPDAO,
                                          InspectorDAO inspectorDAO,
                                          ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            if ("duplicate".equals(parameter)) {
                mv.addObject("message", "ВНИМАНИЕ: Шпильки с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новые шпильки");
        ScrewStud entity = new ScrewStud();
        entityTCPs = TCPDAO.getAll(ScrewStudTCP.class);
        List<ScrewStudJournal> entityJournals = new ArrayList<>();
        for (ScrewStudTCP tcp : entityTCPs) {
            ScrewStudJournal entityJournal = new ScrewStudJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики шпилек");
        ScrewStud entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (ScrewStudJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        int amountUsed = 0;
        if (entity.getBaseValveWithScrewStuds() != null) {
            for(BaseValveWithScrewStud withScrewStud : entity.getBaseValveWithScrewStuds()) {
                amountUsed += withScrewStud.getScrewStudAmount();
            }
        }
        entity.setAmountRemaining(entity.getAmount() - amountUsed);
        entityTCPs = TCPDAO.getAll(ScrewStudTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Шпильки");
        mv.addObject("screwClass", "ScrewStuds");
        mv.addObject("userClickScrews", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/screw-editView");
        mv.addObject("title", title);
        mv.addObject("screwClass", "ScrewStuds");
        mv.addObject("userClickEditScrews", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, ScrewStud entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        drawings = entityDAO.getDistinctDrawing();
        dns = serviceClass.getDns();
        mv.addObject("drawings", drawings);
        mv.addObject("dns", dns);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSheetGateValve")
    public String openSheetGateValve(@RequestParam("valveId") int valveId) {
        return "redirect:/entity/SheetGateValves/showFormForUpdate/" + valveId;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") ScrewStud entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(ScrewStudTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new ScrewStudJournal(tempTCP));
            }
            else {
                List<ScrewStudJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new ScrewStudJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/screw-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") ScrewStud entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        ScrewStudJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(ScrewStudJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/screw-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") ScrewStud entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/screw-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entities == null)
            entities = getAll();
        for(ScrewStud tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Шпильки с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (ScrewStudJournal journal: entity.getEntityJournals()) {
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
        else
            entity.setAmountRemaining(entity.getAmount());
        if (flag)
            entity.setStatus("Cоотв.");
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        return "redirect:/entity/ScrewStuds/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        ScrewStud entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/ScrewStuds/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(ScrewStud entity, String newNumber) {
        ScrewStud entityNew = new ScrewStud(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (ScrewStudJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new ScrewStudJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new ScrewStudJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/screw-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditScrews", true);
        model.addAttribute("screwClass", "ScrewStuds");
        model.addAttribute("title", "Характеристики шпилек");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("dns", dns);
        model.addAttribute("drawings", drawings);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        ScrewStud entity = entityDAO.get(ScrewStud.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(ScrewStud.class, id);
        return "redirect:/entity/ScrewStuds/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ScrewStud> getAll() {
        entities = entityDAO.getAll(ScrewStud.class);
        return entities;
    }
}
