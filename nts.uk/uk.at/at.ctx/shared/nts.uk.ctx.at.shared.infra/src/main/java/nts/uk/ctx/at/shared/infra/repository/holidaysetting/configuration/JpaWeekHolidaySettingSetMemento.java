package nts.uk.ctx.at.shared.infra.repository.holidaysetting.configuration;

import java.math.BigDecimal;

import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.WeekHolidaySettingSetMemento;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.WeekNumberOfDay;
import nts.uk.ctx.at.shared.infra.entity.holidaysetting.configuration.KshmtWeekHdSet;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class JpaWeekHolidaySettingSetMemento.
 */
public class JpaWeekHolidaySettingSetMemento implements WeekHolidaySettingSetMemento{

	/** The kshmt week hd set. */
	private KshmtWeekHdSet kshmtWeekHdSet;
	
	/**
	 * Instantiates a new jpa week holiday setting set memento.
	 *
	 * @param entity the entity
	 */
	public JpaWeekHolidaySettingSetMemento(KshmtWeekHdSet entity){
		if(entity.getCid() == null){
			entity.setCid(AppContexts.user().companyId());
		}
		this.kshmtWeekHdSet = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.WeekHolidaySettingSetMemento#setCID(java.lang.String)
	 */
	@Override
	public void setCID(String CID) {
		// do not code here
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.WeekHolidaySettingSetMemento#setInLegalHoliday(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.WeekNumberOfDay)
	 */
	@Override
	public void setInLegalHoliday(WeekNumberOfDay inLegalHoliday) {
		this.kshmtWeekHdSet.setInLegalHd(new BigDecimal(inLegalHoliday.v()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.WeekHolidaySettingSetMemento#setOutLegalHoliday(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.WeekNumberOfDay)
	 */
	@Override
	public void setOutLegalHoliday(WeekNumberOfDay outLegalHoliday) {
		this.kshmtWeekHdSet.setOutLegalHd(new BigDecimal(outLegalHoliday.v()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.WeekHolidaySettingSetMemento#setStartDay(nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.DayOfWeek)
	 */
	@Override
	public void setStartDay(DayOfWeek StartDay) {
		this.kshmtWeekHdSet.setStartDay(StartDay.value);
	}
}
