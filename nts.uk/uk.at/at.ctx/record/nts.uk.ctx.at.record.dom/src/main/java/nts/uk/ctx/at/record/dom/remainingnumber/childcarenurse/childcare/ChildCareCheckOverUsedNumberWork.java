package nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.common.DayAndTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.ChildCareNurseUsedNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.TempChildCareNurseManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.CareManagementDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.NursingCareLeaveRemainingInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.DayNumberOfUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.TimeOfUse;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.ChildCareNurseUpperLimitPeriod;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.FamilyInfo;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingCategory;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.LaborContractTime;

/**
 *  超過確認用使用数
  * @author yuri_tamakoshi
  *
 */
@Getter
@Setter
public class ChildCareCheckOverUsedNumberWork {

	/** 使用数 */
	private ChildCareNurseUsedNumber usedNumber;

	/**
	 * コンストラクタ
	 */
	public ChildCareCheckOverUsedNumberWork(){

		this.usedNumber = new ChildCareNurseUsedNumber();
	}

	/**
	 * ファクトリー
	 * @param usedNumber 使用数
	 * @return 超過確認用使用数
	 */
	public static ChildCareCheckOverUsedNumberWork of(ChildCareNurseUsedNumber usedNumber) {

		ChildCareCheckOverUsedNumberWork domain = new ChildCareCheckOverUsedNumberWork();
		domain.usedNumber = usedNumber;
		return domain;
	}

	/**
	 * 残数不足数を計算
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param criteriaDate 基準日
	 * @param interimDate 暫定子の看護介護管理データ
	 * @param require
	 * 会社の年休設定を取得する（会社ID）
	 * 社員の契約時間を取得する（社員ID、基準日）
	 * 年休の契約時間を取得する（会社ID、社員ID、基準日、Require）
	 * 介護看護休暇設定を取得する（会社ID、介護看護区分）
	 * 社員IDが一致する家族情報を取得（社員ID）
	 * 介護対象管理データ（家族ID）
	 * @return 子の看護介護残数不足数
	 */
	public ChildCareShortageRemainingNumberWork calcShortageRemainingNumber(String companyId,
			String employeeId, DatePeriod period, GeneralDate criteriaDate,
			TempChildCareNurseManagement interimDate, RequireM4 require) {

		// 子の看護介護残数不足数
		ChildCareShortageRemainingNumberWork shortageRemainingNumberWork = new ChildCareShortageRemainingNumberWork();

		// インスタンスをコピーで作成（加算後使用数）
		ChildCareCheckOverUsedNumberWork resultCheckOverUsedNumberWork = ChildCareCheckOverUsedNumberWork.of(
																																		ChildCareNurseUsedNumber.of(this.usedNumber.getUsedDay(), this.usedNumber.getUsedTimes()));

		// 使用数に暫定管理データ使用数を加算
		resultCheckOverUsedNumberWork.usedNumber.add(interimDate.getUsedNumber());

		// 加算後使用数を日数に積み上げ
		//resultCheckOverUsedNumberWork.usedNumber.usedDayfromUsedTime(companyId, employeeId, criteriaDate, require);
		resultCheckOverUsedNumberWork.usedNumber.contractTime(require, companyId, employeeId, criteriaDate);

		// INPUT．Require．子の看護・介護休暇基本情報を取得する
		NursingCareLeaveRemainingInfo employeeInfo = require.employeeInfo(employeeId);

		// 期間ごとの上限日数を求める
		List<ChildCareNurseUpperLimitPeriod> childCareNurseUpperLimitPeriod = employeeInfo.childCareNurseUpperLimitPeriod(companyId, employeeId, period, criteriaDate, require);

		// 期間終了日時点の上限日数を確認
		ChildCareNurseUpperLimitPeriod upperLimitPeriod =childCareNurseUpperLimitPeriod.stream().
																								filter(x -> x.getPeriod().start().beforeOrEquals(period.end())
																								&&  x.getPeriod().end().afterOrEquals(period.end())).findFirst().get();

		// 子の看護介護残数が上限超過していないか
		boolean checkRemainingNumber = checkRemainingNumber(companyId, employeeId, upperLimitPeriod, criteriaDate, require);

		if(checkRemainingNumber) {
			// true
			// 子の看護介護残数不足数．使用可能数＝暫定管理データの使用数
			shortageRemainingNumberWork.setAvailable(interimDate.getUsedNumber());
			shortageRemainingNumberWork.setShortageRemNum(ChildCareNurseRemainingNumber.of(new DayNumberOfUse(0.0), Optional.of(new TimeOfUse(0))));
		} else {
			// falseの場合
			// 暫定管理データ使用数の内上限超過しないまでの値を求める
			ChildCareNurseUsedNumber childCareNurseUsedNumber = interimDateUpperLimit(companyId, employeeId,  criteriaDate, upperLimitPeriod, interimDate, require); //一時対応　要確認
			shortageRemainingNumberWork.setAvailable(childCareNurseUsedNumber);

			// 残数不足数を求める
			// ===子の看護介護残数不足数．残数不足数 = 暫定管理データの使用数 ー 子の看護介護残数不足数．使用可能数
			val remainDays = new DayNumberOfUse(interimDate.getUsedNumber().getUsedDay().v() - shortageRemainingNumberWork.getAvailable().getUsedDay().v());
			val remainTimes = new TimeOfUse(interimDate.getUsedNumber().getUsedTimes().map(c -> c.valueAsMinutes()).orElse(0)
																		- shortageRemainingNumberWork.getAvailable().getUsedTimes().map(c -> c.valueAsMinutes()).orElse(0));
			shortageRemainingNumberWork.getShortageRemNum().setUsedDays(remainDays);
			shortageRemainingNumberWork.getShortageRemNum().setUsedTime(Optional.of(remainTimes));
		}
		// 「子の看護介護残数不足数」を返す
		return shortageRemainingNumberWork;
	}

	/**
	 * 暫定管理データ使用数の内上限超過しないまでの値を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param limitDays 上限日数
	 * @param interimDate 暫定子の看護介護管理データ
	 * @param require
	 * 会社の年休設定を取得する（会社ID）
	 * 社員の契約時間を取得する（社員ID、基準日）
	 * 年休の契約時間を取得する（会社ID、社員ID、基準日）
	 * @return 上限超過しない数
	 */
	private ChildCareNurseUsedNumber interimDateUpperLimit(String companyId,
			String employeeId, GeneralDate criteriaDate,
			ChildCareNurseUpperLimitPeriod limitDays, TempChildCareNurseManagement interimDate, RequireM3 require) {

		// 上限との差
		DayAndTime dayAndTimeWork = new DayAndTime();

		// 減算される日と時間
		DayAndTime beSubtracted = DayAndTime.of(new TimeOfUse(0), new DayNumberOfUse(limitDays.getLimitDays().v().doubleValue()));
		// 減算する日と時間
		DayAndTime subtract =  DayAndTime.of(usedNumber.getUsedTimes().orElse(new TimeOfUse(0)), usedNumber.getUsedDay());

		// 上限日数と使用時間の差を求める
		dayAndTimeWork = DayAndTime.subDayAndTime(beSubtracted, subtract,  companyId, employeeId, criteriaDate, require);

		// 上限以上の使用数になっていいるか
		// 上限との差．日 <=0 and 上限との差．時間 <= 0
		if(dayAndTimeWork.getDay().v() <= 0 && dayAndTimeWork.getTime().v() <= 0) {
			// 上限超過しない数は０で返す
			return ChildCareNurseUsedNumber.of(new DayNumberOfUse(0d), Optional.of(new TimeOfUse(0))); //一時対応　要確認
		}else {
			// 暫定子の看護介護管理データ．使用数が時間か
			if(interimDate.getUsedNumber().getUsedTimes().isPresent()) {

				// 時間の使用数
				// 上限との差が0.5日か
				if(dayAndTimeWork.getTime().v() == 0.5) {
					// 上限超過しない数は０で返す
					return ChildCareNurseUsedNumber.of(new DayNumberOfUse(0d), Optional.of(new TimeOfUse(0))); //一時対応　要確認
				}else {
					// 上限超過しない数は上限との差の時間を返す
					return ChildCareNurseUsedNumber.of(dayAndTimeWork.getDay(), Optional.of(dayAndTimeWork.getTime()));
				}
			} else {
				// 日の使用数
				if(dayAndTimeWork.getDay().v() == 0.5) {
					// 上限超過しない数は0.5日で返す
					return ChildCareNurseUsedNumber.of(new DayNumberOfUse(0.5), Optional.of(new TimeOfUse(0))); //一時対応　要確認
				}else {
					// 上限超過しない数は０で返す
					return ChildCareNurseUsedNumber.of(new DayNumberOfUse(0d), Optional.of(new TimeOfUse(0))); //一時対応　要確認
				}
			}
		}
	}

	/**
	 * 子の看護介護残数が上限超過していないか
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param limitDays 上限日数
	 * @param criteriaDate 基準日
	 * @param require
	 * @return 残数が残っているか（true、false）
	 */
	public boolean checkRemainingNumber(String companyId,
			String employeeId, 	ChildCareNurseUpperLimitPeriod limitDays, GeneralDate criteriaDate, RequireM3 require) {

		// 減算される日と時間
		DayAndTime beSubtracted = DayAndTime.of(new TimeOfUse(0), new DayNumberOfUse(limitDays.getLimitDays().v().doubleValue()));
		// 減算する日と時間
		DayAndTime subtract =  DayAndTime.of(usedNumber.getUsedTimes().orElse(new TimeOfUse(0)), usedNumber.getUsedDay());

		// 日と時間の減算
		DayAndTime subDayAndTime = DayAndTime.subDayAndTime(beSubtracted, subtract,  companyId, employeeId, criteriaDate, require);

		// 日と時間を残数に変換する
		//	=== 	子の看護介護残数．日数　＝　日と時間．日
		// ===		子の看護介護残数．時間　＝　日と時間．時間
		ChildCareNurseRemainingNumber usedNumber = ChildCareNurseRemainingNumber.of(subDayAndTime.getDay(),
																																					Optional.of(subDayAndTime.getTime()));

		// 残数が上限を超えていないか
		boolean  checkOverUpperLimit = usedNumber.checkOverUpperLimit();

		// 「残数が残っているか（true、false）」を返す
		return checkOverUpperLimit;
	}

	/**
	 * 上限超過チェック
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param criteriaDate 基準日
	 * @param interimDate 暫定子の看護介護管理データ
	 * @param require
	 * @return ChildCareNurseErrors 子の看護介護エラー情報
	 */
	public List<ChildCareNurseErrors> checkOverUsedNumberWork(String companyId,
			String employeeId, DatePeriod period, GeneralDate criteriaDate, TempChildCareNurseManagement interimDate, Require require){

		List<ChildCareNurseErrors> childCareNurseErrors = new ArrayList<>();

		// 暫定管理データを加算
		// === 超過確認用使用数．日数＋＝暫定子の看護介護管理データ．使用日数
		// ===	超過確認用使用数．時間＋＝暫定子の看護介護管理データ．使用時間
		this.usedNumber.add(interimDate.getUsedNumber());

		// 時間使用数を日数に繰り上げ
		//this.usedNumber.usedDayfromUsedTime(companyId, employeeId, criteriaDate, require);
		this.usedNumber.contractTime(require, companyId, employeeId, criteriaDate);

		// INPUT．Require．子の看護・介護休暇基本情報を取得する
		NursingCareLeaveRemainingInfo employeeInfo = require.employeeInfo(employeeId);

		// 期間ごとの上限日数を求める
		List<ChildCareNurseUpperLimitPeriod> childCareNurseUpperLimitPeriod = employeeInfo.childCareNurseUpperLimitPeriod(companyId,
				employeeId, period, criteriaDate, require);

		// 対象日の上限日数を確認
		// ===上限日数期間．期間．開始日 <=暫定残数管理データ．対象日<= 上限日数期間．期間．終了日
		ChildCareNurseUpperLimitPeriod upperLimitPeriod =
				childCareNurseUpperLimitPeriod.stream().filter(x -> x.getPeriod().start().beforeOrEquals(interimDate.getYmd())
				&&  x.getPeriod().end().afterOrEquals(interimDate.getYmd())).findFirst().get();

		// 子の看護介護残数を使い過ぎていないか
		boolean checkRemainingNumber = checkRemainingNumber(companyId, employeeId, upperLimitPeriod, criteriaDate, require);

		if (!checkRemainingNumber) {
			// 上限超過エラーリストに追加
			// ===年月日←暫定子の看護介護管理データ．対象日
			// ===上限日数←上限日数期間．上限日数
			// ===使用数←超過確認用使用数
			childCareNurseErrors.add(ChildCareNurseErrors.of(usedNumber,
					upperLimitPeriod.getLimitDays(),
					interimDate.getYmd()));
		}
		// 「子の看護介護エラー情報」を返す
		return childCareNurseErrors;
	}

	public static interface Require extends RequireM3,  ChildCareNurseUsedNumber.RequireM3, NursingCareLeaveRemainingInfo.RequireM7{

		// 介護看護休暇設定を取得する（会社ID、介護看護区分）
		NursingLeaveSetting nursingLeaveSetting(String companyId, NursingCategory nursingCategory);

		// 子の看護・介護休暇基本情報を取得する（社員ID）
		NursingCareLeaveRemainingInfo employeeInfo(String employeeId);

		// 会社の年休設定を取得する（会社ID）
		AnnualPaidLeaveSetting annualLeaveSet(String companyId);

		// 社員の契約時間を取得する（社員ID、基準日）
		LaborContractTime empContractTime(String employeeId, GeneralDate criteriaDate );

		// 年休の契約時間を取得する（会社ID、社員ID、基準日）
		LaborContractTime contractTime(String companyId, String employeeId,  GeneralDate criteriaDate);

		// 社員IDが一致する家族情報を取得（社員ID）
		List<FamilyInfo> familyInfo(String employeeId);

		// 介護対象管理データ（家族ID）
		CareManagementDate careData(String familyID);

		// 期間の上限日数取得する（会社ID、社員ID、期間、介護看護区分）
		NursingCareLeaveRemainingInfo upperLimitPeriod (String companyId, String employeeId, DatePeriod period, NursingCategory nursingCategory);


	}
	public static interface RequireM4 extends RequireM5, RequireM3,  ChildCareNurseUsedNumber.RequireM3, NursingCareLeaveRemainingInfo.RequireM7{
		// 年休と介護設定等　計６つ
	}

	public static interface RequireM5 extends RequireM1, RequireM2{
		// 社員IDが一致する家族情報を取得（社員ID）
		List<FamilyInfo> familyInfo(String employeeId);

		// 介護対象管理データ（家族ID）
		CareManagementDate careData(String familyID);
	}

	public static interface RequireM1 {
		// 介護看護休暇設定を取得する（会社ID、介護看護区分）
		NursingLeaveSetting nursingLeaveSetting(String companyId, NursingCategory nursingCategory);
	}


	public static interface RequireM2 {
		// 子の看護・介護休暇基本情報を取得する（社員ID）
		NursingCareLeaveRemainingInfo employeeInfo(String employeeId);
	}

	public static interface RequireM3 extends DayAndTime.RequireM3{
		// 年休
	}
}
