package nts.uk.ctx.sys.auth.infra.repository.wkpmanager;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManager;
import nts.uk.ctx.sys.auth.dom.wkpmanager.WorkplaceManagerRepository;
import nts.uk.ctx.sys.auth.infra.entity.wkpmanager.SacmtWorkplaceManager;
import nts.uk.ctx.sys.auth.infra.entity.wkpmanager.SacmtWorkplaceManagerPK;
@Stateless
public class JpaWorkplaceManagerRepository extends JpaRepository implements WorkplaceManagerRepository {
	/**
	 * Query strings
	 */
	private static final String SELECT_ALL = "SELECT wm FROM SacmtWorkplaceManager wm";
	private static final String SELECT_All_BY_SID_WKP_ID = SELECT_ALL
			+ " WHERE wm.employeeId = :employeeId AND wm.workplaceId = :workplaceId";
	private static final String SELECT_All_BY_WKP_ID = SELECT_ALL
			+ " WHERE wm.workplaceId = :workplaceId";

	/**
	 * Get workplace manager list by workplace id
	 * 
	 * 【input】
	 * ・会社ID
	 * ・職場ID
	 * ・基準日
	 * 【output】
	 * ・cls <職場表示名>
	 */
	@Override
	public List<WorkplaceManager> getWkpManagerListByWkpId(String workplaceId) {
		return this.queryProxy().query(SELECT_All_BY_WKP_ID, SacmtWorkplaceManager.class)
				.setParameter("workplaceId", workplaceId).getList(c -> c.toDomain());
	}


	/**
	 * Get workplace manager list by workplace id and employeeid
	 */
	@Override
	public List<WorkplaceManager> getWkpManagerBySIdWkpId(String employeeId, String workplaceId) {
		return this.queryProxy().query(SELECT_All_BY_SID_WKP_ID, SacmtWorkplaceManager.class)
				.setParameter("employeeId", employeeId)
				.setParameter("workplaceId", workplaceId).getList(c -> c.toDomain());
	}
	
	@Override
	public void add(WorkplaceManager wkpManager) {
		this.commandProxy().insert(SacmtWorkplaceManager.toEntity(wkpManager));
		
	}

	@Override
	public void update(WorkplaceManager wkpManager) {
		SacmtWorkplaceManager updateData = SacmtWorkplaceManager.toEntity(wkpManager);
		SacmtWorkplaceManager oldData = this.queryProxy().find(updateData.kacmtWorkplaceManagerPK, SacmtWorkplaceManager.class).get();
		oldData.employeeId = updateData.employeeId;
		oldData.workplaceId = updateData.workplaceId;
		oldData.startDate = updateData.startDate;
		oldData.endDate = updateData.endDate;
		
		this.commandProxy().update(oldData);
	}

	@Override
	public void delete(String wkpManagerId) {
		SacmtWorkplaceManagerPK kacmtWorkplaceManagerPK = new SacmtWorkplaceManagerPK(wkpManagerId);
		this.commandProxy().remove(SacmtWorkplaceManager.class,kacmtWorkplaceManagerPK);
	}
}
