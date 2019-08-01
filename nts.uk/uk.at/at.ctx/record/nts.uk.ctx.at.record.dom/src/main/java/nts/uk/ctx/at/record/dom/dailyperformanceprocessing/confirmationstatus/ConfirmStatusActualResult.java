package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmStatusActualResult {

	/**
	* 対象社員
	*/
	private String employeeId;

	/**
	 * 対象年月日
	 */
	private GeneralDate date;
	
	/**
	 * 確認状態 or 承認状態
	 */
	@Setter
	private boolean status;
	
	/**
	 * 実施可否
	 */
	protected ReleasedAtr permissionCheck;
	
	/**
	 * 解除可否
	 */
	protected ReleasedAtr permissionRelease;
	
	public ConfirmStatusActualResult(String employeeId, GeneralDate date, boolean status) {
		this.employeeId = employeeId;
		this.date = date;
		this.status = status;
		this.permissionCheck = ReleasedAtr.CAN_NOT_IMPLEMENT;
		this.permissionRelease = ReleasedAtr.CAN_NOT_IMPLEMENT;
	}
	
	public ConfirmStatusActualResult setPermission(boolean permissionCheck, boolean permissionRelease) {
		this.permissionCheck = ReleasedAtr.valueOf(permissionCheck ? 1: 0);
		this.permissionRelease = ReleasedAtr.valueOf(permissionRelease ? 1: 0);
		return this;
	}
	
	public ConfirmStatusActualResult setPermissionChecked(boolean permissionCheck) {
		this.permissionCheck = ReleasedAtr.valueOf(permissionCheck ? 1: 0);
		return this;
	}
	
	public ConfirmStatusActualResult setPermissionRelease(boolean permissionRelease) {
		this.permissionRelease = ReleasedAtr.valueOf(permissionRelease ? 1: 0);
		return this;
	}
	
	public boolean notDisableForConfirm() {
		if(status) return permissionRelease == ReleasedAtr.CAN_IMPLEMENT;
		return permissionCheck == ReleasedAtr.CAN_IMPLEMENT;
	}
	
	public boolean notDisableApproval() {
		return notDisableForConfirm();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
		result = prime * result + ((permissionCheck == null) ? 0 : permissionCheck.hashCode());
		result = prime * result + ((permissionRelease == null) ? 0 : permissionRelease.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfirmStatusActualResult other = (ConfirmStatusActualResult) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		if (permissionCheck.value != other.permissionCheck.value)
			return false;
		if (permissionRelease.value != other.permissionRelease.value)
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	

}
