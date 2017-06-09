/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.nursingleave;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KmfmtNursingLeaveSet.
 */
@Setter
@Getter
@Entity
@Table(name = "KNLMT_NURSING_LEAVE_SET")
public class KnlmtNursingLeaveSet extends UkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The knlmtNursingLeaveSetPK. */
    @EmbeddedId
    private KnlmtNursingLeaveSetPK knlmtNursingLeaveSetPK;
    
    /** The manage type. */
    @Basic(optional = false)
    @Column(name = "MANAGE_ATR")
    private Integer manageType;
    
    /** The start md. */
    @Column(name = "STR_MD")
    private Integer startMonthDay;
    
    /** The nursing num leave day. */
    @Column(name = "NURSING_NUM_LEAVE_DAY")
    private Integer nursingNumLeaveDay;
    
    /** The nursing num person. */
    @Column(name = "NURSING_NUM_PERSON")
    private Integer nursingNumPerson;
    
    /** The list work type. */
    @JoinColumns({@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = false),
            @JoinColumn(name = "NURSING_TYPE", referencedColumnName = "NURSING_TYPE", insertable = true, updatable = false)})
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<KnlmtNursingWorkType> listWorkType;

    /**
     * Instantiates a new kmfmt nursing leave set.
     */
    public KnlmtNursingLeaveSet() {
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (knlmtNursingLeaveSetPK != null ? knlmtNursingLeaveSetPK.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KnlmtNursingLeaveSet)) {
            return false;
        }
        KnlmtNursingLeaveSet other = (KnlmtNursingLeaveSet) object;
        if ((this.knlmtNursingLeaveSetPK == null && other.knlmtNursingLeaveSetPK != null)
                || (this.knlmtNursingLeaveSetPK != null && !this.knlmtNursingLeaveSetPK.equals(
                        other.knlmtNursingLeaveSetPK))) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
     */
    @Override
    protected Object getKey() {
        return this.knlmtNursingLeaveSetPK;
    }
}
