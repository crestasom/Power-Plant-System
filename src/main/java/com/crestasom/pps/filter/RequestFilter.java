package com.crestasom.pps.filter;

import java.io.IOException;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;

import com.crestasom.pps.util.PPSUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class RequestFilter implements Filter {
	/**
	 * to put the req id for logging purpose
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String reqId = req.getParameter("reqId");
		MDC.put("reqId", reqId.isBlank() ? PPSUtils.getUUID() : reqId);
		chain.doFilter(request, response);
	}
}