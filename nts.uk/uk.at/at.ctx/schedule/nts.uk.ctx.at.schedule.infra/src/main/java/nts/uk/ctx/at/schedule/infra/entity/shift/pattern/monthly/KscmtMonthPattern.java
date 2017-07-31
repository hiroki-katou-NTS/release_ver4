/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.pattern.monthly;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.JpaEntity;

/**
 * The Class KscmtMonthPattern.
 */
@Getter
@Setter
@Entity
@Table(name = "KSCMT_MONTH_PATTERN")
public class KscmtMonthPattern extends JpaEntity implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The kscmt month pattern PK. */
    @EmbeddedId
    protected KscmtMonthPatternPK kscmtMonthPatternPK;
    
    /** The m pattern name. */
    @Basic(optional = false)
    @NotNull
    @Column(name = "M_PATTERN_NAME")
    private String mPatternName;

    /**
     * Instantiates a new kmpmt month pattern.
     */
    public KscmtMonthPattern() {
    	super();
    }

    public KscmtMonthPattern(KscmtMonthPatternPK kmpmtMonthPatternPK) {
        this.kscmtMonthPatternPK = kmpmtMonthPatternPK;
    }

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.kscmtMonthPatternPK;
	}
    
}
