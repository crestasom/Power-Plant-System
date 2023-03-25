package com.crestasom.pps.model;

import java.util.List;

import com.crestasom.pps.dto.BatteryDTO;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class AddBatteryRequest{
	private List<BatteryDTO> batteryList;
}
