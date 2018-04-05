/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace.affiliate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate.BsymtAffiWorkplaceHist;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaAffWorkplaceHistoryRepository.
 */
@Stateless
public class JpaAffWorkplaceHistoryRepository extends JpaRepository implements AffWorkplaceHistoryRepository {
	private final String QUERY_GET_AFFWORKPLACEHIST_BYSID = "SELECT aw FROM BsymtAffiWorkplaceHist aw "
			+ "WHERE aw.sid = :sid and aw.cid = :companyId ORDER BY aw.strDate";

	private final String QUERY_GET_AFFWORKPLACEHIST_BYSID_DESC = QUERY_GET_AFFWORKPLACEHIST_BYSID + " DESC";

	private static final String SELECT_BY_EMPID_STANDDATE = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " WHERE aw.sid = :employeeId AND aw.strDate <= :standDate AND :standDate <= aw.endDate";

	private static final String SELECT_BY_HISTID = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " WHERE aw.hisId = :histId";

	private static final String SELECT_BY_LIST_WKPIDS_BASEDATE = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " INNER JOIN BsymtAffiWorkplaceHistItem awit on aw.hisId = awit.hisId"
			+ " WHERE awit.workPlaceId IN :wkpIds AND aw.strDate <= :standDate AND :standDate <= aw.endDate";

	private static final String SELECT_BY_WKPID_BASEDATE = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " INNER JOIN BsymtAffiWorkplaceHistItem awit on aw.hisId = awit.hisId"
			+ " WHERE awit.workPlaceId = :workplaceId AND aw.strDate <= :standDate AND :standDate <= aw.endDate";

	private static final String SELECT_BY_LIST_EMPID_STANDDATE = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " WHERE aw.sid IN :employeeIds AND aw.strDate <= :standDate AND :standDate <= aw.endDate";

	private static final String SELECT_BY_LIST_EMPID_BY_LIST_WKPIDS_BASEDATE = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " INNER JOIN BsymtAffiWorkplaceHistItem awit on aw.hisId = awit.hisId"
			+ " WHERE aw.sid IN :employeeIds AND awit.workPlaceId IN :wkpIds AND aw.strDate <= :standDate AND :standDate <= aw.endDate";

	private static final String SELECT_BY_HISTID_AND_DATE = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " INNER JOIN BsymtAffiWorkplaceHistItem awit on aw.hisId = awit.hisId"
			+ " WHERE aw.hisId = :histId AND aw.strDate <= :baseDate AND :baseDate <= aw.endDate";

	private static final String SELECT_BY_EMPIDS = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " INNER JOIN BsymtAffiWorkplaceHistItem awit on aw.hisId = awit.hisId"
			+ " WHERE aw.sid IN :employeeIds AND aw.strDate <= :standDate AND :standDate <= aw.endDate";

	private static final String SELECT_BY_WKPID_PERIOD = "SELECT DISTINCT  a.sid FROM BsymtAffiWorkplaceHist a"
			+ " INNER JOIN BsymtAffiWorkplaceHistItem b ON a.hisId = b.hisId"
			+ " WHERE b.workPlaceId = :workPlaceId AND a.strDate <= :endDate AND  a.endDate >= :startDate";

	private static final String SELECT_BY_LISTSID = "SELECT aw FROM BsymtAffiWorkplaceHist aw"
			+ " INNER JOIN BsymtAffiWorkplaceHistItem awit on aw.hisId = awit.hisId"
			+ " WHERE aw.sid IN :listSid ";

	/**
	 * Convert from domain to entity
	 *
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private BsymtAffiWorkplaceHist toEntity(String cid, String employeeID, DateHistoryItem item) {
		return new BsymtAffiWorkplaceHist(item.identifier(), employeeID, cid, item.start(), item.end());
	}

	/**
	 * Update entity from domain
	 *
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private void updateEntity(DateHistoryItem item, BsymtAffiWorkplaceHist entity) {
		entity.setStrDate(item.start());
		entity.setEndDate(item.end());
	}

	/**
	 * Convert from entity to domain
	 *
	 * @param entity
	 * @return
	 */
	private AffWorkplaceHistory toDomainTemp(List<BsymtAffiWorkplaceHist> listHist) {
		AffWorkplaceHistory domain = new AffWorkplaceHistory(listHist.get(0).getCid(),
				listHist.get(0).getSid(), new ArrayList<DateHistoryItem>());
		for (BsymtAffiWorkplaceHist item : listHist) {
			DateHistoryItem dateItem = new DateHistoryItem(item.getHisId(),
					new DatePeriod(item.getStrDate(), item.getEndDate()));
			domain.getHistoryItems().add(dateItem);
		}
		return domain;
	}

	@Override
	public Optional<AffWorkplaceHistory> getByEmployeeId(String companyId, String employeeId) {
		List<BsymtAffiWorkplaceHist> listHist = this.queryProxy()
				.query(QUERY_GET_AFFWORKPLACEHIST_BYSID, BsymtAffiWorkplaceHist.class).setParameter("sid", employeeId)
				.setParameter("companyId", companyId).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toDomainTemp(listHist));
		}
		return Optional.empty();
	}

	@Override
	public Optional<AffWorkplaceHistory> getByEmployeeIdDesc(String companyId, String employeeId) {
		List<BsymtAffiWorkplaceHist> listHist = this.queryProxy()
				.query(QUERY_GET_AFFWORKPLACEHIST_BYSID_DESC, BsymtAffiWorkplaceHist.class)
				.setParameter("sid", employeeId).setParameter("companyId", companyId).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toDomainTemp(listHist));
		}
		return Optional.empty();
	}

	@Override
	public void add(String cid, String sid, DateHistoryItem item) {
		this.commandProxy().insert(toEntity(cid, sid, item));
	}

	@Override
	public void delete(String histId) {

		Optional<BsymtAffiWorkplaceHist> histItem = this.queryProxy().find(histId, BsymtAffiWorkplaceHist.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtAffiWorkplaceHist");
		}
		this.commandProxy().remove(BsymtAffiWorkplaceHist.class, histId);
	}

	@Override
	public void update(DateHistoryItem item) {
		Optional<BsymtAffiWorkplaceHist> histItem = this.queryProxy().find(item.identifier(),
				BsymtAffiWorkplaceHist.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtAffiWorkplaceHist");
		}
		updateEntity(item, histItem.get());
		this.commandProxy().update(histItem.get());
	}

	@Override
	public Optional<AffWorkplaceHistory> getByEmpIdAndStandDate(String employeeId, GeneralDate standDate) {
		List<BsymtAffiWorkplaceHist> listHist = this.queryProxy()
				.query(SELECT_BY_EMPID_STANDDATE, BsymtAffiWorkplaceHist.class).setParameter("employeeId", employeeId)
				.setParameter("standDate", standDate).getList();
		if (!listHist.isEmpty()) {
			return Optional.of(toDomainTemp(listHist));
		}
		return Optional.empty();
	}

	@Override
	public Optional<AffWorkplaceHistory> getByHistId(String histId) {
		List<BsymtAffiWorkplaceHist> listHist = this.queryProxy().query(SELECT_BY_HISTID, BsymtAffiWorkplaceHist.class)
				.setParameter("histId", histId).getList();
		if (!listHist.isEmpty()) {
			return Optional.of(toDomainTemp(listHist));
		}
		return Optional.empty();
	}

	@Override
	public List<AffWorkplaceHistory> findByEmployees(List<String> employeeIds, GeneralDate date) {
		if (CollectionUtil.isEmpty(employeeIds)) {
			return new ArrayList<>();
		}

		List<BsymtAffiWorkplaceHist> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, 1000, subList -> {
			// Query.
			resultList.addAll(this.queryProxy().query(SELECT_BY_EMPIDS, BsymtAffiWorkplaceHist.class)
					.setParameter("employeeIds", subList).setParameter("standDate", date).getList());
		});

		// Group by his id.
		Map<String, List<BsymtAffiWorkplaceHist>> resultMap = resultList.stream()
				.collect(Collectors.groupingBy(BsymtAffiWorkplaceHist::getHisId));

		// Convert to domain.
		return resultMap.keySet().stream().map(key -> {
			return this.toDomainTemp(resultMap.get(key));
		}).collect(Collectors.toList());
	}

	@Override
	public Optional<AffWorkplaceHistory> getByHistIdAndBaseDate(String histId, GeneralDate date) {
		List<BsymtAffiWorkplaceHist> listHist = this.queryProxy()
				.query(SELECT_BY_HISTID_AND_DATE, BsymtAffiWorkplaceHist.class).setParameter("histId", histId)
				.setParameter("baseDate", date).getList();
		if (!listHist.isEmpty()) {
			return Optional.of(toDomainTemp(listHist));
		}
		return Optional.empty();
	}

	@Override
	public List<AffWorkplaceHistory> getWorkplaceHistoryByEmployeeIdAndDate(GeneralDate baseDate,
			String employeeId) {
		List<BsymtAffiWorkplaceHist> listWkpHist = this.queryProxy()
				.query(SELECT_BY_EMPID_STANDDATE, BsymtAffiWorkplaceHist.class).setParameter("employeeId", employeeId)
				.setParameter("standDate", baseDate).getList();
		if (listWkpHist.isEmpty()) {
			return Collections.emptyList();
		}
		return listWkpHist.stream().map(e -> {
			AffWorkplaceHistory domain = this.toDomain(e);
			return domain;
		}).collect(Collectors.toList());
	}

	@Override
	public List<AffWorkplaceHistory> getWorkplaceHistoryByWkpIdsAndDate(GeneralDate baseDate,
			List<String> workplaceIds) {
		List<BsymtAffiWorkplaceHist> resultList = new ArrayList<>();
		CollectionUtil.split(workplaceIds, 1000, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_LIST_WKPIDS_BASEDATE, BsymtAffiWorkplaceHist.class)
					.setParameter("wkpIds", subList).setParameter("standDate", baseDate).getList());
		});
		if (resultList.isEmpty()) {
			return Collections.emptyList();
		}
		return resultList.stream().map(e -> {
			AffWorkplaceHistory domain = this.toDomain(e);
			return domain;
		}).collect(Collectors.toList());
	}

	@Override
	public List<AffWorkplaceHistory> getWorkplaceHistoryByWorkplaceIdAndDate(GeneralDate baseDate,
			String workplaceId) {
		List<BsymtAffiWorkplaceHist> listWkpHist = this.queryProxy()
				.query(SELECT_BY_WKPID_BASEDATE, BsymtAffiWorkplaceHist.class).setParameter("workplaceId", workplaceId)
				.setParameter("standDate", baseDate).getList();
		if (listWkpHist.isEmpty()) {
			return Collections.emptyList();
		}
		return listWkpHist.stream().map(e -> {
			AffWorkplaceHistory domain = this.toDomain(e);
			return domain;
		}).collect(Collectors.toList());
	}

	@Override
	public List<AffWorkplaceHistory> getWorkplaceHistoryByEmpIdsAndDate(GeneralDate baseDate,
			List<String> employeeIds) {
		List<BsymtAffiWorkplaceHist> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, 1000, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_LIST_EMPID_STANDDATE, BsymtAffiWorkplaceHist.class)
					.setParameter("employeeIds", subList).setParameter("standDate", baseDate).getList());
		});
		if (resultList.isEmpty()) {
			return Collections.emptyList();
		}
		return resultList.stream().map(e -> {
			AffWorkplaceHistory domain = this.toDomain(e);
			return domain;
		}).collect(Collectors.toList());
	}

	@Override
	public List<AffWorkplaceHistory> searchWorkplaceHistory(GeneralDate baseDate,
			List<String> employeeIds, List<String> workplaceIds) {

		if (CollectionUtil.isEmpty(employeeIds) || CollectionUtil.isEmpty(workplaceIds)) {
			return Collections.emptyList();
		}

		List<BsymtAffiWorkplaceHist> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, 1000, empSubList -> {
			CollectionUtil.split(workplaceIds, 1000, wplSubList -> {
				resultList.addAll(this.queryProxy()
						.query(SELECT_BY_LIST_EMPID_BY_LIST_WKPIDS_BASEDATE, BsymtAffiWorkplaceHist.class)
						.setParameter("employeeIds", employeeIds).setParameter("wkpIds", workplaceIds)
						.setParameter("standDate", baseDate).getList());
			});
		});

		return resultList.stream().map(e ->  this.toDomain(e)).collect(Collectors.toList());
	}

	// convert to domain
	private AffWorkplaceHistory toDomain(BsymtAffiWorkplaceHist entity) {
		AffWorkplaceHistory domain = new AffWorkplaceHistory(entity.getCid(), entity.getSid(),
				new ArrayList<DateHistoryItem>());
		DateHistoryItem dateItem = new DateHistoryItem(entity.getHisId(),
				new DatePeriod(entity.getStrDate(), entity.getEndDate()));
		domain.getHistoryItems().add(dateItem);

		return domain;
	}

	@Override
	public List<String> getByWplIdAndPeriod(String workplaceId, GeneralDate startDate, GeneralDate endDate) {

		List<String> listWkpHist = this.queryProxy().query(SELECT_BY_WKPID_PERIOD, String.class)
				.setParameter("workPlaceId", workplaceId).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getList();
		if (!listWkpHist.isEmpty()) {
			return listWkpHist;
		} else {
			return Collections.emptyList();
		}
	}
	
	@Override
	 public List<AffWorkplaceHistory> getByListSid(List<String> listSid) {
	  
	  // Split query.
	  List<BsymtAffiWorkplaceHist> resultList = new ArrayList<>();
	  
	  CollectionUtil.split(listSid, 1000, (subList) -> {
	   resultList.addAll(this.queryProxy().query(SELECT_BY_LISTSID, BsymtAffiWorkplaceHist.class)
	     .setParameter("listSid", subList).getList());
	  });

	  return resultList.stream().map(entity -> this.toDomain(entity)).collect(Collectors.toList());
	 }

}
