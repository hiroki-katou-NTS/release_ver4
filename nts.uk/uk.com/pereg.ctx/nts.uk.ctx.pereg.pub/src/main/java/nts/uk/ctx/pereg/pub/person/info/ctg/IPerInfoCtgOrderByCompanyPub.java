package nts.uk.ctx.pereg.pub.person.info.ctg;

import java.util.HashMap;
import java.util.List;

public interface IPerInfoCtgOrderByCompanyPub {
	HashMap<Integer, HashMap<String, Integer>> getOrderList(List<String> categoryIds, List<String> itemDefinitionIds);
	
	// Function get List Category Combobox CPS007
	PerInfoCtgDataEnumExport  getAllPerInfoCtgHumanByCompany();
	
	List<PerInfoCtgShowExport> getInfoCtgByCtgIdsAndCid(String cid, List<String> ctgIds);
}
