/**
 * 
 */
package nts.uk.ctx.pereg.infra.repository.person.setting.matrix.personinfomatrixitem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.PersonInfoMatrixData;
import nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.PersonInfoMatrixItem;
import nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.PersonInfoMatrixItemRepo;
import nts.uk.ctx.pereg.infra.entity.person.setting.matrix.personinfomatrixitem.PpestPersonInfoMatrixItem;
import nts.uk.ctx.pereg.infra.entity.person.setting.matrix.personinfomatrixitem.PpestPersonInfoMatrixItemPK;

/**
 * @author hieult
 *
 */
@Stateless
public class JpaPersonInfoMatrixItem extends JpaRepository implements PersonInfoMatrixItemRepo {

	private static final String SELECT_BY_KEY = "SELECT c FROM PpestPersonInfoMatrixItem c WHERE c.ppestPersonInfoMatrixItemPK.pInfoCategoryID = :pInfoCategoryID"
			+ " AND c.pInfoDefiID = :pInfoDefiID";

	private static final String SELECT_BY_CATEGORY_ID = "SELECT c FROM PpestPersonInfoMatrixItem c WHERE c.ppestPersonInfoMatrixItemPK.pInfoCategoryID = :pInfoCategoryID";

	private static final String SELECT_DATA_INFO = String.join(" ", "SELECT",
			"pii.PER_INFO_ITEM_DEFINITION_ID, pii.ITEM_CD, pii.ITEM_NAME, pim.REGULATION_ATR, pio.DISPORDER, pim.COLUMN_WIDTH, pii.REQUIRED_ATR",
			"FROM [OOTSUKATEST].[dbo].[PPEMT_PER_INFO_ITEM] pii",
			"LEFT JOIN [OOTSUKATEST].[dbo].[PPEST_PERSON_INFO_MATRIX] pim",
			"ON pii.PER_INFO_ITEM_DEFINITION_ID = pim.PERSON_INFO_ITEM_ID",
			"AND pii.PER_INFO_CTG_ID = pim.PERSON_INFO_CATEGORY_ID",
			"LEFT JOIN [OOTSUKATEST].[dbo].[PPEMT_PER_INFO_ITEM_ORDER] pio",
			"ON pio.PER_INFO_CTG_ID = pii.PER_INFO_CTG_ID",
			"AND pio.PER_INFO_ITEM_DEFINITION_ID = pii.PER_INFO_ITEM_DEFINITION_ID", "WHERE pii.PER_INFO_CTG_ID = ?",
			"AND pii.ABOLITION_ATR = 0");

	@Override
	public Optional<PersonInfoMatrixItem> findbyKey(String pInfoCategoryID, String pInfoDefiID) {
		// TODO Auto-generated method stub
		return this.queryProxy().query(SELECT_BY_KEY, PpestPersonInfoMatrixItem.class)
				.setParameter("pInfoCategoryID", pInfoCategoryID).setParameter("pInfoDefiID", pInfoDefiID)
				.getSingle(c -> c.toDomain());
	}

	@Override
	public void update(PersonInfoMatrixItem newSetting) {

		PpestPersonInfoMatrixItem newEntity = PpestPersonInfoMatrixItem.toEntity(newSetting);
		PpestPersonInfoMatrixItem updateEntity = this.queryProxy()
				.find(newEntity.ppestPersonInfoMatrixItemPK, PpestPersonInfoMatrixItem.class).get();
		updateEntity.columnWidth = newEntity.columnWidth;
		updateEntity.regulationATR = newEntity.regulationATR;
		this.commandProxy().update(updateEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.
	 * PersonInfoMatrixItemRepo#findByCategoryID(java.lang.String)
	 */
	@Override
	public List<PersonInfoMatrixItem> findByCategoryID(String pInfoCategoryID) {

		return this.queryProxy().query(SELECT_BY_CATEGORY_ID, PpestPersonInfoMatrixItem.class)
				.setParameter("pInfoCategoryID", pInfoCategoryID).getList(c -> c.toDomain());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.
	 * PersonInfoMatrixItemRepo#insert(nts.uk.ctx.pereg.dom.person.setting.matrix.
	 * personinfomatrixitem.PersonInfoMatrixItem)
	 */
	@Override
	public void insert(PersonInfoMatrixItem newSetting) {
		PpestPersonInfoMatrixItem newEntity = PpestPersonInfoMatrixItem.toEntity(newSetting);
		Optional<PpestPersonInfoMatrixItem> updateEntity = this.queryProxy().find(newEntity.ppestPersonInfoMatrixItemPK,
				PpestPersonInfoMatrixItem.class);
		if (!updateEntity.isPresent()) {
			this.commandProxy().insert(PpestPersonInfoMatrixItem.toEntity(newSetting));
		} else {
			this.commandProxy().update(updateEntity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.
	 * PersonInfoMatrixItemRepo#insertAll(java.util.List)
	 */
	@Override
	public void insertAll(List<PersonInfoMatrixItem> listNewSetting) {
		List<PpestPersonInfoMatrixItem> listEntity = listNewSetting.stream().map(c -> toEntity(c))
				.collect(Collectors.toList());
		commandProxy().insertAll(listEntity);
	}

	private PpestPersonInfoMatrixItem toEntity(PersonInfoMatrixItem domain) {
		PpestPersonInfoMatrixItemPK pk = new PpestPersonInfoMatrixItemPK(domain.getPInfoCategoryID(),
				domain.getPInfoItemDefiID());
		return new PpestPersonInfoMatrixItem(pk, domain.getColumnWidth(), domain.getRegulationATR().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pereg.dom.person.setting.matrix.personinfomatrixitem.
	 * PersonInfoMatrixItemRepo#findInfoData(java.lang.String, int)
	 */
	@Override
	public List<PersonInfoMatrixData> findInfoData(String pInfoCategoryID) {
		if (pInfoCategoryID.isEmpty()) {
			return new ArrayList<>();
		}

		@SuppressWarnings("unchecked")
		List<Object[]> result = this.getEntityManager()
				.createNativeQuery(SELECT_DATA_INFO)
				.setParameter(1, pInfoCategoryID).getResultList();

		return result.stream().map(m -> new PersonInfoMatrixData(
					m[0] != null ? (String) m[0] : "", 	// perInfoItemDefID
					m[1] != null ? (String) m[1] : "", 	// itemCD
					m[2] != null ? (String) m[2] : "", 	// itemName
					m[3] != null ? ((BigDecimal) m[3]).intValue() == 1 : false,	// regulationATR
					m[4] != null ? ((BigDecimal) m[4]).intValue() : 0,	// dispOrder
					m[5] != null ? ((BigDecimal) m[5]).intValue() : 100, // width
					m[6] != null ? ((BigDecimal) m[6]).intValue() == 1 : false	//required
			)).sorted((o1, o2) -> o1.getDispOrder() - o2.getDispOrder())
			.collect(Collectors.toList());
	}
}
