package nts.uk.ctx.at.request.dom.setting.company.displayname;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
/**
 * 申請表示名設定
 * @author yennth
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppDispName extends AggregateRoot{
	// 会社ID
	private String companyId;       
	// 申請種類
	private ApplicationType appType;
	// 表示名
	private DispName dispName;
	public static AppDispName createFromJavaType(String companyId, int appType, String dispName){
		return new AppDispName(companyId, EnumAdaptor.valueOf(appType, ApplicationType.class), 
				new DispName(dispName));
	}
}
