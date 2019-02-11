package nts.uk.ctx.at.request.pub.screen;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class AppWithOvertimeExport {
	
	/*
	 * 申請種類
	 */
	private Integer appType;
	
	/*
	 * 表示名
	 */
	private String appName;
	
	/*
	 * 残業区分
	 */
	private Integer overtimeAtr;
	
}
