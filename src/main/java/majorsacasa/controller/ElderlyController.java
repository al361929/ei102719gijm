package majorsacasa.controller;

import majorsacasa.dao.ElderlyDao;
import majorsacasa.dao.SocialWorkerDao;
import majorsacasa.model.Elderly;
import majorsacasa.model.SocialWorker;
import majorsacasa.model.UserDetails;
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

@Controller
@RequestMapping("/elderly")
public class ElderlyController  extends Controlador{

    private ElderlyDao elderlyDao;
    private SocialWorkerDao socialWorkerDao;
    private List<String> alergias = Arrays.asList("Ninguna", "Polen", "Frutos secos", "Gluten", "Pepinillo");

    @Autowired
    public void setElderlyDao(ElderlyDao elderlyDao, SocialWorkerDao socialWorkerDao) {
        this.elderlyDao = elderlyDao;
        this.socialWorkerDao = socialWorkerDao;
    }

    @RequestMapping("/list")
    public String listElderlys(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("elderlys", elderlyDao.getElderlys());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String ,String> u=elderlyDao.getUsersInfo();
        model.addAttribute("usuario",u);
        return gestionarAcceso(session, model, "SocialWorker", "elderly/list");
    }

    @RequestMapping(value = "/add")
    public String addElderly(HttpSession session,Model model) {
        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", new Elderly());
        return gestionarAcceso(session,model,"SocialWorker","elderly/add");

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("elderly") Elderly elderly, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/add";
        if (elderly.getAlergias()==null) elderly.setAlergias("");
        elderlyDao.addElderly(elderly);
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
    public String processAddSubmitRegister(@ModelAttribute("elderly") Elderly elderly,Model model, BindingResult bindingResult) {
       if (bindingResult.hasErrors())
            return "elderly/addRegister";
        Boolean check = elderlyDao.checkDNI(elderly.getDni());

        if (!check) {

            bindingResult.rejectValue("dni", "dni", "Ya existe un usuario con este DNI");

            return "elderly/addRegister";
        }
             Boolean checkUser = elderlyDao.checkUser(elderly.getUsuario());

      if (!checkUser) {

            bindingResult.rejectValue("usuario", "usuario", elderly.getUsuario()+" ya esta se esta utilizando");

            return "elderly/addRegister";
        }
        elderlyDao.addElderly(elderly);
        return "redirect:/login";
    }

    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editElderly(HttpSession session, Model model, @PathVariable String dni) {
        Elderly eld = elderlyDao.getElderly(dni);
        if(eld.Alergias()) {
            List<String> alergiasEld = Arrays.asList(eld.getAlergias().split(","));
            model.addAttribute("alergiasEld", alergiasEld);
        }
        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", elderlyDao.getElderly(dni));
        List<SocialWorker> social = socialWorkerDao.getSocialWorkers();
        model.addAttribute("SocialWorkers", social);
        return gestionarAcceso(session, model, "SocialWorker", "elderly/update");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("elderly") Elderly elderly,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/update";

        elderlyDao.updateElderlySINpw(elderly);
        return "redirect:list?nuevo=" + elderly.getDni();
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(HttpSession session,@PathVariable String dni) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (!user.getTipo().equals("SocialWorker") && !user.getTipo().equals("Admin")) {
            return "error/sinPermiso";
        }
        elderlyDao.deleteElderly(dni);
        return "redirect:../list";
    }


    @RequestMapping(value = "/perfil", method = RequestMethod.GET)
    public String editElderlyPerfil(HttpSession session,Model model) {
        String destino= sesionAbierta(session,model,"elderly/perfil");

        if (destino!=null) return destino;

        model.addAttribute("allergies", alergias);
        UserDetails user = (UserDetails) session.getAttribute("user");

        if (user.getTipo()!="ElderlyPeople") return "error/sinPermiso";

        model.addAttribute("elderly", elderlyDao.getElderly(user.getDni()));
        return gestionarAcceso(session,model,"ElderlyPeople","elderly/perfil");
    }

    @RequestMapping(value = "/updatePerfil", method = RequestMethod.POST)
    public String processUpdatePerfilSubmit(@ModelAttribute("elderly") Elderly elderly,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/updatePerfil";
        elderlyDao.updateElderlySinSocialWorker(elderly);
        return "redirect:/request/listElderly";
    }


}
