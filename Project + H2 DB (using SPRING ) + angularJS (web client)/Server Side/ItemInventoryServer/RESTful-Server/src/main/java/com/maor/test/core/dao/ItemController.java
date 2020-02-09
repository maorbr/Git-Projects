package com.maor.test.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maor.test.core.beans.Item;
import com.maor.test.core.exceptions.DataNotFoundException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin("*")
@Api(value = "Items Management System")
@RestController
@RequestMapping("/api") //route
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;
	
	//[IP Address]:[port]/api/items - Show the List of the inventory items list.
	@ApiOperation(value = "Show the List of the inventory items list", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/items")
	public List<Item> getAllItems() {
		return itemRepository.findAll();
	}

	//[IP Address]:[port]/api/items/{id} - Read item details (by item no).
	@ApiOperation(value = "Read item details (by item no)", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/items/{id}")
	public ResponseEntity<Item> getItemByItemNo(
			@ApiParam(value = "Item number from which item object will retrieve", required = true) @PathVariable(value = "id") Long itemNo)
			throws DataNotFoundException {
		Item item = itemRepository.findById(itemNo)
				.orElseThrow(() -> new DataNotFoundException("item " + itemNo + "was not found."));
		return ResponseEntity.ok().body(item);
	}
	
	//[IP Address]:[port]/api/items/withdrawal/{id} - Withdrawal quantity of a specific item from stock.
	@ApiOperation(value = "Withdrawal quantity of a specific item from stock", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PatchMapping("/items/withdrawal/{id}") //
	public ResponseEntity<Item> withdrawalItemByItemNo(
			@ApiParam(value = "Item number to update withdrawal quantity on object", required = true) @PathVariable(value = "id") Long itemNo)
			throws DataNotFoundException {
		Item item = itemRepository.findById(itemNo)
				.orElseThrow(() -> new DataNotFoundException("item " + itemNo + "was not found."));
		// Long balance = item.getAmount();
		item.setAmount((long) 0);
		final Item updatedItem = itemRepository.save(item);
		return ResponseEntity.ok().body(updatedItem);
	}

	//[IP Address]:[port]/api/items//deposit/{id} - Deposit quantity of a specific item to stock.
	@ApiOperation(value = "Deposit quantity of a specific item to stock", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PatchMapping("/items/deposit/{id}")
	public ResponseEntity<Item> DepositItemByItemNo(
			@ApiParam(value = "Item number to update deposit quantity on object", required = true) @PathVariable(value = "id") Long itemNo,
			@Valid @RequestBody Item itemDetails) throws DataNotFoundException {
		Item item = itemRepository.findById(itemNo)
				.orElseThrow(() -> new DataNotFoundException("item " + itemNo + "was not found."));
		item.setAmount(item.getAmount() + itemDetails.getAmount());
		final Item updatedItem = itemRepository.save(item);
		return ResponseEntity.ok().body(updatedItem);
	}

	//[IP Address]:[port]/api/items - Add item to stock.
	@ApiOperation(value = "Add item to stock ", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping("/items")
	public Item addItems(
			@ApiParam(value = "Item object store in database table", required = true) @Valid @RequestBody Item item) {
		return itemRepository.save(item);
	}

	//[IP Address]:[port]/api/items/{id} - Delete an item from stock.
	@ApiOperation(value = "Delete an item from stock", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@DeleteMapping("/items/{id}")
	public Map<String, Boolean> deleteItemByItemNo(
			@ApiParam(value = "Item number from which employee object will delete from database table", required = true) @PathVariable(value = "id") Long itemNo)
			throws DataNotFoundException {
		Item item = itemRepository.findById(itemNo)
				.orElseThrow(() -> new DataNotFoundException("item " + itemNo + "was not found."));
		itemRepository.delete(item);
		Map<String, Boolean> response = new HashMap<>();
		response.put("item " + itemNo + " deleted", Boolean.TRUE);
		return response;
	}
}
