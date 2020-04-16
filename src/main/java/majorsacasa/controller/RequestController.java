package majorsacasa.controller;

import majorsacasa.dao.RequestDao;
import majorsacasa.dao.ServiceDao;
import majorsacasa.model.Request;
import majorsacasa.model.Service;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/request")
public class RequestController extends Controlador{

    private RequestDao requestDao;
    private ServiceDao serviceDao;
    private List estados = Arrays.asList("Pendiente", "Aceptada", "Rechazada", "Cancelada");

    @Autowired
    public void setRequestDao(RequestDao requestDao, ServiceDao serviceDao) {
        this.requestDao = requestDao;
        this.serviceDao = serviceDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("requests", requestDao.getRequests());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "request/list";
    }

    @RequestMapping(value = "/add")
    public String addRequest(Model model) {
        List<Service> servicios = serviceDao.getServices();
        System.out.println(servicios.toString());
        model.addAttribute("servicios", servicios);
        model.addAttribute("request", new Request());
        return "request/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "request/add";
        requestDao.addRequest(request);
        return "redirect:list?nuevo=" + request.getIdRequest();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable int id) {
        model.addAttribute("estados", estados);
        model.addAttribute("request", requestDao.getRequest(id));
        return "request/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "request/update";
        requestDao.updateRequest(request);
        return "redirect:list?nuevo=" + request.getIdRequest();
    }

    @RequestMapping(value = "/delete/{id}")
    public String processDelete(@PathVariable int id) {
        requestDao.deleteRequest(id);
        return "redirect:../list";
    }
    @RequestMapping("/listElderly")
    public String listRequestsElderly(HttpSession session,Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        model.addAttribute("requests", requestDao.getRequestsElderly(user.getDni()));
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session,model,"ElderlyPeople","request/listRequestElderly");

    }
    @RequestMapping(value = "/addRequestElderly")
    public String addRequestElderly(Model model) {
        model.addAttribute("request", new Request());
        return "request/addRequestElderly";
    }

    @RequestMapping(value = "/addRequestElderly", method = RequestMethod.POST)
    public String processAddSubmitRequestElderly(HttpSession session,@ModelAttribute("request") Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "request/addRequestElderly";
        UserDetails user = (UserDetails) session.getAttribute("user");
        request.setDni(user.getDni());
        requestDao.addRequest(request);
        return "redirect:listElderly?nuevo=" + request.getIdRequest();
    }
    @RequestMapping(value = "/cancelarRequest/{id}")
    public String processUpdateEstadp(@PathVariable int id) {
        requestDao.updateEstado(id,"Cancelada");
        return "redirect:../listElderly";
    }

}
