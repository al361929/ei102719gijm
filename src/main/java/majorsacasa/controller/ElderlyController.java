package majorsacasa.controller;

import majorsacasa.dao.ElderlyDao;
import majorsacasa.model.Elderly;
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
@RequestMapping("/elderly")
public class ElderlyController  extends Controlador{

    private ElderlyDao elderlyDao;

    private List alergias = Arrays.asList("Polen", "Frutos secos", "Gluten", "Pepinillo");

    @Autowired
    public void setElderlyDao(ElderlyDao elderlyDao) {
        this.elderlyDao = elderlyDao;
    }

    @RequestMapping("/list")
    public String listElderlys(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("elderlys", elderlyDao.getElderlys());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
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
        elderlyDao.addElderly(elderly);
        return "redirect:list?nuevo=" + elderly.getDni();
    }

    @RequestMapping(value = "/addRegister")
    public String addElderlyRegister(Model model) {
        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", new Elderly());
        return "/elderly/addRegister";

    }

    @RequestMapping(value = "/addRegister", method = RequestMethod.POST)
    public String processAddSubmitRegister(@ModelAttribute("elderly") Elderly elderly, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/addRegister";
        elderlyDao.addElderly(elderly);
        return "redirect:/login";
    }

    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editElderly(HttpSession session, Model model, @PathVariable String dni) {

        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", elderlyDao.getElderly(dni));
        return gestionarAcceso(session, model, "SocialWorker", "elderly/update");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("elderly") Elderly elderly,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/update";
        elderlyDao.updateElderly(elderly);
        return "redirect:list?nuevo=" + elderly.getDni();
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(HttpSession session,@PathVariable String dni) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (!user.getTipo().equals("SocialWorker")) {
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
        elderlyDao.updateElderly(elderly);
        return "redirect:/";
    }
}
