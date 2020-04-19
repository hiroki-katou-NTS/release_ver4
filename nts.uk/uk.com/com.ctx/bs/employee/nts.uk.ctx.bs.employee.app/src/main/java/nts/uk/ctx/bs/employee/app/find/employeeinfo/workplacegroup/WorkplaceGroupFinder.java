package nts.uk.ctx.bs.employee.app.find.employeeinfo.workplacegroup;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.workplace.group.AffWorkplaceGroupRespository;
import nts.uk.ctx.bs.employee.dom.workplace.group.WorkplaceGroup;
import nts.uk.ctx.bs.employee.dom.workplace.group.WorkplaceGroupRespository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author anhdt
 * 
 */
@Stateless
public class WorkplaceGroupFinder {
	
	@Inject
	private WorkplaceGroupRespository wkpGroupRepo;
	
	@Inject
	private AffWorkplaceGroupRespository affWpGroupRepo;
	
	/**
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.基幹.社員.職場.職場グループ.App
	 * <<Query>> 職場グループをすべて取得する
	 * 部品起動
	 * @return List<職場グループ>
	 */
	public WorkplaceGroupDto getWorkplaceGroup () {
		String cid = AppContexts.user().companyId();
		List<WorkplaceGroup> wkpGroups = wkpGroupRepo.getAll(cid);
		return new WorkplaceGroupDto(wkpGroups);
	}
	
	/**
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.基幹.社員.職場.職場グループ.App
	 * <<Query>> 職場グループに所属する職場を取得する
	 * @return
	 */
	public List<String> getLstWorkplaceId (String WKPGRPID) {
		String cid = AppContexts.user().companyId();
		List<String> lstId = affWpGroupRepo.getWKPID(cid, WKPGRPID);
		return lstId;
	}
	
	
	/**
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.基幹.社員.職場.職場グループ.App
	 * 職場グループIDから職場グループを取得する
	 * @param WKPGRPID
	 * @return
	 */
	public WorkplaceGroupDto getWkplaceGroup (String WKPGRPID) {
		String cid = AppContexts.user().companyId();
		Optional<WorkplaceGroup> optional = wkpGroupRepo.getById(cid, WKPGRPID);
		if(!optional.isPresent())
			return null;
		return new WorkplaceGroupDto(optional.get());
	}
}
