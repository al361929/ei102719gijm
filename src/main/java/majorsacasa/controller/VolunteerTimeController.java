package majorsacasa.controller;


import majorsacasa.dao.ValoracionDao;
import majorsacasa.dao.VolunteerDao;
import majorsacasa.dao.VolunteerTimeDao;
import majorsacasa.model.UserDetails;
import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequestMapping("/volunteertime")
public class VolunteerTimeController extends ManageAccessController {
    private ValoracionDao valoracionDao;
    private MailController mailController;
    private VolunteerDao volunteerDao;

    private VolunteerTimeDao volunteerTimeDao;
    private final List<String> meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");


    @Autowired
    public void setVolunteerTimeDao(VolunteerTimeDao volunteerTimeDao, ValoracionDao valoracionDao, VolunteerDao volunteerDao) {
        this.valoracionDao = valoracionDao;
        this.volunteerTimeDao = volunteerTimeDao;
        this.volunteerDao = volunteerDao;
    }

    @RequestMapping("/list")
    public String listVolunteerTimes(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("volunteerstime", volunteerTimeDao.getVolunteersTime());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "volunteertime/list";
    }

    @RequestMapping(value = "/add")
    public String addVolunteerTime(Model model) {
        model.addAttribute("meses", meses);
        model.addAttribute("volunteertime", new VolunteerTime());
        return "volunteertime/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(HttpSession session, @ModelAttribute("volunteertime") VolunteerTime volunteertime, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/add";
        UserDetails user = (UserDetails) session.getAttribute("user");

        volunteertime.setDniElderly(null);
        volunteertime.setDniVolunteer(user.getDni());
        System.out.println(volunteertime.toString());
        volunteerTimeDao.addVolunteerTime(volunteertime);
        int id = volunteerTimeDao.ultimoIdService();

        mailController = new MailController(volunteerDao.getVolunteer(volunteertime.getDniVolunteer()).getEmail());
        mailController.addMail("Se ha añadido un nuevo horario a su cuenta correctamente.");

        return "redirect:../volunteer/scheduleList?nuevo=" + id;


    }

    @RequestMapping(value = "/update/{idVolunteerTime}", method = RequestMethod.GET)
    public String editVolunteerTime(Model model, @PathVariable Integer idVolunteerTime, HttpSession session) {
        model.addAttribute("meses", meses);
        model.addAttribute("volunteertime", volunteerTimeDao.getVolunteerTime(idVolunteerTime));
        return gestionarAcceso(session, model, "Volunteer", "volunteertime/update");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("volunteertime") VolunteerTime volunteertime,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/update";
        volunteerTimeDao.updateVolunteerTime(volunteertime);

        mailController = new MailController(volunteerDao.getVolunteer(volunteertime.getDniVolunteer()).getEmail());
        mailController.updateMail("Se ha actualizado su horario del dia " + volunteertime.getDia() + " del " + volunteertime.getMes() + " de " + volunteertime.getStartTime() + " a " + volunteertime.getEndTime() + ".");

        return "redirect:../volunteer/scheduleList?nuevo=" + volunteertime.getIdVolunteerTime();
    }

    @RequestMapping(value = "/delete/{idVolunteerTime}")
    public String processDelete(@PathVariable Integer idVolunteerTime) {
        VolunteerTime volunteerTime = volunteerTimeDao.getVolunteerTime(idVolunteerTime);
        mailController = new MailController(volunteerDao.getVolunteer(volunteerTime.getDniVolunteer()).getEmail());
        mailController.deleteMail("Se ha eliminado su horario del dia " + volunteerTime.getDia() + " del " + volunteerTime.getMes() + " de " + volunteerTime.getStartTime() + " a " + volunteerTime.getEndTime() + ".");

        volunteerTimeDao.deleteVolunteerTime(idVolunteerTime);
        return "redirect:/volunteer/scheduleList";
    }

    @RequestMapping(value = "/addTime")
    public String addVolunteerTimeVolunteer(HttpSession session, Model model) {
        model.addAttribute("meses", meses);
        UserDetails user = (UserDetails) session.getAttribute("user");

        model.addAttribute("volunteertime", new VolunteerTime());
        model.addAttribute("dniVolunteer", user.getDni());
        return "volunteertime/addTime";
    }

    @RequestMapping(value = "/addTime", method = RequestMethod.POST)
    public String processAddSubmitVolunteer(HttpSession session, @ModelAttribute("volunteertime") VolunteerTime volunteertime, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/addTime";
        UserDetails user = (UserDetails) session.getAttribute("user");
        volunteertime.setDniElderly(null);
        volunteertime.setDniVolunteer(user.getDni());
        volunteerTimeDao.addVolunteerTime(volunteertime);
        if (user.getTipo().equals("Volunteer")) {
            return "volunteer/scheduleList";
        }

        mailController = new MailController(volunteerDao.getVolunteer(volunteertime.getDniVolunteer()).getEmail());
        mailController.addMail("Se ha añadido un nuevo horario a su cuenta correctamente.");

        return "redirect:/";
    }

    @RequestMapping(value = "/scheduleListVolunteer")
    public String getScheduleList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("scheduleList", volunteerTimeDao.getScheduleList(user.getDni()));
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        return "volunteer/scheduleList";
    }

    @RequestMapping(value = "/solicitar/{idVolunteerTime}")
    public String solicitar(HttpSession session, @PathVariable Integer idVolunteerTime) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        volunteerTimeDao.solicitar(idVolunteerTime, user.getDni());
        VolunteerTime vTime = volunteerTimeDao.getVolunteerTime(idVolunteerTime);
        return "redirect:../../volunteer/listVolunteer?nuevo=" + vTime.getDniVolunteer();
    }

}
