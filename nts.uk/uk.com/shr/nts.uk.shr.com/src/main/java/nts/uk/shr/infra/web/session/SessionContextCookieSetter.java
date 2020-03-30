package nts.uk.shr.infra.web.session;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class SessionContextCookieSetter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		SessionContextCookie.createNewCookieFromSession().ifPresent(cookie -> {
			// responseContext.getCookies().put()は使えない。
			// getCookiesの内容は既に確定済みなので、書き換えても反映されないように見える。
			// その代わり、以下のように直接ヘッダを追加することは有効だった。
			responseContext.getHeaders().add("Set-Cookie", cookie);
		});
	}

}
