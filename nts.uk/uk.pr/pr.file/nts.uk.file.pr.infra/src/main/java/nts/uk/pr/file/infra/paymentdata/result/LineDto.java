package nts.uk.pr.file.infra.paymentdata.result;

import java.util.List;

import lombok.Value;

@Value
public class LineDto {
	/**
	 * 行
	 */
	int linePosition;

	List<DetailItemDto> details;
	
	int lineDispayAttribute;

	public static LineDto fromDomain(int linePosition, List<DetailItemDto> details, int lineDispayAttribute) {
		return new LineDto(linePosition, details, lineDispayAttribute);
	}
}
