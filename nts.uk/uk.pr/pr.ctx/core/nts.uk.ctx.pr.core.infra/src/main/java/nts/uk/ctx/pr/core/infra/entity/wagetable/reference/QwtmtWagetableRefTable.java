/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.wagetable.reference;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QwtmtWagetableRefTable.
 */
@Getter
@Setter
@Entity
@Table(name = "QWTMT_WAGETABLE_REF_TABLE")
public class QwtmtWagetableRefTable extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qwtmt wagetable ref table PK. */
	@EmbeddedId
	protected QwtmtWagetableRefTablePK qwtmtWagetableRefTablePK;

	/** The ref table name. */
	@Basic(optional = false)
	@Column(name = "REF_TABLE_NAME")
	private String refTableName;

	/** The wage ref table. */
	@Basic(optional = false)
	@Column(name = "WAGE_REF_TABLE")
	private String wageRefTable;

	/** The wage ref field. */
	@Basic(optional = false)
	@Column(name = "WAGE_REF_FIELD")
	private String wageRefField;

	/** The wage ref disp field. */
	@Basic(optional = false)
	@Column(name = "WAGE_REF_DISP_FIELD")
	private String wageRefDispField;

	/** The wage person table. */
	@Basic(optional = false)
	@Column(name = "WAGE_PERSON_TABLE")
	private String wagePersonTable;

	/** The wage person field. */
	@Basic(optional = false)
	@Column(name = "WAGE_PERSON_FIELD")
	private String wagePersonField;

	/** The wage ref query. */
	@Column(name = "WAGE_REF_QUERY")
	private String wageRefQuery;

	/** The wage person query. */
	@Column(name = "WAGE_PERSON_QUERY")
	private String wagePersonQuery;

	/**
	 * Instantiates a new qwtmt wagetable ref table.
	 */
	public QwtmtWagetableRefTable() {
		super();
	}

	/**
	 * Instantiates a new qwtmt wagetable ref table.
	 *
	 * @param qwtmtWagetableRefTablePK
	 *            the qwtmt wagetable ref table PK
	 */
	public QwtmtWagetableRefTable(QwtmtWagetableRefTablePK qwtmtWagetableRefTablePK) {
		this.qwtmtWagetableRefTablePK = qwtmtWagetableRefTablePK;
	}

	/**
	 * Instantiates a new qwtmt wagetable ref table.
	 *
	 * @param ccd
	 *            the ccd
	 * @param refTableNo
	 *            the ref table no
	 */
	public QwtmtWagetableRefTable(String ccd, String refTableNo) {
		this.qwtmtWagetableRefTablePK = new QwtmtWagetableRefTablePK(ccd, refTableNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qwtmtWagetableRefTablePK != null ? qwtmtWagetableRefTablePK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QwtmtWagetableRefTable)) {
			return false;
		}
		QwtmtWagetableRefTable other = (QwtmtWagetableRefTable) object;
		if ((this.qwtmtWagetableRefTablePK == null && other.qwtmtWagetableRefTablePK != null)
				|| (this.qwtmtWagetableRefTablePK != null
						&& !this.qwtmtWagetableRefTablePK.equals(other.qwtmtWagetableRefTablePK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qwtmtWagetableRefTablePK;
	}

}
