package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;

/**
 * parameter : 特別休暇の付与明細
 * @author do_dt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SpecialLeaveGrantDetails {	
	/**
	 * 特別休暇コード
	 */
	private int code;
	/**
	 * 特別休暇データ区分
	 */
	private DataAtr dataAtr;
	/**
	 * 期限切れ区分
	 */
	private LeaveExpirationStatus expirationStatus;
	/**
	 * 期限日
	 */
	private GeneralDate deadlineDate;
	/**
	 * 社員ID
	 */
	private String sid;
	/**
	 * 付与日
	 */
	private GeneralDate grantDate;
	/**
	 * 明細
	 */
	private SpecialLeaveNumberInfoService details;
	
	private String specialID;
	
}
