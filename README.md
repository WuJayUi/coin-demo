CREATE TABLE coin (
    id INT AUTO_INCREMENT PRIMARY KEY,               
    coin_code VARCHAR(10) NOT NULL ,                 -- 幣別代碼
    coin_name VARCHAR(50) NOT NULL,                  -- 幣別的中文名稱
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- 建立時間
);
