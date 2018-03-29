/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.at.shared.infra.entity.monthlyattditem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KrcmtMonAttendanceItem.
 */
@Getter
@Setter
@Entity
@Table(name = "KRCMT_MON_ATTENDANCE_ITEM")
public class KrcmtMonAttendanceItem extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The krcmt mon attendance item PK. */
	@EmbeddedId
	protected KrcmtMonAttendanceItemPK krcmtMonAttendanceItemPK;

	/** The m atd item name. */
	@Column(name = "M_ATD_ITEM_NAME")
	private String mAtdItemName;

	/** The m atd item atr. */
	@Column(name = "M_ATD_ITEM_ATR")
	private int mAtdItemAtr;

	/** The disp no. */
	@Column(name = "DISP_NO")
	private int dispNo;

	/** The is allow change. */
	@Column(name = "IS_ALLOW_CHANGE")
	private int isAllowChange;

	/** The line break pos name. */
	@Column(name = "LINE_BREAK_POS_NAME")
	private int lineBreakPosName;

	/**
	 * Instantiates a new krcmt mon attendance item.
	 */
	public KrcmtMonAttendanceItem() {
		super();
	}

	/**
	 * Instantiates a new krcmt mon attendance item.
	 *
	 * @param krcmtMonAttendanceItemPK the krcmt mon attendance item PK
	 */
	public KrcmtMonAttendanceItem(KrcmtMonAttendanceItemPK krcmtMonAttendanceItemPK) {
		this.krcmtMonAttendanceItemPK = krcmtMonAttendanceItemPK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (krcmtMonAttendanceItemPK != null ? krcmtMonAttendanceItemPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcmtMonAttendanceItem)) {
			return false;
		}
		KrcmtMonAttendanceItem other = (KrcmtMonAttendanceItem) object;
		if ((this.krcmtMonAttendanceItemPK == null && other.krcmtMonAttendanceItemPK != null)
				|| (this.krcmtMonAttendanceItemPK != null
						&& !this.krcmtMonAttendanceItemPK.equals(other.krcmtMonAttendanceItemPK))) {
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
		return this.getKrcmtMonAttendanceItemPK();
	}

}
