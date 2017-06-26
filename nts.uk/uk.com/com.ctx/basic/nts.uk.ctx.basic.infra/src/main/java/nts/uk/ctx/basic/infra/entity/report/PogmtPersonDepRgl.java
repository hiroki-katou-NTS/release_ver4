/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.infra.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class PogmtPersonDepRgl.
 */

@Setter
@Getter
@Entity
@Table(name = "POGMT_PERSON_DEP_RGL")
public class PogmtPersonDepRgl extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The pogmt person dep rgl PK. */
	@EmbeddedId
	protected PogmtPersonDepRglPK pogmtPersonDepRglPK;

	/** The inv scd. */
	@Column(name = "INV_SCD")
	private String invScd;

	/** The str D. */
	@Column(name = "STR_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate strD;

	/** The end D. */
	@Basic(optional = false)
	@Column(name = "END_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endD;

	/** The exp D. */
	@Basic(optional = false)
	@Column(name = "EXP_D")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expD;

	/** The depcd. */
	@Basic(optional = false)
	@Column(name = "DEPCD")
	private String depcd;

	/** The issue date. */
	@Column(name = "ISSUE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date issueDate;

	/** The issue sts. */
	@Basic(optional = false)
	@Column(name = "ISSUE_STS")
	private short issueSts;

	/** The move type. */
	@Column(name = "MOVE_TYPE")
	private String moveType;

	/** The dist ratio. */
	@Basic(optional = false)
	@Column(name = "DIST_RATIO")
	private BigDecimal distRatio;

	/** The gp 01. */
	@Column(name = "GP01")
	private String gp01;

	/** The gp 02. */
	@Column(name = "GP02")
	private String gp02;

	/** The gp 03. */
	@Column(name = "GP03")
	private String gp03;

	/** The gp 04. */
	@Column(name = "GP04")
	private String gp04;

	/** The gp 05. */
	@Column(name = "GP05")
	private String gp05;

	/** The gp 06. */
	@Column(name = "GP06")
	private String gp06;

	/** The gp 07. */
	@Column(name = "GP07")
	private String gp07;

	/** The gp 08. */
	@Column(name = "GP08")
	private String gp08;

	/** The gp 09. */
	@Column(name = "GP09")
	private String gp09;

	/** The gp 10. */
	@Column(name = "GP10")
	private String gp10;

	/** The gp 11. */
	@Column(name = "GP11")
	private String gp11;

	/** The gp 12. */
	@Column(name = "GP12")
	private String gp12;

	/** The gp 13. */
	@Column(name = "GP13")
	private String gp13;

	/** The gp 14. */
	@Column(name = "GP14")
	private String gp14;

	/** The gp 15. */
	@Column(name = "GP15")
	private String gp15;

	/** The gp 16. */
	@Column(name = "GP16")
	private String gp16;

	/** The gp 17. */
	@Column(name = "GP17")
	private String gp17;

	/** The gp 18. */
	@Column(name = "GP18")
	private String gp18;

	/** The gp 19. */
	@Column(name = "GP19")
	private String gp19;

	/** The gp 20. */
	@Column(name = "GP20")
	private String gp20;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pogmtPersonDepRglPK != null ? pogmtPersonDepRglPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof PogmtPersonDepRgl)) {
			return false;
		}
		PogmtPersonDepRgl other = (PogmtPersonDepRgl) object;
		if ((this.pogmtPersonDepRglPK == null && other.pogmtPersonDepRglPK != null)
				|| (this.pogmtPersonDepRglPK != null
						&& !this.pogmtPersonDepRglPK.equals(other.pogmtPersonDepRglPK))) {
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
		return this.pogmtPersonDepRglPK;
	}

}
