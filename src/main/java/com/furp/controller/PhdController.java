package com.furp.controller;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import com.furp.entity.Result;
import com.furp.service.PhdInfoService;
import com.furp.service.PhdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhdController {
    @Autowired
    private PhdInfoService phdinfoService;
    @Autowired
    private PhdInfoService phdInfoService;

    @GetMapping("/phd/student/info")
    public Result list(){
        System.out.println("Student info");
        List<PhdInfo> phdInfoList =  phdinfoService.findAll();

        return Result.success(phdInfoList);

    }

    /**
     * 根据ID查询phd
     * @param
     * @return
     */
    @GetMapping("/phd/student/info/{id}")
    public Result getInfo(@PathVariable("id") Integer studentId){
        System.out.println("根据studentId查询phdinfo  " + studentId);

        PhdInfo phdInfo = phdInfoService.getById(studentId);

        return Result.success(phdInfo);


    }
    /*
    修改技能
     */
    @PutMapping("/phd/student/research-areas")
    public Result update(@RequestBody PhdInfo phdInfo){
        System.out.println("修改学生专业" + phdInfo);
        phdInfoService.update(phdInfo);
        return Result.success();


    }


}
