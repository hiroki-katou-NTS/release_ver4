package nts.uk.ctx.bs.employee.infra.repository.tempabsence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsHistRepository;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsenceHistory;
import nts.uk.ctx.bs.employee.infra.entity.temporaryabsence.BsymtTempAbsHistory;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaTempAbsHist extends JpaRepository implements TempAbsHistRepository {

	private final String QUERY_GET_TEMPORARYABSENCE_BYSID = "SELECT ta FROM BsymtTempAbsHistory ta"
			+ " WHERE ta.sid = :sid ORDER BY ta.startDate";

	/**
	 * Convert from domain to entity
	 * 
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private BsymtTempAbsHistory toEntity(String employeeID, DateHistoryItem item) {
		return new BsymtTempAbsHistory(item.identifier(), employeeID, item.start(), item.end());
	}

	/**
	 * Update entity from domain
	 * 
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private void updateEntity(DateHistoryItem item, BsymtTempAbsHistory entity) {
		entity.startDate = item.start();
		entity.endDate = item.end();
	}

	/**
	 * Convert from entity to domain
	 * 
	 * @param entity
	 * @return
	 */
	private TempAbsenceHistory toDomain(BsymtTempAbsHistory entity) {
		TempAbsenceHistory domain = new TempAbsenceHistory(entity.sid, new ArrayList<DateHistoryItem>());

		DateHistoryItem dateItem = new DateHistoryItem(entity.histId, new DatePeriod(entity.startDate, entity.endDate));
		domain.add(dateItem);

		return domain;
	}

	@Override
	public void add(String sid, DateHistoryItem item) {
		this.commandProxy().insert(toEntity(sid, item));
	}

	@Override
	public void update(DateHistoryItem item) {

		Optional<BsymtTempAbsHistory> histItem = this.queryProxy().find(item.identifier(), BsymtTempAbsHistory.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtAffiWorkplaceHist");
		}
		updateEntity(item, histItem.get());
		this.commandProxy().update(histItem.get());
	}

	@Override
	public void delete(String histId) {
		Optional<BsymtTempAbsHistory> histItem = this.queryProxy().find(histId, BsymtTempAbsHistory.class);
		if (!histItem.isPresent()) {
			throw new RuntimeException("invalid BsymtTempAbsHistory");
		}
		this.commandProxy().remove(BsymtTempAbsHistory.class, histId);
	}

	@Override
	public Optional<TempAbsenceHistory> getByHistId(String histId) {
		/*
		 * Optional<BsymtTempAbsHistory> existItem =
		 * this.queryProxy().find(histId, BsymtTempAbsHistory.class); if
		 * (existItem.isPresent()) { return
		 * Optional.of(toDomain(existItem.get())); } return Optional.empty();
		 */
		return Optional.of(new TempAbsenceHistory("909909139840",
				Arrays.asList(new DateHistoryItem("123456789012345678901234567890123456",
						new DatePeriod(GeneralDate.ymd(2017, 5, 20), GeneralDate.ymd(2017, 10, 10))))));
	}

	/**
	 * Convert to domain TempAbsenceHistory
	 * 
	 * @param employeeId
	 * @param listHist
	 * @return
	 */
	private TempAbsenceHistory toDomainTemp(String employeeId, List<BsymtTempAbsHistory> listHist) {
		TempAbsenceHistory domain = new TempAbsenceHistory(employeeId, new ArrayList<DateHistoryItem>());
		for (BsymtTempAbsHistory item : listHist) {
			DateHistoryItem dateItem = new DateHistoryItem(item.histId, new DatePeriod(item.startDate, item.endDate));
			domain.add(dateItem);
		}
		return domain;
	}

	@Override
	public Optional<TempAbsenceHistory> getByEmployeeId(String employeeId) {
		List<BsymtTempAbsHistory> listHist = this.queryProxy()
				.query(QUERY_GET_TEMPORARYABSENCE_BYSID, BsymtTempAbsHistory.class).setParameter("sid", employeeId)
				.getList();
		if (!listHist.isEmpty()) {
			return Optional.of(toDomainTemp(employeeId, listHist));
		}
		return Optional.empty();
	}

}
