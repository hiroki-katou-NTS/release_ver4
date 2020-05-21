/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.optitem.calculation;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KrcmtFormulaSetting.
 */
@Getter
@Setter
@Entity
@Table(name = "KRCMT_FORMULA_SETTING")
public class KrcmtFormulaSetting extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The krcmt formula setting PK. */
	@EmbeddedId
	protected KrcmtFormulaSettingPK krcmtFormulaSettingPK;


	/** The minus segment. */
	@Column(name = "MINUS_SEGMENT")
	private int minusSegment;

	/** The operator. */
	@Column(name = "OPERATOR")
	private int operator;

	/** The left set method. */
	@Column(name = "LEFT_SET_METHOD")
	private int leftSetMethod;

	/** The left input val. */
	@Column(name = "LEFT_INPUT_VAL")
	private BigDecimal leftInputVal;

	/** The left formula item id. */
	@Column(name = "LEFT_FORMULA_ITEM_ID")
	private String leftFormulaItemId;

	/** The right set method. */
	@Column(name = "RIGHT_SET_METHOD")
	private int rightSetMethod;

	/** The right input val. */
	@Column(name = "RIGHT_INPUT_VAL")
	private BigDecimal rightInputVal;

	/** The right formula item id. */
	@Column(name = "RIGHT_FORMULA_ITEM_ID")
	private String rightFormulaItemId;

	/**
	 * Instantiates a new krcmt formula setting.
	 */
	public KrcmtFormulaSetting() {
		super();
	}

	/**
	 * Instantiates a new krcmt formula setting.
	 *
	 * @param pk the pk
	 */
	public KrcmtFormulaSetting(KrcmtOptItemFormulaPK pk) {
		super();
		this.krcmtFormulaSettingPK = new KrcmtFormulaSettingPK(pk);
	}

	/**
	 * Instantiates a new krcmt formula setting.
	 *
	 * @param krcmtFormulaSettingPK the krcmt formula setting PK
	 */
	public KrcmtFormulaSetting(KrcmtFormulaSettingPK krcmtFormulaSettingPK) {
		this.krcmtFormulaSettingPK = krcmtFormulaSettingPK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (krcmtFormulaSettingPK != null ? krcmtFormulaSettingPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcmtFormulaSetting)) {
			return false;
		}
		KrcmtFormulaSetting other = (KrcmtFormulaSetting) object;
		if ((this.krcmtFormulaSettingPK == null && other.krcmtFormulaSettingPK != null)
				|| (this.krcmtFormulaSettingPK != null
						&& !this.krcmtFormulaSettingPK.equals(other.krcmtFormulaSettingPK))) {
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
		return this.krcmtFormulaSettingPK;
	}

}
