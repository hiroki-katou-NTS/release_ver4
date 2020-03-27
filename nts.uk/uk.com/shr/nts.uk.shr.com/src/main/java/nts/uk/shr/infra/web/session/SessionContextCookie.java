package nts.uk.shr.infra.web.session;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerResponseContext;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nts.arc.bean.SingletonBeansSoftCache;
import nts.arc.security.csrf.CsrfToken;
import nts.uk.shr.com.context.loginuser.LoginUserContextManager;
import nts.uk.shr.com.context.loginuser.SessionLowLayer;

/**
 * warファイル間でセッションを擬似的に共有するための仕組み
 * CookieにSessionContextの情報を暗号化して持ち回る
 */
@Slf4j
public class SessionContextCookie {

	private static final String COOKIE_SESSION_CONTEXT = "nts.uk.sescon";
	
	public static Optional<String> getSessionContextFrom(HttpServletRequest httpRequest) {
		
		if (httpRequest.getCookies() == null) {
			return Optional.empty();
		}
		
		return Arrays.asList(httpRequest.getCookies()).stream()
				.filter(c -> c.getName().equals(COOKIE_SESSION_CONTEXT))
				.map(c -> c.getValue())
				.findFirst();
	}

	public static void updateCookie(HttpServletResponse response) {
		
		createNewCookieFromSession().ifPresent(cookie -> {
			response.addHeader("Set-Cookie", cookie);
		});
	}
	
	public static void updateCookie(ContainerResponseContext responseContext) {
		
		createNewCookieFromSession().ifPresent(cookie -> {
			// responseContext.getCookies().put()は使えない。
			// getCookiesの内容は既に確定済みなので、書き換えても反映されないように見える。
			// その代わり、以下のように直接ヘッダを追加することは有効だった。
			
			responseContext.getHeaders().add("Set-Cookie", cookie);
		});
	}

	public static Optional<String> createNewCookieFromSession() {
		
		val session = SingletonBeansSoftCache.get(SessionLowLayer.class);
		if (!session.isLoggedIn()) {
			return Optional.empty();
		}
		
		return Optional.of(buildSetCookieHeaderValue(session.secondsSessionTimeout()));
	}
	
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
	
	
	private static final String DELIMITER = "@";

	private static String createStringSessionContext() {
		String userContext = SingletonBeansSoftCache.get(LoginUserContextManager.class).toBase64();
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
		
		SingletonBeansSoftCache.get(LoginUserContextManager.class).restoreBase64(parts[0]);
		CsrfToken.setToSession(parts[1]);
	}
	
	private static String buildSetCookieHeaderValue(int secondsSessionTimeout) {

		// IE11のバージョンによって、Max-Age属性が機能しないため、Expires属性を使う必要がある。
		// ただ、標準のCookieクラスはsetMaxAgeしかないので、やむを得ずSet-Cookieヘッダを直接書き出している
		
		String expiresFormat = OffsetDateTime.now(ZoneOffset.UTC)
				.plusSeconds(secondsSessionTimeout)
				.format(DateTimeFormatter.ofPattern("EEE, d MMM uuuu kk:mm:ss", Locale.ENGLISH))
				+ " GMT";
		
		return new StringBuilder()
				.append(COOKIE_SESSION_CONTEXT)
				.append("=")
				.append(createStringSessionContext())
				.append("; Path=/; HttpOnly; Expires=")
				.append(expiresFormat)
				.toString();
	}
}
