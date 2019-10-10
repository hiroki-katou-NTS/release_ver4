package nts.uk.ctx.at.request.pubimp.application.lateorleaveearly;

/*import java.util.Collection;*/
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarlyRepository;
import nts.uk.ctx.at.request.pub.application.lateorleaveearly.LateOrLeaveEarlyExport;
import nts.uk.ctx.at.request.pub.application.lateorleaveearly.LateOrLeaveEarlyPub;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class LateOrLeaveEarlyPubImpl implements LateOrLeaveEarlyPub {
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private LateOrLeaveEarlyRepository lateOrLeaveEarlyRepository;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<LateOrLeaveEarlyExport> engravingCancelLateorLeaveearly(String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		String companyID = AppContexts.user().companyId();
		List<Application_New> listApp = applicationRepository.getListLateOrLeaveEarly(companyID, employeeID, startDate, endDate);
		if(CollectionUtil.isEmpty(listApp)){
			return Collections.emptyList();
		}
		List<String> listAppID = listApp.stream().map(x -> x.getAppID()).collect(Collectors.toList());
		return lateOrLeaveEarlyRepository.findByActualCancelAtr(listAppID, 1).stream()
				.map(x -> {
					GeneralDate appDate = listApp.stream().filter(y -> y.getAppID().equals(x.getApplication().getAppID())).findFirst().get().getAppDate();
					return new LateOrLeaveEarlyExport(appDate, x.getEarly1().value, x.getLate1().value, x.getEarly2().value, x.getLate2().value);
				}).collect(Collectors.toList());
	}
	
}
