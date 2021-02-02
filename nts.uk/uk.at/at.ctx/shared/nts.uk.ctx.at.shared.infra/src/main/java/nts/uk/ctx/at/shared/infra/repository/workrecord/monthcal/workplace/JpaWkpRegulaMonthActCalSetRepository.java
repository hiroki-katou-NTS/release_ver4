/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workrecord.monthcal.workplace;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.other.wkp.WkpRegulaMonthActCalSet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.other.wkp.WkpRegulaMonthActCalSetRepo;
import nts.uk.ctx.at.shared.infra.entity.workrecord.monthcal.workplace.KrcmtCalcMSetRegWkp;
import nts.uk.ctx.at.shared.infra.entity.workrecord.monthcal.workplace.KrcstWkpRegMCalSetPK;

/**
 * The Class JpaWkpRegulaMonthActCalSetRepository.
 */
@Stateless
public class JpaWkpRegulaMonthActCalSetRepository extends JpaRepository implements WkpRegulaMonthActCalSetRepo {

	private static final String SELECT_BY_CID = "SELECT c FROM KrcmtCalcMSetRegWkp c"
			+ " WHERE c.krcstWkpRegMCalSetPK.cid = :cid";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.
	 * WkpRegulaMonthActCalSetRepository#add(nts.uk.ctx.at.record.dom.workrecord
	 * .monthcal.workplace.WkpRegulaMonthActCalSet)
	 */
	@Override
	public void add(WkpRegulaMonthActCalSet domain) {
		// Create new entity
		KrcmtCalcMSetRegWkp entity = new KrcmtCalcMSetRegWkp();

		// Transfer data
		entity.transfer(domain);
		entity.setKrcstWkpRegMCalSetPK(
				new KrcstWkpRegMCalSetPK(domain.getComId(), domain.getWorkplaceId()));

		// Insert into DB
		this.commandProxy().insert(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.
	 * WkpRegulaMonthActCalSetRepository#update(nts.uk.ctx.at.record.dom.
	 * workrecord.monthcal.workplace.WkpRegulaMonthActCalSet)
	 */
	@Override
	public void update(WkpRegulaMonthActCalSet domain) {
		// Get info
		KrcstWkpRegMCalSetPK pk = new KrcstWkpRegMCalSetPK(domain.getComId(),
				domain.getWorkplaceId());
		
		this.queryProxy().find(pk, KrcmtCalcMSetRegWkp.class).ifPresent(e -> {
			
			e.transfer(domain);
			
			this.commandProxy().update(e);
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.
	 * WkpRegulaMonthActCalSetRepository#find(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Optional<WkpRegulaMonthActCalSet> find(String cid, String wkpId) {
		// Get info
		KrcstWkpRegMCalSetPK pk = new KrcstWkpRegMCalSetPK(cid, wkpId);
		
		return this.queryProxy().find(pk, KrcmtCalcMSetRegWkp.class).map(c -> toDomain(c));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.
	 * WkpRegulaMonthActCalSetRepository#remove(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void remove(String cid, String wkpId) {
		Optional<KrcmtCalcMSetRegWkp> optEntity = this.queryProxy().find(new KrcstWkpRegMCalSetPK(cid, wkpId),
				KrcmtCalcMSetRegWkp.class);
		KrcmtCalcMSetRegWkp entity = optEntity.get();
		this.commandProxy().remove(entity);

	}

	private WkpRegulaMonthActCalSet toDomain (KrcmtCalcMSetRegWkp e) {
		
		return WkpRegulaMonthActCalSet.of(e.getKrcstWkpRegMCalSetPK().getWkpid(), 
				e.getKrcstWkpRegMCalSetPK().getCid(), 
				e.getAggregateTimeSet(), 
				e.getExcessOutsideTimeSet());
	}

	@Override
	public List<WkpRegulaMonthActCalSet> findWkpRegulaMonthAll(String cid) {
		List<KrcmtCalcMSetRegWkp> entitys = this.queryProxy().query(SELECT_BY_CID, KrcmtCalcMSetRegWkp.class)
				.setParameter("cid", cid).getList();
		
		return entitys.stream().map(m -> {
			return toDomain(m);
		}).collect(Collectors.toList());
	}

}
