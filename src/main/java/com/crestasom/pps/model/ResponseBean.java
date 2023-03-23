package com.crestasom.pps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseBean {

	protected String reqId;
	protected Integer respCode;
	protected String respDesc;
}
