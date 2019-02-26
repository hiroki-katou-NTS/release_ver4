package nts.uk.shr.infra.web.session;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nts.arc.layer.ws.ProducedRequest;
import nts.arc.security.csrf.CsrfToken;
import nts.uk.shr.com.context.loginuser.SessionLowLayer;

@Stateless
public class ServletSessionLowLayer implements SessionLowLayer {

	private static final String LOGGED_IN_FLAG = "ServletSessionLowLayer.LOGGED_IN_FLAG";
	
	@Inject @ProducedRequest
	private HttpServletRequest request;

	@Override
	public void loggedIn() {
		this.getSession().ifPresent(s -> s.setAttribute(LOGGED_IN_FLAG, true));
		CsrfToken.loggedIn();
	}

	@Override
	public void loggedOut() {
		this.getSession().ifPresent(s -> {
			s.removeAttribute(LOGGED_IN_FLAG);
			s.invalidate();
		});
	}
	
	@Override
	public boolean isLoggedIn() {
		Object flag = this.getSession().map(s -> s.getAttribute(LOGGED_IN_FLAG)).orElse(null);
		return flag != null && (boolean)flag == true;
	}
	
	private Optional<HttpSession> getSession() {
		return Optional.ofNullable(this.request.getSession(false));
	}

	@Override
	public int secondsSessionTimeout() {
		return this.request.getSession(true).getMaxInactiveInterval();
	}
}
