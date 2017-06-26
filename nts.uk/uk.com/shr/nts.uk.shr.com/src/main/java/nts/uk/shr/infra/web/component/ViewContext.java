package nts.uk.shr.infra.web.component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import nts.arc.i18n.custom.IInternationalization;
import nts.uk.shr.infra.web.component.env.ViewContextEnvWriter;

@FacesComponent(tagName = "viewcontext", createTag = true)
public class ViewContext extends UIComponentBase {

	@Override
	public String getFamily() {
		return this.getClass().getName();
	}

	/**
	 * Render beginning of component
	 * 
	 * @param context
	 *            FacesContext
	 * @throws IOException
	 *             IOException
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		ResponseWriter rw = context.getResponseWriter();
		rw.write("<script type=\"text/javascript\">window.__viewContext = {");

		writeRootPath(context, rw);
		rw.write(",");

		rw.write("};");
		rw.write("__viewContext.primitiveValueConstraints = __viewContext.primitiveValueConstraints || {};");

		CDI.current().select(ViewContextEnvWriter.class).get().write(rw);
		String applicationContextPath = context.getExternalContext().getApplicationContextPath();
		rw.write("</script>");
		rw.write("<script src='/nts.uk.com.web/webapi/loadresource'></script>");

	}

	private static void writeRootPath(FacesContext context, ResponseWriter rw) throws IOException {
		// convert "/hoge/fuga/piyo.xhtml" -> "../../"
		String requestedPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getServletPath();
		String rootPath = requestedPath.replaceAll("[^/]", "").substring(1).replaceAll("/", "../");

		rw.write("rootPath: '" + rootPath + "'");
	}
}
