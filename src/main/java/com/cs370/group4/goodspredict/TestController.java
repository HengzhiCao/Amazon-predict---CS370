package com.cs370.group4.goodspredict;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class TestController {
    @RequestMapping("/testPage")
    public String test(Model model){
        String hello = "Hello Thymeleaf";
        model.addAttribute("hello",hello);

        return "test/test";
    }
}