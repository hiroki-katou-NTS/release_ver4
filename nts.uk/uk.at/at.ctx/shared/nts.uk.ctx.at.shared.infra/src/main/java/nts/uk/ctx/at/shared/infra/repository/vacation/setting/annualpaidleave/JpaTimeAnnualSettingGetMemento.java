/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.annualpaidleave;

import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.TimeDigestiveUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.MaxDayReference;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.MaxTimeDay;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.TimeAnnualSettingGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.YearVacationTimeMaxDay;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.annualpaidleave.KtvmtTimeAnnualSet;

/**
 * The Class JpaTimeVacationSettingGetMemento.
 */
public class JpaTimeAnnualSettingGetMemento implements TimeAnnualSettingGetMemento {
    
    /** The entity. */
    private KtvmtTimeAnnualSet entity;
    
    /**
     * Instantiates a new jpa time vacation setting get memento.
     *
     * @param entity the entity
     */
    public JpaTimeAnnualSettingGetMemento(KtvmtTimeAnnualSet entity) {
        this.entity = entity;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.
     * TimeVacationSettingGetMemento#getCompanyId()
     */
    @Override
    public String getCompanyId() {
        return this.entity.getCid();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.
     * TimeVacationSettingGetMemento#getTimeManageType()
     */
    @Override
    public ManageDistinct getTimeManageType() {
        return ManageDistinct.valueOf(this.entity.getTimeManageAtr());
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.
     * TimeVacationSettingGetMemento#getTimeUnit()
     */
    @Override
    public TimeDigestiveUnit getTimeUnit() {
        return TimeDigestiveUnit.valueOf(this.entity.getTimeUnit());
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.
     * TimeVacationSettingGetMemento#getMaxYearDayLeave()
     */
    @Override
    public YearVacationTimeMaxDay getMaxYearDayLeave() {
        YearVacationTimeMaxDay timeMaxDay = YearVacationTimeMaxDay.builder()
                .manageType(ManageDistinct.valueOf(this.entity.getTimeMaxDayManageAtr()))
                .reference(MaxDayReference.valueOf(this.entity.getTimeMaxDayReference()))
                .maxNumberUniformCompany(new MaxTimeDay(this.entity.getTimeMaxDayUnifComp()))
                .build();
        return timeMaxDay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.
     * TimeVacationSettingGetMemento#isEnoughTimeOneDay()
     */
    @Override
    public boolean isEnoughTimeOneDay() {
        return this.entity.getIsEnoughTimeOneDay() == 1 ? true : false;
    }

}
