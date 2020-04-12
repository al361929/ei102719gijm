package majorsacasa.controller;

import majorsacasa.dao.ElderlyDao;
import majorsacasa.model.Elderly;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

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
    public String listElderlys(HttpSession session,Model model) {
        model.addAttribute("elderlys", elderlyDao.getElderlys());
        return gestionarAcceso(session,model,"SocialWorker","elderly/list");

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
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{dni}", method = RequestMethod.GET)
    public String editElderly(HttpSession session,Model model, @PathVariable String dni) {

        model.addAttribute("allergies", alergias);
        model.addAttribute("elderly", elderlyDao.getElderly(dni));
        return gestionarAcceso(session,model,"SocialWorker","elderly/update");
       // return "elderly/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("elderly") Elderly elderly,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "elderly/update";
        elderlyDao.updateElderly(elderly);
        return "redirect:list";
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
}
