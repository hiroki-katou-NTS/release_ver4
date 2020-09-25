/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.monthly.standardtime.workplace;


import lombok.Getter;

/**
 * 選択した職場の目安時間設定を別の職場へ複写する
 */
@Getter
public class CopyTimeWorkplaceCommand {

	// 会社ID 1
	private  String workplaceId;

	// 労働制 3
	private int laborSystemAtr;

}
