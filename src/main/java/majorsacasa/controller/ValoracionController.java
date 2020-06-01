package majorsacasa.controller;

import majorsacasa.dao.ValoracionDao;
import majorsacasa.dao.ValoracionServiceDao;
import majorsacasa.model.UserDetails;
import majorsacasa.model.Valoracion;
import majorsacasa.model.ValoracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/valoraciones")
public class ValoracionController extends ManageAccessController {

    private ValoracionDao valoracionDao;
    private ValoracionServiceDao valoracionServiceDao;

    @Autowired
    public void setValoracionDao(ValoracionDao valoracionDao, ValoracionServiceDao valoracionServiceDao) {
        this.valoracionDao = valoracionDao;
        this.valoracionServiceDao = valoracionServiceDao;
    }

    @RequestMapping("/list")
    public String listValoraciones(Model model) {
        model.addAttribute("valoraciones", valoracionDao.getValoraciones());
        return "valoraciones/list";
    }

    @RequestMapping("/serviceValoration")
    public String listValoracionesServicios(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("listMisValoraciones", valoracionServiceDao.getMisValoraciones(user.getDni()));
        model.addAttribute("service", valoracionServiceDao.getServices());
        String newValoration = nuevo.orElse("None");
        model.addAttribute("nuevo", newValoration);
        return gestionarAcceso(session, model, "ElderlyPeople", "valoraciones/serviceValoration");
    }

    @RequestMapping(value = "/addValorationService/{id}")
    public String addValoracionService(@PathVariable int id, Model model) {
        ValoracionService v = new ValoracionService();
        v.setIdService(id);
        model.addAttribute("valoracionService", v);

        return "valoraciones/addValorationService";
    }

    @RequestMapping(value = "/addValorationService", method = RequestMethod.POST)
    public String processAddSubmitValoracionService(HttpSession session, Model model, @ModelAttribute("valoracionService") ValoracionService valoracion, BindingResult bindingResult) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        valoracion.setDni(user.getDni());
        Boolean checkValoracion = valoracionServiceDao.checkValoracion(user.getDni(), valoracion.getIdService());

        if (!checkValoracion) {
            bindingResult.rejectValue("idService", "idService", "Ya ha valorado  este servicio");
            return "valoraciones/addValorationService";
        }

        valoracionServiceDao.addValoracion(valoracion);
        int id = valoracion.getIdService();
        return gestionarAcceso(session, model, "ElderlyPeople", "redirect:serviceValoration?nuevo=" + id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("valoracion") Valoracion valoracion, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "valoracion/add";
        valoracionDao.addValoracion(valoracion);
        return "redirect:list?nuevo=" + valoracion.getDni();
    }

    @RequestMapping(value = "/updateService/{idService}", method = RequestMethod.GET)
    public String editValoracionService(HttpSession session, Model model, @PathVariable int idService) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("valoracionService", valoracionServiceDao.getValoracion(idService, user.getDni()));
        return "valoraciones/updateService";
    }

    @RequestMapping(value = "/updateService", method = RequestMethod.POST)
    public String processUpdateSubmitService(HttpSession session, @ModelAttribute("valoracionService") ValoracionService valoracion,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "valoraciones/updateService";
        UserDetails user = (UserDetails) session.getAttribute("user");
        valoracion.setDni(user.getDni());
        valoracionServiceDao.updateValoracion(valoracion);
        int id = valoracion.getIdService();
        return "redirect:serviceValoration?nuevo=" + id;
    }

    @RequestMapping(value = "/update/{dniVolunteer}", method = RequestMethod.GET)
    public String editValoracion(HttpSession session, Model model, @PathVariable String dniVolunteer) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("valoracion", valoracionDao.getValoracion(dniVolunteer, user.getDni()));
        return "valoraciones/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session, @ModelAttribute("valoracion") Valoracion valoracion,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "valoraciones/update";
        UserDetails user = (UserDetails) session.getAttribute("user");
        valoracion.setDni(user.getDni());
        valoracionDao.updateValoracion(valoracion);
        return "redirect:elderlyList?nuevo=" + valoracion.getDniVolunteer();
    }

    @RequestMapping(value = "/delete/{dniVolunteer}")
    public String processDelete(HttpSession session, @PathVariable String dniVolunteer) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        System.out.println("V: " + dniVolunteer + " E: " + user.getDni());
        valoracionDao.deleteValoracion(dniVolunteer, user.getDni());
        return "redirect:../elderlyList";
    }

    @RequestMapping(value = "/deleteService/{idService}")
    public String processDelete(HttpSession session, @PathVariable int idService) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        valoracionServiceDao.deleteValoracion(idService, user.getDni());
        return "redirect:../serviceValoration";
    }

    @RequestMapping(value = "/listMisValoraciones")
    public String ListMisValoraciones(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("listMisValoraciones", valoracionDao.getMisValoraciones(user.getDni()));//getVolunteerAsigned()
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String, Float> v = valoracionDao.getPromedio();
        Float promedio = v.get(user.getDni());
        model.addAttribute("puntuacion", promedio);
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("users", u);

        return gestionarAcceso(session, model, "Volunteer", "valoraciones/listMisValoraciones");
    }

    @RequestMapping(value = "/elderlyList")
    public String ListMisValoracionesElderly(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("listMisValoraciones", valoracionDao.getMisValoracionesElderly(user.getDni()));//getVolunteerAsigned()
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        return gestionarAcceso(session, model, "ElderlyPeople", "valoraciones/elderlyList");
    }

    @RequestMapping(value = "/addValoracion/{dniVolunteer}")
    public String addValoracion2(@PathVariable String dniVolunteer, Model model, HttpSession session) {
        Valoracion v = new Valoracion();
        v.setDniVolunteer(dniVolunteer);
        model.addAttribute("valoracion", v);
        return gestionarAcceso(session, model, "ElderlyPeople", "valoraciones/addValoracion");
    }

    @RequestMapping(value = "/listMisValoraciones/{dniVolunteer}")
    public String ListMisValoraciones2(HttpSession session, Model model, @PathVariable String dniVolunteer) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("listMisValoraciones", valoracionDao.getMisValoraciones(dniVolunteer));//getVolunteerAsigned()
        model.addAttribute("datos", valoracionDao.getUsersInfo());
        HashMap<String, Float> v = valoracionDao.getPromedio();
        Float promedio = v.get(dniVolunteer);
        if (promedio == null) {
            model.addAttribute("puntuacion", "No disponible");

        } else {
            model.addAttribute("puntuacion", promedio);

        }
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("users", u);
        return gestionarAcceso(session, model, "ElderlyPeople", "valoraciones/listMisValoraciones");
    }


    @RequestMapping(value = "/addValoracion", method = RequestMethod.POST)
    public String processAddSubmitValoracion(HttpSession session, Model model, @ModelAttribute("valoracion") Valoracion valoracion, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "valoraciones/addValoracion";
        UserDetails user = (UserDetails) session.getAttribute("user");
        valoracion.setDni(user.getDni());
        Boolean checkValoracion = valoracionDao.checkValoracion(user.getDni(), valoracion.getDniVolunteer());
        System.out.println("estado: " + checkValoracion + "INFO:" + valoracion.toString());
        if (!checkValoracion) {

            bindingResult.rejectValue("dniVolunteer", "dniVolunteer", "Ya ha valorado ha este voluntario");

            return "valoraciones/addValoracion";
        }
        valoracionDao.addValoracion(valoracion);
        return "redirect:elderlyList?nuevo=" + valoracion.getDni();
    }


}
