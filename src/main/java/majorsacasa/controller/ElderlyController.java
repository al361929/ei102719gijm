package majorsacasa.controller;

import majorsacasa.dao.*;
import majorsacasa.mail.MailBody;
import majorsacasa.mail.MailService;
import majorsacasa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.reverse;

@Controller
@RequestMapping("/elderly")
public class ElderlyController extends ManageAccessController {

    private ElderlyDao elderlyDao;
    private SocialWorkerDao socialWorkerDao;
    private VolunteerTimeDao volunteerTimeDao;
    private final List<String> alergias = Arrays.asList("Ninguna", "Polen", "Frutos secos", "Gluten", "Pepinillo");
    private MailBody mailBody;
    private MailService mailService;
    static String mensajeError = "";
    private UserDao userDao;

    @Autowired
    public void setElderlyDao(ElderlyDao elderlyDao, SocialWorkerDao socialWorkerDao, VolunteerTimeDao volunteerTimeDao, MailService mailService, UserDao userDao) {
        this.elderlyDao = elderlyDao;
        this.socialWorkerDao = socialWorkerDao;
        this.volunteerTimeDao = volunteerTimeDao;
        this.mailService = mailService;
        this.userDao = userDao;
    }

    @RequestMapping("/list")
    public String listElderlys(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("elderlys", elderlyDao.getElderlys());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String, String> u = elderlyDao.getUsersInfo();
        model.addAttribute("usuario", u);
        model.addAttribute("mensaje", mensajeError);
        mensajeError="";
        return gestionarAcceso(session, model, "SocialWorker", "elderly/list");
    }

    @RequestMapping(value = "/add")
    public String addElderly(HttpSession session, Model model) {
        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", new Elderly());
        return gestionarAcceso(session, model, "SocialWorker", "elderly/add");

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("elderly") Elderly elderly, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/add";
        if (elderly.getAlergias() == null) elderly.setAlergias("");
        elderlyDao.addElderly(elderly);
        if (!elderly.getEmail().isEmpty()) {
            mailBody = new MailBody(elderly.getEmail());
            mailBody.addMail("El CAS ha registrado su cuenta correctamente.\n" +
                    "El usuario y contraseña con el que puede acceder son:\n" +
                    "Usuario: " + elderly.getUsuario() +
                    "\nContraseña: " + elderly.getContraseña());
        } else {
            mailBody = new MailBody(elderly.getDireccion());
            mailBody.addMail("Se eviaran todos los datos de la cuenta por correo postal a la dirección:" +
                    elderly.getDireccion() + "\nEl usuario y contraseña con el que puede acceder son:\n" +
                    "Usuario: " + elderly.getUsuario() +
                    "\nContraseña: " + elderly.getContraseña());
        }
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
        mailService.sendEmail(mailBody, user);

        return "redirect:list?nuevo=" + elderly.getDni();
    }

    @RequestMapping(value = "/addRegister")
    public String addElderlyRegister(Model model) {
        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", new Elderly());

        List<SocialWorker> social = socialWorkerDao.getSocialWorkers();
        model.addAttribute("SocialWorkers", social);
        return "/elderly/addRegister";

    }

    @RequestMapping(value = "/addRegister", method = RequestMethod.POST)
    public String processAddSubmitRegister(@ModelAttribute("elderly") Elderly elderly, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/addRegister";
        Boolean check = elderlyDao.checkDNI(elderly.getDni());

        if (!check) {

            bindingResult.rejectValue("dni", "dni", "Ya existe un usuario con este DNI");
            model.addAttribute("allergies", alergias);

            return "elderly/addRegister";
        }
        Boolean checkUser = elderlyDao.checkUser(elderly.getUsuario());

        if (!checkUser) {

            bindingResult.rejectValue("usuario", "usuario", elderly.getUsuario() + " ya esta se esta utilizando");
            model.addAttribute("allergies", alergias);

            return "elderly/addRegister";
        }
        elderlyDao.addElderly(elderly);
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());

        if (!elderly.getEmail().isEmpty()) {
            mailBody = new MailBody(elderly.getEmail());
        } else {
            mailBody = new MailBody(elderly.getDireccion());
        }
        mailBody.setContent("Muchas gracias por registrarse en nuestra aplicación" +
                "Su cuenta se ha creado correctamente.\n" +
                "El usuario y contraseña con el que puede acceder son:\n" +
                "Usuario: " + elderly.getUsuario() +
                "\nContraseña: " + elderly.getContraseña());
        mailService.sendEmail(mailBody, user);


        if (user == null) {
            return "redirect:../login";

        }
        if (user.getTipo().equals("Admin"))
            return "redirect:../elderly/list?nuevo=" + elderly.getDni();

        return "redirect:../login";
    }

    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editElderly(HttpSession session, Model model, @PathVariable String dni) {
        Elderly eld = elderlyDao.getElderly(dni);
        if (eld.Alergias()) {
            List<String> alergiasEld = Arrays.asList(eld.getAlergias().split(","));
            model.addAttribute("alergiasEld", alergiasEld);
        }
        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", elderlyDao.getElderly(dni));
        List<SocialWorker> social = socialWorkerDao.getSocialWorkers();
        reverse(social);
        model.addAttribute("SocialWorkers", social);
        return gestionarAcceso(session, model, "SocialWorker", "elderly/update");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("elderly") Elderly elderly, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/update";
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
        elderly.actualizarAlergias();
        elderlyDao.updateElderlySINpw(elderly);

        if (!elderly.getEmail().isEmpty()) {
            mailBody = new MailBody(elderly.getEmail());
        } else {
            mailBody = new MailBody(elderly.getDireccion());
        }
        mailBody.updateMail("Se han actualizado los datos de su cuenta correctamente.");
        mailService.sendEmail(mailBody, user);

        return "redirect:list?nuevo=" + elderly.getDni();
    }

    private Boolean requestsToDelete(String dni) {
        List<Request> requests = elderlyDao.getRequestsElderly(dni);
        for (Request peticion : requests) {
            if (peticion.getState().equals("Pendiente") || peticion.getState().equals("Aceptada")) {
                return false;
            }
        }
        return true;
    }

    private void volunteertimeToDelete(String dni) {
        List<VolunteerTime> volunteerTimes = elderlyDao.getVolunteerTimeElderly(dni);
        for (VolunteerTime horario : volunteerTimes) {
            horario.setDniElderly(null);
            volunteerTimeDao.updateVolunteerTime(horario);
        }
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        Elderly elderly = elderlyDao.getElderly(dni);
        UserDetails usuario = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
        if (!usuario.getTipo().equals("ElderlyPeople") && !usuario.getTipo().equals("Admin")) {
            return "error/sinPermiso";
        }
        if (!elderly.getEmail().isEmpty()) {
            mailBody = new MailBody(elderly.getEmail());
        } else {
            mailBody = new MailBody(elderly.getDireccion());
        }
        if (requestsToDelete(dni)) {
            volunteertimeToDelete(dni);
            elderlyDao.deleteElderly(dni);
            mailBody.deleteMail("Se ha eliminado su cuenta permanentemente.");
            mailService.sendEmail(mailBody, usuario);

            if (usuario.getTipo().equals("ElderlyPeople")) {
                return "redirect:/logout";
            }
        } else {
            if (usuario.getTipo().equals("Admin")) {
                mensajeError = "No puedes borrar una persona mayor que tenga servicios activos";
            } else if (usuario.getTipo().equals("ElderlyPeople")) {
                mensajeError = "No puedes eliminar tu cuenta si tienes servicios activos";
                return "redirect:../../elderly/perfil";
            }
        }
        return "redirect:../list";
    }

    @RequestMapping(value = "/confirmarDelete/{dni}")
    public String confirmarDelete(@PathVariable String dni, HttpSession httpSession, Model model) {
        Elderly elderly = elderlyDao.getElderly(dni);
        model.addAttribute("user", elderly);
        model.addAttribute("userType", "elderly");

        return gestionarAcceso(httpSession, model, "ElderlyPeople", "deletePerfil");
    }

    @RequestMapping(value = "/perfil", method = RequestMethod.GET)
    public String editElderlyPerfil(HttpSession session, Model model) {
        String destino = sesionAbierta(session, model, "elderly/perfil");

        if (destino != null) return destino;

        model.addAttribute("allergies", alergias);
        UserDetails user = (UserDetails) session.getAttribute("user");

        if (user.getTipo() != "ElderlyPeople") return "error/sinPermiso";

        model.addAttribute("mensaje", mensajeError);
        mensajeError = " ";

        model.addAttribute("elderly", elderlyDao.getElderly(user.getDni()));
        return gestionarAcceso(session, model, "ElderlyPeople", "elderly/perfil");
    }

    @RequestMapping(value = "/updatePerfil", method = RequestMethod.POST)
    public String processUpdatePerfilSubmit(@ModelAttribute("elderly") Elderly elderly, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/updatePerfil";
        elderly.actualizarAlergias();
        elderlyDao.updateElderlySinSocialWorker(elderly);

        if (!elderly.getEmail().isEmpty()) {
            mailBody = new MailBody(elderly.getEmail());
        } else {
            mailBody = new MailBody(elderly.getDireccion());
        }
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());

        mailBody.updateMail("Se han actualizado los datos de su cuenta correctamente.");
        mailService.sendEmail(mailBody, user);

        return "redirect:/request/listElderly";
    }


}
