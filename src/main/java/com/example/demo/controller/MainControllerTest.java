package com.example.demo.controller;

import com.example.demo.entity.CoinEntity;
import com.example.demo.service.CoinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllerRq.GetOneRq;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoinService coinService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    
    @Test
    public void testCallCoin() throws Exception {
        Mockito.doNothing().when(coinService).saveCoinData();

        mockMvc.perform(post("/Api/callCoindesk"))
            .andExpect(status().isOk())
            .andExpect(content().string("save ok"));
    }

    
    @Test
    public void testTransformedCoinkData() throws Exception {
        Map<String, Object> mockTransformedData = Map.of(
            "updatedTime", "2024/12/20 10:00:00",
            "currencyInfo", Map.of(
                "USD", Map.of("coinCode", "USD", "coinName", "United States Dollar", "rate", "99,419.675")
            )
        );

        Mockito.when(coinService.transformCoinData()).thenReturn(mockTransformedData);

        mockMvc.perform(get("/Api/transform"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.updatedTime").value("2024/12/20 10:00:00"))
            .andExpect(jsonPath("$.currencyInfo.USD.coinCode").value("USD"))
            .andExpect(jsonPath("$.currencyInfo.USD.coinName").value("United States Dollar"))
            .andExpect(jsonPath("$.currencyInfo.USD.rate").value("99,419.675"));
    }

    
    @Test
    public void testCreateCoin() throws Exception {
        CoinEntity newCoin = new CoinEntity(null, "JPY", "Japanese Yen", "1200.00", 1200.0, LocalDateTime.now());
        CoinEntity savedCoin = new CoinEntity(1L, "JPY", "Japanese Yen", "1200.00", 1200.0, LocalDateTime.now());

        Mockito.when(coinService.create(Mockito.any(CoinEntity.class))).thenReturn(savedCoin);

        mockMvc.perform(post("/Api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCoin)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.coinCode").value("JPY"));
    }

    
    @Test
    public void testGetOne() throws Exception {
        CoinEntity mockCoin = new CoinEntity(1L, "USD", "United States Dollar", "1000.00", 1000.0, LocalDateTime.now());
        GetOneRq param = new GetOneRq();
        param.setId(1);

        Mockito.when(coinService.getOne(1)).thenReturn(mockCoin);

        mockMvc.perform(get("/Api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.coinCode").value("USD"))
            .andExpect(jsonPath("$.coinName").value("United States Dollar"));
    }

    
    @Test
    public void testGetList() throws Exception {
        List<CoinEntity> mockData = List.of(
            new CoinEntity(1L, "USD", "United States Dollar", "1000.00", 1000.0, LocalDateTime.now()),
            new CoinEntity(2L, "EUR", "Euro", "900.00", 900.0, LocalDateTime.now())
        );

        Mockito.when(coinService.getList()).thenReturn(mockData);

        mockMvc.perform(get("/Api"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].coinCode").value("USD"))
            .andExpect(jsonPath("$[1].coinCode").value("EUR"));
    }

    
    @Test
    public void testUpdate() throws Exception {
        CoinEntity updatedCoin = new CoinEntity(1L, "USD", "Updated Dollar", "2000.00", 2000.0, LocalDateTime.now());

        Mockito.when(coinService.update(Mockito.any(CoinEntity.class))).thenReturn("OK");

        mockMvc.perform(put("/Api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCoin)))
            .andExpect(status().isOk())
            .andExpect(content().string("OK"));
    }

    
    @Test
    public void testDelete() throws Exception {
        GetOneRq param = new GetOneRq();
        param.setId(1);

        Mockito.when(coinService.deleteById(1)).thenReturn("Deleted");

        mockMvc.perform(delete("/Api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
            .andExpect(status().isOk())
            .andExpect(content().string("Deleted"));
    }
}
