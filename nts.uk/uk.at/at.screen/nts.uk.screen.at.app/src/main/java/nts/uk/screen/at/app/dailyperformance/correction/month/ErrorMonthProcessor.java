package nts.uk.screen.at.app.dailyperformance.correction.month;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerErrorRepository;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.ValidatorDataDailyRes;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.AttendenceTimeMonthDto;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ErrorMonthProcessor {
	
	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject
	private EmployeeMonthlyPerErrorRepository findError;
	
	@Inject
	private ValidatorDataDailyRes validatorDataDailyRes;

	// 月次のエラーの表示
	public List<DPItemValue> getErrorMonth(Collection<String> employeeIds, DateRange range) {
		String companyId = AppContexts.user().companyId();
		// ドメインモデル「月別実績の勤怠時間」を取得する
		if(employeeIds.isEmpty()) return new ArrayList<>();
		List<AttendenceTimeMonthDto> item = repo.findAttendenceTimeMonth(new ArrayList<>(employeeIds), range);
		val groupEmp = item.stream()
				.collect(Collectors.groupingBy(x -> x.getClosureId().value + "|" + x.getYearMonth().v() + "|"
						+ x.getClosureDate().getClosureDay().v() + "|"
						+ (x.getClosureDate().getLastDayOfMonth() ? 1 : 0)));
		List<EmployeeMonthlyPerError> errors = new ArrayList<>();
		groupEmp.forEach((key, value) -> {
			val emps = value.stream().map(x -> x.getEmployeeId()).collect(Collectors.toSet());
			errors.addAll(findError.findError(new ArrayList<>(emps), value.get(0).getYearMonth(),
					value.get(0).getClosureId(), value.get(0).getClosureDate()));
		});

		List<DPItemValue> lstErrorMap = validatorDataDailyRes.getErrorMonthAll(companyId, errors, false);
		return lstErrorMap;
	}
}
