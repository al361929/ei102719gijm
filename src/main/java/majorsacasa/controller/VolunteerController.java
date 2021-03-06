package majorsacasa.controller;

import majorsacasa.dao.UserDao;
import majorsacasa.dao.ValoracionDao;
import majorsacasa.dao.VolunteerDao;
import majorsacasa.mail.MailBody;
import majorsacasa.mail.MailService;
import majorsacasa.model.UserDetails;
import majorsacasa.model.Volunteer;
import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/volunteer")
public class VolunteerController extends ManageAccessController {

    static String mensajeError = "";
    private VolunteerDao volunteerDao;
    private ValoracionDao valoracionDao;
    private MailBody mailBody;
    private MailService mailService;
    private UserDao userDao;

    @Autowired
    public void setVolunteerDao(VolunteerDao volunteerDao, ValoracionDao valoracionDao, MailService mailService, UserDao userDao) {
        this.volunteerDao = volunteerDao;
        this.valoracionDao = valoracionDao;
        this.mailService = mailService;
        this.userDao = userDao;
    }


    @RequestMapping("/list")
    public String listVolunteers(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("volunteers", volunteerDao.getVolunteersAll());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("mensaje", mensajeError);
        mensajeError=" ";

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
        UserDetails user = userDao.loadUserByUsername(volunteer.getUsuario(), volunteer.getContraseña());

        mailBody = new MailBody(volunteer.getEmail());
        mailBody.addMail("Se ha creado su cuenta correctamente.\n" +
                "El usuario y contraseña con el que puede acceder son:\n" +
                "Usuario: " + volunteer.getUsuario() +
                "\nContraseña: " + volunteer.getContraseña());
        mailService.sendEmail(mailBody, user);

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

            bindingResult.rejectValue("usuario", "usuario", volunteer.getUsuario() + " ya esta se esta utilizando");

            return "volunteer/addRegister";
        }
        volunteerDao.addVolunteer(volunteer);
        UserDetails user = userDao.loadUserByUsername(volunteer.getUsuario(), volunteer.getContraseña());

        mailBody = new MailBody(volunteer.getEmail());
        mailBody.addMail("Se ha creado su cuenta correctamente.\n" +
                "El usuario y contraseña con el que puede acceder son:\n" +
                "Usuario: " + volunteer.getUsuario() +
                "\nContraseña: " + volunteer.getContraseña());
        mailService.sendEmail(mailBody, user);

        return "redirect:../login";
    }

    @RequestMapping(value = "/accept/{dni}")
    public String acceptVolunteerEstado(@PathVariable String dni) {
        Volunteer volunteer = volunteerDao.getVolunteer(dni);
        volunteer.setEstado("Aceptado");
        volunteerDao.updateVolunteerEstado(volunteer.getDni(), volunteer.getEstado());

        UserDetails user = userDao.loadUserByUsername(volunteer.getUsuario(), volunteer.getContraseña());
        mailBody = new MailBody(volunteer.getEmail());
        mailBody.addMail("Se ha aceptado su cuenta.\n" +
                "El usuario y contraseña con el que puede acceder son:\n" +
                "Usuario: " + volunteer.getUsuario() +
                "\nContraseña: " + volunteer.getContraseña());
        mailService.sendEmail(mailBody, user);

        return "redirect:../list?nuevo=" + dni;
    }

    @RequestMapping(value = "/reject/{dni}")
    public String rejectVolunteerEstado(@PathVariable String dni) {
        Volunteer volunteer = volunteerDao.getVolunteer(dni);
        volunteer.setEstado("Rechazado");
        volunteerDao.updateVolunteerEstado(volunteer.getDni(), volunteer.getEstado());

        UserDetails user = userDao.loadUserByUsername(volunteer.getUsuario(), volunteer.getContraseña());
        mailBody = new MailBody(volunteer.getEmail());
        mailBody.addMail("Se ha rechazado su peticion de cuenta, por favor contacta la Conselleria para más información.\n" +
                "Puede contactar a través de: majorsacasagva@gmail.com\n");

        mailService.sendEmail(mailBody, user);

        return "redirect:../list?nuevo=" + dni;

    }

    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editVolunteer(Model model, @PathVariable String dni) {
        model.addAttribute("volunteer", volunteerDao.getVolunteer(dni));
        return "volunteer/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("volunteer") Volunteer volunteer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "volunteer/update";
        }
        volunteerDao.updateVolunteerSINpw(volunteer);
        Volunteer voluntario = volunteerDao.getVolunteer(volunteer.getDni());
        UserDetails user = userDao.loadUserByUsername(voluntario.getUsuario(), voluntario.getContraseña());

        mailBody = new MailBody(voluntario.getEmail());
        mailBody.updateMail("Se han actualizado los datos de su cuenta correctamente.");
        mailService.sendEmail(mailBody, user);

        return "redirect:list?nuevo=" + voluntario.getDni();
    }

    @RequestMapping(value = "/confirmarDelete")
    public String confirmarDelete(HttpSession httpSession, Model model) {
        UserDetails usuario = (UserDetails) httpSession.getAttribute("user");
        model.addAttribute("user", usuario);
        model.addAttribute("userType", usuario.getTipo().toLowerCase());

        return gestionarAcceso(httpSession, model, "Volunteer", "deletePerfil");
    }


    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        Volunteer volunteer = volunteerDao.getVolunteer(dni);
        UserDetails usuario = userDao.loadUserByUsername(volunteer.getUsuario(), volunteer.getContraseña());
        try {
            volunteerDao.deleteVolunteer(dni);
            mailBody = new MailBody(volunteer.getEmail());
            mailBody.deleteMail("Se ha eliminado su cuenta correctamente");
            mailService.sendEmail(mailBody, usuario);
            if (usuario.getTipo().equals("Volunteer")) {
                return "redirect:/logout";
            }
        } catch (Exception e) {
            if (usuario.getTipo().equals("Admin")) {
                mensajeError = "No puedes eliminar un voluntario si tiene horarios";
            } else if (usuario.getTipo().equals("Volunteer")) {
                mensajeError = "No puedes eliminar tu cuenta si tienes horarios activos";
                return "redirect:../../volunteer/perfil";
            }
        }
        return "redirect:../list";
    }

    @RequestMapping(value = "/scheduleList")
    public String getScheduleList(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        List<VolunteerTime> horarios = volunteerDao.getScheduleList(user.getDni());
        HashMap<Integer, Boolean> mapa = new HashMap<>();
        for (VolunteerTime horario : horarios) {
            LocalDate fecha = LocalDate.of(LocalDate.now().getYear(), horario.getMesInt(), horario.getDia());
            if (fecha.isBefore(LocalDate.now())) {
                mapa.put(horario.getIdVolunteerTime(), true);
            } else {
                mapa.put(horario.getIdVolunteerTime(), false);
            }
        }
        model.addAttribute("mapaBorrar", mapa);
        model.addAttribute("scheduleList", volunteerDao.getScheduleList(user.getDni()));
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        model.addAttribute("voluntario", volunteerDao.getVolunteer(user.getDni()));
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session, model, "Volunteer", "volunteer/scheduleList");
    }


    @RequestMapping(value = "/perfil", method = RequestMethod.GET)
    public String editVolunteerPerfil(HttpSession session, Model model) {
        String destino = sesionAbierta(session, model, "volunteer/perfil");
        if (destino != null) return destino;
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user.getTipo() != "Volunteer") return "error/sinPermiso";

        model.addAttribute("mensaje", mensajeError);
        mensajeError = " ";
        model.addAttribute("volunteer", volunteerDao.getVolunteer(user.getDni()));
        return gestionarAcceso(session, model, "Volunteer", "volunteer/perfil");

    }

    @RequestMapping(value = "/updatePerfil", method = RequestMethod.POST)
    public String processUpdateSubmitPerfil(@ModelAttribute("volunteer") Volunteer volunteer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "volunteer/perfil";
        }
        volunteerDao.updateVolunteer(volunteer);
        UserDetails user = userDao.loadUserByUsername(volunteer.getUsuario(), volunteer.getContraseña());

        mailBody = new MailBody(volunteer.getEmail());
        mailBody.updateMail("Se han actualizado los datos de su cuenta correctamente.");
        mailService.sendEmail(mailBody, user);

        return "redirect:/volunteer/scheduleList";
    }

    @RequestMapping(value = "/elderlyList")
    public String getElderlyList(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("elderlyList", volunteerDao.getElderlyList(user.getDni()));
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        return gestionarAcceso(session, model, "Volunteer", "volunteer/elderlyListV");
    }

    @RequestMapping("/listVolunteer")
    public String listVolunteersElderly(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("horarios", volunteerDao.getScheduleListDisponibles());
        model.addAttribute("promedio", valoracionDao.getPromedio());

        return gestionarAcceso(session, model, "ElderlyPeople", "volunteer/listVolunteer");
    }

    @RequestMapping("/misHorariosElderly/{dni}")
    public String listVolunteersElderly2(HttpSession session, Model model, @PathVariable String dni) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("horarios", volunteerDao.getMisHorariosElderly(dni, user.getDni()));
        model.addAttribute("promedio", valoracionDao.getPromedio());
        return gestionarAcceso(session, model, "ElderlyPeople", "volunteer/listVolunteerElderly");
    }


    @RequestMapping("/listMisVolunteer")
    public String listVolunteersElderlyQ(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("misVoluntarios", volunteerDao.getVolunteerAsigned(user.getDni()));

        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("promedio", valoracionDao.getPromedio());

        return gestionarAcceso(session, model, "ElderlyPeople", "volunteer/listMisVolunteer");
    }

}
