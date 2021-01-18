package nts.uk.ctx.at.record.dom.employmentinfoterminal.nrlremote.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.ConvertEmbossCategory;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.CreateStampInfo;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerSerialNo;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalCode;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalName;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.FullIpAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.MacAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.ModelEmpInfoTer;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.MonitorIntervalTime;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.OutPlaceConvert;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.PartialIpAddress;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.nrlremote.NRRomVersion;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.nrlremote.TimeRecordSetFormatList;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.nrlremote.TimeRecordSetUpdateList;
import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminal.EmpInfoTerminalBuilder;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 
 * @author dungbn
 *
 */
@RunWith(JMockit.class)
public class JudgmentWaitingRequestTest {

	@Injectable
	private JudgmentWaitingRequest.Require require;
	
	// データがある(Flag = true)
	@Test
	public void testJudgmentWaitingRequest() {
		
		List<EmpInfoTerminal> empInfoTerminalList = JudgmentWaitingRequestTestHelper.createEmpInfoTerminalList();

		List<EmpInfoTerminalCode> listEmpInfoTerminalCode = empInfoTerminalList.stream().map(e -> e.getEmpInfoTerCode()).collect(Collectors.toList());
		
		List<TimeRecordSetFormatList> listTimeRecordSetFormatList = JudgmentWaitingRequestTestHelper.createListTimeRecordSetFormatList();
		List<TimeRecordSetUpdateList> listTimeRecordSetUpdateList = JudgmentWaitingRequestTestHelper.createListTimeRecordSetUpdateList();

		new Expectations() {
			{
				require.getTimeRecordSetFormatList(JudgmentWaitingRequestTestHelper.contractCode, listEmpInfoTerminalCode);
				result = listTimeRecordSetFormatList;
				require.getTimeRecordUpdateList(JudgmentWaitingRequestTestHelper.contractCode, listEmpInfoTerminalCode);
				result = listTimeRecordSetUpdateList;
			}
		};
		
		val actual = JudgmentWaitingRequest.judgmentReqWaitingStatus(require, JudgmentWaitingRequestTestHelper.contractCode, empInfoTerminalList);
		assertThat(actual.get(JudgmentWaitingRequestTestHelper.empInfoTerminalCode)).isTrue();
		
	}
	
	// データがある(Flag = false)
	@Test
	public void testJudgmentWaitingRequest1() {
		
		List<EmpInfoTerminal> empInfoTerminalList = JudgmentWaitingRequestTestHelper.createEmpInfoTerminalList();

		List<EmpInfoTerminalCode> listEmpInfoTerminalCode = empInfoTerminalList.stream().map(e -> e.getEmpInfoTerCode()).collect(Collectors.toList());
		
		List<TimeRecordSetFormatList> listTimeRecordSetFormatList = JudgmentWaitingRequestTestHelper.createListTimeRecordSetFormatList();
		List<TimeRecordSetUpdateList> listTimeRecordSetUpdateList = JudgmentWaitingRequestTestHelper.createListTimeRecordSetUpdateList1();

		new Expectations() {
			{
				require.getTimeRecordSetFormatList(JudgmentWaitingRequestTestHelper.contractCode, listEmpInfoTerminalCode);
				result = listTimeRecordSetFormatList;
				require.getTimeRecordUpdateList(JudgmentWaitingRequestTestHelper.contractCode, listEmpInfoTerminalCode);
				result = listTimeRecordSetUpdateList;
			}
		};

		val actual = JudgmentWaitingRequest.judgmentReqWaitingStatus(require, JudgmentWaitingRequestTestHelper.contractCode, empInfoTerminalList);
		assertThat(actual.get(JudgmentWaitingRequestTestHelper.empInfoTerminalCode2)).isFalse();
	}
	
	// データがありません
		@Test
		public void testJudgmentWaitingRequest2() {
			
			List<EmpInfoTerminal> empInfoTerminalList = new ArrayList<EmpInfoTerminal>();

			List<EmpInfoTerminalCode> listEmpInfoTerminalCode = empInfoTerminalList.stream().map(e -> e.getEmpInfoTerCode()).collect(Collectors.toList());
			
			List<TimeRecordSetFormatList> listTimeRecordSetFormatList = new ArrayList<TimeRecordSetFormatList>();
			List<TimeRecordSetUpdateList> listTimeRecordSetUpdateList = new ArrayList<TimeRecordSetUpdateList>();

			new Expectations() {
				{
					require.getTimeRecordSetFormatList(JudgmentWaitingRequestTestHelper.contractCode, listEmpInfoTerminalCode);
					result = listTimeRecordSetFormatList;
					require.getTimeRecordUpdateList(JudgmentWaitingRequestTestHelper.contractCode, listEmpInfoTerminalCode);
					result = listTimeRecordSetUpdateList;
				}
			};
			
			val actual = JudgmentWaitingRequest.judgmentReqWaitingStatus(require, JudgmentWaitingRequestTestHelper.contractCode, empInfoTerminalList);
			assertThat(actual.isEmpty()).isTrue();
		}
}
