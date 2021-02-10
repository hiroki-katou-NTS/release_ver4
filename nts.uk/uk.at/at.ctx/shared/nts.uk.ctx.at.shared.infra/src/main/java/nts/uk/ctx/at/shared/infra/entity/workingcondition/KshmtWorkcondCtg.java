/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtWorkcondCtg.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_WORKCOND_CTG")
public class KshmtWorkcondCtg extends UkJpaEntity implements Serializable {

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
	private List<KshmtWorkcondCtgTs> kshmtWorkCatTimeZones;

	/**
	 * Instantiates a new kshmt per work cat.
	 */
	public KshmtWorkcondCtg() {
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
		if (!(object instanceof KshmtWorkcondCtg)) {
			return false;
		}
		KshmtWorkcondCtg other = (KshmtWorkcondCtg) object;
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

	public KshmtWorkcondCtg(KshmtPerWorkCatPK kshmtPerWorkCatPK, String sid, String workTypeCode, String workTimeCode,
			List<KshmtWorkcondCtgTs> kshmtWorkCatTimeZones) {
		super();
		this.kshmtPerWorkCatPK = kshmtPerWorkCatPK;
		this.sid = sid;
		this.workTypeCode = workTypeCode;
		this.workTimeCode = workTimeCode;
		this.kshmtWorkCatTimeZones = kshmtWorkCatTimeZones;
	}
	
	public SingleDaySchedule toDomain() {
		return new SingleDaySchedule(this.workTypeCode,
				kshmtWorkCatTimeZones.stream().map(c -> c.toDomain()).collect(Collectors.toList()),
				Optional.ofNullable(this.workTimeCode));
	}
	
	public static KshmtWorkcondCtg toEntity(SingleDaySchedule domain,String historyId,String sid,int workCategoryAtr) {
		return new KshmtWorkcondCtg(
				new KshmtPerWorkCatPK(historyId, workCategoryAtr),
				sid, 
				domain.getWorkTypeCode().isPresent()?domain.getWorkTypeCode().get().v():null, 
				domain.getWorkTimeCode().isPresent()?domain.getWorkTimeCode().get().v():null,
				domain.getWorkingHours().stream().map(c->KshmtWorkcondCtgTs.toEntity(c, historyId, workCategoryAtr)).collect(Collectors.toList())
				);
	}
}
