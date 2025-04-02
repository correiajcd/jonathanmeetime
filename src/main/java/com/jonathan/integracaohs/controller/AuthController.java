package com.jonathan.integracaohs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.integracaohs.service.AuthService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@RequestMapping(value="/url", method=RequestMethod.GET)
	public ResponseEntity<String> createAuthUrl(){
		return ResponseEntity.ok().body(authService.createAuthUrl());
	}
	
	@RequestMapping(value="/callback", method=RequestMethod.GET)
	public ResponseEntity<String> processCallbackCode(@RequestParam("code") String code){
		return ResponseEntity.ok().body(authService.processCallbackCode(code));
	}
	
}
