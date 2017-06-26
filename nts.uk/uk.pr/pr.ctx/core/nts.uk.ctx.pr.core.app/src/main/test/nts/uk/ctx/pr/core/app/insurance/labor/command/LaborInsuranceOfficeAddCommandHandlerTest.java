/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.command;

import org.junit.Test;
import org.junit.runner.RunWith;

import junit.framework.TestCase;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import nts.uk.ctx.pr.core.app.insurance.labor.command.dto.LaborInsuranceOfficeDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.service.LaborInsuranceOfficeService;

/**
 * The Class LaborInsuranceOfficeAddCommandHandlerTest.
 */
@RunWith(JMockit.class)
public class LaborInsuranceOfficeAddCommandHandlerTest extends TestCase {

	/** The labor insurance office repository. */
	@Injectable
	private LaborInsuranceOfficeRepository laborInsuranceOfficeRepo;

	/** The labor insurance office service. */
	@Injectable
	private LaborInsuranceOfficeService laborInsuranceOfficeService;

	/** The handler. */
	@Tested
	private LaborInsuranceOfficeAddCommandHandler handler = new LaborInsuranceOfficeAddCommandHandler();

	/**
	 * Handler_001.
	 */
	@Test
	public void handler_001() {
		LaborInsuranceOfficeDto laborInsuranceOfficeDto = new LaborInsuranceOfficeDto();
		laborInsuranceOfficeDto.setAddress1st("address1st");
		laborInsuranceOfficeDto.setAddress2nd("address2nd");
		laborInsuranceOfficeDto.setCitySign("citySign");
		laborInsuranceOfficeDto.setCode("code");
		laborInsuranceOfficeDto.setKanaAddress1st("kanaAddress1st");
		laborInsuranceOfficeDto.setKanaAddress2nd("kanaAddress2nd");
		laborInsuranceOfficeDto.setMemo("memo");
		laborInsuranceOfficeDto.setName("name");
		laborInsuranceOfficeDto.setOfficeMark("officeMark");
		laborInsuranceOfficeDto.setOfficeNoA("officeNoA");
		laborInsuranceOfficeDto.setOfficeNoB("officeNoB");
		laborInsuranceOfficeDto.setOfficeNoC("officeNoC");
		laborInsuranceOfficeDto.setPhoneNumber("phoneNumber");
		laborInsuranceOfficeDto.setPicName("picName");
		laborInsuranceOfficeDto.setPicPosition("picPosition");
		laborInsuranceOfficeDto.setPotalCode("potalCode");
		laborInsuranceOfficeDto.setPrefecture("prefecture");
		laborInsuranceOfficeDto.setShortName("shortName");
		laborInsuranceOfficeDto.setVersion(0);

		LaborInsuranceOfficeAddCommand command;
		command = new LaborInsuranceOfficeAddCommand();
		command.setLaborInsuranceOfficeDto(laborInsuranceOfficeDto);
		// Execute
		this.handler.handle(command);
	}

	@Test
	public void handler_002() {
		LaborInsuranceOfficeDto laborInsuranceOfficeDto = new LaborInsuranceOfficeDto();
		laborInsuranceOfficeDto.setAddress1st("1");
		laborInsuranceOfficeDto.setAddress2nd("1");
		laborInsuranceOfficeDto.setCitySign("1");
		laborInsuranceOfficeDto.setCode("2");
		laborInsuranceOfficeDto.setKanaAddress1st("1");
		laborInsuranceOfficeDto.setKanaAddress2nd("1");
		laborInsuranceOfficeDto.setMemo("memo");
		laborInsuranceOfficeDto.setName("2");
		laborInsuranceOfficeDto.setOfficeMark("2");
		laborInsuranceOfficeDto.setOfficeNoA("2");
		laborInsuranceOfficeDto.setOfficeNoB("2");
		laborInsuranceOfficeDto.setOfficeNoC("2");
		laborInsuranceOfficeDto.setPhoneNumber("1");
		laborInsuranceOfficeDto.setPicName("1");
		laborInsuranceOfficeDto.setPicPosition("1");
		laborInsuranceOfficeDto.setPotalCode("1");
		laborInsuranceOfficeDto.setPrefecture("1");
		laborInsuranceOfficeDto.setShortName("2");
		laborInsuranceOfficeDto.setVersion(0);

		LaborInsuranceOfficeAddCommand command;
		command = new LaborInsuranceOfficeAddCommand();
		command.setLaborInsuranceOfficeDto(laborInsuranceOfficeDto);
		// Execute
		this.handler.handle(command);
	}
	@Test
	public void handler_003() {
		LaborInsuranceOfficeDto laborInsuranceOfficeDto = new LaborInsuranceOfficeDto();
		laborInsuranceOfficeDto.setAddress1st("1");
		laborInsuranceOfficeDto.setAddress2nd("1");
		laborInsuranceOfficeDto.setCitySign("1");
		laborInsuranceOfficeDto.setCode("2");
		laborInsuranceOfficeDto.setKanaAddress1st("1");
		laborInsuranceOfficeDto.setKanaAddress2nd("1");
		laborInsuranceOfficeDto.setMemo("memo");
		laborInsuranceOfficeDto.setName("2");
		laborInsuranceOfficeDto.setOfficeMark("2");
		laborInsuranceOfficeDto.setOfficeNoA("2");
		laborInsuranceOfficeDto.setOfficeNoB("2");
		laborInsuranceOfficeDto.setOfficeNoC("2");
		laborInsuranceOfficeDto.setPhoneNumber("1");
		laborInsuranceOfficeDto.setPicName("1");
		laborInsuranceOfficeDto.setPicPosition("1");
		laborInsuranceOfficeDto.setPotalCode("1");
		laborInsuranceOfficeDto.setPrefecture("1");
		laborInsuranceOfficeDto.setShortName("2");
		laborInsuranceOfficeDto.setVersion(0);
		
		LaborInsuranceOfficeAddCommand command;
		command = new LaborInsuranceOfficeAddCommand();
		command.setLaborInsuranceOfficeDto(laborInsuranceOfficeDto);
		// Execute
		this.handler.handle(command);
	}

}