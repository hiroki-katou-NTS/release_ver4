/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionavgearn.limit;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.limit.HealthAvgEarnLimit;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.PensionAvgEarnLimit;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.PensionAvgEarnLimitRepository;

/**
 * The Class JpaPensionAvgEarnLimitRepository.
 */
@Stateless
public class JpaPensionAvgEarnLimitRepository extends JpaRepository
		implements PensionAvgEarnLimitRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit.
	 * PensionAvgEarnLimitRepository#findAll(java.lang.String)
	 */
	@Override
	public List<PensionAvgEarnLimit> findAll(String companyCode) {
		List<PensionAvgEarnLimit> listPensionAvgEarnLimit = new ArrayList<>();
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(1, 88000L, 93000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(2, 98000L, 101000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(3, 104000L, 107000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(4, 110000L, 114000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(5, 118000L, 122000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(6, 126000L, 130000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(7, 134000L, 138000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(8, 142000L, 146000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(9, 150000L, 155000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(10, 160000L, 165000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(11, 170000L, 175000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(12, 180000L, 185000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(13, 190000L, 195000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(14, 200000L, 210000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(15, 220000L, 230000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(16, 240000L, 250000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(17, 260000L, 270000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(18, 280000L, 290000L));
		listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(19, 300000L, 310000L));
		
		int grade = 20;
        long avgEarn = 300000L;
        long salLimit = 310000L;
        
        for (int i=0; i< 20; i++) {
            grade++;
            avgEarn = salLimit + 10000;
            salLimit = avgEarn + 10000;
            listPensionAvgEarnLimit.add(new PensionAvgEarnLimit(grade, avgEarn, salLimit));
        }
		
		return listPensionAvgEarnLimit;
	}

}
