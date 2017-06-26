package nts.uk.ctx.basic.infra.repository.organization.department;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.department.Department;
import nts.uk.ctx.basic.dom.organization.department.DepartmentCode;
import nts.uk.ctx.basic.dom.organization.department.DepartmentGenericName;
import nts.uk.ctx.basic.dom.organization.department.DepartmentMemo;
import nts.uk.ctx.basic.dom.organization.department.DepartmentName;
import nts.uk.ctx.basic.dom.organization.department.DepartmentRepository;
import nts.uk.ctx.basic.dom.organization.shr.HierarchyCode;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDep;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDepMemo;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDepMemoPK;
import nts.uk.ctx.basic.infra.entity.organization.department.CmnmtDepPK;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

@Stateless
public class JpaDepartmentRepository extends JpaRepository implements DepartmentRepository {

	private static final String FIND_ALL_BY_HISTORY;

	private static final String FIND_SINGLE;

	private static final String CHECK_EXIST;

	private static final String FIND_HISTORIES;

	private static final String FIND_MEMO;

	private static final String IS_DUPLICATE_DEPARTMENT_CODE;

	private static final String QUERY_IS_EXISTED;

	private static final String QUERY_IS_EXISTEDHISTORY;

	private static final String UPDATE_ENDDATE;

	private static final String UPDATE_STARTDATE;

	private static final String REMOVE_HISTORY;

	private static final String REMOVE_MEMO;
	
	private static final String REMOVE_DEPARTMENT;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtDep e");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		builderString.append(" ORDER BY e.hierarchyId");
		FIND_ALL_BY_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("UPDATE CmnmtDep e");
		builderString.append(" SET e.endDate = :endDate");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		UPDATE_ENDDATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("UPDATE CmnmtDep e");
		builderString.append(" SET e.startDate = :startDate");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		UPDATE_STARTDATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE FROM CmnmtDep e ");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		REMOVE_HISTORY = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE FROM CmnmtDep e ");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		builderString.append(" AND e.hierarchyId LIKE :hierarchyId");
		REMOVE_DEPARTMENT = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE FROM CmnmtDepMemo e ");
		builderString.append(" WHERE e.cmnmtDepMemoPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepMemoPK.historyId = :historyId");
		REMOVE_MEMO = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtDep e");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.departmentCode = :departmentCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		FIND_SINGLE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtDep e");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		CHECK_EXIST = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e.cmnmtDepPK.historyId");
		builderString.append(" ,e.startDate");
		builderString.append(" ,e.endDate");
		builderString.append(" FROM CmnmtDep e");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" GROUP BY e.cmnmtDepPK.historyId, e.startDate, e.endDate");
		builderString.append(" ORDER BY e.startDate DESC");
		FIND_HISTORIES = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtDepMemo e");
		builderString.append(" WHERE e.cmnmtDepMemoPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepMemoPK.historyId = :historyId");
		FIND_MEMO = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtDep e");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.departmentCode = :departmentCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		IS_DUPLICATE_DEPARTMENT_CODE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtDep e");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.departmentCode = :departmentCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		QUERY_IS_EXISTED = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(e)");
		builderString.append(" FROM CmnmtDep e");
		builderString.append(" WHERE e.cmnmtDepPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtDepPK.historyId = :historyId");
		QUERY_IS_EXISTEDHISTORY = builderString.toString();
	}

	private Department convertToDomain(CmnmtDep cmnmtDep) {
		return new Department(cmnmtDep.getCmnmtDepPK().getCompanyCode(),
				new DepartmentCode(cmnmtDep.getCmnmtDepPK().getDepartmentCode()),
				cmnmtDep.getCmnmtDepPK().getHistoryId(), cmnmtDep.getEndDate(),
				new DepartmentCode(cmnmtDep.getExternalCode()), new DepartmentGenericName(cmnmtDep.getDepNameTotal()),
				new HierarchyCode(cmnmtDep.getHierarchyId()), new DepartmentName(cmnmtDep.getDepName()),
				cmnmtDep.getStartDate());

	}

	private CmnmtDep convertToDbType(Department department) {
		CmnmtDep cmnmtDep = new CmnmtDep();
		String ccd = AppContexts.user().companyCode();
		CmnmtDepPK cmnmtDepPK = new CmnmtDepPK(ccd, department.getHistoryId(),
				department.getDepartmentCode().toString());
		cmnmtDep.setCmnmtDepPK(cmnmtDepPK);
		cmnmtDep.setStartDate(department.getStartDate());
		cmnmtDep.setEndDate(department.getEndDate());
		cmnmtDep.setDepName(department.getDepartmentName().toString());
		cmnmtDep.setDepNameTotal(department.getFullName().toString());
		cmnmtDep.setHierarchyId(department.getHierarchyCode().toString());
		cmnmtDep.setExternalCode(department.getExternalCode() != null ? department.getExternalCode().toString() : "");
		return cmnmtDep;
	}

	private List<CmnmtDep> convertListDepToDbType(List<Department> listdep) {
		List<CmnmtDep> listCmnmtDep = new ArrayList<CmnmtDep>();
		for (int i = 0; i < listdep.size(); i++) {
			CmnmtDep cmnmtDep = new CmnmtDep();
			String ccd = AppContexts.user().companyCode();
			CmnmtDepPK cmnmtDepPK = new CmnmtDepPK(ccd, listdep.get(i).getHistoryId(),
					listdep.get(i).getDepartmentCode().toString());
			cmnmtDep.setCmnmtDepPK(cmnmtDepPK);
			cmnmtDep.setStartDate(listdep.get(i).getStartDate());
			cmnmtDep.setEndDate(listdep.get(i).getEndDate());
			cmnmtDep.setDepName(listdep.get(i).getDepartmentName().toString());
			cmnmtDep.setDepNameTotal(listdep.get(i).getFullName().toString());
			cmnmtDep.setHierarchyId(listdep.get(i).getHierarchyCode().toString());
			cmnmtDep.setExternalCode(
					listdep.get(i).getExternalCode() != null ? listdep.get(i).getExternalCode().toString() : "");
			listCmnmtDep.add(cmnmtDep);
		}
		return listCmnmtDep;
	}

	private CmnmtDepMemo convertDepMemoToDbType(DepartmentMemo departmentMemo) {
		CmnmtDepMemo cmnmtDepMemo = new CmnmtDepMemo();
		CmnmtDepMemoPK depMemoPK = new CmnmtDepMemoPK(departmentMemo.getCompanyCode(), departmentMemo.getHistoryId());
		cmnmtDepMemo.setCmnmtDepMemoPK(depMemoPK);
		cmnmtDepMemo.setMemo(departmentMemo.getMemo().v());
		return cmnmtDepMemo;

	}

	@Override
	public void add(Department department) {
		this.commandProxy().insert(convertToDbType(department));
	}

	@Override
	public void update(Department department) {
		this.commandProxy().update(convertToDbType(department));
	}

	@Override
	public void updateAll(List<Department> list) {
		convertListDepToDbType(list).forEach(item -> {
			this.commandProxy().update(item);
		});
	}

	@Override
	public void registerMemo(String companyCode, String historyId, Memo memo) {
		DepartmentMemo departmentMemo = new DepartmentMemo(companyCode, historyId, memo);
		this.commandProxy().insert(convertDepMemoToDbType(departmentMemo));
	}

	@Override
	public boolean checkExist(String companyCode) {
		return this.queryProxy().query(CHECK_EXIST, long.class).setParameter("companyCode", companyCode)
				.getSingle().get() > 0;
	}

	@Override
	public Optional<Department> findSingleDepartment(String companyCode, DepartmentCode departmentCode,
			String historyId) {
		return this.queryProxy().query(FIND_SINGLE, CmnmtDep.class).setParameter("companyCode", "'" + companyCode + "'")
				.setParameter("departmentCode", "'" + departmentCode.toString() + "'")
				.setParameter("historyId", historyId).getSingle().map(e -> {
					return Optional.of(convertToDomain(e));
				}).orElse(Optional.empty());
	}

	@Override
	public List<Department> findAllByHistory(String companyCode, String historyId) {
		List<CmnmtDep> resultList = this.queryProxy().query(FIND_ALL_BY_HISTORY, CmnmtDep.class)
				.setParameter("companyCode", companyCode).setParameter("historyId", historyId).getList();
		System.out.println(resultList);
		return !resultList.isEmpty() ? resultList.stream().map(item -> {
			return convertToDomain(item);
		}).collect(Collectors.toList()) : new ArrayList<>();
	}

	@Override
	public List<Department> findHistories(String companyCode) {
		List<Object[]> resultList = this.queryProxy().query(FIND_HISTORIES).setParameter("companyCode", companyCode)
				.getList();
		return resultList.stream().map(e -> new Department((String) e[0], (GeneralDate) e[1], (GeneralDate) e[2]))
				.collect(Collectors.toList());
	}
	
	@Override
	public Optional<DepartmentMemo> findMemo(String companyCode, String historyId) {
		return this.queryProxy().query(FIND_MEMO, CmnmtDepMemo.class).setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).getSingle().map(e -> {
					return Optional.of(new DepartmentMemo(e.getCmnmtDepMemoPK().getCompanyCode(),
							e.getCmnmtDepMemoPK().getHistoryId(), new Memo(e.getMemo())));
				}).orElse(Optional.empty());
	}

	@Override
	public boolean isDuplicateDepartmentCode(String companyCode, String historyId, DepartmentCode departmentCode) {
		return this.queryProxy().query(IS_DUPLICATE_DEPARTMENT_CODE, long.class)
				.setParameter("companyCode", companyCode).setParameter("historyId", historyId)
				.setParameter("departmentCode", departmentCode.toString()).getSingle().get() > 0;
	}

	@Override
	public boolean isExistDepartment(String companyCode, String historyId, DepartmentCode departmentCode) {
		return this.queryProxy().query(QUERY_IS_EXISTED, long.class)
				.setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId)
				.setParameter("departmentCode", departmentCode.toString())
				.getSingle().get() > 0;
	}

	@Override
	public void updateMemo(DepartmentMemo departmentMemo) {
		this.commandProxy().update(convertDepMemoToDbType(departmentMemo));

	}

	@Override
	public void addeAll(List<Department> list) {
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
		this.getEntityManager().createQuery(REMOVE_DEPARTMENT).setParameter("companyCode", companyCode)
		.setParameter("historyId", historyId)
		.setParameter("hierarchyId", hierarchyId + "%").executeUpdate();
		
	}

}
