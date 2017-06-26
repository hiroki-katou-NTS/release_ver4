package nts.uk.shr.infra.i18n.loading;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import nts.arc.i18n.custom.ISystemResourceBundle;
import nts.arc.i18n.custom.ResourceType;
import nts.arc.layer.infra.data.EntityManagerLoader;
import nts.uk.shr.infra.i18n.SystemProperties;
import nts.uk.shr.infra.i18n.entity.SystemResource;

@Startup
@Singleton
@ApplicationScoped
public class SystemResourceLoading implements ISystemResourceBundle {
	private static String SELECT_ALL = "Select e from SystemResource e";

	/**
	 * Map<language code,Map<Type,Map<ProgramID,Map<id,content>>>>
	 */
	private final Map<String, Map<ResourceType, Map<String, Map<String, String>>>> systemResourceGroupByProgram;

	@Inject
	public SystemResourceLoading(EntityManagerLoader entityManagerLoader) {
		Map<String, Map<ResourceType, Map<String, Map<String, String>>>> tempGroupByProgram = new HashMap<>();

		EntityManager em = entityManagerLoader.getEntityManager();
		List<SystemResource> resource = em.createQuery(SELECT_ALL, SystemResource.class).getResultList();

		// group by language
		Map<String, List<SystemResource>> resourceGroupingByLanguage = resource.stream()
				.collect(Collectors.groupingBy((x) -> x.getPk().getLanguageCode(), Collectors.toList()));

		resourceGroupingByLanguage.forEach((k, v) -> {

			// Map<Locale,Map<Type,Map<ProgramID,Map<id,content>>>>
			Map<ResourceType, Map<String, Map<String, String>>> groupByProgram = v.stream()
					.collect(Collectors.groupingBy(SystemResource::getType,
							Collectors.groupingBy((x) -> x.getPk().getProgramId(), Collectors.toMap((x) -> {
								if (SystemProperties.SYSTEM_ID.equals(x.getPk().getProgramId())) {
									return x.getPk().getCode();
								} else {
									return x.getPk().getProgramId() + SystemProperties.UNDERSCORE + x.getPk().getCode();
								}

							}, SystemResource::getContent))));

			tempGroupByProgram.put(k, groupByProgram);

		});

		systemResourceGroupByProgram = tempGroupByProgram;

	}

	@Override
	public Map<String, Map<String, String>> getResource(Locale language, ResourceType type) {
		Map<String, Map<String, String>> result = systemResourceGroupByProgram
				.getOrDefault(language.getLanguage(), new HashMap<ResourceType, Map<String, Map<String, String>>>())
				.get(type);

		if (result == null) {
			return new HashMap<String, Map<String, String>>();
		}
		return Collections.unmodifiableMap(result);
	}
}
