package nts.uk.ctx.at.record.pub.workinformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonTimeSheet {

	private int no;
	
	private Integer time;
	
	private Integer tranferTime;
}
