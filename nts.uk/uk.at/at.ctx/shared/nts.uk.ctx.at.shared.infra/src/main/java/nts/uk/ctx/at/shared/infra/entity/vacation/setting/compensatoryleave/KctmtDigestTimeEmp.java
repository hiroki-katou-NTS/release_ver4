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
 * The Class KctmtDigestTimeEmp.
 */
@Setter
@Getter
@Entity
@Table(name = "KCTMT_DIGEST_TIME_EMP")
public class KctmtDigestTimeEmp extends UkJpaEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The kctmt digest time emp PK. */
    @EmbeddedId
    private KctmtDigestTimeEmpPK kctmtDigestTimeEmpPK;

    /** The manage atr. */
    @Basic(optional = false)
    @Column(name = "MANAGE_ATR")
    private Integer manageAtr;

    /** The digestive unit. */
    @Basic(optional = false)
    @Column(name = "DIGESTIVE_UNIT")
    private Integer digestiveUnit;

    public static final JpaEntityMapper<KctmtDigestTimeEmp> MAPPER =
    		new JpaEntityMapper<>(KctmtDigestTimeEmp.class);
    
    /**
     * Instantiates a new kctmt digest time emp.
     */
    public KctmtDigestTimeEmp() {
    }
    
    /**
     * Instantiates a new kctmt digest time emp.
     *
     * @param kctmtDigestTimeEmpPK the kctmt digest time emp PK
     */
    public KctmtDigestTimeEmp(KctmtDigestTimeEmpPK kctmtDigestTimeEmpPK) {
        this.kctmtDigestTimeEmpPK = kctmtDigestTimeEmpPK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kctmtDigestTimeEmpPK != null ? kctmtDigestTimeEmpPK.hashCode() : 0);
        return hash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KctmtDigestTimeEmp)) {
            return false;
        }
        KctmtDigestTimeEmp other = (KctmtDigestTimeEmp) object;
        if ((this.kctmtDigestTimeEmpPK == null && other.kctmtDigestTimeEmpPK != null) 
                || (this.kctmtDigestTimeEmpPK != null && !this.kctmtDigestTimeEmpPK.equals(
                        other.kctmtDigestTimeEmpPK))) {
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
        return this.kctmtDigestTimeEmpPK;
    }

}
