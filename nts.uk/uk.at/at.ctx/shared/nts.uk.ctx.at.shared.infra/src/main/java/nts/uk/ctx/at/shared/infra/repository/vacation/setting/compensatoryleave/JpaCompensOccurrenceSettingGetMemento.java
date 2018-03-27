/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.compensatoryleave;

import javax.ejb.Stateless;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceDivision;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSettingGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KocmtOccurrenceSet;

/**
 * The Class JpaCompensOccurrenceSettingGetMemento.
 */
@Stateless
@NoArgsConstructor
public class JpaCompensOccurrenceSettingGetMemento implements CompensatoryOccurrenceSettingGetMemento {
    
    /** The entity. */
    private KocmtOccurrenceSet entity;
    
    /**
     * Instantiates a new jpa compens occurrence setting get memento.
     *
     * @param entity the entity
     */
    public JpaCompensOccurrenceSettingGetMemento(KocmtOccurrenceSet entity) {
        this.entity = entity;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryOccurrenceSettingGetMemento#getOccurrenceType()
     */
    @Override
    public CompensatoryOccurrenceDivision getOccurrenceType() {
        return CompensatoryOccurrenceDivision.valueOf(this.entity.getKocmtOccurrenceSetPK().getOccurrType());
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryOccurrenceSettingGetMemento#getTransferSetting()
     */
    @Override
    public SubHolTransferSet getTransferSetting() {
        return new SubHolTransferSet(new JpaTransferSettingGetMemento(this.entity));
    }

}
