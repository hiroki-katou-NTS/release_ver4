/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pub.employee;

import lombok.Getter;
import lombok.Setter;

/**
 * 休職休業枠
 * The Class TempAbsenceFrameExport.
 */

@Getter
@Setter
public class TempAbsenceFrameExport {
	
	/** The company id. */
	// 会社ID
	private String companyId;
	
	/** The temp absence fr no. */
	//休職休業枠NO (min = "1", max = "10")
	private int tempAbsenceFrNo;
	
	/** The use classification. */
	//使用区分 (USE(1) - NOT_USE(0))
	private int useClassification;
	
	/** The temp absence fr name. */
	//休職休業枠名称
	private String tempAbsenceFrName;
	
	public TempAbsenceFrameExport() {
		super();
	}	
	
	public TempAbsenceFrameExport(String companyId, int tempAbsenceFrNo, int useClassification,
			String tempAbsenceFrName) {
		super();
		this.companyId = companyId;
		this.tempAbsenceFrNo = tempAbsenceFrNo;
		this.useClassification = useClassification;
		this.tempAbsenceFrName = tempAbsenceFrName;
	}
}
