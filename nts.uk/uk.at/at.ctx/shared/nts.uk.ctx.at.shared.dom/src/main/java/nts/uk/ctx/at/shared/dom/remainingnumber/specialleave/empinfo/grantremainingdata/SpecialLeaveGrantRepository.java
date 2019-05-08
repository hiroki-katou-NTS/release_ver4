package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface SpecialLeaveGrantRepository {

	List<SpecialLeaveGrantRemainingData> getAll(String employeeId, int specialCode);

	List<SpecialLeaveGrantRemainingData> getAllByExpStatus(String employeeId, int specialCode, int expirationStatus);
	
	void add(SpecialLeaveGrantRemainingData data);

	void update(SpecialLeaveGrantRemainingData data);

	void delete(String specialid);

	Optional<SpecialLeaveGrantRemainingData> getBySpecialId(String specialId);
	/**
	 * 特別休暇付与残数データ
	 * @param sid ・社員ID=INPUT．社員ID
	 * @param specialLeaveCode ・特別休暇コード=INPUT．特別休暇コード
	 * @param expirationStatus ・期限切れ状態＝”使用可能”
	 * @param grantDate ・付与日<=集計終了日．AddDays(1) 
	 * @param deadlineDate ・集計開始日<=期限日
	 * @return
	 */
	List<SpecialLeaveGrantRemainingData> getByPeriodStatus(String sid, int specialLeaveCode, LeaveExpirationStatus expirationStatus,
			GeneralDate grantDate, GeneralDate deadlineDate);
	boolean isHasData(String sid, String specialID,GeneralDate grantDate, int specialLeaCode);
	/**
	 * 特別休暇付与残数データ
	 * @param sid ・社員ID=INPUT．社員ID
	 * @param speCode   ・特別休暇コード=INPUT．特別休暇コード
	 * @param datePriod ・集計開始日 < 付与日 <= INPUT．集計終了日 
	 * @param startDate ・集計開始日 <= 期限日
	 * @param expirationStatus ・期限切れ状態 ＝ ”使用可能”
	 * @return 
	 */
	List<SpecialLeaveGrantRemainingData> getByNextDate(String sid, int speCode, DatePeriod datePriod, GeneralDate startDate, LeaveExpirationStatus expirationStatus);

}
