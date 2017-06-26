/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealInsuAvgearnD;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmount;

/**
 * The Class QismtHealthInsuRate_.
 */
@StaticMetamodel(QismtHealthInsuRate.class)
public class QismtHealthInsuRate_ {

	/** The ccd. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> ccd;

	/** The si office cd. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> siOfficeCd;

	/** The hist id. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> histId;

	/** The ins date. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<QismtHealthInsuRate, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Long> exclusVer;

	/** The str ym. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Integer> strYm;

	/** The end ym. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Integer> endYm;

	/** The p pay general rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pPayGeneralRate;

	/** The p pay nursing rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pPayNursingRate;

	/** The p pay specific rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pPaySpecificRate;

	/** The p pay basic rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pPayBasicRate;

	/** The p pay health round atr. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Integer> pPayHealthRoundAtr;

	/** The c pay general rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cPayGeneralRate;

	/** The c pay nursing rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cPayNursingRate;

	/** The c pay specific rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cPaySpecificRate;

	/** The c pay basic rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cPayBasicRate;

	/** The c pay health round atr. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Integer> cPayHealthRoundAtr;

	/** The p bns general rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pBnsGeneralRate;

	/** The p bns nursing rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pBnsNursingRate;

	/** The p bns specific rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pBnsSpecificRate;

	/** The p bns basic rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> pBnsBasicRate;

	/** The p bns health round atr. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Integer> pBnsHealthRoundAtr;

	/** The c bns general rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cBnsGeneralRate;

	/** The c bns nursing rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cBnsNursingRate;

	/** The c bns specific rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cBnsSpecificRate;

	/** The c bns basic rate. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> cBnsBasicRate;

	/** The c bns health round atr. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Integer> cBnsHealthRoundAtr;

	/** The bonus health max mny. */
	public static volatile SingularAttribute<QismtHealthInsuRate, BigDecimal> bonusHealthMaxMny;

	/** The keep entry flg. */
	public static volatile SingularAttribute<QismtHealthInsuRate, Integer> keepEntryFlg;

	/** The qismt heal insu avgearn D list. */
	public static volatile ListAttribute<QismtHealthInsuRate, QismtHealInsuAvgearnD> qismtHealInsuAvgearnDList;
	
	/** The qismt health insu amount list. */
	public static volatile ListAttribute<QismtHealthInsuRate, QismtHealthInsuAmount> qismtHealthInsuAmountList;

}