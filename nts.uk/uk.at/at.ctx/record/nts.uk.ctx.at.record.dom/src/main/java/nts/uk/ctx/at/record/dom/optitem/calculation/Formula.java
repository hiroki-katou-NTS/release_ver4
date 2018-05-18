/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem.calculation;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemNo;
import nts.uk.ctx.at.record.dom.optitem.PerformanceAtr;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class OptionalItemFormula.
 */
// 任意項目計算式
// 責務 : 計算式を作成する
// Responsibility: Create calculation formulas
@Getter
public class Formula extends AggregateRoot {

	/** The company id. */
	// 会社ID
	private CompanyId companyId;

	/** The formula id. */
	// 計算式ID
	private FormulaId formulaId;

	/** The optional item no. */
	// 任意項目NO
	private OptionalItemNo optionalItemNo;

	/** The symbol. */
	// 記号
	private Symbol symbol;

	/** The formula atr. */
	// 属性
	private OptionalItemAtr formulaAtr;

	/** The formula name. */
	// 計算式名称
	private FormulaName formulaName;

	/** The formula setting. */
	// 計算式設定
	private CalcFormulaSetting calcFormulaSetting;

	/** The calculation atr. */
	// 計算区分
	private CalculationAtr calcAtr;

	// ===================== Optional ======================= //
	/** The monthly rounding. */
	// 月別端数処理
	private Optional<Rounding> monthlyRounding;

	/** The daily rounding. */
	// 日別端数処理
	private Optional<Rounding> dailyRounding;

	/**
	 * Instantiates a new optional item formula.
	 *
	 * @param memento the memento
	 */
	public Formula(FormulaGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.formulaId = memento.getFormulaId();
		this.optionalItemNo = memento.getOptionalItemNo();
		this.formulaName = memento.getFormulaName();
		this.calcFormulaSetting = memento.getCalcFormulaSetting();
		this.calcAtr = memento.getCalcFormulaAtr();
		this.formulaAtr = memento.getFormulaAtr();
		this.symbol = memento.getSymbol();
		this.monthlyRounding = memento.getMonthlyRounding();
		this.dailyRounding = memento.getDailyRounding();

	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		super.validate();
		this.validateSymbol();
	}

	/**
	 * Validate symbol.
	 */
	private void validateSymbol() {
		if (this.symbol.greaterThan("ZZ")) {
			throw new BusinessException("Msg_508");
		}
	}


	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FormulaSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setFormulaId(this.formulaId);
		memento.setOptionalItemNo(this.optionalItemNo);
		memento.setFormulaAtr(this.formulaAtr);
		memento.setFormulaName(this.formulaName);
		memento.setSymbol(this.symbol);
		memento.setCalcAtr(this.calcAtr);
		memento.setCalcFormulaSetting(this.calcFormulaSetting);
		memento.setMonthlyRounding(this.monthlyRounding);
		memento.setDailyRounding(this.dailyRounding);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((formulaId == null) ? 0 : formulaId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Formula other = (Formula) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (formulaId == null) {
			if (other.formulaId != null)
				return false;
		} else if (!formulaId.equals(other.formulaId))
			return false;
		return true;
	}

	
	/**
	 * 計算設定を判定し各計算処理へ飛ばす
	 * @param optionalItem 任意項目
	 * @param performanceAtr 実績区分
	 * @param resultCalcFormula 計算結果(List)
	 * @return 計算式の結果クラス
	 */
	public ResultOfCalcFormula dicisionCalc(OptionalItem optionalItem ,PerformanceAtr performanceAtr,List<ResultOfCalcFormula> resultCalcFormula) {
		int calcValue = 0;
		if(this.getCalcAtr().isItemSelection()) {
			//計算式による計算
		}
		else if(this.getCalcAtr().isFormulaSetting()) {
			//項目選択による計算
		}
		else {
			throw new RuntimeException("unknown FormulaSetting"+ this.getCalcAtr());
		}
		
		return ResultOfCalcFormula.of(formulaId,this.formulaAtr, calcValue);
	}
}
