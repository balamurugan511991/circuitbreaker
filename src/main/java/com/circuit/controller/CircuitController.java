package com.circuit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circuit.service.BookService;

@RestController
public class CircuitController {

	@Autowired
	BookService bookService;
	
	
	@GetMapping("/annotation")
	public String annot()
	{
		return bookService.annotation();
	}
	
	
}
