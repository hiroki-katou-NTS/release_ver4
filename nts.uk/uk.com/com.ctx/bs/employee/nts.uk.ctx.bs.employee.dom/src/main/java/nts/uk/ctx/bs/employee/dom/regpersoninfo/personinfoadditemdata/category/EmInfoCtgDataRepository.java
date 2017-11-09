package nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.category;

import java.util.List;
import java.util.Optional;

public interface EmInfoCtgDataRepository {
	
	public Optional<EmpInfoCtgData> getEmpInfoCtgDataBySIdAndCtgId(String sId, String ctgId);
	
	public List<EmpInfoCtgData> getByEmpIdAndCtgId(String employeeId, String categoryId);

	public void addCategoryData(EmpInfoCtgData empInfoCtgData);
	
	void addEmpInfoCtgData(EmpInfoCtgData domain);
	
	void updateEmpInfoCtgData(EmpInfoCtgData domain);
	
	void deleteEmpInfoCtgData(EmpInfoCtgData domain);
	
	
}
