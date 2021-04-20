package nts.uk.ctx.at.request.pubimp.application.lateorleaveearly;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.pub.application.lateorleaveearly.LateOrLeaveEarlyExport;
import nts.uk.ctx.at.request.pub.application.lateorleaveearly.LateOrLeaveEarlyPub;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class LateOrLeaveEarlyPubImpl implements LateOrLeaveEarlyPub {
	
	@Inject
	private ApplicationRepository applicationRepository;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<LateOrLeaveEarlyExport> engravingCancelLateorLeaveearly(String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
//		String companyID = AppContexts.user().companyId();
//		List<Application> listApp = applicationRepository.getListLateOrLeaveEarly(companyID, employeeID, startDate, endDate);
//		if(CollectionUtil.isEmpty(listApp)){
//			return Collections.emptyList();
//		}
//		List<String> listAppID = listApp.stream().map(x -> x.getAppID()).collect(Collectors.toList());
//		return lateOrLeaveEarlyRepository.findByActualCancelAtr(listAppID, 1).stream()
//				.map(x -> {
//					GeneralDate appDate = listApp.stream().filter(y -> y.getAppID().equals(x.getApplication().getAppID())).findFirst().get().getAppDate().getApplicationDate();
//					return new LateOrLeaveEarlyExport(appDate, x.getEarly1().value, x.getLate1().value, x.getEarly2().value, x.getLate2().value);
//				}).collect(Collectors.toList());
		return Collections.emptyList();
	}
	
}
