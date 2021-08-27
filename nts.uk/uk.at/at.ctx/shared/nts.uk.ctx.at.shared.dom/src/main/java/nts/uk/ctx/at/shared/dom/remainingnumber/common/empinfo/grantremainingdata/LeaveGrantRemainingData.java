package nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.RemNumShiftListWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.*;

/**
 * 休暇付与残数データ　
 * @author masaaki_jinno
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveGrantRemainingData extends AggregateRoot {

	//ID
	protected String leaveID;

	/**
	 * 社員ID
	 */
	protected String employeeId;

	/**
	 * 付与日
	 */
	protected GeneralDate grantDate;

	/**
	 * 期限日
	 */
	protected GeneralDate deadline;

	/**
	 * 期限切れ状態
	 */
	protected LeaveExpirationStatus expirationStatus;

	/**
	 * 登録種別
	 */
	protected GrantRemainRegisterType registerType;

	/**
	 * 明細
	 */
	protected LeaveNumberInfo details;

	public static LeaveGrantRemainingData createDataFromJavaType(
			String annLeavID,
			String cID,
			String employeeId,
			GeneralDate grantDate,
			GeneralDate deadline,
			int expirationStatus,
			int registerType,
			double grantDays,
			Integer grantMinutes,
			double usedDays,
			Integer usedMinutes,
			Double stowageDays,
			double remainDays,
			Integer remainMinutes,
			double usedPercent) {

			LeaveGrantRemainingData domain = new LeaveGrantRemainingData();
			domain.leaveID = annLeavID;
			domain.employeeId = employeeId;
			domain.grantDate = grantDate;
			domain.deadline = deadline;
			domain.expirationStatus = EnumAdaptor.valueOf(expirationStatus, LeaveExpirationStatus.class);
			domain.registerType = EnumAdaptor.valueOf(registerType, GrantRemainRegisterType.class);

			domain.details = new LeaveNumberInfo(
					grantDays, grantMinutes, usedDays, usedMinutes, stowageDays,
					remainDays, remainMinutes, usedPercent);

//			if (prescribedDays != null && deductedDays != null && workingDays != null) {
//				domain.annualLeaveConditionInfo = Optional
//						.of(AnnualLeaveConditionInfo.createFromJavaType(prescribedDays, deductedDays, workingDays));
//			} else {
//				domain.annualLeaveConditionInfo = Optional.empty();
//			}
			return domain;
	}

	public void setAllValue(LeaveGrantRemainingData a) {
		this.employeeId = a.employeeId;
		this.grantDate = a.grantDate;
		this.deadline = a.deadline;
		this.expirationStatus = a.expirationStatus;
		this.registerType = a.registerType;
		this.details = a.details.clone();
	}

	/**
	 * ファクトリー
	 * @param cid 会社ID
	 * @param employeeId 社員ID
	 * @param grantDate 付与日
	 * @param deadline 期限日
	 * @param expirationStatus 期限切れ状態
	 * @param registerType 登録種別
	 * @param details 明細
	 * @return 休暇付与残数データ　
	 */
	public static LeaveGrantRemainingData of(
			String cid,
			String employeeId,
			GeneralDate grantDate,
			GeneralDate deadline,
			LeaveExpirationStatus expirationStatus,
			GrantRemainRegisterType registerType,
			LeaveNumberInfo details) {

		LeaveGrantRemainingData domain = new LeaveGrantRemainingData();
		domain.employeeId = employeeId;
		domain.grantDate = grantDate;
		domain.deadline = deadline;
		domain.expirationStatus = expirationStatus;
		domain.registerType = registerType;
		domain.details = details;
		return domain;
	}

	/**
	 * 休暇残数を指定使用数消化する
	 * @param require
	 * @param targetRemainingDatas 付与残数
	 * @param remNumShiftListWork 複数の付与残数の消化処理を行う一時変数
	 * @param leaveUsedNumber 休暇使用数
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param date　年月日
	 * @return dummyData　ダミーデータ
	 */
	public static Optional<LeaveGrantRemainingData> digest(
			LeaveRemainingNumber.RequireM3 require,
			List<LeaveGrantRemainingData> targetRemainingDatas,
			RemNumShiftListWork remNumShiftListWork,
			LeaveUsedNumber leaveUsedNumber,
			String companyId,
			String employeeId,
			GeneralDate date
			){

		// 取得した「付与残数」でループ
		for (val targetRemainingData : targetRemainingDatas){

			// 休暇付与残数を追加する
			remNumShiftListWork.AddLeaveGrantRemainingData(targetRemainingData);

			// 休暇使用数を消化できるかチェック
			if ( remNumShiftListWork.canDigest(
					require, leaveUsedNumber, companyId, employeeId, date) ){
				// 消化できないときはループ
				break;
			}
		}

		// 休暇使用数を消化する
		remNumShiftListWork.digest(
				require, leaveUsedNumber, companyId, employeeId, date);

		// 残数不足で一部消化できなかったとき
		if ( remNumShiftListWork.getUnusedNumber().isLargerThanZero() ){

			// 消化できなかった休暇使用数をもとに、付与残数ダミーデータを作成する

			// 「年休付与残数データ」を作成する
			val dummyRemainData = new AnnualLeaveGrantRemainingData();

			dummyRemainData.setLeaveID(UUID.randomUUID().toString());
			// 社員ID←パラメータ「社員ID」
			dummyRemainData.setEmployeeId(employeeId);

			// 付与日←パラメータ「年月日」
			dummyRemainData.setGrantDate(date);
			// 期限日←パラメータ「年月日」
			dummyRemainData.setDeadline(date);
			// 期限切れ状態←使用可能
			dummyRemainData.setExpirationStatus(LeaveExpirationStatus.AVAILABLE);
			// 登録種別←月締め
			dummyRemainData.setRegisterType(GrantRemainRegisterType.MONTH_CLOSE);

			LeaveNumberInfo leaveNumberInfo = new LeaveNumberInfo();

//			付与残数ダミーデータ.使用数.日数
//			　　←休暇残数シフトリストWORK.処理できなかった休暇使用数.日数
//			付与残数ダミーデータ.使用数.時間
//			　　←休暇残数シフトリストWORK.処理できなかった休暇使用数.時間
			LeaveUsedNumber leaveUsedNumberTmp = new LeaveUsedNumber();
			leaveUsedNumberTmp.setDays(remNumShiftListWork.getUnusedNumber().getDays());
			leaveUsedNumberTmp.setMinutes(remNumShiftListWork.getUnusedNumber().getMinutes());
			leaveNumberInfo.setUsedNumber(leaveUsedNumberTmp);

//			付与残数ダミーデータ.残数.日数
//			← 付与残数ダミーデータ.使用数.日数＊ー１
//			付与残数ダミーデータ.残数.時間
//			← 付与残数ダミーデータ.使用数.時間＊ー１
			LeaveRemainingNumber leaveRemainingNumberTmp = new LeaveRemainingNumber();
			leaveRemainingNumberTmp.setDays(
					new LeaveRemainingDayNumber(remNumShiftListWork.getUnusedNumber().getDays().v()*-1));
			if ( remNumShiftListWork.getUnusedNumber().getMinutes().isPresent() ){
				leaveRemainingNumberTmp.setMinutes(
					Optional.of(new LeaveRemainingTime(remNumShiftListWork.getUnusedNumber().getMinutes().get().v()*-1)));
			}
			leaveNumberInfo.setRemainingNumber(leaveRemainingNumberTmp);

			dummyRemainData.setDetails(leaveNumberInfo);

			// 呼び出し元にダミー情報を返す
			return Optional.of(dummyRemainData);

		}

		return Optional.empty();
	}

	/** 残数不足のときにはtrueを返す */
	public boolean isDummyData() {
		return this.details.isDummyData() && this.isShortageRemain();
	}

	private boolean isShortageRemain() {
		return details.isShortageRemain();
	}

	@Override
	public LeaveGrantRemainingData clone() {
		LeaveGrantRemainingData cloned = new LeaveGrantRemainingData();
		cloned.leaveID = new String(leaveID);
		cloned.employeeId = new String(employeeId);
		cloned.grantDate = grantDate;
		cloned.deadline = deadline;
		cloned.expirationStatus = expirationStatus;
		cloned.registerType = registerType;
		cloned.details = details.clone();

		return cloned;
	}

}
