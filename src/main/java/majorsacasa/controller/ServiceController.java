package majorsacasa.controller;

import majorsacasa.dao.ServiceDao;
import majorsacasa.dao.TypeServiceDao;
import majorsacasa.model.Service;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/service")
public class ServiceController extends ManageAccessController {

    private ServiceDao serviceDao;
    private TypeServiceDao typeserviceDao;


    @Autowired
    public void setServiceDao(ServiceDao serviceDao, TypeServiceDao typeserviceDao) {
        this.serviceDao = serviceDao;
        this.typeserviceDao = typeserviceDao;
    }

    @RequestMapping("/list")
    public String listService(Model model, @RequestParam("nuevo") Optional<String> nuevo, HttpSession session) {
        model.addAttribute("services", serviceDao.getServices());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session, model, "Admin", "service/list");
    }

    @RequestMapping(value = "/add")
    public String addService(Model model) {
        model.addAttribute("service", new Service());
        model.addAttribute("typosServicios", typeserviceDao.getTypeServices());

        return "service/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("service") Service service, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("typosServicios", typeserviceDao.getTypeServices());

            return "service/add";
        }
        serviceDao.addService(service);
        int id = serviceDao.ultimoIdService();
        return "redirect:list?nuevo=" + id;
    }

    @RequestMapping(value = "/update/{idService}", method = RequestMethod.GET)
    public String editService(Model model, @PathVariable Integer idService, HttpSession session) {
        model.addAttribute("service", serviceDao.getService(idService));
        model.addAttribute("typosServicios", typeserviceDao.getTypeServices());
        return gestionarAcceso(session, model, "Admin", "service/update");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("service") Service service,
                                      BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("typosServicios", typeserviceDao.getTypeServices());

            return "service/update";
        }
        serviceDao.updateService(service);
        return "redirect:list?nuevo=" + service.getIdService();
    }

    @RequestMapping(value = "/delete/{idService}")
    public String processDelete(@PathVariable Integer idService) {
        serviceDao.deleteService(idService);
        return "redirect:../list";
    }

    @RequestMapping(value = "/elderlyList")
    public String getElderlyList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("elderlyList", serviceDao.getElderlyList(user.getDni()));
        return gestionarAcceso(session, model, "ElderlyPeople", "/service/elderlyList");
    }

    @RequestMapping(value = "/serviceList")
    public String getServiceCompanyList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("serviceCompanyList", serviceDao.getServiceList(user.getDni()));
        return gestionarAcceso(session, model, "Company", "service/serviceList");
    }
}
