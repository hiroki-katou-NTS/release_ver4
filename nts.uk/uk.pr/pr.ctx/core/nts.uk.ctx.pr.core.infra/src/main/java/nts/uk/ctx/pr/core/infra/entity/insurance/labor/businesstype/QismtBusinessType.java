/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor.businesstype;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsu;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QismtBusinessType.
 */
@Getter
@Setter
@Entity
@Table(name = "QISMT_BUSINESS_TYPE")
public class QismtBusinessType extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ccd. */
	@Id
	@Basic(optional = false)
	@Column(name = "CCD")
	private String ccd;

	/** The biz name 01. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME01")
	private String bizName01;

	/** The biz name 02. */
	@Column(name = "BIZ_NAME02")
	private String bizName02;

	/** The biz name 03. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME03")
	private String bizName03;

	/** The biz name 04. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME04")
	private String bizName04;

	/** The biz name 05. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME05")
	private String bizName05;

	/** The biz name 06. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME06")
	private String bizName06;

	/** The biz name 07. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME07")
	private String bizName07;

	/** The biz name 08. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME08")
	private String bizName08;

	/** The biz name 09. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME09")
	private String bizName09;

	/** The biz name 10. */
	@Basic(optional = false)
	@Column(name = "BIZ_NAME10")
	private String bizName10;

	/** The qismt work accident insu collection. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qismtBusinessType")
	private Collection<QismtWorkAccidentInsu> qismtWorkAccidentInsuCollection;

	/**
	 * Instantiates a new qismt business type.
	 */
	public QismtBusinessType() {
		super();
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
		if (!(object instanceof QismtBusinessType)) {
			return false;
		}
		QismtBusinessType other = (QismtBusinessType) object;
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
