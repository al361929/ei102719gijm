package majorsacasa.controller;

import majorsacasa.dao.ValoracionDao;
import majorsacasa.model.UserDetails;
import majorsacasa.model.Valoracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
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

    @RequestMapping(value = "/listMisValoraciones")
    public String ListMisValoraciones(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("listMisValoraciones", valoracionDao.getMisValoraciones(user.getDni()));//getVolunteerAsigned()
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String ,Float> v=valoracionDao.getPromedio();
        Float promedio=v.get(user.getDni());
        model.addAttribute("puntuacion",promedio);


        return gestionarAcceso(session, model, "Volunteer", "valoraciones/listMisValoraciones");
    }
    @RequestMapping(value = "/elderlyList")
    public String ListMisValoracionesElderly(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("listMisValoraciones", valoracionDao.getMisValoracionesElderly(user.getDni()));//getVolunteerAsigned()
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session, model, "ElderlyPeople", "valoraciones/elderlyList");
    }
    @RequestMapping(value = "/addValoracion/{dniVolunteer}")
    public String addValoracion2(@PathVariable String dniVolunteer,Model model,HttpSession session) {
        Valoracion v= new Valoracion();
        v.setDniVolunteer(dniVolunteer);
        model.addAttribute("valoracion", v);

        return "valoraciones/addValoracion";
    }

    @RequestMapping(value = "/listMisValoraciones/{dniVolunteer}")
    public String ListMisValoraciones2(HttpSession session, Model model,@PathVariable String dniVolunteer) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("listMisValoraciones", valoracionDao.getMisValoraciones(dniVolunteer));//getVolunteerAsigned()
        HashMap<String ,Float> v=valoracionDao.getPromedio();
        Float promedio=v.get(dniVolunteer);
        model.addAttribute("puntuacion",promedio);
        return gestionarAcceso(session, model, "ElderlyPeople", "valoraciones/listMisValoraciones");
    }


    @RequestMapping(value = "/addValoracion", method = RequestMethod.POST)
    public String processAddSubmitValoracion(HttpSession session,Model model,@ModelAttribute("valoracion") Valoracion valoracion, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "valoraciones/addValoracion";
        UserDetails user = (UserDetails) session.getAttribute("user");
        valoracion.setDni(user.getDni());
        Boolean checkValoracion = valoracionDao.checkValoracion(user.getDni(),valoracion.getDniVolunteer());
        System.out.println("estado: "+checkValoracion+ "INFO:"+valoracion.toString());
        if (!checkValoracion) {

            bindingResult.rejectValue("dniVolunteer", "dniVolunteer", "Ya ha valorado ha este voluntario");

            return "valoraciones/addValoracion";
        }
        valoracionDao.addValoracion(valoracion);
        return "redirect:elderlyList?nuevo=" + valoracion.getDni();
    }


}
