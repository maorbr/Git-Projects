package com.maor.test.core.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "items")
public class Item {
	
    @ApiModelProperty(notes = "The database generated item number")
	private Long itemNo;
    
    @ApiModelProperty(notes = "The item name")
	private String name;
    
    @ApiModelProperty(notes = "The item amount")
	private Long amount;
    
    @ApiModelProperty(notes = "The item inventory code")
	private Long inventoryCode;

	public Item() {

	}

	public Item(String name, Long amount, Long inventoryCode) {
		this.name = name;
		this.amount = amount;
		this.inventoryCode = inventoryCode;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AMOUNT", nullable = false)
	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	@Column(name = "INVENTORY_CODE", nullable = false)
	public Long getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(Long inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	@Override
	public String toString() {
		return "Item [itemNo=" + itemNo + ", name=" + name + ", amount=" + amount + ", inventoryCode=" + inventoryCode
				+ "]";
	}
}

