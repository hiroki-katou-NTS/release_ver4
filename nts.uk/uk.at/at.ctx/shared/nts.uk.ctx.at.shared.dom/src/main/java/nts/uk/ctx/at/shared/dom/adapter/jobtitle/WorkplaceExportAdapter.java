package nts.uk.ctx.at.shared.dom.adapter.jobtitle;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface WorkplaceExportAdapter {
	
	List<WorkplaceExportImport> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate);
	
}
