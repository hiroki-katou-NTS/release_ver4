/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.executionlog;


import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateTimeToDBConverter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KscdtScheExeLog.
 */
@Getter
@Setter
@Entity
@Table(name = "KSCDT_SCHE_EXE_LOG")
public class KscdtScheExeLog extends UkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The kscdt sche exe log PK. */
    @EmbeddedId
    protected KscdtScheExeLogPK kscdtScheExeLogPK;
    
    /** The exe sid. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXE_SID")
    private String exeSid;
    
    /** The exe str D. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXE_STR_D")
    @Convert(converter = GeneralDateTimeToDBConverter.class)
    private GeneralDateTime exeStrD;
    
    /** The exe end D. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXE_END_D")
    @Convert(converter = GeneralDateTimeToDBConverter.class)
    private GeneralDateTime exeEndD;
    
    /** The start ymd. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "START_YMD")
    @Convert(converter = GeneralDateToDBConverter.class)
    private GeneralDate startYmd;
    
    /** The end ymd. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "END_YMD")
    @Convert(converter = GeneralDateToDBConverter.class)
    private GeneralDate endYmd;
    
    /** The completion status. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPLETION_STATUS")
    private int completionStatus;

    /**
     * Instantiates a new kscmt sch execution log.
     */
    public KscdtScheExeLog() {
    }

    /**
     * Instantiates a new kscmt sch execution log.
     *
     * @param kscmtSchExecutionLogPK the kscmt sch execution log PK
     */
    public KscdtScheExeLog(KscdtScheExeLogPK kscmtSchExecutionLogPK) {
        this.kscdtScheExeLogPK = kscmtSchExecutionLogPK;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kscdtScheExeLogPK != null ? kscdtScheExeLogPK.hashCode() : 0);
        return hash;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KscdtScheExeLog)) {
			return false;
		}
		KscdtScheExeLog other = (KscdtScheExeLog) object;
		if ((this.kscdtScheExeLogPK == null && other.kscdtScheExeLogPK != null)
				|| (this.kscdtScheExeLogPK != null && !this.kscdtScheExeLogPK.equals(other.kscdtScheExeLogPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "entity.KscmtSchExecutionLog[ kscmtSchExecutionLogPK=" + kscdtScheExeLogPK + " ]";
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.kscdtScheExeLogPK;
	}
    
}

