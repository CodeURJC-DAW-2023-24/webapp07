package com.daw.webapp07.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewErrorController implements ErrorController {

    // This method is called when the user tries to access a page that does not exist
    @GetMapping("/error")
    public String error() {
        return "error-page";
    }

    // This method is called when the user tries to access with a wrong password or username
    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }

    // This method is called when the controllers throw an error
    @GetMapping("/error-page")
    public String errorHandle() {
        return "error-page";
    }
}
