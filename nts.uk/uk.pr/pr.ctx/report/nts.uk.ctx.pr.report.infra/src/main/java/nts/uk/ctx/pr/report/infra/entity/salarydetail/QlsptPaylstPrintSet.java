/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.entity.salarydetail;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QlsptPaylstPrintSet.
 */
@Getter
@Setter
@Entity
@Table(name = "QLSPT_PAYLST_PRINT_SET")
public class QlsptPaylstPrintSet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Id
	@Column(name = "CCD")
	private String ccd;

	/** The show payment. */
	@Column(name = "SHOW_PAYMENT")
	private Integer showPayment;

	/** The sum person set. */
	@Column(name = "SUM_PERSON_SET")
	private Integer sumPersonSet;

	/** The sum month person set. */
	@Column(name = "SUM_MONTH_PERSON_SET")
	private Integer sumMonthPersonSet;

	/** The sum each deprt set. */
	@Column(name = "SUM_EACH_DEPRT_SET")
	private Integer sumEachDeprtSet;

	/** The sum month deprt set. */
	@Column(name = "SUM_MONTH_DEPRT_SET")
	private Integer sumMonthDeprtSet;

	/** The sum dep hrchy index set. */
	@Column(name = "SUM_DEP_HRCHY_INDEX_SET")
	private Integer sumDepHrchyIndexSet;

	/** The sum month dep hrchy set. */
	@Column(name = "SUM_MONTH_DEP_HRCHY_SET")
	private Integer sumMonthDepHrchySet;

	/** The hrchy index 1. */
	@Column(name = "HRCHY_INDEX1")
	private Integer hrchyIndex1;

	/** The hrchy index 2. */
	@Column(name = "HRCHY_INDEX2")
	private Integer hrchyIndex2;

	/** The hrchy index 3. */
	@Column(name = "HRCHY_INDEX3")
	private Integer hrchyIndex3;

	/** The hrchy index 4. */
	@Column(name = "HRCHY_INDEX4")
	private Integer hrchyIndex4;

	/** The hrchy index 5. */
	@Column(name = "HRCHY_INDEX5")
	private Integer hrchyIndex5;

	/** The total set. */
	@Column(name = "TOTAL_SET")
	private Integer totalSet;

	/** The month total set. */
	@Column(name = "MONTH_TOTAL_SET")
	private Integer monthTotalSet;

	/**
	 * Instantiates a new qlspt paylst print set.
	 */
	public QlsptPaylstPrintSet() {
		super();
	}

	/**
	 * Instantiates a new qlspt paylst print set.
	 *
	 * @param ccd
	 *            the ccd
	 */
	public QlsptPaylstPrintSet(String ccd) {
		super();
		this.ccd = ccd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ccd != null ? ccd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QlsptPaylstPrintSet)) {
			return false;
		}
		QlsptPaylstPrintSet other = (QlsptPaylstPrintSet) object;
		if ((this.ccd == null && other.ccd != null)
				|| (this.ccd != null && !this.ccd.equals(other.ccd))) {
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
		return this.ccd;
	}

}
