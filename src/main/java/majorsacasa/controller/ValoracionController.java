package majorsacasa.controller;

import majorsacasa.dao.SocialWorkerDao;
import majorsacasa.dao.ValoracionDao;
import majorsacasa.model.SocialWorker;
import majorsacasa.model.UserDetails;
import majorsacasa.model.Valoracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/valoraciones")
public class ValoracionController extends Controlador {

    private ValoracionDao valoracionDao;

    @Autowired
    public void setValoracionDao(ValoracionDao valoracionDao) {
        this.valoracionDao = valoracionDao;
    }

    @RequestMapping("/list")
    public String listValoraciones(Model model) {
        model.addAttribute("valoraciones", valoracionDao.getValoraciones());
        return "valoraciones/list";
    }

    @RequestMapping(value = "/add")
    public String addValoracion(Model model) {
        model.addAttribute("valoracion", new Valoracion());
        return "valoraciones/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("valoracion") Valoracion valoracion, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "valoracion/add";
        valoracionDao.addValoracion(valoracion);
        return "redirect:list?nuevo=" + valoracion.getDni();
    }

    @RequestMapping(value = "/update/{dniVolunteer}/{dni}", method = RequestMethod.GET)
    public String editValoracion(Model model, @PathVariable String dniVolunteer, @PathVariable String dni) {
        model.addAttribute("valoracion", valoracionDao.getValoracion(dniVolunteer, dni));
        return "valoraciones/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("valoracion") Valoracion valoracion,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "valoraciones/update";
        valoracionDao.updateValoracion(valoracion);
        return "redirect:list?nuevo=" + valoracion.getDni();
    }

    @RequestMapping(value = "/delete/{dniVolunteer}/{dni}")
    public String processDelete(@PathVariable String dniVolunteer, @PathVariable String dni) {
        valoracionDao.deleteValoracion(dniVolunteer, dni);
        return "redirect:../list";
    }

}
