package majorsacasa.controller;

import majorsacasa.model.UserDetails;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public class ManageAccessController {
    public String gestionarAcceso(HttpSession session, Model model, String categoria, String url) {

        String destino = sesionAbierta(session, model, url);
        if (destino != null) return destino;

        UserDetails user = (UserDetails) session.getAttribute("user");
        if (!user.getTipo().equals(categoria) && !user.getTipo().equals("Admin")) {
            return "error/sinPermiso";
        }
        return url;
    }

    public String sesionAbierta(HttpSession session, Model model, String url) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new UserDetails());
            session.setAttribute("nextUrl", url);
            return "login";
        }
        return null;
    }
}
