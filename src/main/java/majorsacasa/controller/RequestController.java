package majorsacasa.controller;

import majorsacasa.dao.RequestDao;
import majorsacasa.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/request")
public class RequestController {

    private RequestDao requestDao;

    @Autowired
    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model) {
        model.addAttribute("solicitudes", requestDao.getRequests());
        return "request/list";
    }

    @RequestMapping(value = "/add")
    public String addRequest(Model model) {
        model.addAttribute("request", new Request());
        return "request/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("socialWorker") Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "request/add";
        requestDao.addRequest(request);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable String dni) {
        model.addAttribute("request", requestDao.getRequest(dni));
        return "request/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "request/update";
        requestDao.updateRequest(request);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        requestDao.deleteRequest(dni);
        return "redirect:../list";
    }
}
