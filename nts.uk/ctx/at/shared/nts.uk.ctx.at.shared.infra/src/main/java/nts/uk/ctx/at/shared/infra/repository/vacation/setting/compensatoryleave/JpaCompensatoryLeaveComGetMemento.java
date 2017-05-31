/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.compensatoryleave;

import java.util.List;

import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.NormalVacationSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.OccurrenceVacationSetting;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KmfmtCompensLeaveCom;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KmfmtNormalVacationSet;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KmfmtOccurVacationSet;

/**
 * The Class JpaCompensatoryLeaveComGetMemento.
 */
public class JpaCompensatoryLeaveComGetMemento implements CompensatoryLeaveComGetMemento {

    /** The entity. */
    private KmfmtCompensLeaveCom entity;

    /** The entity normal. */
    private KmfmtNormalVacationSet entityNormal;
    
    /** The entity occur. */
    private List<KmfmtOccurVacationSet> lstEntityOccur;

    /**
     * Instantiates a new jpa compensatory leave com get memento.
     *
     * @param entity the entity
     * @param entityNormal the entity normal
     * @param entityOccur the entity occur
     */
    public JpaCompensatoryLeaveComGetMemento(KmfmtCompensLeaveCom entity, KmfmtNormalVacationSet entityNormal,
    		List<KmfmtOccurVacationSet> lstEntityOccur) {
        this.entity = entity;
        this.entityNormal = entityNormal;
        this.lstEntityOccur = lstEntityOccur;
    }

    /**
     * Gets the company id.
     *
     * @return the company id
     */
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryLeaveComGetMemento#getCompanyId()
     */
    @Override
    public String getCompanyId() {
        return this.entity.getCid();
    }

    /**
     * Gets the checks if is managed.
     *
     * @return the checks if is managed
     */
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryLeaveComGetMemento#getIsManaged()
     */
    @Override
    public ManageDistinct getIsManaged() {
        return ManageDistinct.valueOf(this.entity.getManageAtr());
    }

    /**
     * Gets the normal vacation setting.
     *
     * @return the normal vacation setting
     */
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryLeaveComGetMemento#getNormalVacationSetting()
     */
    @Override
    public NormalVacationSetting getNormalVacationSetting() {
        return new NormalVacationSetting(new JpaNormalVacationGetMemento(this.entityNormal));
    }

    /**
     * Gets the occurrence vacation setting.
     *
     * @return the occurrence vacation setting
     */
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryLeaveComGetMemento#getOccurrenceVacationSetting()
     */
    @Override
    public OccurrenceVacationSetting getOccurrenceVacationSetting() {
        return new OccurrenceVacationSetting(new JpaOccurrenceVacationGetMemento(this.lstEntityOccur));
    }
}
