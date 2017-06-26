package nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing.ProcessingName;
import nts.uk.shr.com.primitive.sample.ProcessingNo;

/**
 * Aggregate Root: payment date processing master.
 *
 */
public class PaymentDateProcessingMaster extends AggregateRoot {
	/**
	 * Payroll bonus attribute
	 */
	@Getter
	private PayBonusAtr payBonusAttribute;

	/**
	 * Processing Number.
	 */
	@Getter
	private ProcessingNo processingNo;

	/**
	 * Processing Name.
	 */
	@Getter
	private ProcessingName processingName;

	/**
	 * Current processing year month.
	 */
	@Getter
	private YearMonth currentProcessingYm;

	/**
	 * Display attribute.
	 */
	@Getter
	private DisplayAtr displayAttribute;

	/**
	 * Constructor with parameter.
	 * 
	 * @param payBonusAtribute
	 *            Payroll Bonus Attribute
	 * @param processingNo
	 *            Processing No
	 * @param processingName
	 *            Processing Name
	 * @param currentProcessingYm
	 *            Current Processing Year Month
	 * @param displayAtribute
	 *            Display Attribute
	 */
	public PaymentDateProcessingMaster(PayBonusAtr payBonusAttribute, ProcessingNo processingNo,
			ProcessingName processingName, YearMonth currentProcessingYm, DisplayAtr displayAttribute) {
		super();
		this.payBonusAttribute = payBonusAttribute;
		this.processingNo = processingNo;
		this.processingName = processingName;
		this.currentProcessingYm = currentProcessingYm;
		this.displayAttribute = displayAttribute;
	}

	/**
	 * Create instance using Java type parameters.
	 */
	public static PaymentDateProcessingMaster createFromJavaType(int payBonusAtr, int processingNo,
			String processingName, int currentProcessingYm, int displayAttribute) {
		return new PaymentDateProcessingMaster(EnumAdaptor.valueOf(payBonusAtr, PayBonusAtr.class),
				new ProcessingNo(processingNo), new ProcessingName(processingName), new YearMonth(currentProcessingYm),
				EnumAdaptor.valueOf(displayAttribute, DisplayAtr.class));
	}
}
