package nts.uk.shr.com.program.nosession;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.val;
import nts.uk.shr.com.program.ProgramsManager;

public class PathsNoSession {

	public static PathsNoSession WEB_APIS = PathsNoSession.define(
			"/sample/.*",
			"/public/.*",
			"/ntscommons/arc/task/async/.*",
			"/shr/infra/file/storage/.*"
			);

	public static PathsNoSession WEB_SCREENS = PathsNoSession.define(
			"/view/sample/.*",
			"/view/common/error/.*",
			ProgramsManager.CCG007A.getPPath(),
			ProgramsManager.CCG007B.getPPath(),
			ProgramsManager.CCG007C.getPPath(),
			ProgramsManager.CCG007D.getPPath()
			);

	
	private final Set<Pattern> patterns;
	
	private PathsNoSession(Set<Pattern> patterns) {
		this.patterns = patterns;
	}
	
	public static PathsNoSession define(String... patternStrings) {
		val patterns = Arrays.asList(patternStrings).stream()
                .map(regex -> Pattern.compile(regex))
                .collect(Collectors.toSet());
		return new PathsNoSession(patterns);
	}
    
    /**
     * Return true if the request requires session.
     * 
     * @param requestedRootRelativePath requestedRootRelativePath
     * @param getRequestedPath getRequestedPath
     * @return true if the request requires session
     */
    public boolean sessionRequired(String requestedRootRelativePath) {
        
        if (requestedRootRelativePath == null) {
            return false;
        }
        
        String pathToBeChecked = requestedRootRelativePath.toLowerCase();
        
        return this.patterns.stream()
                .noneMatch(p -> {
                	return p.matcher(pathToBeChecked).matches();
                });
    }
}
