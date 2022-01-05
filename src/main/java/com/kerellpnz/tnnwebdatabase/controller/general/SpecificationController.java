package com.kerellpnz.tnnwebdatabase.controller.general;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.BaseEntityDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.PIDDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Customer;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.general.Specification;
import com.kerellpnz.tnnwebdatabase.entity.journal.general.PIDJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.general.PIDTCP;
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
@RequestMapping("/Specifications")
public class SpecificationController extends BaseEntityController {

    private final BaseEntityDAO<Specification> specificationDAO;
    private final PIDDAO entityDAO;
    private final BaseEntityDAO<Customer> customerDAO;
    private final BaseEntityDAO<PIDJournal> baseJournalDAO;
    private final BaseEntityDAO<PIDTCP> TCPDAO;
    private final InspectorDAO inspectorDAO;
    
    private List<PIDTCP> entityTCPs;
    private List<String> designations;

    private List<Specification> specifications;

    public SpecificationController(HttpSession session,
                                   JournalNumberDAO journalNumberDAO,
                                   BaseEntityDAO<Specification> specificationDAO,
                                   PIDDAO entityDAO,
                                   BaseEntityDAO<Customer> customerDAO,
                                   BaseEntityDAO<PIDJournal> baseJournalDAO,
                                   BaseEntityDAO<PIDTCP> TCPDAO,
                                   InspectorDAO inspectorDAO) {
        super(session, journalNumberDAO);
        this.specificationDAO = specificationDAO;
        this.entityDAO = entityDAO;
        this.customerDAO = customerDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.inspectorDAO = inspectorDAO;
    }

    @GetMapping("/showAll")
    public ModelAndView showAll(@RequestParam(name="parameter", required = false) String parameter) {
        ModelAndView mv = modelInitializeIndex();
        if(parameter != null) {
            switch (parameter) {
                case "success" -> mv.addObject("message", "Спецификация успешно добавлена!");
                case "duplicate" -> mv.addObject("message", "Спецификация с таким номером уже существует! Воспользуйтесь поиском!");
                case "error" -> mv.addObject("message", "PID не найден!");
            }
        }
        return mv;
    }

    @ModelAttribute("customers")
    public List<Customer> getCustomers() {
        return customerDAO.getAll(Customer.class);
    }

    @ModelAttribute("specification")
    public Specification getSpecificationModel() {
        return new Specification();
    }

    @ModelAttribute("pids")
    public List<String> getPIDNumbers() {
        return entityDAO.getPIDNumbers();
    }

    @GetMapping("/findPID")
    public ModelAndView findPID(@RequestParam("PIDNumber") String number) throws EntityNotFoundException {
        Integer id = entityDAO.getIdByNumber(number);
        if (id == null)
            return this.showAll("error");
        return this.showFormForUpdateNormal(id);
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("specification") Specification specification) {
        String parameter = "success";
        for(Specification tempEntity : specifications) {
            if (specification.getNumber().equals(tempEntity.getNumber())) {
                if (specification.getId() != tempEntity.getId()) {
                    parameter = "duplicate";
                    break;
                }
            }
        }
        if (parameter.equals("success"))
            specificationDAO.saveOrUpdate(specification);
        return "redirect:/Specifications/showAll?parameter=" + parameter;
    }

    @GetMapping("/showFormForAdd/{id}")
    public ModelAndView showFormForAdd(@PathVariable int id) throws EntityNotFoundException {
        ModelAndView mv = modelInitializeEdit("Новый PID");
        Specification specification = specificationDAO.get(Specification.class, id);
        if (specification == null)
            throw new EntityNotFoundException();
        PID pid = new PID(specification);
        entityTCPs = TCPDAO.getAll(PIDTCP.class);
        List<PIDJournal> pidJournals = new ArrayList<>();
        for (PIDTCP tcp : entityTCPs) {
            PIDJournal pidJournal = new PIDJournal(tcp);
            pidJournals.add(pidJournal);
        }
        pid.setEntityJournals(pidJournals);
        return modelInitializeObject(mv, pid);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        PID entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}/{name}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId, @PathVariable String name) throws EntityNotFoundException {
        PID entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        entity.setReqName(name);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(PID entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики PID");
        for (PIDJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(PIDTCP.class);
        return modelInitializeObject(mv, entity);
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("title", "Спецификации");
        mv.addObject("userClickSpecifications", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/general/pid-editView");
        mv.addObject("title", title);
        mv.addObject("userClickEditPID", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, PID pid) {
        designations = entityDAO.getDistinctDesignations();
        mv.addObject("designations", designations);
        mv.addObject("entityTCPs", entityTCPs);
        mv.addObject("editMode", true);
        mv.addObject("entity", pid);
        return mv;
    }

    @PostMapping(value = "/action", params = "addOperation")
    public String addOperation(@ModelAttribute("entity") PID pid, Model model) {
        if (pid.getTempTCPId() != null) {
            BaseTCP tempTCP = TCPDAO.get(PIDTCP.class, pid.getTempTCPId());
            if (pid.getEntityJournals() != null) {
                pid.getEntityJournals().add(new PIDJournal(tempTCP));
            }
            else {
                List<PIDJournal> pidJournals = new ArrayList<>();
                pidJournals.add(new PIDJournal(tempTCP));
                pid.setEntityJournals(pidJournals);
            }
        }
        modelInitialize(model);
        model.addAttribute("pid", pid);
        return "./unit/general/pid-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperation")
    public String deleteOperation (@ModelAttribute("entity") PID pid, Model model, @RequestParam String deleteOperation) {
        modelInitialize(model);
        int i = Integer.parseInt(deleteOperation);
        PIDJournal pidJournal = pid.getEntityJournals().get(i);
        if (pidJournal.getId() != 0) {
            baseJournalDAO.delete(PIDJournal.class, pidJournal.getId());
        }
        pid.getEntityJournals().remove(i);
        model.addAttribute("pid", pid);
        return "./unit/general/pid-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") PID pid, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/general/pid-editView";
        }
        boolean isEmpty = true;
        UserModel userModel = this.getUserModel();

        List<PID> pids = entityDAO.getAll(PID.class);
        for(PID tempEntity : pids) {
            if (pid.getNumber().equals(tempEntity.getNumber())) {
                if (pid.getId() != tempEntity.getId())
                    return sendMessage("ОШИБКА: PID с таким характеристиками уже существует!", model);
            }
        }

        if (pid.getEntityJournals() != null) {
            for (PIDJournal journal: pid.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(pid);
                if (journal.getDate() != null)
                    isEmpty = false;
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        pid.setSpecification(specificationDAO.get(Specification.class, pid.getSpecification().getId()));
        entityDAO.saveOrUpdate(pid);
        if (pid.getReqId() != null) {
            if (pid.getReqName().equals("valve"))
                return "redirect:/entity/SheetGateValves/showFormForUpdate/" + pid.getReqId();
            else
                return "redirect:/entity/Nozzles/showFormForUpdate/" + pid.getReqId();
        }
        else
            return "redirect:/Specifications/showAll";
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/general/pid-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("designations", designations);
        model.addAttribute("userClickEditPID", true);
        model.addAttribute("title", "Редактирование PID");
        model.addAttribute("editMode", true);
        model.addAttribute("entityTCPs", entityTCPs);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Specification specification = specificationDAO.get(Specification.class, id);
        if (specification == null)
            throw new EntityNotFoundException();
        specificationDAO.delete(Specification.class, id);
        return "redirect:/Specifications/showAll";
    }

    @GetMapping("/delete/pid/{id}")
    public String deletePID(@PathVariable int id) throws EntityNotFoundException {
        PID pid = entityDAO.get(PID.class, id);
        if (pid == null)
            throw new EntityNotFoundException();
        entityDAO.delete(PID.class, id);
        return "redirect:/Specifications/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Specification> getAll() {
        specifications = specificationDAO.getAll(Specification.class);
        return specifications;
    }
}
