package majorsacasa.controller;

import majorsacasa.dao.ProduceDao;
import majorsacasa.model.Produce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/produce")
public class ProduceController {

    private ProduceDao produceDao;

    @Autowired
    public void setProduceDao(ProduceDao produceDao) {
        this.produceDao = produceDao;
    }

    @RequestMapping("/list")
    public String listProduces(Model model) {
        model.addAttribute("produces", produceDao.getProduces());
        return "produce/list";
    }

    @RequestMapping(value = "/add")
    public String addProduce(Model model) {
        model.addAttribute("produce", new Produce());
        return "produce/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("produce") Produce produce, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "produce/add";
        produceDao.addProduce(produce);
        return "redirect:list";
    }


    @RequestMapping(value = "/delete/{idInvoice}/{idRequest}")
    public String processDelete(@PathVariable int idInvoice, @PathVariable int idRequest) {
        produceDao.deleteProduce(idInvoice, idRequest);
        return "redirect:../list";
    }
}
