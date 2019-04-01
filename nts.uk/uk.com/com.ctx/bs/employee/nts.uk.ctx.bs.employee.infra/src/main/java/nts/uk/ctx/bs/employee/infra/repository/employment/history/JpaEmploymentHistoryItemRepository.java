package nts.uk.ctx.bs.employee.infra.repository.employment.history;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentInfo;
import nts.uk.ctx.bs.employee.dom.employment.EmpmInfo;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItem;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryOfEmployee;
import nts.uk.ctx.bs.employee.infra.entity.employment.history.BsymtEmploymentHistItem;
import nts.uk.ctx.bs.employee.infra.entity.employment.history.BsymtEmploymentHistItem_;
import nts.uk.ctx.bs.employee.infra.entity.employment.history.BsymtEmploymentHist_;
import nts.uk.ctx.bs.person.dom.person.common.ConstantUtils;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaEmploymentHistoryItemRepository extends JpaRepository implements EmploymentHistoryItemRepository {

//	private static final String SEL_HIS_ITEM = " SELECT a.bsymtEmploymentPK.code ,a.name FROM BsymtEmployment a"
//			+ " INNER JOIN BsymtEmploymentHist h" 
//			+ " ON a.bsymtEmploymentPK.cid = h.companyId"
//			+ " INNER JOIN BsymtEmploymentHistItem i"
//			+ " ON  h.hisId = i.hisId " 
//			+ " AND h.sid  = i.sid"
//			+ " AND a.bsymtEmploymentPK.code =  i.empCode" 
//			+ " WHERE h.sid =:sid" + " AND h.strDate <= :date"
//			+ " AND h.endDate >= :date " 
//			+ " AND a.bsymtEmploymentPK.cid =:companyId";
	
	private static final String SELECT_BY_EMPID_BASEDATE = "SELECT ehi FROM BsymtEmploymentHistItem ehi"
			+ " INNER JOIN  BsymtEmploymentHist eh on eh.hisId = ehi.hisId"
			+ " WHERE eh.sid = :sid AND eh.strDate <= :basedate AND :basedate <= eh.endDate";

	private static final String SELECT_BY_EMPID = "SELECT ehi.sid, eh.strDate, eh.endDate, ehi.empCode FROM BsymtEmploymentHistItem ehi"
			+ " INNER JOIN  BsymtEmploymentHist eh on eh.hisId = ehi.hisId"
			+ " WHERE eh.sid = :sid ORDER BY eh.strDate";
	
//	/** The Constant SELECT_BY_HISTIDS. */
//	private static final String SELECT_BY_HISTIDS = "SELECT aw FROM BsymtEmploymentHistItem aw"
//			+ " WHERE aw.hisId IN :historyId";
	
//	private static final String SELECT_BY_LIST_EMPTCODE_DATEPERIOD = "SELECT ehi FROM BsymtEmploymentHistItem ehi" 
//			+ " INNER JOIN  BsymtEmploymentHist eh on eh.hisId = ehi.hisId" 
//			+ " WHERE ehi.empCode IN :employmentCodes AND eh.strDate <= :endDate AND :startDate <= eh.endDate";
	
	private static final String GET_LST_SID_BY_EMPTCODE_DATEPERIOD = "SELECT ehi.sid FROM BsymtEmploymentHistItem ehi" 
			+ " INNER JOIN  BsymtEmploymentHist eh on eh.hisId = ehi.hisId " 
			+ " WHERE ehi.empCode IN :employmentCodes AND eh.strDate <= :endDate AND :startDate <= eh.endDate"
			+ " AND eh.companyId = :companyId";
	//hoatt
	private static final String GET_BY_LSTSID_DATE = "SELECT h.sid, a.bsymtEmploymentPK.code, a.name"
			+ " FROM BsymtEmployment a"
			+ " INNER JOIN BsymtEmploymentHist h"
			+ " ON a.bsymtEmploymentPK.cid = h.companyId"
			+ " INNER JOIN BsymtEmploymentHistItem i"
			+ " ON h.hisId = i.hisId AND h.sid = i.sid"
			+ " AND a.bsymtEmploymentPK.code = i.empCode"
			+ " WHERE a.bsymtEmploymentPK.cid = :companyId"
			+ " AND h.sid IN :lstSID"
			+ " AND h.strDate <= :date AND h.endDate >= :date";
	
	@Override
	public Optional<EmploymentInfo> getDetailEmploymentHistoryItem(String companyId, String sid, GeneralDate date) {
		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT a.CODE ,a.NAME FROM BSYMT_EMPLOYMENT a");
		builder.append(" INNER JOIN BSYMT_EMPLOYMENT_HIST h");
		builder.append(" ON a.CID = h.CID");
		builder.append(" INNER JOIN BSYMT_EMPLOYMENT_HIS_ITEM i");
		builder.append(" ON h.HIST_ID = i.HIST_ID AND h.SID = i.SID AND a.CODE = i.EMP_CD");
		builder.append(" WHERE a.CID = ? AND h.SID = ? ");
		builder.append(" AND h.START_DATE <= ? AND h.END_DATE >= ?");
		try (val statement = this.connection().prepareStatement(builder.toString())) {
			statement.setString(1, companyId);
			statement.setString(2, sid);
			statement.setDate(3, Date.valueOf(date.localDate()));
			statement.setDate(4, Date.valueOf(date.localDate()));
			return new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				EmploymentInfo emp = new EmploymentInfo();
				if (rec.getString("CODE") != null) {
					emp.setEmploymentCode(rec.getString("CODE"));
				}
				if (rec.getString("NAME") != null) {
					emp.setEmploymentName(rec.getString("NAME"));
				}
				return emp;
			});
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
//		Optional<EmploymentInfo> employee = this.queryProxy().query(SEL_HIS_ITEM, Object[].class)
//				.setParameter("sid", sid).setParameter("date", date).setParameter("companyId", companyId)
//				.getSingle(c -> toDomainEmployee(c));
//		return employee;
	}
	
	@Override
	public Optional<EmploymentHistoryItem> getByHistoryId(String historyId) {
		Optional<BsymtEmploymentHistItem> hiDataOpt = this.queryProxy().find(historyId, BsymtEmploymentHistItem.class);
		if (hiDataOpt.isPresent()) {
			BsymtEmploymentHistItem ent = hiDataOpt.get();
			return Optional.of( EmploymentHistoryItem.createFromJavaType(ent.hisId, ent.sid, ent.empCode, ent.salarySegment));
		}
		return null;
	}

	/**
	 * Convert from domain to entity
	 * 
	 * @param domain
	 * @return
	 */
	private BsymtEmploymentHistItem toEntity(EmploymentHistoryItem domain) {
		return new BsymtEmploymentHistItem(domain.getHistoryId(), domain.getEmployeeId(),
				domain.getEmploymentCode().v(), domain.getSalarySegment() !=null? domain.getSalarySegment().value: null);
	}

//	private EmploymentInfo toDomainEmployee(Object[] entity) {
//		EmploymentInfo emp = new EmploymentInfo();
//		if (entity[0] != null) {
//			emp.setEmploymentCode(entity[0].toString());
//		}
//		if (entity[1] != null) {
//			emp.setEmploymentName(entity[1].toString());
//		}
//		return emp;
//	}

	/**
	 * Update entity from domain
	 * 
	 * @param domain
	 * @param entity
	 */
	private void updateEntity(EmploymentHistoryItem domain, BsymtEmploymentHistItem entity) {
		entity.empCode = domain.getEmploymentCode().v();
		entity.salarySegment = domain.getSalarySegment()!= null? domain.getSalarySegment().value: null;
	}

	@Override
	public void add(EmploymentHistoryItem domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update(EmploymentHistoryItem domain) {
		Optional<BsymtEmploymentHistItem> existItem = this.queryProxy().find(domain.getHistoryId(),
				BsymtEmploymentHistItem.class);
		if (!existItem.isPresent()) {
			throw new RuntimeException("Invalid BsymtEmploymentHistItem");
		}
		updateEntity(domain, existItem.get());
		this.commandProxy().update(existItem.get());
	}

	@Override
	public void delete(String histId) {
		Optional<BsymtEmploymentHistItem> existItem = this.queryProxy().find(histId, BsymtEmploymentHistItem.class);
		if (!existItem.isPresent()) {
			throw new RuntimeException("Invalid BsymtEmploymentHistItem");
		}
		this.commandProxy().remove(BsymtEmploymentHistItem.class, histId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.employment.history.
	 * EmploymentHistoryRepository#searchEmployee(nts.arc.time.GeneralDate,
	 * java.util.List)
	 */
	@Override
	public List<EmploymentHistoryItem> searchEmployee(GeneralDate baseDate,
			List<String> employmentCodes) {
		
		// check not data input
		if (CollectionUtil.isEmpty(employmentCodes)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_EMPLOYMENT_HIST (KmnmtEmploymentHist SQL)
		CriteriaQuery<BsymtEmploymentHistItem> cq = criteriaBuilder
				.createQuery(BsymtEmploymentHistItem.class);

		// root data
		Root<BsymtEmploymentHistItem> root = cq.from(BsymtEmploymentHistItem.class);

		// select root
		cq.select(root);
		
		// Split query.
		List<BsymtEmploymentHistItem> resultList = new ArrayList<>();
		
		CollectionUtil.split(employmentCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// employment in data employment
			lstpredicateWhere
					.add(criteriaBuilder.and(root.get(BsymtEmploymentHistItem_.empCode).in(subList)));

			// start date <= base date
			lstpredicateWhere.add(criteriaBuilder
					.lessThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
							.get(BsymtEmploymentHist_.strDate), baseDate));

			// endDate >= base date
			lstpredicateWhere.add(criteriaBuilder
					.greaterThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
							.get(BsymtEmploymentHist_.endDate), baseDate));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			// create query
			TypedQuery<BsymtEmploymentHistItem> query = em.createQuery(cq);
			resultList.addAll(query.getResultList());
		});
		

		// exclude select
		return resultList.stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.employment.history.
	 * EmploymentHistoryRepository#searchEmployee(java.util.List,
	 * nts.arc.time.GeneralDate, java.util.List)
	 */
	@Override
	public List<EmploymentHistoryItem> searchEmployee(GeneralDate baseDate, List<String> employeeIds, 
			List<String> employmentCodes) {
		if (CollectionUtil.isEmpty(employeeIds) || CollectionUtil.isEmpty(employmentCodes)) {
			return new ArrayList<>();
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_EMPLOYMENT_HIST (KmnmtEmploymentHist SQL)
		CriteriaQuery<BsymtEmploymentHistItem> cq = criteriaBuilder
				.createQuery(BsymtEmploymentHistItem.class);

		// root data
		Root<BsymtEmploymentHistItem> root = cq.from(BsymtEmploymentHistItem.class);

		// select root
		cq.select(root);

		List<BsymtEmploymentHistItem> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, employeeSubList -> {
			CollectionUtil.split(employmentCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, employmentSubList -> {
				// add where
				List<Predicate> lstpredicateWhere = new ArrayList<>();

				// employment in data employment
				lstpredicateWhere
						.add(criteriaBuilder.and(root.get(BsymtEmploymentHistItem_.empCode).in(employmentSubList)));

				// employee id in data employee id
				lstpredicateWhere
						.add(criteriaBuilder.and(root.get(BsymtEmploymentHistItem_.sid).in(employeeSubList)));

				// start date <= base date
				lstpredicateWhere.add(criteriaBuilder
						.lessThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
								.get(BsymtEmploymentHist_.strDate), baseDate));

				// endDate >= base date
				lstpredicateWhere.add(criteriaBuilder
						.greaterThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
								.get(BsymtEmploymentHist_.endDate), baseDate));

				// set where to SQL
				cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

				// create query
				TypedQuery<BsymtEmploymentHistItem> query = em.createQuery(cq);
				resultList.addAll(query.getResultList());
			});
		});
		

		// exclude select
		return resultList.stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.basic.dom.company.organization.employee.employment.AffEmploymentHistoryRepository
	 * #searchEmploymentOfSids(java.util.List, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<EmploymentHistoryItem> searchEmploymentOfSids(List<String> employeeIds,
			GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_EMPLOYMENT_HIST (KmnmtEmploymentHist SQL)
		CriteriaQuery<BsymtEmploymentHistItem> cq = criteriaBuilder
				.createQuery(BsymtEmploymentHistItem.class);

		// root data
		Root<BsymtEmploymentHistItem> root = cq.from(BsymtEmploymentHistItem.class);

		// select root
		cq.select(root);
		if (CollectionUtil.isEmpty(employeeIds)) {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			// start date <= base date
			lstpredicateWhere.add(criteriaBuilder
					.lessThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
							.get(BsymtEmploymentHist_.strDate), baseDate));
	
			// endDate >= base date
			lstpredicateWhere.add(criteriaBuilder
					.greaterThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
							.get(BsymtEmploymentHist_.endDate), baseDate));
	
			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
	
			// create query
			TypedQuery<BsymtEmploymentHistItem> query = em.createQuery(cq);
	
			// exclude select
			return query.getResultList().stream().map(category -> toDomain(category))
					.collect(Collectors.toList());
		}
		
		// Split employee ids.
		List<BsymtEmploymentHistItem> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			

			// employee id in data employee id
			lstpredicateWhere
					.add(criteriaBuilder.and(root.get(BsymtEmploymentHistItem_.sid).in(subList)));

			lstpredicateWhere.add(criteriaBuilder
					.lessThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
							.get(BsymtEmploymentHist_.strDate), baseDate));
	
			// endDate >= base date
			lstpredicateWhere.add(criteriaBuilder
					.greaterThanOrEqualTo(root.get(BsymtEmploymentHistItem_.bsymtEmploymentHist)
							.get(BsymtEmploymentHist_.endDate), baseDate));
			
			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			// create query
			TypedQuery<BsymtEmploymentHistItem> query = em.createQuery(cq);
			resultList.addAll(query.getResultList());
		});
		

		// exclude select
		return resultList.stream().map(category -> toDomain(category)).collect(Collectors.toList());
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the employment history
	 */
	private EmploymentHistoryItem toDomain(BsymtEmploymentHistItem entity) {
		return EmploymentHistoryItem.createFromJavaType(entity.hisId, entity.sid, entity.empCode, entity.salarySegment);
	}

	@Override
	public List<EmploymentHistoryItem> getEmploymentByEmpIdAndDate(GeneralDate basedate, String employeeId) {
		
		List<BsymtEmploymentHistItem> listHistItem = this.queryProxy()
				.query(SELECT_BY_EMPID_BASEDATE, BsymtEmploymentHistItem.class)
				.setParameter("sid", employeeId).setParameter("basedate", basedate)
				.getList();

		// Check exist items
		if (listHistItem.isEmpty()) {
			return Collections.emptyList();
		}

		// Return
		return listHistItem.stream().map(e -> {
			EmploymentHistoryItem domain = this.toDomain(e);
			return domain;
		}).collect(Collectors.toList());
	}

	@Override
	public List<EmploymentHistoryOfEmployee> getEmploymentBySID(String employeeId) {
		List<Object[]> listTemp =  this.queryProxy()
				.query(SELECT_BY_EMPID, Object[].class)
				.setParameter("sid", employeeId).getList();
		
		if (listTemp == null || listTemp.isEmpty()){
			return Collections.emptyList();
		}
		return  listTemp.stream().map(i -> createDomainFromEntity(i)).collect(Collectors.toList());

		
	}
	/**
	 * Convert from entity to domain
	 * @param i
	 * @return
	 */
	private EmploymentHistoryOfEmployee createDomainFromEntity(Object[] i){
		String sID = String.valueOf(i[0]);
		GeneralDate startDate = GeneralDate.fromString(String.valueOf(i[1]), ConstantUtils.FORMAT_DATE_YYYYMMDD);
		GeneralDate endDate = GeneralDate.fromString(String.valueOf(i[2]), ConstantUtils.FORMAT_DATE_YYYYMMDD);
		String empCD = String.valueOf(i[3]);
		EmploymentHistoryOfEmployee emHisOfSid = new EmploymentHistoryOfEmployee(sID, startDate, endDate, empCD);
		
		return emHisOfSid;
	}

	@Override
	@SneakyThrows
	public List<EmploymentHistoryItem> getByListHistoryId(List<String> historyIds) {
		if (CollectionUtil.isEmpty(historyIds)) {
			return new ArrayList<>();
		}
		List<BsymtEmploymentHistItem> listHistItem = new ArrayList<>();
		CollectionUtil.split(historyIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
				try(PreparedStatement statement = this.connection().prepareStatement(
						"select * from BSYMT_EMPLOYMENT_HIS_ITEM a"
					  + " where a.HIST_ID in (" + NtsStatement.In.createParamsString(subList) + ")")){
					for (int i = 0; i < subList.size(); i++) {
						statement.setString(i + 1, subList.get(i));
					}
					
					List<BsymtEmploymentHistItem> results = new NtsResultSet(statement.executeQuery()).getList(rec -> {
						BsymtEmploymentHistItem entity = new BsymtEmploymentHistItem();
						entity.hisId = rec.getString("HIST_ID");
						entity.sid = rec.getString("SID");
						entity.empCode = rec.getString("EMP_CD");
						entity.salarySegment  = rec.getInt("SALARY_SEGMENT");
						return entity;
					});
					
					listHistItem.addAll(results);
					
				} catch (SQLException e) {
					throw new RuntimeException(e);
				};
		});
		return listHistItem.stream().map(item -> toDomain(item))
				.collect(Collectors.toList());
	
	}
//
//	"SELECT aw FROM BsymtEmploymentHistItem aw"
//	+ " WHERE aw.hisId IN :historyId"
//	@Override
//	public List<EmploymentHistoryItem> getListEmptByListCodeAndDatePeriod(DatePeriod dateperiod,
//			List<String> employmentCodes) {
//		List<BsymtEmploymentHistItem> listHistItem = new ArrayList<>();
//		CollectionUtil.split(employmentCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
//			listHistItem.addAll(this.queryProxy().query(SELECT_BY_LIST_EMPTCODE_DATEPERIOD, BsymtEmploymentHistItem.class)
//					.setParameter("employmentCodes", subList)
//					.setParameter("startDate", dateperiod.start())
//					.setParameter("endDate", dateperiod.end())
//					.getList());
//		});
//		if(listHistItem.isEmpty()){
//			return Collections.emptyList();
//		}
//		return listHistItem.stream().map(e -> {
//			EmploymentHistoryItem domain = this.toDomain(e);
//			return domain;
//		}).collect(Collectors.toList());
//	}

	@Override
	public List<String> getLstSidByListCodeAndDatePeriod(DatePeriod dateperiod, List<String> employmentCodes) {
		List<String> listSid = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		CollectionUtil.split(employmentCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			listSid.addAll(this.queryProxy().query(GET_LST_SID_BY_EMPTCODE_DATEPERIOD, String.class)
					.setParameter("employmentCodes", subList)
					.setParameter("startDate", dateperiod.start())
					.setParameter("endDate", dateperiod.end())
					.setParameter("companyId", companyId)
					.getList());
		});
		if(listSid.isEmpty()){
			return Collections.emptyList();
		}
		return listSid;
	}

	@Override
	@SneakyThrows
	public List<EmploymentHistoryItem> getEmploymentHistoryItem(String cid, GeneralDate baseDate) {
		List<BsymtEmploymentHistItem> listHistItem = new ArrayList<>();
				try(PreparedStatement statement = this.connection().prepareStatement(
						"SELECT DISTINCT b.* FROM BSYMT_EMPLOYMENT_HIST a INNER JOIN BSYMT_EMPLOYMENT_HIS_ITEM b ON a.HIST_ID = b.HIST_ID"
					  + " WHERE a.CID = ? AND a.START_DATE  <= ? AND a.END_DATE >= ? ORDER BY b.EMP_CD")){
					statement.setString(1, cid);
					statement.setDate(2, Date.valueOf(baseDate.localDate()));
					statement.setDate(3, Date.valueOf(baseDate.localDate()));
					List<BsymtEmploymentHistItem> results = new NtsResultSet(statement.executeQuery()).getList(rec -> {
						BsymtEmploymentHistItem entity = new BsymtEmploymentHistItem();
						entity.hisId = rec.getString("HIST_ID");
						entity.sid = rec.getString("SID");
						entity.empCode = rec.getString("EMP_CD");
						entity.salarySegment  = rec.getInt("SALARY_SEGMENT");
						return entity;
					});
					
					listHistItem.addAll(results);
					
				} catch (SQLException e) {
					throw new RuntimeException(e);
				};
		
		return listHistItem.stream().map(item -> toDomain(item))
				.collect(Collectors.toList());
	}
	//key: sid, value: EmploymentInfo
	@Override
	public Map<String, EmpmInfo> getLstDetailEmpHistItem(String companyId, List<String> lstSID, GeneralDate date) {
		List<EmpmInfo> lst = new ArrayList<>();
		CollectionUtil.split(lstSID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			lst.addAll(this.queryProxy().query(GET_BY_LSTSID_DATE, Object[].class)
					.setParameter("companyId", companyId)
					.setParameter("lstSID", splitData)
					.setParameter("date", date)
					.getList(c -> new EmpmInfo(c[0].toString(), c[1].toString(), c[2].toString())));
		});
		Map<String, EmpmInfo> mapResult = new HashMap<>();
		for(String sid : lstSID){
			List<EmpmInfo> empInfo = lst.stream().filter(c -> c.getSid().equals(sid)).collect(Collectors.toList());
			if(empInfo.isEmpty()){
				continue;
			}
			mapResult.put(sid, empInfo.get(0));
		}
		return mapResult;
	}

	@Override
	public List<EmploymentHistoryOfEmployee> getEmploymentBySID(List<String> sids,
			List<String> employmentCodes, DatePeriod dateRange) {
		if (CollectionUtil.isEmpty(sids)) {
			return new ArrayList<>();
		}
		String cid = AppContexts.user().companyId();
		List<EmploymentHistoryOfEmployee> listHistItem = new ArrayList<>();
		CollectionUtil.split(sids, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String sql = "SELECT a.SID, a.START_DATE, a.END_DATE, b.EMP_CD FROM BSYMT_EMPLOYMENT_HIST a INNER JOIN BSYMT_EMPLOYMENT_HIS_ITEM b ON a.HIST_ID = b.HIST_ID"
					  + " WHERE a.CID = ? AND a.START_DATE  <= ? AND  ? <= a.END_DATE AND  b.EMP_CD IN ("
					  +  NtsStatement.In.createParamsString(employmentCodes) + ")"
					  + " AND a.SID IN (" + NtsStatement.In.createParamsString(subList) + ")  ORDER BY b.EMP_CD";
				try(PreparedStatement statement = this.connection().prepareStatement(sql)){
					statement.setString(1, cid);
					statement.setDate(2, Date.valueOf(dateRange.end().localDate()));
					statement.setDate(3, Date.valueOf(dateRange.start().localDate()));
					int sizeEmmCode = employmentCodes.size();
					for (int i = 0; i < sizeEmmCode; i++) {
						statement.setString(4 + i, employmentCodes.get(i));
					}
					
					for (int i = 0; i < subList.size(); i++) {
						statement.setString(4 + sizeEmmCode + i , subList.get(i));
					}
					
					List<EmploymentHistoryOfEmployee> results = new NtsResultSet(statement.executeQuery()).getList(rec -> {
						EmploymentHistoryOfEmployee e = new EmploymentHistoryOfEmployee();
						e.setSId(rec.getString("SID"));
						e.setStartDate(rec.getGeneralDate("START_DATE"));
						e.setEndDate(rec.getGeneralDate("END_DATE"));
						e.setEmploymentCD(rec.getString("EMP_CD"));						
						return e;
					});
					
					listHistItem.addAll(results);
					
				} catch (SQLException e) {
					throw new RuntimeException(e);
				};
		});
		return listHistItem;
	}
}
