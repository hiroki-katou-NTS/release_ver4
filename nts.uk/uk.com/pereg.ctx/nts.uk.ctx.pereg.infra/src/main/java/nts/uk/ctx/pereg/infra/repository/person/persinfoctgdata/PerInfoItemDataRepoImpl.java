/**
 * 
 */
package nts.uk.ctx.pereg.infra.repository.person.persinfoctgdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.ctx.pereg.dom.person.personinfoctgdata.item.PersonInfoItemData;
import nts.uk.ctx.pereg.infra.entity.person.info.ctg.PpemtPerInfoCtg;
import nts.uk.ctx.pereg.infra.entity.person.info.item.PpemtPerInfoItem;
import nts.uk.ctx.pereg.infra.entity.person.personinfoctgdata.PpemtPerInfoItemData;
import nts.uk.ctx.pereg.infra.entity.person.personinfoctgdata.PpemtPerInfoItemDataPK;

/**
 * @author danpv
 *
 */
@Stateless
public class PerInfoItemDataRepoImpl extends JpaRepository implements PerInfoItemDataRepository {

	private static final String SELECT_ALL_INFO_ITEM_NO_WHERE = "SELECT id,pi.requiredAtr,pi.itemName,pi.itemCd,ic.pInfoCtgId,pc.categoryCd FROM PpemtPerInfoItemData id"
			+ " INNER JOIN PpemtPerInfoItem pi"
			+ " ON id.primaryKey.perInfoDefId = pi.ppemtPerInfoItemPK.perInfoItemDefId"
			+ " INNER JOIN PpemtPerInfoCtgData ic" + " ON id.primaryKey.recordId = ic.recordId"
			+ " INNER JOIN PpemtPerInfoCtg pc" + " ON ic.pInfoCtgId = pc.ppemtPerInfoCtgPK.perInfoCtgId";

	private static final String GET_BY_RID = "SELECT itemData, itemInfo, infoCtg FROM PpemtPerInfoItemData itemData"
			+ " INNER JOIN PpemtPerInfoItem itemInfo ON itemData.primaryKey.perInfoDefId = itemInfo.ppemtPerInfoItemPK.perInfoItemDefId"
			+ " INNER JOIN PpemtPerInfoCtg infoCtg ON itemInfo.perInfoCtgId = infoCtg.ppemtPerInfoCtgPK.perInfoCtgId"
			+ " where itemData.primaryKey.recordId = :recordId";

	private static final String SELECT_ALL_INFO_ITEM_BY_CTGID_AND_PID = SELECT_ALL_INFO_ITEM_NO_WHERE
			+ " WHERE ic.pInfoCtgId = :ctgid AND ic.pId = :pid";

	private PersonInfoItemData toDomain(Object[] entity) {

		int dataStateType = entity[4] != null ? Integer.valueOf(entity[3].toString()) : 0;

		BigDecimal intValue = new BigDecimal(entity[6] != null ? Integer.valueOf(entity[6].toString()) : null);

		GeneralDate dateValue = GeneralDate.fromString(String.valueOf(entity[7].toString()), "yyyy-MM-dd");

		int isRequired = Integer.parseInt(entity[8] != null ? entity[9].toString() : "0");

		return PersonInfoItemData.createFromJavaType(entity[10].toString(), entity[0].toString(), entity[1].toString(),
				entity[11].toString(), entity[12].toString(), entity[9].toString(), isRequired, dataStateType,
				entity[5].toString(), intValue, dateValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.
	 * PerInfoItemDataRepository#getAllInfoItem(java.lang.String)
	 */
	@Override
	public List<PersonInfoItemData> getAllInfoItem(String categoryCd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PersonInfoItemData> getAllInfoItemByRecordId(String recordId) {

		List<Object[]> datas = this.queryProxy().query(GET_BY_RID).setParameter("recordId", recordId).getList();

		if (datas == null){
			return new ArrayList<>();
		}
			
		return datas.stream().map(data -> {
			PpemtPerInfoItemData itemData = (PpemtPerInfoItemData) data[0];
			PpemtPerInfoItem itemInfo = (PpemtPerInfoItem) data[1];
			PpemtPerInfoCtg infoCtg = (PpemtPerInfoCtg) data[2];
			return PersonInfoItemData.createFromJavaType(itemInfo.itemCd, itemInfo.ppemtPerInfoItemPK.perInfoItemDefId,
					itemData.primaryKey.recordId, infoCtg.ppemtPerInfoCtgPK.perInfoCtgId, infoCtg.categoryCd,
					itemInfo.itemName, itemInfo.requiredAtr, itemData.saveDataAtr, itemData.stringVal, itemData.intVal,
					itemData.dateVal);
		}).collect(Collectors.toList());
		
	}

	// sonnlb code start
	private PpemtPerInfoItemData toEntity(PersonInfoItemData domain) {

		PpemtPerInfoItemDataPK key = new PpemtPerInfoItemDataPK(domain.getRecordId(), domain.getPerInfoItemDefId());

		String stringValue = domain.getDataState().getStringValue();

		BigDecimal intValue = domain.getDataState().getNumberValue();

		GeneralDate dateValue = domain.getDataState().getDateValue();

		return new PpemtPerInfoItemData(key, domain.getDataState().getDataStateType().value, stringValue, intValue,
				dateValue);
	}

	private void updateEntity(PersonInfoItemData domain, PpemtPerInfoItemData entity) {
		entity.saveDataAtr = domain.getDataState().getDataStateType().value;
		entity.stringVal = domain.getDataState().getStringValue();
		entity.intVal = domain.getDataState().getNumberValue();
		entity.dateVal = domain.getDataState().getDateValue();
	}

	/**
	 * Add item data
	 * 
	 * @param domain
	 */
	@Override
	public void addItemData(PersonInfoItemData domain) {
		this.commandProxy().insert(toEntity(domain));

	}
	// sonnlb code end

	@Override
	public void updateItemData(PersonInfoItemData domain) {
		PpemtPerInfoItemDataPK key = new PpemtPerInfoItemDataPK(domain.getRecordId(), domain.getPerInfoItemDefId());
		Optional<PpemtPerInfoItemData> existItem = this.queryProxy().find(key, PpemtPerInfoItemData.class);
		if (!existItem.isPresent()) {
			return;
		}
		if (!existItem.isPresent()) {
			throw new RuntimeException("invalid PersonInfoItemData");
		}
		// Update entity
		updateEntity(domain, existItem.get());
		// Update table
		this.commandProxy().update(existItem.get());
	}

	@Override
	public void deleteItemData(PersonInfoItemData domain) {
		PpemtPerInfoItemDataPK key = new PpemtPerInfoItemDataPK(domain.getRecordId(), domain.getPerInfoItemDefId());
		this.commandProxy().remove(PpemtPerInfoItemData.class, key);

	}

	@Override
	public List<PersonInfoItemData> getAllInfoItemByPidCtgId(String ctgId, String pid) {

		return this.queryProxy().query(SELECT_ALL_INFO_ITEM_BY_CTGID_AND_PID, Object[].class)
				.setParameter("ctgid", ctgId).setParameter("pid", pid).getList(c -> toDomain(c));
	}

}
