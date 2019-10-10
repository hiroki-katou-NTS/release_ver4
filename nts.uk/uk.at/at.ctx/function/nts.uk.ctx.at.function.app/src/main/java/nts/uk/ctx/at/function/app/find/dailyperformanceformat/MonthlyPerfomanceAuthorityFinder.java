package nts.uk.ctx.at.function.app.find.dailyperformanceformat;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AtItemNameAdapter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.TypeOfItemImport;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattendanceitem.service.CompanyMonthlyItemService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
public class MonthlyPerfomanceAuthorityFinder {

	@Inject
	private CompanyMonthlyItemService companyMonthlyItemService;

	@Inject
	private AtItemNameAdapter atItemNameAdapter;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AttItemName> getListAttendanceItemName(List<Integer> monthlyAttendanceItemIds) {
		LoginUserContext login = AppContexts.user();
		String companyId = login.companyId();
		return companyMonthlyItemService.getMonthlyItems(companyId, Optional.empty(), monthlyAttendanceItemIds, null);
	}
	
	public List<AttItemName> getMonthlyItemsNew(String cid, Optional<String> authorityId) {
		return companyMonthlyItemService.getMonthlyItemsNew(cid, authorityId);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AttItemName> getListAttendanceItemNameByType(int type) {
		return this.atItemNameAdapter.getNameOfAttdItemByType(EnumAdaptor.valueOf(type, TypeOfItemImport.class));
	}
}
