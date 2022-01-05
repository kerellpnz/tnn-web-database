package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.detail.ScrewNutDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.ScrewNutJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.ScrewNutTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseValveWithScrewNut;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ScrewNut;
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
@RequestMapping("/entity/ScrewNuts")
public class ScrewNutController extends BaseEntityController {

    private final ScrewNutDAO entityDAO;
    private final BaseEntityDAO<ScrewNutJournal> baseJournalDAO;
    private final BaseEntityDAO<ScrewNutTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<String> dns;
    private List<ScrewNutTCP> entityTCPs;

    private List<ScrewNut> entities;
    private String parameter;

    @Autowired
    public ScrewNutController(HttpSession session,
                               JournalNumberDAO journalNumberDAO,
                               ScrewNutDAO entityDAO,
                               BaseEntityDAO<ScrewNutJournal> baseJournalDAO,
                               BaseEntityDAO<ScrewNutTCP> TCPDAO,
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
                mv.addObject("message", "ВНИМАНИЕ: Гайки с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новые гайки");
        ScrewNut entity = new ScrewNut();
        entityTCPs = TCPDAO.getAll(ScrewNutTCP.class);
        List<ScrewNutJournal> entityJournals = new ArrayList<>();
        for (ScrewNutTCP tcp : entityTCPs) {
            ScrewNutJournal entityJournal = new ScrewNutJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики гаек");
        ScrewNut entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (ScrewNutJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        int amountUsed = 0;
        if (entity.getBaseValveWithScrewNuts() != null) {
            for(BaseValveWithScrewNut withScrewNut : entity.getBaseValveWithScrewNuts()) {
                amountUsed += withScrewNut.getScrewNutAmount();
            }
        }
        entity.setAmountRemaining(entity.getAmount() - amountUsed);
        entityTCPs = TCPDAO.getAll(ScrewNutTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Гайки");
        mv.addObject("screwClass", "ScrewNuts");
        mv.addObject("userClickScrews", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/screw-editView");
        mv.addObject("title", title);
        mv.addObject("screwClass", "ScrewNuts");
        mv.addObject("userClickEditScrews", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, ScrewNut entity) {
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
    public String addOperation(@ModelAttribute("entity") ScrewNut entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(ScrewNutTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new ScrewNutJournal(tempTCP));
            }
            else {
                List<ScrewNutJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new ScrewNutJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/screw-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") ScrewNut entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        ScrewNutJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(ScrewNutJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/screw-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") ScrewNut entity, BindingResult results, Model model) {
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
        for(ScrewNut tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Гайки с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (ScrewNutJournal journal: entity.getEntityJournals()) {
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
        return "redirect:/entity/ScrewNuts/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        ScrewNut entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/ScrewNuts/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(ScrewNut entity, String newNumber) {
        ScrewNut entityNew = new ScrewNut(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (ScrewNutJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new ScrewNutJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new ScrewNutJournal(journal, entityNew));
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
        model.addAttribute("screwClass", "ScrewNuts");
        model.addAttribute("title", "Характеристики гаек");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("dns", dns);
        model.addAttribute("drawings", drawings);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        ScrewNut entity = entityDAO.get(ScrewNut.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(ScrewNut.class, id);
        return "redirect:/entity/ScrewNuts/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ScrewNut> getAll() {
        entities = entityDAO.getAll(ScrewNut.class);
        return entities;
    }
}
