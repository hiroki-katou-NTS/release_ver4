/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.env.app.find.mailnoticeset.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.sys.env.app.find.mailnoticeset.company.dto.MailDestinationFunctionDto;

/**
 * The Class SettingDataDto.
 */
@Getter
@Setter
@AllArgsConstructor
public class SettingDataDto {

	/** The mail function dto. */
	public List<MailFunctionDto> mailFunctionDto;
	
	/** The mail destination function dto. */
	public MailDestinationFunctionDto mailDestinationFunctionDto;
	
}
