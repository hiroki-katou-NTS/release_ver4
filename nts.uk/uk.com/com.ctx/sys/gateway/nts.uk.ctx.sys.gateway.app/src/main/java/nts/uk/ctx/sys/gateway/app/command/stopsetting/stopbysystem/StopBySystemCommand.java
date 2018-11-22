package nts.uk.ctx.sys.gateway.app.command.stopsetting.stopbysystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StopBySystemCommand {
	/** 契約コード */
	private String contractCd;

	/** 利用停止モード */
	private Integer usageStopMode;

	/** 利用停止のメッセージ */
	private String usageStopMessage;

	/** システム利用状態 */
	private Integer systemStatus;

	/** 停止予告のメッセージ */
	private String stopMessage;

}
