package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.model.Company;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/company")
public class CompanyController extends Controlador{

    private CompanyDao companyDao;

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @RequestMapping("/list")
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyDao.getCompanies());
        return "company/list";
    }

    @RequestMapping(value = "/add")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company());
        return "company/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("company") Company company, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "company/add";
        companyDao.addCompany(company);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{nif}", method = RequestMethod.GET)
    public String editCompany(Model model, @PathVariable String nif) {
        model.addAttribute("company", companyDao.getCompany(nif));
        return "company/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("company") Company company,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "company/update";
        companyDao.updateCompany(company);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{nif}")
    public String processDelete(@PathVariable String nif) {
        companyDao.deleteCompany(nif);
        return "redirect:../list";
    }

    @RequestMapping(value = "/perfil")
    public String getPerfil(HttpSession session, Model model) {
        String destino= sesionAbierta(session,model,"company/perfil");
        if (destino!=null) return destino;

        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("company", companyDao.getCompany(user.getDni()));
        return gestionarAcceso(session,model,"Company","company/perfil");

        //return "socialWorker/elderlyListSW";
    }
    @RequestMapping(value = "/updatePerfil", method = RequestMethod.POST)
    public String processUpdatePerfilSubmit(@ModelAttribute("company") Company company,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "company/perfil";
        companyDao.updateCompany(company);
        return "redirect:/";
    }

}
