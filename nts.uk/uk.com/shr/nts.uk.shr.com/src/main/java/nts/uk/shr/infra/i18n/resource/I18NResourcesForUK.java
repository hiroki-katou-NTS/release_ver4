package nts.uk.shr.infra.i18n.resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nts.arc.i18n.I18NResources;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.com.constants.DefaultSettingKeys;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.LanguageConsts;
import nts.uk.shr.com.i18n.resource.I18NResourceCustomizer;
import nts.uk.shr.com.system.config.InitializeWhenDeploy;
import nts.uk.shr.infra.i18n.loading.LanguageMasterRepository;
import nts.uk.shr.infra.i18n.resource.container.I18NResourcesRepository;

@ApplicationScoped
@Slf4j
public class I18NResourcesForUK implements I18NResources, I18NResourceCustomizer, InitializeWhenDeploy {

	@Inject
	private I18NResourcesRepository resourcesRepository;
	
	@Inject
	private LanguageMasterRepository languageRepository;
	
	private I18NResourcesDefault defaultResources = new I18NResourcesDefault();

	private I18NResourcesCustomized customizedResources = new I18NResourcesCustomized();
	
	private GeneralDateTime SERVER_START_TIME = null;
	
	private I18NResourceContentProcessor contentProcessor = new I18NResourceContentProcessor(
			id -> this.localize(id).orElse(id));
	
	@Override
	public void initialize() {
		log.info("[INIT START] nts.uk.shr.infra.i18n.resource.I18NResourcesForUK");
		
		this.languageRepository.getSystemLanguages().stream()
				.map(l -> l.getLanguageId())
				.forEach(languageId -> {
					this.defaultResources.put(
							languageId, this.resourcesRepository.loadResourcesDefault(languageId));
					this.customizedResources.put(
							languageId, this.resourcesRepository.loadResourcesEachCompanies(languageId));
				});
		this.SERVER_START_TIME = GeneralDateTime.now();
		log.info("[INIT END] nts.uk.shr.infra.i18n.resource.I18NResourcesForUK");
	}

	@Override
	public Optional<String> getRawContent(String resourceId) {
		String languageId = LanguageConsts.DEFAULT_LANGUAGE_ID;

		if (AppContexts.user().hasLoggedIn()) {
			languageId = AppContexts.user().language().basicLanguageId();
			String companyId = AppContexts.user().companyId();
			
			//this.refreshIfRequired(languageId, companyId);
			
			val customizedOptional = this.customizedResources.getContent(companyId, languageId, resourceId);
			if (customizedOptional.isPresent()) {
				return customizedOptional;
			}
		}
		
		return this.defaultResources.getContent(languageId, resourceId);
	}

	@Override
	public Optional<String> localize(String resourceId, List<String> params) {
		return this.getRawContent(resourceId)
				.map(content -> this.contentProcessor.process(LanguageConsts.DEFAULT_LANGUAGE_ID, content, params));
	}

	@Override
	public void replaceSystemClass(String resourceId, String newContent) {
		
		val context = CompanyAndLanguage.createAsLogin();
		
		this.resourcesRepository.replaceSystemClass(
				context.companyId,
				context.languageId,
				"C", resourceId, newContent);
		
		this.resourcesRepository.refreshResource(context.companyId, context.languageId, GeneralDateTime.now());
	}
	
	public String getVersionOfCurrentCompany() {
		
		val context = CompanyAndLanguage.createAsLogin();
		GeneralDateTime lastUpdated = this.resourcesRepository.getLastUpdatedDateTime(context.companyId, context.languageId).orElse(null);
		
		if (lastUpdated == null || lastUpdated.before(SERVER_START_TIME)) {
			lastUpdated = SERVER_START_TIME;
		} 
		
		if(lastUpdated == null){
			return "0";
		}
		
		return lastUpdated.toString("yyyyMMdd_hhmmss");
	}
	
	public Map<String, String> loadForUserByClassId(String classId) {
		
		val context = CompanyAndLanguage.createAsLogin();
		
		return this.loadForUserByClassId(context.languageId, classId, context.companyId);
	}
	
	public Map<String, String> loadForUserByClassId(String languageId, String classId, String companyId) {
		
		val toMerge = this.defaultResources.createContentsMapByClassId(languageId, classId);
		
		this.refreshIfRequired(languageId, companyId);
		
		val customizedMap = this.customizedResources.createContentsMapByClassId(languageId, classId, companyId);
		
		customizedMap.forEach((resourceId, contents) -> {
			toMerge.put(resourceId, contents);
		});

		return toMerge;
	}
	
	public Map<String, String> loadForUserByResourceType(I18NResourceType resourceType) {

		val context = CompanyAndLanguage.createAsLogin();
		
		return this.loadForUserByResourceType(context.languageId, context.companyId, resourceType);
	}
	
	public Map<String, String> loadForUserByResourceType(String languageId, String companyId, I18NResourceType resourceType) {
		
		val toMerge = this.defaultResources.createContentsMapByResourceType(languageId, resourceType);

		this.refreshIfRequired(languageId, companyId);
		
		val customizedMap = this.customizedResources.createContentsMapByResourceType(languageId, companyId, resourceType);
		
		customizedMap.forEach((resourceId, contents) -> {
			toMerge.put(resourceId, contents);
		});

		return toMerge;
	}
	
	public void refreshDefaultResource() {

		this.languageRepository.getSystemLanguages().stream()
				.map(l -> l.getLanguageId())
				.forEach(languageId -> {
					this.defaultResources.put(
							languageId, this.resourcesRepository.loadResourcesDefault(languageId));
				});
	}

	private void refreshIfRequired(String languageId, String companyId) {
		
		this.resourcesRepository.getLastUpdatedDateTime(companyId, languageId).ifPresent(datetimeOfDataSource -> {
			if (this.customizedResources.requiresToUpdate(companyId, languageId, datetimeOfDataSource)) {
				val newContainer = this.resourcesRepository.loadResourcesOfCompany(companyId, languageId);
				this.customizedResources.update(languageId, companyId, newContainer);
			}
		});
	}

	@Override
	public void refreshIfRequired() {
		String companyId = DefaultSettingKeys.COMPANY_ID;
		String languageId = LanguageConsts.DEFAULT_LANGUAGE_ID;
		
		if (AppContexts.user().hasLoggedIn()) {
			companyId = AppContexts.user().companyId();
			languageId = AppContexts.user().language().basicLanguageId();
		}
		
		this.refreshIfRequired(languageId, companyId);
	}
	
	@RequiredArgsConstructor
	private static class CompanyAndLanguage {
		
		public final String companyId;
		public final String languageId;
		
		public static CompanyAndLanguage createAsLogin() {
			String languageId = LanguageConsts.DEFAULT_LANGUAGE_ID;
			String companyId = DefaultSettingKeys.COMPANY_ID;
			
			if (AppContexts.user().hasLoggedIn()) {
				languageId = AppContexts.user().language().basicLanguageId();
				companyId = AppContexts.user().companyId();
			}
			
			return new CompanyAndLanguage(companyId, languageId);
		}
	}
}
