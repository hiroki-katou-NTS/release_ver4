package nts.uk.shr.com.context;

import nts.arc.scoped.request.RequestContextProvider;

public final class AppContexts {

	public static LoginUserContext user() {
		return new LoginUserContext();
	}
	
	public static String programId() {
		return RequestContextProvider.get().get(AppContextsConfig.KEY_PROGRAM_ID);
	}
}
