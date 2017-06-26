package nts.uk.ctx.at.record.dom.divergencetime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
@Getter
@AllArgsConstructor
public class InputSetting extends DomainObject{

	private UseSetting selectUseSet;
	
	private UseSetting cancelErrSelReason;
	
	public static InputSetting convert(int selectUseSet,
			int cancelErrSelReason){
		return new InputSetting(
				EnumAdaptor.valueOf(selectUseSet, UseSetting.class),
				EnumAdaptor.valueOf(cancelErrSelReason,UseSetting.class));
	}
}
