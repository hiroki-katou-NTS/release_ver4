package nts.uk.shr.infra.web.component.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.gul.text.StringUtil;

public class TagContentsUtil {

	public static List<String> readMultipleLinesString(String contentsString) {
        return Arrays.asList(contentsString.split("\n")).stream()
    	        .filter(s -> !StringUtil.isNullOrEmpty(s, true))
    	        .map(s -> s.trim())
    	        .collect(Collectors.toList());
	}
	
	public static Optional<Class<?>> findClass(String fqnOfClass) {
		try {
			return Optional.of(Class.forName(fqnOfClass));
		} catch (ClassNotFoundException ex) {
			return Optional.empty();
		}
	}
	
}
