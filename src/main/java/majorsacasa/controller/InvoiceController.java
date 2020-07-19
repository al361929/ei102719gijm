package majorsacasa.controller;

import majorsacasa.dao.*;
import majorsacasa.mail.MailBody;
import majorsacasa.mail.MailService;
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
public class InvoiceController extends ManageAccessController {

    private InvoiceDao invoiceDao;
    private ElderlyDao elderlyDao;
    private ProduceDao produceDao;
    private RequestDao requestDao;
    private ServiceDao serviceDao;
    private MailBody mailBody;
    private MailService mailService;
    private UserDao userDao;

    @Value("${upload.file.directory}")
    private String uploadDirectory;

    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao, ElderlyDao elderlyDao, ProduceDao produceDao, RequestDao requestDao, ServiceDao serviceDao, MailService mailService) {
        this.invoiceDao = invoiceDao;
        this.elderlyDao = elderlyDao;
        this.produceDao = produceDao;
        this.requestDao = requestDao;
        this.serviceDao = serviceDao;
        this.mailService = mailService;

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
        Elderly elderly = elderlyDao.getElderly(invoice.getDniElderly());
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());

        mailBody = new MailBody(elderlyDao.getElderly(invoice.getDniElderly()).getEmail());
        mailBody.addMail("Se ha generado la factura correspondiente a la petición de servicio: " + serviceDao.getService(requestDao.getRequest(produceDao.getProduceInvoice(invoice.getInvoiceNumber()).getIdRequest()).getIdService()).getDescription() + " y lo puede ver en su lista de facturas.");
        mailService.sendEmail(mailBody, user);

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
            return "invoice/update";
        }
        invoiceDao.updateInvoice(invoice);
        Elderly elderly = elderlyDao.getElderly(invoice.getDniElderly());
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());

        mailBody = new MailBody(elderlyDao.getElderly(invoice.getDniElderly()).getEmail());
        mailBody.updateMail("Se han actualizado los datos de su factura correctamente.");
        mailService.sendEmail(mailBody, user);

        return "redirect:list?nuevo=" + invoice.getInvoiceNumber();
    }

    @RequestMapping(value = "/generatePDF/{idInvoice}", method = RequestMethod.GET)
    public String generatePDF(HttpSession session, @PathVariable Integer idInvoice) {
        GeneratePDFController generatePDF = new GeneratePDFController();
        String path = uploadDirectory + "/invoice/" + idInvoice + ".pdf";
        Invoice invoice = invoiceDao.getInvoice(idInvoice);
        Elderly elderly = elderlyDao.getElderly(invoiceDao.getInvoice(idInvoice).getDniElderly());
        Request request = requestDao.getRequest(produceDao.getProduceInvoice(idInvoice).getIdRequest());
        Service service = serviceDao.getService(requestDao.getRequest(produceDao.getProduceInvoice(idInvoice).getIdRequest()).getIdService());
        generatePDF.createPDF(new File(path), invoice, elderly, request, service);
        invoiceDao.updloadInvoice(idInvoice, true);

        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
        mailBody = new MailBody(elderlyDao.getElderly(invoice.getDniElderly()).getEmail());
        mailBody.updateMail("Se ha generado la factura en PDF correspondiente al servicio: " + serviceDao.getService(requestDao.getRequest(produceDao.getProduceInvoice(invoice.getInvoiceNumber()).getIdRequest()).getIdService()).getDescription() + " y lo puede ver en su lista de facturas.");
        mailService.sendEmail(mailBody, user);

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

            Elderly elderly = elderlyDao.getElderly(invoice.getDniElderly());
            UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
            mailBody = new MailBody(elderlyDao.getElderly(invoice.getDniElderly()).getEmail());
            mailBody.updateMail("Se ha añadido el PDF de la factura correspondiente al servicio: " + serviceDao.getService(requestDao.getRequest(produceDao.getProduceInvoice(invoice.getInvoiceNumber()).getIdRequest()).getIdService()).getDescription() + " y lo puede ver en su lista de facturas.");
            mailService.sendEmail(mailBody, user);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:list?nuevo=" + invoice.getInvoiceNumber();
    }

    @RequestMapping(value = "/verPDF/{idInvoice}", method = RequestMethod.GET)
    public String seePDF(Model model, @PathVariable Integer idInvoice, HttpSession session) {
        String ruta = "/pdfs/invoice/" + idInvoice + ".pdf";

        model.addAttribute("filename", ruta);
        return gestionarAcceso(session, model, "ElderlyPeople", "verPDF");
    }

    @RequestMapping(value = "/delete/{invoiceNumber}")
    public String processDelete(@PathVariable Integer invoiceNumber) {

        Elderly elderly = elderlyDao.getElderly(invoiceDao.getInvoice(invoiceNumber).getDniElderly());
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
        mailBody = new MailBody(elderly.getEmail());
        mailBody.deleteMail("Se ha eliminado la factura correspondiente al servicio: " + serviceDao.getService(requestDao.getRequest(produceDao.getProduceInvoice(invoiceNumber).getIdRequest()).getIdService()).getDescription() + " y lo puede ver en su perfil.");
        mailService.sendEmail(mailBody, user);

        invoiceDao.deleteInvoice(invoiceNumber);

        return "redirect:../list";
    }

    @RequestMapping(value = "/invoiceListElderly")
    public String getInvoiceCompanyList(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("invoices", invoiceDao.getInvoiceElderly(user.getDni()));
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("info", invoiceDao.getDescriptionInvoice());
        return gestionarAcceso(session, model, "ElderlyPeople", "invoice/invoiceListElderly");
    }

}
