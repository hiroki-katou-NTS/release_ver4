/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.itemmaster;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.report.app.itemmaster.query.ItemMasterCategory;
import nts.uk.ctx.pr.report.app.itemmaster.query.ItemMaterRepository;
import nts.uk.ctx.pr.report.app.itemmaster.query.MasterItemDto;

/**
 * The Class JpaItemMasterRepository.
 */
@Stateless
public class JpaItemMasterQueryRepository extends JpaRepository implements ItemMaterRepository {
	
	/** The Constant SELECTE_FROM_MASTER_ITEM_TABLE. */
	private static final String SELECTE_FROM_MASTER_ITEM_TABLE = "SELECT c.qcamtItemPK.itemCd,"
			+ " c.qcamtItemPK.ctgAtr, c.itemName FROM QcamtItem c";

	/** The Constant SELECT_ALL. */
	private static final String SELECT_ALL = SELECTE_FROM_MASTER_ITEM_TABLE 
			+ " WHERE c.qcamtItemPK.ccd = :companyCode ";

	/** The Constant SELECT_BY_CODES. */
	private static final String SELECT_BY_CODES = SELECTE_FROM_MASTER_ITEM_TABLE
			+ " WHERE c.qcamtItemPK.ccd = :companyCode AND  c.qcamtItemPK.itemCd IN :itemCodeList ";

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.app.itemmaster.query.ItemMaterRepository
	 * #findAll(java.lang.String)
	 */
	@Override
	public List<MasterItemDto> findAll(String companyCode) {
		return this.queryProxy().query(SELECT_ALL)
				.setParameter("companyCode", companyCode)
				.getList()
				.stream().map(data -> {
					return MasterItemDto.builder()
							.code((String) data[0])
							.name((String) data[2])
							.category(ItemMasterCategory.valueOf((int) data[1]))
							.build();
				}).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.app.itemmaster.query.ItemMaterRepository
	 * #findByCodes(java.lang.String, java.util.List)
	 */
	@Override
	public List<MasterItemDto> findByCodes(String companyCode, List<String> codes) {
		if (CollectionUtil.isEmpty(codes)) {
			return Collections.emptyList();
		}
		return this.queryProxy().query(SELECT_BY_CODES)
				.setParameter("companyCode", companyCode)
				.setParameter("itemCodeList", codes)
				.getList()
				.stream().map(data -> {
					return MasterItemDto.builder()
							.code((String) data[0])
							.name((String) data[2])
							.category(ItemMasterCategory.valueOf((int) data[1]))
							.build();
				}).collect(Collectors.toList());
	}

}
