/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementSettingDto;

/**
 * The Class SettingInfoInModel.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingInfoInModel {

	/** The history id. */
	private String historyId;

	/** The element setting. */
	private List<ElementSettingDto> settings;

}
