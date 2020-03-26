package nts.uk.shr.infra.web.util;

import java.util.Optional;
import java.util.function.Function;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.bean.SingletonBeansSoftCache;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.gul.text.StringUtil;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.context.RequestInfo;
import nts.uk.shr.com.context.ScreenIdentifier;
import nts.uk.shr.com.context.loginuser.role.DefaultLoginUserRoles;
import nts.uk.shr.com.menu.ShareStandardMenuAdapter;
import nts.uk.shr.com.security.audittrail.UserInfoAdaptorForLog;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;
import nts.uk.shr.com.security.audittrail.basic.LoginInformation;
import nts.uk.shr.com.security.audittrail.correction.content.UserInfo;
import nts.uk.shr.com.security.audittrail.start.StartPageLog;
import nts.uk.shr.com.security.audittrail.start.StartPageLogStorageRepository;
import nts.uk.shr.infra.application.auth.WindowsAccount;

@Stateless
public class StartPageLogService {

	@Inject
	private ShareStandardMenuAdapter menuAdapter;
	
	@Inject
	private StartPageLogStorageRepository logFactory;
	
	public void writeLog(String url) {
		
		writeLog(ScreenIdentifier.create(url));
	}
	
	public void writeLog(ScreenIdentifier targetPg) {
		LoginUserContext context = AppContexts.user();
		RequestInfo requseted = AppContexts.requestedWebApi();
		RequestInfo beforeRequseted = AppContexts.beforeRequestedWebApi();
		WindowsAccount windowsAccount = AppContexts.windowsAccount();
		
		if(StringUtil.isNullOrEmpty(targetPg.getProgramId(), true) || FilterHelper.isLoginPage(targetPg)){
			return;
		}
		
		String comId = getValue(context, c -> c.companyId());
		
		if(!getMenuAdapter().isEsistMenuWith(comId, targetPg.getScreenId(), targetPg.getProgramId(), targetPg.getQueryString())){
			return;
		}
		
		LogBasicInformation basic = new LogBasicInformation(
				IdentifierUtil.randomUniqueId(), 
				comId,
				UserInfo.employee(
						getValue(context, c -> c.userId()), 
						getValue(context, c -> {
							if(c.employeeId() == null){
								return c.userId();
							}
							return c.employeeId();
						}),
						getValue(context, c -> {
							UserInfoAdaptorForLog userAdapter = SingletonBeansSoftCache.get(UserInfoAdaptorForLog.class);
							if(context.isEmployee()){
								return userAdapter.findByEmployeeId(c.employeeId()).getUserName();
							}
							return userAdapter.findByUserId(c.userId()).getUserName();
						})), 
				new LoginInformation(
						getValue(requseted, c -> c.getRequestIpAddress()),
						getValue(requseted, c -> c.getRequestPcName()), 
						getValue(windowsAccount, c -> c.getUserName())),
				GeneralDateTime.now(), 
				getValue(context, c -> {
					return getValue(c.roles(), role -> DefaultLoginUserRoles.cloneFrom(role));
				}), targetPg, Optional.empty());
		
		getLogStorage().save(initLog(basic, getValue(beforeRequseted, c -> c.getFullRequestPath())));
	}
	
	private ShareStandardMenuAdapter getMenuAdapter(){
		if(menuAdapter != null){
			return menuAdapter;
		}
		
		return SingletonBeansSoftCache.get(ShareStandardMenuAdapter.class);
	}
	
	private StartPageLogStorageRepository getLogStorage(){
		if(logFactory != null){
			return logFactory;
		}
		
		return SingletonBeansSoftCache.get(StartPageLogStorageRepository.class);
	}

	private StartPageLog initLog(LogBasicInformation basic, String beforeUrl) {

		if (beforeUrl == null) {
			return StartPageLog.specialStarted(basic);
		}

		return StartPageLog.pageStarted(getReferered(beforeUrl), basic);
	}

	private ScreenIdentifier getReferered(String beforeUrl) {

		if (StringUtil.isNullOrEmpty(beforeUrl, true)) {
			return null;
		}

		return ScreenIdentifier.create(beforeUrl);
	}

	private <U, T> T getValue(U source, Function<U, T> getter) {
		if (source != null) {
			return getter.apply(source);
		}

		return null;
	}
}
