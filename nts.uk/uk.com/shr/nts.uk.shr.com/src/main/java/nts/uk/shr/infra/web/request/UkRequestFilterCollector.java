package nts.uk.shr.infra.web.request;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.ws.preprocess.RequestFilterCollector;
import nts.arc.layer.ws.preprocess.RequestFilterMapping;
import nts.arc.layer.ws.preprocess.filters.RequestPerformanceLogFilter;
import nts.uk.shr.infra.application.auth.WindowsAccountCatcher;
import nts.uk.shr.infra.web.session.BatchRequestProcessor;
import nts.uk.shr.infra.web.session.ScreenLoginSessionValidator;
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
	
	private static final List<RequestFilterMapping> FILTERS = Arrays.asList(
			RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new RequestPerformanceLogFilter()),
			RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new CorsPreflightFilter()),
			RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new SharingSessionFilter()),
			RequestFilterMapping.map(PathPattern.BATCH_WEB_APIS, new BatchRequestProcessor()),
			RequestFilterMapping.map(PathPattern.ALL_WEB_APIS, new ProgramIdDetector()),
			//RequestFilterMapping.map(PathPattern.ALL_SCREENS, new ScreenLoginSessionValidator()),
			RequestFilterMapping.map(PathPattern.ALL_WEB_APIS, new WebApiLoginSessionValidator()),
			RequestFilterMapping.map(PathPattern.LOGIN_SCREENS, new WindowsAccountCatcher()),
			RequestFilterMapping.map(PathPattern.ALL_SCREENS, new StartPageLogWriter()),
//			RequestFilterMapping.map(PathPattern.ALL_WEB_APIS, new CsrfProtectionFilter(PathsNoSession.WEB_APIS)),
		
			// This must be executed last
			// 最後じゃなくても大丈夫かもしれないが、処理内容を考えると、念の為、最後にしておきたい。
			RequestFilterMapping.map(PathPattern.ALL_REQUESTS, new ContextHolderSwitch())
			);

	@Override
	public List<RequestFilterMapping> collect() {
		return FILTERS;
	}

}
