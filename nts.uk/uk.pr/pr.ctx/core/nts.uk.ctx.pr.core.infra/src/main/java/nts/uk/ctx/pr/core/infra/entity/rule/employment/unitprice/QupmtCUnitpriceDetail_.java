/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class QupmtCUnitpriceHist_.
 */
@StaticMetamodel(QupmtCUnitpriceDetail.class)
public class QupmtCUnitpriceDetail_ {

	/** The qupmt C unitprice detail PK. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, QupmtCUnitpriceDetailPK> qupmtCUnitpriceDetailPK;

	/** The ins date. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Long> exclusVer;

	/** The str ym. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> strYm;

	/** The end ym. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> endYm;

	/** The budget. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, BigDecimal> budget;

	/** The fix pay set. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> fixPaySet;

	/** The fix pay atr. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> fixPayAtr;

	/** The fix pay atr monthly. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> fixPayAtrMonthly;

	/** The fix pay atr daymonth. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> fixPayAtrDaymonth;

	/** The fix pay atr daily. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> fixPayAtrDaily;

	/** The fix pay atr hourly. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, Integer> fixPayAtrHourly;

	/** The memo. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, String> memo;

	/** The qupmt C unitprice header. */
	public static volatile SingularAttribute<QupmtCUnitpriceDetail, QupmtCUnitpriceHeader> qupmtCUnitpriceHeader;
}