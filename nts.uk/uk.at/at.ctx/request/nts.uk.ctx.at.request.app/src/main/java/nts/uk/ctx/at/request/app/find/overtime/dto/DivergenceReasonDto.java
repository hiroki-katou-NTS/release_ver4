package nts.uk.ctx.at.request.app.find.overtime.dto;

import lombok.Data;

@Data
public class DivergenceReasonDto {
	/**
	 * divergenceReasonID
	 */
	private String divergenceReasonID;
	/**
	 * reasonTemp
	 */
	private String reasonTemp;
	
	private String divergenceReasonIdDefault;
}
