package nts.uk.shr.infra.web.request;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.ws.preprocess.RequestFilterCollector;
import nts.arc.layer.ws.preprocess.RequestFilterMapping;
import nts.arc.layer.ws.preprocess.filters.RequestPerformanceLogFilter;
import nts.arc.layer.ws.preprocess.filters.RequestPerformancePoolFilter;
import nts.arc.system.ServerSystemProperties;
import nts.uk.shr.infra.application.auth.WindowsAccountCatcher;
import nts.uk.shr.infra.web.session.BatchRequestProcessor;
import nts.uk.shr.infra.web.session.SharingSessionFilter;
import nts.uk.shr.infra.web.session.WebApiLoginSessionValidator;

@Stateless
public class UkRequestFilterCollector implements RequestFilterCollector {
	
	private static class PathPattern {
		static final String ALL_REQUESTS = ".*";
		static final String ALL_SCREENS = ".*\\.xhtml.*";
		static final String ALL_WEB_APIS = ".*/webapi/.*";
		static final String BATCH_WEB_APIS = ".*/webapi/batch/.*";
		static final String LOGIN_SCREENS = ".*/view/ccg/007/.*";
	}
	
	private static final List<RequestFilterMapping> FILTERS = new ArrayList<>();
	
	static {
			FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new RequestPerformanceLogFilter()));
			FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new CorsPreflightFilter()));
			FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new SharingSessionFilter()));
			FILTERS.add(RequestFilterMapping.map(PathPattern.BATCH_WEB_APIS, new BatchRequestProcessor()));
			FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_WEB_APIS, new ProgramIdDetector()));
			
			if (ServerSystemProperties.logMode()) {
				FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new RequestPerformancePoolFilter()));
			}
			
			//RequestFilterMapping.map(PathPattern.ALL_SCREENS, new ScreenLoginSessionValidator()),
			FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_WEB_APIS, new WebApiLoginSessionValidator()));
			FILTERS.add(RequestFilterMapping.map(PathPattern.LOGIN_SCREENS, new WindowsAccountCatcher()));
			FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_SCREENS, new StartPageLogWriter()));
//			RequestFilterMapping.map(PathPattern.ALL_WEB_APIS, new CsrfProtectionFilter(PathsNoSession.WEB_APIS)),
		
			// This must be executed last
			// 最後じゃなくても大丈夫かもしれないが、処理内容を考えると、念の為、最後にしておきたい。
			FILTERS.add(RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new ContextHolderSwitch()));
	}

	@Override
	public List<RequestFilterMapping> collect() {
		return FILTERS;
	}

}
