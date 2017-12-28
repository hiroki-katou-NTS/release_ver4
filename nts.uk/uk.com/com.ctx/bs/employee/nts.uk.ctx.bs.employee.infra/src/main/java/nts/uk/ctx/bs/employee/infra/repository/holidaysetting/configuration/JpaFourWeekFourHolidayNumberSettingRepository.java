package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.configuration;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingRepository;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.configuration.KshmtFourweekfourHdNumbSet;

/**
 * The Class JpaFourWeekFourHolidayNumberSettingRepository.
 */
@Stateless
public class JpaFourWeekFourHolidayNumberSettingRepository extends JpaRepository implements FourWeekFourHolidayNumberSettingRepository {

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingRepository#findByCID(java.lang.String)
	 */
	@Override
	public Optional<FourWeekFourHolidayNumberSetting> findByCID(String companyId) {
		return this.queryProxy().find(companyId, KshmtFourweekfourHdNumbSet.class).map(e -> this.toDomain(e));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingRepository#update(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSetting)
	 */
	@Override
	public void update(FourWeekFourHolidayNumberSetting domain) {
		this.commandProxy().update(this.toEntity(domain));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingRepository#add(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSetting)
	 */
	@Override
	public void add(FourWeekFourHolidayNumberSetting domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}
	
	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kshmt fourweekfour hd numb set
	 */
	private KshmtFourweekfourHdNumbSet toEntity(FourWeekFourHolidayNumberSetting domain){
		KshmtFourweekfourHdNumbSet entity = new KshmtFourweekfourHdNumbSet();
		domain.saveToMemento(new JpaFourWeekFourHolidayNumberSettingSetMemento(entity));
		return entity;
	}
	
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the four week four holiday number setting
	 */
	private FourWeekFourHolidayNumberSetting toDomain(KshmtFourweekfourHdNumbSet entity){
		FourWeekFourHolidayNumberSetting domain = 
				new FourWeekFourHolidayNumberSetting(new JpaFourWeekFourHolidayNumberSettingGetMemento(entity));
		return domain;
	}
}
