package nts.uk.ctx.at.record.pubimp.monthly.vacation.childcarenurse.childcare;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.AggrResultOfChildCareNurse;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.GetRemainingNumberChildCareService;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.TmpChildCareNurseMngWork;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseAggrPeriodDaysInfo;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseAggrPeriodInfo;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseErrors;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNursePeriodExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseRemainingNumber;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseStartdateDaysInfo;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseStartdateInfo;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.GetRemainingNumberChildCareNurse;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.ChildCareNurseUsedNumber;

/**
 * 実装：期間中の子の看護休暇残数を取得
 * @author yuri_tamakoshi
 */
@Stateless
public class GetRemainingNumberChildCareNursePubImpl implements GetRemainingNumberChildCareNurse {

	@Inject
	private GetRemainingNumberChildCareService getRemainingNumberChildCareService;

	/**
	 * 期間中の子の看護休暇残数を取得
	 * @param employeeId 社員ID
	 * @param period 集計期間
	 * @param performReferenceAtr 実績のみ参照区分(月次モード orその他)
	 * @param criteriaDate 基準日
	 * @param isOverWrite 上書きフラグ(Optional)
	 * @param tempChildCareDataforOverWriteList List<上書き用の暫定管理データ>(Optional)
	 * @param prevChildCareLeave 前回の子の看護休暇の集計結果<Optional>
	 * @param createAtr 作成元区分(Optional)
	 * @param periodOverWrite 上書き対象期間(Optional)
	 * @return 子の看護介護休暇集計結果
	 */
	@Override
	public ChildCareNursePeriodExport getChildCareNurseRemNumWithinPeriod(String employeeId,DatePeriod period,
			InterimRemainMngMode performReferenceAtr,
			GeneralDate criteriaDate,
			Optional<Boolean> isOverWrite,
			Optional<List<TmpChildCareNurseMngWork>> tempChildCareDataforOverWriteList,
			Optional<AggrResultOfChildCareNurse> prevChildCareLeave,
			Optional<CreateAtr> createAtr,
			Optional<GeneralDate> periodOverWrite) {

		AggrResultOfChildCareNurse result = getRemainingNumberChildCareService.getChildCareRemNumWithinPeriod(
				employeeId, period, performReferenceAtr, criteriaDate, isOverWrite, tempChildCareDataforOverWriteList, prevChildCareLeave, createAtr, periodOverWrite);

		// 固定値を返す（一時対応）
		return mapToPub(result);
	}

	// Exportから変換
	private ChildCareNursePeriodExport mapToPub(AggrResultOfChildCareNurse c) {
		return new ChildCareNursePeriodExport(
					createError(c.getChildCareNurseErrors()) ,
					ChildCareNurseUsedNumber.of(
							c.getAsOfPeriodEnd().getUsedDay(),
							c.getAsOfPeriodEnd().getUsedTimes()),
					ChildCareNurseStartdateDaysInfo.of(
							mapToPub(c.getStartdateDays().getThisYear()),
							c.getStartdateDays().getNextYear().map(ny -> mapToPub(ny))),
					c.isStartDateAtr(),
					ChildCareNurseAggrPeriodDaysInfo.of(
							mapToPubAggrPeriodInfo(c.getAggrperiodinfo().getThisYear()),
							c.getAggrperiodinfo().getNextYear().map(ny -> mapToPubAggrPeriodInfo(ny))));
	}

	//  起算日からの休暇情報
	private ChildCareNurseStartdateInfo mapToPub(nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseStartdateInfo domain) {
		return ChildCareNurseStartdateInfo.of(
					ChildCareNurseUsedNumber.of(
							domain.getUsedDays().getUsedDay(),
							domain.getUsedDays().getUsedTimes()),
						ChildCareNurseRemainingNumber.of(
								domain.getRemainingNumber().getUsedDays().v(),
								domain.getRemainingNumber().getUsedTime().map(t -> t.v())),
						domain.getLimitDays().v());
	}

	// 集計期間の休暇情報
	private ChildCareNurseAggrPeriodInfo mapToPubAggrPeriodInfo(nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseAggrPeriodInfo domain) {
		return ChildCareNurseAggrPeriodInfo.of(
							domain.getUsedCount().v(),
							domain.getUsedDays().v(),
							ChildCareNurseUsedNumber.of(
									domain.getAggrPeriodUsedNumber().getUsedDay(),
									domain.getAggrPeriodUsedNumber().getUsedTimes()));
	}


	// 子の看護休暇エラー情報
	private List<ChildCareNurseErrors> createError(List<nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseErrors> childCareNurseErrors) {

		return childCareNurseErrors.stream().map(c ->
																ChildCareNurseErrors.of(
																		ChildCareNurseUsedNumber.of(
																				c.getUsedNumber().getUsedDay(),
																				c.getUsedNumber().getUsedTimes()),
																		c.getLimitDays().v(),
																		c.getYmd()))
				.collect(Collectors.toList());
	}
}