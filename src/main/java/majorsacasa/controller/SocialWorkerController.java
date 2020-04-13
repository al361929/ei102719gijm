package majorsacasa.controller;

import majorsacasa.dao.SocialWorkerDao;
import majorsacasa.model.SocialWorker;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/socialWorker")
public class SocialWorkerController extends Controlador{

    private SocialWorkerDao socialWorkerDao;

    @Autowired
    public void setSocialWorkerDao(SocialWorkerDao socialWorkerDao) {
        this.socialWorkerDao = socialWorkerDao;
    }

    @RequestMapping("/list")
    public String listSocialWorkers(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("socialWorkers", socialWorkerDao.getSocialWorkers());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "socialWorker/list";
    }

    @RequestMapping(value = "/add")
    public String addSocialWorker(Model model) {
        model.addAttribute("socialWorker", new SocialWorker());
        return "socialWorker/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("socialWorker") SocialWorker socialWorker, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "socialWorker/add";
        socialWorkerDao.addSocialWorker(socialWorker);
        return "redirect:list?nuevo=" + socialWorker.getDni();
    }

    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editSocialWorker(Model model, @PathVariable String dni) {
        model.addAttribute("socialWorker", socialWorkerDao.getSocialWorker(dni));
        return "socialWorker/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("socialWorker") SocialWorker socialWorker,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "socialWorker/update";
        socialWorkerDao.updateSocialWorker(socialWorker);
        return "redirect:list?nuevo=" + socialWorker.getDni();
    }

    @RequestMapping(value = "/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        socialWorkerDao.deleteSocialWorker(dni);
        return "redirect:../list";
    }

    @RequestMapping(value = "/elderlyList")
    public String getElderlyList(HttpSession session,Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("elderlyList", socialWorkerDao.getElderlyList(user.getDni()));
        return gestionarAcceso(session,model,"SocialWorker","socialWorker/elderlyListSW");

        //return "socialWorker/elderlyListSW";
    }

    @RequestMapping(value = "/perfil", method = RequestMethod.GET)
    public String editSocialWorkerPerfil(HttpSession session,Model model) {
        String destino= sesionAbierta(session,model,"elderly/perfil");
        if (destino!=null) return destino;
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user.getTipo()!="SocialWorker") return "error/sinPermiso";


        model.addAttribute("socialWorker", socialWorkerDao.getSocialWorker(user.getDni()));
        //return "socialWorker/update";
        return gestionarAcceso(session,model,"SocialWorker","socialWorker/perfil");

    }

    @RequestMapping(value = "/updatePerfil", method = RequestMethod.POST)
    public String processUpdateSubmitPerfil(@ModelAttribute("socialWorker") SocialWorker socialWorker,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "socialWorker/perfil";
        socialWorkerDao.updateSocialWorker(socialWorker);
        return "redirect:/";
    }




}
