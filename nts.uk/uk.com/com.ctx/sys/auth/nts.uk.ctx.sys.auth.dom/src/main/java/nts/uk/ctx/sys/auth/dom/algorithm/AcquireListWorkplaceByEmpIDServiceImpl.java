package nts.uk.ctx.sys.auth.dom.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmpWorkplaceHistoryAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmpWorkplaceHistoryImport;
import nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.sys.auth.dom.role.EmployeeReferenceRange;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AcquireListWorkplaceByEmpIDServiceImpl implements AcquireListWorkplaceByEmpIDService {
	@Inject
	private EmpWorkplaceHistoryAdapter empWorkplaceHistoryAdapter;

	private WorkplaceAdapter workplaceAdapter;
	
	@Override
	public List<String> getListWorkPlaceID(String employeeID, int empRange, GeneralDate referenceDate) {
		List<String> listWorkPlaceID3 = new ArrayList<>();
		if (empRange == EmployeeReferenceRange.ALL_EMPLOYEE.value) {
			// imported（権限管理）「職場」を取得する Lấy Request 157
			List<String> listWorkPlaceID = empWorkplaceHistoryAdapter.getListWorkPlaceIDByDate(referenceDate);
			return listWorkPlaceID;
		} else {
			// ドメインモデル「職場管理者」をすべて取得する
			// (Lấy all domain 「職場管理者」)
			//[RQ613]指定社員の職場管理者の職場リストを取得する（配下含む）
			List<String> subListWorkPlace = workplaceAdapter.getWorkplaceId(GeneralDate.today(), employeeID);
			// imported（権限管理）「所属職場履歴」を取得する Lay RequestList 30
			Optional<EmpWorkplaceHistoryImport> workPlaceImport = empWorkplaceHistoryAdapter.findBySid(employeeID, referenceDate);
			String workPlaceID1 = workPlaceImport.get().getWorkplaceID();
			if (empRange == EmployeeReferenceRange.DEPARTMENT_AND_CHILD.value) {
				// 配下の職場をすべて取得する Lay Request list 154
				listWorkPlaceID3 = workplaceAdapter.findListWorkplaceIdByCidAndWkpIdAndBaseDate(AppContexts.user().companyId(), workPlaceID1, referenceDate);
			}
			List<String> listResultWorkPlaceID = new ArrayList<>();
			listResultWorkPlaceID.add(workPlaceID1);
			listResultWorkPlaceID.addAll(subListWorkPlace);
			listResultWorkPlaceID.addAll(listWorkPlaceID3);
			// filter duplicate WorkPlaceID
			return listResultWorkPlaceID.stream().distinct().collect(Collectors.toList());
		}
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.dom.algorithm.AcquireListWorkplaceByEmpIDService#getListWorkPlaceIDNoWkpAdmin(java.lang.String, int, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> getListWorkPlaceIDNoWkpAdmin(String employeeID, int empRange, GeneralDate referenceDate) {
		List<String> listWorkPlaceID2 = new ArrayList<>();
		
		// 社員参照範囲＝全社員 (Phạm vi tham chiếu employee = all employee)
		if (empRange == EmployeeReferenceRange.ALL_EMPLOYEE.value) {
			// imported（権限管理）「職場」を取得する Lấy Request 157
			List<String> listWorkPlaceID = empWorkplaceHistoryAdapter.getListWorkPlaceIDByDate(referenceDate);
			return listWorkPlaceID;
		} else { // それ以外(Còn lại)
			// imported（権限管理）「所属職場履歴」を取得する Lay RequestList 30
			Optional<EmpWorkplaceHistoryImport> workPlaceImport = empWorkplaceHistoryAdapter.findBySid(employeeID, referenceDate);
			String workPlaceID1 = workPlaceImport.get().getWorkplaceID();
			
			// 社員参照範囲＝部門（配下含む）
			if (empRange == EmployeeReferenceRange.DEPARTMENT_AND_CHILD.value) {
				// 配下の職場をすべて取得する Lay Request list 154
				listWorkPlaceID2 = workplaceAdapter.findListWorkplaceIdByCidAndWkpIdAndBaseDate(AppContexts.user().companyId(), workPlaceID1, referenceDate);
			}
			List<String> listResultWorkPlaceID = new ArrayList<>();
			listResultWorkPlaceID.add(workPlaceID1);
			listResultWorkPlaceID.addAll(listWorkPlaceID2);
			// filter duplicate WorkPlaceID
			return listResultWorkPlaceID.stream().distinct().collect(Collectors.toList());
		}
	}

}
