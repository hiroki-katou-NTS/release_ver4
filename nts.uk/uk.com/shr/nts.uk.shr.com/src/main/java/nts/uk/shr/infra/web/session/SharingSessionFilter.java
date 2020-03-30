package nts.uk.shr.infra.web.session;

import java.io.IOException;
import java.util.Arrays;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.val;
import nts.arc.security.csrf.CsrfToken;
import nts.uk.shr.com.context.loginuser.LoginUserContextManager;
import nts.uk.shr.com.context.loginuser.SessionLowLayer;

public class SharingSessionFilter implements Filter {
	
	private static final String COOKIE_SESSION_CONTEXT = "nts.uk.sescon";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		val sessionLowLayer = CDI.current().select(SessionLowLayer.class).get();
		boolean isLoggedIn = sessionLowLayer.isLoggedIn();
		val httpRequest = (HttpServletRequest) request;

		/*
		 * Cookieでセッション情報がやってきて、
		 * 1.かつ、ログインセッション情報が無い場合
		 *   -> 主にcom.webからat.webへの初回の移動とかでありえる
		 * 
		 * 2.かつ、ログインセッション情報があり、それと一致しない場合
		 *   -> 主にcom.webからat.webへの移動前に会社切り替えとかやった場合
		 * 
		 * 上記1,2いずれの場合も、Cookieの情報が正しいとみなして、サーバ側のセッションを書き換える。
		 * ログインしているのにCookieがやってこないケースは無いと思われるが、もしあったとしてもサーバ上のセッション情報でそのまま動作すれば良い
		 */
		if (httpRequest.getCookies() != null) {
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
		
		chain.doFilter(request, response);
		
		// サーバ側の処理でセッション情報が変化しているかどうかに関わらず、Cookieの情報を最新化しておく。
		if (CDI.current().select(SessionLowLayer.class).get().isLoggedIn()) {
			val httpResponse = (HttpServletResponse) response;
			val newSessionContextCookie = new Cookie(COOKIE_SESSION_CONTEXT, createStringSessionContext());
			newSessionContextCookie.setPath("/");
			newSessionContextCookie.setMaxAge(sessionLowLayer.secondsSessionTimeout());
			httpResponse.addCookie(newSessionContextCookie);
		}
	}

	@Override
	public void destroy() {
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
			return;
		}
		CDI.current().select(LoginUserContextManager.class).get().restoreBase64(parts[0]);
		CsrfToken.setToSession(parts[1]);
	}
}
