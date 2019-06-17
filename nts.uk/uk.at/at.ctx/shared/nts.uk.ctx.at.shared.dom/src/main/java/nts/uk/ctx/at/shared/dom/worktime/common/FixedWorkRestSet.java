/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class FixedWorkRestSet.
 */
//固定勤務の休憩設定
@Getter
@NoArgsConstructor
public class FixedWorkRestSet extends WorkTimeDomainObject implements Cloneable{

	/** The common rest set. */
	//共通の休憩設定
	private CommonRestSetting commonRestSet; 
	
	/** The is plan actual not match master refer. */
	//予定と実績の勤務が一致しな場合はマスタ参照
	private boolean isPlanActualNotMatchMasterRefer;
	
	/** The calculate method. */
	//計算方法
	private FixedRestCalculateMethod calculateMethod;

	/**
	 * Instantiates a new fixed work rest set.
	 *
	 * @param memento the memento
	 */
	public FixedWorkRestSet(FixedWorkRestSetGetMemento memento) {
		this.commonRestSet = memento.getCommonRestSet();
		this.isPlanActualNotMatchMasterRefer = memento.getIsPlanActualNotMatchMasterRefer();
		this.calculateMethod = memento.getCalculateMethod();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FixedWorkRestSetSetMemento memento) {
		memento.setCommonRestSet(this.commonRestSet);
		memento.setIsPlanActualNotMatchMasterRefer(this.isPlanActualNotMatchMasterRefer);
		memento.setCalculateMethod(this.calculateMethod);
	}
	
	/**
	 * 計算方法を予定時間参照へ切り替える
	 */
	public void changeCalcMethodToSche() {
		this.calculateMethod = FixedRestCalculateMethod.PLAN_REF;
	}
	
	@Override
	public FixedWorkRestSet clone() {
		FixedWorkRestSet cloned = new FixedWorkRestSet();
		try {
			cloned.commonRestSet = this.commonRestSet.clone();
			cloned.isPlanActualNotMatchMasterRefer = isPlanActualNotMatchMasterRefer ? true : false;
			cloned.calculateMethod = FixedRestCalculateMethod.valueOf(this.calculateMethod.value);
		}
		catch (Exception e){
			throw new RuntimeException("FixedWorkRestSet clone error.");
		}
		return cloned;
	}
}
