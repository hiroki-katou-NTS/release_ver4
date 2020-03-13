package nts.uk.shr.infra.data.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;

import nts.arc.bean.SingletonBeansSoftCache;
import nts.arc.layer.infra.data.EntityManagerLoader;
import nts.arc.layer.infra.data.entity.JpaEntity;
import nts.gul.reflection.ReflectionUtil;
import nts.gul.reflection.ReflectionUtil.Condition;

/**
 * Entity type utilities.
 *
 */
public class EntityTypeUtil {

	/**
	 * Get ruled primary key names.
	 * @param tableName
	 * @return name list
	 */
	public static List<String> getRuledPrimaryKeyNames(String tableName) {
		return getAllRuledColumnNames(tableName, c -> getPrimaryKeyNames(c));
	}
	
	/**
	 * Get all ruled column names.
	 * @param tableName
	 * @return name list
	 */
	public static List<String> getAllRuledColumnNames(String tableName) {
		return getAllRuledColumnNames(tableName, c -> getAllColumnNames(c));
	}
	
	/**
	 * Get primary key names.
	 * @param tableName
	 * @return name list
	 */
	public static List<String> getPrimaryKeyNames(String tableName) {
		return getAllColumnNames(tableName, c -> getPrimaryKeyNames(c));
	}
	
	/**
	 * Get all column names.
	 * @param tableName
	 * @return name list
	 */
	public static List<String> getAllColumnNames(String tableName) {
		return getAllColumnNames(tableName, c -> getAllColumnNames(c));
	}
	
	/**
	 * Get all ruled column names.
	 * @param tableName
	 * @param provider
	 * @return name list
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAllRuledColumnNames(String tableName, Function<Class<? extends JpaEntity>, List<String>> provider) {
		
		if (Strings.isNullOrEmpty(tableName)) return Collections.emptyList();
		String[] parts = tableName.split("_");
		
		if (parts.length > 0) {
			String entityName = Arrays.asList(parts).stream()
					.map(s -> CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s))
					.reduce("", (a, b) -> a + b);
 			EntityManager em = SingletonBeansSoftCache.get(EntityManagerLoader.class).getEntityManager();
 			
  			return em.getMetamodel().getEntities().stream().filter(e -> e.getName().equals(entityName))
	 				.map(e -> e.getJavaType()).findFirst().map(c -> provider.apply((Class<? extends JpaEntity>)c))
	 				.orElse(Collections.emptyList());
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Get all column names.
	 * @param tableName
	 * @param provider
	 * @return name list
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAllColumnNames(String tableName, Function<Class<? extends JpaEntity>, List<String>> provider) {
		
		if (Strings.isNullOrEmpty(tableName)) return Collections.emptyList();
		EntityManager em = SingletonBeansSoftCache.get(EntityManagerLoader.class).getEntityManager();
		
		return em.getMetamodel().getEntities().stream().map(e -> e.getJavaType())
				.filter(c -> {
					Table table = c.getDeclaredAnnotation(Table.class);
					return table == null ? false : tableName.equals(table.name());
				}).findFirst().map(c -> provider.apply((Class<? extends JpaEntity>)c))
				.orElse(Collections.emptyList());
	}
	
	public static List<String> getPrimaryKeyNames(Class<? extends JpaEntity> entityClass) {
		return getPrimaryKeyNames(entityClass, false);
	}
	
	/**
	 * Get primary key names.
	 * @param entityClass
	 * @return name list
	 */
	public static List<String> getPrimaryKeyNames(Class<? extends JpaEntity> entityClass, boolean excludesId) {
		
		List<String> embedded = ReflectionUtil.getStreamOfFieldsAnnotated(entityClass, Condition.ALL, EmbeddedId.class)
						.map(f -> f.getType()).findFirst().map(c -> getColumnNames(c)).orElse(new ArrayList<>());
		
		if (!excludesId) {
			List<String> ids = ReflectionUtil.getStreamOfFieldsAnnotated(entityClass, Condition.ALL, Id.class)
				.map(f -> {
					Column column = f.getDeclaredAnnotation(Column.class);
					return column == null ? "" : column.name();
				}).collect(Collectors.toList());
			embedded.addAll(ids);
		}
		
		return embedded;
	}
	
	/**
	 * Get all column names.
	 * @param entityClass
	 * @return name list
	 */
	public static List<String> getAllColumnNames(Class<? extends JpaEntity> entityClass) {
		
 		List<String> names = getPrimaryKeyNames(entityClass, true);
 		names.addAll(getColumnNames(entityClass));
 		
 		return names;
	}
	
	/**
	 * Get column names.
	 * @param entityClass
	 * @return name list
	 */
	public static List<String> getColumnNames(Class<?> entityClass) {
		
		return ReflectionUtil.getStreamOfFieldsAnnotated(entityClass, Condition.ALL, Column.class)
				.map(f -> { 
					Column colAn = f.getDeclaredAnnotation(Column.class);
					return colAn != null ? colAn.name() : "";
				}).collect(Collectors.toList());
	}
	
}
