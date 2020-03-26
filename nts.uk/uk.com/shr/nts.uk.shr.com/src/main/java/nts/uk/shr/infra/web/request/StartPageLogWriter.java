package nts.uk.shr.infra.web.request;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nts.arc.bean.SingletonBeansSoftCache;
import nts.gul.text.StringUtil;
import nts.uk.shr.com.context.AppContextsConfig;
import nts.uk.shr.com.context.RequestInfo;
import nts.uk.shr.com.context.ScreenIdentifier;
import nts.uk.shr.com.program.Program;
import nts.uk.shr.com.program.ProgramsManager;
import nts.uk.shr.infra.web.util.FilterConst;
import nts.uk.shr.infra.web.util.FilterHelper;
import nts.uk.shr.infra.web.util.StartPageLogService;

public class StartPageLogWriter implements Filter {

	private static final List<Program> DEFAULT_NOT_LOG = Arrays.asList(ProgramsManager.KAF000B, ProgramsManager.CMM045A);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		writeLog(request);
		
		Cookie cookie = new Cookie(FilterConst.JUMP_FROM_MENU, "");
        cookie.setMaxAge(0);
        ((HttpServletResponse) response).addCookie(cookie);

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	public void writeLog(ServletRequest request) {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestPagePath = httpRequest.getRequestURL().toString();

		ScreenIdentifier target = ScreenIdentifier.create(requestPagePath, httpRequest.getQueryString());

		boolean isStartFromMenu = isStartFromMenu(httpRequest);
		if (!isStartFromMenu) {
			
			String before = getReferered(httpRequest);
			if(before != null){
				String ip = FilterHelper.getClientIp(httpRequest);
				String pcName = FilterHelper.getPcName(ip);
				String webApi = FilterHelper.detectWebapi(before);

				AppContextsConfig.setBeforeRequestedWebAPI(new RequestInfo(before, webApi, ip, pcName));
			}
		}
		String targetPg = target.getProgramId() + target.getScreenId();
		if (DEFAULT_NOT_LOG.stream().filter(c -> c.getPId().equals(targetPg)).findFirst().isPresent()) {
			return;
		}

		StartPageLogService logService = SingletonBeansSoftCache.get(StartPageLogService.class);

		logService.writeLog(target);

	}
	


	private boolean isStartFromMenu(HttpServletRequest httpRequest) {
		if (httpRequest.getCookies() == null) {
			return false;
		}
		return Stream.of(httpRequest.getCookies()).filter(c -> c.getName().equals(FilterConst.JUMP_FROM_MENU))
				.findFirst().isPresent();
	}

	private String getReferered(HttpServletRequest r) {
		String refereredPath = r.getHeader(FilterConst.REFERED_REQUEST);

		if (StringUtil.isNullOrEmpty(refereredPath, true)) {
			return null;
		}

		return refereredPath;
	}

}
