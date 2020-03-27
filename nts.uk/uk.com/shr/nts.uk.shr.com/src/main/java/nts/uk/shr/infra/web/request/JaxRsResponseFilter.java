package nts.uk.shr.infra.web.request;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import nts.uk.shr.infra.web.session.SessionContextCookie;

/**
 * JAX-RSのResponse用のフィルタ
 * サーブレットフィルタの場合、doFilterの後でレスポンスヘッダを書き換えることができないので、そういう用途の場合はこちらを利用する
 */
@Provider
public class JaxRsResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		// セッション共有
		// SharingSessionFilterではWebAPIのレスポンスヘッダへのCookie書き込みができないのでここで処理
		SessionContextCookie.updateCookie(responseContext);
	}

}
