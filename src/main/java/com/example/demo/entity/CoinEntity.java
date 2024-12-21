package com.example.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coin", schema = "testdb")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoinEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
    @Column(name = "coinCode")
    private String coinCode;

    @Column(name = "coinName")
    private String coinName;
    
    @Column(name = "rate")
    private String rate;

    @Column(name = "rateFloat")
    private Double rateFloat;
    
    @Column(name = "createdAt")
    private LocalDateTime  createdAt;
}
