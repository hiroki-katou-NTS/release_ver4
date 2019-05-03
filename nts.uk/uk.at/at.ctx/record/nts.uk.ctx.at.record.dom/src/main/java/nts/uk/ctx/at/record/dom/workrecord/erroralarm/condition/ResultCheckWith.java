package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultCheckWith {
	private boolean check;
	
	private String result;
}
