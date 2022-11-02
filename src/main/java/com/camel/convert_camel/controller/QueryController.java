package com.camel.convert_camel.controller;

import com.camel.convert_camel.domain.DbStatus;
import com.camel.convert_camel.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    @GetMapping("/")
    public String query(Model model){
        model.addAttribute("dbStatus", queryService.initDbStatus());
        return "index";
    }

    @PostMapping ("/runquery")
    public String query(Model model, DbStatus dbStatus){
        model.addAttribute("dbStatus", dbStatus);
        model.addAttribute("colnums", queryService.colnums(dbStatus));
        return "query";
    }


}
