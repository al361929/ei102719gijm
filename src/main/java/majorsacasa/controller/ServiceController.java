package majorsacasa.controller;

import majorsacasa.dao.ServiceDao;
import majorsacasa.model.Service;
import majorsacasa.model.SocialWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/service")
public class ServiceController {

    private ServiceDao serviceDao;

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @RequestMapping("/list")
    public String listService(Model model) {
        model.addAttribute("services", serviceDao.getServices());
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
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idService}", method = RequestMethod.GET)
    public String editSocialWorker(Model model, @PathVariable String idService) {
        model.addAttribute("service", serviceDao.getService(idService));
        return "service/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("service") Service service,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "service/update";
        serviceDao.updateService(service);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idService}")
    public String processDelete(@PathVariable String idService) {
        serviceDao.deleteService(idService);
        return "redirect:../list";
    }
}
