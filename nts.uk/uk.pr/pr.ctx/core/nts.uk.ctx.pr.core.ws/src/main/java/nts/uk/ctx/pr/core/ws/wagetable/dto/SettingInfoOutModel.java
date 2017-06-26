/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.dto;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementSettingDto;

/**
 * The Class SettingInfoOutModel.
 */
@Data
public class SettingInfoOutModel {

	/** The element settings. */
	private List<ElementSettingDto> elementSettings;

}
	