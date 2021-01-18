package nts.uk.ctx.at.shared.app.workrule.workinghours;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContainsResultDto {

	// 含まれているか true:含まれている/false:含まれていない
	private boolean check;
	
	// 時間帯
	private TimeSpanForCalcSharedDto timeSpan;
	
	private String nameError;
	
	private String timeInput;
	
	private boolean isWorkNo1;
}
