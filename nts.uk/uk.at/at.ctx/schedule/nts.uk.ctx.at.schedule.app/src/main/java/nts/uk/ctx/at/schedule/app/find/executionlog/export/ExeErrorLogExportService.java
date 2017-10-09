/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.executionlog.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.i18n.custom.IInternationalization;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleErrorLogDto;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.EmployeeDto;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.SCEmployeeAdapter;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class ExtBudgetErrorExportService.
 */
@Stateless
public class ExeErrorLogExportService extends ExportService<String> {

    /** The generator. */
    @Inject
    private ExeErrorLogGenerator generator;

    /** The error repo. */
    @Inject
    private ScheduleErrorLogRepository errorRepo;

    /** The internationalization. */
    @Inject
    private IInternationalization internationalization;

    /** The employee adapter. */
	@Inject
	private SCEmployeeAdapter employeeAdapter;
	
    /** The Constant LST_NAME_ID. */
    private static final List<String> LST_NAME_ID_HEADER = Arrays.asList("KSC001_56", "KSC001_57", "KSC001_58",
            "KSC001_59");

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.arc.layer.app.file.export.ExportService#handle(nts.arc.layer.app.file
     * .export.ExportServiceContext)
     */
    @Override
    protected void handle(ExportServiceContext<String> context) {
        // get execute id
        String executeId = context.getQuery();
        
        // find list data error
        List<ScheduleErrorLog> lstError = this.errorRepo.findByExecutionId(executeId);
        // check has data
        if (CollectionUtil.isEmpty(lstError)) {
            throw new BusinessException("Msg_160");
        }
        
//        lstError.stream().sorted(comparator).
        // convert to dto
        List<ScheduleErrorLogDto> lstErrorDto = lstError.stream()
                .map(domain -> {
                	ScheduleErrorLogDto dto = new ScheduleErrorLogDto();
                    domain.saveToMemento(dto);
					EmployeeDto employee = this.employeeAdapter.findByEmployeeId(dto.getEmployeeId());
					dto.setEmployeeCode(employee.getEmployeeCode());
					dto.setEmployeeName(employee.getEmployeeName());
                    return dto;
                }).collect(Collectors.toList());
        //sort by employee code
        List<ScheduleErrorLogDto> afterSort = lstErrorDto.stream().sorted(ScheduleErrorLogComparator).collect(Collectors.toList());
        // set data export
        ExportData exportData = ExportData.builder()
                .employeeId(AppContexts.user().employeeId())
                .lstHeader(this.finHeader())
                .lstError(afterSort)
                .build();
        // generate file
        this.generator.generate(context.getGeneratorContext(), exportData);
    }

    /**
     * Fin header.
     *
     * @return the list
     */
    private List<String> finHeader() {
        List<String> lstHeader = new ArrayList<>();
        for (String nameId : LST_NAME_ID_HEADER) {
            Optional<String> optional = this.internationalization.getItemName(nameId);
            if (!optional.isPresent()) {
                throw new RuntimeException("NameId of header not found.");
            }
            lstHeader.add(optional.get());
        }
        return lstHeader;
    }
    
	/** The Schedule error log comparator. */
	public static Comparator<ScheduleErrorLogDto> ScheduleErrorLogComparator = new Comparator<ScheduleErrorLogDto>() {

		@Override
		public int compare(ScheduleErrorLogDto arg0, ScheduleErrorLogDto arg1) {
			String employeeCode0 = arg0.getEmployeeCode().toUpperCase();
			String employeeCode1 = arg1.getEmployeeCode().toUpperCase();
			return employeeCode0.compareTo(employeeCode1);
		}
	};
}
