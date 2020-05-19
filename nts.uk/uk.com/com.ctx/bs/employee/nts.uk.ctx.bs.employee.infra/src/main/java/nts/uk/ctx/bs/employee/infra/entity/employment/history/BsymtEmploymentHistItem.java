/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.employment.history;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtEmploymentHistItem.
 */
@Entity
@Table(name = "BSYMT_EMPLOYMENT_HIS_ITEM")
public class BsymtEmploymentHistItem extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The historyid -  PK. */
	@Id
	@Column(name = "HIST_ID")
	public String hisId;

	/** The employeeId. */
	@Basic(optional = false)
	@Column(name = "SID")
	public String sid;
	
	/** The empCode. */
	@Basic(optional = false)
	@Column(name = "EMP_CD")
	public String empCode;
	
	/** The empCode.
	 * 1 = dailySalary - 日給
	 * 2 = dailyMonthlySalary - 日給月給
	 * 3 = hourlySalary - 時間給
	 * 4 = monthlySalary - 月給
	 *  */
	@Basic(optional = true)
	@Column(name = "SALARY_SEGMENT")
	public Integer salarySegment;
	
	/** The bsymt aff class history. */
	// Add by ThanhNC
	@OneToOne
	@PrimaryKeyJoinColumns({ @PrimaryKeyJoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID") })
	public BsymtEmploymentHist bsymtEmploymentHist;

	/**
	 * Instantiates a new bsymt employment hist item.
	 *
	 * @param hisId the his id
	 * @param sid the sid
	 * @param empCode the emp code
	 * @param salarySegment the salary segment
	 */
	public BsymtEmploymentHistItem(String hisId, String sid, String empCode, Integer salarySegment) {
		super();
		this.hisId = hisId;
		this.sid = sid;
		this.empCode = empCode;
		this.salarySegment = salarySegment;
	}
	
	/**
	 * Instantiates a new cempt employment.
	 */
	public BsymtEmploymentHistItem() {
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
	
	public static final JpaEntityMapper<BsymtEmploymentHistItem> MAPPER = new JpaEntityMapper<>(BsymtEmploymentHistItem.class);

}
