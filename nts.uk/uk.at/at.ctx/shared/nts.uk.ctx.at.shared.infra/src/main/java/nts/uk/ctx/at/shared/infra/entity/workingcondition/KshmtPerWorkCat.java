/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtPerWorkCat.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_PER_WORK_CAT")
public class KshmtPerWorkCat extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt per work cat PK. */
	@EmbeddedId
	protected KshmtPerWorkCatPK kshmtPerWorkCatPK;
	
	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;
	
	/** SID  */
	@Column(name = "SID")
	private String sid;

	/** The work type code. */
	@Column(name = "WORK_TYPE_CODE")
	private String workTypeCode;

	/** The work time code. */
	@Column(name = "WORK_TIME_CODE")
	private String workTimeCode;

	/** The kshmt per work cats. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = true, updatable = true) ,
			@JoinColumn(name = "PER_WORK_CAT_ATR", referencedColumnName = "PER_WORK_CAT_ATR", insertable = true, updatable = true) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KshmtWorkCatTimeZone> kshmtWorkCatTimeZones;

	public static final JpaEntityMapper<KshmtPerWorkCat> MAPPER = new JpaEntityMapper<>(KshmtPerWorkCat.class);
	
	/**
	 * Instantiates a new kshmt per work cat.
	 */
	public KshmtPerWorkCat() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (kshmtPerWorkCatPK != null ? kshmtPerWorkCatPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtPerWorkCat)) {
			return false;
		}
		KshmtPerWorkCat other = (KshmtPerWorkCat) object;
		if ((this.kshmtPerWorkCatPK == null && other.kshmtPerWorkCatPK != null)
				|| (this.kshmtPerWorkCatPK != null
						&& !this.kshmtPerWorkCatPK.equals(other.kshmtPerWorkCatPK))) {
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
		return this.kshmtPerWorkCatPK;
	}
}
