/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KclmtAcquisitionEmp.
 */
@Setter
@Getter
@Entity
@Table(name = "KCLMT_ACQUISITION_EMP")
public class KclmtAcquisitionEmp extends UkJpaEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The kclmt acquisition emp PK. */
    @EmbeddedId
    private KclmtAcquisitionEmpPK kclmtAcquisitionEmpPK;

    /** The exp time. */
    @Basic(optional = false)
    @Column(name = "EXP_TIME")
    private Integer expTime;

    /** The preemp permit atr. */
    @Basic(optional = false)
    @Column(name = "PREEMP_PERMIT_ATR")
    private Integer preempPermitAtr;
    
    /** The deadl check month. */
    @Basic(optional = false)
    @Column(name = "DEADL_CHECK_MONTH")
    private Integer deadlCheckMonth;

    public static final JpaEntityMapper<KclmtAcquisitionEmp> MAPPER =
    		new JpaEntityMapper<>(KclmtAcquisitionEmp.class);
    
    /**
     * Instantiates a new kclmt acquisition emp.
     */
    public KclmtAcquisitionEmp() {
    }

    
    /**
     * Instantiates a new kclmt acquisition emp.
     *
     * @param kclmtAcquisitionEmpPK the kclmt acquisition emp PK
     */
    public KclmtAcquisitionEmp(KclmtAcquisitionEmpPK kclmtAcquisitionEmpPK) {
        this.kclmtAcquisitionEmpPK = kclmtAcquisitionEmpPK;
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kclmtAcquisitionEmpPK != null ? kclmtAcquisitionEmpPK.hashCode() : 0);
        return hash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KclmtAcquisitionEmp)) {
            return false;
        }
        KclmtAcquisitionEmp other = (KclmtAcquisitionEmp) object;
        if ((this.kclmtAcquisitionEmpPK == null && other.kclmtAcquisitionEmpPK != null)
                || (this.kclmtAcquisitionEmpPK != null && !this.kclmtAcquisitionEmpPK.equals(
                        other.kclmtAcquisitionEmpPK))) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
     */
    @Override
    protected Object getKey() {
        return this.kclmtAcquisitionEmpPK;
    }
}
