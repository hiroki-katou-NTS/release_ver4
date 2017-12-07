package nts.uk.ctx.bs.employee.app.command.temporaryabsence;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.PeregRecordId;

@Getter
public class UpdateTemporaryAbsenceCommand {
	/**社員ID*/
	@PeregEmployeeId
	private String employeeId;
	
	/**休職休業ID*/
	@PeregRecordId
	private String histoyId;
	
	@PeregItem("IS00089")
	private BigDecimal leaveHolidayAtr;
	
	/** 備考 */
	@PeregItem("IS00097")
	private String remarks;
	
	@PeregItem("IS00098")
	private BigDecimal soInsPayCategory;
	
	// -------------- extend object ----------------------
	/**
	 * 出産種別 （産前休業の場合）
	 */
	@PeregItem("IS00092")
	private boolean multiple;
	
	@PeregItem("IS00090")
	private boolean sameFamily;
	
	@PeregItem("IS00093")
	private BigDecimal childType;

	@PeregItem("IS00094")
	private GeneralDate createDate;

	@PeregItem("IS00096")
	private boolean spouseIsLeave;
	
	@PeregItem("IS00091")
	private BigDecimal sameFamilyDays;
	
	@PeregItem("IS00087")
	private GeneralDate startDate;
	
	@PeregItem("IS00088")
	private GeneralDate endDate;
	
	
	// TODO
	@PeregItem("IS00095")
	private Boolean childCareSameFamily;
	

	@PeregItem("")
	private String familyMemberId;
}
