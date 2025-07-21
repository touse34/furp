package com.furp.controller;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import com.furp.entity.Result;
import com.furp.service.PhdInfoService;
import com.furp.service.PhdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhdController {
    @Autowired
    private PhdInfoService phdinfoService;

    @RequestMapping("/phd/student/info")
    public Result list(){
        System.out.println("Student info");
        List<PhdInfo> phdInfoList =  phdinfoService.findAll();

        return Result.success(phdInfoList);

    }
}
