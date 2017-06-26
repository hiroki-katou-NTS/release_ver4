/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class CmnmtCalled.
 */
@Setter
@Getter
@Entity
@Table(name = "CMNMT_CALLED")
public class CmnmtCalled implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The ins date. */
    @Column(name = "INS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insDate;
    
    /** The ins ccd. */
    @Column(name = "INS_CCD")
    private String insCcd;
    
    /** The ins scd. */
    @Column(name = "INS_SCD")
    private String insScd;
    
    /** The ins pg. */
    @Column(name = "INS_PG")
    private String insPg;
    
    /** The upd date. */
    @Column(name = "UPD_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updDate;
    
    /** The upd ccd. */
    @Column(name = "UPD_CCD")
    private String updCcd;
    
    /** The upd scd. */
    @Column(name = "UPD_SCD")
    private String updScd;
    
    /** The upd pg. */
    @Column(name = "UPD_PG")
    private String updPg;
    
    /** The exclus ver. */
    @Basic(optional = false)
    @Column(name = "EXCLUS_VER")
    private int exclusVer;
    
    /** The ccd. */
    @Id
    @Basic(optional = false)
    @Column(name = "CCD")
    private String ccd;
    
    /** The emp. */
    @Basic(optional = false)
    @Column(name = "EMP")
    private String emp;
    
    /** The dep. */
    @Basic(optional = false)
    @Column(name = "DEP")
    private String dep;
    
    /** The workshop. */
    @Basic(optional = false)
    @Column(name = "WORKSHOP")
    private String workshop;
    
    /** The class 1. */
    @Basic(optional = false)
    @Column(name = "CLASS")
    private String class1;
    
    /** The jobtitle. */
    @Basic(optional = false)
    @Column(name = "JOBTITLE")
    private String jobtitle;
    
    /** The person. */
    @Basic(optional = false)
    @Column(name = "PERSON")
    private String person;
    
    /** The office. */
    @Basic(optional = false)
    @Column(name = "OFFICE")
    private String office;
    
    /** The work. */
    @Basic(optional = false)
    @Column(name = "WORK")
    private String work;
    
    /** The work place. */
    @Basic(optional = false)
    @Column(name = "WORK_PLACE")
    private String workPlace;
    
    /** The project. */
    @Basic(optional = false)
    @Column(name = "PROJECT")
    private String project;
    
    /** The ad hoc work. */
    @Basic(optional = false)
    @Column(name = "AD_HOC_WORK")
    private String adHocWork;
    
    /** The substitute holiday. */
    @Basic(optional = false)
    @Column(name = "SUBSTITUTE_HOLIDAY")
    private String substituteHoliday;
    
    /** The substitute work. */
    @Basic(optional = false)
    @Column(name = "SUBSTITUTE_WORK")
    private String substituteWork;
    
    /** The compensation holiday. */
    @Basic(optional = false)
    @Column(name = "COMPENSATION_HOLIDAY")
    private String compensationHoliday;
    
    /** The exsess holiday. */
    @Basic(optional = false)
    @Column(name = "EXSESS_HOLIDAY")
    private String exsessHoliday;
    
    /** The binding time. */
    @Basic(optional = false)
    @Column(name = "BINDING_TIME")
    private String bindingTime;
    
    /** The pay absense days. */
    @Basic(optional = false)
    @Column(name = "PAY_ABSENSE_DAYS")
    private String payAbsenseDays;
    
    /** The pay attendance days. */
    @Basic(optional = false)
    @Column(name = "PAY_ATTENDANCE_DAYS")
    private String payAttendanceDays;

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccd != null ? ccd.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmnmtCalled)) {
            return false;
        }
        CmnmtCalled other = (CmnmtCalled) object;
        if ((this.ccd == null && other.ccd != null) 
        		|| (this.ccd != null && !this.ccd.equals(other.ccd))) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "entity.CmnmtCalled[ ccd=" + ccd + " ]";
    }
    
}
