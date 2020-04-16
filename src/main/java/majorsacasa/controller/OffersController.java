package majorsacasa.controller;

import majorsacasa.dao.OffersDao;
import majorsacasa.dao.ServiceDao;
import majorsacasa.model.Offer;
import majorsacasa.model.Service;
import majorsacasa.model.UserDetails;
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
public class OffersController extends Controlador{
    private OffersDao offersDao;
    private ServiceDao serviceDao;


    @Autowired
    public void setOffersDao(OffersDao offersDao, ServiceDao serviceDao) {
        this.serviceDao = serviceDao;

        this.offersDao = offersDao;
    }

    @RequestMapping("/list")
    public String listOffers(Model model, @RequestParam("nuevo") Optional<String> nuevo) {
        model.addAttribute("offers", offersDao.getOffers());
        String newVolunteerTime = nuevo.orElse("None");
        model.addAttribute("nuevo", newVolunteerTime);
        return "offer/list";
    }

    @RequestMapping(value = "/addService")
    public String addOffer(Model model) {
        model.addAttribute("offer", new Offer());
        List<Service> servicios = serviceDao.getServices();
        System.out.println(servicios.toString());

        model.addAttribute("servicios", servicios);

        return "offer/addService";
    }

    @RequestMapping(value = "/addService", method = RequestMethod.POST)
    public String processAddSubmit(HttpSession session,Model model, @ModelAttribute("offer") Offer offers, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "offer/addService";
        UserDetails user = (UserDetails) session.getAttribute("user");

        offers.setNif(user.getDni());
        offersDao.addOffers(offers);
       // return "redirect:list?nuevo=" + offers.getIdService();
        return gestionarAcceso(session,model,"Company","redirect:../company/serviceList?nuevo=" + offers.getIdService());

    }

    //POSIBLE FALLO
    @RequestMapping(value = "/delete/{idService}/{nif}")
    public String processDelete(@PathVariable String idService, @PathVariable String nif) {
        offersDao.deleteOffers(idService, nif);
        return "redirect:../list";
    }
}
