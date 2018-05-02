package com.pinyougou.manage.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/login")
//@Controller
@RestController //组合注解@Controller @ResponseBody
public class LoginController {

    @GetMapping("/getUsername")
    public Map<String,Object> getUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        return map;
    }


}
