package nts.uk.pr.file.infra.paymentdata.result;

import lombok.Value;

@Value
public class PrintPositionCategoryDto {

	/**
	 * 位置
	 */
	private final int categoryAtr;

	/**
	 * 行
	 */
	private final int lines;

	public static PrintPositionCategoryDto fromDomain(int categoryAtr, int lines) {
		return new PrintPositionCategoryDto(categoryAtr, lines);
	}

}
