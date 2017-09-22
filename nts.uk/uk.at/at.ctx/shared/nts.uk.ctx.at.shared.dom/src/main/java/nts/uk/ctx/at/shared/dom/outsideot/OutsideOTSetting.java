/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.outsideot;

import java.util.List;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.OvertimeNote;

/**
 * OT = Overtime
 * The Class OutsideOTSetting.
 */
//時間外超過設定
@Getter
public class OutsideOTSetting extends AggregateRoot{
	
	/** The company id. */
	// 会社ID
	private CompanyId companyId;

	/** The note. */
	// 備考
	private OvertimeNote note;
	
	/** The breakdown items. */
	// 内訳項目一覧
	private List<OutsideOTBRDItem> breakdownItems;
	
	/** The calculation method. */
	// 計算方法
	private OutsideOTCalMed calculationMethod;
	
	/** The over times. */
	// 超過時間一覧
	private List<Overtime> overtimes;
	
	/**
	 * Instantiates a new overtime setting.
	 *
	 * @param memento the memento
	 */
	public OutsideOTSetting(OutsideOTSettingGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.note = memento.getNote();
		this.breakdownItems = memento.getBreakdownItems();
		this.calculationMethod = memento.getCalculationMethod();
		this.overtimes = memento.getOvertimes();
		
		// validate domain
		if(CollectionUtil.isEmpty(this.breakdownItems)){
			throw new BusinessException("Msg_485");
		}
		if (!checkUseBreakdownItem()) {
			throw new BusinessException("Msg_485");
		}
		if(CollectionUtil.isEmpty(this.overtimes)){
			throw new BusinessException("Msg_486");
		}
		if (!checkUseOvertime()) {
			throw new BusinessException("Msg_486");
		}
		if(this.checkOverlapProductNumber()){
			throw new BusinessException("Msg_490");
		}
	}
	
	/**
	 * Check use breakdown item.
	 *
	 * @return true, if successful
	 */
	private boolean checkUseBreakdownItem() {
		for (OutsideOTBRDItem breakdownItem : this.breakdownItems) {
			if (breakdownItem
					.getUseClassification().value == UseClassification.UseClass_Use.value) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check use overtime.
	 *
	 * @return true, if successful
	 */
	private boolean checkUseOvertime() {
		for (Overtime overtime : this.overtimes) {
			if (overtime.getUseClassification().value == UseClassification.UseClass_Use.value) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check overlap product number.
	 *
	 * @return true, if successful
	 */
	private boolean checkOverlapProductNumber() {
		for (OutsideOTBRDItem breakdownItem1 : this.breakdownItems) {
			for (OutsideOTBRDItem breakdownItem2 : this.breakdownItems) {
				if (breakdownItem1.getBreakdownItemNo().value != breakdownItem2
						.getBreakdownItemNo().value
						&& breakdownItem1.getProductNumber().value == breakdownItem2
								.getProductNumber().value) {
					return true;
				}
			}
		}
		return false;
	}
	

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(OutsideOTSettingSetMemento memento){
		memento.setCompanyId(this.companyId);
		memento.setNote(this.note);
		memento.setBreakdownItems(this.breakdownItems);
		memento.setCalculationMethod(this.calculationMethod);
		memento.setOvertimes(this.overtimes);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
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
		OutsideOTSetting other = (OutsideOTSetting) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}

}
