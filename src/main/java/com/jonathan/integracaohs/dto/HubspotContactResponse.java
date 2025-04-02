package com.jonathan.integracaohs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HubspotContactResponse {

	private Long id;
	private ContactDTO properties;
	
}
