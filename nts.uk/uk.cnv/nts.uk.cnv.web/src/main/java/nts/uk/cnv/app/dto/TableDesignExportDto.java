package nts.uk.cnv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TableDesignExportDto {
	String tableName;
	String type;
	boolean withComment;
	private String feature;
	private String date;

	public GeneralDateTime getDateTime() {
		return GeneralDateTime.now();
	}
}
