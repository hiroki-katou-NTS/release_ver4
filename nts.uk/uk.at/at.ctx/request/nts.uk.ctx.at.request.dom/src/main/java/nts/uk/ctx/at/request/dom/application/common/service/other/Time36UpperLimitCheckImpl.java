package nts.uk.ctx.at.request.dom.application.common.service.other;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.AgreeTimeOfMonthExport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.AgreementExcessInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.AgreementTimeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.AgreementTimeImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.AgreementTimeStatusAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.ExcessTimesYearAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AppTimeItem;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.Time36UpperLimitCheckResult;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.outsideot.service.MonthlyItems;
import nts.uk.ctx.at.shared.dom.outsideot.service.OutsideOTSettingService;
import nts.uk.ctx.at.shared.dom.outsideot.service.Time36AgreementTargetItem;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SEmpHistoryImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SysEmploymentHisAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;

@Stateless
public class Time36UpperLimitCheckImpl implements Time36UpperLimitCheck {

	@Inject
	private ClosureService closureService;

	@Inject
	SysEmploymentHisAdapter sysEmploymentHisAdapter;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;

	@Inject
	private AgreementTimeAdapter agreementTimeAdapter;

	@Inject
	private ExcessTimesYearAdapter excessTimesYearAdapter;

	@Inject
	private OutsideOTSettingService outsideOTSettingService;

	@Inject
	private AgreementTimeStatusAdapter agreementTimeStatusAdapter;
	
	@Inject
	private ClosureRepository closureRepository;

	@Override
	public Time36UpperLimitCheckResult checkRegister(String companyId, String employeeId, GeneralDate appDate,
			ApplicationType appType, List<AppTimeItem> appTimeItems) {
		boolean errorFlg = false;
		String employmentCD = StringUtils.EMPTY;
		List<AgreementTimeImport> agreementTimeList = Collections.emptyList();
		// 「時間外時間の詳細」をクリア
		AppOvertimeDetail appOvertimeDetail = new AppOvertimeDetail();

		
		
		// 社員所属雇用履歴を取得
		Optional<SEmpHistoryImport> empHistOtp = sysEmploymentHisAdapter.findSEmpHistBySid(companyId, employeeId,
				GeneralDate.today());
		if (empHistOtp.isPresent()) {
			employmentCD = empHistOtp.get().getEmploymentCode();
		}
		// 雇用に紐づく締めを取得する
		Optional<ClosureEmployment> closureEmpOtp = closureEmploymentRepository.findByEmploymentCD(companyId,
				employmentCD);
		Optional<Closure> opClosureSystem = closureRepository.findById(companyId, closureEmpOtp.get().getClosureId());
		if(!opClosureSystem.isPresent()){
			throw new RuntimeException("khong co closure");
		}
		Closure closureSystem = opClosureSystem.get();
		
		// 指定した年月日時点の締め期間を取得する
		Closure closure = closureService.getClosureDataByEmployee(employeeId, appDate);
		Optional<ClosurePeriod> closurePeriodOpt = closure.getClosurePeriodByYmd(appDate);
		
		appOvertimeDetail.setYearMonth(closurePeriodOpt.get().getYearMonth());
		
		// 36協定時間の取得
		agreementTimeList = agreementTimeAdapter.getAgreementTime(companyId, Arrays.asList(employeeId),
				closurePeriodOpt.get().getYearMonth(), closureSystem.getClosureId());
		
		if (agreementTimeList.isEmpty()) {
			return new Time36UpperLimitCheckResult(errorFlg, Optional.ofNullable(appOvertimeDetail));
		}
		
		AgreementTimeImport agreementTime = agreementTimeList.get(0);
		if (agreementTime.getConfirmed().isPresent()) {
			AgreeTimeOfMonthExport confirmed = agreementTime.getConfirmed().get();
			// 「時間外時間の詳細」．限度エラー時間=「36協定時間一覧」．確定情報．限度エラー時間
			appOvertimeDetail.setLimitErrorTime(confirmed.getLimitErrorTime());
			// 「時間外時間の詳細」．実績時間=「36協定時間一覧」．確定情報．36協定時間
			appOvertimeDetail.setActualTime(confirmed.getAgreementTime());
			// 「時間外時間の詳細」．限度アラーム時間=「36協定時間一覧」．確定情報．限度アラーム時間
			appOvertimeDetail.setLimitAlarmTime(confirmed.getLimitAlarmTime());
			// 「時間外時間の詳細」．特例限度エラー時間=「36協定時間一覧」．確定情報．特例限度エラー時間
			appOvertimeDetail.setExceptionLimitErrorTime(confirmed.getExceptionLimitErrorTime().isPresent()
					? confirmed.getExceptionLimitErrorTime().get() : null);
			// 「時間外時間の詳細」．特例限度アラーム時間=「36協定時間一覧」．確定情報．特例限度アラーム時間
			appOvertimeDetail.setExceptionLimitAlarmTime(confirmed.getExceptionLimitAlarmTime().isPresent()
					? confirmed.getExceptionLimitAlarmTime().get() : null);
		}
		// 36協定対象項目一覧を取得
		Time36AgreementTargetItem targetItem = outsideOTSettingService.getTime36AgreementTargetItem(companyId);
		// INPUT．残業休出区分をチェックする
		if (ApplicationType.OVER_TIME_APPLICATION.equals(appType)) {
			// INPUT．時間外時間一覧の36協定時間対象の枠を合計する
			appOvertimeDetail.setApplicationTime(this.calcOvertimeAppTime(appTimeItems, targetItem));
		} else if (ApplicationType.BREAK_TIME_APPLICATION.equals(appType)) {
			// INPUT．時間外時間一覧の36協定時間対象の枠を合計する
			appOvertimeDetail.setApplicationTime(this.calcBreakAppTime(appTimeItems, targetItem));
		}
		// 超過回数を取得する
		Year year = new Year(appOvertimeDetail.getYearMonth().year());
		AgreementExcessInfoImport agreeInfo = excessTimesYearAdapter.getExcessTimesYear(employeeId, year);
		// 「時間外時間の詳細」．36年間超過回数 = 返した回数
		appOvertimeDetail.setNumOfYear36Over(agreeInfo.getExcessTimes());
		// 「時間外時間の詳細」．36年間超過月.Add(返した超過の年月)
		appOvertimeDetail.setYear36OverMonth(agreeInfo.getYearMonths());
		// 36協定時間の状態チェック
		AgreementTimeStatusOfMonthly checkAgreement = agreementTimeStatusAdapter.checkAgreementTimeStatus(
				new AttendanceTimeMonth(
						appOvertimeDetail.getApplicationTime().v() + appOvertimeDetail.getActualTime().v()),
				appOvertimeDetail.getLimitAlarmTime(), appOvertimeDetail.getLimitErrorTime(),
				appOvertimeDetail.getExceptionLimitErrorTime(), appOvertimeDetail.getExceptionLimitErrorTime());
		if (AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR.equals(checkAgreement)
				|| AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ERROR.equals(checkAgreement)) {
			errorFlg = true;
			// 「時間外時間の詳細」．年月は取得した超過月詳細に存在するかチェックする
			if (!appOvertimeDetail.existOverMonth(appOvertimeDetail.getYearMonth())) {
				// 「時間外時間の詳細」．36年間超過回数 += 1
				appOvertimeDetail.plusNumOfYear36Over();
				// 「時間外時間の詳細」．36年間超過月.Add(「時間外時間の詳細」．年月)
				appOvertimeDetail.addOverMonth(appOvertimeDetail.getYearMonth());
			}
		} else if(AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ALARM.equals(checkAgreement)
				|| AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR_SP.equals(checkAgreement)){
			// 「時間外時間の詳細」．年月は取得した超過月詳細に存在するかチェックする
			if (!appOvertimeDetail.existOverMonth(appOvertimeDetail.getYearMonth())) {
				// 「時間外時間の詳細」．36年間超過回数 += 1
				appOvertimeDetail.plusNumOfYear36Over();
				// 「時間外時間の詳細」．36年間超過月.Add(「時間外時間の詳細」．年月)
				appOvertimeDetail.addOverMonth(appOvertimeDetail.getYearMonth());
			}
		}
		return new Time36UpperLimitCheckResult(errorFlg, Optional.ofNullable(appOvertimeDetail));
	}

	@Override
	public Time36UpperLimitCheckResult checkUpdate(String companyId, Optional<AppOvertimeDetail> appOvertimeDetailOpt,
			String employeeId, ApplicationType appType, List<AppTimeItem> appTimeItems) {
		boolean errorFlg = false;

		if (!appOvertimeDetailOpt.isPresent()) {
			return new Time36UpperLimitCheckResult(errorFlg, appOvertimeDetailOpt);
		}
		AppOvertimeDetail appOvertimeDetail = appOvertimeDetailOpt.get();
		// 「時間外時間の詳細」．36時間をチェックする
		if (appOvertimeDetail.getLimitErrorTime().v() <= 0) {
			return new Time36UpperLimitCheckResult(errorFlg, Optional.ofNullable(appOvertimeDetail));
		}
		// 36協定対象項目一覧を取得
		Time36AgreementTargetItem targetItem = outsideOTSettingService.getTime36AgreementTargetItem(companyId);
		// INPUT．残業休出区分をチェックする
		if (ApplicationType.OVER_TIME_APPLICATION.equals(appType)) {
			// INPUT．時間外時間一覧の36協定時間対象の枠を合計する
			appOvertimeDetail.setApplicationTime(this.calcOvertimeAppTime(appTimeItems, targetItem));
		} else if (ApplicationType.BREAK_TIME_APPLICATION.equals(appType)) {
			// INPUT．時間外時間一覧の36協定時間対象の枠を合計する
			appOvertimeDetail.setApplicationTime(this.calcBreakAppTime(appTimeItems, targetItem));
		}
		// 36協定時間の状態チェック
		AgreementTimeStatusOfMonthly checkAgreement = agreementTimeStatusAdapter.checkAgreementTimeStatus(
				new AttendanceTimeMonth(
						appOvertimeDetail.getApplicationTime().v() + appOvertimeDetail.getActualTime().v()),
				appOvertimeDetail.getLimitAlarmTime(), appOvertimeDetail.getLimitErrorTime(),
				appOvertimeDetail.getExceptionLimitErrorTime(), appOvertimeDetail.getExceptionLimitErrorTime());
		if (AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR.equals(checkAgreement)
				|| AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ERROR.equals(checkAgreement)) {
			errorFlg = true;
			// 「時間外時間の詳細」．年月は「時間外時間の詳細」．36年間超過月に存在するかチェックする
			if (!appOvertimeDetail.existOverMonth(appOvertimeDetail.getYearMonth())) {
				// 「時間外時間の詳細」．36年間超過回数 +=
				appOvertimeDetail.plusNumOfYear36Over();
				// 1、「時間外時間の詳細」．36年間超過月.Add(「時間外時間の詳細」．年月)
				appOvertimeDetail.addOverMonth(appOvertimeDetail.getYearMonth());
			}
		} else if(AgreementTimeStatusOfMonthly.EXCESS_EXCEPTION_LIMIT_ALARM.equals(checkAgreement)
				|| AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR_SP.equals(checkAgreement)){
			// 「時間外時間の詳細」．年月は「時間外時間の詳細」．36年間超過月に存在するかチェックする
			if (!appOvertimeDetail.existOverMonth(appOvertimeDetail.getYearMonth())) {
				// 「時間外時間の詳細」．36年間超過回数 +=
				appOvertimeDetail.plusNumOfYear36Over();
				// 1、「時間外時間の詳細」．36年間超過月.Add(「時間外時間の詳細」．年月)
				appOvertimeDetail.addOverMonth(appOvertimeDetail.getYearMonth());
			}
		}
		return new Time36UpperLimitCheckResult(errorFlg, Optional.ofNullable(appOvertimeDetail));
	}

	private int calcOvertimeAppTime(List<AppTimeItem> appTimeItems, Time36AgreementTargetItem targetItem) {
		int sumAppTime = 0;
		List<AppTimeItem> appItemFrame = appTimeItems.stream()
				.filter(x -> x.getFrameNo() <= MonthlyItems.OVERTIME_10.frameNo).collect(Collectors.toList());
		for (AppTimeItem appTime : appItemFrame) {
			Optional<Integer> frameOtp = targetItem.getOvertimeFrNo().stream().filter(x -> x == appTime.getFrameNo())
					.findFirst();
			if (frameOtp.isPresent()) {
				sumAppTime += appTime.getAppTime();
			}
		}
		if (targetItem.isTargetFlex()) {
			sumAppTime += appTimeItems.stream().filter(x -> x.getFrameNo() > MonthlyItems.OVERTIME_10.frameNo)
					.mapToInt(x -> x.getAppTime()).sum();
		}
		return sumAppTime;
	}

	private int calcBreakAppTime(List<AppTimeItem> appTimeItems, Time36AgreementTargetItem targetItem) {
		int sumAppTime = 0;
		List<AppTimeItem> appItemFrame = appTimeItems.stream()
				.filter(x -> x.getFrameNo() <= MonthlyItems.OVERTIME_10.frameNo).collect(Collectors.toList());
		for (AppTimeItem appTime : appItemFrame) {
			Optional<Integer> frameOtp = targetItem.getBreakFrNo().stream().filter(x -> x == appTime.getFrameNo())
					.findFirst();
			if (frameOtp.isPresent()) {
				sumAppTime += appTime.getAppTime();
			}
		}
		return sumAppTime;
	}
}
