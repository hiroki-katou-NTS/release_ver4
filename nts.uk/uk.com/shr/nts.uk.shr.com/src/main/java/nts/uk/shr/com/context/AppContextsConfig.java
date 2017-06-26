package nts.uk.shr.com.context;

import nts.arc.scoped.request.RequestContextProvider;

public class AppContextsConfig {
	
	public static final String KEY_PROGRAM_ID = "programId";

	public static void setProgramId(String programId) {
		RequestContextProvider.get().put(KEY_PROGRAM_ID, programId);
	}
}
