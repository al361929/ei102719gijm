package majorsacasa.controller;

import majorsacasa.dao.TypeServiceDao;
import majorsacasa.model.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/typeService")
public class TypeServiceController {
    private TypeServiceDao typeserviceDao;

    @Autowired
    public void setServiceDao( TypeServiceDao typeserviceDao) {
        this.typeserviceDao = typeserviceDao;
    }
    @RequestMapping(value = "/add")
    public String addService(Model model) {
        model.addAttribute("TypeService", new TypeService());

        return "typeService/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("TypeService") TypeService typeService, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "typeService/add";
        }
        typeserviceDao.addService(typeService);
        return "redirect:../service/add";
    }


}
