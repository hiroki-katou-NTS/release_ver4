package nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.add;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;

@Getter
public class AddSpecialleave9informationCommand {

	@PeregEmployeeId
	private String sID;
	
	//	特別休暇付与基準日
	@PeregItem("IS00351")
	private GeneralDate grantDate;
	
	//	特別休暇管理
	@PeregItem("IS00352")
	private int useAtr;
	
	//	付与設定
	@PeregItem("IS00353")
	private int appSet;
	
	//	付与日数
	@PeregItem("IS00354")
	private Integer grantDays;
	
	//	付与テーブル
	@PeregItem("IS00355")
	private String grantTable;
	
	//	次回付与日
	@PeregItem("IS00356")
	private String nextGrantDate;

	//	特別休暇残数
	@PeregItem("IS00357")
	private String spHDRemain;

}
