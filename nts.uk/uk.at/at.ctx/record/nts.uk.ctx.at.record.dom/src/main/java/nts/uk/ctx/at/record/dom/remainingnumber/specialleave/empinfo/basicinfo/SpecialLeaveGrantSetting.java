package nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.basicinfo;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.base.PerServiceLengthTableCD;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialLeaveGrantSetting {
	
	// 付与基準日
	private GeneralDate grantDate;
	
	// 付与日数
	private Optional<GrantNumber> grantDays;
	
	// 付与テーブル
	private Optional<PerServiceLengthTableCD> grantTable;
	
	public SpecialLeaveGrantSetting(GeneralDate grantDate, Integer grantDays, String grantTbl){
		this.grantDate = grantDate;
		this.grantDays = Optional.ofNullable(new GrantNumber(grantDays));
		this.grantTable = Optional.ofNullable(new PerServiceLengthTableCD(grantTbl));
	}
	
}
