package nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;

/**
 * 
 * @author HungTT - 積立年休付付与時点残数履歴データ
 *
 */

@Getter
public class ReserveLeaveGrantTimeRemainHistoryData extends AggregateRoot {

	/**
	 * 積立年休付与残数データID
	 */
	private String rsvLeaID;

	/**
	 * 社員ID
	 */
	private String employeeId;

	/**
	 * 付与日
	 */
	private GeneralDate grantDate;

	/**
	 * 期限日
	 */
	private GeneralDate deadline;

	/**
	 * 期限切れ状態
	 */
	private LeaveExpirationStatus expirationStatus;

	/**
	 * 登録種別
	 */
	private GrantRemainRegisterType registerType;

	/**
	 * 明細
	 */
	private ReserveLeaveNumberInfo details;

	// 付与処理日
	private GeneralDate grantProcessDate;

	public ReserveLeaveGrantTimeRemainHistoryData(String id, String employeeId, GeneralDate grantDate,
			GeneralDate deadline, int expirationStatus, int registerType, double grantDays, double usedDays,
			Double overLimitDays, double remainDays, GeneralDate grantProcessDate) {
		super();
		this.rsvLeaID = id;
		this.employeeId = employeeId;
		this.grantDate = grantDate;
		this.deadline = deadline;
		this.expirationStatus = EnumAdaptor.valueOf(expirationStatus, LeaveExpirationStatus.class);
		this.registerType = EnumAdaptor.valueOf(registerType, GrantRemainRegisterType.class);
		this.details = new ReserveLeaveNumberInfo(grantDays, usedDays, overLimitDays, remainDays);
		this.grantProcessDate = grantProcessDate;
	}

	public ReserveLeaveGrantTimeRemainHistoryData(ReserveLeaveGrantRemainingData data, GeneralDate grantProcessDate) {
		super();
		this.rsvLeaID = data.getRsvLeaID();
		this.employeeId = data.getEmployeeId();
		this.grantDate = data.getGrantDate();
		this.deadline = data.getDeadline();
		this.expirationStatus = data.getExpirationStatus();
		this.registerType = data.getRegisterType();
		this.details = data.getDetails();
		this.grantProcessDate = grantProcessDate;
	}

}
