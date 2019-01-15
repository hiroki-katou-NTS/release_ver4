package nts.uk.ctx.at.record.app.service.dailyimport;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Getter
@NoArgsConstructor
public class RecordImportError {

	private String employeeCode;

	private GeneralDate ymd = null;

	private List<String> items = new ArrayList<>();

	public RecordImportError(String empCode) {
		this.employeeCode = empCode;
	}

	public RecordImportError(String empCode, GeneralDate ymd) {
		this.employeeCode = empCode;
		this.ymd = ymd;
	}
}
