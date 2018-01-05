package nts.uk.ctx.at.shared.app.command.calculation.holiday;

import lombok.Data;

/**
 * 
 * @author phongtq
 *
 */
@Data
public class WorkDepLaborCommand {
	/** 会社ID */
	private String companyId;

	/** 実働のみで計算する */
	private int calcActualOperationPre;

	/** インターバル免除時間を含めて計算する */
	private int exemptTaxTimePre;

	/** 育児・介護時間を含めて計算する */
	private int incChildNursingCarePre;

	/** フレックスの所定超過時 */
	private int predeterminedOvertimePre;

	/** 加算する */
	private int additionTimePre;

	/** 遅刻・早退を控除しない */
	private int notDeductLateleavePre;

	/** 通常、変形の所定超過時 */
	private int deformatExcValue;

	/** インターバル免除時間を含めて計算する */
	private int exemptTaxTimeWork;

	/** 欠勤時間をマイナスする */
	private int minusAbsenceTimeWork;

	/** 実働のみで計算する */
	private int calcActualOperationWork;

	/** 育児・介護時間を含めて計算する */
	private int incChildNursingCareWork;

	/** 遅刻・早退を控除しない */
	private int notDeductLateleaveWork;

	/** フレックスの所定不足時 */
	private int predeterminDeficiencyWork;

	/** 加算する */
	private int additionTimeWork;
}
