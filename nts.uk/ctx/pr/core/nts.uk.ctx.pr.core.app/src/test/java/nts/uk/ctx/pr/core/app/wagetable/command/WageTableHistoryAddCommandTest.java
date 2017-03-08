/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import junit.framework.TestCase;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.CodeItemDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.RangeItemDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.RefModeDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.StepModeDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WageTableDemensionDetailDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WageTableItemDto;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.element.RefMode;
import nts.uk.ctx.pr.core.dom.wagetable.element.StepMode;
import nts.uk.ctx.pr.core.dom.wagetable.history.WageTableDemensionDetail;
import nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistory;
import nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItem;

/**
 * The Class CommonSettingUpdateHandlerTest.
 */
@RunWith(JMockit.class)
public class WageTableHistoryAddCommandTest extends TestCase {

	/** The handler. */
	@Tested
	private WageTableHistoryAddCommand command;

	/**
	 * To domain 001.
	 */
	@Test
	public void toDomain_001() {
		CompanyCode companyCode = new CompanyCode("0001");

		CodeItemDto codeItemDto1 = new CodeItemDto();
		codeItemDto1.setReferenceCode("referenceCode1");
		codeItemDto1.setUuid("uuid1");

		CodeItemDto codeItemDto2 = new CodeItemDto();
		codeItemDto2.setReferenceCode("referenceCode2");
		codeItemDto2.setUuid("uuid2");

		List<CodeItemDto> codeItemDtos = Arrays.asList(codeItemDto1, codeItemDto2);

		RefModeDto refModeDto = new RefModeDto();
		refModeDto.setType(ElementType.MASTER_REF.value);
		refModeDto.setCompanyCode(companyCode.v());
		refModeDto.setRefNo("tb01");
		refModeDto.setItems(codeItemDtos);

		RangeItemDto rangeItemDto1 = new RangeItemDto();
		rangeItemDto1.setOrderNumber(1);
		rangeItemDto1.setStartVal(1d);
		rangeItemDto1.setEndVal(5d);
		rangeItemDto1.setUuid("uuid3");

		RangeItemDto rangeItemDto2 = new RangeItemDto();
		rangeItemDto2.setOrderNumber(1);
		rangeItemDto2.setStartVal(5d);
		rangeItemDto2.setEndVal(10d);
		rangeItemDto2.setUuid("uuid4");

		List<RangeItemDto> rangeItemDtos = Arrays.asList(rangeItemDto1, rangeItemDto2);

		StepModeDto stepModeDto = new StepModeDto();
		stepModeDto.setType(ElementType.AGE_FIX.value);
		stepModeDto.setLowerLimit(1d);
		stepModeDto.setUpperLimit(10d);
		stepModeDto.setInterval(5d);
		stepModeDto.setItems(rangeItemDtos);

		WageTableDemensionDetailDto demensionDetailDto1 = new WageTableDemensionDetailDto();
		demensionDetailDto1.setDemensionNo(DemensionNo.DEMENSION_1ST.value);
		demensionDetailDto1.setElementModeSetting(refModeDto);

		WageTableDemensionDetailDto demensionDetailDto2 = new WageTableDemensionDetailDto();
		demensionDetailDto2.setDemensionNo(DemensionNo.DEMENSION_2ND.value);
		demensionDetailDto2.setElementModeSetting(stepModeDto);

		List<WageTableDemensionDetailDto> demensionDetails = Arrays.asList(demensionDetailDto1,
				demensionDetailDto2);

		WageTableItemDto wageTableItemDto = new WageTableItemDto();
		wageTableItemDto.setElement1Id("element1Id");
		wageTableItemDto.setElement2Id("element2Id");
		wageTableItemDto.setElement3Id("element3Id");
		wageTableItemDto.setAmount(BigDecimal.valueOf(111111));

		List<WageTableItemDto> valueItems = Arrays.asList(wageTableItemDto);

		command = new WageTableHistoryAddCommand();
		command.setCode("001");
		command.setStartMonth("2016/01");
		command.setEndMonth("2016/07");
		command.setDemensionDetails(demensionDetails);
		command.setValueItems(valueItems);

		// Execute
		WageTableHistory wageTableHistory = command.toDomain(companyCode);

		// Assert
		assertEquals("001", wageTableHistory.getCode().v());
		assertEquals(true, wageTableHistory.getApplyRange().getStartMonth().v() == 201601);
		assertEquals(true, wageTableHistory.getApplyRange().getEndMonth().v() == 201607);

		WageTableDemensionDetail wageTableDemensionDetail1 = wageTableHistory.getDemensionItems()
				.get(0);
		assertEquals(DemensionNo.DEMENSION_1ST.value,
				wageTableDemensionDetail1.getDemensionNo().value);

		assertEquals(true, wageTableDemensionDetail1.getElementModeSetting() instanceof RefMode);
		RefMode elementMode1 = (RefMode) wageTableDemensionDetail1.getElementModeSetting();
		// TODO Company is null.
		// assertEquals("0001", elementMode1.getCompanyCode().v());
		assertEquals(ElementType.MASTER_REF.value, elementMode1.getElementType().value);
		assertEquals("tb01", elementMode1.getRefNo().v());
		assertEquals("referenceCode1", elementMode1.getItems().get(0).getReferenceCode());
		assertEquals("uuid1", elementMode1.getItems().get(0).getUuid());
		assertEquals("referenceCode2", elementMode1.getItems().get(1).getReferenceCode());
		assertEquals("uuid2", elementMode1.getItems().get(1).getUuid());

		WageTableDemensionDetail wageTableDemensionDetail2 = wageTableHistory.getDemensionItems()
				.get(1);
		assertEquals(DemensionNo.DEMENSION_2ND.value,
				wageTableDemensionDetail2.getDemensionNo().value);

		assertEquals(true, wageTableDemensionDetail2.getElementModeSetting() instanceof StepMode);
		StepMode elementMode2 = (StepMode) wageTableDemensionDetail2.getElementModeSetting();
		assertEquals(ElementType.AGE_FIX.value, elementMode2.getElementType().value);
		assertEquals(1d, elementMode2.getLowerLimit().doubleValue());
		assertEquals(10d, elementMode2.getUpperLimit().doubleValue());
		assertEquals(5d, elementMode2.getInterval().doubleValue());
		assertEquals("uuid3", elementMode2.getItems().get(0).getUuid());
		assertEquals(1, elementMode2.getItems().get(0).getOrderNumber().intValue());

		WageTableItem wageTableItem1 = wageTableHistory.getValueItems().get(0);
		assertEquals("element1Id", wageTableItem1.getElement1Id().v());
		assertEquals("element2Id", wageTableItem1.getElement2Id().v());
		assertEquals("element3Id", wageTableItem1.getElement3Id().v());
		assertEquals(BigDecimal.valueOf(111111),
				wageTableHistory.getValueItems().get(0).getAmount());

	}

}