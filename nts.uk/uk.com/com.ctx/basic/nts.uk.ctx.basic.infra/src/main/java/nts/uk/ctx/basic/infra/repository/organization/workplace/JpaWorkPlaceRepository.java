package nts.uk.ctx.basic.infra.repository.organization.workplace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.department.Department;
import nts.uk.ctx.basic.dom.organization.department.DepartmentMemo;
import nts.uk.ctx.basic.dom.organization.shr.HierarchyCode;
import nts.uk.ctx.basic.dom.organization.workplace.ParentChildAttribute;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlace;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceCode;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceGenericName;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceMemo;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceName;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceRepository;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDep;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDepMemo;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDepMemoPK;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDepPK;
import nts.uk.ctx.basic.infra.entity.organization.workplace.CmnmtWorkPlace;
import nts.uk.ctx.basic.infra.entity.organization.workplace.CmnmtWorkPlaceMemo;
import nts.uk.ctx.basic.infra.entity.organization.workplace.CmnmtWorkPlaceMemoPK;
import nts.uk.ctx.basic.infra.entity.organization.workplace.CmnmtWorkPlacePK;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

@Stateless
public class JpaWorkPlaceRepository extends JpaRepository implements WorkPlaceRepository {

	private static final String FIND_SINGLE;

	private static final String CHECK_EXIST;

	private static final String FIND_HISTORIES;
	
	private static final String UPDATE_ENDDATE;

	private static final String FIND_MEMO;

	private static final String FIND_ALL_BY_HISTORY;

	private static final String IS_DUPLICATE_WORK_PLACE_CODE;

	private static final String QUERY_IS_EXISTED;
	
	private static final String QUERY_IS_EXISTEDHISTORY;
	
	private static final String REMOVE_HISTORY;
	
	private static final String REMOVE_MEMO;
	
	private static final String UPDATE_STARTDATE;
	
	private static final String REMOVE_WORKPLACE;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtWorkPlace e");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		builderString.append(" ORDER BY e.hierarchyId");
		FIND_ALL_BY_HISTORY = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE FROM CmnmtWorkPlaceMemo e ");
		builderString.append(" WHERE e.cmnmtWorkPlaceMemoPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlaceMemoPK.historyId = :historyId");
		REMOVE_MEMO = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE FROM CmnmtWorkPlace e ");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		builderString.append(" AND e.hierarchyId LIKE :hierarchyId");
		REMOVE_WORKPLACE = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("UPDATE CmnmtWorkPlace e");
		builderString.append(" SET e.startDate = :startDate");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		UPDATE_STARTDATE = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("UPDATE CmnmtWorkPlace e");
		builderString.append(" SET e.endDate = :endDate");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		UPDATE_ENDDATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtWorkPlace e");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.workPlaceCode = :workPlaceCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		FIND_SINGLE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtWorkPlace e");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		CHECK_EXIST = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e.cmnmtWorkPlacePK.historyId");
		builderString.append(" ,e.startDate");
		builderString.append(" ,e.endDate");
		builderString.append(" FROM CmnmtWorkPlace e");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" GROUP BY e.cmnmtWorkPlacePK.historyId, e.startDate, e.endDate");
		builderString.append(" ORDER BY e.startDate DESC");
		FIND_HISTORIES = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtWorkPlaceMemo e");
		builderString.append(" WHERE e.cmnmtWorkPlaceMemoPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlaceMemoPK.historyId = :historyId");
		FIND_MEMO = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtWorkPlace e");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.workPlaceCode = :workPlaceCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		IS_DUPLICATE_WORK_PLACE_CODE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtWorkPlace e");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.workPlaceCode = :workPlaceCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		QUERY_IS_EXISTED = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtWorkPlace e");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		QUERY_IS_EXISTEDHISTORY = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE FROM CmnmtWorkPlace e ");
		builderString.append(" WHERE e.cmnmtWorkPlacePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtWorkPlacePK.historyId = :historyId");
		REMOVE_HISTORY = builderString.toString();
	}

	@Override
	public void add(WorkPlace workPlace) {
		this.commandProxy().insert(convertToDbType(workPlace));
	}

	@Override
	public void update(WorkPlace workPlace) {
		this.commandProxy().update(convertToDbType(workPlace));
	}

	@Override
	public void registerMemo(String companyCode, String historyId, Memo memo) {
		WorkPlaceMemo workPlaceMemo = new WorkPlaceMemo(companyCode, historyId, memo);
		this.commandProxy().insert(convertDepMemoToDbType(workPlaceMemo));
	}

	@Override
	public Optional<WorkPlace> findSingle(String companyCode, WorkPlaceCode workPlaceCode, String historyId) {
		return this.queryProxy().query(FIND_SINGLE, CmnmtWorkPlace.class).setParameter("companyCode", companyCode)
				.setParameter("workPlaceCode", workPlaceCode.toString()).setParameter("historyId", historyId)
				.getSingle().map(e -> {
					return Optional.of(convertToDomain(e));
				}).orElse(Optional.empty());
	}

	@Override
	public List<WorkPlace> findAllByHistory(String companyCode, String historyId) {
		List<CmnmtWorkPlace> resultList = this.queryProxy().query(FIND_ALL_BY_HISTORY, CmnmtWorkPlace.class)
				.setParameter("companyCode", companyCode).setParameter("historyId", historyId).getList();
		return !resultList.isEmpty() ? resultList.stream().map(item -> {
			return convertToDomain(item);
		}).collect(Collectors.toList()) : new ArrayList<>();
	}

	@Override
	public List<WorkPlace> findHistories(String companyCode) {
		List<Object[]> resultList = this.queryProxy().query(FIND_HISTORIES).setParameter("companyCode", companyCode)
				.getList();
		return resultList.stream().map(e -> new WorkPlace((String) e[0], (GeneralDate) e[1], (GeneralDate) e[2]))
				.collect(Collectors.toList());
	}

	@Override
	public boolean checkExist(String companyCode) {
		return this.queryProxy().query(CHECK_EXIST, long.class).setParameter("companyCode", companyCode).getSingle()
				.get() > 0;
	}

	@Override
	public Optional<WorkPlaceMemo> findMemo(String companyCode, String historyId) {
		return this.queryProxy().query(FIND_MEMO, CmnmtWorkPlaceMemo.class).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).getSingle().map(e -> {
					return Optional.of(new WorkPlaceMemo(e.getCmnmtWorkPlaceMemoPK().getCompanyCode(),
							e.getCmnmtWorkPlaceMemoPK().getHistoryId(), new Memo(e.getMemo())));
				}).orElse(Optional.empty());
	}

	private CmnmtWorkPlaceMemo convertDepMemoToDbType(WorkPlaceMemo workplaceMemo) {
		CmnmtWorkPlaceMemo cmnmtwkpMemo = new CmnmtWorkPlaceMemo();
		CmnmtWorkPlaceMemoPK wkpMemoPK = new CmnmtWorkPlaceMemoPK(workplaceMemo.getCompanyCode(),
				workplaceMemo.getHistoryId());
		cmnmtwkpMemo.setCmnmtWorkPlaceMemoPK(wkpMemoPK);
		cmnmtwkpMemo.setMemo(workplaceMemo.getMemo().v());
		return cmnmtwkpMemo;

	}

	private WorkPlace convertToDomain(CmnmtWorkPlace cmnmtWorkPlace) {
		return new WorkPlace(cmnmtWorkPlace.getCmnmtWorkPlacePK().getCompanyCode(),
				new WorkPlaceCode(cmnmtWorkPlace.getCmnmtWorkPlacePK().getWorkPlaceCode()),
				cmnmtWorkPlace.getCmnmtWorkPlacePK().getHistoryId(), cmnmtWorkPlace.getEndDate(),
				new WorkPlaceCode(cmnmtWorkPlace.getExternalCode()),
				new WorkPlaceGenericName(cmnmtWorkPlace.getGenericName()),
				new HierarchyCode(cmnmtWorkPlace.getHierarchyId()), new WorkPlaceName(cmnmtWorkPlace.getName()),
				new ParentChildAttribute(new BigDecimal(cmnmtWorkPlace.getParentChildAttribute1())),
				new ParentChildAttribute(new BigDecimal(cmnmtWorkPlace.getParentChildAttribute2())),
				new WorkPlaceCode(cmnmtWorkPlace.getParentWorkCode1()),
				new WorkPlaceCode(cmnmtWorkPlace.getParentWorkCode2()), cmnmtWorkPlace.getStartDate());
	}

	private CmnmtWorkPlace convertToDbType(WorkPlace workPlace) {
		String companyCode = AppContexts.user().companyCode();
		CmnmtWorkPlace cmnmtWorkPlace = new CmnmtWorkPlace();
		CmnmtWorkPlacePK cmnmtWorkPlacePK = new CmnmtWorkPlacePK();
		cmnmtWorkPlacePK.setCompanyCode(companyCode);
		cmnmtWorkPlacePK.setHistoryId(workPlace.getHistoryId());
		cmnmtWorkPlacePK.setWorkPlaceCode(workPlace.getWorkPlaceCode().toString());
		cmnmtWorkPlace.setCmnmtWorkPlacePK(cmnmtWorkPlacePK);
		cmnmtWorkPlace.setEndDate(workPlace.getEndDate());
		cmnmtWorkPlace.setExternalCode(workPlace.getExternalCode().toString());
		cmnmtWorkPlace.setGenericName(workPlace.getGenericName().v());
		cmnmtWorkPlace.setHierarchyId(workPlace.getHierarchyCode().toString());
		cmnmtWorkPlace.setName(workPlace.getName().toString());
		cmnmtWorkPlace.setParentChildAttribute1(workPlace.getParentChildAttribute1().toString());
		cmnmtWorkPlace.setParentChildAttribute2(workPlace.getParentChildAttribute2().toString());
		cmnmtWorkPlace.setParentWorkCode1(workPlace.getParentWorkCode1().toString());
		cmnmtWorkPlace.setParentWorkCode2(workPlace.getParentWorkCode2().toString());
		cmnmtWorkPlace.setStartDate(workPlace.getStartDate());
		return cmnmtWorkPlace;
	}

	@Override
	public boolean isDuplicateWorkPlaceCode(String companyCode, WorkPlaceCode workPlaceCode, String historyId) {
		return this.queryProxy().query(IS_DUPLICATE_WORK_PLACE_CODE, long.class)
				.setParameter("companyCode", companyCode).setParameter("workPlaceCode", workPlaceCode.toString())
				.setParameter("historyId", historyId).getSingle().get() > 0;
	}

	@Override
	public boolean isExistWorkPace(String companyCode, String historyId, WorkPlaceCode workPlaceCode) {
		return this.queryProxy().query(QUERY_IS_EXISTED, long.class).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).setParameter("workPlaceCode", workPlaceCode.toString())
				.getSingle().get() > 0;
	}

	@Override
	public void updateMemo(WorkPlaceMemo workplaceMemo) {
		this.commandProxy().update(convertDepMemoToDbType(workplaceMemo));
	}

	@Override
	public void updateAll(List<WorkPlace> list) {
		convertListDepToDbType(list).forEach(item -> {
			this.commandProxy().update(item);
		});
	}

	private List<CmnmtWorkPlace> convertListDepToDbType(List<WorkPlace> listwkp) {
		List<CmnmtWorkPlace> listCmnmtWkp = new ArrayList<CmnmtWorkPlace>();
		for (int i = 0; i < listwkp.size(); i++) {
			CmnmtWorkPlace cmnmtWkp = new CmnmtWorkPlace();
			String ccd = AppContexts.user().companyCode();
			CmnmtWorkPlacePK cmnmtWkpPK = new CmnmtWorkPlacePK(ccd, listwkp.get(i).getHistoryId(),
					listwkp.get(i).getWorkPlaceCode().toString());
			cmnmtWkp.setCmnmtWorkPlacePK(cmnmtWkpPK);
			cmnmtWkp.setEndDate(listwkp.get(i).getEndDate());
			cmnmtWkp.setExternalCode(
					listwkp.get(i).getExternalCode() != null ? listwkp.get(i).getExternalCode().toString() : "");
			cmnmtWkp.setGenericName(
					listwkp.get(i).getGenericName() != null ? listwkp.get(i).getGenericName().toString() : "");
			cmnmtWkp.setHierarchyId(listwkp.get(i).getHierarchyCode().toString());
			cmnmtWkp.setName(listwkp.get(i).getName() != null ? listwkp.get(i).getName().toString() : "");
			cmnmtWkp.setParentChildAttribute1(listwkp.get(i).getParentChildAttribute1().toString());
			cmnmtWkp.setParentChildAttribute2(listwkp.get(i).getParentChildAttribute2().toString());
			cmnmtWkp.setParentWorkCode1(listwkp.get(i).getParentWorkCode1().toString());
			cmnmtWkp.setParentWorkCode2(listwkp.get(i).getParentWorkCode2().toString());
			cmnmtWkp.setStartDate(listwkp.get(i).getStartDate());
			listCmnmtWkp.add(cmnmtWkp);
		}
		return listCmnmtWkp;
	}

	@Override
	public void addAll(List<WorkPlace> list) {
		convertListDepToDbType(list).forEach(item -> {
			this.commandProxy().insert(item);
		});
	}

	@Override
	public void updateEnddate(String companyCode, String historyId, GeneralDate endDate) {
		this.getEntityManager().createQuery(UPDATE_ENDDATE).setParameter("companyCode", companyCode)
		.setParameter("historyId", historyId).setParameter("endDate", endDate).executeUpdate();
	}

	@Override
	public boolean isExistHistory(String companyCode, String historyId) {
		return this.queryProxy().query(QUERY_IS_EXISTEDHISTORY, long.class).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).getSingle().get() > 0;
	}

	@Override
	public void removeHistory(String companyCode, String historyId) {
		this.getEntityManager().createQuery(REMOVE_HISTORY).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).executeUpdate();
	}

	@Override
	public void removeMemoByHistId(String companyCode, String historyId) {
		this.getEntityManager().createQuery(REMOVE_MEMO).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).executeUpdate();
	}

	@Override
	public void updateStartdate(String companyCode, String historyId, GeneralDate startDate) {
		this.getEntityManager().createQuery(UPDATE_STARTDATE).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).setParameter("startDate", startDate).executeUpdate();
	}

	@Override
	public void remove(String companyCode, String historyId, String hierarchyId) {
		this.getEntityManager().createQuery(REMOVE_WORKPLACE).setParameter("companyCode", companyCode)
		.setParameter("historyId", historyId)
		.setParameter("hierarchyId", hierarchyId + "%").executeUpdate();
	}

}
