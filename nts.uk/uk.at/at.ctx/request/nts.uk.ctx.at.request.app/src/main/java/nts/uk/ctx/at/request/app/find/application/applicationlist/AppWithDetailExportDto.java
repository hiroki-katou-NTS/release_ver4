package nts.uk.ctx.at.request.app.find.application.applicationlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class AppWithDetailExportDto {
	
	/*
	 * 申請種類
	 */
	@Setter
	private Integer appType;
	
	/*
	 * 表示名
	 */
	private String appName;
	
	/*
	 * 残業区分
	 * 1: 早出残業
	 * 2: 通常残業
	 * 3: 早出残業・通常残業 
	 */
	private Integer overtimeAtr;
	
	/*
	 * 打刻申請モード
	 * 
	 * 0: 外出許可
	 * 1: 出退勤漏れ
	 * 2: 打刻取消
	 * 3: レコーダイメージ
	 * 4: その他
	 */
	private Integer stampAtr;
	
}
