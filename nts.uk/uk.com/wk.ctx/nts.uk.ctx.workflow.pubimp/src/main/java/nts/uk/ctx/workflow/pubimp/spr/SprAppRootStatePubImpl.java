package nts.uk.ctx.workflow.pubimp.spr;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.service.ApprovalRootStateStatusService;
import nts.uk.ctx.workflow.pub.spr.SprAppRootStatePub;
import nts.uk.ctx.workflow.pub.spr.export.AppRootStateStatusSprExport;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SprAppRootStatePubImpl implements SprAppRootStatePub {
	
	@Inject
	private ApprovalRootStateStatusService approvalRootStateStatusService;

	@Override
	public List<AppRootStateStatusSprExport> getStatusByEmpAndDate(String employeeID, GeneralDate startDate, GeneralDate endDate,
			Integer rootType) {
		return approvalRootStateStatusService.getStatusByEmpAndDate(employeeID, startDate, endDate, rootType)
				.stream().map(x -> new AppRootStateStatusSprExport(
						x.getDate(), 
						x.getEmployeeID(), 
						x.getDailyConfirmAtr().value))
				.collect(Collectors.toList());
	}

}
