package nts.uk.cnv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class GetCategoryTablesDto {
	String tableId;
	String tableName;
}
