/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * The persistent class for the KRCMT_DVGC_TIME database table.
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_DVGC_TIME")
public class KrcmtDvgcTime extends ContractUkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@EmbeddedId
	private KrcstDvgcTimePK id;

	/** The dvgc reason inputed. */
	@Column(name = "DVGC_REASON_INPUTED")
	private boolean dvgcReasonInputed;

	/** The dvgc reason selected. */
	@Column(name = "DVGC_REASON_SELECTED")
	private boolean dvgcReasonSelected;

	/** The dvgc time name. */
	@Column(name = "DVGC_TIME_NAME")
	private String dvgcTimeName;

	/** The dvgc time use set. */
	@Column(name = "DVGC_TIME_USE_SET")
	private BigDecimal dvgcTimeUseSet;

	/** The dvgc type. */
	@Column(name = "DVGC_TYPE")
	private BigDecimal dvgcType;

	/** The reason input canceled. */
	@Column(name = "REASON_INPUT_CANCELED")
	private boolean reasonInputCanceled;

	/** The reason select canceled. */
	@Column(name = "REASON_SELECT_CANCELED")
	private boolean reasonSelectCanceled;
	
	/** The krcst dvgc attendances. */
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "NO", referencedColumnName = "NO", insertable = true, updatable = true) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KrcmtDvgcAttendance> krcstDvgcAttendances;

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.id;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcmtDvgcTime)) {
			return false;
		}
		KrcmtDvgcTime other = (KrcmtDvgcTime) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}
}