package majorsacasa.controller;

import majorsacasa.dao.InvoiceDao;
import majorsacasa.model.Invoice;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;


@Controller
@RequestMapping("/invoice")
public class InvoiceController extends Controlador{

    private InvoiceDao invoiceDao;

    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
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

    @RequestMapping(value = "/delete/{invoiceElderly}")
    public String processDelete(@PathVariable Integer invoiceNumber) {
        invoiceDao.deleteInvoice(invoiceNumber);
        return "redirect:../list";
    }
    @RequestMapping(value = "/invoiceListElderly")
    public String getInvoiceCompanyList(HttpSession session, Model model) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("invoiceCompanyList", invoiceDao.getInvoiceElderly(user.getDni()));
        return gestionarAcceso(session, model, "ElderlyPeople", "invoice/invoiceListElderly");
    }

}
