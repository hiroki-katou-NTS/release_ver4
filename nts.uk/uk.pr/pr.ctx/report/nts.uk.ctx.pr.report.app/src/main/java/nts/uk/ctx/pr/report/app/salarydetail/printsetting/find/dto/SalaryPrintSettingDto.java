/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.printsetting.find.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SalaryPrintSettingDto {

	/** The company code. */
	private String companyCode;

	/** The show payment. */
	private Boolean showPayment;

	/** The sum person set. */
	private Boolean sumPersonSet;

	/** The sum month person set. */
	private Boolean sumMonthPersonSet;

	/** The sum each deprt set. */
	private Boolean sumEachDeprtSet;

	/** The sum month deprt set. */
	private Boolean sumMonthDeprtSet;

	/** The sum dep hrchy index set. */
	private Boolean sumDepHrchyIndexSet;

	/** The sum month dep hrchy set. */
	private Boolean sumMonthDepHrchySet;

	/** The hrchy index 1. */
	private Boolean hrchyIndex1;

	/** The hrchy index 2. */
	private Boolean hrchyIndex2;

	/** The hrchy index 3. */
	private Boolean hrchyIndex3;

	/** The hrchy index 4. */
	private Boolean hrchyIndex4;

	/** The hrchy index 5. */
	private Boolean hrchyIndex5;

	/** The hrchy index 6. */
	private Boolean hrchyIndex6;

	/** The hrchy index 7. */
	private Boolean hrchyIndex7;

	/** The hrchy index 8. */
	private Boolean hrchyIndex8;

	/** The hrchy index 9. */
	private Boolean hrchyIndex9;

	/** The total set. */
	private Boolean totalSet;

	/** The month total set. */
	private Boolean monthTotalSet;
}
