package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.CoinEntity;
import com.example.demo.service.CoinService;
import controllerRq.GetOneRq;

@RestController
@RequestMapping(value = "/Api")
public class MainController {
  
    @Autowired
    private CoinService coinService;
    
    //打api存進db
    @PostMapping("/callCoindesk")
    public ResponseEntity<String> callCoindesk() {
        try {
            coinService.saveCoinData();
            return ResponseEntity.ok("save ok");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    //打api將拿到的資料轉換
    @GetMapping("/transform")
    public ResponseEntity<Map<String, Object>> getTransformedCoindeskData() {
        try {
            Map<String, Object> transformedData = coinService.transformCoinData();
            return ResponseEntity.ok(transformedData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    //db新增
    @PostMapping
    public ResponseEntity<CoinEntity> create(@RequestBody CoinEntity coin) {
        return ResponseEntity.ok(coinService.create(coin));
    }

    //db找單一資料
    @GetMapping
    public ResponseEntity<CoinEntity> getOne(@RequestBody GetOneRq param) {
        return ResponseEntity.ok(coinService.getOne(param.getId()));
    }

    //db找全部資料
    @GetMapping
    public ResponseEntity<List<CoinEntity>> getList() {
        return ResponseEntity.ok(coinService.getList());
    }

    //db修改資料
    @PutMapping
    public ResponseEntity<String> update(@RequestBody CoinEntity coin) {
        return ResponseEntity.ok(coinService.update(coin));
    }

    //db刪除資料
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody GetOneRq param) {
        return ResponseEntity.ok(coinService.deleteById(param.getId()));
    }
}

