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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/request")
public class RequestController extends Controlador{
    private ContractDao contractDao;
    private ValoracionDao valoracionDao;
    private InvoiceDao invoiceDao;

    private ProduceDao produceDao;

    private RequestDao requestDao;
    private OffersDao offersDao;
    private ServiceDao serviceDao;
    private CompanyDao companyDao;
    private ElderlyDao elderlyDao;
    private List estados = Arrays.asList("Pendiente", "Aceptada", "Rechazada", "Cancelada");

    @Autowired
    public void setRequestDao(ProduceDao produceDao, InvoiceDao invoiceDao,ValoracionDao valoracionDao,RequestDao requestDao, ServiceDao serviceDao, CompanyDao companyDao, ElderlyDao elderlyDao,ContractDao contractDao,OffersDao offersDao) {
        this.requestDao = requestDao;
        this.serviceDao = serviceDao;
        this.companyDao = companyDao;
        this.elderlyDao = elderlyDao;
        this.contractDao = contractDao;
        this.offersDao= offersDao;
        this.valoracionDao =valoracionDao;
        this.invoiceDao = invoiceDao;
        this.produceDao =produceDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("requests", requestDao.getRequests());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "request/list";
    }

    @RequestMapping(value = "/add")
    public String addRequest(Model model) {
        List<Service> servicios = serviceDao.getServices();
        model.addAttribute("servicios", servicios);

        List<Elderly> elderly = elderlyDao.getElderlys();
        model.addAttribute("elderlys", elderly);

        List<Company> company = companyDao.getCompanies();
        model.addAttribute("companyies", company);

        model.addAttribute("request", new Request());

        return "request/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("request") Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "request/add";
        requestDao.addRequest(request);
        return "redirect:list?nuevo=" + request.getIdRequest();
    }
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public String infoRequest(Model model, @PathVariable int id) {
        Request request = requestDao.getRequest(id);
        model.addAttribute("request", request);
        HashMap<String ,String> u=valoracionDao.getUsersInfo();
        model.addAttribute("usuario",u);
        HashMap <Integer,String> servicios = requestDao.getMapServiceElderly();
        model.addAttribute("servicios", servicios);
        return "request/info";

    }
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable int id) {
        Request request = requestDao.getRequest(id);
        model.addAttribute("estados", estados);
        model.addAttribute("request", request);
        List<Company> companies = companyDao.getCompanyServiceOffer(request.getIdService());
        System.out.println(companies.toString());
        model.addAttribute("companyies", companies);
        return "request/update";

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request,Model model,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "request/update";
        }

        if (request.getState().equals("Aceptada")) {
            request.setDateAccept(LocalDate.now());
            Invoice factura = new Invoice();
            factura.setDateInvoice(LocalDate.now());
            factura.setInvoicePDF(false);
            Service service = serviceDao.getService(request.getIdService());
            factura.setDniElderly(request.getDni());
            Integer precioTotal = service.getPrice() * requestDao.getRequest(request.getIdRequest()).getNumDias();
            factura.setTotalPrice(precioTotal);
            invoiceDao.addInvoice(factura);
            Produce p = new Produce();
            p.setIdInvoice(invoiceDao.getUltimoInvoice());
            p.setIdRequest(request.getIdRequest());
            produceDao.addProduce(p);

        }else{
            if(request.getState().equals("Rechazada")) request.setDateReject(LocalDate.now());

        }
        List<Company> company = companyDao.getCompanies();
        model.addAttribute("companyies", company);

        HashMap<String ,String> u=valoracionDao.getUsersInfo();
        model.addAttribute("usuario",u);

        requestDao.updateRequest(request);
        //return "redirect:list?nuevo=" + request.getIdRequest();
        return "redirect:../request/list?nuevo=" + request.getDni();

    }

    @RequestMapping(value = "/list/{dni}")
    public String listRequestsPersonalizado(@PathVariable String dni,Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("requests", requestDao.getRequestsElderly(dni));
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String ,String> u=valoracionDao.getUsersInfo();
        model.addAttribute("usuario",u);
        HashMap <Integer,String> servicios = requestDao.getMapServiceElderly();
        model.addAttribute("servicios", servicios);
        return "request/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String processDelete(@PathVariable int id) {
        requestDao.deleteRequest(id);
        return "redirect:../list";
    }
    @RequestMapping("/listElderly")
    public String listRequestsElderly(HttpSession session,Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        model.addAttribute("requests", requestDao.getRequestsElderly(user.getDni()));
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String ,String> u=valoracionDao.getUsersInfo();
        model.addAttribute("usuario",u);
        HashMap <Integer,String> servicios = requestDao.getMapServiceElderly();
        model.addAttribute("servicios", servicios);
        return gestionarAcceso(session,model,"ElderlyPeople","request/listRequestElderly");

    }
    @RequestMapping(value = "/addRequestElderly")
    public String addRequestElderly(Model model) {
        model.addAttribute("request", new Request());
        List<Service> servicios = serviceDao.getServices();
        model.addAttribute("servicios", servicios);

        List<Company> company = companyDao.getCompanies();
        model.addAttribute("companyies", company);

        return "request/addRequestElderly";
    }

    @RequestMapping(value = "/addRequestElderly", method = RequestMethod.POST)
    public String processAddSubmitRequestElderly(HttpSession session,@ModelAttribute("request") Request request,Model model, BindingResult bindingResult) {
        Service servicio = serviceDao.getService(request.getIdService());
        UserDetails user = (UserDetails) session.getAttribute("user");
        request.setNif("0");
        if (requestDao.checkService(servicio.getServiceType(), user.getDni())) {
            bindingResult.rejectValue("idService", "badserv", " Ya has solicitado este servicio");
            List<Service> servicios = serviceDao.getServices();
            model.addAttribute("servicios", servicios);
            List<Company> company = companyDao.getCompanies();
            model.addAttribute("companyies", company);

            return "request/addRequestElderly";
        }
        /*if (!request.getNif().equals("0") && offersDao.checkService(request.getNif(),request.getIdService())){
            bindingResult.rejectValue("nif", "badOffer", " Esta empresa no ofrece ese servicio.");
            List<Service> servicios = serviceDao.getServices();
            model.addAttribute("servicios", servicios);
            List<Company> company = companyDao.getCompanies();
            model.addAttribute("companyies", company);
            return "request/addRequestElderly";
        }*/

        if (bindingResult.hasErrors()) {
            System.out.println("error");
            return "request/addRequestElderly";
        }
        request.setDni(user.getDni());

        requestDao.addRequest(request);

        int id = requestDao.ultimoIdRequest();

        System.out.println("Dias: "+request.getDias());

        return "redirect:/request/listElderly?nuevo=" + id;
      //  return "redirect:listElderly?nuevo="+id;

    }
    @RequestMapping(value = "/cancelarRequest/{id}")
    public String processUpdateEstadp(@PathVariable int id) {
        requestDao.updateEstado(id,"Cancelada");
        return "redirect:../listElderly?nuevo="+id;
    }

}
