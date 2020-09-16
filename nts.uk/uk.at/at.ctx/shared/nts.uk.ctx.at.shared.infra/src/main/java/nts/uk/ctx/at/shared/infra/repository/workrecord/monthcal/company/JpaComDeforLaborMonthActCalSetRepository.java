/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workrecord.monthcal.company;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.workrecord.monthcal.calcmethod.other.com.ComDeforLaborMonthActCalSet;
import nts.uk.ctx.at.shared.dom.workrecord.monthcal.calcmethod.other.com.ComDeforLaborMonthActCalSetRepo;
import nts.uk.ctx.at.shared.infra.entity.workrecord.monthcal.company.KrcstComDeforMCalSet;

/**
 * The Class JpaComDeforLaborMonthActCalSetRepository.
 */
@Stateless
public class JpaComDeforLaborMonthActCalSetRepository extends JpaRepository
		implements ComDeforLaborMonthActCalSetRepo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.company.
	 * ComDeforLaborMonthActCalSetRepository#find(java.lang.String)
	 */
	@Override
	public Optional<ComDeforLaborMonthActCalSet> find(String companyId) {
		// Get info
		return this.queryProxy().find(companyId, KrcstComDeforMCalSet.class).map(c -> toDomain(c));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.company.
	 * ComDeforLaborMonthActCalSetRepository#add(nts.uk.ctx.at.record.dom.
	 * workrecord.monthcal.company.ComDeforLaborMonthActCalSet)
	 */
	@Override
	public void add(ComDeforLaborMonthActCalSet domain) {
		// Create new entity
		KrcstComDeforMCalSet entity = new KrcstComDeforMCalSet();

		// Transfer data
		entity.transfer(domain);
		entity.setCid(domain.getComId());

		// Insert into DB
		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.company.
	 * ComDeforLaborMonthActCalSetRepository#update(nts.uk.ctx.at.record.dom.
	 * workrecord.monthcal.company.ComDeforLaborMonthActCalSet)
	 */
	@Override
	public void update(ComDeforLaborMonthActCalSet domain) {
		// Get info
		this.queryProxy().find(domain.getComId(), KrcstComDeforMCalSet.class).ifPresent(e -> {
			
			e.transfer(domain);
			
			this.commandProxy().update(e);
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.workrecord.monthcal.company.
	 * ComDeforLaborMonthActCalSetRepository#remove(java.lang.String)
	 */
	@Override
	public void remove(String companyId) {
		this.commandProxy().remove(KrcstComDeforMCalSet.class, companyId);
	}

	private ComDeforLaborMonthActCalSet toDomain (KrcstComDeforMCalSet e) {
		
		return ComDeforLaborMonthActCalSet.of(
				e.getCid(), 
				e.getAggregateTimeSet(), e.getExcessOutsideTimeSet(),
				e.deforLaborCalSetting(),
				e.deforLaborSettlementPeriod());
	}

}
