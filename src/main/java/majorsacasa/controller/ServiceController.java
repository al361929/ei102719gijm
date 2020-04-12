package majorsacasa.controller;

import majorsacasa.dao.ServiceDao;
import majorsacasa.model.Service;
import majorsacasa.model.SocialWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/service")
public class ServiceController {

    private ServiceDao serviceDao;

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @RequestMapping("/list")
    public String listService(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("services", serviceDao.getServices());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "service/list";
    }

    @RequestMapping(value = "/add")
    public String addService(Model model) {
        model.addAttribute("service", new Service());
        return "service/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("service") Service service, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "service/add";
        serviceDao.addService(service);
        return "redirect:list?nuevo=" + service.getIdService();
    }

    @RequestMapping(value = "/update/{idService}", method = RequestMethod.GET)
    public String editService(Model model, @PathVariable Integer idService) {
        model.addAttribute("service", serviceDao.getService(idService));
        return "service/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("service") Service service,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
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
}
