package nts.uk.shr.infra.i18n.resource.web.webapi;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lombok.val;
import nts.arc.bean.SingletonBeansSoftCache;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.com.constants.DefaultSettingKeys;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.LanguageConsts;
import nts.uk.shr.infra.i18n.resource.I18NResourceType;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;
import nts.uk.shr.infra.i18n.resource.container.I18NResourcesRepository;

@Path("i18n/resources")
@Produces("text/javascript")
public class I18NResourcesWebService {
	
	public static String VERSION = GeneralDateTime.now().toString("yyyyMMddHHmmss");
	
	private static final int SECONDS_IN_YEAR = 365 * 24 * 60 * 60;

	@Inject
	private I18NResourcesForUK i18n;
	
	@Inject
	private I18NResourcesRepository resourceRepository;
	
	@POST
	@Path("rawcontent/{resourceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public JavaTypeResult<String> getRawContent(@PathParam("resourceId") String resourceId){
		return new JavaTypeResult<String>(i18n.getRawContent(resourceId).orElse(resourceId));
	}
	
	public static String getHtmlToLoadResources() {
		I18NResourcesForUK i18n = SingletonBeansSoftCache.get(I18NResourcesForUK.class);

		String companyId = DefaultSettingKeys.COMPANY_ID;
		String languageId = LanguageConsts.DEFAULT_LANGUAGE_ID;
		if (AppContexts.user().hasLoggedIn()) {
			companyId = AppContexts.user().companyId();
			languageId = AppContexts.user().language().basicLanguageId();
		}
		String systemId = "COM";
		String version = i18n.getVersionOfCurrentCompany();
		String companyVersion = createEtagString(companyId, languageId, systemId, version);
		return "<script src=\"/nts.uk.com.web/webapi/i18n/resources/screen?v=" + companyVersion + "\"></script>";
	}
	
	@POST
	@Path("refresh/default")
	public void refreshDefaultResource() {
		this.i18n.refreshDefaultResource();
	}
	
	@POST
	@Path("refresh/currentcompany")
	public void refreshCurrentCompany() {
		val user = AppContexts.user();
		this.resourceRepository.refreshResource(user.companyId(), user.language().basicLanguageId(), GeneralDateTime.now());
	}
	
	@GET
	@Path("screen")
	public Response getResourceForScreen(@Context Request request) {

		String companyId = DefaultSettingKeys.COMPANY_ID;
		String languageId = LanguageConsts.DEFAULT_LANGUAGE_ID;
		
		if (AppContexts.user().hasLoggedIn()) {
			companyId = AppContexts.user().companyId();
			languageId = AppContexts.user().language().basicLanguageId();
		}
		
		String systemId = "COM";
		String version = this.i18n.getVersionOfCurrentCompany();
		
		EntityTag eTag = createEtag(companyId, languageId, systemId, version);
		ResponseBuilder responseBuilder = request.evaluatePreconditions(eTag);
		if (responseBuilder == null) {
			responseBuilder = Response.ok(initialResource(languageId, companyId));
			responseBuilder.tag(eTag);
		}

		responseBuilder.cacheControl(createCacheControl());

		return responseBuilder.build();
	}

	private String initialResource(String languageId, String companyId) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("var systemLanguage='");
		builder.append(languageId);
		builder.append("';");
		
		builder.append("var names=");
		builder.append(createJsObject(this.i18n.loadForUserByResourceType(languageId, companyId, I18NResourceType.ITEM_NAME)));
		builder.append(";");
		
		builder.append("var messages=");
		builder.append(createJsObject(this.i18n.loadForUserByResourceType(languageId, companyId, I18NResourceType.MESSAGE)));
		builder.append(";");
		
		return builder.toString();
	}

	private static String createJsObject(Map<String, String> resource) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");

		builder.append(resource.entrySet().stream()
				.map(e -> createJsProperty(e))
				.collect(Collectors.joining(",")));

		builder.append("}");
		return builder.toString();
	}
	
	private static String createJsProperty(Map.Entry<String, String> e) {
		String value = e.getValue()
				.replace(System.getProperty("line.separator"), "\\r\\n")
				.replace("\"", "\\\"");
		
		return e.getKey() + ":\"" + value + "\"";
	}

	private static CacheControl createCacheControl() {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setNoCache(true);
		cacheControl.setMaxAge(SECONDS_IN_YEAR);
		cacheControl.setPrivate(false);
		return cacheControl;
	}
	
	private static EntityTag createEtag(String companyId, String languageId, String systemId, String version) {
		// tag's format companyId_languageId_systemId_version
		return new EntityTag(createEtagString(companyId, languageId, systemId, version));
	}
	
	private static String createEtagString(String companyId, String languageId, String systemId, String version) {
		StringBuilder builder = new StringBuilder();
		builder.append(companyId);
		builder.append("_");
		builder.append(languageId);
		builder.append("_");
		builder.append(systemId);
		builder.append("_");
		builder.append(version);
		return builder.toString();
	}
}
