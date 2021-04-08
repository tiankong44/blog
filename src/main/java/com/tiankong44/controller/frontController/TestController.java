package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping({"/foreach"})
    @ResponseBody
    public BaseRes foreach() {
        BaseRes res = new BaseRes();
        res = this.testService.test();
        return res;

    }


    @RequestMapping({"/insert"})
    @ResponseBody
    public BaseRes inset() {
        BaseRes res = new BaseRes();
        res = this.testService.inset();
        return res;

    }
}
