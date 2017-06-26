package nts.uk.shr.com.program;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ProgramsManager
 * 
 */
public class ProgramsManager {
	
	/**
	 * Ccg001d
	 */
	public static final Program Ccg001d = new Program(
			WebAppId.AT, ProgramIdConsts.Ccg001d, "PgName_Ccg001d", "/view/ccg/001/d/index.xhtml");
	
	// TODO: Define new programs here.
	
	/**
	 * All programs map.
	 */
	private static final Map<WebAppId, List<Program>> PROGRAMS;
	
	static {
		Function<Field, Optional<Program>> pg = f -> {
			try {
				if (Modifier.isStatic(f.getModifiers()))
					return Optional.of((Program) f.get(null));
				else return Optional.empty();
			} catch (IllegalArgumentException | IllegalAccessException ex) {
				throw new RuntimeException(ex);
			}
		};
		PROGRAMS = Arrays.asList(ProgramsManager.class.getFields()).stream().filter(f -> f.getType() == Program.class)
					.map(f -> pg.apply(f).get()).collect(Collectors.groupingBy(Program::getAppId));
	}
	
	/**
	 * Finds program.
	 * 
	 * @param appId appId.
	 * @param path program path.
	 * @return optional program.
	 */
	public static Optional<Program> find(WebAppId appId, String path) {
		if (appId == null || path == null) return Optional.empty();
		Optional<Set<Program>> programsOpt = getSet(appId);
		if (!programsOpt.isPresent()) return Optional.empty();
		return programsOpt.get().stream().filter(a -> path.equals(a.getPPath())).findFirst();
	}
	
	/**
	 * Finds program Id.
	 * 
	 * @param appId appId.
	 * @param path path.
	 * @return optional program Id.
	 */
	public static Optional<String> idOf(WebAppId appId, String path) {
		return Optional.ofNullable(find(appId, path).orElse(new Program()).getPId());
	}
	
	/**
	 * Finds program name.
	 * 
	 * @param appId appId.
	 * @param path path.
	 * @return optional program name.
	 */
	public static Optional<String> nameOf(WebAppId appId, String path) {
		return Optional.ofNullable(find(appId, path).orElse(new Program()).getPName());
	}
	
	/**
	 * Gets predefined set.
	 * 
	 * @param appId appId.
	 * @return optional program set.
	 */
	private static Optional<Set<Program>> getSet(WebAppId appId) {
		List<Program> programs = PROGRAMS.get(appId);
		if (programs == null || programs.size() == 0) return Optional.empty();
		return Optional.of(programs.stream().collect(Collectors.toSet()));
	}
}
