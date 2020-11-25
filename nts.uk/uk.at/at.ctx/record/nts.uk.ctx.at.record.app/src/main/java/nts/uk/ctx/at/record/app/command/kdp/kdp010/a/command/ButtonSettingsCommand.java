package nts.uk.ctx.at.record.app.command.kdp.kdp010.a.command;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.AudioType;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonPositionNo;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonSettings;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 
 * @author thanhpv
 *
 */
@Data
@NoArgsConstructor
public class ButtonSettingsCommand {

	private Integer buttonPositionNo;

	private ButtonDisSetCommand buttonDisSet;

	private ButtonTypeCommand buttonType;

	private Integer usrArt;

	private Integer audioType;

	public ButtonSettings toDomain() {
		return new ButtonSettings(new ButtonPositionNo(this.buttonPositionNo), this.buttonDisSet.toDomain(), this.buttonType.toDomain(), NotUseAtr.valueOf(this.usrArt), AudioType.valueOf(this.audioType));
	}

}