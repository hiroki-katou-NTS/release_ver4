package repository.person.setting.selectionitem;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import entity.person.setting.selectionitem.PpemtHistorySelection;
import entity.person.setting.selectionitem.PpemtHistorySelectionPK;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.PerInfoHistorySelection;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.PerInfoHistorySelectionRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaPerInfoHistorySelectionRepository extends JpaRepository implements PerInfoHistorySelectionRepository {

	private static final String SELECT_ALL = "SELECT si FROM PpemtHistorySelection si";
	private static final String SELECT_ALL_HISTORY_SELECTION = SELECT_ALL
			+ " WHERE si.selectionItemId = :selectionItemId";

	private static final String SELECT_ALL_HISTORY_STARTDATE_SELECTION = SELECT_ALL
			+ " WHERE si.startDate > :startDate AND si.endDate = :endDate";

	private static final String SELECT_ALL_HISTORY_COMPANYID_SELECTION = SELECT_ALL
			+ " WHERE si.selectionItemId = :selectionItemId AND si.companyId=:companyId";

	private static final String SELECT_HISTORY_BY_DATE = "SELECT a" + " FROM PpemtHistorySelection a"
			+ " INNER JOIN PpemtSelItemOrder b" + " ON a.selectionItemId = b.selectionIdPK.selectionId"
			+ " AND a.histidPK.histId = b.histId" + " AND a.selectionItemId IN :lstSelItemId"
			+ " AND a.startDate <= :baseDate" + " AND a.endDate >= :baseDate" + " ORDER BY b.dispOrder";

	private static final String SELECT_ALL_HISTID = SELECT_ALL + " WHERE si.histId = :histId";

	@Override
	public void add(PerInfoHistorySelection domain) {
		this.commandProxy().insert(toHistEntity(domain));

	}

	@Override
	public void update(PerInfoHistorySelection domain) {
		this.commandProxy().update(toHistEntity(domain));

	}

	@Override
	public void remove(String histId) {
		PpemtHistorySelectionPK pk = new PpemtHistorySelectionPK(histId);
		this.commandProxy().remove(PpemtHistorySelection.class, pk);

	}

	private PerInfoHistorySelection toDomain(PpemtHistorySelection entity) {
		DatePeriod datePeriod = new DatePeriod(entity.startDate, entity.endDate);

		return PerInfoHistorySelection.createHistorySelection(entity.histidPK.histId, entity.selectionItemId,
				entity.companyId, datePeriod);
	}

	@Override
	public List<PerInfoHistorySelection> getAllHistoryBySelectionItemId(String selectionItemId) {

		return this.queryProxy().query(SELECT_ALL_HISTORY_SELECTION, PpemtHistorySelection.class)
				.setParameter("selectionItemId", selectionItemId).getList(c -> toDomain(c));
	}

	public Optional<PerInfoHistorySelection> getAllHistoryByHistId(String histId) {
		PpemtHistorySelectionPK pkHistorySelection = new PpemtHistorySelectionPK(histId);

		return this.queryProxy().find(pkHistorySelection, PpemtHistorySelection.class).map(c -> toDomain(c));
	}

	private static PpemtHistorySelection toHistEntity(PerInfoHistorySelection domain) {
		PpemtHistorySelectionPK key = new PpemtHistorySelectionPK(domain.getHistId());

		return new PpemtHistorySelection(key, domain.getSelectionItemId(), domain.getCompanyCode(),
				domain.getPeriod().start(), domain.getPeriod().end());
	}

	// historyStartDateSelection
	@Override
	public List<PerInfoHistorySelection> historyStartDateSelection(GeneralDate startDate) {
		GeneralDate endDate = GeneralDate.fromString("9999/12/31", "yyyy/MM/dd");
		return this.queryProxy().query(SELECT_ALL_HISTORY_STARTDATE_SELECTION, PpemtHistorySelection.class)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getList(c -> toDomain(c));
	}

	// test:
	@Override
	public List<PerInfoHistorySelection> getAllPerInfoHistorySelection(String selectionItemId, String companyId) {

		return this.queryProxy().query(SELECT_ALL_HISTORY_COMPANYID_SELECTION, PpemtHistorySelection.class)
				.setParameter("selectionItemId", selectionItemId).setParameter("companyId", companyId)
				.getList(c -> toDomain(c));
	}

	// hoatt
	@Override
	public List<PerInfoHistorySelection> getHistorySelItemByDate(GeneralDate baseDate, List<String> lstSelItemId) {
		return this.queryProxy().query(SELECT_HISTORY_BY_DATE, PpemtHistorySelection.class)
				.setParameter("baseDate", baseDate).setParameter("lstSelItemId", lstSelItemId)
				.getList(c -> toDomain(c));
	}

	// Tuannv:
	@Override
	public List<String> getAllHistId(String histId) {
		return queryProxy().query(SELECT_ALL_HISTID, String.class).setParameter("histId", histId).getList();
	}

}
