package nts.uk.shr.infra.i18n.loading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import nts.arc.i18n.custom.ICompanyResourceBundle;
import nts.arc.i18n.custom.ResourceChangedEvent;
import nts.arc.i18n.custom.ResourceItem;
import nts.arc.layer.infra.data.EntityManagerLoader;
import nts.uk.shr.infra.i18n.entity.CompanyResource;

@Singleton
@Startup
@ApplicationScoped
public class CompanyResourceLoading implements ICompanyResourceBundle {
	@Inject
	private EntityManagerLoader managerLoader;

	private static final String SELECT_ALL = "Select e from CompanyResource e";
	private static final String SELECT_COMPANY_ALL = "Select e from CompanyResource e where e.primaryKey.companyID =:companyCode";
	// Map<company code, Map<languageCode, List<resource>>>
	private Map<String, Map<String, List<CompanyResource>>> resource;

	public CompanyResourceLoading() {
	}

	@PostConstruct
	private void load() {
		resource = new HashMap<>();
		EntityManager em = managerLoader.getEntityManager();
		List<CompanyResource> compResource = em.createQuery(SELECT_ALL, CompanyResource.class).getResultList();
		resource = compResource.stream().collect(Collectors.groupingBy((p) -> p.getPrimaryKey().getCompanyID(),
				Collectors.groupingBy((x) -> x.getPrimaryKey().getLanguageCode(), Collectors.toList())));
	}

	@Override
	public List<ResourceItem> getResource(String companyCode, Locale language) {

		return resource.getOrDefault(companyCode, new HashMap<String, List<CompanyResource>>())
				.getOrDefault(language.getLanguage(), new ArrayList<CompanyResource>()).stream().map(p -> {
					ResourceItem item = ResourceItem.of(p.getPrimaryKey().getCode(), p.getContent());
					return item;
				}).collect(Collectors.toList());
	}

	@Override
	public void loadResourceOfCompany(ResourceChangedEvent event) {
		EntityManager em = managerLoader.getEntityManager();
		List<CompanyResource> compResource = em.createQuery(SELECT_COMPANY_ALL, CompanyResource.class)
				.setParameter("companyCode", event.getCompanyCode()).getResultList();
		Map<String, List<CompanyResource>> groupedByLanguageCode = compResource.stream()
				.collect(Collectors.groupingBy((x) -> x.getPrimaryKey().getLanguageCode(), Collectors.toList()));

		resource.put(event.getCompanyCode(), groupedByLanguageCode);
	}
	
}
