/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.printsetting.find;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.report.app.salarydetail.printsetting.find.dto.SalaryPrintSettingDto;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SalaryPrintSettingFinder.
 */
@Stateless
public class SalaryPrintSettingFinder {

	/** The repository. */
	@Inject
	private SalaryPrintSettingRepository repository;

	/**
	 * Find.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the salary print setting dto
	 */
	public SalaryPrintSettingDto find() {
		String companyCode = AppContexts.user().companyCode();
		SalaryPrintSetting domain = repository.find(companyCode);
		SalaryPrintSettingDto dto = SalaryPrintSettingDto.builder().companyCode(companyCode)
				.hrchyIndex1(domain.getHrchyIndex1()).hrchyIndex2(domain.getHrchyIndex2())
				.hrchyIndex3(domain.getHrchyIndex3()).hrchyIndex4(domain.getHrchyIndex4())
				.hrchyIndex5(domain.getHrchyIndex5()).hrchyIndex6(domain.getHrchyIndex6())
				.hrchyIndex7(domain.getHrchyIndex7()).hrchyIndex8(domain.getHrchyIndex8())
				.hrchyIndex9(domain.getHrchyIndex9()).totalSet(domain.getTotalSet())
				.monthTotalSet(domain.getMonthTotalSet()).showPayment(domain.getShowPayment())
				.sumDepHrchyIndexSet(domain.getSumDepHrchyIndexSet()).sumEachDeprtSet(domain.getSumEachDeprtSet())
				.sumMonthDepHrchySet(domain.getSumMonthDepHrchySet()).sumMonthDeprtSet(domain.getSumMonthDeprtSet())
				.sumMonthPersonSet(domain.getSumMonthPersonSet()).sumPersonSet(domain.getSumPersonSet()).build();
		return dto;
	}
}
