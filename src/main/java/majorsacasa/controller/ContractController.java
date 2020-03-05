package majorsacasa.controller;

import majorsacasa.dao.ContractDao;
import majorsacasa.model.Contract;
import majorsacasa.model.Volunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/contract")
public class ContractController {

    private ContractDao contractDao;

    @Autowired
    public void setContractDao(ContractDao contractDao) {
        this.contractDao = contractDao;
    }

    @RequestMapping("/list")
    public String listContracts(Model model) {
        model.addAttribute("contracts", contractDao.getContracts());
        return "contract/list";
    }

    @RequestMapping(value = "/add")
    public String addContract(Model model) {
        model.addAttribute("contract", new Contract());
        return "contract/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("volunteer") Contract contract, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "contract/add";
        contractDao.addContract(contract);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{nif}", method = RequestMethod.GET)
    public String editContract(Model model, @PathVariable String nif) {
        model.addAttribute("contract", contractDao.getContract(nif));
        return "contract/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("contract") Contract contract,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "contract/update";
        contractDao.updateContract(contract);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{nif}")
    public String processDelete(@PathVariable String nif) {
        contractDao.deleteContract(nif);
        return "redirect:../list";
    }
}
