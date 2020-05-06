package majorsacasa.controller;

import majorsacasa.dao.*;
import majorsacasa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/invoice")
public class InvoiceController extends Controlador {

    private InvoiceDao invoiceDao;
    private ElderlyDao elderlyDao;
    private ProduceDao produceDao;
    private RequestDao requestDao;
    private ServiceDao serviceDao;
    private ContractDao contractDao;

    @Value("${upload.file.directory}")
    private String uploadDirectory;

    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao, ElderlyDao elderlyDao, ProduceDao produceDao, RequestDao requestDao, ServiceDao serviceDao, ContractDao contractDao) {
        this.invoiceDao = invoiceDao;
        this.elderlyDao = elderlyDao;
        this.produceDao = produceDao;
        this.requestDao = requestDao;
        this.serviceDao = serviceDao;
        this.contractDao = contractDao;

    }

    @RequestMapping("/list")
    public String listInvoices(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("invoices", invoiceDao.getInvoices());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "invoice/list";
    }

    @RequestMapping(value = "/add")
    public String addInvoice(Model model) {
        model.addAttribute("invoice", new Invoice());
        List<Elderly> elderly = elderlyDao.getElderlys();
        model.addAttribute("elderlys", elderly);

        return "invoice/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "invoice/add";
        }
        invoiceDao.addInvoice(invoice);
        return "redirect:list?nuevo=" + invoice.getInvoiceNumber();
    }

    @RequestMapping(value = "/update/{idInvoice}", method = RequestMethod.GET)
    public String editInvoice(Model model, @PathVariable Integer idInvoice) {
        model.addAttribute("invoice", invoiceDao.getInvoice(idInvoice));
        return "invoice/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("invoice") Invoice invoice,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(invoice.toString());
            return "invoice/update";
        }
        invoiceDao.updateInvoice(invoice);
        return "redirect:list?nuevo=" + invoice.getInvoiceNumber();
    }

    @RequestMapping(value = "/generatePDF/{idInvoice}", method = RequestMethod.GET)
    public String generatePDF(HttpSession session, @PathVariable Integer idInvoice) {
        GeneratePDFController generatePDF = new GeneratePDFController();
        String path = uploadDirectory + "/invoice/" + idInvoice + ".pdf";
        Invoice invoice = invoiceDao.getInvoice(idInvoice);
        Elderly elderly = elderlyDao.getElderly(invoiceDao.getInvoice(idInvoice).getDniElderly());
        Request request = requestDao.getRequest(produceDao.getProduce(idInvoice).getIdRequest());
        Service service = serviceDao.getService(requestDao.getRequest(produceDao.getProduce(idInvoice).getIdRequest()).getIdService());
        generatePDF.createPDF(new File(path), invoice, elderly, request, service);
        invoiceDao.updloadInvoice(idInvoice, true);
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user.getTipo().equals("ElderlyPeople")) return "redirect:../invoiceListElderly?nuevo=" + idInvoice;

        return "redirect:../list?nuevo=" + idInvoice;
    }

    @RequestMapping(value = "/upload/{idInvoice}", method = RequestMethod.GET)
    public String prepareUploadInvoice(Model model, @PathVariable Integer idInvoice) {
        model.addAttribute("invoice", invoiceDao.getInvoice(idInvoice));
        return "invoice/upload";
    }



    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @ModelAttribute("invoice") Invoice invoice) {
        if (file.isEmpty()) {
            return "invoice/upload";
        }
        try {
            // Obtener el fichero y guardarlo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "/invoice/" + invoice.getInvoiceNumber() + ".pdf");
            Files.write(path, bytes);
            invoiceDao.updloadInvoice(invoice.getInvoiceNumber(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:list?nuevo=" + invoice.getInvoiceNumber();
    }

    @RequestMapping(value = "/verPDF/{idInvoice}", method = RequestMethod.GET)
    public String seePDF(Model model, @PathVariable Integer idInvoice) {
        String ruta = "/pdfs/invoice/" + idInvoice + ".pdf";
        model.addAttribute("filename", ruta);
        return "verPDF";
    }

    @RequestMapping(value = "/delete/{invoiceNumber}")
    public String processDelete(@PathVariable Integer invoiceNumber) {
        invoiceDao.deleteInvoice(invoiceNumber);
        return "redirect:../list";
    }

    @RequestMapping(value = "/invoiceListElderly")
    public String getInvoiceCompanyList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("invoices", invoiceDao.getInvoiceElderly(user.getDni()));
        return gestionarAcceso(session, model, "ElderlyPeople", "invoice/invoiceListElderly");
    }

}
