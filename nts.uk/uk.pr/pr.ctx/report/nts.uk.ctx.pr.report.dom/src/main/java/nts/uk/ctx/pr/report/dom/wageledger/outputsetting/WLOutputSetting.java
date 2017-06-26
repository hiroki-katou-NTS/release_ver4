/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class OutputSetting.
 */
@Getter
public class WLOutputSetting extends DomainObject {
	
	/** The code. */
	private WLOutputSettingCode code;
	
	/** The name. */
	private WLOutputSettingName name;
	
	/** The once sheet per person. */
	private Boolean onceSheetPerPerson;
	
	/** The company code. */
	private String companyCode;
	
	/** The category settings. */
	private List<WLCategorySetting> categorySettings;

	/**
	 * Instantiates a new WL output setting.
	 *
	 * @param memento the memento
	 */
	public WLOutputSetting(WLOutputSettingGetMemento memento) {
		super();
		this.code = memento.getCode();
		this.name = memento.getName();
		this.onceSheetPerPerson = memento.getOnceSheetPerPerson();
		this.companyCode = memento.getCompanyCode();
		this.categorySettings = memento.getCategorySettings();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WLOutputSettingSetMemento memento) {
		memento.setCode(this.code);
		memento.setName(this.name);
		memento.setOnceSheetPerPerson(this.onceSheetPerPerson);
		memento.setCompanyCode(this.companyCode);
		memento.setCategorySettings(this.categorySettings);
	}

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
		WLOutputSetting other = (WLOutputSetting) obj;
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
	
}
