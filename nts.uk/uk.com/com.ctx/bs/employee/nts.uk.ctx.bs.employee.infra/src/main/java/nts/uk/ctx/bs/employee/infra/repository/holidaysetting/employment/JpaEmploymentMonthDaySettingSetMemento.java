package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.employment;

import java.math.BigDecimal;
import java.util.List;

import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.PublicHolidayMonthSetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingSetMemento;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.employment.KshmtEmpMonthDaySet;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.employment.KshmtEmpMonthDaySetPK;

/**
 * The Class JpaEmploymentMonthDaySettingSetMemento.
 */
public class JpaEmploymentMonthDaySettingSetMemento implements EmploymentMonthDaySettingSetMemento{
	
	/** The list kshmt emp month day set. */
	private List<KshmtEmpMonthDaySet> listKshmtEmpMonthDaySet;
	
	/** The company id. */
	private String companyId;
	
	/** The emp cd. */
	private String empCd;
	
	/** The year. */
	private int year;

	/**
	 * Instantiates a new jpa employment month day setting set memento.
	 *
	 * @param entities the entities
	 */
	public JpaEmploymentMonthDaySettingSetMemento(List<KshmtEmpMonthDaySet> entities){
		entities.stream().forEach(item -> {
			if (item.getKshmtEmpMonthDaySetPK() == null) {
				item.setKshmtEmpMonthDaySetPK(new KshmtEmpMonthDaySetPK());
			}
		});
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingSetMemento#setCompanyId(nts.uk.ctx.bs.employee.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.companyId = companyId.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingSetMemento#setEmploymentCode(java.lang.String)
	 */
	@Override
	public void setEmploymentCode(String employmentCode) {
		this.empCd = employmentCode;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingSetMemento#setManagementYear(nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year)
	 */
	@Override
	public void setManagementYear(Year managementYear) {
		this.year = managementYear.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingSetMemento#setPublicHolidayMonthSettings(java.util.List)
	 */
	@Override
	public void setPublicHolidayMonthSettings(List<PublicHolidayMonthSetting> publicHolidayMonthSettings) {
		publicHolidayMonthSettings.stream().forEach(item -> {
			KshmtEmpMonthDaySet entity = new KshmtEmpMonthDaySet();
			entity.setKshmtEmpMonthDaySetPK(new KshmtEmpMonthDaySetPK());
			entity.getKshmtEmpMonthDaySetPK().setCid(this.companyId);
			entity.getKshmtEmpMonthDaySetPK().setEmpCd(this.empCd);
			entity.getKshmtEmpMonthDaySetPK().setManageYear(this.year);
			entity.getKshmtEmpMonthDaySetPK().setMonth(item.getMonth());
			entity.setInLegalHd(new BigDecimal(item.getInLegalHoliday().v()));
			
			this.listKshmtEmpMonthDaySet.add(entity);
		});
	}
}
