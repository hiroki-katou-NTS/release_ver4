package nts.uk.screen.at.app.monthlyperformance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorSettingDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 日別実績のエラーをチェックする
 * @author tutk
 *
 */
@Stateless
public class CheckDailyPerError {
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepo;
	
	@Inject
	private DailyPerformanceScreenRepo dailyPerformanceScreenRepo;
	
	public List<CheckEmpEralOuput> checkDailyPerError(List<String> listEmployeeId,DatePeriod period,List<AffCompanyHistImport> listAffCompanyHistImport) {
		String companyId = AppContexts.user().companyId();
		
		//ドメインモデル「社員の日別実績エラー一覧」をすべて取得する
		List<EmployeeDailyPerError> data = employeeDailyPerErrorRepo.finds(listEmployeeId, period);
		List<EmployeeDailyPerError> listDataNew = new ArrayList<>();
		if(data.isEmpty())
			return Collections.emptyList();
		for(EmployeeDailyPerError employeeDailyPerError : data) {
			for(AffCompanyHistImport affCompanyHistImport : listAffCompanyHistImport) {
				if(employeeDailyPerError.getEmployeeID().equals(affCompanyHistImport.getEmployeeId())) {
					for(AffComHistItemImport affComHistItem : affCompanyHistImport.getLstAffComHistItem()) {
						if(employeeDailyPerError.getDate().afterOrEquals(affComHistItem.getDatePeriod().start())
							&& employeeDailyPerError.getDate().beforeOrEquals(affComHistItem.getDatePeriod().end())) {
							listDataNew.add(employeeDailyPerError);
							break;
						}
					}
				}
			}
		}
		List<CheckEmpEralOuput> listCheckEmpEralOuput = new ArrayList<>();
		
		//対応するドメインモデル「勤務実績のエラーアラーム」をすべて取得する
		List<DPErrorSettingDto> lstErrorSetting = this.dailyPerformanceScreenRepo
				.getErrorSetting(companyId, listDataNew.stream().map(e -> e.getErrorAlarmWorkRecordCode().v()).collect(Collectors.toList()), true, true, false);
		for(String empID : listEmployeeId) {
			List<EmployeeDailyPerError> listErrorByID = listDataNew.stream().filter(c->c.getEmployeeID().equals(empID)).collect(Collectors.toList());
			boolean checkError = false;
			boolean checkAlarm = false;
			for(EmployeeDailyPerError employeeDailyPerError : listErrorByID) {
				for(DPErrorSettingDto dPErrorSettingDto : lstErrorSetting) {
					if(employeeDailyPerError.getErrorAlarmWorkRecordCode().v().equals(dPErrorSettingDto.getErrorAlarmCode())) {
						if(dPErrorSettingDto.getTypeAtr() ==  TypeErrorAlarm.ERROR.value) {
							checkError = true;
						}else if(dPErrorSettingDto.getTypeAtr() ==  TypeErrorAlarm.ALARM.value) {
							checkAlarm = true;
						}
					}
				}	
			}
			if(checkError == true && checkAlarm == true) {
				listCheckEmpEralOuput.add(new CheckEmpEralOuput(empID, TypeErrorAlarm.ERROR_ALARM));
			}else if(checkError == true) {
				listCheckEmpEralOuput.add(new CheckEmpEralOuput(empID, TypeErrorAlarm.ERROR));
			}else if(checkAlarm == true) {
				listCheckEmpEralOuput.add(new CheckEmpEralOuput(empID, TypeErrorAlarm.ALARM));
			}else {
				listCheckEmpEralOuput.add(new CheckEmpEralOuput(empID, TypeErrorAlarm.NO_ERROR_ALARM));
			}
		}
		
		
		return listCheckEmpEralOuput;
		
	}

}
