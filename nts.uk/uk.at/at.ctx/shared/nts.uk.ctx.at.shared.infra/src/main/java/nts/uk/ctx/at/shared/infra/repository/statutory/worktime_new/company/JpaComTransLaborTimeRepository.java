/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.statutory.worktime_new.company;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.common.TimeOfDay;
import nts.uk.ctx.at.shared.dom.common.WeeklyTime;
import nts.uk.ctx.at.shared.dom.statutory.worktime.week.DailyUnit;
import nts.uk.ctx.at.shared.dom.statutory.worktime.week.WeeklyUnit;
import nts.uk.ctx.at.shared.dom.statutory.worktime.week.defor.DeforLaborTimeCom;
import nts.uk.ctx.at.shared.dom.statutory.worktime.week.defor.DeforLaborTimeComRepo;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.company.KshstComTransLabTime;

/**
 * The Class JpaComTransLaborTimeRepository.
 */
@Stateless
public class JpaComTransLaborTimeRepository extends JpaRepository
		implements DeforLaborTimeComRepo {

	/*
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.
	 * ComTransLaborTimeRepository#create(nts.uk.ctx.at.shared.dom.statutory.
	 * worktime.companyNew.ComTransLaborTime)
	 */
	@Override
	public void create(DeforLaborTimeCom setting) {
		KshstComTransLabTime entity = new KshstComTransLabTime();
		
		entity.setDailyTime(setting.getDailyTime().getDailyTime().v());
		entity.setWeeklyTime(setting.getWeeklyTime().getTime().v());
		entity.setCid(setting.getComId());
		
		this.commandProxy().insert(entity);
	}

	/*
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.
	 * ComTransLaborTimeRepository#update(nts.uk.ctx.at.shared.dom.statutory.
	 * worktime.companyNew.ComTransLaborTime)
	 */
	@Override
	public void update(DeforLaborTimeCom setting) {
		KshstComTransLabTime entity = this.queryProxy().find(setting.getComId(), KshstComTransLabTime.class).get();

		entity.setDailyTime(setting.getDailyTime().getDailyTime().v());
		entity.setWeeklyTime(setting.getWeeklyTime().getTime().v());
		
		this.commandProxy().update(entity);
	}

	/*
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.
	 * ComTransLaborTimeRepository#remove(java.lang.String)
	 */
	@Override
	public void remove(String companyId) {
		this.commandProxy().remove(KshstComTransLabTime.class, companyId);
	}

	/*
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.
	 * ComTransLaborTimeRepository#find(java.lang.String)
	 */
	@Override
	public Optional<DeforLaborTimeCom> find(String companyId) {

		Optional<KshstComTransLabTime> optEntity = this.queryProxy().find(companyId,
				KshstComTransLabTime.class);

		// Check exist
		if (!optEntity.isPresent()) {
			return Optional.empty();
		}

		return Optional.ofNullable(this.toDomain(optEntity.get()));
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entities
	 * @return the com trans labor time
	 */
	private DeforLaborTimeCom toDomain(KshstComTransLabTime entity) {
		return DeforLaborTimeCom.of(entity.getCid(),
				new WeeklyUnit(new WeeklyTime(entity.getWeeklyTime())), 
				new DailyUnit(new TimeOfDay(entity.getDailyTime())));
	}
}
