package nts.uk.ctx.basic.dom.organization.department;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.primitive.Memo;

public interface DepartmentRepository {

	void add(Department department);

	void update(Department department);

	void updateAll(List<Department> list);

	void updateEnddate(String ccd, String historyId, GeneralDate endDate);
	
	void updateStartdate(String ccd, String historyId, GeneralDate startDate);

	void addeAll(List<Department> list);

	void remove(String companyCode, String historyId, String hierachyId);

	void removeHistory(String companyCode, String historyId);

	void removeMemoByHistId(String companyCode, String historyId);

	boolean isExistHistory(String companyCode, String hisoryId);

	void registerMemo(String companyCode, String historyId, Memo memo);

	void updateMemo(DepartmentMemo departmentMemo);

	Optional<Department> findSingleDepartment(String companyCode, DepartmentCode departmentCode, String historyId);

	List<Department> findAllByHistory(String companyCode, String historyId);

	List<Department> findHistories(String companyCode);

	boolean checkExist(String companyCode);

	boolean isExistDepartment(String companyCode, String historyId,DepartmentCode departmentCode);
	
	Optional<DepartmentMemo> findMemo(String companyCode, String historyId);

	boolean isDuplicateDepartmentCode(String companyCode,String historyId, DepartmentCode departmentCode);

}
