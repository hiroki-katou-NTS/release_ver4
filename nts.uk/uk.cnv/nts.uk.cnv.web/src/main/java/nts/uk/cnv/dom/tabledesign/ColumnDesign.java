package nts.uk.cnv.dom.tabledesign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.cnv.dom.databasetype.DataType;
import nts.uk.cnv.dom.databasetype.DataTypeDefine;

@AllArgsConstructor
@Getter
public class ColumnDesign {
	private int id;
	private String name;
	private DataType type;
	private int maxLength;
	private int scale;
	/**private int precision;**/
	private boolean nullable;

	private boolean primaryKey;
	private int primaryKeySeq;
	private boolean uniqueKey;
	private int uniqueKeySeq;

	private String defaultValue;
	private String comment;

	public String getColumnContaintDdl(DataTypeDefine datatypedefine) {
		return "\t" + this.name + " " +
				datatypedefine.dataType(this.type, this.maxLength, this.scale) +
			(this.nullable ? " NULL" : " NOT NULL") +
			(this.defaultValue != null && !this.defaultValue.isEmpty() ? " DEFAULT " + this.defaultValue : "");
	}
}
