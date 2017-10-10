package repository.person.setting.selection;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import entity.person.setting.selection.PpemtSelectionItem;
import entity.person.setting.selection.PpemtSelectionItemPK;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.IPerInfoSelectionItemRepository;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.PerInfoSelectionItem;

@Stateless
public class JpaPerInfoSelectionItemRepository extends JpaRepository implements IPerInfoSelectionItemRepository {

	private static final String SELECT_ALL = "SELECT si FROM PpemtSelectionItem si";
	private static final String SELECT_ALL_SELECTION_ITEM_BY_CONTRACTCODE = SELECT_ALL
			+ " WHERE si.contractCd = :contractCd";
	private static final String SELECT_All_SELECTION_ITEM_NAME = SELECT_ALL
			+ " WHERE si.selectionItemName = :selectionItemName";
	private static final String SELECT_All_SELECTION_ITEM_CLASSIFI = SELECT_ALL
			+ " WHERE si.selectionItemClassification = :selectionItemClassification";

	@Override
	public void add(PerInfoSelectionItem domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update(PerInfoSelectionItem domain) {
		this.commandProxy().update(toEntity(domain));
	}

	@Override
	public void remove(String selectionItemId) {
		PpemtSelectionItemPK pk = new PpemtSelectionItemPK(selectionItemId);
		this.commandProxy().remove(PpemtSelectionItem.class, pk);
	}

	@Override
	public List<PerInfoSelectionItem> getAllPerInfoSelectionItem(String contractCd) {
		return this.queryProxy().query(SELECT_ALL_SELECTION_ITEM_BY_CONTRACTCODE, PpemtSelectionItem.class)
				.setParameter("contractCd", contractCd).getList(c -> toDomain(c));
	}

	private PerInfoSelectionItem toDomain(PpemtSelectionItem entity) {
		return PerInfoSelectionItem.createFromJavaType(entity.selectionItemPk.selectionItemId, entity.selectionItemName,
				entity.memo, entity.selectionItemClsAtr, entity.contractCd, entity.integrationCd, entity.selectionCd,
				entity.characterTypeAtr, entity.selectionName, entity.selectionExtCd);
	}

	// check selectionItemId
	@Override
	public Optional<PerInfoSelectionItem> getPerInfoSelectionItem(String selectionItemId) {
		PpemtSelectionItemPK pk = new PpemtSelectionItemPK(selectionItemId);
		return this.queryProxy().find(pk, PpemtSelectionItem.class).map(c -> toDomain(c));
	}

	// check selectionItemName
	@Override
	public Optional<PerInfoSelectionItem> checkItemName(String selectionItemName) {
		return this.queryProxy().query(SELECT_All_SELECTION_ITEM_NAME, PpemtSelectionItem.class)
				.setParameter("selectionItemName", selectionItemName).getSingle(c -> toDomain(c));
	}

	// check SelectionItemClassification
	@Override
	public Optional<PerInfoSelectionItem> checkItemClassification(String selectionItemClassification) {
		return this.queryProxy().query(SELECT_All_SELECTION_ITEM_CLASSIFI, PpemtSelectionItem.class)
				.setParameter("selectionItemClassification", selectionItemClassification).getSingle(c -> toDomain(c));

	}

	@Override
	public boolean checkExist(String selectionItemId) {
		// TODO Auto-generated method stub
		return false;
	}

	private static PpemtSelectionItem toEntity(PerInfoSelectionItem domain) {
		PpemtSelectionItemPK key = new PpemtSelectionItemPK(domain.getSelectionItemId());
		return new PpemtSelectionItem(key, domain.getSelectionItemName().v(), domain.getContractCode(),
				domain.getIntegrationCode().v(), domain.getSelectionItemClassification().value,
				domain.getFormatSelection().getSelectionCode().v(), domain.getFormatSelection().getSelectionName().v(),
				domain.getFormatSelection().getSelectionExternalCode().v(),
				domain.getFormatSelection().getSelectionCodeCharacter().value, domain.getMemo().v());

	}

}
