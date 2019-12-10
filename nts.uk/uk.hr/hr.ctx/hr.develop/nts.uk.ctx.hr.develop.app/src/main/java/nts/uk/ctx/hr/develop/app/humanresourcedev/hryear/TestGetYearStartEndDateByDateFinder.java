package nts.uk.ctx.hr.develop.app.humanresourcedev.hryear;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.hr.develop.dom.humanresourcedev.hryear.service.IGetYearStartEndDateByDate;
import nts.uk.ctx.hr.develop.dom.humanresourcedev.hryear.service.YearStartEnd;
@Stateless
public class TestGetYearStartEndDateByDateFinder {
	@Inject
	private IGetYearStartEndDateByDate getYearStartEndDateByDateImpl;
	
	
	public YearStartEnd getByDate(String companyId, GeneralDate baseDate) {
		return getYearStartEndDateByDateImpl.getByDate(companyId, baseDate);
	}
}
