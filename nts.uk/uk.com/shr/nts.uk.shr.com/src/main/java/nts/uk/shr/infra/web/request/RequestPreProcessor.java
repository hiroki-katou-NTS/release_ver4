package nts.uk.shr.infra.web.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import nts.uk.shr.com.context.AppContextsConfig;
import nts.uk.shr.com.program.ProgramsManager;
import nts.uk.shr.com.program.WebAppId;

/**
 * RequestPreProcessor
 * 
 */
public class RequestPreProcessor implements Filter {
	
	/**
	 * Program path header.
	 */
	private static final String PG_PATH = "PG-Path";
	
	/**
	 * Web app map.
	 */
	@SuppressWarnings("serial")
	private final Map<WebAppId, String> webApps = new HashMap<WebAppId, String>(){
		{
			put(WebAppId.COM, "nts.uk.com.web");
			put(WebAppId.PR, "nts.uk.pr.web");
			put(WebAppId.AT, "nts.uk.at.web");
		}
	};

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter
	 * #doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute(InputPart.DEFAULT_CHARSET_PROPERTY,"ISO-8859-1");
		String requestPagePath = ((HttpServletRequest) request).getHeader(PG_PATH);
		if (requestPagePath == null) {
			chain.doFilter(request, response);
			return;
		}
		Iterator<Entry<WebAppId, String>> iterator = webApps.entrySet().iterator();
		Entry<WebAppId, String> entry = null;
		int startIndex = -1;
		String partialPath = null;
		WebAppId appId = null;
		
		while (iterator.hasNext()) {
			entry = iterator.next();
			startIndex = requestPagePath.indexOf(entry.getValue());
			if (startIndex != -1) {
				 partialPath = requestPagePath.substring(startIndex + entry.getValue().length());
				 appId = entry.getKey();
				 break;
			}
		}
		ProgramsManager.idOf(appId, partialPath).ifPresent(id -> AppContextsConfig.setProgramId(id));
		chain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}
