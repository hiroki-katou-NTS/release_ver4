/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workdayoff.frame;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Getter
@Setter
@Entity
@Table(name = "KSHST_WORKDAYOFF_FRAME")
public class KshstWorkdayoffFrame extends UkJpaEntity implements Serializable {
	
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The kshst workdayoff frame PK. */
    @EmbeddedId
    protected KshstWorkdayoffFramePK kshstWorkdayoffFramePK;
    
    /** The exclus ver. */
    @Column(name = "EXCLUS_VER")
    private int exclusVer;
  
    /** The use atr. */
    @Column(name = "USE_ATR")
    private short useAtr;
   
    /** The wdo fr name. */
    @Column(name = "WDO_FR_NAME")
    private String wdoFrName;
   
    /** The trans fr name. */
    @Column(name = "TRANS_FR_NAME")
    private String transFrName;

    /**
     * Instantiates a new kshst workdayoff frame.
     */
    public KshstWorkdayoffFrame() {
    	super();
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kshstWorkdayoffFramePK != null ? kshstWorkdayoffFramePK.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KshstWorkdayoffFrame)) {
            return false;
        }
        KshstWorkdayoffFrame other = (KshstWorkdayoffFrame) object;
        if ((this.kshstWorkdayoffFramePK == null && other.kshstWorkdayoffFramePK != null) || (this.kshstWorkdayoffFramePK != null && !this.kshstWorkdayoffFramePK.equals(other.kshstWorkdayoffFramePK))) {
            return false;
        }
        return true;
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.kshstWorkdayoffFramePK;
	}

	public KshstWorkdayoffFrame(KshstWorkdayoffFramePK kshstWorkdayoffFramePK, int exclusVer, short useAtr,
			String wdoFrName, String transFrName) {
		super();
		this.kshstWorkdayoffFramePK = kshstWorkdayoffFramePK;
		this.exclusVer = exclusVer;
		this.useAtr = useAtr;
		this.wdoFrName = wdoFrName;
		this.transFrName = transFrName;
	}
    
}
