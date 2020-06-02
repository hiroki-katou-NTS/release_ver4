/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.DomainObject;
import nts.gul.serialize.binary.SerializableWithOptional;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;

/**
 * The Class PremiumHolidayCalcMethod.
 */
// 休暇の割増計算方法
@NoArgsConstructor
@Getter
public class PremiumHolidayCalcMethod extends DomainObject implements SerializableWithOptional{

	/** Serializable */
	private static final long serialVersionUID = 1L;
	
	/** The calculate actual operation. */
	// 実働のみで計算する
	private CalcurationByActualTimeAtr calculateActualOperation;
	
	/** The advance set. */
	// 詳細設定
	private Optional<PremiumCalcMethodDetailOfHoliday> advanceSet;

	private void writeObject(ObjectOutputStream stream){
		writeObjectWithOptional(stream);
	}
	
	private void readObject(ObjectInputStream stream){
		readObjectWithOptional(stream);
	}
	
	/**
	 * @param calculateActualOperation
	 * @param advanceSet
	 */
	public PremiumHolidayCalcMethod(int calculateActualOperation,
			PremiumCalcMethodDetailOfHoliday advanceSet) {
		super();
		this.calculateActualOperation = CalcurationByActualTimeAtr.valueOf(calculateActualOperation);
		this.advanceSet = Optional.ofNullable(advanceSet);
	}

	public PremiumHolidayCalcMethod(CalcurationByActualTimeAtr calculateActualOperation,
			Optional<PremiumCalcMethodDetailOfHoliday> advanceSet) {
		super();
		this.calculateActualOperation = calculateActualOperation;
		this.advanceSet = advanceSet;
	}

	public PremiumHolidayCalcMethod(int calculateActualOperation, Optional<PremiumCalcMethodDetailOfHoliday> emptyItem) {
		super();
		this.calculateActualOperation = CalcurationByActualTimeAtr.valueOf(calculateActualOperation);
		this.advanceSet = emptyItem;
	}
	
}

