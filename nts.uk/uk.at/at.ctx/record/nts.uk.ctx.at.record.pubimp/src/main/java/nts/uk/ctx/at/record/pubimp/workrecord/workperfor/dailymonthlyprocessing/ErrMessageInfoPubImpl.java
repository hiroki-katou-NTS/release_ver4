package nts.uk.ctx.at.record.pubimp.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.pub.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoEx;
import nts.uk.ctx.at.record.pub.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoPub;

@Stateless
public class ErrMessageInfoPubImpl implements ErrMessageInfoPub {

	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;

	@Override
	public List<ErrMessageInfoEx> getAllErrMessageInfoByID(String empCalAndSumExecLogID, int executionContent) {
		return errMessageInfoRepository.getAllErrMessageInfoByID(empCalAndSumExecLogID, executionContent).stream()
				.map(c -> convertToDomain(c)).collect(Collectors.toList());
	}

	@Override
	public List<ErrMessageInfoEx> getAllErrMessageInfoByEmpID(String empCalAndSumExecLogID) {
		return errMessageInfoRepository.getAllErrMessageInfoByEmpID(empCalAndSumExecLogID).stream()
				.map(c -> convertToDomain(c)).collect(Collectors.toList());
	}

	@Override
	public List<ErrMessageInfoEx> getErrMessageInfoByExecutionContent(String empCalAndSumExecLogID,
			int executionContent) {
		return errMessageInfoRepository.getErrMessageInfoByExecutionContent(empCalAndSumExecLogID, executionContent)
				.stream().map(c -> convertToDomain(c)).collect(Collectors.toList());
	}

	@Override
	public void add(ErrMessageInfoEx errMessageInfo) {
		errMessageInfoRepository.add(convertToExport(errMessageInfo));
	}

	@Override
	public void addList(List<ErrMessageInfoEx> errMessageInfos) {
		errMessageInfoRepository
				.addList(errMessageInfos.stream().map(c -> convertToExport(c)).collect(Collectors.toList()));

	}

	private ErrMessageInfoEx convertToDomain(ErrMessageInfo domain) {
		return new ErrMessageInfoEx(domain.getEmployeeID(), domain.getEmpCalAndSumExecLogID(),
				domain.getResourceID().v(), domain.getExecutionContent().value, domain.getDisposalDay(),
				domain.getMessageError().v());
	}

	private ErrMessageInfo convertToExport(ErrMessageInfoEx ex) {
		return new ErrMessageInfo(ex.getEmployeeID(), ex.getEmpCalAndSumExecLogID(),
				new ErrMessageResource(ex.getResourceID()),
				EnumAdaptor.valueOf(ex.getExecutionContent(), ExecutionContent.class), ex.getDisposalDay(),
				new ErrMessageContent(ex.getMessageError()));
	}

}
