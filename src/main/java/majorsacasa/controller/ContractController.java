package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.dao.ContractDao;
import majorsacasa.dao.ElderlyDao;
import majorsacasa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/contract")
public class ContractController extends Controlador {

    private ContractDao contractDao;
    private CompanyDao companyDao;
    private ElderlyDao elderlyDao;

    @Value("${upload.file.directory}")
    private String uploadDirectory;

    @Autowired
    public void setContractDao(ContractDao contractDao, CompanyDao companyDao, ElderlyDao elderlyDao) {
        this.contractDao = contractDao;
        this.companyDao = companyDao;
        this.elderlyDao = elderlyDao;
    }


    @RequestMapping("/list")
    public String listContracts(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("contracts", contractDao.getContracts());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "contract/list";
    }

    @RequestMapping(value = "/add")
    public String addContract(Model model) {

        model.addAttribute("contract", new Contract());

        List<Elderly> elderly = elderlyDao.getElderlys();
        model.addAttribute("elderlys", elderly);

        List<Company> company = companyDao.getCompanies();
        model.addAttribute("companyies", company);


        return "contract/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("contract") Contract contract, BindingResult bindingResult) {
       /* Boolean checkCompany = companyDao.checkCompany(contract.getNifcompany());
        if (!checkCompany) {
            bindingResult.rejectValue("nifcompany", "badnif", "No existe la empresa");
            return "contract/add";
        }
        Boolean checkElderly = elderlyDao.checkElderly(contract.getDnielderly());
        if (!checkElderly) {
            bindingResult.rejectValue("dnielderly", "baddni", "No existe la persona mayor");
            return "contract/add";
        }*/
        System.out.println(contract.toString());
        contractDao.addContract(contract);
        return "redirect:list?nuevo=" + contract.getIdContract();
    }

    @RequestMapping(value = "/update/{idContract}", method = RequestMethod.GET)
    public String editContract(Model model, @PathVariable Integer idContract) {
        model.addAttribute("contract", contractDao.getContract(idContract));
        return "contract/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("contract") Contract contract,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "contract/update";
        contractDao.updateContract(contract);
        return "redirect:list?nuevo=" + contract.getIdContract();
    }

    @RequestMapping(value = "/upload/{idContract}", method = RequestMethod.GET)
    public String prepareUploadContract(Model model, @PathVariable Integer idContract) {
        model.addAttribute("contrato", contractDao.getContract(idContract));
        return "contract/upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @ModelAttribute("contrato") Contract contract) {
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
        return "redirect:list?nuevo=" + contract.getIdContract();
    }

    @RequestMapping(value = "/verPDF/{idContract}", method = RequestMethod.GET)
    public String seePDF(Model model, @PathVariable Integer idContract) {
        model.addAttribute("contrato", idContract);
        String ruta = uploadDirectory + "/contract/" + idContract + ".pdf";
        model.addAttribute("filename", ruta);
        return "contract/verPDF";
    }

    @RequestMapping(value = "/delete/{idContract}")
    public String processDelete(@PathVariable Integer idContract) {
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
