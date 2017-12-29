package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.workplace;

import java.util.List;

import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.PublicHolidayMonthSetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingSetMemento;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.workplace.KshmtWkpMonthDaySet;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.workplace.KshmtWkpMonthDaySetPK;

/**
 * The Class JpaWorkplaceMonthDaySettingSetMemento.
 */
public class JpaWorkplaceMonthDaySettingSetMemento implements WorkplaceMonthDaySettingSetMemento{
	
	/** The list kshmt wkp month day set. */
	private List<KshmtWkpMonthDaySet> listKshmtWkpMonthDaySet;
	
	/** The company id. */
	private String companyId;
	
	/** The workplace id. */
	private String workplaceId;
	
	/** The year. */
	private int year;

	/**
	 * Instantiates a new jpa workplace month day setting set memento.
	 *
	 * @param entities the entities
	 */
	public JpaWorkplaceMonthDaySettingSetMemento(List<KshmtWkpMonthDaySet> entities){
		entities.stream().forEach(item -> {
			if (item.getKshmtWkpMonthDaySetPK() == null) {
				item.setKshmtWkpMonthDaySetPK(new KshmtWkpMonthDaySetPK());
			}
		});
		this.listKshmtWkpMonthDaySet = entities;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingSetMemento#setCompanyId(nts.uk.ctx.bs.employee.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.companyId = companyId.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingSetMemento#setWorkplaceID(java.lang.String)
	 */
	@Override
	public void setWorkplaceID(String workplaceID) {
		this.workplaceId = workplaceID;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingSetMemento#setManagementYear(nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year)
	 */
	@Override
	public void setManagementYear(Year managementYear) {
		this.year = managementYear.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingSetMemento#setPublicHolidayMonthSettings(java.util.List)
	 */
	@Override
	public void setPublicHolidayMonthSettings(List<PublicHolidayMonthSetting> publicHolidayMonthSettings) {
		publicHolidayMonthSettings.stream().forEach(item -> {
			KshmtWkpMonthDaySet entity = new KshmtWkpMonthDaySet();
			entity.setKshmtWkpMonthDaySetPK(new KshmtWkpMonthDaySetPK());
			entity.getKshmtWkpMonthDaySetPK().setCid(this.companyId);
			entity.getKshmtWkpMonthDaySetPK().setWkpId(this.workplaceId);
			entity.getKshmtWkpMonthDaySetPK().setManageYear(this.year);
			entity.getKshmtWkpMonthDaySetPK().setMonth(item.getMonth());
			entity.setInLegalHd(item.getInLegalHoliday().v());
			
			this.listKshmtWkpMonthDaySet.add(entity);
		});
	}
}
