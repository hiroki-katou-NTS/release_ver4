package nts.uk.ctx.at.function.dom.adapter.worklocation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
@NoArgsConstructor
public class RecordWorkInfoFunAdapterDto {
	private String employeeId;
	
	private GeneralDate workingDate;
	
	private String workTimeCode;
	
	private String workTypeCode;

	public RecordWorkInfoFunAdapterDto(String employeeId, String workTimeCode, String workTypeCode, GeneralDate workingDate) {
		super();
		this.employeeId = employeeId;
		this.workTimeCode = workTimeCode;
		this.workTypeCode = workTypeCode;
		this.workingDate = workingDate;
	}
	
	
}
