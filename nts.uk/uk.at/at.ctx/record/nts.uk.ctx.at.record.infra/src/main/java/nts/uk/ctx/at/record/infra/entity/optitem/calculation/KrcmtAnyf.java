/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.optitem.calculation;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KrcmtAnyf.
 */
@Getter
@Setter
@Entity
@Table(name = "KRCMT_ANYF")
public class KrcmtAnyf extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant ITEM_SELECTION. */
	public static final int ITEM_SELECTION = 0;

	/** The Constant FORMULA_SETTING. */
	public static final int FORMULA_SETTING = 1;

	/** The krcmt opt item formula PK. */
	@EmbeddedId
	protected KrcmtOptItemFormulaPK krcmtOptItemFormulaPK;

	/** The formula name. */
	@Column(name = "FORMULA_NAME")
	private String formulaName;

	/** The formula atr. */
	@Column(name = "FORMULA_ATR")
	private int formulaAtr;

	/** The symbol. */
	@Column(name = "SYMBOL")
	private String symbol;

	/** The calc atr. */
	@Column(name = "CALC_ATR")
	private int calcAtr;

	/** The krcmt formula roundings. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcmtOptItemFormula", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KrcmtAnyfRound> krcmtFormulaRoundings;

	/** The krcmt calc item selections. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcmtOptItemFormula", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KrcmtAnyfItemSelect> krcmtCalcItemSelections;

	/** The krcmt formula setting. */
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
		@JoinColumn(name = "OPTIONAL_ITEM_NO", referencedColumnName = "OPTIONAL_ITEM_NO", insertable = false, updatable = false),
		@JoinColumn(name = "FORMULA_ID", referencedColumnName = "FORMULA_ID", insertable = false, updatable = false)
	})
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private KrcmtAnyfDetail krcmtFormulaSetting;


	/**
	 * Instantiates a new krcmt opt item formula.
	 */
	public KrcmtAnyf() {
		super();
	}

	/**
	 * Instantiates a new krcmt opt item formula.
	 *
	 * @param krcmtOptItemFormulaPK
	 *            the krcmt opt item formula PK
	 */
	public KrcmtAnyf(KrcmtOptItemFormulaPK krcmtOptItemFormulaPK) {
		this.krcmtOptItemFormulaPK = krcmtOptItemFormulaPK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (krcmtOptItemFormulaPK != null ? krcmtOptItemFormulaPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcmtAnyf)) {
			return false;
		}
		KrcmtAnyf other = (KrcmtAnyf) object;
		if ((this.krcmtOptItemFormulaPK == null && other.krcmtOptItemFormulaPK != null)
				|| (this.krcmtOptItemFormulaPK != null
						&& !this.krcmtOptItemFormulaPK.equals(other.krcmtOptItemFormulaPK))) {
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
		return this.krcmtOptItemFormulaPK;
	}

}
