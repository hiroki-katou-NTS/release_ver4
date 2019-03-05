package nts.uk.ctx.at.shared.dom.vacation.obligannleause;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.ReferenceAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedDayNumber;

/**
 * ドメインサービス：年休使用義務
 * @author shuichi_ishida
 */
public interface ObligedAnnLeaUseService {

	/**
	 * 使用義務日数の取得
	 * @param companyId 会社ID
	 * @param distributeAtr 期間按分使用区分
	 * @param obligedAnnualLeaveUse 年休使用義務日数
	 * @return 年休使用義務日数
	 */
	Optional<AnnualLeaveUsedDayNumber> getObligedUseDays(String companyId, boolean distributeAtr,
			ObligedAnnualLeaveUse obligedAnnualLeaveUse);
	
	/**
	 * 義務日数計算期間内の年休使用数を取得
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param criteria 基準日
	 * @param referenceAtr 参照先区分
	 * @param distributeAtr 期間按分使用区分
	 * @param obligedAnnualLeaveUse 年休使用義務日数
	 * @return 年休使用数
	 */
	Optional<AnnualLeaveUsedDayNumber> getAnnualLeaveUsedDays(String companyId, String employeeId,
			GeneralDate criteria, ReferenceAtr referenceAtr, boolean distributeAtr,
			ObligedAnnualLeaveUse obligedAnnualLeaveUse);
	
	/**
	 * 按分が必要かどうか判断
	 * @param distributeAtr 期間按分使用区分
	 * @param obligedAnnualLeaveUse 年休使用義務日数
	 * @return　true:必要、false:不要
	 */
	boolean checkNeedForProportion(boolean distributeAtr, ObligedAnnualLeaveUse obligedAnnualLeaveUse);
	
	/**
	 * 現在の付与期間と重複する付与期間を持つ残数履歴データを取得
	 * @param distributeAtr 期間按分使用区分
	 * @param obligedAnnualLeaveUse 年休使用義務日数
	 * @return 年休付与情報Output
	 */
	AnnLeaGrantInfoOutput getRemainDatasAtDupGrantPeriod(boolean distributeAtr,
			ObligedAnnualLeaveUse obligedAnnualLeaveUse);
	
	/**
	 * 年休使用義務日数の期間按分
	 * @param distributeAtr 期間按分使用区分
	 * @param obligedAnnualLeaveUse 年休使用義務日数
	 * @return 年休使用義務日数
	 */
	Optional<AnnualLeaveUsedDayNumber> distributePeriod(boolean distributeAtr,
			ObligedAnnualLeaveUse obligedAnnualLeaveUse);
}
