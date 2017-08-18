/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.estimate.employment;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KscmtEstDaysEmpSetPK.
 */
@Getter
@Setter
@Embeddable
public class KscmtEstDaysEmpSetPK implements Serializable {
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The cid. */
	@Basic(optional = false)
    @NotNull
    @Column(name = "CID")
    private String cid;
    
    /** The empcd. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMPCD")
    private String empcd;
    
    /** The target year. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "TARGET_YEAR")
    private int targetYear;
    
    /** The target cls. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "TARGET_CLS")
    private int targetCls;

    /**
     * Instantiates a new kscmt est days emp set PK.
     */
    public KscmtEstDaysEmpSetPK() {
    }

    /**
     * Instantiates a new kscmt est days emp set PK.
     *
     * @param cid the cid
     * @param empcd the empcd
     * @param targetYear the target year
     * @param targetCls the target cls
     */
    public KscmtEstDaysEmpSetPK(String cid, String empcd, int targetYear, int targetCls) {
        this.cid = cid;
        this.empcd = empcd;
        this.targetYear = targetYear;
        this.targetCls = targetCls;
    }

    
}
