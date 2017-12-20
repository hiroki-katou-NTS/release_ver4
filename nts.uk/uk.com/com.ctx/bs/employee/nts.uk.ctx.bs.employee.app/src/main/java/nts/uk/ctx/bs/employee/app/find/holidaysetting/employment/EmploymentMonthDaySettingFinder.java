/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.holidaysetting.employment;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class EmploymentMonthDaySettingFinder.
 */
@Stateless
public class EmploymentMonthDaySettingFinder {
	/** The repository. */
	@Inject
	private EmploymentMonthDaySettingRepository repository;
	
	/**
	 * Gets the employee month day setting.
	 *
	 * @param empCd the emp cd
	 * @param year the year
	 * @return the employee month day setting
	 */
	public EmploymentMonthDaySettingDto getEmploymentMonthDaySetting(String empCd ,int year){
		String companyId = AppContexts.user().companyId();
		
		Optional<EmploymentMonthDaySetting> optional = this.repository.findByYear(new CompanyId(companyId), empCd ,new Year(year));
		
		if(optional.isPresent()){
			EmploymentMonthDaySetting domain = optional.get();
			
			EmploymentMonthDaySettingDto dto = new EmploymentMonthDaySettingDto();
			domain.saveToMemento(dto);
			
			return dto;
		}
		return null;
	}
}
