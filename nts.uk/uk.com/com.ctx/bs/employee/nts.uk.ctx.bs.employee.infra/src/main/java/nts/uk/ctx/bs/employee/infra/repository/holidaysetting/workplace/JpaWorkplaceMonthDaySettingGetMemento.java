package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.workplace;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.MonthlyNumberOfDays;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.PublicHolidayMonthSetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingGetMemento;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.workplace.KshmtWkpMonthDaySet;

/**
 * The Class JpaWorkplaceMonthDaySettingGetMemento.
 */
public class JpaWorkplaceMonthDaySettingGetMemento implements WorkplaceMonthDaySettingGetMemento {
	
	/** The list kshmt wkp month day set. */
	private List<KshmtWkpMonthDaySet> listKshmtWkpMonthDaySet;
	
	/**
	 * Instantiates a new jpa workplace month day setting get memento.
	 *
	 * @param entities the entities
	 */
	public JpaWorkplaceMonthDaySettingGetMemento(List<KshmtWkpMonthDaySet> entities){
		this.listKshmtWkpMonthDaySet = entities;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingGetMemento#getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(this.listKshmtWkpMonthDaySet.get(0).getKshmtWkpMonthDaySetPK().getCid());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingGetMemento#getWorkplaceID()
	 */
	@Override
	public String getWorkplaceID() {
		return this.listKshmtWkpMonthDaySet.get(0).getKshmtWkpMonthDaySetPK().getWkpId();
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingGetMemento#getManagementYear()
	 */
	@Override
	public Year getManagementYear() {
		return new Year(this.listKshmtWkpMonthDaySet.get(0).getKshmtWkpMonthDaySetPK().getManageYear());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.workplace.WorkplaceMonthDaySettingGetMemento#getPublicHolidayMonthSettings()
	 */
	@Override
	public List<PublicHolidayMonthSetting> getPublicHolidayMonthSettings() {
		return this.listKshmtWkpMonthDaySet.stream().map(e -> {
			PublicHolidayMonthSetting domain = new PublicHolidayMonthSetting(new Year(e.getKshmtWkpMonthDaySetPK().getManageYear()),
					new Integer(e.getKshmtWkpMonthDaySetPK().getMonth()),
					new MonthlyNumberOfDays(e.getInLegalHd().intValue()));
			return domain;
		}).collect(Collectors.toList());
	}
}
