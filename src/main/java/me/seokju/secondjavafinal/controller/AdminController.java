package me.seokju.secondjavafinal.controller;

import me.seokju.secondjavafinal.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 장바구니에 있는 것들 수강 테이블로 옮기기
    @GetMapping("/move-cart-to-timetable")
    public void moveCartToTimeTable() {
        adminService.moveCartToTimeTable();
    }
}
