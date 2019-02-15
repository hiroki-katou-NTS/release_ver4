package nts.uk.ctx.at.record.app.find.workrecord.erroralarm.otkcustomize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtkWorkTypeDto {
	/** The work type code. */
	private String code;
	
	/** The name. */
	private String name;
	
	/** The deprecate. */
	private Integer deprecate;

}
