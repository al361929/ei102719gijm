package majorsacasa.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DemoExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(){
        return "error/exceptionError";
    }

}