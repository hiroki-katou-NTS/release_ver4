package nts.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsRequestCacheHandler implements Filter {

	static final Pattern JS_REQUEST_PATTERN = Pattern.compile(".*\\/lib\\/.*\\.js.*"); 
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		if (JS_REQUEST_PATTERN.matcher(httpRequest.getRequestURL()).find()) {
			
			httpResponse.addHeader("Cache-Control", "no-store");
		}
		chain.doFilter(request, httpResponse);
	}

	@Override
	public void destroy() {
	}
	
}
