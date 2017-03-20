/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.report.dom.company.CompanyCode;

/**
 * The Class SalaryOutputSetting.
 */
@Getter
public class SalaryOutputSetting extends DomainObject {

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
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
		SalaryOutputSetting other = (SalaryOutputSetting) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		return true;
	}

	/** The company code. */
	private CompanyCode companyCode;

	/** The code. */
	private SalaryOutputSettingCode code;

	/** The name. */
	private SalaryOutputSettingName name;

	/** The category settings. */
	private List<SalaryCategorySetting> categorySettings;

	/**
	 * Instantiates a new salary output setting.
	 *
	 * @param memento the memento
	 */
	public SalaryOutputSetting(SalaryOutputSettingGetMemento memento) {
		super();
		this.companyCode = memento.getCompanyCode();
		this.code = memento.getCode();
		this.name = memento.getName();
		this.categorySettings = memento.getCategorySettings();

	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(SalaryOutputSettingSetMemento memento) {
		memento.setCode(this.code);
		memento.setName(this.name);
		memento.setCompanyCode(this.companyCode);
		memento.setCategorySettings(this.categorySettings);
	}
}
