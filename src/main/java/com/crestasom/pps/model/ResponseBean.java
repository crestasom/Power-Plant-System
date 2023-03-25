package com.crestasom.pps.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseBean {

	protected String reqId;
	protected Integer respCode;
	protected String respDesc;
}
