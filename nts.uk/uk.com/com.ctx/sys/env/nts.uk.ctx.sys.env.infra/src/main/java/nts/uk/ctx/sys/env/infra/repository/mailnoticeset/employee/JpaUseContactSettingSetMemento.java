/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.env.infra.repository.mailnoticeset.employee;

import nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UseContactSettingSetMemento;
import nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UserInfoItem;
import nts.uk.ctx.sys.env.infra.entity.mailnoticeset.employee.SevstUseContactSet;
import nts.uk.ctx.sys.env.infra.entity.mailnoticeset.employee.SevstUseContactSetPK;

/**
 * The Class JpaUseContactSettingSetMemento.
 */
public class JpaUseContactSettingSetMemento implements UseContactSettingSetMemento{
	
	/** The entity. */
	private SevstUseContactSet entity;
	
	/** The company id. */
	private String companyId;
	
	/**
	 * Instantiates a new jpa use contact setting set memento.
	 *
	 * @param entity the entity
	 * @param companyId the company id
	 */
	public JpaUseContactSettingSetMemento(SevstUseContactSet entity,String companyId) {
		this.entity = entity;
		this.companyId = companyId;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UseContactSettingSetMemento#setEmployeeID(java.lang.String)
	 */
	@Override
	public void setEmployeeID(String EmployeeID) {
		SevstUseContactSetPK pk = new SevstUseContactSetPK(companyId, EmployeeID);
		this.entity.setSevstUseContactSetPK(pk);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UseContactSettingSetMemento#setSettingItem(nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UserInfoItem)
	 */
	@Override
	public void setSettingItem(UserInfoItem settingItem) {
		this.entity.setSetItem(settingItem.value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UseContactSettingSetMemento#setUseMailSetting(boolean)
	 */
	@Override
	public void setUseMailSetting(boolean useMailSetting) {
		this.entity.setUseMailSet(useMailSetting ? 1 : 0);
	}

}
