package nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.add;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;

@Getter
public class AddSpecialleave15informationCommand {

	@PeregEmployeeId
	private String sID;
	
	//	特別休暇付与基準日
	@PeregItem("IS00587")
	private GeneralDate grantDate;
	
	//	特別休暇管理
	@PeregItem("IS00588")
	private int useAtr;
	
	//	付与設定
	@PeregItem("IS00589")
	private int appSet;
	
	//	付与日数
	@PeregItem("IS00590")
	private Integer grantDays;
	
	//	付与テーブル
	@PeregItem("IS00591")
	private String grantTable;
	
	//	次回付与日
	@PeregItem("IS00592")
	private String nextGrantDate;

	//	特別休暇残数
	@PeregItem("IS00593")
	private String spHDRemain;

}
