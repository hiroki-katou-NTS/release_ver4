/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.classification.affiliate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtAffClassHistory.
 */
@Entity
@Table(name = "BSYMT_AFF_CLASS_HISTORY")
public class BsymtAffClassHistory extends UkJpaEntity{

	/** The history id. */
	@Id
	@Column(name = "HIST_ID")
	public String historyId;
	
	/** The cid. */
	@Column(name = "CID")
	public String cid;
	
	/** The sid. */
	@Column(name = "SID")
	public String sid;
	
	/** The start date. */
	@Column(name = "START_DATE")
	public GeneralDate startDate;
	
	/** The end date. */
	@Column(name = "END_DATE")
	public GeneralDate endDate;
	
//	 Merge BSYMT_AFF_CLASS_HISTORY To BSYMT_AFF_CLASS_HIS_ITEM  because response
	/** The classification code. */
	@Column(name = "CLASSIFICATION_CODE")
	public String classificationCode;
	
	/** The bsymt aff class hist item. */
	@OneToOne
	@PrimaryKeyJoinColumns({ @PrimaryKeyJoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID") })
	public BsymtAffClassHistItem bsymtAffClassHistItem;
	
	/**
	 * Instantiates a new bsymt aff class history.
	 */
	public BsymtAffClassHistory() {
		super();
	}
	
	/**
	 * Instantiates a new bsymt aff class history ver 1.
	 *
	 * @param historyId the history id
	 * @param cid the cid
	 * @param sid the sid
	 * @param startDate the start date
	 * @param endDate the end date
	 */
	public BsymtAffClassHistory(String historyId, String cid, String sid,
			GeneralDate startDate, GeneralDate endDate, String classificationCode) {
		super();
		this.historyId = historyId;
		this.cid = cid;
		this.sid = sid;
		this.startDate = startDate;
		this.endDate = endDate;
		this.classificationCode = classificationCode;
	}
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return historyId;
	}

	public String getEmployeeId() {
		return sid;
	}

}
