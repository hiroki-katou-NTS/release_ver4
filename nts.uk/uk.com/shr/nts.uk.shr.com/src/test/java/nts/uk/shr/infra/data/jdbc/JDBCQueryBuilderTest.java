package nts.uk.shr.infra.data.jdbc;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.Test;

import nts.uk.shr.infra.i18n.entity.LanguageMaster;

public class JDBCQueryBuilderTest {

	@Test
	public void test_select_all() {
		String query = JDBCQueryBuilder.start(LanguageMaster.class)
				.type(QueryType.SELECT).build();
		
		Assert.isNotNull(query, "test");
	}
	
	@Test()
	public void test_select_some() {
		String query = JDBCQueryBuilder.start(LanguageMaster.class)
				.type(QueryType.SELECT).selectFields("LANGUAGE_ID", "LANGUAGE_CODE").build();
		
		Assert.isNotNull(query, "test");
	}
	

	
	@Test()
	public void test_select_where() {
		String query = JDBCQueryBuilder.start(LanguageMaster.class)
				.type(QueryType.SELECT).selectFields("LANGUAGE_ID")
				.conditions(new FieldWithValue("LANGUAGE_ID", " = ", "ja"), ConditionJoin.OR).build();
		
		Assert.isNotNull(query, "test");
	}
	
	@Test()
	public void test_delete() {
		String query = JDBCQueryBuilder.start(LanguageMaster.class)
				.type(QueryType.DELETE)
				.conditions(new FieldWithValue("LANGUAGE_ID", " = ", "ja"), ConditionJoin.OR).build();
		
		Assert.isNotNull(query, "test");
	}
	
	@Test()
	public void test_update() {
		String query = JDBCQueryBuilder.start(LanguageMaster.class)
				.type(QueryType.UPDATE).changeFields(new FieldWithValue("LANGUAGE_NAME", " = ", "test"))
				.conditions(new FieldWithValue("LANGUAGE_ID", " = ", "ja"), ConditionJoin.OR).build();
		
		Assert.isNotNull(query, "test");
	}
	
	@Test()
	public void test_insert() {
		String query = JDBCQueryBuilder.start(LanguageMaster.class)
				.type(QueryType.INSERT)
				.changeFields(new FieldWithValue("LANGUAGE_NAME", "=", "test"), 
						new FieldWithValue("LANGUAGE_ID", "=", "test"), 
						new FieldWithValue("LANGUAGE_CODE", "=", "test"))
				.build();
		
		Assert.isNotNull(query, "test");
	}
}
