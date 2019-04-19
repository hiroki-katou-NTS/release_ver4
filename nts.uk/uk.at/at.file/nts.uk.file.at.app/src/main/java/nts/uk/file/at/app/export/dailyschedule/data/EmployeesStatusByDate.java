package nts.uk.file.at.app.export.dailyschedule.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

//日付毎の所属状況
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeesStatusByDate {
	// 対象年月日
	private GeneralDate targetDate;
	// 対象社員
	private List<String> employees;
}
