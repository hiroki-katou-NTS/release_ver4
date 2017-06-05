/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The KmfmtCompensLeaveCom.
 */

@Setter
@Getter
@Entity
@Table(name = "KMFMT_COMPENS_LEAVE_COM")
public class KmfmtCompensLeaveCom extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Id
	@Column(name = "CID")
	private String cid;

	/** The manage atr. */
	@Column(name = "MANAGE_ATR")
	private int manageAtr;
	
	@OneToOne(optional = true, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public KmfmtNormalVacationSet normal;
	
	@JoinColumns(@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = false))
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<KmfmtOccurVacationSet> listOccur;

	/**
	 * Instantiates a new kmfmt compens leave com.
	 */
	public KmfmtCompensLeaveCom() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (cid != null ? cid.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KmfmtCompensLeaveCom)) {
			return false;
		}
		KmfmtCompensLeaveCom other = (KmfmtCompensLeaveCom) object;
		if ((this.cid == null && other.cid != null)
				|| (this.cid != null && !this.cid.equals(other.cid))) {
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
		return this.cid;
	}
}
