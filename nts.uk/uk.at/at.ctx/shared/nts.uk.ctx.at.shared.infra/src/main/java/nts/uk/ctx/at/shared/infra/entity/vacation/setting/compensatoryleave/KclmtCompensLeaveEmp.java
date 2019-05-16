/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KclmtCompensLeaveEmp.
 */
@Setter
@Getter
@Entity
@Table(name = "KCLMT_COMPENS_LEAVE_EMP")
public class KclmtCompensLeaveEmp extends UkJpaEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The kclmt compens leave emp PK. */
    @EmbeddedId
    protected KclmtCompensLeaveEmpPK kclmtCompensLeaveEmpPK;
    
    /** The manage atr. */
    @Basic(optional = false)
    @Column(name = "MANAGE_ATR")
    private Integer manageAtr;
    
    /** The manage atr. */
    @Basic(optional = false)
    @Column(name = "DEADL_CHECK_MONTH")
    private Integer deadlCheckMonth;
    
    /** The kclmt acquisition emp. */
    @JoinColumns({@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
            @JoinColumn(name = "EMPCD", referencedColumnName = "EMPCD", insertable = false, updatable = false)})
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
    private KclmtAcquisitionEmp kclmtAcquisitionEmp;
    
    /** The kctmt digest time emp. */
    @JoinColumns({@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
        @JoinColumn(name = "EMPCD", referencedColumnName = "EMPCD", insertable = false, updatable = false)})
    @OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private KctmtDigestTimeEmp kctmtDigestTimeEmp;
    
    public static final JpaEntityMapper<KclmtCompensLeaveEmp> MAPPER =
    		new JpaEntityMapper<>(KclmtCompensLeaveEmp.class);
    /**
     * Instantiates a new kclmt compens leave emp.
     */
    public KclmtCompensLeaveEmp() {
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kclmtCompensLeaveEmpPK != null ? kclmtCompensLeaveEmpPK.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        // not set
        if (!(object instanceof KclmtCompensLeaveEmp)) {
            return false;
        }
        KclmtCompensLeaveEmp other = (KclmtCompensLeaveEmp) object;
        if ((this.kclmtCompensLeaveEmpPK == null && other.kclmtCompensLeaveEmpPK != null)
                || (this.kclmtCompensLeaveEmpPK != null
                        && !this.kclmtCompensLeaveEmpPK.equals(other.kclmtCompensLeaveEmpPK))) {
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
        return this.kclmtCompensLeaveEmpPK;
    }
}
