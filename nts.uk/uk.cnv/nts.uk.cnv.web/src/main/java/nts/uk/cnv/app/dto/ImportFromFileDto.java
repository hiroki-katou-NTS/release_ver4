package nts.uk.cnv.app.dto;

import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
public class ImportFromFileDto {
	String path;
	String type;
	String branch;
	String date;

	public GeneralDate getDate() {
		return (this.date.isEmpty())
				? GeneralDate.today()
				: (this.date.contains("/"))
					? GeneralDate.fromString(this.date,"yyyy/MM/dd")
					: GeneralDate.fromString(this.date,"yyyy-MM-dd");
	}
}
