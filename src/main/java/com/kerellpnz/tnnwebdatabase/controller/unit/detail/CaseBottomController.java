package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseUnitController;
import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.CaseBottomDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.CaseBottomJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.CaseBottomTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.SheetMaterial;
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
@RequestMapping("/entity/CaseBottoms")
public class CaseBottomController extends BaseUnitController {

    private final CaseBottomDAO entityDAO;
    private final BaseEntityDAO<CaseBottomJournal> baseJournalDAO;
    private final BaseEntityDAO<CaseBottomTCP> TCPDAO;
    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<CaseBottomTCP> entityTCPs;
    private List<MetalMaterial> metalMaterials;
    private List<String> dns;

    private List<CaseBottom> entities;
    private String parameter;

    public CaseBottomController(HttpSession session,
                                JournalNumberDAO journalNumberDAO,
                                CaseBottomDAO entityDAO,
                                BaseEntityDAO<CaseBottomJournal> baseJournalDAO,
                                BaseEntityDAO<CaseBottomTCP> TCPDAO,
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
                case "badMetal" -> mv.addObject("message", "????????????????: ?????????????????? ???????????? ?????????? ???????????? \"???? ??????????.\"," +
                        " ?????????????? ???????? ???? ???????????? ???????????????? ?? ????????????.");
                case "error" -> mv.addObject("message", "?????????? ?????????? ???? ???????????????? ????????????!");
                case "duplicate" -> mv.addObject("message", "????????????????: ?????????? ?? ???????????? ???????????????????????????????? ?????? ????????????????????!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("?????????? ??????????");
        CaseBottom entity = new CaseBottom();
        entityTCPs = TCPDAO.getAll(CaseBottomTCP.class);
        List<CaseBottomJournal> entityJournals = new ArrayList<>();
        for (CaseBottomTCP tcp : entityTCPs) {
            CaseBottomJournal entityJournal = new CaseBottomJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        CaseBottom entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}/{name}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId, @PathVariable String name) throws EntityNotFoundException {
        CaseBottom entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        entity.setReqName(name);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(CaseBottom entity) {
        ModelAndView mv = modelInitializeEdit("???????????????????????????? ??????????");
        for (CaseBottomJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(CaseBottomTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "??????????");
        mv.addObject("commonDetailClass", "CaseBottoms");
        mv.addObject("userClickCommonDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/common-details-editView");
        mv.addObject("title", title);
        mv.addObject("commonDetailClass", "CaseBottoms");
        mv.addObject("userClickEditCommonDetails", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, CaseBottom entity) {
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

    @PostMapping(value = "/action", params = "openMaterial")
    public String openMaterial(@ModelAttribute("entity") CaseBottom entity, Model model) {
        if (entity.getMetalMaterial() != null) {
            if (entity.getMetalMaterial() instanceof SheetMaterial)
                return "redirect:/entity/SheetMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
            else
                return "redirect:/entity/RolledMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
        }
        modelInitialize(model);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") CaseBottom entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(CaseBottomTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new CaseBottomJournal(tempTCP));
            }
            else {
                List<CaseBottomJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new CaseBottomJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") CaseBottom entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        CaseBottomJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(CaseBottomJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/common-details-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") CaseBottom entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/common-details-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        parameter = "success";
        UserModel userModel = this.getUserModel();

        if (entity.getMetalMaterial() != null) {
            if (!checkMaterial(entity, entity.getMetalMaterial())) {
                flag = false;
                parameter = "badMetal";
            }
        }
        else {
            if(entity.getMaterial().isBlank() || entity.getMelt().isBlank()) {
                return sendMessage("?????????????? ????????????/????????????????!", model);
            }
        }

        if (entities == null)
            entities = getAll();
        for(CaseBottom tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("????????????: ?????????? ?? ?????????? ???????????????????????????????? ?????? ????????????????????!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (CaseBottomJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("????????????:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("???? ??????????????????????????"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
            }
        }
        if (isEmpty) {
            return sendMessage("????????????: ???? ?????????? ???????? ???? ?????????? ???????????????? ????????????????!", model);
        }
        if (flag)
            entity.setStatus("C????????.");
        else
            entity.setStatus("???? ??????????.");
        entityDAO.saveOrUpdate(entity);
        if (entity.getReqId() != null) {
            if (entity.getReqName().equals("case"))
                return "redirect:/entity/SheetGateValveCases/showFormForUpdate/" + entity.getReqId();
            else
                return "redirect:/entity/SheetGateValveCovers/showFormForUpdate/" + entity.getReqId();
        }
        else
            return "redirect:/entity/CaseBottoms/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/common-details-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditCommonDetails", true);
        model.addAttribute("title", "???????????????????????????? ??????????");
        model.addAttribute("commonDetailClass", "CaseBottoms");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("drawings", drawings);
        model.addAttribute("metalMaterials", metalMaterials);
        model.addAttribute("dns", dns);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        CaseBottom entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/CaseBottoms/showAll?parameter=" + parameter;
    }

    @PostMapping("/multi-copy")
    public ModelAndView multiCopyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("quantity") Integer quantity){
        CaseBottom entity = entityDAO.getForCopy(entityId.getId());
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

    private void newEntityInitialize(CaseBottom entity, String newNumber) {
        CaseBottom entityNew = new CaseBottom(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (CaseBottomJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new CaseBottomJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new CaseBottomJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        CaseBottom entity = entityDAO.get(CaseBottom.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(CaseBottom.class, id);
        return "redirect:/entity/CaseBottoms/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<CaseBottom> getAll() {
        entities = entityDAO.getAll(CaseBottom.class);
        return entities;
    }
}
