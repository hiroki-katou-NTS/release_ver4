package nts.uk.shr.infra.web.session;

import java.util.function.Function;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import nts.arc.bean.SingletonBeansSoftCache;
import nts.uk.shr.com.context.loginuser.SessionLowLayer;
import nts.uk.shr.com.program.nosession.PathsNoSession;

public class ServletPathsNoSession {

	private final PathsNoSession paths;
	private final Function<HttpServletRequest, String> getRequestedPath;
	
	public ServletPathsNoSession(PathsNoSession paths, Function<HttpServletRequest, String> getRequestedPath) {
		this.paths = paths;
		this.getRequestedPath = getRequestedPath;
	}
	
    /**
     * validate request session.
     * @param request ServletRequest
     */
    public boolean validate(ServletRequest request) {

        // only work with HttpServletRequest objects
        if (!(request instanceof HttpServletRequest)) {
            throw new IllegalArgumentException("HttpServlet object is required to validate session.");
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestedPath = this.getRequestedPath.apply(httpRequest).toLowerCase();
        
        return !paths.sessionRequired(requestedPath)
                || SingletonBeansSoftCache.get(SessionLowLayer.class).isLoggedIn();
    }


}
