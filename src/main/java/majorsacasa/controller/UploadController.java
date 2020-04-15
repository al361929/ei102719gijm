package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.dao.ContractDao;
import majorsacasa.dao.ElderlyDao;
import majorsacasa.dao.InvoiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/contract/upload/{idContract}", method = RequestMethod.GET)
    public String prepareUploadContract(Model model, @PathVariable Integer idContract) {
        model.addAttribute("contrato", contractDao.getContract(idContract));
        return "contract/upload";
    }

    @RequestMapping(value = "/invoice/upload/{invoiceNumber}", method = RequestMethod.GET)
    public String prepareUploadInvoice(Model model, @PathVariable Integer invoiceNumber) {
        model.addAttribute("idContract", invoiceDao.getInvoice(invoiceNumber));
        return "invoice/upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            // Enviar mensaje de error porque no hay fichero seleccionado
            redirectAttributes.addFlashAttribute("message",
                    "Please select a file to upload");
            return "redirect:contract/uploadStatus";
        }

        try {
            // Obtener el fichero y guardarlo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "/contract/"
                    + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + path + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:contract/list";
    }

}
