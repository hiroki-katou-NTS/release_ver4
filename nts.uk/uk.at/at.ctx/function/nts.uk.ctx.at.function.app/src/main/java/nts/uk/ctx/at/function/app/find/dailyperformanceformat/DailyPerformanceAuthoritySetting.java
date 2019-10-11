package nts.uk.ctx.at.function.app.find.dailyperformanceformat;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.service.CompanyDailyItemService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DailyPerformanceAuthoritySetting {

	@Inject
	private CompanyDailyItemService companyDailyItemService;

	public List<AttItemName> getListAttendanceItemName(List<Integer> dailyAttendanceItemIds) {
		LoginUserContext login = AppContexts.user();
		String companyId = login.companyId();
		return companyDailyItemService.getDailyItems(companyId, Optional.empty(), dailyAttendanceItemIds, null);
	}
}
