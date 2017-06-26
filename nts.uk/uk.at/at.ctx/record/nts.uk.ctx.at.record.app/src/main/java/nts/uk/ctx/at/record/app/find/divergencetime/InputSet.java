package nts.uk.ctx.at.record.app.find.divergencetime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class InputSet {
	
	private int selectUseSet;
	
	private int cancelErrSelReason;
	
	public static InputSet convertType(int selectUseSet,int cancelErrSelReason){
		return new InputSet(selectUseSet,cancelErrSelReason);
	}
}
