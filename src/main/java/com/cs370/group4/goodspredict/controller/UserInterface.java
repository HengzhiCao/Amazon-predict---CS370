package com.cs370.group4.goodspredict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserInterface {
@RequestMapping("/")
    public String index(){
        return "Hello";
    }
}
