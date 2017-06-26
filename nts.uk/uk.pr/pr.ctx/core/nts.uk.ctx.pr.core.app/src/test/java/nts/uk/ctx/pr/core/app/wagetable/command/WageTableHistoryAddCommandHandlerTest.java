///******************************************************************
// * Copyright (c) 2017 Nittsu System to present.                   *
// * All right reserved.                                            *
// *****************************************************************/
//package nts.uk.ctx.pr.core.app.wagetable.command;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import junit.framework.TestCase;
//import mockit.Expectations;
//import mockit.Injectable;
//import mockit.Mock;
//import mockit.MockUp;
//import mockit.Tested;
//import mockit.integration.junit4.JMockit;
//import nts.arc.layer.app.command.CommandHandlerContext;
//import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtElementDto;
//import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHeadDto;
//import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHistoryDto;
//import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtItemDto;
//import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;
//import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryRepository;
//
///**
// * The Class CommonSettingUpdateHandlerTest.
// */
//@RunWith(JMockit.class)
//public class WageTableHistoryAddCommandHandlerTest extends TestCase {
//
//	// ============= REPOSITORY ==================//
//	/** The wage table history repo. */
//	@Injectable
//	private WtHistoryRepository wageTableHistoryRepo;
//
//	/** The handler. */
//	@Tested
//	private WtHistoryAddCommandHandler handler;
//
//	/**
//	 * Handler 001.
//	 */
//	@Test
//	public void handler_001() {
//
//		// Expectation.
//		new Expectations() {
//			{
//				wageTableHistoryRepo.update((WtHistory) this.any);
//				this.times = 1;
//			}
//		};
//
//		List<WtElementDto> demensionDetails = Arrays
//				.asList(new WtElementDto());
//
//		List<WtItemDto> valueItems = Arrays.asList(new WtItemDto());
//
//		WtHeadDto wageTableHeadDto = new WtHeadDto();
//
//		WtHistoryDto wageTableHistoryDto = new WtHistoryDto();
//
//		wageTableHistoryDto.setStartMonth("01/2016");
//		wageTableHistoryDto.setEndMonth("07/2016");
//		wageTableHistoryDto.setDemensionDetails(demensionDetails);
//		wageTableHistoryDto.setValueItems(valueItems);
//
//		WtHistoryAddCommand command = new WtHistoryAddCommand();
//		command.setWageTableHeadDto(wageTableHeadDto);
//		command.setWageTableHistoryDto(wageTableHistoryDto);
//
//		MockUp<CommandHandlerContext<WtHistoryAddCommand>> mockedContext = new MockUp<CommandHandlerContext<WtHistoryAddCommand>>() {
//			@Mock
//			WtHistoryAddCommand getCommand() {
//				return command;
//			}
//		};
//
//		CommandHandlerContext<WtHistoryAddCommand> context = mockedContext.getMockInstance();
//
//		// Execute
//		this.handler.handle(context);
//	}
//
//}