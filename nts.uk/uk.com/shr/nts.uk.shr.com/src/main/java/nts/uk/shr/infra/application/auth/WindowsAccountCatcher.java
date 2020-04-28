package nts.uk.shr.infra.application.auth;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import nts.arc.layer.ws.preprocess.filters.RequestPerformanceDiagnose;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.AppContextsConfig;

/**
 * Get login Windows account.
 */
@Slf4j
public class WindowsAccountCatcher implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request; 
		Principal principal = httpRequest.getUserPrincipal();
		if (principal != null) {
			String account = principal.getName();
			Optional<WindowsAccount> accOpt = getAccountInfo(account);
			accOpt.ifPresent(AppContextsConfig::setWindowsAccount);
			
			/** Write log for window account name */
			log.info("WINDOWS ACCOUNT NAME IS CATCHED: " + account);
			
			// Only for debug
			WindowsAccount ac = AppContexts.windowsAccount();
			if (ac == null) {
				AppContextsConfig.setWindowsAccount(new WindowsAccount(principal.getName(), null));
			} else {
				AppContextsConfig.setWindowsAccount(new WindowsAccount(ac.getDomain() + ";" + principal.getName(), ac.getUserName()));
			}
		}
		
		chain.doFilter(request, response);
	}
	
	/**
	 * Get account information.
	 * @param account account
	 * @return Windows account
	 */
	private Optional<WindowsAccount> getAccountInfo(String account) {
		if (account == null) return Optional.empty();
		String[] infos = account.split("\\\\");
		if (infos.length != 2) return Optional.empty();
		return Optional.of(new WindowsAccount(infos[0], infos[1]));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}
}
