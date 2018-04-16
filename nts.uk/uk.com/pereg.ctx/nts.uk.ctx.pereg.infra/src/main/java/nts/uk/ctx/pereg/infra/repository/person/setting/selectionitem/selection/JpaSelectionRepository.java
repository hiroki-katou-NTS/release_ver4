package nts.uk.ctx.pereg.infra.repository.person.setting.selectionitem.selection;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.Selection;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionRepository;
import nts.uk.ctx.pereg.infra.entity.person.setting.selectionitem.selection.PpemtSelection;
import nts.uk.ctx.pereg.infra.entity.person.setting.selectionitem.selection.PpemtSelectionPK;

/**
 * 
 * @author tuannv
 *
 */

@Stateless
public class JpaSelectionRepository extends JpaRepository implements SelectionRepository {

	private static final String SELECT_ALL = "SELECT si FROM PpemtSelection si";
	private static final String SELECT_ALL_HISTORY_ID = SELECT_ALL + " WHERE si.histId = :histId";
	private static final String SELECT_ALL_SELECTION_CD = SELECT_ALL
			+ " WHERE si.selectionCd = :selectionCd AND si.histId = :histId";
	private static final String SELECT_ALL_SELECTION_BY_SELECTIONID = SELECT_ALL
			+ " WHERE si.selectionId = :selectionId";

	private static final String SEL_ALL_BY_SEL_ID_PERSON_TYPE = " SELECT se , item.selectionItemName FROM PpemtSelectionItem item"
			+ " INNER JOIN PpemtHistorySelection his ON item.selectionItemPk.selectionItemId = his.selectionItemId"
			+ " INNER JOIN PpemtSelection se ON his.histidPK.histId = se.histId"
			+ " INNER JOIN PpemtSelItemOrder o ON his.histidPK.histId = o.histId AND se.selectionId.selectionId = o.selectionIdPK.selectionId"
			+ " WHERE his.startDate <= :baseDate AND his.endDate >= :baseDate"
			+ " AND item.selectionItemPk.selectionItemId =:selectionItemId AND item.selectionItemClsAtr =:selectionItemClsAtr"
			+ " AND his.companyId =:cid"
			+ " ORDER BY o.dispOrder";
	
	// selection for company
	private static final String SEL_ALL_BY_SEL_ID_PERSON_TYPE_BY_CID = " SELECT se , item.selectionItemName FROM PpemtSelectionItem  item"
			+ " INNER JOIN PpemtHistorySelection his "
			+ " ON item.selectionItemPk.selectionItemId = his.selectionItemId" + " INNER JOIN PpemtSelection se"
			+ " ON his.histidPK.histId = se.histId" + " INNER JOIN PpemtSelItemOrder order"
			+ " ON his.histidPK.histId = order.histId "
			+ " AND se.selectionId.selectionId = order.selectionIdPK.selectionId " + " WHERE his.startDate <= :baseDate"
			+ " AND his.endDate >= :baseDate " + " AND item.selectionItemPk.selectionItemId =:selectionItemId"
			+ " AND  his.companyId =:companyId"
			+ " ORDER BY order.dispOrder";
	
	private static final String SEL_ALL_BY_SEL_ID = " SELECT se FROM PpemtSelectionItem  item"
			+ " INNER JOIN PpemtHistorySelection his "
			+ " ON item.selectionItemPk.selectionItemId = his.selectionItemId" + " INNER JOIN PpemtSelection se"
			+ " ON his.histidPK.histId = se.histId" + " INNER JOIN PpemtSelItemOrder order"
			+ " ON his.histidPK.histId = order.histId "
			+ " AND se.selectionId.selectionId = order.selectionIdPK.selectionId " + " WHERE his.startDate <= :baseDate"
			+ " AND his.endDate >= :baseDate " + " AND item.selectionItemPk.selectionItemId =:selectionItemId"
			+ " ORDER BY order.dispOrder";
	// Lanlt

	@Override
	public void add(Selection selection) {
		this.commandProxy().insert(toEntity(selection));

	}

	@Override
	public void update(Selection selection) {
		PpemtSelection newEntity = toEntity(selection);
		PpemtSelection updateEntity = this.queryProxy().find(newEntity.selectionId, PpemtSelection.class).get();
		updateEntity.selectionName = newEntity.selectionName;
		updateEntity.externalCd = newEntity.externalCd;
		updateEntity.histId = newEntity.histId;
		updateEntity.selectionCd = newEntity.selectionCd;
		updateEntity.memo = newEntity.memo;
		this.commandProxy().update(updateEntity);
	}

	@Override
	public void remove(String selectionId) {
		PpemtSelectionPK pk = new PpemtSelectionPK(selectionId);
		this.commandProxy().remove(PpemtSelection.class, pk);

	}

	// Domain:
	private Selection toDomain(PpemtSelection entity) {
		return Selection.createFromSelection(entity.selectionId.selectionId, entity.histId, entity.selectionCd,
				entity.selectionName, entity.externalCd, entity.memo);

	}
	
	// Domain:
	private Selection toDomain(Object[] entity) {
		PpemtSelection sel =  (PpemtSelection) entity[0];
		Selection sel1 = Selection.createFromSelection(sel.selectionId.selectionId, sel.histId, sel.selectionCd,
				sel.selectionName, sel.externalCd, sel.memo, entity[1].toString());
		return sel1;

	}

	@Override
	public List<Selection> getAllSelectByHistId(String histId) {
		return this.queryProxy().query(SELECT_ALL_HISTORY_ID, PpemtSelection.class).setParameter("histId", histId)
				.getList(c -> toDomain(c));
	}

	@Override
	public Optional<Selection> getSelectionByHistId(String histId) {
		return this.queryProxy().query(SELECT_ALL_HISTORY_ID, PpemtSelection.class).setParameter("histId", histId)
				.getSingle(c -> toDomain(c));
	}

	// to Entity:
	private static PpemtSelection toEntity(Selection domain) {
		PpemtSelectionPK key = new PpemtSelectionPK(domain.getSelectionID());
		return new PpemtSelection(key, domain.getHistId(), domain.getSelectionCD().v(), domain.getSelectionName().v(),
				domain.getExternalCD().v(), domain.getMemoSelection().v());

	}

	// check by selectionCD:
	@Override
	public Optional<Selection> getSelectionBySelectionCd(String selectionCd) {

		return this.queryProxy().query(SELECT_ALL_SELECTION_CD, PpemtSelection.class)
				.setParameter("selectionCd", selectionCd).getSingle(c -> toDomain(c));
	}

	@Override
	public List<Selection> getAllSelectionBySelectionCdAndHistId(String selectionCd, String histId) {
		return queryProxy().query(SELECT_ALL_SELECTION_CD, PpemtSelection.class)
				.setParameter("selectionCd", selectionCd).setParameter("histId", histId).getList(c -> toDomain(c));

	}

	@Override
	public List<String> getAllHistId(String histId) {
		return queryProxy().query(SELECT_ALL_HISTORY_ID, String.class).setParameter("histId", histId).getList();

	}

	// Lanlt
	@Override
	public List<Selection> getAllSelectionByHistoryId(String cid, String selectionItemId, GeneralDate baseDate, int selectionItemClsAtr) {
		return this.queryProxy().query(SEL_ALL_BY_SEL_ID_PERSON_TYPE, Object[].class)
				.setParameter("cid", cid).setParameter("selectionItemClsAtr", selectionItemClsAtr)
				.setParameter("selectionItemId", selectionItemId).setParameter("baseDate", baseDate)
				.getList(c -> toDomain(c));
	}
	// Lanlt

	@Override
	public List<Selection> getAllSelectionBySelectionID(String selectionId) {
		return this.queryProxy().query(SELECT_ALL_SELECTION_BY_SELECTIONID, PpemtSelection.class)
				.setParameter("selectionId", selectionId).getList(c -> toDomain(c));
	}

	@Override
	public List<Selection> getAllSelectionByHistoryId(String selectionItemId, GeneralDate baseDate) {
		List<Selection> selectionLst = this.queryProxy().query(SEL_ALL_BY_SEL_ID, PpemtSelection.class)
				.setParameter("selectionItemId", selectionItemId).setParameter("baseDate", baseDate)
				.getList(c -> toDomain(c));
		return selectionLst;
	}

	@Override
	public List<Selection> getAllSelectionByCompanyId(String companyId, String selectionItemId, GeneralDate baseDate) {
		List<Selection> selectionLst = this.queryProxy().query(SEL_ALL_BY_SEL_ID_PERSON_TYPE_BY_CID, Object[].class)
				.setParameter("selectionItemId", selectionItemId).setParameter("baseDate", baseDate)
				.setParameter("companyId", companyId)
				.getList(c -> toDomain(c));
		return selectionLst;
	}

}
