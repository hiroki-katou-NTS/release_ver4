package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.company;

import java.math.BigDecimal;
import java.util.List;

import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.PublicHolidayMonthSetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.company.CompanyMonthDaySettingSetMemento;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.company.KshmtComMonthDaySet;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.company.KshmtComMonthDaySetPK;


/**
 * The Class JpaCompanyMonthDaySettingSetMemento.
 */
public class JpaCompanyMonthDaySettingSetMemento implements CompanyMonthDaySettingSetMemento{
	
	/** The list kshmt com month day set. */
	private List<KshmtComMonthDaySet> listKshmtComMonthDaySet;
	
	/** The company id. */
	private String companyId;
	
	/** The year. */
	private int year;

	/**
	 * Instantiates a new jpa company month day setting set memento.
	 *
	 * @param entities the entities
	 */
	public JpaCompanyMonthDaySettingSetMemento(List<KshmtComMonthDaySet> entities){
		entities.stream().forEach(item -> {
			if (item.getKshmtComMonthDaySetPK() == null) {
				item.setKshmtComMonthDaySetPK(new KshmtComMonthDaySetPK());
			}
		});
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.company.CompanyMonthDaySettingSetMemento#setCompanyId(nts.uk.ctx.bs.employee.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.companyId = companyId.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.company.CompanyMonthDaySettingSetMemento#setManagementYear(nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year)
	 */
	@Override
	public void setManagementYear(Year managementYear) {
		this.year = managementYear.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.company.CompanyMonthDaySettingSetMemento#setPublicHolidayMonthSettings(java.util.List)
	 */
	@Override
	public void setPublicHolidayMonthSettings(List<PublicHolidayMonthSetting> publicHolidayMonthSettings) {
		publicHolidayMonthSettings.stream().forEach(item -> {
			KshmtComMonthDaySet entity = new KshmtComMonthDaySet();
			entity.setKshmtComMonthDaySetPK(new KshmtComMonthDaySetPK());
			entity.getKshmtComMonthDaySetPK().setCid(this.companyId);
			entity.getKshmtComMonthDaySetPK().setManageYear(this.year);
			entity.getKshmtComMonthDaySetPK().setMonth(item.getMonth());
			entity.setInLegalHd(new BigDecimal(item.getInLegalHoliday().v()));
			
			this.listKshmtComMonthDaySet.add(entity);
		});
	}
	
}
