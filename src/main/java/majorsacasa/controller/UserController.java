package majorsacasa.controller;

import majorsacasa.dao.UserDao;
import majorsacasa.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserDao userDao;

    @Autowired
    public void setSociDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping("/list")
    public String listSocis(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new UserDetails());
            session.setAttribute("nextUrl", "user/list");
            return "login";
        }
        UserDetails user = (UserDetails) session.getAttribute("user");
        if (user.getTipo().equals("SocialWorker"))
            return "login";
        model.addAttribute("users", userDao.listAllUsers());
        return "user/list";
    }
}
