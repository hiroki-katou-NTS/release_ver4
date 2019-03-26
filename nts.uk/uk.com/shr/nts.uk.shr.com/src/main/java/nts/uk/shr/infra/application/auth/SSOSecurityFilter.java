package nts.uk.shr.infra.application.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import waffle.servlet.NegotiateSecurityFilter;

/**
 * SSO security filter.
 *
 */
public class SSOSecurityFilter extends NegotiateSecurityFilter {
	
	/**
	 * Sign on query string
	 */
	private static final List<String> SIGN_ON = Arrays.asList("signon=on","signon=ON","signon=On","signon=oN"); 

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String queryString = httpRequest.getQueryString();
		if (queryString != null && SIGN_ON.contains(queryString)) {
			super.doFilter(request, response, chain);
		}
		chain.doFilter(request, response);
	}
	
}
