package nts.uk.ctx.at.function.ac.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoImport;
import nts.uk.ctx.at.record.pub.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoEx;
import nts.uk.ctx.at.record.pub.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoPub;

@Stateless
public class ErrMessageInfoAcFinder implements ErrMessageInfoAdapter {

	@Inject
	private ErrMessageInfoPub errMessageInfoPub;

	@Override
	public List<ErrMessageInfoImport> getAllErrMessageInfoByID(String empCalAndSumExecLogID, int executionContent) {
		List<ErrMessageInfoEx> datas = errMessageInfoPub.getAllErrMessageInfoByID(empCalAndSumExecLogID,
				executionContent);
		return datas.stream().map(c -> convertToExport(c)).collect(Collectors.toList());
	}

	@Override
	public List<ErrMessageInfoImport> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID) {
		List<ErrMessageInfoEx> datas = errMessageInfoPub.getAllErrMessageInfoByEmpID(empCalAndSumExecLogID);
		return datas.stream().map(c -> convertToExport(c)).collect(Collectors.toList());
	}

	@Override
	public List<ErrMessageInfoImport> getErrMessageInfoByExecutionContent(String empCalAndSumExecLogID,
			int executionContent) {
		List<ErrMessageInfoEx> datas = errMessageInfoPub.getErrMessageInfoByExecutionContent(empCalAndSumExecLogID,
				executionContent);
		return datas.stream().map(c -> convertToExport(c)).collect(Collectors.toList());
	}

	@Override
	public void add(ErrMessageInfoImport errMessageInfo) {
		errMessageInfoPub.add(convertToImport(errMessageInfo));

	}

	@Override
	public void addList(List<ErrMessageInfoImport> errMessageInfos) {
		errMessageInfoPub.addList(errMessageInfos.stream().map(c -> convertToImport(c)).collect(Collectors.toList()));

	}

	private ErrMessageInfoEx convertToImport(ErrMessageInfoImport imp) {
		return new ErrMessageInfoEx(imp.getEmployeeID(), imp.getEmpCalAndSumExecLogID(), imp.getResourceID(),
				imp.getExecutionContent(), imp.getDisposalDay(), imp.getMessageError());
	}

	private ErrMessageInfoImport convertToExport(ErrMessageInfoEx ex) {
		return new ErrMessageInfoImport(ex.getEmployeeID(), ex.getEmpCalAndSumExecLogID(), ex.getResourceID(),
				ex.getExecutionContent(), ex.getDisposalDay(), ex.getMessageError());
	}

}
