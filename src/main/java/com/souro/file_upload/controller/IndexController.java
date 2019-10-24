package com.souro.file_upload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * @author
 *
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "upload";
    }

}