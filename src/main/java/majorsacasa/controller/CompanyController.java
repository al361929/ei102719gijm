package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.model.Company;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/company")
public class CompanyController extends Controlador{

    private CompanyDao companyDao;

    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @RequestMapping("/list")
    public String listCompanies(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("companies", companyDao.getCompanies());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
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
        return "redirect:list?nuevo=" + company.getNif();
    }

    @RequestMapping(value = "/addRegister")
    public String addCompanyRegister(Model model) {
        model.addAttribute("company", new Company());
        return "company/addRegister";
    }

    @RequestMapping(value = "/addRegister", method = RequestMethod.POST)
    public String processAddSubmitRegister(@ModelAttribute("company") Company company, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "company/addRegister";
        companyDao.addCompany(company);
        return "redirect:login" + company.getNif();
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
        return "redirect:list?nuevo=" + company.getNif();
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
        if (user.getTipo()!="Company") return "error/sinPermiso";

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

   /* @RequestMapping(value = "/invoiceListComapny")
    public String getInvoiceCompanyList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("invoiceCompanyList", companyDao.getInvoiceCompany(user.getDni()));
        return gestionarAcceso(session, model, "Company", "company/invoiceListCompany");
    }*/

    @RequestMapping(value = "/serviceList")
    public String getServiceCompanyList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("serviceCompanyList", companyDao.getServiceList(user.getDni()));
        return gestionarAcceso(session, model, "Company", "company/serviceList");
    }


}
