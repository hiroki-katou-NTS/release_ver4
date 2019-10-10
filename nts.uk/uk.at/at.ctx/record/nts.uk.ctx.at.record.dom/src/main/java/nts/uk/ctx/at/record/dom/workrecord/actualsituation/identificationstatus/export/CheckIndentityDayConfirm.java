package nts.uk.ctx.at.record.dom.workrecord.actualsituation.identificationstatus.export;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhnx 対象日の本人確認が済んでいるかチェックする
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CheckIndentityDayConfirm {

	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepository;

	@Inject
	private IdentificationRepository identificationRepository;

	public boolean checkIndentityDay(String employeeId, List<GeneralDate> dates) {
		// TODO 対応するドメインモデル「本人確認処理の利用設定」を取得する
		String companyId = AppContexts.user().companyId();
		Optional<IdentityProcessUseSet> indentityOpt = identityProcessUseSetRepository.findByKey(companyId);
		if (indentityOpt.isPresent() && !indentityOpt.get().isUseConfirmByYourself())
			return true;
		// 対応するドメインモデル「日の本人確認」がすべて存在するかチェックする
		List<Identification> indentitys = identificationRepository.findByEmployeeID(employeeId, dates);
		Set<GeneralDate> dateProcess = indentitys.stream().map(x -> x.getProcessingYmd()).collect(Collectors.toSet());
		if (dateProcess.size() == dates.size())
			return true;
		return false;
	}
}
