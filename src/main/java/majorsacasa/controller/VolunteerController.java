package majorsacasa.controller;

import majorsacasa.dao.ValoracionDao;
import majorsacasa.dao.VolunteerDao;
import majorsacasa.dao.VolunteerTimeDao;
import majorsacasa.model.UserDetails;
import majorsacasa.model.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;


@Controller
@RequestMapping("/volunteer")
public class VolunteerController extends Controlador {

    private VolunteerDao volunteerDao;
    private VolunteerTimeDao volunteerTimeDao;
    private ValoracionDao valoracionDao;

    @Autowired
    public void setVolunteerDao(VolunteerDao volunteerDao, ValoracionDao valoracionDao, VolunteerTimeDao volunteerTimeDao) {
        this.volunteerDao = volunteerDao;
        this.valoracionDao = valoracionDao;
        this.volunteerTimeDao = volunteerTimeDao;
    }


    @RequestMapping("/list")
    public String listVolunteers(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("volunteers", volunteerDao.getVolunteersAll());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session, model, "SocialWorker", "volunteer/list");
    }

    @RequestMapping(value = "/add")
    public String addVolunteer(Model model) {
        model.addAttribute("volunteer", new Volunteer());
        return "volunteer/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("volunteer") Volunteer volunteer, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteer/add";
        volunteerDao.addVolunteer(volunteer);
        return "redirect:list?nuevo=" + volunteer.getDni();
    }

    @RequestMapping(value = "/addRegister")
    public String addVolunteerRegister(Model model) {
        model.addAttribute("volunteer", new Volunteer());
        return "volunteer/addRegister";
    }

    @RequestMapping(value = "/addRegister", method = RequestMethod.POST)
    public String processAddSubmitRegister(@ModelAttribute("volunteer") Volunteer volunteer, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteer/addRegister";
        Boolean check = volunteerDao.checkDNI(volunteer.getDni());
        Boolean checkUser = volunteerDao.checkUser(volunteer.getUsuario());

        if (!check) {

            bindingResult.rejectValue("dni", "dni", "Ya existe un usuario con este DNI");

            return "volunteer/addRegister";
        }
        if (!checkUser) {

            bindingResult.rejectValue("usuario", "usuario", volunteer.getUsuario()+" ya esta se esta utilizando");

            return "volunteer/addRegister";
        }
        volunteerDao.addVolunteer(volunteer);
        return "redirect:../login";
    }
    @RequestMapping(value = "/updateAcepted/{dni}")
    public String editVolunteerEstado(Model model, @PathVariable String dni) {
        Volunteer v= volunteerDao.getVolunteer(dni);
        v.setEstado("Aceptado");
        volunteerDao.updateVolunteerEstado(v.getDni(),v.getEstado());

        return "redirect:../list?nuevo=" +dni;

    }
    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editVolunteer(Model model, @PathVariable String dni) {
        model.addAttribute("volunteer", volunteerDao.getVolunteer(dni));
        return "volunteer/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("volunteer") Volunteer volunteer,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "volunteer/update";
        }
        volunteerDao.updateVolunteerSINpw(volunteer);
        return "redirect:list?nuevo=" + volunteer.getDni();
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        volunteerDao.deleteVolunteer(dni);
        return "redirect:../list";
    }

    @RequestMapping(value = "/scheduleList")
    public String getScheduleList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("scheduleList", volunteerDao.getScheduleList(user.getDni()));
        HashMap<String ,String> u=valoracionDao.getUsersInfo();
        model.addAttribute("usuario",u);
        return "volunteer/scheduleList";
    }



    @RequestMapping(value = "/perfil", method = RequestMethod.GET)
    public String editVolunteerPerfil(HttpSession session,Model model) {
        String destino= sesionAbierta(session,model,"volunteer/perfil");
        if (destino!=null) return destino;
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user.getTipo()!="Volunteer") return "error/sinPermiso";

        model.addAttribute("volunteer", volunteerDao.getVolunteer(user.getDni()));
        //return "volunteer/update";
        return gestionarAcceso(session,model,"Volunteer","volunteer/perfil");

    }

    @RequestMapping(value = "/updatePerfil", method = RequestMethod.POST)
    public String processUpdateSubmitPerfil(@ModelAttribute("volunteer") Volunteer volunteer,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.toString());
            System.out.println(volunteer.toString());
            return "volunteer/perfil";
        }
        System.out.println(volunteer.toString());
        volunteerDao.updateVolunteer(volunteer);
        return "redirect:/volunteer/scheduleList";
    }

    @RequestMapping(value = "/elderlyList")
    public String getElderlyList(HttpSession session,Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("elderlyList", volunteerDao.getElderlyList(user.getDni()));
        HashMap<String ,String> u=valoracionDao.getUsersInfo();
        model.addAttribute("usuario",u);
        return gestionarAcceso(session,model,"Volunteer","volunteer/elderlyListV");

        //return "socialWorker/elderlyListSW";
    }

    @RequestMapping("/listVolunteer")
    public String listVolunteersElderly(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        //model.addAttribute("volunteers", volunteerDao.getVolunteers());
        UserDetails user = (UserDetails) session.getAttribute("user");
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("horarios", volunteerDao.getScheduleListDisponibles());
        model.addAttribute("promedio", valoracionDao.getPromedio());
        System.out.println(volunteerDao.getScheduleListDisponibles());

        return gestionarAcceso(session, model, "ElderlyPeople", "volunteer/listVolunteer");
    }

    @RequestMapping("/listMisVolunteer")
    public String listVolunteersElderlyQ(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        model.addAttribute("misVoluntarios", volunteerDao.getVolunteerAsigned(user.getDni()));//getVolunteerAsigned()
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("promedio",valoracionDao.getPromedio());

        return gestionarAcceso(session, model, "ElderlyPeople", "volunteer/listMisVolunteer");
    }
/*
    @RequestMapping(value = "/tiempoDelVoluntario/{dni}")
    public String getScheduleList9(@PathVariable String dni,HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        Volunteer v = volunteerDao.getVolunteer(dni);
        model.addAttribute("name","Horarios de "+v.getNombre());
        model.addAttribute("scheduleList", volunteerDao.getScheduleListDisponibles(dni));
        return "volunteer/tiempoDelVoluntario";
    }*/

}
