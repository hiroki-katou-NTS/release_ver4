/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.jobtitle.affiliate;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtAffJobTitleHist. 所属職位履歴
 */
@Getter
@Setter
@Entity
@Table(name = "BSYMT_AFF_JOB_HIST")
public class BsymtAffJobTitleHist extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The historyid - PK. */
	@Id
	@Column(name = "HIST_ID")
	public String hisId;

	/** The employeeId. */
	@Column(name = "SID")
	public String sid;

	/** The company id */
	@Column(name = "CID")
	public String cid;

	@Column(name = "START_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate strDate;

	@Column(name = "END_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate endDate;
	
//	 Merge BSYMT_AFF_JOB_HIST To BSYMT_AFF_JOB_HIST_ITEM  because response
	/** The empCode. */
	@Basic(optional = false)
	@Column(name = "JOB_TITLE_ID")
	public String jobTitleId;
	
	@Basic(optional = false)
	@Column(name = "NOTE")
	public String note;
	
	/** The bsymt aff job title hist item. */
	@OneToOne
	@PrimaryKeyJoinColumns({ @PrimaryKeyJoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID") })
	public BsymtAffJobTitleHistItem bsymtAffJobTitleHistItem;

	/**
	 * Instantiates a new cempt employment.
	 */
	public BsymtAffJobTitleHist() {
		super();
	}

	/**
	 * Instantiates a new bsymt aff job title hist.
	 *
	 * @param hisId
	 *            the his id
	 * @param sid
	 *            the sid
	 * @param cid
	 *            the cid
	 * @param strDate
	 *            the str date
	 * @param endDate
	 *            the end date
	 */
	public BsymtAffJobTitleHist(String hisId, String sid, String cid, GeneralDate strDate,
			GeneralDate endDate, String jobTitleId, String note) {
		super();
		this.hisId = hisId;
		this.sid = sid;
		this.cid = cid;
		this.strDate = strDate;
		this.endDate = endDate;
		this.jobTitleId = jobTitleId;
		this.note = note;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.hisId;
	}

}
