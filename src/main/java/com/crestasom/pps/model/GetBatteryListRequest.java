package com.crestasom.pps.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetBatteryListRequest{
	private Integer postCodeStart;
	private Integer postCodeEnd;
}
