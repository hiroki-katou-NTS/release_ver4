package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementDomainService;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AgeementTimeCommonSettingService {

	@Inject
	private AgreementDomainService agreementDomainService;
	
	@Inject
	private WorkingConditionItemRepository workConditionItem;
	
	public AgeementTimeCommonSetting getCommonService(String companyId, List<String> employeeIds, DatePeriod datePeriod){
		Set<GeneralDate> dates = new HashSet<>(datePeriod.datesBetween());
		return new AgeementTimeCommonSetting(agreementDomainService.getBasicSet(companyId, employeeIds, datePeriod),
											workConditionItem.getBySidAndPeriod(employeeIds.stream().collect(Collectors.toMap(c -> c, c -> dates))));
	}
}
