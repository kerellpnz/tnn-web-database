package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.dao.*;
import com.kerellpnz.tnnwebdatabase.dao.detail.FrontalSaddleSealingDAO;
import com.kerellpnz.tnnwebdatabase.dao.detail.SaddleDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.dao.general.PIDDAO;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.ServiceClass;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.SaddleJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.detail.SaddleTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.FrontalSaddleSealing;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Saddle;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.SheetMaterial;
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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/entity/Saddles")
public class SaddleController extends BaseUnitControllerWithZk<Saddle> {

    private final SaddleDAO entityDAO;
    private final BaseEntityDAO<SheetGateValve> sheetGateValveDAO;
    private final BaseEntityDAO<SaddleJournal> baseJournalDAO;
    private final BaseTCPDAO<SaddleTCP> TCPDAO;
    private final PIDDAO pidDAO;
    private final BaseEntityDAO<MetalMaterial> metalMaterialDAO;
    private final FrontalSaddleSealingDAO frontalSaddleSealingDAO;
    private final InspectorDAO inspectorDAO;
    private final ServiceClass serviceClass;

    private List<String> drawings;
    private List<SaddleTCP> entityTCPs;
    private List<FrontalSaddleSealing> frontalSaddleSeals;
    private List<MetalMaterial> metalMaterials;
    private List<PID> pids;
    private List<String> dns;
    private List<String> pns;
    private List<SaddleTCP> inputControlTCP;
    private List<SaddleTCP> mechanicalTCP;

    private List<Saddle> entities;
    private String parameter;

    @Autowired
    public SaddleController(HttpSession session,
                                JournalNumberDAO journalNumberDAO,
                                SaddleDAO entityDAO,
                                BaseEntityDAO<SheetGateValve> sheetGateValveDAO,
                                BaseEntityDAO<SaddleJournal> baseJournalDAO,
                                BaseTCPDAO<SaddleTCP> TCPDAO,
                                PIDDAO pidDAO,
                                FrontalSaddleSealingDAO frontalSaddleSealingDAO,
                                BaseEntityDAO<MetalMaterial> metalMaterialDAO,
                                InspectorDAO inspectorDAO,
                                ServiceClass serviceClass) {
        super(session, journalNumberDAO);
        this.entityDAO = entityDAO;
        this.sheetGateValveDAO = sheetGateValveDAO;
        this.baseJournalDAO = baseJournalDAO;
        this.TCPDAO = TCPDAO;
        this.pidDAO = pidDAO;
        this.frontalSaddleSealingDAO = frontalSaddleSealingDAO;
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
                        " поэтому этот же статус применен к обойме.");
                case "error" -> mv.addObject("message", "Номер обоймы не является числом!");
                case "duplicate" -> mv.addObject("message", "ВНИМАНИЕ: Обойма с такими характеристиками уже существует!");
            }
        }
        return mv;
    }

    @GetMapping("/showFormForAdd")
    public ModelAndView showFormForAdd() {
        ModelAndView mv = modelInitializeEdit("Новая обойма");
        Saddle entity = new Saddle();
        entityTCPs = TCPDAO.getAll(SaddleTCP.class);
        List<SaddleJournal> entityJournals = new ArrayList<>();
        for (SaddleTCP tcp : entityTCPs) {
            SaddleJournal entityJournal = new SaddleJournal(tcp);
            entityJournals.add(entityJournal);
        }
        entity.setEntityJournals(entityJournals);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    @RequestMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdateNormal(@PathVariable int id) throws EntityNotFoundException {
        Saddle entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        return showFormForUpdate(entity);
    }

    @RequestMapping("/showFormForUpdate/{id}/{reqId}")
    public ModelAndView showFormForUpdateWithReqId(@PathVariable int id, @PathVariable int reqId) throws EntityNotFoundException {
        Saddle entity = entityDAO.get(id);
        if (entity == null)
            throw new EntityNotFoundException();
        entity.setReqId(reqId);
        return showFormForUpdate(entity);
    }

    private ModelAndView showFormForUpdate(Saddle entity) {
        ModelAndView mv = modelInitializeEdit("Характеристики обоймы");
        for (SaddleJournal entityJournal: entity.getEntityJournals()) {
            if (entityJournal.getInspectorId() != null) {
                Inspector inspector = inspectorDAO.get(Inspector.class, entityJournal.getInspectorId());
                entityJournal.setInspectorName(inspector.getName() + "\n" + inspector.getApointment());
            }
        }
        entityTCPs = TCPDAO.getAll(SaddleTCP.class);
        TCPInitialize(entityTCPs);
        return modelInitializeObject(mv, entity);
    }

    private void TCPInitialize(List<SaddleTCP> entityTCPs) {
        inputControlTCP = new ArrayList<>();
        mechanicalTCP = new ArrayList<>();
        if (entityTCPs != null) {
            for(SaddleTCP tcp : entityTCPs) {
                switch (tcp.getOperationType()) {
                    case "Входной контроль" -> inputControlTCP.add(tcp);
                    case "Обработка" -> mechanicalTCP.add(tcp);
                }
            }
        }
    }

    private ModelAndView modelInitializeIndex() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("assemblyDetail", "Saddles");
        mv.addObject("title", "Обойма");
        mv.addObject("userClickAssemblyDetail", true);
        return mv;
    }

    private ModelAndView modelInitializeEdit(String title) {
        ModelAndView mv = new ModelAndView("./unit/detail/assembly-detail-editView");
        mv.addObject("assemblyDetail", "Saddles");
        mv.addObject("title", title);
        mv.addObject("userClickEditAssemblyDetail", true);
        return mv;
    }

    private ModelAndView modelInitializeObject(ModelAndView mv, Saddle entity) {
        metalMaterials = metalMaterialDAO.getAll(MetalMaterial.class);
        frontalSaddleSeals = frontalSaddleSealingDAO.getAll(FrontalSaddleSealing.class);
        drawings = entityDAO.getDistinctDrawing();
        pids = pidDAO.getAll(PID.class);
        dns = serviceClass.getDns();
        pns = serviceClass.getPns();
        mv.addObject("metalMaterials", metalMaterials);
        mv.addObject("frontalSaddleSeals", frontalSaddleSeals);
        mv.addObject("drawings", drawings);
        mv.addObject("pids", pids);
        mv.addObject("dns", dns);
        mv.addObject("pns", pns);
        mv.addObject("inputControlTCP", inputControlTCP);
        mv.addObject("mechanicalTCP", mechanicalTCP);
        mv.addObject("editMode", true);
        mv.addObject("entity", entity);

        if (entity.getEntityJournals() != null) {
            for(SaddleJournal journal : entity.getEntityJournals()) {
                String operationType = TCPDAO.getOperationTypeById(SaddleTCP.class, journal.getPointId());
                switch (operationType) {
                    case "Входной контроль" -> entity.getInputControlJournals().add(journal);
                    case "Обработка" -> entity.getMechanicalJournals().add(journal);
                }
            }
            if (entity.getInputControlJournals() != null) entity.getInputControlJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
            if (entity.getMechanicalJournals() != null) entity.getMechanicalJournals()
                    .sort(Comparator.comparingInt(BaseJournal::getPointId));
        }

        return mv;
    }

    @PostMapping(value = "/action", params = "openPID")
    public String openPID(@ModelAttribute("entity") Saddle entity, Model model) {
        if (entity.getPid() != null) {
            if (entity.getId() != 0)
                return "redirect:/Specifications/showFormForUpdate/" + entity.getPid().getId()+ "/" + entity.getId() + "/saddle";
            else
                return "redirect:/Specifications/showFormForUpdate/" + entity.getPid().getId();
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "openMaterial")
    public String openMaterial(@ModelAttribute("entity") Saddle entity, Model model) {
        if (entity.getMetalMaterial() != null) {
            if (entity.getMetalMaterial() instanceof SheetMaterial)
                return "redirect:/entity/SheetMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
            else
                return "redirect:/entity/RolledMaterials/showFormForUpdate/" + entity.getMetalMaterial().getId();
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @RequestMapping("/openSeal")
    public String openSeal(@RequestParam("sealId") int sealId) {
        return "redirect:/entity/FrontalSaddleSealings/showFormForUpdate/" + sealId;
    }

    @PostMapping(value = "/action", params = "addSeal")
    public String addSeal(@ModelAttribute("entity") Saddle entity, Model model) {
        if (entity.getTempSealId() != null) {
            FrontalSaddleSealing sealing = frontalSaddleSealingDAO.getForSaddle(entity.getTempSealId());
            if (sealing.getAmount() - sealing.getSaddles().size() > 0) {
                if (entity.getFrontalSaddleSeals() != null) {
                    if (entity.getFrontalSaddleSeals().size() < 4) {
                        for(FrontalSaddleSealing tempSealing: entity.getFrontalSaddleSeals()) {
                            if (tempSealing.getId() == sealing.getId())
                                return sendMessage("Выбранное уплотнение уже присутствует!", model);
                        }
                        entity.getFrontalSaddleSeals().add(sealing);
                    }
                    else return sendMessage("Невозможно привязать больше 4-х уплотнений!", model);
                }
                else {
                    List<FrontalSaddleSealing> frontalSaddleSeals = new ArrayList<>();
                    frontalSaddleSeals.add(sealing);
                    entity.setFrontalSaddleSeals(frontalSaddleSeals);
                }
                model.addAttribute("entity", entity);
            }
            else return sendMessage("Уплотнения данной партии закончились! Применение можно посмотреть внутри самого уплотнения.", model);
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "deleteSeal")
    public String deleteSeal (@ModelAttribute("entity") Saddle entity, Model model, @RequestParam int deleteSeal) {
        entity.getFrontalSaddleSeals().remove(deleteSeal);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/assembly-detail-editView";
    }

    private void addOperation(Integer tempTCPid, List<SaddleJournal> targetJournals) {
        BaseTCP tempTCP = TCPDAO.get(SaddleTCP.class, tempTCPid);
        targetJournals.add(new SaddleJournal(tempTCP));
        targetJournals.sort(Comparator.comparingInt(BaseJournal::getPointId));
    }

    @PostMapping(value = "/action", params = "addOperationInput")
    public String addOperationInput(@ModelAttribute("entity") Saddle entity, Model model) {
        if (entity.getInputTCPId() != null) {
            if (entity.getInputControlJournals() != null) {
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            else {
                List<SaddleJournal> entityJournals = new ArrayList<>();
                entity.setInputControlJournals(entityJournals);
                addOperation(entity.getInputTCPId(), entity.getInputControlJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationInput")
    public String deleteOperationInput (@ModelAttribute("entity") Saddle entity, Model model, @RequestParam String deleteOperationInput) {
        int i = Integer.parseInt(deleteOperationInput);
        SaddleJournal entityJournal = entity.getInputControlJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SaddleJournal.class, entityJournal.getId());
        }
        entity.getInputControlJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "addOperationMech")
    public String addOperationMech(@ModelAttribute("entity") Saddle entity, Model model) {
        if (entity.getMechTCPId() != null) {
            if (entity.getMechanicalJournals() != null) {
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            else {
                List<SaddleJournal> entityJournals = new ArrayList<>();
                entity.setMechanicalJournals(entityJournals);
                addOperation(entity.getMechTCPId(), entity.getMechanicalJournals());
            }
            model.addAttribute("entity", entity);
        }
        modelInitialize(model);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "deleteOperationMech")
    public String deleteOperationMech (@ModelAttribute("entity") Saddle entity, Model model, @RequestParam String deleteOperationMech) {
        int i = Integer.parseInt(deleteOperationMech);
        SaddleJournal entityJournal = entity.getMechanicalJournals().get(i);
        if (entityJournal.getId() != 0) {
            baseJournalDAO.delete(SaddleJournal.class, entityJournal.getId());
        }
        entity.getMechanicalJournals().remove(i);
        modelInitialize(model);
        model.addAttribute("entity", entity);
        return "./unit/detail/assembly-detail-editView";
    }

    @PostMapping(value = "/action", params = "save")
    public String save(@Valid @ModelAttribute("entity") Saddle entity, BindingResult results, Model model) {
        if(results.hasErrors()) {
            modelInitialize(model);
            return "./unit/detail/assembly-detail-editView";
        }

        boolean flag = true;
        boolean isEmpty = true;
        boolean assemblyFlag1 = false;
        boolean assemblyFlag2 = false;
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
                return sendMessage("Введите плавку/материал!", model);
            }
        }

        if (entities == null)
            entities = getAll();
        String resultDup = checkDuplicatesWithZK(entity, entities);
        if (resultDup.startsWith("ОШИБКА"))
            return sendMessage(resultDup, model);

        entity.setEntityJournals(
                Stream.of(entity.getInputControlJournals(),
                                entity.getMechanicalJournals())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));

        if (entity.getEntityJournals() != null) {
            for (SaddleJournal journal: entity.getEntityJournals()) {
                String result = checkJournal(journal, userModel);
                if (result.startsWith("ОШИБКА:")) {
                    return sendMessage(result, model);
                }
                journal.setDetailId(entity);
                if (result.equals("Не соответствует"))
                    flag = false;
                if (journal.getDate() != null)
                    isEmpty = false;
                if (journal.getPointId() == 80 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag1 = true;
                }
                if (journal.getPointId() == 155 && journal.getDate() != null && journal.getStatus().equals("Соответствует")) {
                    assemblyFlag2 = true;
                }
                if (journal.getPointId() == 80 && journal.getDate() != null) {
                    if (entity.getPn().isBlank()) {
                        return sendMessage("Не выбрано давление!", model);
                    }
                }
            }
        }
        if (isEmpty) {
            return sendMessage("ОШИБКА: Не может быть ни одной принятой операции!", model);
        }
        if(entity.getFrontalSaddleSeals() != null) {
            List<FrontalSaddleSealing> tempSealsList = new ArrayList<>();
            for(FrontalSaddleSealing frontalSaddleSealing : entity.getFrontalSaddleSeals()) {
                FrontalSaddleSealing sealing = frontalSaddleSealingDAO.get(FrontalSaddleSealing.class, frontalSaddleSealing.getId());
                tempSealsList.add(sealing);
            }
            entity.setFrontalSaddleSeals(tempSealsList);
        }
        if(entity.getSheetGateValve() != null) {
            SheetGateValve sheetGateValve = sheetGateValveDAO.get(SheetGateValve.class,
                    entity.getSheetGateValve().getId());
            entity.setSheetGateValve(sheetGateValve);
        }
        if (flag) {
            entity.setStatus("Cоотв.");
            if (assemblyFlag1 && assemblyFlag2)
                entity.setStatus("Готово к сборке");
        }
        else
            entity.setStatus("НЕ СООТВ.");
        entityDAO.saveOrUpdate(entity);
        if (entity.getReqId() != null)
            return "redirect:/entity/SheetGateValves/showFormForUpdate/" + entity.getReqId();
        else
            return "redirect:/entity/Saddles/showAll?parameter=" + parameter;
    }

    private String sendMessage(String message, Model model) {
        modelInitialize(model);
        model.addAttribute("message", message);
        return "./unit/detail/assembly-detail-editView";
    }

    private void modelInitialize(Model model) {
        model.addAttribute("userClickEditAssemblyDetail", true);
        model.addAttribute("title", "Характеристики обоймы");
        model.addAttribute("editMode", true);
        model.addAttribute("assemblyDetail", "Saddles");
        model.addAttribute("metalMaterials", metalMaterials);
        model.addAttribute("frontalSaddleSeals", frontalSaddleSeals);
        model.addAttribute("drawings", drawings);
        model.addAttribute("pids", pids);
        model.addAttribute("dns", dns);
        model.addAttribute("pns", pns);
        model.addAttribute("inputControlTCP", inputControlTCP);
        model.addAttribute("mechanicalTCP", mechanicalTCP);
    }

    @PostMapping("/single-copy")
    public String copyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("number") String number){
        Saddle entity = entityDAO.getForCopy(entityId.getId());
        parameter = "success";
        newEntityInitialize(entity, number);
        return "redirect:/entity/Saddles/showAll?parameter=" + parameter;
    }

    @PostMapping("/multi-copy")
    public ModelAndView multiCopyRow(@ModelAttribute("entityId") BaseEntity entityId, @RequestParam("quantity") String quantity){
        Saddle entity = entityDAO.getForCopy(entityId.getId());
        if (!entity.getNumber().matches("\\d+")) {
            return showAll("error");
        }
        Integer number = Integer.parseInt(entity.getNumber());
        parameter = "success";
        for (int i = 0; i < Integer.parseInt(quantity); i++) {
            String newNumber = (++number).toString();
            newEntityInitialize(entity, newNumber);
        }
        return showAll(parameter);
    }

    private void newEntityInitialize(Saddle entity, String newNumber) {
        Saddle entityNew = new Saddle(entity, newNumber);
        if (entities.contains(entityNew)) {
            parameter = "duplicate";
            return;
        }
        entityNew.setEntityJournals(new ArrayList<>());
        UserModel userModel = getUserModel();
        if (entity.getEntityJournals() != null) {
            for (SaddleJournal journal: entity.getEntityJournals()) {
                if (journal.getDate() != null)
                    entityNew.getEntityJournals().add(new SaddleJournal(journal, userModel.getId(), userModel.getJournalNumber(), entityNew));
                else
                    entityNew.getEntityJournals().add(new SaddleJournal(journal, entityNew));
            }
        }
        entityDAO.saveOrUpdate(entityNew);
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws EntityNotFoundException {
        Saddle entity = entityDAO.get(Saddle.class, id);
        if (entity == null)
            throw new EntityNotFoundException();
        entityDAO.delete(Saddle.class, id);
        return "redirect:/entity/Saddles/showAll";
    }

    @GetMapping("json/getAll")
    @ResponseBody
    public List<Saddle> getAll() {
        entities = entityDAO.getAll(Saddle.class);
        return entities;
    }
}
