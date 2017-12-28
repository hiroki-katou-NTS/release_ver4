package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.configuration;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.ForwardSettingOfPublicHoliday;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.ForwardSettingOfPublicHolidayRepository;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.configuration.KshmtForwardSetOfPublicHd;

/**
 * The Class JpaForwardSettingOfPublicHolidayRepository.
 */
@Stateless
public class JpaForwardSettingOfPublicHolidayRepository extends JpaRepository implements ForwardSettingOfPublicHolidayRepository {

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.ForwardSettingOfPublicHolidayRepository#findByCID(java.lang.String)
	 */
	@Override
	public Optional<ForwardSettingOfPublicHoliday> findByCID(String companyId) {
		return this.queryProxy().find(companyId, KshmtForwardSetOfPublicHd.class).map(e -> this.toDomain(e));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.ForwardSettingOfPublicHolidayRepository#update(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.ForwardSettingOfPublicHoliday)
	 */
	@Override
	public void update(ForwardSettingOfPublicHoliday domain) {
		this.commandProxy().update(this.toEntity(domain));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.ForwardSettingOfPublicHolidayRepository#add(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.ForwardSettingOfPublicHoliday)
	 */
	@Override
	public void add(ForwardSettingOfPublicHoliday domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}
	
	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kshmt forward set of public hd
	 */
	private KshmtForwardSetOfPublicHd toEntity(ForwardSettingOfPublicHoliday domain){
		KshmtForwardSetOfPublicHd entity = new KshmtForwardSetOfPublicHd();
		domain.saveToMemento(new JpaForwardSettingOfPublicHolidaySetMemento(entity));
		return entity;
	}
	
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the forward setting of public holiday
	 */
	private ForwardSettingOfPublicHoliday toDomain(KshmtForwardSetOfPublicHd entity){
		ForwardSettingOfPublicHoliday domain = new ForwardSettingOfPublicHoliday(
														new JpaForwardSettingOfPublicHolidayGetMemento(entity));
		
		return domain;
	}
}
