/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.optitem.calculation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KrcmtCalcItemSelection.
 */
@Getter
@Setter
@Entity
@Table(name = "KRCMT_CALC_ITEM_SELECTION")
public class KrcmtCalcItemSelection extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The krcmt calc item selection PK. */
	@EmbeddedId
	protected KrcmtCalcItemSelectionPK krcmtCalcItemSelectionPK;

	/** The krcmt opt item formula. */
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
		@JoinColumn(name = "OPTIONAL_ITEM_NO", referencedColumnName = "OPTIONAL_ITEM_NO", insertable = false, updatable = false),
		@JoinColumn(name = "FORMULA_ID", referencedColumnName = "FORMULA_ID", insertable = false, updatable = false)
		})
	private KrcmtOptItemFormula krcmtOptItemFormula;

	/** The minus segment. */
	@Column(name = "MINUS_SEGMENT")
	private int minusSegment;

	/** The operator. */
	@Column(name = "OPERATOR")
	private int operator;

	/**
	 * Instantiates a new krcmt calc item selection.
	 */
	public KrcmtCalcItemSelection() {
		super();
	}

	/**
	 * Instantiates a new krcmt calc item selection.
	 *
	 * @param krcmtCalcItemSelectionPK the krcmt calc item selection PK
	 */
	public KrcmtCalcItemSelection(KrcmtCalcItemSelectionPK krcmtCalcItemSelectionPK) {
		this.krcmtCalcItemSelectionPK = krcmtCalcItemSelectionPK;
	}

	public KrcmtCalcItemSelection(KrcmtOptItemFormulaPK pk) {
		this.krcmtCalcItemSelectionPK = new KrcmtCalcItemSelectionPK();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (krcmtCalcItemSelectionPK != null ? krcmtCalcItemSelectionPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcmtCalcItemSelection)) {
			return false;
		}
		KrcmtCalcItemSelection other = (KrcmtCalcItemSelection) object;
		if ((this.krcmtCalcItemSelectionPK == null && other.krcmtCalcItemSelectionPK != null)
				|| (this.krcmtCalcItemSelectionPK != null
						&& !this.krcmtCalcItemSelectionPK.equals(other.krcmtCalcItemSelectionPK))) {
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
		return this.krcmtCalcItemSelectionPK;
	}

}
