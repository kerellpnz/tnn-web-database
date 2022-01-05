package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.FlangeDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.FlangeJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.FlangeTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Flange;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
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
@RequestMapping("/entity/Flanges")
public class FlangeController extends BaseEntityController {

    private final FlangeDAO entityDAO;
    private final BaseEntityDAO<FlangeJournal> baseJournalDAO;
    private final BaseEntityDAO<FlangeTCP> TCPDAO;
    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<FlangeTCP> entityTCPs;
    private List<MetalMaterial> metalMaterials;
    private List<String> dns;

    private List<Flange> entities;
    private String parameter;

    public FlangeController(HttpSession session,
                                JournalNumberDAO journalNumberDAO,
                                FlangeDAO entityDAO,
                                BaseEntityDAO<FlangeJournal> baseJournalDAO,
                                BaseEntityDAO<FlangeTCP> TCPDAO,
                                BaseEntityDAO<MetalMaterial> metalMaterialDAO,
                                InspectorDAO inspectorDAO,
                                ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.metalMaterialDAO = metalMaterialDAO;
        this.inspectorDAO = inspectorDAO;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            switch (parameter) {
                case "badMetal" -> mv.addObject("message", "ВНИМАНИЕ: Выбранный прокат имеет статус \"НЕ СООТВ.\"," +
                        " поэтому этот же статус применен к фланцу.");
                case "error" -> mv.addObject("message", "Номер фланца не является числом!");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Фланец с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новый фланец");
        Flange entity = new Flange();
        entityTCPs = TCPDAO.getAll(FlangeTCP.class);
        List<FlangeJournal> entityJournals = new ArrayList<>();
        for (FlangeTCP tcp : entityTCPs) {
            FlangeJournal entityJournal = new FlangeJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity, entityTCPs);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        Flange entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}/{name}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId, @PathVariable String name) throws EntityNotFoundException {
        Flange entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        entity.setReqName(name);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(Flange entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики фланца");
        for (FlangeJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(FlangeTCP.class);
        return modelInitializeObject(mv, entity, entityTCPs);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Фланец");
        mv.addObject("commonDetailClass", "Flanges");
        mv.addObject("userClickCommonDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/common-details-editView");
        mv.addObject("title", title);
        mv.addObject("commonDetailClass", "Flanges");
        mv.addObject("userClickEditCommonDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, Flange entity, List<FlangeTCP> entityTCPs) {
        metalMaterials = metalMaterialDAO.getAll(MetalMaterial.class);
        drawings = entityDAO.getDistinctDrawing();
        dns = serviceClass.getDns();
        mv.addObject("metalMaterials", metalMaterials);
        mv.addObject("drawings", drawings);
        mv.addObject("dns", dns);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);
        return mv;
    }


    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") Flange entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(FlangeTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new FlangeJournal(tempTCP));
            }
            else {
                List<FlangeJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new FlangeJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") Flange entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        FlangeJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(FlangeJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") Flange entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/common-details-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if(entity.getMaterial().isBlank() || entity.getMelt().isBlank()) {
            return sendMessage("Введите плавку/материал!", model);
        }

        if (entities == null)
            entities = getAll();
        for(Flange tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: Фланец с таким характеристиками уже существует!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (FlangeJournal journal: entity.getEntityJournals()) {
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
        if (flag)
            entity.setStatus("Cоотв.");
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        if (entity.getReqId() != null) {
            if (entity.getReqName().equals("case"))
                return "redirect:/entity/SheetGateValveCases/showFormForUpdate/" + entity.getReqId();
            else
                return "redirect:/entity/SheetGateValveCovers/showFormForUpdate/" + entity.getReqId();
        }
        else
            return "redirect:/entity/Flanges/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/common-details-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditCommonDetails", true);
        model.addAttribute("title", "Характеристики фланца");
        model.addAttribute("editMode", true);
        model.addAttribute("commonDetailClass", "Flanges");
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("drawings", drawings);
        model.addAttribute("metalMaterials", metalMaterials);
        model.addAttribute("dns", dns);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        Flange entity = entityDAO.get(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/Flanges/showAll?parameter=" + parameter;
    }

    @PostMapping("/multi-copy")
    public ModelAndView multiCopyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("quantity") Integer quantity){
        Flange entity = entityDAO.get(entityId.getId());
        if (!entity.getNumber().matches("\\d+")) {
            return showAll("error");
        }
        Integer number = Integer.parseInt(entity.getNumber());
        parameter = "success";
        for (int i = 0; i < quantity; i++) {
            String newNumber = (++number).toString();
            newEntityInitialize(entity, newNumber);
        }
        return showAll(parameter);
    }

    private void newEntityInitialize(Flange entity, String newNumber) {
        Flange entityNew = new Flange(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (FlangeJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new FlangeJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new FlangeJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Flange entity = entityDAO.get(Flange.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(Flange.class, id);
        return "redirect:/entity/Flanges/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Flange> getAll() {
        entities = entityDAO.getAll(Flange.class);
        return entities;
    }
}
