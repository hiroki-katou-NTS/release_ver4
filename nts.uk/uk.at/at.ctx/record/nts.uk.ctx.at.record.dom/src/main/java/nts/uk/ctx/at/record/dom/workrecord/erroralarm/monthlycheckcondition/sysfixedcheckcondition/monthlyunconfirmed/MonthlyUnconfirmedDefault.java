package nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.sysfixedcheckcondition.monthlyunconfirmed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcessRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class MonthlyUnconfirmedDefault implements MonthlyUnconfirmedService {

	@Inject
	private IdentityProcessRepository identityProcessRepo;
	
	@Inject
	private ConfirmationMonthRepository confirmationMonthRepo;
	
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	@Override
	public Optional<ValueExtractAlarmWR> checkMonthlyUnconfirmed(String employeeID,int yearMonth) {
		String companyID = AppContexts.user().companyId();
		//ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcess> identityProcess = identityProcessRepo.getIdentityProcessById(companyID);
		if(!identityProcess.isPresent()) {
			return Optional.empty();
		}
		//ドメインモデル「月別実績の勤怠時間」を取得する
		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlys = new ArrayList<>();
		YearMonth ym = YearMonth.of(yearMonth);
		attendanceTimeOfMonthlys = attendanceTimeOfMonthlyRepo.findByYearMonthOrderByStartYmd(employeeID,
				YearMonth.of(yearMonth));
		//「月の本人確認を利用する」をチェックする  :  利用する場合
		if(identityProcess.get().getUseMonthSelfCK() == 1) {
			for (AttendanceTimeOfMonthly tmp : attendanceTimeOfMonthlys) {
				// fix bug 101936 (thêm closureDate)
				ClosureDate closureDate = new ClosureDate(tmp.getClosureDate().getClosureDay().v() + 1, tmp.getClosureDate().getLastDayOfMonth());
				// ドメインモデル「月の本人確認」を取得する
				// fix bug 101936
				Optional<ConfirmationMonth> confirmationMonth = confirmationMonthRepo.findByKey(companyID, employeeID,
						tmp.getClosureId(), closureDate, tmp.getYearMonth());
				// 取得できた場合
				if (!confirmationMonth.isPresent()) {
					return Optional.empty();
				}
				// 取得できなかった場合
				GeneralDate date = GeneralDate.ymd(ym.year(), ym.month(), 1);
				return Optional.of(new ValueExtractAlarmWR(null, employeeID, date, TextResource.localize("KAL010_100"),
						TextResource.localize("KAL010_271"), TextResource.localize("KAL010_272"), null,null));

			}
		}
		return Optional.empty();
	}
	
	@Override
	public List<ValueExtractAlarmWR> checkMonthlyUnconfirmeds(String employeeID, int yearMonth,
			Optional<IdentityProcessUseSet> identityProcess) {
		String companyID = AppContexts.user().companyId();
		List<ValueExtractAlarmWR> lstDataReturn = new ArrayList<>();
		// ドメインモデル「本人確認処理の利用設定」を取得する
		if (!identityProcess.isPresent()) {
			return lstDataReturn;
		}
		// ドメインモデル「月別実績の勤怠時間」を取得する
		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlys = new ArrayList<>();
		YearMonth ym = YearMonth.of(yearMonth);
		attendanceTimeOfMonthlys = attendanceTimeOfMonthlyRepo.findByYearMonthOrderByStartYmd(employeeID,
				YearMonth.of(yearMonth));
		// 「月の本人確認を利用する」をチェックする : 利用する場合
		if (identityProcess.get().isUseIdentityOfMonth()) {
			for (AttendanceTimeOfMonthly tmp : attendanceTimeOfMonthlys) {
				// fix bug 101936 (thêm closureDate)
				ClosureDate closureDate = new ClosureDate(tmp.getClosureDate().getClosureDay().v() + 1,
						tmp.getClosureDate().getLastDayOfMonth());
				// ドメインモデル「月の本人確認」を取得する
				// fix bug 101936
				Optional<ConfirmationMonth> confirmationMonth = confirmationMonthRepo.findByKey(companyID, employeeID,
						tmp.getClosureId(), closureDate, tmp.getYearMonth());
				// 取得できた場合
				if (!confirmationMonth.isPresent()) {
					// 取得できなかった場合
					GeneralDate date = GeneralDate.ymd(ym.year(), ym.month(), 1);
					ValueExtractAlarmWR valueExtractAlarmWR = new ValueExtractAlarmWR(null, employeeID, date,
							TextResource.localize("KAL010_100"), TextResource.localize("KAL010_102"),
							TextResource.localize("KAL010_108"), null,null);
					lstDataReturn.add(valueExtractAlarmWR);
				}

			}
		}
		return lstDataReturn;
	}
	
	@Override
	public List<ValueExtractAlarmWR> checkMonthlyUnconfirmeds(List<String> employeeID, List<YearMonth> yearMonth, Optional<IdentityProcessUseSet> identityProcess) {
		List<ValueExtractAlarmWR> lstDataReturn = new ArrayList<>();
		// ドメインモデル「本人確認処理の利用設定」を取得する
		if (!identityProcess.isPresent()) {
			return lstDataReturn;
		}
		// 「月の本人確認を利用する」をチェックする : 利用する場合
		if (identityProcess.get().isUseIdentityOfMonth()) {
			
			// ドメインモデル「月別実績の勤怠時間」を取得する
			List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlys = attendanceTimeOfMonthlyRepo.findBySidsAndYearMonths(employeeID, yearMonth);
			
			List<ConfirmationMonth> confirmMonthlys = confirmationMonthRepo.findBySomeProperty(employeeID, yearMonth);
			for (AttendanceTimeOfMonthly tmp : attendanceTimeOfMonthlys) {
				// fix bug 101936 (thêm closureDate)
				// ドメインモデル「月の本人確認」を取得する
				// fix bug 101936
				Optional<ConfirmationMonth> confirmationMonth = confirmMonthlys.stream().filter(c -> {
					return c.getEmployeeId().equals(tmp.getEmployeeId()) && c.getClosureId() == tmp.getClosureId() 
							&& c.getProcessYM().equals(tmp.getYearMonth()) && c.getClosureDate().equals(tmp.getClosureDate());
				}).findFirst();
				// 取得できた場合
				if (!confirmationMonth.isPresent()) {
					// 取得できなかった場合
					GeneralDate date = GeneralDate.ymd(tmp.getYearMonth().year(), tmp.getYearMonth().month(), 1);
					ValueExtractAlarmWR valueExtractAlarmWR = new ValueExtractAlarmWR(null, tmp.getEmployeeId(), date,
							TextResource.localize("KAL010_100"), TextResource.localize("KAL010_102"),
							TextResource.localize("KAL010_108"), null,null);
					lstDataReturn.add(valueExtractAlarmWR);
				}

			}
		}
		return lstDataReturn;
	}

}
