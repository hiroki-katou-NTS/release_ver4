/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.printsetting.service.internal;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.SalaryPrintSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.printsetting.service.SalaryPrintSettingService;

/**
 * The Class SalaryPrintSettingServiceImpl.
 */
@Stateless
public class SalaryPrintSettingServiceImpl implements SalaryPrintSettingService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.service.
	 * SalaryPrintSettingService#validateRequiredItem(nts.uk.ctx.pr.report.dom.
	 * salarydetail.printsetting.SalaryPrintSetting)
	 */
	@Override
	public void validateRequiredItem(SalaryPrintSetting setting) {
		if (setting.getHrchyIndex1() == null || setting.getHrchyIndex2() == null
				|| setting.getHrchyIndex3() == null || setting.getHrchyIndex4() == null
				|| setting.getHrchyIndex5() == null || setting.getHrchyIndex6() == null
				|| setting.getHrchyIndex7() == null || setting.getHrchyIndex8() == null
				|| setting.getHrchyIndex9() == null || setting.getMonthTotalSet() == null
				|| setting.getShowPayment() == null || setting.getTotalSet() == null
				|| setting.getSumDepHrchyIndexSet() == null || setting.getSumEachDeprtSet() == null
				|| setting.getSumMonthDepHrchySet() == null || setting.getSumMonthDeprtSet() == null
				|| setting.getSumMonthPersonSet() == null || setting.getSumPersonSet() == null) {
			throw new BusinessException("ER001");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.printsetting.service.
	 * SalaryPrintSettingService#validateHrchyCount(nts.uk.ctx.pr.report.dom.
	 * salarydetail.printsetting.SalaryPrintSetting)
	 */
	@Override
	public void validateSelection(SalaryPrintSetting setting) {

		// Check setting selection.
		if (!isCorrectSetting(setting)) {
			throw new BusinessException("ER031");
		}

		// Count selection.
		int count = (setting.getHrchyIndex1() ? 1 : 0) + (setting.getHrchyIndex2() ? 1 : 0)
				+ (setting.getHrchyIndex3() ? 1 : 0) + (setting.getHrchyIndex4() ? 1 : 0)
				+ (setting.getHrchyIndex5() ? 1 : 0) + (setting.getHrchyIndex6() ? 1 : 0)
				+ (setting.getHrchyIndex7() ? 1 : 0) + (setting.getHrchyIndex8() ? 1 : 0)
				+ (setting.getHrchyIndex9() ? 1 : 0);

		if (setting.getSumDepHrchyIndexSet() || setting.getSumMonthDepHrchySet()) {
			// No select
			// Select leastest once item
			if (count == 0) {
				// TODO: find msg id
				throw new BusinessException("部門階層が正しく指定されていません");
			}

			// No more 5 items
			if (count > 5) {
				// TODO: find msg id
				throw new BusinessException("部門階層が正しく指定されていません");
			}
		}
	}

	/**
	 * Checks if is correct setting.
	 *
	 * @param setting
	 *            the setting
	 * @return true, if is correct setting
	 */
	private boolean isCorrectSetting(SalaryPrintSetting setting) {
		// 明細 payment
		// 総合計 total
		// 総合 月計 month total
		// 個人計 person
		// 個人 月計 month person
		// 部門計 depart
		// 部門 月計 month depart
		// 部門階層累計 index_set
		// 部門階層累計 月計 hrchy_set
		return setting.getShowPayment() || setting.getTotalSet() || setting.getMonthTotalSet()
				|| setting.getSumPersonSet() || setting.getSumMonthPersonSet()
				|| setting.getSumEachDeprtSet() || setting.getSumMonthDeprtSet()
				|| setting.getSumDepHrchyIndexSet() || setting.getSumMonthDepHrchySet();
	}

}
