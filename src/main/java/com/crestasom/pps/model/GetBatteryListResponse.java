package com.crestasom.pps.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetBatteryListResponse extends ResponseBean {
	private List<String> batteryNames;
	private Integer totalBatteryCapacity;
	private Double averageBatteryCapacity;
}
