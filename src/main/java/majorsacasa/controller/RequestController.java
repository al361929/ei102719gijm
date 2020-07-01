package majorsacasa.controller;

import majorsacasa.dao.*;
import majorsacasa.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/request")
public class RequestController extends ManageAccessController {
    private ContractDao contractDao;
    private ValoracionDao valoracionDao;
    private InvoiceDao invoiceDao;
    private ProduceDao produceDao;
    private MailController mailController;
    private RequestDao requestDao;
    private OffersDao offersDao;
    private ServiceDao serviceDao;
    private CompanyDao companyDao;
    private ElderlyDao elderlyDao;
    private final List estados = Arrays.asList("Pendiente", "Aceptada", "Rechazada", "Cancelada");
    static String mensajeError = "";

    @Autowired
    public void setRequestDao(ProduceDao produceDao, InvoiceDao invoiceDao, ValoracionDao valoracionDao, RequestDao requestDao, ServiceDao serviceDao, CompanyDao companyDao, ElderlyDao elderlyDao, ContractDao contractDao, OffersDao offersDao) {
        this.requestDao = requestDao;
        this.serviceDao = serviceDao;
        this.companyDao = companyDao;
        this.elderlyDao = elderlyDao;
        this.contractDao = contractDao;
        this.offersDao = offersDao;
        this.valoracionDao = valoracionDao;
        this.invoiceDao = invoiceDao;
        this.produceDao = produceDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("requests", requestDao.getRequests());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("mensaje", mensajeError);
        mensajeError = "";
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

        mailController = new MailController(elderlyDao.getElderly(request.getDni()).getEmail());
        mailController = new MailController(elderlyDao.getElderly(request.getDni()).getEmail());
        mailController.addMail("La solicitud correspondiente al servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación.");

        return "redirect:list?nuevo=" + request.getIdRequest();
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public String infoRequest(Model model, @PathVariable int id, HttpSession session) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        Request request = requestDao.getRequest(id);
        model.addAttribute("request", request);
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        HashMap<Integer, String> servicios = requestDao.getMapServiceElderly();
        model.addAttribute("servicios", servicios);
        if (user.getUsername().equals("casCommitee")) {
            return gestionarAcceso(session, model, "Admin", "request/info");
        }
        return gestionarAcceso(session, model, "ElderlyPeople", "request/info");
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable int id) {
        Request request = requestDao.getRequest(id);
        model.addAttribute("estados", estados);
        model.addAttribute("request", request);
        List<Company> companies = companyDao.getCompanyServiceOffer(request.getIdService());
        model.addAttribute("companyies", companies);
        return "request/update";

    }

    public int calculateNumDias(LocalDate fechaIni, LocalDate fechaFin, ArrayList<String> diasRequest) {
        int total = 0;
        DayOfWeek diaSemana;
        LocalDate fecha = fechaIni;

        while (fecha.isBefore(fechaFin)) {
            diaSemana = fecha.getDayOfWeek();
            if (diasRequest.contains(diaSemana.toString())) {
                total += 1;
            }
            fecha = fecha.plusDays(1);
        }
        return total;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("request") Request request, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "request/update";
        }

        if (request.getState().equals("Aceptada")) {
            // MODIFICAMOS LA REQUEST
            request.setDateAccept(LocalDate.now());
            request.setDias(requestDao.getRequest(request.getIdRequest()).getDias());
            Service service = serviceDao.getService(request.getIdService());

            //SE CREA LA FACTURA Y SE ASIGNAN/CALCULAN TODOS LOS DATOS
            Invoice factura = new Invoice();
            factura.setDateInvoice(LocalDate.now());
            factura.setInvoicePDF(false);
            factura.setDniElderly(request.getDni());
            int numDias = calculateNumDias(request.getDateStart(), request.getDateEnd(), request.getDiasIngles());
            Integer precioTotal = service.getPrice() * numDias;
            factura.setTotalPrice(precioTotal);
            invoiceDao.addInvoice(factura);
            Produce p = new Produce();
            p.setIdInvoice(invoiceDao.getUltimoInvoice());
            p.setIdRequest(request.getIdRequest());
            produceDao.addProduce(p);

            // SE LE COMUNICA A LA PERSONA MAYOR DE SU SOLICITUD
            Elderly elderly = elderlyDao.getElderly(request.getDni());
            mailController = new MailController(elderly.getEmail());
            mailController.updateMail("Su solicitud del servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " ha sido " + request.getState() + " y lo puede ver en su lista de solicitudes. Por favor, no olvide valorar el servicio.");

            // SE LE COMUNICA A LA EMPRESA QUE SE LE HA ASIGNADO UNA PERSONA MAYOR
            HashMap<String, String> mapaServiciosCompany = serviceDao.getMapServiceCompany();
            Company empresa = companyDao.getCompany(request.getNif());
            mailController = new MailController(companyDao.getCompany(request.getNif()).getEmail());
            mailController.updateMail("Se le ha asignado una persona mayor a su servicio de " + mapaServiciosCompany.get(empresa.getNif()) +
                    ". Puede ver los detalles en el listado de personas mayores de su servicio.\n" +
                    "La persona mayor se llama: " + elderly.getNombre() + " " + elderly.getApellidos() + " y su dirección es: " + elderly.getDireccion());
        } else {
            if (request.getState().equals("Rechazada")) request.setDateReject(LocalDate.now());

            mailController = new MailController(elderlyDao.getElderly(request.getDni()).getEmail());
            mailController.updateMail("Su solicitud del servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " ha sido " + request.getState() + " y lo puede ver en su lista de solicitudes.");

        }
        List<Company> company = companyDao.getCompanies();
        model.addAttribute("companyies", company);

        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);

        requestDao.updateRequest(request);
        return "redirect:../request/list/" + request.getDni();

    }

    // LISTA DEL casCommitee
    @RequestMapping(value = "/list/{dni}")
    public String listRequestsPersonalizado(@PathVariable String dni, Model model, @RequestParam("nuevo") Optional<String> nuevo, HttpSession session) {
        model.addAttribute("requests", requestDao.getRequestsElderly(dni));
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        HashMap<Integer, String> servicios = requestDao.getMapServiceElderly();
        model.addAttribute("servicios", servicios);
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        model.addAttribute("mensaje", mensajeError);
        mensajeError = "";
        return gestionarAcceso(session, model, "Admin", "request/list");
    }

    @RequestMapping(value = "/delete/{idRequest}")
    public String processDelete(@PathVariable int idRequest) {
        Request request = requestDao.getRequest(idRequest);
        if (request.getState().equals("Rechazada") || request.getState().equals("Cancelada")) {
            mailController = new MailController(elderlyDao.getElderly(requestDao.getRequest(idRequest).getDni()).getEmail());
            mailController.deleteMail("Se ha eliminado la solicitud correspondiente al servicio: " + serviceDao.getService(requestDao.getRequest(idRequest).getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación");
            requestDao.deleteRequest(idRequest);
        } else {
            mensajeError = "No puedes borrar una petición aceptada o pendiente";
        }
        return "redirect:../list/" + request.getDni();
    }

    // LISTA DEL ELDERLY PEOPLE
    @RequestMapping("/listElderly")
    public String listRequestsElderly(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        model.addAttribute("requests", requestDao.getRequestsElderly(user.getDni()));
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);
        HashMap<Integer, String> servicios = requestDao.getMapServiceElderly();
        model.addAttribute("servicios", servicios);

        return gestionarAcceso(session, model, "ElderlyPeople", "request/listRequestElderly");

    }

    @RequestMapping(value = "/addRequestElderly")
    public String addRequestElderly(Model model, HttpSession session) {
        model.addAttribute("request", new Request());
        List<Service> servicios = serviceDao.getServices();
        model.addAttribute("servicios", servicios);

        List<Company> company = companyDao.getCompanies();
        model.addAttribute("companyies", company);

        model.addAttribute("mensaje", mensajeError);
        mensajeError = "";
        return gestionarAcceso(session, model, "ElderlyPeople", "request/addRequestElderly");
    }

    @RequestMapping(value = "/addRequestElderly", method = RequestMethod.POST)
    public String processAddSubmitRequestElderly(HttpSession session, @ModelAttribute("request") Request request, Model model, BindingResult bindingResult) {
        Service servicio = serviceDao.getService(request.getIdService());
        UserDetails user = (UserDetails) session.getAttribute("user");
        request.setNif("0");
        if (bindingResult.hasErrors()) {
            return "request/addRequestElderly";
        }
        if (request.getDateStart().isBefore(LocalDate.now()) || request.getDateEnd().isBefore(request.getDateStart())) {
            mensajeError = "La fecha de inicio no puede ser anterior a hoy y la fecha de finalización anterior a la de inicio";
            return "redirect:/request/addRequestElderly";
        }
        if (requestDao.checkService(servicio.getServiceType(), user.getDni())) {
            bindingResult.rejectValue("idService", "badserv", " Ya has solicitado este servicio");
            List<Service> servicios = serviceDao.getServices();
            model.addAttribute("servicios", servicios);
            List<Company> company = companyDao.getCompanies();
            model.addAttribute("companyies", company);

            return "request/addRequestElderly";
        }
        request.setDateRequest(LocalDate.now());
        request.setState("Pendiente");
        request.setDni(user.getDni());
        requestDao.addRequest(request);
        int id = requestDao.ultimoIdRequest();

        mailController = new MailController(elderlyDao.getElderly(request.getDni()).getEmail());
        mailController.addMail("La solicitud correspondiente al servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación.");


        return "redirect:/request/listElderly?nuevo=" + id;

    }

    @RequestMapping(value = "/cancelarRequest/{idRequest}")
    public String processUpdateEstadp(@PathVariable int idRequest) {

        mailController = new MailController(elderlyDao.getElderly(requestDao.getRequest(idRequest).getDni()).getEmail());
        mailController.deleteMail("Se ha cancelado la solicitud correspondiente al servicio: " + serviceDao.getService(requestDao.getRequest(idRequest).getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación.");

        requestDao.updateEstado(idRequest, "Cancelada");
        return "redirect:../listElderly?nuevo=" + idRequest;
    }

}
