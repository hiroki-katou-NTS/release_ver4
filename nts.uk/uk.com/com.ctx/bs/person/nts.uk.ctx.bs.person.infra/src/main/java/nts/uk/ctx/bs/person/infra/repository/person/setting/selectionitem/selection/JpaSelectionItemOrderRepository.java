package nts.uk.ctx.bs.person.infra.repository.person.setting.selectionitem.selection;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionItemOrder;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionItemOrderRepository;
import nts.uk.ctx.bs.person.infra.entity.person.setting.selectionitem.selection.PpemtSelItemOrder;
import nts.uk.ctx.bs.person.infra.entity.person.setting.selectionitem.selection.PpemtSelItemOrderPK;

/**
 * 
 * @author tuannv
 *
 */

@Stateless
public class JpaSelectionItemOrderRepository extends JpaRepository implements SelectionItemOrderRepository {

	private static final String SELECT_ALL = "SELECT si FROM PpemtSelItemOrder si";
	private static final String SELECT_ALL_HISTORY_ID = SELECT_ALL + " WHERE si.histId = :histId";
	private static final String SELECT_ALL_SELECTION_ID = SELECT_ALL
			+ " WHERE si.selectionIdPK.selectionId = :selectionId";

	@Override
	public void add(SelectionItemOrder selectionItemOrder) {
		this.commandProxy().insert(toEntity(selectionItemOrder));

	}

	@Override
	public void remove(String selectionItemOrder) {
		PpemtSelItemOrderPK pk = new PpemtSelItemOrderPK(selectionItemOrder);
		this.commandProxy().remove(PpemtSelItemOrder.class, pk);

	}

	// Domain:
	private SelectionItemOrder toDomain(PpemtSelItemOrder entity) {
		return SelectionItemOrder.selectionItemOrder(entity.selectionIdPK.selectionId, entity.histId, entity.dispOrder,
				entity.initSelection);

	}

	// to Entity:
	private static PpemtSelItemOrder toEntity(SelectionItemOrder domain) {
		PpemtSelItemOrderPK key = new PpemtSelItemOrderPK(domain.getSelectionID());
		return new PpemtSelItemOrder(key, domain.getHistId(), domain.getDisporder().v(),
				domain.getInitSelection().value);
	}

	@Override
	public List<SelectionItemOrder> getAllOrderSelectionByHistId(String histId) {
		return this.queryProxy().query(SELECT_ALL_HISTORY_ID, PpemtSelItemOrder.class).setParameter("histId", histId)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<SelectionItemOrder> getAllOrderBySelectionId(String selectionId) {
		return this.queryProxy().query(SELECT_ALL_SELECTION_ID, PpemtSelItemOrder.class)
				.setParameter("selectionId", selectionId).getList(c -> toDomain(c));
	}

	// hoatt
	/**
	 * update List Selection Item Order
	 * 
	 * @param lstSelOrder
	 */
	@Override
	public void updateListSelOrder(List<SelectionItemOrder> lstSelOrder) {
		List<PpemtSelItemOrder> lstEntity = new ArrayList<>();
		for (SelectionItemOrder selItemOrder : lstSelOrder) {
			PpemtSelItemOrder selOrderUi = toEntity(selItemOrder);
			PpemtSelItemOrder selOrder = this.queryProxy()
					.find(new PpemtSelItemOrderPK(selItemOrder.getSelectionID()), PpemtSelItemOrder.class).get();
			selOrder.setDispOrder(selOrderUi.dispOrder);
			selOrder.setInitSelection(selOrderUi.initSelection);
			lstEntity.add(toEntity(selItemOrder));
		}
		this.commandProxy().updateAll(lstEntity);
	}
}
