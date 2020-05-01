package majorsacasa.controller;

import majorsacasa.dao.UserDao;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserDetails());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") UserDetails user,
                             BindingResult bindingResult, HttpSession session) {
        //UserValidator userValidator = new UserValidator();
        //userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }
        // Comprova que el login siga correcte
        // intentant carregar les dades de l'usuari
        user = userDao.loadUserByUsername(user.getUsername(), user.getPassword());
        if (user == null) {
            bindingResult.rejectValue("password", "badpw", "Contrasenya incorrecta");
            return "login";
        }
        // Autenticats correctament.
        // Guardem les dades de l'usuari autenticat a la sessió
        String url = (String) session.getAttribute("nextUrl");
        session.setAttribute("user", user);

        // Torna a la pàgina principal
        if (url != null)
            return "redirect:/" + url;
        if (user.getTipo().equals("Volunteer")) {
            return "redirect:/volunteer/scheduleList";
        }else{
            if (user.getTipo().equals("ElderlyPeople")) {
                return "redirect:/request/listElderly";
            }else{
                if (user.getTipo().equals("Company")) {
                    return "redirect:/company/contractList";
                }else{
                    if (user.getTipo().equals("SocialWorker")) {
                        return "redirect:/socialWorker/elderlyList";
                    }else{
                        if (user.getCode()==6) {
                            return "redirect:/company/list";
                        }
                        if (user.getCode()==7) {
                            return "redirect:/elderly/list";
                        }
                        if (user.getCode()==8) {
                            return "redirect:/volunteer/list";
                        }
                        if (user.getTipo().equals("Admin")) {
                            return "redirect:/contract/list";
                        }
                    }
                }
            }
        }

        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
