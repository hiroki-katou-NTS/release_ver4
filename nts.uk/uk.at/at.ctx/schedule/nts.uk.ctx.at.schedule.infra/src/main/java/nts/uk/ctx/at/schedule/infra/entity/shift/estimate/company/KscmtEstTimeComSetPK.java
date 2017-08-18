/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.estimate.company;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * The Class KscmtEstTimeComSetPK.
 */
@Embeddable
public class KscmtEstTimeComSetPK implements Serializable {
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The cid. */
	@Basic(optional = false)
    @NotNull
    @Column(name = "CID")
    private String cid;
    
    /** The target year. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "TARGET_YEAR")
    private short targetYear;
    
    /** The target cls. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "TARGET_CLS")
    private short targetCls;

    /**
     * Instantiates a new kscmt est time com set PK.
     */
    public KscmtEstTimeComSetPK() {
    }

    /**
     * Instantiates a new kscmt est time com set PK.
     *
     * @param cid the cid
     * @param targetYear the target year
     * @param targetCls the target cls
     */
    public KscmtEstTimeComSetPK(String cid, short targetYear, short targetCls) {
        this.cid = cid;
        this.targetYear = targetYear;
        this.targetCls = targetCls;
    }
    
}
