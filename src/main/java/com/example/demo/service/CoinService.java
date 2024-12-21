package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.entity.CoinEntity;
import com.example.demo.repository.Coinrepository;


@Service
public class CoinService {

	@Autowired
	private Coinrepository coinrepository;
	
    @Autowired
    private RestTemplate restTemplate;
	
	public CoinEntity create(CoinEntity test) {

		return coinrepository.save(test);
	}
	
	public CoinEntity getOne(Integer id) {
	    return coinrepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Coin not found and ID = " + id));
	}
	
	public List<CoinEntity> getList() {
		return coinrepository.findAll();
	}
	
	public String update(CoinEntity test) {
		coinrepository.save(test);
		return "oK";
	}
	
	public String deleteById(Integer id) {
	    coinrepository.deleteById(id);
	    return "Deleted";
	}
	
    public void saveCoinData() {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

        try {
            // 使用restTemplate
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // 用ObjectMapper解析Json
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            Map<String, Object> bpi = (Map<String, Object>) responseBody.get("bpi");
            Map<String, Object> usdData = (Map<String, Object>) bpi.get("USD");

            // 將資料存進db
            CoinEntity coin = new CoinEntity();
            coin.setCoinCode((String) usdData.get("code"));
            coin.setCoinName((String) usdData.get("description"));
            coin.setRate((String) usdData.get("rate"));
            coin.setRateFloat((Double) usdData.get("rate_float"));

            coinrepository.save(coin);
        } catch (Exception e) {
            throw new RuntimeException("Error Message: " + e.getMessage());
        }
    }
    
    public Map<String, Object> transformCoinData() {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

        try {
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            Map<String, Object> bpi = (Map<String, Object>) responseBody.get("bpi");

            //將取到的資料格式化
            Map<String, String> timeData = (Map<String, String>) responseBody.get("time");
            String updatedISO = timeData.get("updatedISO");
            LocalDateTime updatedAt = LocalDateTime.parse(updatedISO);
            String formattedTime = updatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
       
            Map<String, Object> transformedData = new HashMap<>();
            transformedData.put("updatedTime", formattedTime);

            Map<String, Object> coinInfo = new HashMap<>();
            for (Map.Entry<String, Object> entry : bpi.entrySet()) {
                String coin = entry.getKey();
                Map<String, Object> coinData = (Map<String, Object>) entry.getValue();

                Map<String, Object> info = new HashMap<>();
                info.put("coinCode", coin);
                info.put("coinName", coinData.get("description"));
                info.put("rate", coinData.get("rate"));

                coinInfo.put(coin, info);
            }

            transformedData.put("currencyInfo", coinInfo);

            return transformedData;

        } catch (Exception e) {
            throw new RuntimeException("Error processing Coindesk API: " + e.getMessage());
        }
    }
}
