package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.ShearPinDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.ShearPinJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.ShearPinTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseValveWithShearPin;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ShearPin;
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
@RequestMapping("/entity/ShearPins")
public class ShearPinController extends BaseEntityController {

    private final ShearPinDAO entityDAO;
    private final BaseEntityDAO<ShearPinJournal> baseJournalDAO;
    private final BaseEntityDAO<ShearPinTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<String> diameters;
    private List<String> tensileStrengths;
    private List<String> dns;
    private List<ShearPinTCP> entityTCPs;

    private List<ShearPin> entities;
    private String parameter;

    @Autowired
    public ShearPinController(HttpSession session,
                               JournalNumberDAO journalNumberDAO,
                              ShearPinDAO entityDAO,
                               BaseEntityDAO<ShearPinJournal> baseJournalDAO,
                               BaseEntityDAO<ShearPinTCP> TCPDAO,
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
                mv.addObject("message", "ВНИМАНИЕ: Штифты с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новые штифты");
        ShearPin entity = new ShearPin();
        entityTCPs = TCPDAO.getAll(ShearPinTCP.class);
        List<ShearPinJournal> entityJournals = new ArrayList<>();
        for (ShearPinTCP tcp : entityTCPs) {
            ShearPinJournal entityJournal = new ShearPinJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики штифтов");
        ShearPin entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (ShearPinJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        int amountUsed = 0;
        if (entity.getBaseValveWithShearPins() != null) {
            for(BaseValveWithShearPin withShearPin : entity.getBaseValveWithShearPins()) {
                amountUsed += withShearPin.getShearPinAmount();
            }
        }
        entity.setAmountRemaining(entity.getAmount() - amountUsed);
        entityTCPs = TCPDAO.getAll(ShearPinTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Штифты");
        mv.addObject("screwClass", "ShearPins");
        mv.addObject("userClickShearPins", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/shear-pin-editView");
        mv.addObject("title", title);
        mv.addObject("screwClass", "ShearPins");
        mv.addObject("userClickEditShearPins", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, ShearPin entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        drawings = entityDAO.getDistinctDrawing();
        diameters = entityDAO.getDistinctDiameter();
        tensileStrengths = entityDAO.getDistinctTensileStrength();
        dns = serviceClass.getDns();
        mv.addObject("drawings", drawings);
        mv.addObject("diameters", diameters);
        mv.addObject("tensileStrengths", tensileStrengths);
        mv.addObject("dns", dns);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSheetGateValve")
    public String openSheetGateValve(@RequestParam("valveId") int valveId) {
        return "redirect:/entity/SheetGateValves/showFormForUpdate/" + valveId;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") ShearPin entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(ShearPinTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new ShearPinJournal(tempTCP));
            }
            else {
                List<ShearPinJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new ShearPinJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/shear-pin-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") ShearPin entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        ShearPinJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(ShearPinJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/shear-pin-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") ShearPin entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/shear-pin-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entities == null)
            entities = getAll();
        for(ShearPin tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Штифты с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (ShearPinJournal journal: entity.getEntityJournals()) {
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
        return "redirect:/entity/ShearPins/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number,
                          @RequestParam("diameter") String diameter, @RequestParam("tensileStrength") String tensileStrength,
                          @RequestParam("pull") String pull, @RequestParam("amount") Integer amount) {
        ShearPin entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number,
                 diameter,  tensileStrength,
                 pull, amount);
        return "redirect:/entity/ShearPins/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(ShearPin entity, String number,
                                     String diameter, String tensileStrength,
                                     String pull, Integer amount) {
        ShearPin entityNew = new ShearPin(entity, number, diameter, tensileStrength,
                pull, amount);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (ShearPinJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new ShearPinJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new ShearPinJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/shear-pin-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditShearPins", true);
        model.addAttribute("screwClass", "ShearPins");
        model.addAttribute("title", "Характеристики штифтов");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("dns", dns);
        model.addAttribute("drawings", drawings);
        model.addAttribute("diameters", diameters);
        model.addAttribute("tensileStrengths", tensileStrengths);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        ShearPin entity = entityDAO.get(ShearPin.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(ShearPin.class, id);
        return "redirect:/entity/ShearPins/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<ShearPin> getAll() {
        entities = entityDAO.getAll(ShearPin.class);
        return entities;
    }
}
