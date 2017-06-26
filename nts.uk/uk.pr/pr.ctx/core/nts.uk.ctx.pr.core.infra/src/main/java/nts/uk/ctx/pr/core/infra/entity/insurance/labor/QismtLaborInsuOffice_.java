/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.labor;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QismtLaborInsuOffice_.
 */
@StaticMetamodel(QismtLaborInsuOffice.class)
public class QismtLaborInsuOffice_ {

	/** The qismt social insu office PK. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, QismtLaborInsuOfficePK> qismtLaborInsuOfficePK;

	/** The ins date. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, Long> exclusVer;

	/** The li office name. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> liOfficeName;

	/** The li office ab name. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> liOfficeAbName;

	/** The president name. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> presidentName;

	/** The president title. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> presidentTitle;

	/** The postal. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> postal;

	/** The address 1. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> address1;

	/** The address 2. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> address2;

	/** The kn address 1. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> knAddress1;

	/** The kn address 2. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> knAddress2;

	/** The tel no. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> telNo;

	/** The city sign. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> citySign;

	/** The office mark. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> officeMark;

	/** The office no A. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> officeNoA;

	/** The office no B. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> officeNoB;

	/** The office no C. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, Character> officeNoC;

	/** The memo. */
	public static volatile SingularAttribute<QismtLaborInsuOffice, String> memo;

}