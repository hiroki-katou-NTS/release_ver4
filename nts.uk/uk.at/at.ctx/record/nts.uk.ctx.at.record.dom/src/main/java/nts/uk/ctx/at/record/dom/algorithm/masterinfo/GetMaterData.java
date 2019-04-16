package nts.uk.ctx.at.record.dom.algorithm.masterinfo;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;

public interface GetMaterData {

	public Map<Integer, Map<String, CodeNameInfo>> getAllDataMaster(String companyId, GeneralDate dateReference, List<Integer> lstDivNO);
	
}
