package majorsacasa.controller;

import majorsacasa.dao.*;
import majorsacasa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/company")
public class CompanyController extends ManageAccessController {

    private CompanyDao companyDao;
    private ValoracionDao valoracionDao;
    private ServiceDao serviceDao;
    private MailController mailController;
    static String mensajeError = "";
    private RequestDao requestdao;
    private ElderlyDao elderlyDao;

    @Autowired
    public void setCompanyDao(ValoracionDao valoracionDao, CompanyDao companyDao, ServiceDao serviceDao, RequestDao requestDao, ElderlyDao elderlyDao) {
        this.companyDao = companyDao;
        this.valoracionDao = valoracionDao;
        this.serviceDao = serviceDao;
        this.requestdao = requestDao;
        this.elderlyDao = elderlyDao;
    }

    @RequestMapping("/list")
    public String listCompanies(Model model, @RequestParam("nuevo") Optional<String> nuevo, HttpSession session) {
        model.addAttribute("companies", companyDao.getCompanies());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("mensaje", mensajeError);
        mensajeError="";
        model.addAttribute("mapa", serviceDao.getMapServiceCompany());
        return gestionarAcceso(session, model, "Admin", "company/list");
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

        mailController = new MailController(company.getEmail());
        mailController.addMail("El CAS ha registrado su cuenta correctamente.\n" +
                "El usuario y contraseña con el que puede acceder son:\n" +
                "Usuario: " + company.getNombreUsuario() +
                "\nContraseña: " + company.getPassword());

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
        Boolean check = companyDao.checkDNI(company.getNif());
        Boolean checkUser = companyDao.checkUser(company.getNombreUsuario());

        if (!check) {

            bindingResult.rejectValue("nif", "nif", "Ya existe una empresa con este NIF");

            return "company/addRegister";
        }
        if (!checkUser) {

            bindingResult.rejectValue("nombreUsuario", "nombreUsuario", company.getNombreUsuario() + " ya esta se esta utilizando");

            return "company/addRegister";
        }
        companyDao.addCompany(company);

        mailController = new MailController(company.getEmail());
        mailController.addMail("El CAS ha registrado su cuenta correctamente.\n" +
                "El usuario y contraseña con el que puede acceder son:\n" +
                "Usuario: " + company.getNombreUsuario() +
                "\nContraseña: " + company.getPassword());

        return "redirect:list?nuevo=" + company.getNif();
    }

    @RequestMapping(value = "/addContract")
    public String addCompanyContract(Model model) {
        model.addAttribute("company", new Company());
        return "company/addContract";
    }

    @RequestMapping(value = "/addContract", method = RequestMethod.POST)
    public String processAddSubmitContract(@ModelAttribute("company") Company company, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "company/addContract";
        Boolean check = companyDao.checkDNI(company.getNif());
        Boolean checkUser = companyDao.checkUser(company.getNombreUsuario());

        if (!check) {

            bindingResult.rejectValue("nif", "nif", "Ya existe una empresa con este NIF");

            return "company/addContract";
        }
        if (!checkUser) {

            bindingResult.rejectValue("nombreUsuario", "nombreUsuario", company.getNombreUsuario() + " ya esta se esta utilizando");

            return "company/addContract";
        }
        companyDao.addCompany(company);

        mailController = new MailController(company.getEmail());
        mailController.addMail("El CAS ha registrado su cuenta correctamente.\n" +
                "El usuario y contraseña con el que puede acceder son:\n" +
                "Usuario: " + company.getNombreUsuario() +
                "\nContraseña: " + company.getPassword());

        return "redirect:../offer/addService/" + company.getNif();
    }

    @RequestMapping(value = "/update/{nif}", method = RequestMethod.GET)
    public String editCompany(Model model, @PathVariable String nif, HttpSession session) {
        model.addAttribute("company", companyDao.getCompany(nif));
        return gestionarAcceso(session, model, "Admin", "company/update");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("company") Company company,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "company/update";
        companyDao.updateCompanySINpw(company);

        mailController = new MailController(company.getEmail());
        mailController.updateMail("Se han actualizado los datos de su cuenta correctamente.");

        return "redirect:list?nuevo=" + company.getNif();
    }

    @RequestMapping(value = "/confirmarDelete")
    public String confirmarDelete(HttpSession httpSession, Model model) {
        UserDetails usuario = (UserDetails) httpSession.getAttribute("user");
        model.addAttribute("user", usuario);
        model.addAttribute("userType", usuario.getTipo().toLowerCase());

        return gestionarAcceso(httpSession, model, "Company", "deletePerfil");
    }

    private Boolean comprobarContratos(String nif) {
        List<Contract> contratos = companyDao.getContractsList(nif);
        for (Contract contrato : contratos) {
            if (contrato.getDateDown().isAfter(LocalDate.now())) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/delete/{nif}")
    public String processDelete(@PathVariable String nif, Model model) {
        if (comprobarContratos(nif)) {
            mailController = new MailController(companyDao.getCompany(nif).getEmail());
            companyDao.deleteCompany(nif);
            mailController.deleteMail("Se ha eliminado su cuenta permanentemente.");
            return "redirect:/logout";
        } else {
            mensajeError = "No puedes borrar una empresa si tiene contratos";
        }
        return "redirect:../perfil";
    }

    @RequestMapping(value = "/perfil")
    public String getPerfil(HttpSession session, Model model) {
        String destino = sesionAbierta(session, model, "company/perfil");
        if (destino != null) return destino;

        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user.getTipo() != "Company") return "error/sinPermiso";
        model.addAttribute("company", companyDao.getCompany(user.getDni()));

        model.addAttribute("mensaje", mensajeError);
        mensajeError = " ";
        return gestionarAcceso(session, model, "Company", "company/perfil");
    }

    @RequestMapping(value = "/updatePerfil", method = RequestMethod.POST)
    public String processUpdatePerfilSubmit(@ModelAttribute("company") Company company,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "company/perfil";
        companyDao.updateCompany(company);

        mailController = new MailController(company.getEmail());
        mailController.updateMail("Se han actualizado los datos de su cuenta correctamente.");

        return "redirect:/company/contractList";
    }

    @RequestMapping(value = "/contractList")
    public String getContractList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("contracts", companyDao.getContractsList(user.getDni()));
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        return gestionarAcceso(session, model, "Company", "company/contractList");
    }

    @RequestMapping(value = "/contractListCompany/{nif}")
    public String getContractListP(HttpSession session, Model model, @PathVariable String nif) {
        model.addAttribute("contracts", companyDao.getContractsList(nif));
        model.addAttribute("usuario", valoracionDao.getUsersInfo());
        return "company/contractList";
    }

    @RequestMapping(value = "/serviceElderly/{idService}")
    public String serviceElderly(Model model, HttpSession httpSession, @PathVariable Integer idService) {
        UserDetails user = (UserDetails) httpSession.getAttribute("user");
        List<Request> peticiones = requestdao.getRequestsCompany(user.getDni(), idService);
        List<Elderly> personas = new ArrayList<>();
        Map<String, String> mapRequestDias = new HashMap<>();
        for (Request req : peticiones) {
            personas.add(elderlyDao.getElderly(req.getDni()));
            mapRequestDias.put(req.getDni(), req.getDias());
        }
        model.addAttribute("mapRequestDias", mapRequestDias);
        model.addAttribute("personas", personas);
        model.addAttribute("servicio", serviceDao.getService(idService));
        return gestionarAcceso(httpSession, model, "Company", "service/detailsElderly");
    }
}
