package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrorMessageInfo;

@Getter
@Setter
@NoArgsConstructor
public class OutputAcquireReflectEmbossingNew {
	
	List<ErrorMessageInfo> listErrorMessageInfo = new ArrayList<>();
	
	List<Stamp> listStamp = new ArrayList<>();

	public OutputAcquireReflectEmbossingNew(List<ErrorMessageInfo> listErrorMessageInfo, List<Stamp> listStamp) {
		super();
		this.listErrorMessageInfo = listErrorMessageInfo;
		this.listStamp = listStamp;
	}
}
