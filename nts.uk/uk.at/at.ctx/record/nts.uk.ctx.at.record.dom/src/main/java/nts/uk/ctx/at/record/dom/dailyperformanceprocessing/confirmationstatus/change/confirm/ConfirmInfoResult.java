package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConfirmInfoResult {

	//社員ID
	private String employeeId;
	
	//対象期間
	private DatePeriod period;
	
	//所属状況
	StatusOfEmployeeExport statusOfEmp;
	
	//日の情報
	InformationDay informationDay;
	
	//月の情報
	List<InformationMonth> informationMonths;

	//エラー発生年月日
	private List<EmployeeDateErrorOuput> lstOut;
	
	//社員、期間に一致する申請を取得する
	private List<ApplicationRecordImport> lstApplication;
	

}
