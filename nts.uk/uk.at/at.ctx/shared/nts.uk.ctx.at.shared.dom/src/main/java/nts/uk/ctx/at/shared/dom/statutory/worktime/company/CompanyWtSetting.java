/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.statutory.worktime.company;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.amountrounding.AmountUnit;
import nts.uk.ctx.at.shared.dom.statutory.worktime.shared.DeformationLaborSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.shared.FlexSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.shared.NormalSetting;

/**
 * 会社労働時間設定.
 */
@Getter
public class CompanyWtSetting extends AggregateRoot {

	/** フレックス勤務労働時間設定. */
	private FlexSetting flexSetting;

	/** 変形労働労働時間設定. */
	private DeformationLaborSetting deformationLaborSetting;

	/** 年. */
	private Year year;

	/** 会社ID. */
	private CompanyId companyId;

	/** 通常勤務労働時間設定. */
	private NormalSetting normalSetting;

	/**
	 * Instantiates a new company setting.
	 */
	public CompanyWtSetting() {
	}

	/**
	 * Instantiates a new company setting.
	 *
	 * @param memento the memento
	 */
	public CompanyWtSetting(CompanyWtSettingGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.flexSetting = memento.getFlexSetting();
		this.deformationLaborSetting = memento.getDeformationLaborSetting();
		this.normalSetting = memento.getNormalSetting();
		this.year = memento.getYear();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(CompanyWtSettingSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setYear(this.year);
		memento.setDeformationLaborSetting(this.deformationLaborSetting);
		memento.setFlexSetting(this.flexSetting);
		memento.setNormalSetting(this.normalSetting);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		CompanyWtSetting other = (CompanyWtSetting) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
}
