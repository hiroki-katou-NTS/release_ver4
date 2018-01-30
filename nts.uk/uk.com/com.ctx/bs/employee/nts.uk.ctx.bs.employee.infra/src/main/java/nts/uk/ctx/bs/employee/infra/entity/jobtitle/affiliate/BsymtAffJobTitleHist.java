/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.jobtitle.affiliate;

import java.io.Serializable;

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
	@Column(name = "HISTORY_ID")
	private String hisId;

	/** The employeeId. */
	@Column(name = "SID")
	private String sid;

	/** The company id */
	@Column(name = "CID")
	private String cid;

	@Column(name = "STR_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate strDate;

	@Column(name = "END_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endDate;
	
	/** The bsymt aff job title hist item. */
	@OneToOne
	@PrimaryKeyJoinColumns({ @PrimaryKeyJoinColumn(name = "HISTORY_ID", referencedColumnName = "HISTORY_ID") })
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
			GeneralDate endDate) {
		super();
		this.hisId = hisId;
		this.sid = sid;
		this.cid = cid;
		this.strDate = strDate;
		this.endDate = endDate;
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
