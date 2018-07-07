package nts.uk.ctx.exio.app.find.exo.exoutsummarysetting;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.exio.dom.exo.exoutsummaryservice.ExOutSummarySettingService;

@Stateless
public class ExOutSummarySettingFinder {
	@Inject
	private ExOutSummarySettingService service;
	
	public ExOutSummarySettingDto getExOutSummarySetting(String conditionSetCd) {
		return new ExOutSummarySettingDto(service.getExOutSummarySetting(conditionSetCd));
	}
}
