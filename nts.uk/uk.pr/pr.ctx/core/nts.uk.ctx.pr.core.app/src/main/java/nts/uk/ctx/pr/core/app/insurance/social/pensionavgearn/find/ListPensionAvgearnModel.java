/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * The Class ListPensionAvgearnModel.
 */
@Data
@Builder
public class ListPensionAvgearnModel {

	/** The list pension avgearn dto. */
	private List<PensionAvgearnDto> listPensionAvgearnDto;

	/** The history id. */
	private String historyId;
}
