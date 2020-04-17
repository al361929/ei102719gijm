package majorsacasa.controller;


import majorsacasa.dao.VolunteerTimeDao;
import majorsacasa.model.UserDetails;
import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequestMapping("/volunteertime")
public class VolunteerTimeController {

    private VolunteerTimeDao volunteerTimeDao;
    private List<String> meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");


    @Autowired
    public void setVolunteerTimeDao(VolunteerTimeDao volunteerTimeDao) {
        this.volunteerTimeDao = volunteerTimeDao;
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
    public String processAddSubmit(@ModelAttribute("volunteertime") VolunteerTime volunteertime, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/add";
        volunteertime.setDniElderly(null);
        volunteerTimeDao.addVolunteerTime(volunteertime);
        return "redirect:list?nuevo=" + volunteertime.getIdVolunteerTime();
    }

    @RequestMapping(value = "/update/{idVolunteerTime}", method = RequestMethod.GET)
    public String editVolunteerTime(Model model, @PathVariable Integer idVolunteerTime) {
        model.addAttribute("meses", meses);
        model.addAttribute("volunteertime", volunteerTimeDao.getVolunteerTime(idVolunteerTime));
        return "volunteertime/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("volunteertime") VolunteerTime volunteertime,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/update";
        volunteerTimeDao.updateVolunteerTime(volunteertime);
        return "redirect:list?nuevo=" + volunteertime.getIdVolunteerTime();
    }

    @RequestMapping(value = "/delete/{idVolunteerTime}")
    public String processDelete(@PathVariable Integer idVolunteerTime) {
        volunteerTimeDao.deleteVolunteerTime(idVolunteerTime);
        return "redirect:../list";
    }
    @RequestMapping(value = "/addTime")
    public String addVolunteerTimeVolunteer(HttpSession session,Model model) {
        model.addAttribute("meses", meses);
        UserDetails user = (UserDetails) session.getAttribute("user");

        model.addAttribute("volunteertime", new VolunteerTime());
        model.addAttribute("dniVolunteer",user.getDni());
        return "volunteertime/addTime";
    }
    @RequestMapping(value = "/addTime", method = RequestMethod.POST)
    public String processAddSubmitVolunteer(HttpSession session,@ModelAttribute("volunteertime") VolunteerTime volunteertime, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/addTime";
        UserDetails user = (UserDetails) session.getAttribute("user");
        volunteertime.setDniElderly(null);
        volunteertime.setDniVolunteer(user.getDni());
        volunteerTimeDao.addVolunteerTime(volunteertime);
        return "redirect:/";
    }
}
