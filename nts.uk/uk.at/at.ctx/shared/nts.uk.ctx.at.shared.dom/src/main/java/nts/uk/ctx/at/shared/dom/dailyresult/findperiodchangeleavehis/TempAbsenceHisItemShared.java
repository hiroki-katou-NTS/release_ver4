package nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TempAbsenceHisItemShared {
	/**
	 * 休職休業枠NO
	 */
	private int tempAbsenceFrNo;

	/**
	 * 履歴ID
	 */
	private String historyId;

	/**
	 * 社員ID
	 */
	private String employeeId;

	// ------------- Optional ----------------

	/**
	 * 備考
	 */
	private String remarks;

	/**
	 * 社会保険支給対象区分
	 */
	private Integer soInsPayCategory;

	/**
	 * Optional 家族メンバーId Family member id
	 */
	private String familyMemberId;

	public TempAbsenceHisItemShared(int tempAbsenceFrNo, String historyId, String employeeId, String remarks,
			Integer soInsPayCategory, String familyMemberId) {
		super();
		this.tempAbsenceFrNo = tempAbsenceFrNo;
		this.historyId = historyId;
		this.employeeId = employeeId;
		this.remarks = remarks;
		this.soInsPayCategory = soInsPayCategory;
		this.familyMemberId = familyMemberId;
	}
}
