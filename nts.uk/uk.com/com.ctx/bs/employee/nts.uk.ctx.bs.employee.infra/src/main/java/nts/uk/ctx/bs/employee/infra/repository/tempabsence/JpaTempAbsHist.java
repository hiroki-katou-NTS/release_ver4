package nts.uk.ctx.bs.employee.infra.repository.tempabsence;

import java.util.ArrayList;
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
	
	private final String GET_BY_SID_DATE = "select h from BsymtTempAbsHistory h"
			+ " where h.sid = :sid and h.startDate <= :standardDate and h.endDate >= :standardDate";

	/**
	 * Convert from domain to entity
	 * 
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private BsymtTempAbsHistory toEntity(String companyId, String employeeID, DateHistoryItem item) {
		return new BsymtTempAbsHistory(item.identifier(), companyId, employeeID, item.start(), item.end());
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

	@Override
	public void add(String cid, String sid, DateHistoryItem item) {
		this.commandProxy().insert(toEntity(cid, sid, item));
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
	public Optional<DateHistoryItem> getByHistId(String histId) {

		Optional<BsymtTempAbsHistory> existItem = this.queryProxy().find(histId, BsymtTempAbsHistory.class);
		if (existItem.isPresent()) {
			BsymtTempAbsHistory entity = existItem.get();
			return Optional.of(new DateHistoryItem(entity.histId, new DatePeriod(entity.startDate, entity.endDate)));
		}
		return Optional.empty();

	}

	@Override
	public Optional<DateHistoryItem> getItemByEmpIdAndStandardDate(String employeeId, GeneralDate standardDate) {
		Optional<BsymtTempAbsHistory> optionData = this.queryProxy().query(GET_BY_SID_DATE, BsymtTempAbsHistory.class)
				.setParameter("sid", employeeId).setParameter("standardDate", standardDate).getSingle();
		if (optionData.isPresent()) {
			BsymtTempAbsHistory entity = optionData.get();
			return Optional.of(new DateHistoryItem(entity.histId, new DatePeriod(entity.startDate, entity.endDate)));
		}
		return Optional.empty();
	}

	/**
	 * Convert to domain TempAbsenceHistory
	 * 
	 * @param employeeId
	 * @param listHist
	 * @return
	 */
	private TempAbsenceHistory toDomainTemp(List<BsymtTempAbsHistory> listHist) {
		TempAbsenceHistory domain = new TempAbsenceHistory(listHist.get(0).cid, listHist.get(0).sid, new ArrayList<DateHistoryItem>());
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
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toDomainTemp(listHist));
		}
		return Optional.empty();
	}

}
