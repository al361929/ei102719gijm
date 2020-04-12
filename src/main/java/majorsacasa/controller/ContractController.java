package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.dao.ContractDao;
import majorsacasa.model.Contract;
import majorsacasa.model.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/contract")
public class ContractController {

    private ContractDao contractDao;
    private CompanyDao companyDao;

    @Autowired
    public void setContractDao(ContractDao contractDao, CompanyDao companyDao) {
        this.contractDao = contractDao;
        this.companyDao = companyDao;
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
        return "contract/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("contract") Contract contract, BindingResult bindingResult) {
        Boolean check = companyDao.checkCompany(contract.getNifcompany());
        System.out.println(check);
        if (!check) {
            bindingResult.rejectValue("nifcompany", "badnif", "No existe la empresa");
            return "contract/add";
        }
        if (bindingResult.hasErrors())
            return "contract/add";
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

    @RequestMapping(value = "/delete/{idContract}")
    public String processDelete(@PathVariable Integer idContract) {
        contractDao.deleteContract(idContract);
        return "redirect:../list";
    }

}
