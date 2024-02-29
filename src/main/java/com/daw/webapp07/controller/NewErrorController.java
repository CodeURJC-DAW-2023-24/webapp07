package com.daw.webapp07.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewErrorController implements ErrorController {

    @GetMapping("/error")
    public String error() {
        return "error-page";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }

    @GetMapping("/error-page")
    public String errorHandle() {
        return "error-page";
    }
}
