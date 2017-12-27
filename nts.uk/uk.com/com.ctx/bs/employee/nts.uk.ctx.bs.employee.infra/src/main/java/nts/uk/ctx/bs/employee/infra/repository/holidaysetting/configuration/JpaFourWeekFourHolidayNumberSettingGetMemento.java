package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.configuration;

import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekDay;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingGetMemento;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekPublicHoliday;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.LastWeekHolidayNumberOfFourWeek;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.LastWeekHolidayNumberOfOneWeek;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.OneWeekPublicHoliday;
import nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.WeekNumberOfDay;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.configuration.KshmtFourweekfourHdNumbSet;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class JpaFourWeekFourHolidayNumberSettingGetMemento.
 */
public class JpaFourWeekFourHolidayNumberSettingGetMemento implements FourWeekFourHolidayNumberSettingGetMemento{
	
	/** The Constant TRUE_VALUE. */
	private final static int TRUE_VALUE = 1;
	
	/** The kshmt fourweekfour hd numb set. */
	private KshmtFourweekfourHdNumbSet kshmtFourweekfourHdNumbSet;
	
	/**
	 * Instantiates a new jpa four week four holiday number setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFourWeekFourHolidayNumberSettingGetMemento(KshmtFourweekfourHdNumbSet entity){
		this.kshmtFourweekfourHdNumbSet = entity;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingGetMemento#getIsOneWeekHoliday()
	 */
	@Override
	public boolean getIsOneWeekHoliday() {
		if(this.kshmtFourweekfourHdNumbSet.getIsOneWeekHd() == TRUE_VALUE){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingGetMemento#getOneWeek()
	 */
	@Override
	public OneWeekPublicHoliday getOneWeek() {
		LastWeekHolidayNumberOfOneWeek obj = new LastWeekHolidayNumberOfOneWeek(new WeekNumberOfDay(this.kshmtFourweekfourHdNumbSet.getInLegalHdLwhnoow().doubleValue()),
													new WeekNumberOfDay(this.kshmtFourweekfourHdNumbSet.getOutLegalHdLwhnoow().doubleValue()));
		return new OneWeekPublicHoliday(obj, 
										new WeekNumberOfDay(this.kshmtFourweekfourHdNumbSet.getInLegalHdOwph().doubleValue()),
										new WeekNumberOfDay(this.kshmtFourweekfourHdNumbSet.getOutLegalHdOwph().doubleValue()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingGetMemento#getIsFourWeekHoliday()
	 */
	@Override
	public boolean getIsFourWeekHoliday() {
		if(this.kshmtFourweekfourHdNumbSet.getIsFourWeekHd() == TRUE_VALUE){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingGetMemento#getFourWeek()
	 */
	@Override
	public FourWeekPublicHoliday getFourWeek() {
		LastWeekHolidayNumberOfFourWeek obj = new LastWeekHolidayNumberOfFourWeek(
														new FourWeekDay(this.kshmtFourweekfourHdNumbSet.getInLegalHdLwhnofw().doubleValue()),
														new FourWeekDay(this.kshmtFourweekfourHdNumbSet.getOutLegalHdLwhnofw().doubleValue()));
		return new FourWeekPublicHoliday(obj,
										 new FourWeekDay(this.kshmtFourweekfourHdNumbSet.getInLegelHdFwph().doubleValue()),
										 new FourWeekDay(this.kshmtFourweekfourHdNumbSet.getOutLegalHdFwph().doubleValue()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.configuration.FourWeekFourHolidayNumberSettingGetMemento#getCID()
	 */
	@Override
	public String getCID() {
		return AppContexts.user().companyId();
	}

}
