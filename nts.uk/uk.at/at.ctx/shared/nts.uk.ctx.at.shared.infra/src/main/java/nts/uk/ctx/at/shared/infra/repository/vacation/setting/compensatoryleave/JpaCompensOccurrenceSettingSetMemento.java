/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.compensatoryleave;

import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceDivision;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSettingSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KocmtOccurrenceSet;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KocmtOccurrenceSetPK;

/**
 * The Class JpaCompensOccurrenceSettingSetMemento.
 */
public class JpaCompensOccurrenceSettingSetMemento implements CompensatoryOccurrenceSettingSetMemento {
    
    /** The company id. */
    private String companyId;
    
    /** The entity. */
    private KocmtOccurrenceSet entity;
    
    /**
     * Instantiates a new jpa compens occurrence setting set memento.
     *
     * @param companyId the company id
     * @param entity the entity
     */
    public JpaCompensOccurrenceSettingSetMemento(String companyId, KocmtOccurrenceSet entity) {
        this.companyId = companyId;
        this.entity = entity;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryOccurrenceSettingSetMemento#setOccurrenceType(nts.uk.ctx.at.
     * shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryOccurrenceDivision)
     */
    @Override
    public void setOccurrenceType(CompensatoryOccurrenceDivision occurrenceType) {
        KocmtOccurrenceSetPK pk = new KocmtOccurrenceSetPK();
        pk.setCid(this.companyId);
        pk.setOccurrType(occurrenceType.value);
        this.entity.setKocmtOccurrenceSetPK(pk);
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensatoryOccurrenceSettingSetMemento#setTransferSetting(nts.uk.ctx.at.
     * shared.dom.vacation.setting.compensatoryleave.TransferSetting)
     */
    @Override
    public void setTransferSetting(SubHolTransferSet transferSetting) {
        JpaTransferSettingSetMemento memento = new JpaTransferSettingSetMemento(this.entity);
        transferSetting.saveToMemento(memento);
    }

}
