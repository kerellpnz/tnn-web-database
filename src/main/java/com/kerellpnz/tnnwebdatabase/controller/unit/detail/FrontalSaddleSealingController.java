package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.FrontalSaddleSealingDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.FrontalSaddleSealingJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.FrontalSaddleSealingTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.FrontalSaddleSealing;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Saddle;
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
@RequestMapping("/entity/FrontalSaddleSealings")
public class FrontalSaddleSealingController extends BaseEntityController {

    private final FrontalSaddleSealingDAO entityDAO;
    private final BaseEntityDAO<Saddle> saddleDAO;
    private final BaseEntityDAO<FrontalSaddleSealingJournal> baseJournalDAO;
    private final BaseEntityDAO<FrontalSaddleSealingTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> materials;
    private List<String> drawings;
    private List<String> names;
    private List<FrontalSaddleSealingTCP> entityTCPs;

    private List<FrontalSaddleSealing> entities;
    private String parameter;

    @Autowired
    public FrontalSaddleSealingController(HttpSession session,
                                   JournalNumberDAO journalNumberDAO,
                                   FrontalSaddleSealingDAO entityDAO,
                                   BaseEntityDAO<Saddle> saddleDAO,
                                   BaseEntityDAO<FrontalSaddleSealingJournal> baseJournalDAO,
                                   BaseEntityDAO<FrontalSaddleSealingTCP> TCPDAO,
                                   InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.saddleDAO = saddleDAO;
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
        ModelAndView mv = modelInitializeEdit("Новое уплотнение ЗШ");
        FrontalSaddleSealing entity = new FrontalSaddleSealing();
        entityTCPs = TCPDAO.getAll(FrontalSaddleSealingTCP.class);
        List<FrontalSaddleSealingJournal> entityJournals = new ArrayList<>();
        for (FrontalSaddleSealingTCP tcp : entityTCPs) {
            FrontalSaddleSealingJournal entityJournal = new FrontalSaddleSealingJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики уплотнения ЗШ");
        FrontalSaddleSealing entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (FrontalSaddleSealingJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        if (entity.getSaddles() != null)
            entity.setAmountRemaining(entity.getAmount() - entity.getSaddles().size() - entity.getSheetGateValveCovers().size());
        entityTCPs = TCPDAO.getAll(FrontalSaddleSealingTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Уплотнения ЗШ");
        mv.addObject("sealingClass", "FrontalSaddleSealings");
        mv.addObject("userClickSealings", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/sealing-editView");
        mv.addObject("title", title);
        mv.addObject("sealingClass", "FrontalSaddleSealings");
        mv.addObject("userClickEditSealings", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, FrontalSaddleSealing entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        materials = entityDAO.getDistinctMaterial(FrontalSaddleSealing.class);
        drawings = entityDAO.getDistinctDrawing(FrontalSaddleSealing.class);
        names = entityDAO.getDistinctName(FrontalSaddleSealing.class);
        mv.addObject("materials", materials);
        mv.addObject("drawings", drawings);
        mv.addObject("names", names);
        mv.addObject("entity", entity);
        return mv;
    }

    @GetMapping("/openSaddle")
    public String openSaddle(@RequestParam("saddleId") int saddleId) {
        return "redirect:/entity/Saddles/showFormForUpdate/" + saddleId;
    }

    @GetMapping("/openValveCover")
    public String openValveCover(@RequestParam("coverId") int coverId) {
        return "redirect:/entity/SheetGateValveCovers/showFormForUpdate/" + coverId;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") FrontalSaddleSealing entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(FrontalSaddleSealingTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new FrontalSaddleSealingJournal(tempTCP));
            }
            else {
                List<FrontalSaddleSealingJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new FrontalSaddleSealingJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/sealing-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") FrontalSaddleSealing entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        FrontalSaddleSealingJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(FrontalSaddleSealingJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/sealing-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") FrontalSaddleSealing entity, BindingResult results, Model model) {
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
        for(FrontalSaddleSealing tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Уплотнение с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (FrontalSaddleSealingJournal journal: entity.getEntityJournals()) {
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
        if(entity.getSaddles() != null) {
            List<Saddle> tempSaddlesList = new ArrayList<>();
            for(Saddle saddle : entity.getSaddles()) {
                tempSaddlesList.add(saddleDAO.get(Saddle.class, saddle.getId()));
            }
            entity.setSaddles(tempSaddlesList);
        }
        else
            entity.setAmountRemaining(entity.getAmount());
        if (flag)
            entity.setStatus("Cоотв.");
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        return "redirect:/entity/FrontalSaddleSealings/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        FrontalSaddleSealing entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/FrontalSaddleSealings/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(FrontalSaddleSealing entity, String newNumber) {
        FrontalSaddleSealing entityNew = new FrontalSaddleSealing(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (FrontalSaddleSealingJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new FrontalSaddleSealingJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new FrontalSaddleSealingJournal(journal, entityNew));
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
        model.addAttribute("sealingClass", "FrontalSaddleSealings");
        model.addAttribute("title", "Характеристики уплотнения ЗШ");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("materials", materials);
        model.addAttribute("drawings", drawings);
        model.addAttribute("names", names);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        FrontalSaddleSealing entity = entityDAO.get(FrontalSaddleSealing.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(FrontalSaddleSealing.class, id);
        return "redirect:/entity/FrontalSaddleSealings/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<FrontalSaddleSealing> getAll() {
        entities = entityDAO.getAll(FrontalSaddleSealing.class);
        return entities;
    }
}
