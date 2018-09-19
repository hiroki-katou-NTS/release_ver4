package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.PerServiceLengthTableCD;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialLeaveGrantSetting {
	
	// 付与基準日
	private GeneralDate grantDate;
	
	// 付与日数
	private Optional<GrantNumber> grantDays;
	
	// 26.4.2018 - EA 修正履歴　履歴＃1475 勤続年数付与テーブル
	private Optional<PerServiceLengthTableCD> grantTable;
	
	public SpecialLeaveGrantSetting(GeneralDate grantDate, Integer grantDays, String grantTbl){
		this.grantDate = grantDate;
		this.grantDays = Optional.ofNullable(grantDays == null ? null : new GrantNumber(grantDays));
		this.grantTable = Optional.ofNullable(new PerServiceLengthTableCD(grantTbl));
	}
	
}
