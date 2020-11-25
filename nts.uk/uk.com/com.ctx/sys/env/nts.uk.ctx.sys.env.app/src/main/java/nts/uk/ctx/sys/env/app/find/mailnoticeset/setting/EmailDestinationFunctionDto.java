package nts.uk.ctx.sys.env.app.find.mailnoticeset.setting;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Dto メール送信先機能
 */
@Data
@Builder
public class EmailDestinationFunctionDto {
	/**
	 * メール分類
	 */
	private Integer emailClassification;

	/**
	 * 機能ID
	 */
	private List<Integer> functionIds;
}