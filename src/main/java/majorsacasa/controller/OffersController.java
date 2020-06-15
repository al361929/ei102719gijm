package majorsacasa.controller;

import majorsacasa.dao.CompanyDao;
import majorsacasa.dao.OffersDao;
import majorsacasa.dao.ServiceDao;
import majorsacasa.model.Offer;
import majorsacasa.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/offer")
public class OffersController extends ManageAccessController {
    private OffersDao offersDao;
    private ServiceDao serviceDao;
    private CompanyDao companyDao;


    @Autowired
    public void setOffersDao(OffersDao offersDao, ServiceDao serviceDao, CompanyDao companyDao) {
        this.serviceDao = serviceDao;
        this.companyDao = companyDao;
        this.offersDao = offersDao;
    }

    @RequestMapping("/list")
    public String listOffers(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("offers", offersDao.getOffers());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "offer/list";
    }

    @RequestMapping(value = "/addService/{nif}")
    public String addOffer(Model model, @PathVariable String nif) {

        List<Service> servicios = serviceDao.getServices();
        model.addAttribute("servicios", servicios);

        Offer offer = new Offer();
        offer.setNif(nif);
        model.addAttribute("offer", offer);

        model.addAttribute("company", companyDao.getCompany(nif).getNombre());
        return "offer/addService";
    }

    @RequestMapping(value = "/addService", method = RequestMethod.POST)
    public String processAddSubmit(HttpSession session, Model model, @ModelAttribute("offer") Offer offer, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "offer/addService";
        Boolean checkCompany = offersDao.checkService(offer.getNif());

        if (!checkCompany) {
            List<Service> servicios = serviceDao.getServiceList(offer.getNif());
            String name = servicios.get(0).getDescription();
            bindingResult.rejectValue("idService", "badnif", " Ya estas ofreciendo: " + name);

            servicios = serviceDao.getServices();
            model.addAttribute("servicios", servicios);


            return "offer/addService";
        }

        offersDao.addOffers(offer);

        return gestionarAcceso(session, model, "Admin", "redirect:../contract/add");

    }

    //POSIBLE FALLO
    @RequestMapping(value = "/delete/{idService}/{nif}")
    public String processDelete(@PathVariable String idService, @PathVariable String nif) {
        offersDao.deleteOffers(idService, nif);
        return "redirect:../list";
    }
}
