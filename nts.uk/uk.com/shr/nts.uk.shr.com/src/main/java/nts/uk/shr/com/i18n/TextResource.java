package nts.uk.shr.com.i18n;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nts.arc.bean.SingletonBeansSoftCache;
import nts.arc.i18n.I18NResources;

public class TextResource {

	public static String localize(String resourceId, String... params) {
		return localize(
				resourceId,
				params.length > 0 ? Arrays.asList(params) : Collections.emptyList());
	}

	public static String localize(String resourceId, List<String> params) {
		return SingletonBeansSoftCache.get(I18NResources.class).localize(resourceId, params)
				.orElse(resourceId);
	}
}
