package nts.uk.pr.file.infra.paymentdata.result;

import java.util.List;

import lombok.Value;

@Value
public class LayoutMasterCategoryDto {
	/** カテゴリ区分 */
	int categoryAttribute;

	String categoryName;

	/** カテゴリ表示位置 */
	int categoryPosition;

	int lineCounts;

	List<LineDto> lines;

	public static LayoutMasterCategoryDto fromDomain(int categoryAttribute, String categoryName, int categoryPosition,
			int lineCounts, List<LineDto> lines) {
		return new LayoutMasterCategoryDto(categoryAttribute, categoryName, categoryPosition, lineCounts, lines);
	}
}
