package nts.uk.ctx.bs.employee.infra.repository.employment.history;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistory;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryRepository;
import nts.uk.ctx.bs.employee.infra.entity.employment.history.BsymtEmploymentHist;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaEmploymentHistoryRepository extends JpaRepository implements EmploymentHistoryRepository {

	private final String QUERY_BYEMPLOYEEID = "SELECT ep FROM BsymtEmploymentHist ep"
			+ " WHERE ep.sid = :sid and ep.companyId = :cid ORDER BY ep.strDate";

	private final String QUERY_BYEMPLOYEEID_DESC = QUERY_BYEMPLOYEEID + " DESC";

	private final String GET_BY_EMPID_AND_STD = "SELECT h FROM BsymtEmploymentHist h"
			+ " WHERE h.sid = :sid AND h.strDate <= :stdDate AND h.endDate >= :stdDate";

	private static final String SELECT_BY_LISTSID = "SELECT a FROM BsymtEmploymentHist a"
			+ " INNER JOIN BsymtEmploymentHistItem b on a.hisId = b.hisId" + " WHERE a.sid IN :listSid "
			+ " AND ( a.strDate >= start AND a.strDate <= end AND a.endDate >= start AND a.endDate <= end ) "
			+ " OR  ( a.strDate >= start AND a.strDate <= end AND a.endDate > start )"
			+ " OR  ( a.endDate >= start AND a.endDate <= end AND a.strDate < start )";

	/**
	 * Convert from BsymtEmploymentHist to domain EmploymentHistory
	 * 
	 * @param sid
	 * @param listHist
	 * @return
	 */
	private EmploymentHistory toEmploymentHistory(List<BsymtEmploymentHist> listHist) {
		EmploymentHistory empment = new EmploymentHistory(listHist.get(0).companyId, listHist.get(0).sid,
				new ArrayList<>());
		DateHistoryItem dateItem = null;
		for (BsymtEmploymentHist item : listHist) {
			dateItem = new DateHistoryItem(item.hisId, new DatePeriod(item.strDate, item.endDate));
			empment.getHistoryItems().add(dateItem);
		}
		return empment;
	}

	@Override
	public Optional<EmploymentHistory> getByEmployeeId(String cid, String sid) {
		List<BsymtEmploymentHist> listHist = this.queryProxy().query(QUERY_BYEMPLOYEEID, BsymtEmploymentHist.class)
				.setParameter("sid", sid).setParameter("cid", cid).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toEmploymentHistory(listHist));
		}
		return Optional.empty();
	}

	@Override
	public Optional<EmploymentHistory> getByEmployeeIdDesc(String cid, String sid) {
		List<BsymtEmploymentHist> listHist = this.queryProxy().query(QUERY_BYEMPLOYEEID_DESC, BsymtEmploymentHist.class)
				.setParameter("sid", sid).setParameter("cid", cid).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toEmploymentHistory(listHist));
		}
		return Optional.empty();
	}

	@Override
	public Optional<DateHistoryItem> getByEmployeeIdAndStandardDate(String employeeId, GeneralDate standardDate) {
		Optional<BsymtEmploymentHist> optionData = this.queryProxy()
				.query(GET_BY_EMPID_AND_STD, BsymtEmploymentHist.class).setParameter("sid", employeeId)
				.setParameter("stdDate", standardDate).getSingle();
		if (optionData.isPresent()) {
			BsymtEmploymentHist entity = optionData.get();
			return Optional.of(new DateHistoryItem(entity.hisId, new DatePeriod(entity.strDate, entity.endDate)));
		}
		return Optional.empty();
	}

	@Override
	public Optional<DateHistoryItem> getByHistoryId(String historyId) {
		Optional<BsymtEmploymentHist> optionData = this.queryProxy().find(historyId, BsymtEmploymentHist.class);
		if (optionData.isPresent()) {
			BsymtEmploymentHist entity = optionData.get();
			return Optional.of(new DateHistoryItem(historyId, new DatePeriod(entity.strDate, entity.endDate)));
		}
		return Optional.empty();
	}

	@Override
	public void add(String sid, DateHistoryItem domain) {
		this.commandProxy().insert(toEntity(sid, domain));
	}

	@Override
	public void update(DateHistoryItem itemToBeUpdated) {
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(itemToBeUpdated.identifier(),
				BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtEmploymentHist");
		}
		updateEntity(itemToBeUpdated, histItem.get());
		this.commandProxy().update(histItem.get());

	}

	@Override
	public void delete(String histId) {
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(histId, BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtEmploymentHist");
		}
		this.commandProxy().remove(BsymtEmploymentHist.class, histId);

	}

	/**
	 * Convert from domain to entity
	 * 
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private BsymtEmploymentHist toEntity(String employeeID, DateHistoryItem item) {
		String companyId = AppContexts.user().companyId();
		return new BsymtEmploymentHist(item.identifier(), companyId, employeeID, item.start(), item.end());
	}

	/**
	 * Update entity from domain
	 * 
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private void updateEntity(DateHistoryItem item, BsymtEmploymentHist entity) {
		entity.strDate = item.start();
		entity.endDate = item.end();
	}

	/**
	 * Update item before when updating or deleting
	 * 
	 * @param domain
	 * @param item
	 */
	@SuppressWarnings("unused")
	private void updateItemBefore(EmploymentHistory domain, DateHistoryItem item) {
		// Update item before
		Optional<DateHistoryItem> beforeItem = domain.immediatelyBefore(item);
		if (!beforeItem.isPresent()) {
			return;
		}
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(beforeItem.get().identifier(),
				BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			return;
		}
		updateEntity(beforeItem.get(), histItem.get());
		this.commandProxy().update(histItem.get());
	}

	/**
	 * Update item after when updating or deleting
	 * 
	 * @param domain
	 * @param item
	 */
	@SuppressWarnings("unused")
	private void updateItemAfter(EmploymentHistory domain, DateHistoryItem item) {
		// Update item after
		Optional<DateHistoryItem> aferItem = domain.immediatelyAfter(item);
		if (!aferItem.isPresent()) {
			return;
		}
		Optional<BsymtEmploymentHist> histItem = this.queryProxy().find(aferItem.get().identifier(),
				BsymtEmploymentHist.class);
		if (!histItem.isPresent()) {
			return;
		}
		updateEntity(aferItem.get(), histItem.get());
		this.commandProxy().update(histItem.get());
	}

	// convert to domain
	private EmploymentHistory toDomain(BsymtEmploymentHist entity) {
		EmploymentHistory domain = new EmploymentHistory(entity.companyId, entity.sid,
				new ArrayList<DateHistoryItem>());
		DateHistoryItem dateItem = new DateHistoryItem(entity.hisId, new DatePeriod(entity.strDate, entity.endDate));
		domain.getHistoryItems().add(dateItem);

		return domain;
	}

	@Override
	public List<EmploymentHistory> getByListSid(List<String> employeeIds, DatePeriod datePeriod) {

		// Split query.
		List<BsymtEmploymentHist> lstEmpHist = new ArrayList<>();

		CollectionUtil.split(employeeIds, 1000, (subList) -> {
			lstEmpHist.addAll(this.queryProxy().query(SELECT_BY_LISTSID, BsymtEmploymentHist.class)
					.setParameter("listSid", subList).setParameter("start", datePeriod.start())
					.setParameter("end", datePeriod.end()).getList());
		});

		Map<String, List<BsymtEmploymentHist>> map = lstEmpHist.stream().collect(Collectors.groupingBy(x -> x.sid));

		List<EmploymentHistory> result = new ArrayList<>();

		for (Map.Entry<String, List<BsymtEmploymentHist>> info : map.entrySet()) {
			EmploymentHistory empHistory = new EmploymentHistory();
			empHistory.setEmployeeId(info.getKey());
			List<BsymtEmploymentHist> values = info.getValue();
			if (!values.isEmpty()) {
				empHistory.setHistoryItems(
						values.stream().map(x -> new DateHistoryItem(x.hisId, new DatePeriod(x.strDate, x.endDate)))
								.collect(Collectors.toList()));
			} else {
				empHistory.setHistoryItems(new ArrayList<>());
			}
			result.add(empHistory);
		}

		return result;

	}

}
