/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.postcode;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The Class CpcstPostcode_.
 */
@StaticMetamodel(CpcstPostcode.class)
public class CpcstPostcode_ {

	/** The ins date. */
	public static volatile SingularAttribute<CpcstPostcode, Date> insDate;

	/** The ins ccd. */
	public static volatile SingularAttribute<CpcstPostcode, String> insCcd;

	/** The ins scd. */
	public static volatile SingularAttribute<CpcstPostcode, String> insScd;

	/** The ins pg. */
	public static volatile SingularAttribute<CpcstPostcode, String> insPg;

	/** The upd date. */
	public static volatile SingularAttribute<CpcstPostcode, Date> updDate;

	/** The upd ccd. */
	public static volatile SingularAttribute<CpcstPostcode, String> updCcd;

	/** The upd scd. */
	public static volatile SingularAttribute<CpcstPostcode, String> updScd;

	/** The upd pg. */
	public static volatile SingularAttribute<CpcstPostcode, String> updPg;

	/** The exclus ver. */
	public static volatile SingularAttribute<CpcstPostcode, Long> exclusVer;

	/** The id. */
	public static volatile SingularAttribute<CpcstPostcode, Integer> id;

	/** The local gov code. */
	public static volatile SingularAttribute<CpcstPostcode, String> localGovCode;

	/** The postcode. */
	public static volatile SingularAttribute<CpcstPostcode, String> postcode;

	/** The prefecture name kn. */
	public static volatile SingularAttribute<CpcstPostcode, String> prefectureNameKn;

	/** The municipality name kn. */
	public static volatile SingularAttribute<CpcstPostcode, String> municipalityNameKn;

	/** The town name kn. */
	public static volatile SingularAttribute<CpcstPostcode, String> townNameKn;

	/** The prefecture name. */
	public static volatile SingularAttribute<CpcstPostcode, String> prefectureName;

	/** The municipality name. */
	public static volatile SingularAttribute<CpcstPostcode, String> municipalityName;

	/** The town name. */
	public static volatile SingularAttribute<CpcstPostcode, String> townName;

}