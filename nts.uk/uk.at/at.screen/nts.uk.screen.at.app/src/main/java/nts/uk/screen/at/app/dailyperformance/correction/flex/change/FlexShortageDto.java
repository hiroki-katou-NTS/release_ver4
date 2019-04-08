package nts.uk.screen.at.app.dailyperformance.correction.flex.change;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.flex.CalcFlexChangeDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.DPMonthParent;
import nts.uk.shr.com.i18n.TextResource;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlexShortageDto {

	// フレックス不足時間
	private ItemValue value18;
	private ItemValue value19;
	private ItemValue value21;
	private ItemValue value189;
	private ItemValue value190;
	private ItemValue value191;

	private boolean canflex;
	private boolean showFlex;
	private DPMonthParent monthParent;
	// private boolean retiredFlag;

	private CalcFlexChangeDto calc;
	private String redConditionMessage;
	private String messageNotForward;

	private boolean error;
	private List<MessageError> messageError = new ArrayList<>();

	public FlexShortageDto(ItemValue value18, ItemValue value19, ItemValue value21, ItemValue value189,
			ItemValue value190, ItemValue value191) {
		this.value18 = value18;
		this.value19 = value19;
		this.value21 = value21;
		this.value189 = value189;
		this.value190 = value190;
		this.value191 = value191;
	}

	public FlexShortageDto createCanFlex(boolean flex) {
		this.canflex = flex;
		return this;
	}

	public FlexShortageDto createShowFlex(boolean flex) {
		this.showFlex = flex;
		return this;
	}

	public FlexShortageDto createError(List<ErrorFlexMonthDto> errorFlexs) {
		redConditionMessage = redConditionMessage == null ? "" : redConditionMessage;
		if (errorFlexs.isEmpty()) {
			this.error = false;
			return this;
		} else {
			this.error = true;
			errorFlexs.stream().forEach(errorFlex -> {
				if (errorFlex.flex != null) {
					this.messageError.add(messageErrorId(errorFlex.flex.intValue()));
				}

				if (errorFlex.annualHoliday != null) {
					this.messageError.add(new MessageError(TextResource.localize("Msg_1292", redConditionMessage),"Msg_1292"));
				}
			});
			return this;
		}
	}

	private MessageError messageErrorId(int value) {
		switch (value) {
		case 0:
			return new MessageError(TextResource.localize("Msg_1174", redConditionMessage), "Msg_1174");
		case 1:
			return new MessageError(TextResource.localize("Msg_1175", redConditionMessage), "Msg_1175");
		case 2:
			return new MessageError(TextResource.localize("Msg_1291", redConditionMessage), "Msg_1291");
		case 3:
			return new MessageError(TextResource.localize("Msg_1448"), "Msg_1448");
		default:
			return null;
		}
	}

	public FlexShortageDto createMonthParent(DPMonthParent dPMonthParent) {
		this.monthParent = dPMonthParent;
		return this;
	}

	// public FlexShortageDto createRetiredFlag(boolean flag){
	// this.retiredFlag = flag;
	// return this;
	// }

	public FlexShortageDto createCalcFlex(CalcFlexChangeDto calc) {
		this.calc = calc;
		return this;
	}

	public FlexShortageDto createRedConditionMessage(String condition) {
		this.redConditionMessage = condition;
		return this;
	}

	public FlexShortageDto createNotForward(String message) {
		this.messageNotForward = message;
		return this;
	}
	
	@AllArgsConstructor
	@Data
	public class MessageError{
		private String message;
		private String messageId;
	}
}

