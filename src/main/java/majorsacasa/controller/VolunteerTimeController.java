package majorsacasa.controller;


import majorsacasa.dao.VolunteerTimeDao;
import majorsacasa.model.VolunteerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@RequestMapping("/volunteertime")
public class VolunteerTimeController {

    private VolunteerTimeDao volunteerTimeDao;

    @Autowired
    public void setVolunteerTimeDao(VolunteerTimeDao volunteerTimeDao) {
        this.volunteerTimeDao = volunteerTimeDao;
    }

    @RequestMapping("/list")
    public String listSocialWorkers(Model model) {
        model.addAttribute("volunteersTime", volunteerTimeDao.getVolunteersTime());
        return "volunteertime/list";
    }

    @RequestMapping(value = "/add")
    public String addSocialWorker(Model model) {
        model.addAttribute("volunteertime", new VolunteerTime());
        return "volunteertime/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("volunteertime") VolunteerTime volunteertime, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/add";
        volunteerTimeDao.addVolunteerTime(volunteertime);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{dniVoluntario}", method = RequestMethod.GET)
    public String editSocialWorker(Model model, @PathVariable String dniVoluntario) {
        model.addAttribute("volunteertime", volunteerTimeDao.getVolunteerTime(dniVoluntario));
        return "volunteertime/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("volunteertime") VolunteerTime volunteertime,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "volunteertime/update";
        volunteerTimeDao.updateVolunteerTime(volunteertime);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{dniVoluntario}")
    public String processDelete(@PathVariable String dniVoluntario) {
        volunteerTimeDao.deleteVolunteerTime(dniVoluntario);
        return "redirect:../list";
    }

}
