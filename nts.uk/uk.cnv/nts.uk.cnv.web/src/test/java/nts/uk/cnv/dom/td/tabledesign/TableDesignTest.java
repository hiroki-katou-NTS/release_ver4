package nts.uk.cnv.dom.td.tabledesign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lombok.val;
import nts.uk.cnv.dom.td.schema.tabledesign.TableDesign;
import nts.uk.cnv.dom.td.schema.tabledesign.TableName;
import nts.uk.cnv.dom.td.schema.tabledesign.column.ColumnDesign;
import nts.uk.cnv.dom.td.schema.tabledesign.column.DataType;
import nts.uk.cnv.dom.td.schema.tabledesign.column.DefineColumnType;
import nts.uk.cnv.dom.td.schema.tabledesign.constraint.PrimaryKey;
import nts.uk.cnv.dom.td.schema.tabledesign.constraint.TableConstraints;
import nts.uk.cnv.dom.td.schema.tabledesign.constraint.TableIndex;
import nts.uk.cnv.dom.td.tabledefinetype.DatabaseSpec;
import nts.uk.cnv.dom.td.tabledefinetype.databasetype.DatabaseType;

public class TableDesignTest {
	private final String OBJECT_NAME_REGEX = "\\[*[A-Z]+[A-Z|0-9|_]*\\]*";
	private final String TABLE_NAME_REGEX = OBJECT_NAME_REGEX;
	private final String COLUMN_NAME_REGEX = OBJECT_NAME_REGEX;
	private final String CHECK_CONSTAINT = "0 to 2";
	private final String CHECK_CONSTAINT_REGEX = "\\(" + COLUMN_NAME_REGEX + " >= [0-9]* AND " + COLUMN_NAME_REGEX + " <= [0-9]*\\)";

	private final String COLUMN_DEFINITION_REGEX_MSSQL = ""
			+ COLUMN_NAME_REGEX
			+ "\\s*(DECIMAL|INT|NVARCHAR|VARCHAR|NCHAR|CHAR|DATETIME2|DATE)*\\s*(\\([0-9|,]+\\))*"
			+ "\\s*(NULL|NOT NULL)"
			+ "\\s*(DEFAULT\\s*['|A-Z|0-9]*\\s*)*"
			+ "\\s*(CHECK\\s*" + CHECK_CONSTAINT_REGEX + "\\s*)*";

	private final String COLUMN_DEFINITION_REGEX_PG = ""
			+ COLUMN_NAME_REGEX
			+ "\\s*(NUMERIC|INT|NVARCHAR|VARCHAR|NCHAR|CHAR|TIMESTAMP|DATE)*\\s*(\\([0-9|,]+\\))*"
			+ "\\s*(NULL|NOT NULL)"
			+ "\\s*(DEFAULT\\s*['|A-Z|0-9]*\\s*)*"
			+ "\\s*(CHECK\\s*" + CHECK_CONSTAINT_REGEX + "\\s*)*";

	private final String TABLE_CONSTRAINT_REGEX = ""
			+ "(CONSTRAINT\\s*" + OBJECT_NAME_REGEX + ")*\\s*"
			+ "(PRIMARY KEY|UNIQUE)\\s*\\(\\s*(" + COLUMN_NAME_REGEX + ")+\\s*(\\s*,\\s*" + COLUMN_NAME_REGEX + "\\s*)*\\s*\\);*\\s*";

	private final String CREATE_TABLE_REGEX_MSSQL = "CREATE TABLE\\s*" + TABLE_NAME_REGEX + "\\s*\\(\\s*"
			+ "(" + COLUMN_DEFINITION_REGEX_MSSQL + ")+\\s*"
			+ "(,\\s*" + COLUMN_DEFINITION_REGEX_MSSQL + ")*\\s*"
			+ "(,\\s*" + TABLE_CONSTRAINT_REGEX + ")*\\s*"
			+ "\\)" + "\\s*";

	private final String CREATE_TABLE_REGEX_PG = "CREATE TABLE\\s*" + TABLE_NAME_REGEX + "\\s*\\(\\s*"
			+ "(" + COLUMN_DEFINITION_REGEX_PG + ")+\\s*"
			+ "(,\\s*" + COLUMN_DEFINITION_REGEX_PG + ")*\\s*"
			+ "(,\\s*" + TABLE_CONSTRAINT_REGEX + ")*\\s*"
			+ "\\)" + "\\s*";

	@Test
	public void test_createSimpleTableSql_SQLServer() {
		TableDesign target = createDummy();

		String result = target.createSimpleTableSql(DatabaseType.valueOf("sqlserver").spec());

		DatabaseSpec sqlSpec = DatabaseType.valueOf("sqlserver").spec();
		Assert.assertTrue(!result.contains(sqlSpec.tableCommentDdl("[TABLE_NAME]", "[TABLE_JPNAME]")));
		Assert.assertTrue(!result.contains(sqlSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_1]", "[JPNAME]")));
		Assert.assertTrue(!result.contains(sqlSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_2]", "[JPNAME]")));
		//Assert.assertTrue(!result.contains(sqlSpec.rlsDdl("[TABLE_NAME]")));
		Assert.assertTrue(result.split(";")[0].matches(CREATE_TABLE_REGEX_MSSQL));
	}

	@Test
	public void test_createSimpleTableSql_Postgres() {
		TableDesign target = createDummy();

		String result = target.createSimpleTableSql(DatabaseType.valueOf("postgres").spec());

		DatabaseSpec postgreSpec = DatabaseType.valueOf("postgres").spec();
		Assert.assertTrue(!result.contains(postgreSpec.tableCommentDdl("[TABLE_NAME]", "[TABLE_JPNAME]")));
		Assert.assertTrue(!result.contains(postgreSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_1]", "[JPNAME]")));
		Assert.assertTrue(!result.contains(postgreSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_2]", "[JPNAME]")));
		Assert.assertTrue(!result.contains(postgreSpec.rlsDdl("[TABLE_NAME]")));
		Assert.assertTrue(result.split(";")[0].matches(CREATE_TABLE_REGEX_PG));
	}

	@Test	public void test_createFullTableSql_SQLServer() {
		TableDesign target = createDummy();

		DatabaseSpec sqlserverSpec = DatabaseType.valueOf("sqlserver").spec();
		DatabaseSpec postgreSpec = DatabaseType.valueOf("postgres").spec();
		String result = target.createFullTableSql(sqlserverSpec);

		Assert.assertTrue(result.contains(sqlserverSpec.tableCommentDdl("[TABLE_NAME]", "[TABLE_JPNAME]")));
		Assert.assertTrue(result.contains(sqlserverSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_1]", "[JPNAME]")));
		Assert.assertTrue(result.contains(sqlserverSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_2]", "[JPNAME]")));
		Assert.assertTrue(!result.contains(postgreSpec.rlsDdl("[TABLE_NAME]")));
		Assert.assertTrue(result.split(";")[0].matches(CREATE_TABLE_REGEX_MSSQL));
	}

	@Test
	public void test_createFullTableSql_Postgres() {
		TableDesign target = createDummy();

		DatabaseSpec postgreSpec = DatabaseType.valueOf("postgres").spec();

		String result = target.createFullTableSql(postgreSpec);

		Assert.assertTrue(result.contains(postgreSpec.tableCommentDdl("[TABLE_NAME]", "[TABLE_JPNAME]")));
		Assert.assertTrue(result.contains(postgreSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_1]", "[JPNAME]")));
		Assert.assertTrue(result.contains(postgreSpec.columnCommentDdl("[TABLE_NAME]", "[COLUMN_NAME_2]", "[JPNAME]")));
		Assert.assertTrue(result.split(";")[0].matches(CREATE_TABLE_REGEX_PG));
	}

	private TableDesign createDummy() {
		List<ColumnDesign> cols = new ArrayList<>();
		DefineColumnType type = new DefineColumnType(DataType.INT, 1, 0, false, "", CHECK_CONSTAINT);
		cols.add(new ColumnDesign("0", "[COLUMN_NAME_1]", "[JPNAME]", type, "", 0));
		cols.add(new ColumnDesign("1", "[COLUMN_NAME_2]", "[JPNAME]", type, "", 1));

		return new TableDesign(
					"[TABLE_ID]", new TableName("[TABLE_NAME]"), "[TABLE_JPNAME]",
					cols,
					createTableConstraints());
	}

	private static TableConstraints createTableConstraints() {

		val pk = new PrimaryKey(Arrays.asList("0"), true);
		val index = Arrays.asList(new TableIndex("_1", Arrays.asList("1"), false));

		return new TableConstraints(pk, Collections.emptyList(), index);
	}
}
