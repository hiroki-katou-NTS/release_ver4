package nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.update;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;

@Getter
public class UpdateSpecialleave2informationCommand {
	@PeregEmployeeId
	private String sID;
	//	特別休暇付与基準日
	@PeregItem("IS00309")
	private GeneralDate grantDate;
	
	//	特別休暇管理
	@PeregItem("IS00310")
	private int useAtr;
	
	//	付与設定
	@PeregItem("IS00311")
	private int appSet;
	
	//	付与日数
	@PeregItem("IS00312")
	private Integer grantDays;
	
	//	付与テーブル
	@PeregItem("IS00313")
	private String grantTable;
	
	//	次回付与日
	@PeregItem("IS00314")
	private String nextGrantDate;

	//	特別休暇残数
	@PeregItem("IS00315")
	private String spHDRemain;

}
