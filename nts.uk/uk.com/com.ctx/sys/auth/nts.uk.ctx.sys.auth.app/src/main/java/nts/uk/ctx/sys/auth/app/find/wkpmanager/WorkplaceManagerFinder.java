package nts.uk.ctx.sys.auth.app.find.wkpmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.auth.app.find.wkpmanager.dto.WorkplaceManagerDto;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManager;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManagerRepository;
import nts.uk.ctx.sys.auth.dom.wkpmanager.dom.EmpBasicInfoImport;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class WorkplaceManagerFinder {
	
	@Inject
	private EmpInfoFinder empFinder;

	@Inject
	private WorkplaceManagerRepository wkpManagerRepo;
	
	/**
	 * アルゴリズム「職場管理者登録基本データ取得」を実行する
	 * @return
	 */
	public List<WorkplaceManagerDto> findAll(String workplaceId) {
		/*
		 *  ドメインモデル「職場管理者」を取得する
		 */
		List<WorkplaceManagerDto> wkpManagerList = this.getWorkplaceManagerList(workplaceId);
		if (!CollectionUtil.isEmpty(wkpManagerList)) {
			List<String> sidList = wkpManagerList.stream().map(m -> m.getEmployeeId()).collect(Collectors.toList());
			List<EmpBasicInfoImport> empList = this.empFinder.getEmployeeInfoList(sidList);
			wkpManagerList.forEach(
					m->m.setEmployeeInfo(
							empList.stream().filter(x -> x.getEmployeeId().equals(m.getEmployeeId()))
											.findAny()
											.orElse(new EmpBasicInfoImport())));
		}

		return wkpManagerList;
	}
	
	public List<WorkplaceManagerDto> getCanManageWpkForLoginUser() {
		
		return this.wkpManagerRepo.findListWkpManagerByEmpIdAndBaseDate(AppContexts.user().employeeId(), GeneralDate.today())
				.stream().map(WorkplaceManagerDto::fromDomain).collect(Collectors.toList());
	}
	
	private List<WorkplaceManagerDto> getWorkplaceManagerList(String workplaceId) {
		
		return this.wkpManagerRepo.getWkpManagerListByWkpId(workplaceId)
				.stream().map(a -> {
					WorkplaceManagerDto dto = WorkplaceManagerDto.fromDomain(a);
					return dto;
				}).collect(Collectors.toList());
	}
	
	public List<String> findAllWorkplaceId(GeneralDate referenceDate) {
		String employeeId = AppContexts.user().employeeId();
		
		List<String> listWkpId = new ArrayList<>();
		
		// get workplace manager 
		List<WorkplaceManager> listWkpManager = wkpManagerRepo.findListWkpManagerByEmpIdAndBaseDate(employeeId, referenceDate);
		
		// add wkpId to listWkpId
		listWkpId = listWkpManager.stream().map(m -> m.getWorkplaceId()).collect(Collectors.toList());

		return listWkpId;
	}
}
