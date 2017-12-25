package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.employee;

import java.math.BigDecimal;
import java.util.List;

import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.PublicHolidayMonthSetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.employee.EmployeeMonthDaySettingSetMemento;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.employee.KshmtEmployeeMonthDaySet;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.employee.KshmtEmployeeMonthDaySetPK;

/**
 * The Class JpaEmployeeMonthDaySettingSetMemento.
 */
public class JpaEmployeeMonthDaySettingSetMemento implements EmployeeMonthDaySettingSetMemento{
	

	/** The list kshmt employee month day set. */
	private List<KshmtEmployeeMonthDaySet> listKshmtEmployeeMonthDaySet;
	
	/** The company id. */
	private String companyId;
	
	/** The s id. */
	private String sId;
	
	/** The year. */
	private int year;

	/**
	 * Instantiates a new jpa employee month day setting set memento.
	 *
	 * @param entities the entities
	 */
	public JpaEmployeeMonthDaySettingSetMemento(List<KshmtEmployeeMonthDaySet> entities){
		entities.stream().forEach(item -> {
			if (item.getKshmtEmployeeMonthDaySetPK() == null) {
				item.setKshmtEmployeeMonthDaySetPK(new KshmtEmployeeMonthDaySetPK());
			}
		});
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employee.EmployeeMonthDaySettingSetMemento#setCompanyId(nts.uk.ctx.bs.employee.dom.common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.companyId = companyId.v();
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employee.EmployeeMonthDaySettingSetMemento#setEmployeeId(java.lang.String)
	 */
	@Override
	public void setEmployeeId(String employeeId) {
		this.sId = employeeId;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employee.EmployeeMonthDaySettingSetMemento#setManagementYear(nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year)
	 */
	@Override
	public void setManagementYear(Year managementYear) {
		this.year = managementYear.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employee.EmployeeMonthDaySettingSetMemento#setPublicHolidayMonthSettings(java.util.List)
	 */
	@Override
	public void setPublicHolidayMonthSettings(List<PublicHolidayMonthSetting> publicHolidayMonthSettings) {
		publicHolidayMonthSettings.stream().forEach(item -> {
			KshmtEmployeeMonthDaySet entity = new KshmtEmployeeMonthDaySet();
			entity.getKshmtEmployeeMonthDaySetPK().setCid(this.companyId);
			entity.getKshmtEmployeeMonthDaySetPK().setSid(this.sId);
			entity.getKshmtEmployeeMonthDaySetPK().setManageYear(this.year);
			entity.getKshmtEmployeeMonthDaySetPK().setMonth(item.getMonth());
			entity.setInLegalHd(new BigDecimal(item.getInLegalHoliday().v()));
			entity.setOutLegalHd(new BigDecimal(item.getOutLegalHoliday().v()));
			
			this.listKshmtEmployeeMonthDaySet.add(entity);
		});
	}
	
}
