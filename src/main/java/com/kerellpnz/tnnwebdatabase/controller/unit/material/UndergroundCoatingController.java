package com.kerellpnz.tnnwebdatabase.controller.unit.material;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.material.UndergroundCoatingDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.UndergroundCoatingJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.AnticorrosiveCoatingTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.UndergroundCoating;
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
@RequestMapping("/entity/UndergroundCoatings")
public class UndergroundCoatingController extends BaseEntityController {

    private final UndergroundCoatingDAO entityDAO;
    private final BaseEntityDAO<SheetGateValve> sheetGateValveDAO;
    private final BaseEntityDAO<UndergroundCoatingJournal> baseJournalDAO;
    private final BaseEntityDAO<AnticorrosiveCoatingTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;

    private List<String> names;
    private List<String> factories;
    private List<AnticorrosiveCoatingTCP> entityTCPs;

    private List<UndergroundCoating> entities;
    private String parameter;

    public UndergroundCoatingController(HttpSession session,
                               JournalNumberDAO journalNumberDAO,
                               UndergroundCoatingDAO entityDAO,
                               BaseEntityDAO<SheetGateValve> sheetGateValveDAO,
                               BaseEntityDAO<UndergroundCoatingJournal> baseJournalDAO,
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
        mv.addObject("title", "??????????????????");
        mv.addObject("userClickEntityView", true);
        if(parameter != null) {
            if ("duplicate".equals(parameter)) {
                mv.addObject("message", "????????????????: ???????????????? ?? ???????????? ???????????????????????????????? ?????? ????????????????????!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("?????????? ???????????????? (??????????????????)");
        UndergroundCoating entity = new UndergroundCoating();
        entityTCPs = TCPDAO.getAll(AnticorrosiveCoatingTCP.class);
        List<UndergroundCoatingJournal> entityJournals = new ArrayList<>();
        for (AnticorrosiveCoatingTCP tcp : entityTCPs) {
            UndergroundCoatingJournal entityJournal = new UndergroundCoatingJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        return modelInitializeObject(mv, entity);
    }


    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("???????????????????????????? ???????????????? (??????????????????)");
        UndergroundCoating entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        for (UndergroundCoatingJournal entityJournal: entity.getEntityJournals()) {
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
        mv.addObject("coatingClass", "UndergroundCoatings");
        mv.addObject("userClickCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/material/coating-editView");
        mv.addObject("title", title);
        mv.addObject("coatingClass", "UndergroundCoatings");
        mv.addObject("userClickEditCoatings", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, UndergroundCoating entity) {
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        factories = entityDAO.getDistinctFactory(UndergroundCoating.class);
        names = entityDAO.getDistinctName(UndergroundCoating.class);
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
    public String addOperation(@ModelAttribute("entity") UndergroundCoating entity, Model model) {
        if (entity.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(AnticorrosiveCoatingTCP.class, entity.getTempTCPId());
            if (entity.getEntityJournals() != null) {
                UndergroundCoatingJournal entityJournal = new UndergroundCoatingJournal(tempTCP);
                entity.getEntityJournals().add(entityJournal);
            }
            else {
                List<UndergroundCoatingJournal> entityJournals = new ArrayList<>();
                UndergroundCoatingJournal entityJournal = new UndergroundCoatingJournal(tempTCP);
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
    public String deleteOperation (@ModelAttribute("entity") UndergroundCoating entity, Model model, @RequestParam String deleteOperation) {
        int i = Integer.parseInt(deleteOperation);
        UndergroundCoatingJournal entityJournal = entity.getEntityJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(UndergroundCoatingJournal.class, entityJournal.getId());
        }
        entity.getEntityJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/material/coating-editView";
    }


    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") UndergroundCoating entity, BindingResult results, Model model) {
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
        for(UndergroundCoating tempEntity : entities) {
            if (entity.equals(tempEntity)) {
                if (entity.getId() != tempEntity.getId())
                    return sendMessage("????????????: ???????????????? ?? ?????????? ???????????????????????????????? ?????? ????????????????????!", model);
            }
        }

        if (entity.getEntityJournals() != null) {
            for (UndergroundCoatingJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("????????????:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("???? ??????????????????????????"))
                    flag = false;
                if (journal.getDate() != null) {
                    isEmpty = false;
                    entity.setInputControlDate(journal.getDate());
                }
            }
        }
        if (isEmpty) {
            return sendMessage("????????????: ???? ?????????? ???????? ???? ?????????? ???????????????? ????????????????!", model);
        }
        if(entity.getSheetGateValves() != null) {
            List<SheetGateValve> tempValvesList = new ArrayList<>();
            for(SheetGateValve valve : entity.getSheetGateValves()) {
                tempValvesList.add(sheetGateValveDAO.get(SheetGateValve.class, valve.getId()));
            }
            entity.setSheetGateValves(tempValvesList);
        }
        if (flag)
            entity.setStatus("C????????.");
        else
            entity.setStatus("???? ??????????.");
        entityDAO.saveOrUpdate(entity);
        return "redirect:/entity/UndergroundCoatings/showAll";
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/material/coating-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditCoatings", true);
        model.addAttribute("coatingClass", "UndergroundCoatings");
        model.addAttribute("title", "???????????????????????????? ???????????????? (??????????????????)");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
        model.addAttribute("factories", factories);
        model.addAttribute("names", names);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        UndergroundCoating entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/UndergroundCoatings/showAll?parameter=" + parameter;
    }

    private void newEntityInitialize(UndergroundCoating entity, String newNumber) {
        UndergroundCoating entityNew = new UndergroundCoating(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (UndergroundCoatingJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new UndergroundCoatingJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new UndergroundCoatingJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        UndergroundCoating entity = entityDAO.get(UndergroundCoating.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(UndergroundCoating.class, id);
        return "redirect:/entity/UndergroundCoatings/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<UndergroundCoating> getAll() {
        entities = entityDAO.getAll(UndergroundCoating.class);
        return entities;
    }
}
