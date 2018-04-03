package nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.add;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;

@Getter
public class AddSpecialleave17informationCommand {

	@PeregEmployeeId
	private String sID;
	
	//	特別休暇付与基準日
	@PeregItem("IS00601")
	private GeneralDate grantDate;
	
	//	特別休暇管理
	@PeregItem("IS00602")
	private int useAtr;
	
	//	付与設定
	@PeregItem("IS00603")
	private int appSet;
	
	//	付与日数
	@PeregItem("IS00604")
	private Integer grantDays;
	
	//	付与テーブル
	@PeregItem("IS00605")
	private String grantTable;
	
	//	次回付与日
	@PeregItem("IS00606")
	private String nextGrantDate;

	//	特別休暇残数
	@PeregItem("IS00607")
	private String spHDRemain;

}
