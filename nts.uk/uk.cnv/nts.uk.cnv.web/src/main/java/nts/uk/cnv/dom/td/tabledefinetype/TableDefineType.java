package nts.uk.cnv.dom.td.tabledefinetype;

public interface TableDefineType {
	// 型
	public String dataType(DataType type, Integer... length);
	public DataType parse(String type, Integer... length) ;

	public String tableCommentDdl(String tableName, String jpName);
	public String columnCommentDdl(String tableName, String columnName, String jpName);
	public String rlsDdl(String tableName);
	public String convertBoolDefault(String value);
}
