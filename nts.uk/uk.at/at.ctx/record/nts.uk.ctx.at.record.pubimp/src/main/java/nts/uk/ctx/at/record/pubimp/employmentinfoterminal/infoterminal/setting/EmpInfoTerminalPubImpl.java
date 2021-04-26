package nts.uk.ctx.at.record.pubimp.employmentinfoterminal.infoterminal.setting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalCode;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.repo.EmpInfoTerminalRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.pub.employmentinfoterminal.infoterminal.setting.EmpInfoTerminalPub;
import nts.uk.ctx.at.record.pub.employmentinfoterminal.infoterminal.setting.dto.EmpInfoTerminalExport;
import nts.uk.ctx.at.record.pub.employmentinfoterminal.infoterminal.setting.dto.EmpInfoTerminalExport.EmpInfoTerminalBuilder;

/**
 * @author ThanhNX 就業情報端末Repository
 *
 */
@Stateless
public class EmpInfoTerminalPubImpl implements EmpInfoTerminalPub {

	@Inject
	private EmpInfoTerminalRepository repo;

	@Override
	public Optional<EmpInfoTerminalExport> getEmpInfoTerminal(String empInfoTerCode, String contractCode) {

		return repo.getEmpInfoTerminal(new EmpInfoTerminalCode(empInfoTerCode), new ContractCode(contractCode))
				.map(x -> convertTo(x));
	}

	private EmpInfoTerminalExport convertTo(EmpInfoTerminal setting) {
		return new EmpInfoTerminalBuilder(Optional.ofNullable(setting.getIpAddress().toString()), setting.getMacAddress().v(),
				setting.getEmpInfoTerCode().v(), setting.getTerSerialNo().map(x -> x.v()),
				setting.getEmpInfoTerName().v(), setting.getContractCode().v())
						.modelEmpInfoTer(setting.getModelEmpInfoTer().value).intervalTime(setting.getIntervalTime().v())
						.empInfoTerMemo(setting.getEmpInfoTerMemo().map(x -> x.v())).build();
	}
}
