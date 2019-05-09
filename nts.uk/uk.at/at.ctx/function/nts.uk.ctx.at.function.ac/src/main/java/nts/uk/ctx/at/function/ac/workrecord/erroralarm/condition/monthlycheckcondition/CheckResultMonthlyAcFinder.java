package nts.uk.ctx.at.function.ac.workrecord.erroralarm.condition.monthlycheckcondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.Check36AgreementValueImport;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.CheckResultMonthlyAdapter;
import nts.uk.ctx.at.function.dom.adapter.checkresultmonthly.MonthlyRecordValuesImport;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.AttendanceItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlConAttendanceItemAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.AgreementCheckCon36FunImport;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.SpecHolidayCheckConFunImport;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.Check36AgreementValue;
import nts.uk.ctx.at.record.pub.monthly.GetMonthlyRecordPub;
import nts.uk.ctx.at.record.pub.monthly.MonthlyRecordValuesExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.AttendanceItemConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErAlAtdItemConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErAlConditionsAttendanceItemPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.AgreementCheckCon36PubEx;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.CheckResultMonthlyPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.SpecHolidayCheckConPubEx;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class CheckResultMonthlyAcFinder implements CheckResultMonthlyAdapter {

	@Inject
	private CheckResultMonthlyPub  checkResultMonthlyPub;
	
	@Inject
	private GetMonthlyRecordPub  getMonthlyRecordPub;
	
	@Override
	public boolean checkPublicHoliday(String companyId, String employeeCd, String employeeId, String workplaceId,
			boolean isManageComPublicHd, YearMonth yearMonth, SpecHolidayCheckConFunImport specHolidayCheckCon) {
		return checkResultMonthlyPub.checkPublicHoliday(companyId, employeeCd, employeeId, workplaceId, isManageComPublicHd, yearMonth,
				convertToSpecHolidayCheckConFunImport(specHolidayCheckCon));
	}
	
	private SpecHolidayCheckConPubEx convertToSpecHolidayCheckConFunImport(SpecHolidayCheckConFunImport specHolidayCheckCon) {
		return new SpecHolidayCheckConPubEx(
				specHolidayCheckCon.getErrorAlarmCheckID(),
				specHolidayCheckCon.getCompareOperator(),
				specHolidayCheckCon.getNumberDayDiffHoliday1(),
				specHolidayCheckCon.getNumberDayDiffHoliday2()
				);
	}

	@Override
	public Check36AgreementValueImport check36AgreementCondition(String employeeId,YearMonth yearMonth,int closureID,ClosureDate closureDate, AgreementCheckCon36FunImport agreementCheckCon36) {
		
		return convertToCheck36AgreementValue(checkResultMonthlyPub.check36AgreementCondition(employeeId, yearMonth, closureID, closureDate,
				convertToAgreementCheckCon36Import(agreementCheckCon36)));
	}
	
	private AgreementCheckCon36PubEx convertToAgreementCheckCon36Import(AgreementCheckCon36FunImport agreementCheckCon36) {
		return new AgreementCheckCon36PubEx(
				agreementCheckCon36.getErrorAlarmCheckID(),
				agreementCheckCon36.getClassification(),
				agreementCheckCon36.getCompareOperator(),
				agreementCheckCon36.getEralBeforeTime()
				);
	}
	
	private Check36AgreementValueImport convertToCheck36AgreementValue(Check36AgreementValue export ) {
		return new Check36AgreementValueImport(
				export.isCheck36AgreementCon(),
				export.getErrorValue(),
				export.getAlarmValue());
	}

	@Override
	public boolean checkPerTimeMonActualResult(YearMonth yearMonth, int closureID, ClosureDate closureDate,
			String employeeID, AttendanceItemConAdapterDto attendanceItemCondition) {
		return checkResultMonthlyPub.checkPerTimeMonActualResult(yearMonth, closureID, closureDate, employeeID, 
				convertToAttendanceItemCon(attendanceItemCondition));
	}
	
	private AttendanceItemConditionPubExport convertToAttendanceItemCon (AttendanceItemConAdapterDto export) {
		return new AttendanceItemConditionPubExport(
				export.getOperatorBetweenGroups(),
				convertToErAlConAttendanceItem(export.getGroup1()),
				export.getGroup2() ==null ?null:convertToErAlConAttendanceItem(export.getGroup2()),
				export.isGroup2UseAtr()
				);
	}
	
	private ErAlConditionsAttendanceItemPubExport convertToErAlConAttendanceItem(ErAlConAttendanceItemAdapterDto export) {
		return new  ErAlConditionsAttendanceItemPubExport(
				export.getAtdItemConGroupId(),
				export.getConditionOperator(),
				export.getLstErAlAtdItemCon().stream().map(c->convertToErAlAtdItemCon(c)).collect(Collectors.toList())
				);
	}
	private ErAlAtdItemConditionPubExport convertToErAlAtdItemCon(ErAlAtdItemConAdapterDto export) {
		return new ErAlAtdItemConditionPubExport(
				export.getTargetNO(),
				export.getConditionAtr(),
				export.isUseAtr(),
				export.getUncountableAtdItem(),
				export.getCountableAddAtdItems(),
				export.getCountableSubAtdItems(),
				export.getConditionType(),
				export.getCompareOperator(),
				export.getSingleAtdItem(),
				export.getCompareStartValue(),
				export.getCompareEndValue(),
				export.getInputCheckCondition()
				);
	}

	@Override
	public List<MonthlyRecordValuesImport> getListMonthlyRecords(String employeeId ,YearMonthPeriod period, List<Integer> itemIds) {
		List<MonthlyRecordValuesExport> x = getMonthlyRecordPub.algorithm(employeeId, period, itemIds);
		List<MonthlyRecordValuesImport> y = x.stream().map(z -> {
			return MonthlyRecordValuesImport.of(z.getYearMonth(), z.getClosureId(),z.getClosureDate(),z.getItemValues());
		}).collect(Collectors.toList());
		return y;
	}
	
	public Map<String, List<MonthlyRecordValuesImport>> getListMonthlyRecords(List<String> employeeId ,YearMonthPeriod period, List<Integer> itemIds){
		return getMonthlyRecordPub.algorithm(employeeId, period, itemIds).entrySet().stream().collect(Collectors.toMap(c -> c.getKey(), c -> {
			return c.getValue().stream().map(z -> {
				return MonthlyRecordValuesImport.of(z.getYearMonth(), z.getClosureId(),z.getClosureDate(),z.getItemValues());
			}).collect(Collectors.toList());
		}));
	}
	
	@Override
	public List<Check36AgreementValueImport> check36AgreementConditions(String employeeId, List<MonthlyRecordValuesImport> monthlyRecordValues,AgreementCheckCon36FunImport agreementCheckCon36, Optional<Closure> closure) {
		List<Check36AgreementValueImport> lstReturn = new ArrayList<>();
		List<MonthlyRecordValuesImport> monthlyFilterResult = new ArrayList<>();
		// Process Close is not null 
		if(closure.isPresent()){
			for (MonthlyRecordValuesImport monthlyRecordValuesImport : monthlyRecordValues) {
				if (monthlyRecordValuesImport.getClosureId().value == closure.get().getClosureId().value) {
					monthlyFilterResult.add(monthlyRecordValuesImport);
				}
			}
		
		}else{
			monthlyFilterResult = monthlyRecordValues;
		}
		if(!CollectionUtil.isEmpty(monthlyRecordValues)){
			for (MonthlyRecordValuesImport monthlyRecordValuesImport : monthlyRecordValues) {
				YearMonth yearMonth = monthlyRecordValuesImport.getYearMonth();
				int closureId = monthlyRecordValuesImport.getClosureId().value;
				Check36AgreementValueImport checkAgreementError = convertToCheck36AgreementValue(checkResultMonthlyPub.check36AgreementCondition(employeeId, yearMonth, 
						closureId, monthlyRecordValuesImport.getClosureDate(),convertToAgreementCheckCon36Import(agreementCheckCon36)));
				checkAgreementError.setYm(yearMonth);
				lstReturn.add(checkAgreementError);
			}
		}

		return lstReturn;
	}	
	//HoiDD No.257
	@Override
	public Map<String, Integer> checkPerTimeMonActualResult(YearMonth yearMonth, String employeeID, AttendanceItemConAdapterDto attendanceItemCondition) {
		return checkResultMonthlyPub.checkPerTimeMonActualResult(yearMonth, employeeID, 
				convertToAttendanceItemCon(attendanceItemCondition));
	}
	
	//HoiDD No.257
		@Override
		public Map<String,Map<YearMonth,Map<String,Integer>>> checkPerTimeMonActualResult(YearMonthPeriod yearMonth, List<String> employeeID, Map<String, AttendanceItemConAdapterDto> attendanceItemCondition,Map<String, Map<YearMonth, Map<String,String>>> resultsData) {
			return checkResultMonthlyPub.checkPerTimeMonActualResult(yearMonth, employeeID, 
					attendanceItemCondition.entrySet().stream().collect(Collectors.toMap(c -> c.getKey(), c -> convertToAttendanceItemCon(c.getValue()))),
					resultsData);
		}
}
