/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.insurance.find;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.report.app.insurance.find.dto.CheckListPrintSettingDto;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSetting;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class ChecklistPrintSettingFinder.
 */
@Stateless
public class ChecklistPrintSettingFinder {

	/** The checklist print setting repository. */
	@Inject
	private ChecklistPrintSettingRepository checklistPrintSettingRepository;

	/**
	 * Find.
	 *
	 * @return the check list print setting find out dto
	 */
	public CheckListPrintSettingDto find() {
		String companyCode = AppContexts.user().companyCode();
		CheckListPrintSettingDto dto = CheckListPrintSettingDto.createDefaultSetting();
		
		// Find check list setting.
		Optional<ChecklistPrintSetting> printSetting = this.checklistPrintSettingRepository
				.findByCompanyCode(companyCode);
		if (printSetting.isPresent()) {
			printSetting.get().saveToMemento(dto);
		}
		return dto;
	}

}
