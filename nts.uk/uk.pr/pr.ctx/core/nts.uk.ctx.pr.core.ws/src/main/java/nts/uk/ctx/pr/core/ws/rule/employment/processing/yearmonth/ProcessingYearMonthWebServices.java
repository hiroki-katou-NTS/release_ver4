package nts.uk.ctx.pr.core.ws.rule.employment.processing.yearmonth;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005aCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005aCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005bCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005bCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005cCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005cCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005dCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005dCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth.Qmm005eCommand;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.PaydayDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.PaydayFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.PaydayProcessingDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.PaydayProcessingFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.PaydayProcessingSelect4Dto;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.PaydayProcessingsDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.StandardDayDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.StandardDayFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.SystemDayDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth.SystemDayFinder;
import nts.uk.shr.com.context.AppContexts;

@Path("pr/core")
@Produces("application/json")
public class ProcessingYearMonthWebServices extends WebService {
	@Inject
	private PaydayFinder paydayFinder;

	@Inject
	private PaydayProcessingFinder paydayProcessingFinder;

	@Inject
	private SystemDayFinder systemDayFinder;

	@Inject
	private StandardDayFinder standardDayFinder;

	@Inject
	private Qmm005cCommandHandler qmm005cCommandHandler;

	@Inject
	private Qmm005dCommandHandler qmm005dCommandHandler;

	@Inject
	private Qmm005aCommandHandler qmm005aUpdateCommandHandler;

	@Inject
	private Qmm005bCommandHandler qmm005bCommandHandler;

	@POST
	@Path("paydayprocessing/getbyccd")
	public List<PaydayProcessingDto> getPaydayProcessing(String companyCode) {
		return paydayProcessingFinder.select3(companyCode);
	}

	@POST
	@Path("paydayprocessing/getbyccdanddisatr1")
	public List<PaydayProcessingSelect4Dto> getPaydayProcessing2(String companyCode) {
		return paydayProcessingFinder.select4(companyCode);
	}

	@POST
	@Path("qmm005a/getdata")
	public List<PaydayProcessingsDto> qmm005aGetData() {
		PaydayProcessingsDto[] domain = new PaydayProcessingsDto[5];

		String companyCode = AppContexts.user().companyCode();
		List<PaydayProcessingDto> paydayProcessings = paydayProcessingFinder.select3(companyCode);

		for (PaydayProcessingDto paydayProcessing : paydayProcessings) {
			List<PaydayDto> paydayDtos = paydayFinder.select12(companyCode, 0, 0).stream()
					.filter(f -> f.getProcessingNo() == paydayProcessing.getProcessingNo())
					.collect(Collectors.toList());

			List<PaydayDto> paydayBonusDtos = paydayFinder.select12(companyCode, 1, 0).stream()
					.filter(f -> f.getProcessingNo() == paydayProcessing.getProcessingNo())
					.collect(Collectors.toList());

			domain[paydayProcessings.indexOf(paydayProcessing)] = new PaydayProcessingsDto(paydayProcessing, paydayDtos,
					paydayBonusDtos);
		}

		return Arrays.asList(domain);
	}

	@POST
	@Path("qmm005a/update")
	public void qmm005aUpdate(Qmm005aCommand command) {
		try {
			qmm005aUpdateCommandHandler.handle(command);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@POST
	@Path("qmm005b/getdata")
	public List<PaydayDto> qmm005bGetData(Qmm005bCommand command) {
		String companyCode = AppContexts.user().companyCode();

		return paydayFinder.select4(companyCode, command.getProcessingNo());
	}

	@POST
	@Path("qmm005b/update")
	public void qmm005bUpdate(Qmm005bCommand command) {
		try {
			qmm005bCommandHandler.handle(command);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@POST
	@Path("qmm005c/insert")
	public void qmm005cInsert(Qmm005cCommand command) {
		try {
			qmm005cCommandHandler.handle(command);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@POST
	@Path("qmm005d/getdata")
	public Object[] qmm005dGetData(Qmm005dCommand command) {
		String companyCode = AppContexts.user().companyCode();

		SystemDayDto systemDayDto = systemDayFinder.select1(companyCode, command.getProcessingNo());
		StandardDayDto standardDayDto = standardDayFinder.select1(companyCode, command.getProcessingNo());

		return new Object[] { systemDayDto, standardDayDto };
	}

	@POST
	@Path("qmm005d/update")
	public void qmm005dUpdate(Qmm005dCommand command) {
		try {
			qmm005dCommandHandler.handle(command);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@POST
	@Path("qmm005e/getdata")
	public Object[] qmm005eGetData(Qmm005eCommand command) {
		String companyCode = AppContexts.user().companyCode();

		SystemDayDto systemDayDto = systemDayFinder.select1(companyCode, command.getProcessingNo());
		StandardDayDto standardDayDto = standardDayFinder.select1(companyCode, command.getProcessingNo());

		return new Object[] { systemDayDto, standardDayDto };
	}
}
