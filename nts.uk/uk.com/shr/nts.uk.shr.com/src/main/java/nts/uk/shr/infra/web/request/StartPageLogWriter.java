package nts.uk.shr.infra.web.request;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.gul.text.StringUtil;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.context.RequestInfo;
import nts.uk.shr.com.context.ScreenIdentifier;
import nts.uk.shr.com.context.loginuser.role.DefaultLoginUserRoles;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;
import nts.uk.shr.com.security.audittrail.basic.LoginInformation;
import nts.uk.shr.com.security.audittrail.correction.content.UserInfo;
import nts.uk.shr.com.security.audittrail.start.StartPageLog;
import nts.uk.shr.com.security.audittrail.start.StartPageLogStorageRepository;
import nts.uk.shr.com.user.UserInfoAdapter;
import nts.uk.shr.infra.application.auth.WindowsAccount;
import nts.uk.shr.infra.web.util.FilterConst;
import nts.uk.shr.infra.web.util.FilterHelper;
import nts.uk.shr.infra.web.util.MenuRequestContainer;
import nts.uk.shr.infra.web.util.QueryStringAnalyzer;
import nts.uk.shr.infra.web.util.data.MenuRequestInfo;

public class StartPageLogWriter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		writeLog(request);
		
		chain.doFilter(request, response);
	}

	private void writeLog(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		LoginUserContext context = AppContexts.user();
		RequestInfo requseted = AppContexts.requestedWebApi();
		WindowsAccount windowsAccount = AppContexts.windowsAccount();
		String requestPagePath = httpRequest.getRequestURL().toString();
		
		boolean requestedFromMenu = MenuRequestContainer.requestedWith(new MenuRequestInfo(
																					requseted.getRequestIpAddress(), 
																					GeneralDateTime.now(), 	
																					requseted.getFullRequestPath()));
		
		ScreenIdentifier targetPg = screenIdentify(requestPagePath, httpRequest.getQueryString());
		
		if(StringUtil.isNullOrEmpty(targetPg.getProgramId(), true)){
			return;
		}
		
		LogBasicInformation basic = new LogBasicInformation(
				IdentifierUtil.randomUniqueId(), 
				getValue(context, c -> c.companyId()),
				UserInfo.employee(
						getValue(context, c -> c.userId()), 
						getValue(context, c -> c.employeeId()),
						getValue(context, c -> {
							UserInfoAdapter userAdapter = CDI.current().select(UserInfoAdapter.class).get();
							return userAdapter.getUserName(c.userId());
						})), 
				new LoginInformation(
						getValue(requseted, c -> c.getRequestIpAddress()),
						getValue(requseted, c -> c.getRequestPcName()), 
						getValue(windowsAccount, c -> c.getUserName())),
				GeneralDateTime.now(), 
				getValue(context, c -> {
					return getValue(c.roles(), role -> DefaultLoginUserRoles.cloneFrom(role));
				}), targetPg, Optional.empty());
		
		saveLog(initLog(httpRequest, basic, requestedFromMenu));
	}

	private StartPageLog initLog(HttpServletRequest httpRequest, LogBasicInformation basic, boolean requestedFromMenu) {
		
		if(requestedFromMenu){
			return StartPageLog.specialStarted(basic);
		}
		
		return StartPageLog.pageStarted(getReferered(httpRequest), basic);
	}

	@Override
	public void destroy() {
	}

	private void saveLog(StartPageLog log) {
		StartPageLogStorageRepository logStorage = CDI.current().select(StartPageLogStorageRepository.class).get();
		
		logStorage.save(log);
	}

	private ScreenIdentifier getReferered(HttpServletRequest r) {
		String refereredPath = r.getHeader(FilterConst.REFERED_REQUEST);
		
		if(StringUtil.isNullOrEmpty(refereredPath, true)){
			return null;
		}
		
		return screenIdentify(refereredPath, null);
	}
	
	private ScreenIdentifier screenIdentify(String path, String defaultQueryString){
		String programId = FilterHelper.detectProgram(path).orElse("");
		String pId = StringUtil.isNullOrEmpty(programId, true) ? "" : programId.substring(0, 6);
		String sId = programId.replace(pId, "");
		return new ScreenIdentifier(pId, sId, 
				defaultQueryString == null ? getQueryStringFrom(path) : defaultQueryString);
	}
	
	private String getQueryStringFrom(String query){
		String[] qs = query.split(FilterConst.QUERY_STRING_SEPARATOR);
		QueryStringAnalyzer analyzer = new QueryStringAnalyzer(qs.length == 2 ? qs[1] : "");
		return analyzer.buildQueryExclude(FilterConst.MENU_FLAG);
	}
	
	private <U, T> T getValue(U source, Function<U, T> getter){
		if(source != null){
			return getter.apply(source);
		}
		
		return null;
	}

}

