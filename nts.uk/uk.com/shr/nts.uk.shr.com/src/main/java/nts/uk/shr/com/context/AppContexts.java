package nts.uk.shr.com.context;

import lombok.val;
import nts.arc.bean.SingletonBeansSoftCache;
import nts.arc.scoped.request.RequestContextProvider;
import nts.arc.scoped.session.SessionContextProvider;
import nts.uk.shr.com.context.loginuser.NullLoginUserContext;
import nts.uk.shr.infra.application.auth.WindowsAccount;
import nts.uk.shr.com.system.config.SystemConfiguration;

public final class AppContexts {

	public static LoginUserContext user() {
		val context = SessionContextProvider.get().get(LoginUserContext.KEY_SESSION_SCOPED);
		if (context == null) {
			return new NullLoginUserContext();
		} else {
			return (LoginUserContext) context;
		}
	}
	
	public static String programId() {
		return RequestContextProvider.get().get(AppContextsConfig.KEY_PROGRAM_ID);
	}
	
	public static RequestInfo requestedWebApi() {
		return RequestContextProvider.get().get(AppContextsConfig.REQUEST_WEBAPI);
	}
	
	public static RequestInfo beforeRequestedWebApi() {
		return RequestContextProvider.get().get(AppContextsConfig.BEFORE_REQUEST_WEBAPI);
	}

	public static WindowsAccount windowsAccount() {
		WindowsAccount account = SessionContextProvider.get().get(AppContextsConfig.WINS_ACCOUNT);
		if (account == null) {
			return new WindowsAccount("", "");
		} else {
			return account;
		}
	}	
	public static SystemConfiguration system() {
		return SingletonBeansSoftCache.get(SystemConfiguration.class);
	}
}
