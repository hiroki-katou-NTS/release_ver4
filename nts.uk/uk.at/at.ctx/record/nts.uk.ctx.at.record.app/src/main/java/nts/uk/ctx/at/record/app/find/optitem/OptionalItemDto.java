/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.find.optitem;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.app.find.optitem.calculation.FormulaDto;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.scherec.optitem.CalcResultRange;
import nts.uk.ctx.at.shared.dom.scherec.optitem.CalcUsageAtr;
import nts.uk.ctx.at.shared.dom.scherec.optitem.EmpConditionAtr;
import nts.uk.ctx.at.shared.dom.scherec.optitem.OptionalItemAtr;
import nts.uk.ctx.at.shared.dom.scherec.optitem.OptionalItemName;
import nts.uk.ctx.at.shared.dom.scherec.optitem.OptionalItemNo;
import nts.uk.ctx.at.shared.dom.scherec.optitem.OptionalItemSetMemento;
import nts.uk.ctx.at.shared.dom.scherec.optitem.OptionalItemUsageAtr;
import nts.uk.ctx.at.shared.dom.scherec.optitem.PerformanceAtr;
import nts.uk.ctx.at.shared.dom.scherec.optitem.UnitOfOptionalItem;

/**
 * The Class OptionalItemDto.
 */
@Getter
@Setter
public class OptionalItemDto implements OptionalItemSetMemento {

	/** The optional item no. */
	private int optionalItemNo;

	/** The optional item name. */
	private String optionalItemName;

	/** The optional item atr. */
	private int optionalItemAtr;

	/** The usage classification. */
	private int usageAtr;

	/** The calculation atr. */
	private int calcAtr;

	/** The emp condition classification. */
	private int empConditionAtr;

	/** The performance classification. */
	private int performanceAtr;

	/** The calculation result range. */
	private CalcResultRangeDto calcResultRange;

	/** The formulas. */
	private List<FormulaDto> formulas;

	/** The unit. */
	private String unit;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setCompanyId(nts.uk.ctx.at.shared.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId comId) {
		// Not used.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setOptionalItemNo(nts.uk.ctx.at.shared.dom.timeitemmanagement.
	 * OptionalItemNo)
	 */
	@Override
	public void setOptionalItemNo(OptionalItemNo optionalItemNo) {
		this.optionalItemNo = optionalItemNo.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setOptionalItemName(nts.uk.ctx.at.shared.dom.timeitemmanagement.
	 * OptionalItemName)
	 */
	@Override
	public void setOptionalItemName(OptionalItemName optionalItemName) {
		this.optionalItemName = optionalItemName.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setOptionalItemAttribute(nts.uk.ctx.at.shared.dom.timeitemmanagement.
	 * OptionalItemAttribute)
	 */
	@Override
	public void setOptionalItemAtr(OptionalItemAtr optionalItemAttribute) {
		this.optionalItemAtr = optionalItemAttribute.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setOptionalItemUsageClassification(nts.uk.ctx.at.shared.dom.
	 * timeitemmanagement.OptionalItemUsageClassification)
	 */
	@Override
	public void setOptionalItemUsageAtr(OptionalItemUsageAtr optionalItemUsageClassification) {
		this.usageAtr = optionalItemUsageClassification.value;
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.scherec.optitem.OptionalItemSetMemento#
	 * setCalcAtr(nts.uk.ctx.at.shared.dom.scherec.optitem.CalcUsageAtr)
	 */
	@Override
	public void setCalcAtr(CalcUsageAtr calcAtr) {
		this.calcAtr = calcAtr.value;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setEmpConditionClassification(nts.uk.ctx.at.shared.dom.timeitemmanagement
	 * .EmpConditionClassification)
	 */
	@Override
	public void setEmpConditionAtr(EmpConditionAtr empConditionClassification) {
		this.empConditionAtr = empConditionClassification.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setPerformanceClassification(nts.uk.ctx.at.shared.dom.timeitemmanagement.
	 * PerformanceClassification)
	 */
	@Override
	public void setPerformanceAtr(PerformanceAtr performanceClassification) {
		this.performanceAtr = performanceClassification.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.timeitemmanagement.OptionalItemSetMemento#
	 * setCalculationResultRange(nts.uk.ctx.at.shared.dom.timeitemmanagement.
	 * CalculationResultRange)
	 */
	@Override
	public void setCalculationResultRange(CalcResultRange calculationResultRange) {
		this.calcResultRange = new CalcResultRangeDto();
		calculationResultRange.saveToMemento(this.calcResultRange);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.OptionalItemSetMemento#setUnit(nts.uk.
	 * ctx.at.record.dom.optitem.UnitOfOptionalItem)
	 */
	@Override
	public void setUnit(UnitOfOptionalItem unit) {
		this.unit = unit.v();
	}
}
