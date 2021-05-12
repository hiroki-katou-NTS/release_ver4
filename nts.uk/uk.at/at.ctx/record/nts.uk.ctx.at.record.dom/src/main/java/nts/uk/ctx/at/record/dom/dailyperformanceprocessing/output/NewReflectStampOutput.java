package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;

@Getter
@Setter
@NoArgsConstructor
public class NewReflectStampOutput {
	
	private ReflectStampOutput reflectStampOutput = new ReflectStampOutput();
	
	private List<ErrMessageInfo> errMesInfos = new ArrayList<>();

}
