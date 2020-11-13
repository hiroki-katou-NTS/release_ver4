package nts.uk.ctx.sys.portal.app.command.toppage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopPageNewBaseCommand {
	
	/** コード */
	private String topPageCode;
	/** レイアウトの表示種類 */
	private Integer layoutDisp;
	/** 会社ID */
	private String cid;
	/** 名称 */
	private String topPageName;
}
