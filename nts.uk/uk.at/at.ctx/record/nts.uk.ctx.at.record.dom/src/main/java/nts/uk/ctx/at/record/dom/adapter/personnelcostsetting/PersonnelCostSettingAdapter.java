package nts.uk.ctx.at.record.dom.adapter.personnelcostsetting;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface PersonnelCostSettingAdapter {
	
	List<PersonnelCostSettingImport> find(String companyId, GeneralDate baseDate);

}
