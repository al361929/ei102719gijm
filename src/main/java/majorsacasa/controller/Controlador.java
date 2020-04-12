package majorsacasa.controller;

import majorsacasa.model.UserDetails;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public class Controlador {
    public String gestionarAcceso(HttpSession session, Model model, String categoria, String url){
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new UserDetails());
            session.setAttribute("nextUrl", url);
            return "login";
        }
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (!user.getTipo().equals(categoria)) {
            return "error/sinPermiso";
        }
        return url;
    }
}
