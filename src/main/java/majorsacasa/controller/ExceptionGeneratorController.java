package majorsacasa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
public class ExceptionGeneratorController {
    @RequestMapping("/anotherService")
    public String generator() throws Exception {
        throw new Exception("excepcion");
    }


}