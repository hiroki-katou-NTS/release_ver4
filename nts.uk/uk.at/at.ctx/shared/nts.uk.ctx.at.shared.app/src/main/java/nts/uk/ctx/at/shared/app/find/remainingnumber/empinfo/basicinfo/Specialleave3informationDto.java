package nts.uk.ctx.at.shared.app.find.remainingnumber.empinfo.basicinfo;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Getter
@Setter
public class Specialleave3informationDto   extends PeregDomainDto {

	@PeregEmployeeId
	private String sID;
	
	//	特別休暇付与基準日
	@PeregItem("IS00309")
	private GeneralDate grantDate;
	
	//	特別休暇管理
	@PeregItem("IS00310")
	private Integer useAtr;
	
	//	付与設定
	@PeregItem("IS00311")
	private Integer appSet;
	
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
	public static Specialleave3informationDto createFromDomain(SpecialLeaveBasicInfo domain){
		Specialleave3informationDto dto = new Specialleave3informationDto();
		dto.grantDate = domain.getGrantSetting().getGrantDate();
		dto.useAtr = domain.getUsed().value;
		dto.appSet = domain.getApplicationSet().value;
		if (domain.getGrantSetting().getGrantDays().isPresent()){
			dto.grantDays = domain.getGrantSetting().getGrantDays().get().v();
		}
		if (domain.getGrantSetting().getGrantTable().isPresent()){
			dto.grantTable = domain.getGrantSetting().getGrantTable().get().v();
		}
		dto.setRecordId(domain.getSID());
		return dto;
	}
	
	public static Specialleave3informationDto createFromDomain(Object[] domain){
		Specialleave3informationDto dto = new Specialleave3informationDto();
		dto.setRecordId((String)domain[0]);
		dto.useAtr = (Integer) domain[3];
		dto.appSet = (Integer) domain[4];
		dto.grantDate = domain[5] == null? null: (GeneralDate) domain[5];
		dto.grantDays = (Integer)domain[6];
		dto.grantTable = (String)domain[7];
		return dto;
	}
}
