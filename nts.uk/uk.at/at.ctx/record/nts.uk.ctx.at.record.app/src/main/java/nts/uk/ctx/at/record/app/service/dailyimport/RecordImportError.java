package nts.uk.ctx.at.record.app.service.dailyimport;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@NoArgsConstructor
public class RecordImportError {

	private String employeeCode;

	private GeneralDate ymd = null;

	private List<String> items = new ArrayList<>();
	
	@Setter
	private String message;

	public RecordImportError(String empCode) {
		this.employeeCode = empCode;
	}

	public RecordImportError(String empCode, GeneralDate ymd) {
		this.employeeCode = empCode;
		this.ymd = ymd;
	}

	public RecordImportError(String empCode, GeneralDate ymd, String message) {
		this.employeeCode = empCode;
		this.ymd = ymd;
		this.message = message;
	}
}
