package nts.uk.ctx.at.record.app.command.remainingnumber.empinfo.basicinfo.update;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;

@Getter
public class UpdateSpecialleave18informationCommand {
	@PeregEmployeeId
	private String sID;
	//	特別休暇付与基準日
	@PeregItem("IS00608")
	private GeneralDate grantDate;
	
	//	特別休暇管理
	@PeregItem("IS00609")
	private int useAtr;
	
	//	付与設定
	@PeregItem("IS00610")
	private int appSet;
	
	//	付与日数
	@PeregItem("IS00611")
	private Integer grantDays;
	
	//	付与テーブル
	@PeregItem("IS00612")
	private String grantTable;
	
	//	次回付与日
	@PeregItem("IS00613")
	private String nextGrantDate;

	//	特別休暇残数
	@PeregItem("IS00614")
	private String spHDRemain;

}
