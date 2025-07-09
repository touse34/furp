package com.furp.controller;


import com.furp.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {
    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/run")
    public ResponseEntity<String> triggerAutoSchedule() {
        try {
            System.out.println("接收到排程请求，开始执行 autoSchedule 算法...");
            schedulingService.autoSchedule();
            System.out.println("autoSchedule 算法执行完毕。");
            return ResponseEntity.ok("自动排程任务已成功触发并执行完毕！");
        } catch (Exception e) {
            // 在实际项目中，这里应该有更完善的异常处理
            e.printStackTrace();
            return ResponseEntity.status(500).body("排程过程中发生错误: " + e.getMessage());
        }
    }
}
