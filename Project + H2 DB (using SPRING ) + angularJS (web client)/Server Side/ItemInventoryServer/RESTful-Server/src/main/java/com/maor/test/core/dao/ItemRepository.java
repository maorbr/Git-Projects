package com.maor.test.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maor.test.core.beans.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
	
}
