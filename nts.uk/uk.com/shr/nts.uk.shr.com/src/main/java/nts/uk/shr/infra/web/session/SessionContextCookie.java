package nts.uk.shr.infra.web.session;

import java.util.Arrays;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nts.arc.security.csrf.CsrfToken;
import nts.uk.shr.com.context.loginuser.LoginUserContextManager;

@Slf4j
public class SessionContextCookie {

	private static final String COOKIE_SESSION_CONTEXT = "nts.uk.sescon";
	
	public static void restoreSessionFromCookie(HttpServletRequest httpRequest, boolean isLoggedIn) {

		Arrays.asList(httpRequest.getCookies()).stream()
				.filter(c -> c.getName().equals(COOKIE_SESSION_CONTEXT))
				.map(c -> c.getValue())
				.findFirst()
				.ifPresent(sessionContext -> {
					if (!isLoggedIn || !sessionContext.equals(createStringSessionContext())) {
						restoreSessionContext(sessionContext);
					}
				});
	}
	
	public static void setCookieFromSession(HttpServletResponse httpResponse) {
		
		val newSessionContextCookie = new Cookie(COOKIE_SESSION_CONTEXT, createStringSessionContext());
		newSessionContextCookie.setPath("/");
		httpResponse.addCookie(newSessionContextCookie);
	}
	
	private static final String DELIMITER = "@";

	private static String createStringSessionContext() {
		String userContext = CDI.current().select(LoginUserContextManager.class).get().toBase64();
		String csrfToken = CsrfToken.getFromSession();
		
		// '='はCookieに含めると誤作動を起こすようなので、置換しておく
		return (userContext + DELIMITER + csrfToken).replace('=', '*');
	}
	
	private static void restoreSessionContext(String sessionContextInCookie) {
		val parts = sessionContextInCookie.replace('*', '=').split(DELIMITER);
		if (parts.length != 2) {
			log.error("Invalid session context cookie: " + sessionContextInCookie);
			return;
		}
		
		CDI.current().select(LoginUserContextManager.class).get().restoreBase64(parts[0]);
		CsrfToken.setToSession(parts[1]);
	}
}
