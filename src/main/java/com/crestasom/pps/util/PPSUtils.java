package com.crestasom.pps.util;

import java.util.UUID;

public class PPSUtils {
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
}
