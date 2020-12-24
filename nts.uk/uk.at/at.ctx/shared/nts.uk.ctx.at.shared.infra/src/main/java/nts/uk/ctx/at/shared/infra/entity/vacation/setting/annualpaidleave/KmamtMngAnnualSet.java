/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.annualpaidleave;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KmamtMngAnnualSet.
 */
@Setter
@Getter
@Entity
@Table(name = "KMAMT_MNG_ANNUAL_SET")
public class KmamtMngAnnualSet extends UkJpaEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The cid. */
    @Id
    @Basic(optional = false)
    @Column(name = "CID")
    private String cid;

//    /** The half max grant day. */
//    @Column(name = "HALF_MAX_GRANT_DAY")
//    private Double halfMaxGrantDay;
//
//    /** The half max day year. */
//    @Basic(optional = false)
//    @Column(name = "HALF_MAX_DAY_YEAR")
//    private Integer halfMaxDayYear;

    /** The half manage atr. */
    @Column(name = "HALF_MANAGE_ATR")
    private Integer halfManageAtr;

    /** The half max reference. */
    @Column(name = "HALF_MAX_REFERENCE")
    private Integer halfMaxReference;

    /** The half max uniform comp. */
    @Column(name = "HALF_MAX_UNIFORM_COMP")
    private Integer halfMaxUniformComp;

    /** The is work day cal. */
    @Column(name = "IS_WORK_DAY_CAL")
    private Integer isWorkDayCal;

    /** The retention year. */
    @Column(name = "RETENTION_YEAR")
    private Integer retentionYear;

    /** The retention year. */
    @Column(name = "HALF_ROUND_PROC")
    private Integer halfRoundProc;

    /** The retention year. */
    @Column(name = "SCHEDULD_WORKING_DAYS")
    private Double scheduleWorkingDays;



//    /** The remaining max day. */
//    @Column(name = "REMAINING_MAX_DAY")
//    private Double remainingMaxDay;

//    /** The next grant day disp atr. */
//    @Column(name = "NEXT_GRANT_DAY_DISP_ATR")
//    private Integer nextGrantDayDispAtr;

//    /** The remaining num disp atr. */
//    @Column(name = "REMAINING_NUM_DISP_ATR")
//    private Integer remainingNumDispAtr;

//    /** The yearly of day. */
//    @Column(name = "YEARLY_OF_DAYS")
//    private Double yearlyOfDays;

//    /** The Round Processing Classification. */
//    @Column(name = "ROUND_PRO_CLA")
//    private Integer roundProcessCla;

    /**
     * Instantiates a new kmamt mng annual set.
     */
    public KmamtMngAnnualSet() {
    }

    /**
     * Instantiates a new kmamt mng annual set.
     *
     * @param cid the cid
     */
    public KmamtMngAnnualSet(String cid) {
        this.cid = cid;
    }

    /**
     * Instantiates a new kmamt mng annual set.
     *
     * @param cid the cid
     * @param halfMaxDayYear the half max day year
     */
    public KmamtMngAnnualSet(String cid, Integer halfMaxDayYear) {
        this.cid = cid;
        this.halfMaxDayYear = halfMaxDayYear;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KmamtMngAnnualSet)) {
            return false;
        }
        KmamtMngAnnualSet other = (KmamtMngAnnualSet) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
     */
    @Override
    protected Object getKey() {
        return this.cid;
    }
}
