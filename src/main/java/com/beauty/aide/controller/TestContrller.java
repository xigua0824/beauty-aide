package com.beauty.aide.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoliu
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestContrller {

    @GetMapping("/health")
    public Object health() {
        return "ok";
    }

}
