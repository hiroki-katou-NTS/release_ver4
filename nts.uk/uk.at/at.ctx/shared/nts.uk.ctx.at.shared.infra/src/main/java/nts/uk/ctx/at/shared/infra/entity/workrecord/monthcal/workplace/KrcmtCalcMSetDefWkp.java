/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workrecord.monthcal.workplace;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.infra.entity.workrecord.monthcal.KrcstDeforMCalSet;

/**
 * The Class KrcmtCalcMSetDefWkp.
 */
@Getter
@Setter
@Entity
@Table(name = "KRCST_WKP_DEFOR_M_CAL_SET")
public class KrcmtCalcMSetDefWkp extends KrcstDeforMCalSet implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The krcst wkp defor M cal set PK. */
	@EmbeddedId
	protected KrcstWkpDeforMCalSetPK krcstWkpDeforMCalSetPK;

	/**
	 * Instantiates a new krcst wkp defor M cal set.
	 */
	public KrcmtCalcMSetDefWkp() {
		super();
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (krcstWkpDeforMCalSetPK != null ? krcstWkpDeforMCalSetPK.hashCode() : 0);
		return hash;
	}

	/**
	 * Equals.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcmtCalcMSetDefWkp)) {
			return false;
		}
		KrcmtCalcMSetDefWkp other = (KrcmtCalcMSetDefWkp) object;
		if ((this.krcstWkpDeforMCalSetPK == null && other.krcstWkpDeforMCalSetPK != null)
				|| (this.krcstWkpDeforMCalSetPK != null
						&& !this.krcstWkpDeforMCalSetPK.equals(other.krcstWkpDeforMCalSetPK))) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	@Override
	protected Object getKey() {
		return this.krcstWkpDeforMCalSetPK;
	}

}
