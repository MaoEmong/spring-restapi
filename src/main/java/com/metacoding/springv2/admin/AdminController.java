package com.metacoding.springv2.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    // cos만 접속 가능(인증이 아닌 admin 권한 체크)
    @GetMapping("/admin/test")
    public String test() {
        return "<h1>관리자 페이지</h1>";
    }
}
