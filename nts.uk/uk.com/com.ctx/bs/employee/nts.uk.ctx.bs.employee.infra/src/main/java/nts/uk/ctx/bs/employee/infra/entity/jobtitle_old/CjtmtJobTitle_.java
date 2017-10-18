/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.jobtitle_old;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.arc.time.GeneralDate;

/**
 * The Class CjtmtJobTitle_.
 */
@StaticMetamodel(CjtmtJobTitle.class)
public class CjtmtJobTitle_ {

	/** The cjtmt job title PK. */
	public static volatile SingularAttribute<CjtmtJobTitle, CjtmtJobTitlePK> cjtmtJobTitlePK;

	/** The job name. */
	public static volatile SingularAttribute<CjtmtJobTitle, String> jobName;

	/** The sequence code. */
	public static volatile SingularAttribute<CjtmtJobTitle, String> sequenceCode;

	/** The end date. */
	public static volatile SingularAttribute<CjtmtJobTitle, GeneralDate> endDate;

	/** The start date. */
	public static volatile SingularAttribute<CjtmtJobTitle, GeneralDate> startDate;

	/** The csqmt sequence master. */
	public static volatile SingularAttribute<CjtmtJobTitle, CsqmtSequenceMaster> csqmtSequenceMaster;
}
