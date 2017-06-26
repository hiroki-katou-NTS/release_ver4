/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.postcode.find;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class PostCode.
 */

@Getter
@Setter
public class PostCode {

	/** The id. */
	private String id;

	/** The local gov code. */
	private String localGovCode;

	/** The postcode. */
	private String postcode;

	/** The prefecture name kn. */
	private String prefectureNameKn;

	/** The municipality name kn. */
	private String municipalityNameKn;

	/** The town name kn. */
	private String townNameKn;

	/** The prefecture name. */
	private String prefectureName;

	/** The municipality name. */
	private String municipalityName;

	/** The town name. */
	private String townName;

}
