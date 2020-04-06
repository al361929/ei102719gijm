package majorsacasa.controller;

import majorsacasa.dao.OffersDao;
import majorsacasa.model.Offers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/offers")
public class OffersController {
    private OffersDao offersDao;

    @Autowired
    public void setOffersDao(OffersDao offersDao) {
        this.offersDao = offersDao;
    }

    @RequestMapping("/list")
    public String listOffers(Model model) {
        model.addAttribute("offers", offersDao.getOffers());
        return "offers/list";
    }

    @RequestMapping(value = "/add")
    public String addOffer(Model model) {
        model.addAttribute("offer", new Offers());
        return "offers/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("offer") Offers offers, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "offers/add";
        offersDao.addOffers(offers);
        return "redirect:list";
    }


    //POSIBLE FALLO
    @RequestMapping(value = "/delete/{idService}/{nif}")
    public String processDelete(@PathVariable String idService, @PathVariable String nif) {
        offersDao.deleteOffers(idService, nif);
        return "redirect:../list";
    }
}
