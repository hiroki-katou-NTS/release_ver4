/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.pattern.monthly.setting;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.JpaEntity;

/**
 * The Class KscmtMonthPatternSet.
 */
@Getter
@Setter
@Entity
@Table(name = "KSCMT_MONTH_PATTERN_SET")
public class KscmtMonthPatternSet extends JpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** The sid. */
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SID")
    private String sid;
    
    /** The m pattern cd. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "M_PATTERN_CD")
    private String mPatternCd;

    public KscmtMonthPatternSet() {
    }

    public KscmtMonthPatternSet(String sid) {
        this.sid = sid;
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.sid;
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mPatternCd == null) ? 0 : mPatternCd.hashCode());
		result = prime * result + ((sid == null) ? 0 : sid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		KscmtMonthPatternSet other = (KscmtMonthPatternSet) obj;
		if (mPatternCd == null) {
			if (other.mPatternCd != null)
				return false;
		} else if (!mPatternCd.equals(other.mPatternCd))
			return false;
		if (sid == null) {
			if (other.sid != null)
				return false;
		} else if (!sid.equals(other.sid))
			return false;
		return true;
	}

}
