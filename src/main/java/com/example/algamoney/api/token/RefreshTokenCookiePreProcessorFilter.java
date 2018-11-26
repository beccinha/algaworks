package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {
//a ideia da classe é para poder n ter q ficar exigindo o codigo do refreshToken e sim o proprio cookie 
	//vai poder fazer o refresh
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI())){
			if("refresh_token".equals(req.getParameter("grant_type"))){
				if(req.getCookies() != null) {
					for(Cookie cookie : req.getCookies()) {
						if(cookie.getName().equals("refreshToken")) {
							String refreshToken = cookie.getValue();
							req = new MyServletRequestWrapper(req, refreshToken);
						}
					}
				}
			}
		}
		chain.doFilter(req, response);
	}

	@Override
	public void destroy() {
		
	}
	
	static class MyServletRequestWrapper extends HttpServletRequestWrapper{
		//criou uma class a parte para poder utilizar o refresh_token
		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
			
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true);
			
			return map;
		}
	}
	
	
}
