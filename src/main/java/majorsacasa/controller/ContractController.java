package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.dao.ContractDao;
import majorsacasa.dao.ElderlyDao;
import majorsacasa.dao.ValoracionDao;
import majorsacasa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.reverse;

@Controller
@RequestMapping("/contract")
public class ContractController extends ManageAccessController {

    private ContractDao contractDao;
    private CompanyDao companyDao;
    private ElderlyDao elderlyDao;
    private ValoracionDao valoracionDao;
    private MailController mailController;


    @Value("${upload.file.directory}")
    private String uploadDirectory;

    @Autowired
    public void setContractDao(ContractDao contractDao, CompanyDao companyDao, ElderlyDao elderlyDao, ValoracionDao valoracionDao) {
        this.contractDao = contractDao;
        this.companyDao = companyDao;
        this.elderlyDao = elderlyDao;
        this.valoracionDao = valoracionDao;
    }


    @RequestMapping("/list")
    public String listContracts(Model model, @RequestParam("nuevo") Optional<String> nuevo, HttpSession session) {
        model.addAttribute("contracts", contractDao.getContracts());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("usuarios", valoracionDao.getUsersInfo());
        model.addAttribute("nuevo", newVolunteerTime);
        return gestionarAcceso(session, model, "Admin", "contract/list");
    }

    @RequestMapping(value = "/add")
    public String addContract(Model model, HttpSession session) {

        model.addAttribute("contract", new Contract());

        List<Elderly> elderly = elderlyDao.getElderlys();
        model.addAttribute("elderlys", elderly);

        List<Company> company = companyDao.getCompanies();
        reverse(company);
        model.addAttribute("companyies", company);

        return gestionarAcceso(session, model, "Admin", "contract/add");
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("contract") Contract contract, BindingResult bindingResult) {
        contractDao.addContract(contract);
        int id = contractDao.getUltimoContrato();

        mailController = new MailController(companyDao.getCompany(contract.getNifcompany()).getEmail());
        mailController.updateMail("El CAS ha añadido su contrato correctamente y lo puede ver en su perfil");

        return "redirect:list?nuevo=" + id;
    }

    @RequestMapping(value = "/update/{idContract}", method = RequestMethod.GET)
    public String editContract(Model model, @PathVariable Integer idContract, HttpSession session) {
        model.addAttribute("contract", contractDao.getContract(idContract));
        return gestionarAcceso(session, model, "Admin", "contract/update");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("contract") Contract contract,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "contract/update";
        contractDao.updateContract(contract);

        mailController = new MailController(companyDao.getCompany(contract.getNifcompany()).getEmail());
        mailController.addMail("Se han actualizado los datos de su contrato correctamente");

        return "redirect:list?nuevo=" + contract.getIdContract();
    }

    @RequestMapping(value = "/upload/{idContract}", method = RequestMethod.GET)
    public String prepareUploadContract(Model model, @PathVariable Integer idContract) {
        model.addAttribute("contrato", contractDao.getContract(idContract));
        return "contract/upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(HttpSession session, @RequestParam("file") MultipartFile file, @ModelAttribute("contrato") Contract contract) {
        if (file.isEmpty()) {
            return "contract/upload";
        }
        try {
            // Obtener el fichero y guardarlo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "/contract/" + contract.getIdContract() + ".pdf");
            Files.write(path, bytes);
            contractDao.uploadPDF(contract.getIdContract(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user.getTipo().equals("Company")) {
            return "redirect:../company/contractList?nuevo=" + contract.getIdContract();
        }

        mailController = new MailController(companyDao.getCompany(contract.getNifcompany()).getEmail());
        mailController.addMail("Se ha añadido su contrato en versión PDF. Lo puede ver en la lista de contratos de su cuenta");

        return "redirect:list?nuevo=" + contract.getIdContract();
    }

    @RequestMapping(value = "/verPDF/{idContract}", method = RequestMethod.GET)
    public String seePDF(Model model, @PathVariable Integer idContract, HttpSession session) {
        String ruta = "/pdfs/contract/" + idContract + ".pdf";
        model.addAttribute("filename", ruta);
        return gestionarAcceso(session, model, "Company", "verPDF");
    }

    @RequestMapping(value = "/delete/{idContract}")
    public String processDelete(@PathVariable Integer idContract) {
        mailController = new MailController(companyDao.getCompany(contractDao.getContract(idContract).getNifcompany()).getEmail());
        mailController.deleteMail("Se ha eliminado su cuenta permanentemente");
        contractDao.deleteContract(idContract);
        return "redirect:../list";
    }

    @RequestMapping(value = "/contractElderlyList")
    public String getElderlyContractList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("elderlyListC", contractDao.getElderlyList(user.getDni()));
        return gestionarAcceso(session, model, "ElderlyPeople", "contract/contractElderlyList");
    }
}