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
    public String processAddSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "request/add";
        requestDao.addRequest(request);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable int id) {
        model.addAttribute("request", requestDao.getRequest(id));
        return "request/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "request/update";
        System.out.println(request.toString());
        requestDao.updateRequest(request);
        System.out.println(request.toString());
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String processDelete(@PathVariable int id) {
        requestDao.deleteRequest(id);
        return "redirect:../list";
    }
}
