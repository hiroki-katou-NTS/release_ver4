package nts.uk.shr.find.postcode;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
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
