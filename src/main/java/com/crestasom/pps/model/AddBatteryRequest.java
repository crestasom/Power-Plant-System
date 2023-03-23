package com.crestasom.pps.model;

import java.util.List;

import com.crestasom.pps.dto.BatteryDTO;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class AddBatteryRequest extends RequestBean {
	private List<BatteryDTO> batteryList;
}
