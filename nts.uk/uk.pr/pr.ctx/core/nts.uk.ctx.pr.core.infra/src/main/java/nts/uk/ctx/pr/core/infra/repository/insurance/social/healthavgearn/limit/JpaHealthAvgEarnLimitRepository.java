/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthavgearn.limit;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimit;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimitRepository;

/**
 * The Class JpaHealthAvgEarnLimitRepository.
 */
@Stateless
public class JpaHealthAvgEarnLimitRepository extends JpaRepository
		implements HealthAvgEarnLimitRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.avgearn.
	 * HealthAvgEarnLimitRepository#findAll(java.lang.String)
	 */
	@Override
	public List<HealthAvgEarnLimit> findAll(String companyCode) {
		List<HealthAvgEarnLimit> listHealthAvgEarnLimit = new ArrayList<HealthAvgEarnLimit>();
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(1, 58000L, 63000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(2, 68000L, 73000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(3, 78000L, 83000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(4, 88000L, 93000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(5, 98000L, 101000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(6, 104000L, 107000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(7, 110000L, 114000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(8, 118000L, 122000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(9, 126000L, 130000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(10, 134000L, 138000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(11, 142000L, 146000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(12, 150000L, 155000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(13, 160000L, 165000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(14, 170000L, 175000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(15, 180000L, 185000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(16, 190000L, 195000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(17, 200000L, 210000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(18, 220000L, 230000L));
		listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(19, 240000L, 250000L));
		
		int grade = 20;
		long avgEarn = 240000L;
		long salLimit = 250000L;
		
		for (int i=0; i< 20; i++) {
		    grade++;
		    avgEarn = salLimit + 10000;
            salLimit = avgEarn + 10000;
		    listHealthAvgEarnLimit.add(new HealthAvgEarnLimit(grade, avgEarn, salLimit));
		}
		
		return listHealthAvgEarnLimit;
	}

}
