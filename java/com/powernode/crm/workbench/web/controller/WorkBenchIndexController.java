package com.powernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkBenchIndexController {
    @RequestMapping("/workbench/index.do")//从视图解析器下面往下找
    public String index(){
        return "workbench/index";
    }
}
