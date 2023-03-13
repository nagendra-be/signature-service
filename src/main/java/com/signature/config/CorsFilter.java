package com.signature.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;

		String origin = request.getHeader("origin");

		if (!this.validateOrigin(origin, request.getRequestURI())) {
			throw new RuntimeException("Invalid Origin : " + origin);
		}

		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

		chain.doFilter(req, res);
	}

	private Boolean validateOrigin(String origin, String requestUri) {

		boolean isValidOrigin = StringUtils.isEmpty(origin)
				|| (origin.matches(".*localhost.*|.*127.0.0.1.*") && this.isValidUrl(origin));
		if (StringUtils.isNotEmpty(requestUri) && isValidOrigin) {
			return Boolean.TRUE;
		} else if (isValidOrigin) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Boolean isValidUrl(String origin) {
		if (!StringUtils.isEmpty(origin)) {
			Matcher matcher = Pattern.compile("^(http|https):\\/\\/[^\\s\\/$.?#].[^\\s]*$\r\n").matcher(origin);
			return matcher.matches();
		}
		return Boolean.FALSE;
	}
}
