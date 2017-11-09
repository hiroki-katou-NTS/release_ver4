/**
 * 
 */
package repository.person.persinfoctgdata;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import entity.person.personinfoctgdata.PpemtPerInfoItemData;
import entity.person.personinfoctgdata.PpemtPerInfoItemDataPK;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.ctx.bs.person.dom.person.personinfoctgdata.item.PersonInfoItemData;

/**
 * @author danpv
 *
 */
@Stateless
public class PerInfoItemDataRepoImpl extends JpaRepository implements PerInfoItemDataRepository {

	private static final String GET_BY_RID = "select idata from PpemtPerInfoItemData idata"
			+ " where idata.primaryKey.recordId = :recordId";

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
		List<PpemtPerInfoItemData> datas = this.queryProxy().query(GET_BY_RID, PpemtPerInfoItemData.class).getList();
		return datas.stream()
				.map(ent -> PersonInfoItemData.createFromJavaType(ent.primaryKey.perInfoDefId, ent.primaryKey.recordId,
						ent.saveDataAtr, ent.stringVal, BigDecimal.valueOf(ent.intVal), ent.dateVal))
				.collect(Collectors.toList());
	}

	// sonnlb code start
	private PpemtPerInfoItemData toEntity(PersonInfoItemData domain) {

		PpemtPerInfoItemDataPK key = new PpemtPerInfoItemDataPK(domain.getRecordId(), domain.getPerInfoItemDefId());

		String stringValue = domain.getDataState().getStringValue();

		int intValue = domain.getDataState().getNumberValue().intValue();

		GeneralDate dateValue = domain.getDataState().getDateValue();

		return new PpemtPerInfoItemData(key, domain.getDataState().getDataStateType().value, stringValue, intValue,
				dateValue);
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

}
