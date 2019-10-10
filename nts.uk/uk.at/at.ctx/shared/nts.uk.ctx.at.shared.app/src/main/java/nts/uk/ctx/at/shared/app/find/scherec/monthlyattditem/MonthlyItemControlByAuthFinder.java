package nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.scherec.monthlyattendanceitem.MonthlyItemControlByAuthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattendanceitem.MonthlyItemControlByAuthority;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class MonthlyItemControlByAuthFinder {
	
	@Inject
	private MonthlyItemControlByAuthRepository repo;
	
	public MonthlyItemControlByAuthDto getMonthlyItemControlByRoleID(String roleID) {
		String companyID = AppContexts.user().companyId();
		Optional<MonthlyItemControlByAuthority> data = repo.getMonthlyAttdItem(companyID, roleID);
		if(data.isPresent())
			return MonthlyItemControlByAuthDto.fromDomain(data.get());
		return null;
	}
	
	public MonthlyItemControlByAuthDto getMonthlyItemControlByToUse(String companyID ,String roleID,List<Integer> itemMonthlyID, int toUse ) {
		Optional<MonthlyItemControlByAuthority> data = this.repo.getMonthlyAttdItemByUse(companyID, roleID, itemMonthlyID, toUse);
		if(data.isPresent())
			return MonthlyItemControlByAuthDto.fromDomain(data.get());
		return null;
	}
	
	

}
