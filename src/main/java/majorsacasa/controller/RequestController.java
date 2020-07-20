package majorsacasa.controller;

import majorsacasa.dao.*;
import majorsacasa.mail.MailBody;
import majorsacasa.mail.MailService;
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
    private MailBody mailBody;
    private MailService mailService;
    private UserDao userDao;
    private RequestDao requestDao;
    private ServiceDao serviceDao;
    private CompanyDao companyDao;
    private ElderlyDao elderlyDao;
    private final List estados = Arrays.asList("Pendiente", "Aceptada", "Rechazada", "Cancelada");
    static String mensajeError = "";

    @Autowired
    public void setRequestDao(ProduceDao produceDao, InvoiceDao invoiceDao, ValoracionDao valoracionDao, RequestDao requestDao, ServiceDao serviceDao, CompanyDao companyDao, ElderlyDao elderlyDao, ContractDao contractDao, UserDao userDao, MailService mailService) {
        this.requestDao = requestDao;
        this.serviceDao = serviceDao;
        this.companyDao = companyDao;
        this.elderlyDao = elderlyDao;
        this.contractDao = contractDao;
        this.valoracionDao = valoracionDao;
        this.invoiceDao = invoiceDao;
        this.produceDao = produceDao;
        this.mailService = mailService;
        this.userDao = userDao;
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

        Elderly elderly = elderlyDao.getElderly(request.getDni());
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());

        mailBody = new MailBody(elderly.getEmail());
        mailBody.addMail("La solicitud correspondiente al servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación.");
        mailService.sendEmail(mailBody, user);

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

    private int calculateNumDias(LocalDate fechaIni, LocalDate fechaFin, ArrayList<String> diasRequest) {
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
            UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
            mailBody = new MailBody(elderly.getEmail());
            mailBody.updateMail("Su solicitud del servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " ha sido " + request.getState() + " y lo puede ver en su lista de solicitudes. Por favor, no olvide valorar el servicio.");
            mailService.sendEmail(mailBody, user);

            // SE LE COMUNICA A LA EMPRESA QUE SE LE HA ASIGNADO UNA PERSONA MAYOR
            HashMap<String, String> mapaServiciosCompany = serviceDao.getMapServiceCompany();
            Company empresa = companyDao.getCompany(request.getNif());
            user = userDao.loadUserByUsername(empresa.getNombreUsuario(), empresa.getPassword());
            mailBody = new MailBody(empresa.getEmail());
            mailBody.updateMail("Se le ha asignado una persona mayor a su servicio de " + mapaServiciosCompany.get(empresa.getNif()) +
                    ". Puede ver los detalles en el listado de personas mayores de su servicio.\n" +
                    "La persona mayor se llama: " + elderly.getNombre() + " " + elderly.getApellidos() + " y su dirección es: " + elderly.getDireccion());
            mailService.sendEmail(mailBody, user);
        } else {
            if (request.getState().equals("Rechazada")) {
                request.setDateReject(LocalDate.now());
            }

            Elderly elderly = elderlyDao.getElderly(request.getDni());
            UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
            mailBody = new MailBody(elderly.getEmail());
            mailBody.updateMail("Su solicitud del servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " ha sido " + request.getState() + " y lo puede ver en su lista de solicitudes.");
            mailService.sendEmail(mailBody, user);
        }
        List<Company> company = companyDao.getCompanies();
        model.addAttribute("companyies", company);

        HashMap<String, String> u = valoracionDao.getUsersInfo();
        model.addAttribute("usuario", u);

        requestDao.updateRequest(request);
        return "redirect:../request/list/" + request.getDni();

    }

    // COMPROBAR FECHAS DE LAS PETICIONES
    private void comprobarRequests(Request request) {
        if (request.getDateEnd().isBefore(LocalDate.now())) {
            requestDao.updateEstado(request.getIdRequest(), "Finalizada");
        }
    }

    // LISTA DEL casCommitee
    @RequestMapping(value = "/list/{dni}")
    public String listRequestsPersonalizado(@PathVariable String dni, Model model, @RequestParam("nuevo") Optional<String> nuevo, HttpSession session) {
        List<Request> peticiones = requestDao.getRequestsElderly(dni);
        for (Request request : peticiones) {
            comprobarRequests(request);
        }
        model.addAttribute("requests", peticiones);
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
        Elderly elderly = elderlyDao.getElderly(request.getDni());
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
        if (!request.getState().equals("Pendiente") || !request.getState().equals("Aceptada")) {
            requestDao.deleteRequest(idRequest);
            mailBody = new MailBody(elderly.getEmail());
            mailBody.deleteMail("Se ha eliminado la solicitud correspondiente al servicio: " + serviceDao.getService(requestDao.getRequest(idRequest).getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación");
            mailService.sendEmail(mailBody, user);
        } else {
            mensajeError = "No puedes borrar una petición aceptada o pendiente";
        }
        return "redirect:../list/" + request.getDni();
    }

    // LISTA DEL ELDERLY PEOPLE
    @RequestMapping("/listElderly")
    public String listRequestsElderly(HttpSession session, Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        List<Request> peticiones = requestDao.getRequestsElderly(user.getDni());
        for (Request request : peticiones) {
            comprobarRequests(request);
        }
        model.addAttribute("requests", peticiones);
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
    public String processAddSubmitRequestElderly(@ModelAttribute("request") Request request, Model model, BindingResult bindingResult) {
        Service servicio = serviceDao.getService(request.getIdService());
        Elderly elderly = elderlyDao.getElderly(request.getDni());
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
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

        mailBody = new MailBody(elderly.getEmail());
        mailBody.addMail("La solicitud correspondiente al servicio: " + serviceDao.getService(request.getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación.");
        mailService.sendEmail(mailBody, user);

        return "redirect:/request/listElderly?nuevo=" + id;

    }

    @RequestMapping(value = "/cancelarRequest/{idRequest}")
    public String processUpdateEstadp(@PathVariable int idRequest) {
        Elderly elderly = elderlyDao.getElderly(requestDao.getRequest(idRequest).getDni());
        UserDetails user = userDao.loadUserByUsername(elderly.getUsuario(), elderly.getContraseña());
        mailBody = new MailBody(elderly.getEmail());
        mailBody.deleteMail("Se ha cancelado la solicitud correspondiente al servicio: " + serviceDao.getService(requestDao.getRequest(idRequest).getIdService()).getDescription() + " se ha enviado correctamente y está pendiente de aceptación.");
        mailService.sendEmail(mailBody, user);

        requestDao.updateEstado(idRequest, "Cancelada");

        // RECALCULAR EL PRECIO DE LA FACTURA HASTA EL DIA EN EL QUE SE HA CANCELADO
        Request request = requestDao.getRequest(idRequest);
        Produce produce = produceDao.getProduceRequest(idRequest);
        Invoice factura = invoiceDao.getInvoice(produce.getIdInvoice());
        int numDias = calculateNumDias(request.getDateStart(), LocalDate.now(), request.getDiasIngles());
        Integer precioTotal = serviceDao.getService(request.getIdService()).getPrice() * numDias;
        factura.setTotalPrice(precioTotal);
        factura.setInvoicePDF(false);
        invoiceDao.updateInvoice(factura);
        return "redirect:../listElderly?nuevo=" + idRequest;
    }

}
