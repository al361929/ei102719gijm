package majorsacasa.controller;

import majorsacasa.dao.VolunteerDao;
import majorsacasa.model.UserDetails;
import majorsacasa.model.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/volunteer")
public class VolunteerController extends Controlador {

    private VolunteerDao volunteerDao;

    @Autowired
    public void setVolunteerDao(VolunteerDao volunteerDao) {
        this.volunteerDao = volunteerDao;
    }


    @RequestMapping("/list")
    public String listVolunteers(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("volunteers", volunteerDao.getVolunteers());
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
        volunteerDao.addVolunteer(volunteer);
        return "redirect:../login";
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
            System.out.println(bindingResult.toString());
            System.out.println(volunteer.toString());
            return "volunteer/update";
        }
        System.out.println(volunteer.toString());
        volunteerDao.updateVolunteer(volunteer);
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
        return "redirect:/";
    }

    @RequestMapping(value = "/elderlyList")
    public String getElderlyList(HttpSession session,Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("elderlyList", volunteerDao.getElderlyList(user.getDni()));
        return gestionarAcceso(session,model,"Volunteer","volunteer/elderlyListV");

        //return "socialWorker/elderlyListSW";
    }

    @RequestMapping("/listVolunteer")
    public String listVolunteersElderly(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("volunteers", volunteerDao.getVolunteers());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session, model, "ElderlyPeople", "volunteer/listVolunteer");
    }

    @RequestMapping("/listMisVolunteer")
    public String listVolunteersElderlyQ(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        model.addAttribute("volunteers", volunteerDao.getVolunteerAsigned(user.getDni()));//getVolunteerAsigned()
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session, model, "ElderlyPeople", "volunteer/listMisVolunteer");
    }

}
