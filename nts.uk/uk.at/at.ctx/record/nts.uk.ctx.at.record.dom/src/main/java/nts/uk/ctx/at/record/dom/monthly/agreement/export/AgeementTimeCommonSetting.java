package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.standardtime.BasicAgreementSetting;
import nts.uk.ctx.at.record.dom.standardtime.BasicAgreementSettingsGetter;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;

@AllArgsConstructor
@NoArgsConstructor
public class AgeementTimeCommonSetting {

	private BasicAgreementSettingsGetter basicSetGetter;
	
	private Map<String, Map<GeneralDate, WorkingConditionItem>> workCondition;
	
	public BasicAgreementSetting getBasicSet(String companyId, String employeeId, GeneralDate criteria, WorkingSystem workingSystem){
		return basicSetGetter.getBasicSet(companyId, employeeId, criteria, workingSystem);
	}
	
	public Optional<WorkingConditionItem> getWorkCondition(String empId, GeneralDate criteria) {
		if(workCondition.containsKey(empId)){
			if(workCondition.get(empId).containsKey(criteria)){
				return Optional.of(workCondition.get(empId).get(criteria));
			}
		}
		return Optional.empty();
	}
}
