package com.crestasom.pps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class GetBatteryListRequest extends RequestBean {
	private Integer postCodeStart;
	private Integer postCodeEnd;
}
