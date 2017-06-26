package nts.uk.ctx.pr.formula.dom.formula;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.formula.dom.enums.AdjustmentAtr;
import nts.uk.ctx.pr.formula.dom.enums.BaseMoneyAtr;
import nts.uk.ctx.pr.formula.dom.enums.DivideValueSet;
import nts.uk.ctx.pr.formula.dom.enums.EasyFormulaTypeAtr;
import nts.uk.ctx.pr.formula.dom.enums.RoundAtr;
import nts.uk.ctx.pr.formula.dom.primitive.DivideValue;
import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.EasyFormulaName;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.primitive.Money;
import nts.uk.ctx.pr.formula.dom.primitive.PremiumRate;
import nts.uk.ctx.pr.formula.dom.primitive.WorkItemCode;
import nts.uk.ctx.pr.formula.dom.primitive.WorkValue;

/**
 * @author hungnm
 *
 */
@Getter
public class FormulaEasyDetail extends AggregateRoot {

	private String companyCode;

	private FormulaCode formulaCode;

	private String historyId;

	private EasyFormulaCode easyFormulaCode;

	private EasyFormulaName easyFormulaName;

	private EasyFormulaTypeAtr easyFormulaTypeAtr;

	private Money baseFixedAmount;

	private BaseMoneyAtr baseAmountDevision;

	private DivideValue baseFixedValue;

	private DivideValueSet baseValueDevision;

	private PremiumRate premiumRate;

	private RoundAtr roundProcessingDevision;

	private WorkItemCode coefficientDivision;

	private WorkValue coefficientFixedValue;

	private AdjustmentAtr adjustmentDevision;

	private RoundAtr totalRounding;

	/**
	 * @param companyCode
	 * @param formulaCode
	 * @param historyId
	 * @param easyFormulaCode
	 * @param easyFormulaName
	 * @param easyFormulaTypeAtr
	 * @param baseFixedAmount
	 * @param baseAmountDevision
	 * @param baseFixedValue
	 * @param baseValueDevision
	 * @param premiumRate
	 * @param roundProcessingDevision
	 * @param coefficientDivision
	 * @param coefficientFixedValue
	 * @param adjustmentDevision
	 * @param totalRounding
	 * @param limitValue
	 */
	public FormulaEasyDetail(String companyCode, FormulaCode formulaCode, String historyId,
			EasyFormulaCode easyFormulaCode, EasyFormulaName easyFormulaName, EasyFormulaTypeAtr easyFormulaTypeAtr,
			Money baseFixedAmount, BaseMoneyAtr baseAmountDevision, DivideValue baseFixedValue,
			DivideValueSet baseValueDevision, PremiumRate premiumRate, RoundAtr roundProcessingDevision,
			WorkItemCode coefficientDivision, WorkValue coefficientFixedValue, AdjustmentAtr adjustmentDevision,
			RoundAtr totalRounding) {
		super();
		this.companyCode = companyCode;
		this.formulaCode = formulaCode;
		this.historyId = historyId;
		this.easyFormulaCode = easyFormulaCode;
		this.easyFormulaName = easyFormulaName;
		this.easyFormulaTypeAtr = easyFormulaTypeAtr;
		this.baseFixedAmount = baseFixedAmount;
		this.baseAmountDevision = baseAmountDevision;
		this.baseFixedValue = baseFixedValue;
		this.baseValueDevision = baseValueDevision;
		this.premiumRate = premiumRate;
		this.roundProcessingDevision = roundProcessingDevision;
		this.coefficientDivision = coefficientDivision;
		this.coefficientFixedValue = coefficientFixedValue;
		this.adjustmentDevision = adjustmentDevision;
		this.totalRounding = totalRounding;
	}

	public static FormulaEasyDetail createFromJavaType(String companyCode, String formulaCode, String historyId,
			String easyFormulaCode, String easyFormulaName, BigDecimal easyFormulaTypeAtr, BigDecimal baseFixedAmount,
			BigDecimal baseAmountDevision, BigDecimal baseFixedValue, BigDecimal baseValueDevision,
			BigDecimal premiumRate, BigDecimal roundProcessingDevision, String coefficientDivision,
			BigDecimal coefficientFixedValue, BigDecimal adjustmentDevision, BigDecimal totalRounding) {
		return new FormulaEasyDetail(companyCode, new FormulaCode(formulaCode), historyId,
				new EasyFormulaCode(easyFormulaCode), new EasyFormulaName(easyFormulaName),
				EnumAdaptor.valueOf(easyFormulaTypeAtr.intValue(), EasyFormulaTypeAtr.class),
				new Money(baseFixedAmount), EnumAdaptor.valueOf(baseAmountDevision.intValue(), BaseMoneyAtr.class),
				new DivideValue(baseFixedValue),
				EnumAdaptor.valueOf(baseValueDevision.intValue(), DivideValueSet.class), new PremiumRate(premiumRate),
				EnumAdaptor.valueOf(roundProcessingDevision.intValue(), RoundAtr.class),
				new WorkItemCode(coefficientDivision), new WorkValue(coefficientFixedValue),
				EnumAdaptor.valueOf(adjustmentDevision.intValue(), AdjustmentAtr.class),
				EnumAdaptor.valueOf(totalRounding.intValue(), RoundAtr.class));
	}

	public void validate() {
		if (StringUtil.isNullOrEmpty(easyFormulaName.v(), true)
				|| StringUtil.isNullOrEmpty(baseFixedAmount.toString(), true)
				|| StringUtil.isNullOrEmpty(premiumRate.toString(), true)
				|| StringUtil.isNullOrEmpty(baseFixedValue.toString(), true)
				|| StringUtil.isNullOrEmpty(coefficientFixedValue.toString(), true)) {
			throw new BusinessException("ER001");
		}
	}

}
