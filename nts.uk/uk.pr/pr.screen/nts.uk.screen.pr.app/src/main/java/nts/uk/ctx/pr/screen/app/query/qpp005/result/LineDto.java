package nts.uk.ctx.pr.screen.app.query.qpp005.result;

import java.util.List;

import lombok.Value;

@Value
public class LineDto {

	/**
	 * 行
	 */
	int linePosition;

	List<DetailItemDto> details;

	public static LineDto fromDomain(int linePosition, List<DetailItemDto> details) {
		return new LineDto(linePosition, details);
	}
}
