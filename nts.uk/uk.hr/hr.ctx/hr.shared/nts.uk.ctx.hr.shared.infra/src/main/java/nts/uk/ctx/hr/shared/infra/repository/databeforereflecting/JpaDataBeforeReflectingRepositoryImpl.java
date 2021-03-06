
package nts.uk.ctx.hr.shared.infra.repository.databeforereflecting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nts.arc.error.BusinessException;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.text.StringUtil;
import nts.uk.ctx.hr.shared.dom.databeforereflecting.common.DataBeforeReflectingPerInfo;
import nts.uk.ctx.hr.shared.dom.databeforereflecting.common.DataBeforeReflectingRepository;
import nts.uk.ctx.hr.shared.infra.entity.databeforereflecting.PreReflecData;
import nts.uk.ctx.hr.shared.infra.entity.databeforereflecting.PreReflecDataPk;

@Stateless
public class JpaDataBeforeReflectingRepositoryImpl extends JpaRepository implements DataBeforeReflectingRepository {

	public static final String SELECT_BY_SID = "SELECT c FROM PreReflecData c WHERE c.sId IN :sIds";

	public static final String SELECT_BY_WORKID_SIDS = "SELECT c FROM PreReflecData c WHERE c.workId = :workId and c.sId IN :listSid";

	public static final String SELECT_BY_WORKID_CID = "SELECT c FROM PreReflecData c "
			+ " WHERE 1=1 and c.workId = :workId " + " and c.companyId = :cid "
			+ " and c.stattus != 3 ORDER BY date_01 ASC;";

	public static final String DELETE_BY_HIST_ID = "DELETE FROM PreReflecData c WHERE c.preReflecDataPk.historyId = :histId";
	
	public static final String SELECT_BY_HIST_ID = "SELECT c FROM PreReflecData c WHERE c.preReflecDataPk.historyId = :histId";
	
	public static final String SELECT_QUERY = "SELECT c FROM PreReflecData c WHERE 1=1";
	
	@Override
	public void addData(List<DataBeforeReflectingPerInfo> listDomain) {
		if (listDomain.isEmpty()) {
			return;
		}
		
		List<String> sIds = listDomain.stream().map(i -> i.sId).collect(Collectors.toList());
		
		List<PreReflecData> entitys = this.queryProxy()
				.query(SELECT_BY_SID, PreReflecData.class)
				.setParameter("sIds", sIds).getList();
		
		if(!entitys.isEmpty()) {
			throw new BusinessException("Msg_1992");
		}
		
		List<PreReflecData> listEntity = listDomain.stream().map(i -> {
			return PreReflecData.fromDomain(i);
		}).collect(Collectors.toList());

		this.commandProxy().insertAll(listEntity);
		this.getEntityManager().flush();

	}
	
	@Override
	public void addDataNoCheckSid(List<DataBeforeReflectingPerInfo> listDomain) {
		
		if (listDomain.isEmpty()) {
			return;
		}
		
		List<PreReflecData> listEntity = listDomain.stream().map(i -> {
			return PreReflecData.fromDomain(i);
		}).collect(Collectors.toList());

		this.commandProxy().insertAll(listEntity);
		this.getEntityManager().flush();

	}

	@Override
	public void updateData(List<DataBeforeReflectingPerInfo> listDomain) {
		if (listDomain.isEmpty()) {
			return;
		}

		listDomain.forEach(dm -> {
			String histId = dm.getHistoryId();
			Optional<PreReflecData> entityOpt = this.queryProxy().find(new PreReflecDataPk(histId),
					PreReflecData.class);
			if (entityOpt.isPresent()) {
				// Update entity
				PreReflecData entity = entityOpt.get();
				entity.updateEntity(dm);
				this.commandProxy().update(entity);
				this.getEntityManager().flush();
				System.out.println("Update entity Success - " + listDomain.indexOf(dm));

			} else {
				System.out.println("entity does not exist");
			}
		});
	}

	@Override
	public void deleteData(List<DataBeforeReflectingPerInfo> listDomain) {
		if (listDomain.isEmpty()) {
			return;
		}

		listDomain.forEach(dm -> {
			String histId = dm.getHistoryId();
			Optional<PreReflecData> entityOpt = this.queryProxy().find(new PreReflecDataPk(histId), PreReflecData.class);
			if (entityOpt.isPresent()) {
				PreReflecDataPk pk = new PreReflecDataPk(dm.getHistoryId());
				this.commandProxy().remove(PreReflecData.class, pk);
				System.out.println("Delete entity Success - " + listDomain.indexOf(dm));

			} else {
				System.out.println("entity does not exist");
			}
		});
	}

	@Override
	public void deleteDataByHistId(String histId) {
		Optional<PreReflecData> entityOpt = this.queryProxy().find(new PreReflecDataPk(histId), PreReflecData.class);
		if (entityOpt.isPresent()) {
			PreReflecDataPk pk = new PreReflecDataPk(histId);
			this.commandProxy().remove(PreReflecData.class, pk);
			System.out.println("Delete entity Success ");

		} else {
			System.out.println("entity does not exist");
		}
	}

	@Override
	public List<DataBeforeReflectingPerInfo> getData(int workId, List<String> listSid) {
		if (listSid.isEmpty()) {
			return new ArrayList<>();
		}

		List<DataBeforeReflectingPerInfo> listDomain = this.queryProxy()
				.query(SELECT_BY_WORKID_SIDS, PreReflecData.class).setParameter("workId", workId)
				.setParameter("listSid", listSid).getList(dm -> dm.toDomain());
		return listDomain;
	}

	@Override
	public List<DataBeforeReflectingPerInfo> getData(String cid, Integer workId, List<String> listSid,List<String> listPid,
			Optional<Boolean> includReflected, Optional<String> sortByColumnName, Optional<String> orderType) {

		String sql = SELECT_QUERY;

		sql += getCidSql(cid);

		sql += getWorkIdSql(workId);

		sql += getListSidSql(listSid);

		sql += getPidSql(listPid, listSid);

		sql += getIncludReflectedSql(includReflected);

		sql += getSortByColumnNameSql(sortByColumnName);
		
		sql += getOrderTypeSql(sortByColumnName, orderType);
		

		EntityManager em = this.getEntityManager();
		TypedQuery<PreReflecData> query = em.createQuery(sql, PreReflecData.class);

		List<PreReflecData> listEntity = query.getResultList();

		List<DataBeforeReflectingPerInfo> listDomain = listEntity.stream().map(dm -> dm.toDomain())
				.collect(Collectors.toList());

		return listDomain;
	}
	
	

	private String getOrderTypeSql(Optional<String> sortByColumnName, Optional<String> orderType) {
		String result = "";
		if (sortByColumnName.isPresent() && orderType.isPresent()) {
			result = " " + orderType.get();
		}
		return result;
	}

	private String getSortByColumnNameSql(Optional<String> sortByColumnName) {
		String result = "";
		if (sortByColumnName.isPresent()) {
			result = " ORDER BY c." + sortByColumnName.get();
		}
		return result;
	}

	private String getIncludReflectedSql(Optional<Boolean> includReflected) {
		String result = "";

		if ((!includReflected.isPresent()) || (includReflected.isPresent() && includReflected.get() == false)) {
			result = " and c.stattus != 3 ";
		}
		return result;
	}

	private String getPidSql(List<String> listPid, List<String> listSid) {
		String result = "";
		if (!listPid.isEmpty()) {
			String inClause = "";
			if (listSid.size() == 1) {
				inClause = inClause + "('" + listPid.get(0) + "')";
			} else {
				for (int i = 0; i < listPid.size(); i++) {
					if (i == 0) {
						inClause = inClause + "('" + listPid.get(0) + "'";
					} else if (i == listPid.size() - 1) {
						inClause = inClause + ",'" + listPid.get(i) + "')";
					} else {
						inClause = inClause + ",'" + listPid.get(i) + "'";
					}
				}
			}

			result = " and c.pId IN " + inClause;
		}
		return result;
	}

	private String getListSidSql(List<String> listSid) {
		
		String result = "";
		
		if (!listSid.isEmpty()) {
			String inClause = "";
			if (listSid.size() ==  1) {
				inClause = inClause + "('" + listSid.get(0) + "')"  ;
			}else{
				for (int i = 0; i < listSid.size(); i++) {
					if (i == 0) {
						inClause = inClause + "('" + listSid.get(0) + "'"  ;
					} else if (i == listSid.size() - 1) {
						inClause = inClause + ",'" + listSid.get(i) + "')"  ;
					} else {
						inClause = inClause + ",'" + listSid.get(i) + "'"  ;
					}
				}
			}
			
			result = " and c.sId IN " + inClause;
		}
		return result;
	}

	private String getWorkIdSql(Integer workId) {
		String result = "";

		if (workId != null) {
			result = " and c.workId  = '" + workId + "'";
		}
		return result;
	}

	private String getCidSql(String cid) {

		String result = "";
		if (!StringUtil.isNullOrEmpty(cid, true)) {
			result = " and c.companyId  = '" + cid + "'";
		}

		return result;
	}

	@Override
	public Optional<DataBeforeReflectingPerInfo> getByHistId(String histId) {
		return this.queryProxy().query(SELECT_BY_HIST_ID, PreReflecData.class).setParameter("histId", histId)
				.getSingle().map(x -> x.toDomain());
	}

	@Override
	public boolean checkExitByWorkIdCidSid(String companyId, String sid) {
		
		String query = "SELECT c FROM PreReflecData c WHERE c.companyId = :companyId and c.sId = :sid and (c.workId = 1 or c.workId = 2)";
		List<PreReflecData> entitys = this.queryProxy()
				.query(query, PreReflecData.class)
				.setParameter("companyId", companyId)
				.setParameter("sid", sid).getList();
		
		if (entitys.isEmpty()) {
			return false;
		}
		
		return true;
	}

	@Override
	public List<DataBeforeReflectingPerInfo> getDataByApproveSid(String cid, Integer workId, String sid,
			Optional<Boolean> includReflected, Optional<String> sortByColumnName, Optional<String> orderType) {
		String sql = SELECT_QUERY;

		sql += getCidSql(cid);

		sql += getWorkIdSql(workId);

		sql += " AND (c.approveSid1 = '"+ sid +"' AND  c.approveStatus1 != 2) OR (c.approveSid2 = '"+ sid +"' AND  c.approveStatus2 != 2)";

		sql += getIncludReflectedSql(includReflected);

		sql += getSortByColumnNameSql(sortByColumnName);
		
		sql += getOrderTypeSql(sortByColumnName, orderType);
		

		EntityManager em = this.getEntityManager();
		TypedQuery<PreReflecData> query = em.createQuery(sql, PreReflecData.class);

		List<PreReflecData> listEntity = query.getResultList();

		List<DataBeforeReflectingPerInfo> listDomain = listEntity.stream().map(dm -> dm.toDomain())
				.collect(Collectors.toList());

		return listDomain;
	}


	private static final String SELECT_WORK_ID = "SELECT c.workId FROM PreReflecData c WHERE c.companyId = :companyId "
			+ "AND ((c.approveSid1 = :employeeId AND c.approveStatus1 = 0) "
			+ "OR (c.approveSid2 = :employeeId AND c.approveStatus2 = 0)) ";
	
	@Override
	public List<Integer> getWorkId(String cId, String employeeId) {
		return this.queryProxy().query(SELECT_WORK_ID, Integer.class)
				.setParameter("companyId", cId)
				.setParameter("employeeId", employeeId)
				.getList();
	}
}

