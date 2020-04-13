package majorsacasa.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class DemoExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception ex) {
        ModelAndView mav = new ModelAndView("error/exceptionError");
        mav.addObject("message", ex.getMessage());
        //mav.addObject("errorName", ex.getHeaders());
        return mav;
        //return "error/exceptionError";
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFoundError(NoHandlerFoundException ex) {
        ModelAndView mav = new ModelAndView("error/exceptionError");
        mav.addObject("message", ex.getMessage());
        //mav.addObject("errorName", ex.getHeaders());
        return mav;

    }
}