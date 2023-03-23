package com.crestasom.pps.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class BatteryDTO {
	private String name;
	private int postCode;
	private int capacity;
}
