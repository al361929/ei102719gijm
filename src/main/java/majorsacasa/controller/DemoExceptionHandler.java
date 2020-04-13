package majorsacasa.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class DemoExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception ex){
        ModelAndView mav = new ModelAndView("error/exceptionError");
        mav.addObject("message", ex.getMessage());
        //mav.addObject("errorName", ex.getErrorName());
        return mav;
        //return "error/exceptionError";
    }
    @ExceptionHandler(value = MajorsaCasaException.class)
    public ModelAndView handleClubException(MajorsaCasaException ex){

        ModelAndView mav = new ModelAndView("error/exceptionError");
        mav.addObject("message", ex.getMessage());
        mav.addObject("errorName", ex.getErrorName());
        return mav;
    }

}