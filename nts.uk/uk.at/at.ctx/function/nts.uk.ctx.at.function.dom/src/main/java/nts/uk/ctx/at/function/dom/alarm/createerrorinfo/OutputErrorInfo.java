package nts.uk.ctx.at.function.dom.alarm.createerrorinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author tutk
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputErrorInfo {
	/**送信エラーメッセージ*/
	private String error;
	
	/**管理者未設定メッセージ*/
	private String errorWkp;
}
