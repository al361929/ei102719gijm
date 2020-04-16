package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.dao.ContractDao;
import majorsacasa.dao.ElderlyDao;
import majorsacasa.dao.InvoiceDao;
import majorsacasa.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    // Inyectamos el valor configurado en application.properties a la variable
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    private ContractDao contractDao;
    private InvoiceDao invoiceDao;

    @Autowired
    public void setContractDao(ContractDao contractDao, InvoiceDao invoiceDao) {
        this.contractDao = contractDao;
        this.invoiceDao = invoiceDao;
    }

    /*@RequestMapping(value = "/contract/upload/{idContract}", method = RequestMethod.GET)
    public String prepareUploadContract(Model model, @PathVariable Integer idContract) {
        model.addAttribute("contrato", contractDao.getContract(idContract));
        return "contract/upload";
    }*/

    @RequestMapping(value = "/invoice/upload/{invoiceNumber}", method = RequestMethod.GET)
    public String prepareUploadInvoice(Model model, @PathVariable Integer invoiceNumber) {
        model.addAttribute("idContract", invoiceDao.getInvoice(invoiceNumber));
        return "invoice/upload";
    }

    /*@RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @ModelAttribute("contract") Contract contract) {
        if (file.isEmpty()) {
            return "redirect:contract/upload";
        }
        try {
            // Obtener el fichero y guardarlo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "/contract/" + contract.getIdContract() + ".pdf");
            Files.write(path, bytes);
            contractDao.getContract(contract.getIdContract()).setContractPDF(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:contract/list";
    }*/

}
