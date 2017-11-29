package nts.uk.ctx.bs.employee.infra.entity.temporaryabsence;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BSYMT_TEMP_ABS_HIS_ITEM")
public class BsymtTempAbsHisItem extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@Id
	@Column(name = "HIST_ID")
	public String histId;

	@Basic(optional = false)
	@Column(name = "SID")
	public String sid;
	
	@Basic(optional = false)
	@Column(name = "LEAVE_HOLIDAY_ATR")
	public int leaveHolidayAtr;

	@Basic(optional = true)
	@Column(name = "REMARKS")
	public String remarks;
	
	@Basic(optional = true)
	@Column(name = "SO_INS_PAY_CATEGORY")
	public int soInsPayCategory;
	
	// -------------- extend object ----------------------
	
	@Basic(optional = true)
	@Column(name = "MULTIPLE")
	public Integer multiple;

	@Basic(optional = true)
	@Column(name = "FAMILY_MEMBER_ID")
	public String familyMemberId;
	
	@Basic(optional = true)
	@Column(name = "SAME_FAMILY")
	public Integer sameFamily;
	
	@Basic(optional = true)
	@Column(name = "CHILD_TYPE")
	public Integer childType;

	@Basic(optional = true)
	@Column(name = "CREATE_DATE")
	public GeneralDate createDate;

	@Basic(optional = true)
	@Column(name = "SPOUSE_IS_LEAVE")
	public Integer spouseIsLeave;
	
	@Basic(optional = true)
	@Column(name = "SAME_FAMILY_DAYS")
	public Integer sameFamilyDays;
	
	@Override
	protected Object getKey() {
		return histId;
	}
	/**
	 * createLeave
	 * createSickLeave
	 * createAnyLeave
	 * @param historyId
	 * @param employeeId
	 * @param remarks
	 * @param soInsPayCategory
	 */
	public BsymtTempAbsHisItem (String historyId, String employeeId, int leaveHolidayAtr, String remarks,
			Integer soInsPayCategory) {
		this.histId = historyId;
		this.sid = employeeId;
		this.leaveHolidayAtr = leaveHolidayAtr;
		this.remarks = remarks;
		this.soInsPayCategory = soInsPayCategory;
	}
	/**
	 * createMidweekClosure
	 * @param historyId
	 * @param employeeId
	 * @param remarks
	 * @param soInsPayCategory
	 * @param multiple
	 */
	public BsymtTempAbsHisItem (String historyId, String employeeId, int leaveHolidayAtr, String remarks,
			Integer soInsPayCategory, Boolean multiple) {
		this.histId = historyId;
		this.sid = employeeId;
		this.leaveHolidayAtr = leaveHolidayAtr;
		this.remarks = remarks;
		this.soInsPayCategory = soInsPayCategory;
		this.multiple = multiple ? 1: 0;
	}
	/**
	 * createAfterChildbirth
	 * @param historyId
	 * @param employeeId
	 * @param remarks
	 * @param soInsPayCategory
	 * @param familyMemberId
	 */
	public BsymtTempAbsHisItem (String historyId, String employeeId, int leaveHolidayAtr, String remarks,
			Integer soInsPayCategory, String familyMemberId) {
		this.histId = historyId;
		this.sid = employeeId;
		this.leaveHolidayAtr = leaveHolidayAtr;
		this.remarks = remarks;
		this.soInsPayCategory = soInsPayCategory;
		this.familyMemberId = familyMemberId;
	}
	/**
	 * createChildCareHoliday
	 * @param historyId
	 * @param employeeId
	 * @param remarks
	 * @param soInsPayCategory
	 * @param sameFamily
	 * @param childType
	 * @param familyMemberId
	 * @param createDate
	 * @param spouseIsLeave
	 */
	public BsymtTempAbsHisItem (String historyId, String employeeId, int leaveHolidayAtr, String remarks,
			Integer soInsPayCategory, Boolean sameFamily, Integer childType, String familyMemberId,
			GeneralDate createDate, Boolean spouseIsLeave) {
		this.histId = historyId;
		this.sid = employeeId;
		this.leaveHolidayAtr = leaveHolidayAtr;
		this.remarks = remarks;
		this.soInsPayCategory = soInsPayCategory;
		this.sameFamily = sameFamily ? 1: 0;
		this.childType = childType;
		this.familyMemberId = familyMemberId;
		this.createDate = createDate;
		this.spouseIsLeave = spouseIsLeave ? 1 : 0;
	}
	/**
	 * createCareHoliday
	 * @param historyId
	 * @param employeeId
	 * @param remarks
	 * @param soInsPayCategory
	 * @param sameFamily
	 * @param sameFamilyDays
	 * @param familyMemberId
	 */
	public BsymtTempAbsHisItem (String historyId, String employeeId, int leaveHolidayAtr, String remarks,
			Integer soInsPayCategory, Boolean sameFamily, Integer sameFamilyDays, String familyMemberId) {
		this.histId = historyId;
		this.sid = employeeId;
		this.leaveHolidayAtr = leaveHolidayAtr;
		this.remarks = remarks;
		this.soInsPayCategory = soInsPayCategory;
		this.sameFamily = sameFamily ? 1: 0;
		this.familyMemberId = familyMemberId;
		this.sameFamilyDays = sameFamilyDays;
	}
}
