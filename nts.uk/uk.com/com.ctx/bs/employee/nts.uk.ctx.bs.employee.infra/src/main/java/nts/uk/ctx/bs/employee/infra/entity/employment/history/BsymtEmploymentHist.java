/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.employment.history;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtEmploymentHist.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "BSYMT_EMPLOYMENT_HIST")
public class BsymtEmploymentHist extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The historyid - PK. */
	@Id
	@Column(name = "HISTORY_ID")
	private String hisId;

	@Column(name = "CID")
	public String companyId;

	/** The employeeId. */
	@Basic(optional = false)
	@Column(name = "SID")
	private String sid;

	@Basic(optional = true)
	@Column(name = "STR_DATE")
	private GeneralDate strDate;

	@Basic(optional = true)
	@Column(name = "END_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endDate;

	/**
	 * Instantiates a new cempt employment.
	 */
	public BsymtEmploymentHist() {
		super();
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
