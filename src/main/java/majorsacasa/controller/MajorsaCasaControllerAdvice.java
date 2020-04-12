package majorsacasa.controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ControllerAdvice
public class MajorsaCasaControllerAdvice {
    @ExceptionHandler(value = MajorsaCasaException.class)
    public ModelAndView handleClubException(MajorsaCasaException ex){

        ModelAndView mav = new ModelAndView("error/exceptionError");
        mav.addObject("message", ex.getMessage());
        mav.addObject("errorName", ex.getErrorName());
        return mav;
    }
   /*@ExceptionHandler(value=NoHandlerFoundException.class)
   @RequestMapping(value = "/error")
   public ModelAndView handleNotFoundError(HttpServletResponse response,
                                        NoHandlerFoundException ex) {
            ModelAndView mav = new ModelAndView("error/exceptionError");
            mav.addObject("message", "La pàgina no existe");
            mav.addObject("errorName", "Error 404");
            return mav;

        }

    @RequestMapping("/anotherService")
    public String generator() throws Exception {
        throw new Exception("excepcion");}
    }*/
   @RequestMapping("/greeting")
   public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World")String name, Model model){
       model.addAttribute("name", name);
       return "greeting";
   }



}
