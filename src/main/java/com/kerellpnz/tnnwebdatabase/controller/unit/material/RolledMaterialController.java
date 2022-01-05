package com.kerellpnz.tnnwebdatabase.controller.unit.material;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.material.RolledMaterialDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.RolledMaterialJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.material.MetalMaterialTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.RolledMaterial;
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
@RequestMapping("/entity/RolledMaterials")
public class RolledMaterialController extends BaseEntityController {

    private final RolledMaterialDAO entityDAO;
    private final BaseEntityDAO<RolledMaterialJournal> baseJournalDAO;
    private final BaseEntityDAO<MetalMaterialTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> materials;
    private List<String> firstSizes;
    private List<String> secondSizes;
    private List<String> thirdSizes;
    private List<MetalMaterialTCP> entityTCPs;

    @Autowired
    public RolledMaterialController(HttpSession session,
                                   JournalNumberDAO journalNumberDAO,
                                   RolledMaterialDAO entityDAO,
                                   BaseEntityDAO<RolledMaterialJournal> baseJournalDAO,
                                   BaseEntityDAO<MetalMaterialTCP> TCPDAO,
                                   InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll() {
        ModelAndView mv = modelInitializeIndex();
        mv.addObject("userClickEntityView", true);
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новый прокат");
        RolledMaterial entity = new RolledMaterial();
        entityTCPs = TCPDAO.getAll(MetalMaterialTCP.class);
        List<RolledMaterialJournal> entityJournals = new ArrayList<>();
        for (MetalMaterialTCP tcp : entityTCPs) {
            RolledMaterialJournal entityJournal = new RolledMaterialJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Характеристики проката");
        RolledMaterial entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (RolledMaterialJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(MetalMaterialTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Прокат");
        mv.addObject("materialClass", "RolledMaterials");
        mv.addObject("userClickMetalMaterials", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/material/metal-material-editView");
        mv.addObject("title", title);
        mv.addObject("materialClass", "RolledMaterials");
        mv.addObject("userClickEditMetalMaterials", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, RolledMaterial entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        materials = entityDAO.getDistinctMaterial(RolledMaterial.class);
        firstSizes = entityDAO.getDistinctFirstSize(RolledMaterial.class);
        secondSizes = entityDAO.getDistinctSecondSize(RolledMaterial.class);
        thirdSizes = entityDAO.getDistinctThirdSize(RolledMaterial.class);
        mv.addObject("materials", materials);
        mv.addObject("firstSizes", firstSizes);
        mv.addObject("secondSizes", secondSizes);
        mv.addObject("thirdSizes", thirdSizes);
        mv.addObject("entity", entity);
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") RolledMaterial entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(MetalMaterialTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                entity.getEntityJournals().add(new RolledMaterialJournal(tempTCP));
            }
            else {
                List<RolledMaterialJournal> entityJournals = new ArrayList<>();
                entityJournals.add(new RolledMaterialJournal(tempTCP));
                entity.setEntityJournals(entityJournals);
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/material/metal-material-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") RolledMaterial entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        RolledMaterialJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(RolledMaterialJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/material/metal-material-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") RolledMaterial entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/material/metal-material-editView";
        }
        boolean flag = true;
        boolean isEmpty = true;
        UserModel userModel = this.getUserModel();
        if (entity.getEntityJournals() != null) {
            for (RolledMaterialJournal journal: entity.getEntityJournals()) {
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
        return "redirect:/entity/RolledMaterials/showAll";
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        RolledMaterial entity = entityDAO.get(entityId.getId());
        newEntityInitialize(entity, number);
        return "redirect:/entity/RolledMaterials/showAll";
    }

    private void newEntityInitialize(RolledMaterial entity, String newNumber) {
        RolledMaterial entityNew = new RolledMaterial(entity, newNumber);
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (RolledMaterialJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new RolledMaterialJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new RolledMaterialJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/material/metal-material-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditMetalMaterials", true);
        model.addAttribute("materialClass", "RolledMaterials");
        model.addAttribute("title", "Характеристики проката");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("materials", materials);
        model.addAttribute("firstSizes", firstSizes);
        model.addAttribute("secondSizes", secondSizes);
        model.addAttribute("thirdSizes", thirdSizes);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        RolledMaterial entity = entityDAO.get(RolledMaterial.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(RolledMaterial.class, id);
        return "redirect:/entity/RolledMaterials/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<RolledMaterial> getAll() {
        return entityDAO.getAll(RolledMaterial.class);
    }
}
