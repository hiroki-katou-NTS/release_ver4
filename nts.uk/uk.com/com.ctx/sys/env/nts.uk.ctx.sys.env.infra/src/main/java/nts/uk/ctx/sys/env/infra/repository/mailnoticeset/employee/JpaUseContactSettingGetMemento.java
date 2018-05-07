package nts.uk.ctx.sys.env.infra.repository.mailnoticeset.employee;

import nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UseContactSettingGetMemento;
import nts.uk.ctx.sys.env.dom.mailnoticeset.employee.UserInfoItem;
import nts.uk.ctx.sys.env.infra.entity.mailnoticeset.employee.SevstUseContactSet;

public class JpaUseContactSettingGetMemento implements UseContactSettingGetMemento {

	private static final Integer TRUE_VAL = 1;
	private SevstUseContactSet entity;
	
	public JpaUseContactSettingGetMemento(SevstUseContactSet e) {
		this.entity = e;
	}

	@Override
	public String getEmployeeID() {
		return this.entity.getSevstUseContactSetPK().getSid();
	}

	@Override
	public UserInfoItem getSettingItem() {
		return UserInfoItem.valueOf(this.entity.getSetItem());
	}

	@Override
	public boolean isUseMailSetting() {
		return this.entity.getUseMailSet() == TRUE_VAL;
	}

}
