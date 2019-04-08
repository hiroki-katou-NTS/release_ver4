package nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.shr.com.i18n.TextResource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlexShortageRCDto {

	private boolean error;
	private String redConditionMessage;
	private String flexHoliday;
	private List<RCMessageErrorDaily> messageError = new ArrayList<>();
	private ItemFlex dataCalc;
	private long version;
	
	public FlexShortageRCDto createError(boolean error) {
		this.error = error;
		return this;
	}
	
	public FlexShortageRCDto createMessage(String redConditionMessage, String flexHoliday) {
		this.redConditionMessage = redConditionMessage;
		this.flexHoliday = flexHoliday;
		return this;
	}
	
	public FlexShortageRCDto createDataCalc(ItemFlex dataCalc) {
		this.dataCalc = dataCalc;
		return this;
	}
	
	public FlexShortageRCDto createError(Optional<EmployeeMonthlyPerError> errorFlex) {
		if (!errorFlex.isPresent()) {
			this.error = false;
			return this;
		} else {
			this.error = true;
			if (errorFlex.get().getFlex().isPresent()) {
				this.messageError.add(messageErrorId(errorFlex.get().getFlex().get().value));
			}

			if (errorFlex.get().getAnnualHoliday().isPresent()) {
				this.messageError.add(new RCMessageErrorDaily(TextResource.localize("Msg_1292", flexHoliday),"Msg_1292"));
			}
			return this;
		}
	}

	private RCMessageErrorDaily messageErrorId(int value) {
		switch (value) {
		case 0:
			return new RCMessageErrorDaily(TextResource.localize("Msg_1174", redConditionMessage), "Msg_1174");
		case 1:
			return new RCMessageErrorDaily(TextResource.localize("Msg_1175", redConditionMessage), "Msg_1175");
		case 2:
			return new RCMessageErrorDaily(TextResource.localize("Msg_1291", flexHoliday), "Msg_1291");
		case 3:
			return new RCMessageErrorDaily(TextResource.localize("Msg_1448"), "Msg_1448");
		default:
			return null;
		}
	}

	public FlexShortageRCDto(boolean error, String redConditionMessage, String flexHoliday,
			List<RCMessageErrorDaily> messageError, ItemFlex dataCalc) {
		super();
		this.error = error;
		this.redConditionMessage = redConditionMessage;
		this.flexHoliday = flexHoliday;
		this.messageError = messageError;
		this.dataCalc = dataCalc;
	}

}
