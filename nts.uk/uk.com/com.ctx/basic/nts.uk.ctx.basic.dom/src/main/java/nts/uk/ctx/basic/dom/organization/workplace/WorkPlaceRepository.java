package nts.uk.ctx.basic.dom.organization.workplace;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.department.Department;
import nts.uk.ctx.basic.dom.organization.department.DepartmentCode;
import nts.uk.ctx.basic.dom.organization.department.DepartmentMemo;
import nts.uk.shr.com.primitive.Memo;

public interface WorkPlaceRepository {

	void add(WorkPlace workPlace);

	void update(WorkPlace workPlace);

	void addAll(List<WorkPlace> workPlace);

	void updateAll(List<WorkPlace> list);

	void remove(String companyCode, String historyId, String hierachyId);

	void registerMemo(String companyCode, String historyId, Memo memo);

	Optional<WorkPlace> findSingle(String companyCode, WorkPlaceCode workPlaceCode, String historyId);

	List<WorkPlace> findAllByHistory(String companyCode, String historyId);

	List<WorkPlace> findHistories(String companyCode);

	boolean checkExist(String companyCode);

	Optional<WorkPlaceMemo> findMemo(String companyCode, String historyId);

	boolean isExistWorkPace(String companyCode, String historyId, WorkPlaceCode workplaceCode);

	boolean isDuplicateWorkPlaceCode(String companyCode, WorkPlaceCode workPlaceCode, String HistoryID);

	void updateMemo(WorkPlaceMemo workplaceMemo);
	
	void updateEnddate(String ccd, String historyId, GeneralDate endDate);
	
	boolean isExistHistory(String companyCode, String hisoryId);
	
	void removeHistory(String companyCode, String historyId);
	
	void removeMemoByHistId(String companyCode, String historyId);
	
	void updateStartdate(String ccd, String historyId, GeneralDate startDate);
}
