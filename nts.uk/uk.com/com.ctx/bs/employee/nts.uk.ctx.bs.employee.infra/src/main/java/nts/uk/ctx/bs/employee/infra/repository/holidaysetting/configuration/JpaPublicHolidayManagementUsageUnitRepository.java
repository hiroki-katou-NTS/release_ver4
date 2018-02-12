/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.configuration;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.PublicHolidayManagementUsageUnit;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.PublicHolidayManagementUsageUnitRepository;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.configuration.KshmtPubHdMngUnitSet;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class JpaPublicHolidayManagementUsageUnitRepository.
 */
@Stateless
public class JpaPublicHolidayManagementUsageUnitRepository extends JpaRepository implements PublicHolidayManagementUsageUnitRepository{

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.PublicHolidayManagementUsageUnitRepository#findByCID(java.lang.String)
	 */
	@Override
	public Optional<PublicHolidayManagementUsageUnit> findByCID(String companyId) {
		return this.queryProxy().find(companyId, KshmtPubHdMngUnitSet.class).map(e -> this.toDomain(e));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.PublicHolidayManagementUsageUnitRepository#update(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.PublicHolidayManagementUsageUnit)
	 */
	@Override
	public void update(PublicHolidayManagementUsageUnit domain) {
		this.commandProxy().update(this.toEntity(domain));
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.PublicHolidayManagementUsageUnitRepository#add(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.PublicHolidayManagementUsageUnit)
	 */
	@Override
	public void add(PublicHolidayManagementUsageUnit domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}
	
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the public holiday management usage unit
	 */
	private PublicHolidayManagementUsageUnit toDomain(KshmtPubHdMngUnitSet entity){
		PublicHolidayManagementUsageUnit domain = new PublicHolidayManagementUsageUnit(
															entity.getIsManageSPubHd(),
															entity.getIsManageWkpPubHd(),
															entity.getIsManageEmpPubHd()
														);
		
		return domain;
	}
	
	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kshmt pub hd mng unit set
	 */
	private KshmtPubHdMngUnitSet toEntity(PublicHolidayManagementUsageUnit domain){
		KshmtPubHdMngUnitSet e = new KshmtPubHdMngUnitSet();
		e.setIsManageEmpPubHd(domain.getIsManageEmpPublicHd());
		e.setIsManageSPubHd(domain.getIsManageEmployeePublicHd());
		e.setIsManageWkpPubHd(domain.getIsManageWkpPublicHd());
		e.setCid(AppContexts.user().companyId());
		return e;
	}
}
