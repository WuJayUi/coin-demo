package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CoinEntity;


@Repository
public interface Coinrepository  extends JpaRepository<CoinEntity, String>{
	
	Optional<CoinEntity> findById(Integer id);
	
	List<CoinEntity> deleteById(Integer id);

}
