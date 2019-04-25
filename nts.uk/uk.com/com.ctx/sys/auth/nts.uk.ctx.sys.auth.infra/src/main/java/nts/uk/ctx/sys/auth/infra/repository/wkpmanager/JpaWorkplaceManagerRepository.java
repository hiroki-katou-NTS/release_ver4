package nts.uk.ctx.sys.auth.infra.repository.wkpmanager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
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
			+ " WHERE wm.workplaceId = :workplaceId ORDER BY wm.employeeId, wm.startDate";	
	private static final String SELECT_ALL_BY_SID_BASE_DATE = "SELECT wm FROM SacmtWorkplaceManager wm"
			+ " WHERE wm.employeeId = :employeeId AND wm.startDate <= :baseDate AND wm.endDate >= :baseDate";
	private static final String FIND_BY_WKP_DATE_MANAGER = "SELECT wm FROM SacmtWorkplaceManager wm"
			+ " WHERE wm.workplaceId = :workplaceId" 
			+ " AND wm.startDate <= :baseDate AND wm.endDate >= :baseDate"
			+ " AND wm.kacmtWorkplaceManagerPK.workplaceManagerId IN :wkpManagerLst";
	

	/**
	 * Get workplace manager list by workplace id
	 * 
	 * 【input�
	 * ・会社ID
	 * ・職場ID
	 * ・基準日
	 * 【output�
	 * ・cls <職場表示�
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

	@Override
	public List<WorkplaceManager> findListWkpManagerByEmpIdAndBaseDate(String employeeId, GeneralDate baseDate) {			
		return this.queryProxy().query(SELECT_ALL_BY_SID_BASE_DATE, SacmtWorkplaceManager.class)
				.setParameter("employeeId", employeeId)
				.setParameter("baseDate", baseDate).getList(c -> c.toDomain());
	}


	@Override
	public List<WorkplaceManager> findByWkpDateAndManager(String wkpID, GeneralDate baseDate,
			List<String> wkpManagerIDLst) {
		List<WorkplaceManager> resultList = new ArrayList<>();
		if(wkpManagerIDLst.isEmpty())
			return resultList;
		CollectionUtil.split(wkpManagerIDLst, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(FIND_BY_WKP_DATE_MANAGER, SacmtWorkplaceManager.class)
				.setParameter("workplaceId", wkpID)
				.setParameter("baseDate", baseDate)
				.setParameter("wkpManagerLst", subList)
				.getList(c -> c.toDomain()));
		});
		return resultList;
	}
	
}
