package nts.uk.ctx.bs.employee.pub.jobtitle;

import java.util.List;


public interface SequenceMasterPub {
	
	 List<SequenceMasterExport> findAll(String companyId, String sequenceCode);
}
