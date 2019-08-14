package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Arrays;
import java.util.Collections;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
//import nts.uk.ctx.at.record.dom.adapter.approvalrootstate.AppRootStateConfirmAdapter;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.approvalmanagement.domainservice.DeleteApprovalStaOfDailyPerforService;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationattribute.repo.CalAttrOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerformRepo;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.shorttimework.repo.ShortTimeOfDailyPerformanceRepository;
//import nts.uk.ctx.at.record.dom.stamp.StampRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.updateworkinfo.DeleteWorkInfoOfDailyPerforService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;

/**
 * 日別実績を削除する
 * 
 * @author nampt
 *
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class DeleteWorkInfoOfDaiPerService {
	
	@Inject
	private DeleteWorkInfoOfDailyPerforService deleteWorkInfoOfDailyPerforService;

	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInforOfDailyPerforRepository;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;

	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporaryTimeOfDailyPerformanceRepository;

	@Inject
	private EditStateOfDailyPerformanceRepository editStateOfDailyPerformanceRepository;

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository;

	@Inject
	private OutingTimeOfDailyPerformanceRepository outingTimeOfDailyPerformanceRepository;

//	@Inject
//	private StampRepository stampRepository;

	@Inject
	private DeleteApprovalStaOfDailyPerforService deleteApprovalStaOfDailyPerforService;

	@Inject
	private IdentificationRepository identificationRepository;

	@Inject
	private AttendanceTimeRepository attendanceTimeRepository;
	
	@Inject
	private ShortTimeOfDailyPerformanceRepository shortTimeOfDailyPerformanceRepository;
	
	@Inject
	private CalAttrOfDailyPerformanceRepository calAttrOfDailyPerformanceRepository;
	
	@Inject
	private AttendanceLeavingGateOfDailyRepo attendanceLeavingGateOfDailyRepo;
	
	@Inject
	private WorkTypeOfDailyPerforRepository workTypeOfDailyPerforRepository;
	
	@Inject
	private PCLogOnInfoOfDailyRepo pcLogOnInfoOfDailyRepo;
	
	@Inject
	private SpecificDateAttrOfDailyPerforRepo specificDateAttrOfDailyPerforRepo;
	
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepository;
	
	@Inject
	private AnyItemValueOfDailyRepo anyItemValueOfDailyRepo;
	
//	@Inject
//	private AppRootStateConfirmAdapter appRootStateConfirmAdapter;
	
	@Inject 
	private RemarksOfDailyPerformRepo remarksOfDailyPerformRepo;
	
	@Inject
	private DailyRecordAdUpService dailyRecordAdUpService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteWorkInfoOfDaiPerService(String employeeId, GeneralDate day) {
    	this.deleteWorkInfoOfDailyPerforService.deleteWorkInfoOfDailyPerforService(employeeId, day);
		this.deleteApprovalStaOfDailyPerforService.deleteApprovalStaOfDailyPerforService(employeeId, day);
		this.affiliationInforOfDailyPerforRepository.delete(employeeId, day);
		this.identificationRepository.removeByEmployeeIdAndDate(employeeId, day);
		this.timeLeavingOfDailyPerformanceRepository.delete(employeeId, day);
		this.editStateOfDailyPerformanceRepository.delete(employeeId, day);
		this.attendanceTimeRepository.deleteByEmployeeIdAndDate(employeeId, day);
		this.temporaryTimeOfDailyPerformanceRepository.delete(employeeId, day);
		this.breakTimeOfDailyPerformanceRepository.delete(employeeId, day);
		this.outingTimeOfDailyPerformanceRepository.delete(employeeId, day);
		this.calAttrOfDailyPerformanceRepository.deleteByKey(employeeId, day);
		this.attendanceLeavingGateOfDailyRepo.removeByKey(employeeId, day);
		this.workTypeOfDailyPerforRepository.delete(employeeId, day);
		this.pcLogOnInfoOfDailyRepo.removeByKey(employeeId, day);
		// anyitem
		// AnyItemValueOfDailyRepo
		this.shortTimeOfDailyPerformanceRepository.deleteByEmployeeIdAndDate(employeeId, day);
		// AttendanceTimeByWorkOfDailyRepository
		this.specificDateAttrOfDailyPerforRepo.deleteByEmployeeIdAndDate(employeeId, day);
//		this.employeeDailyPerErrorRepository.removeParam(employeeId, day);
		this.dailyRecordAdUpService.adUpEmpError(Collections.emptyList(), Arrays.asList(Pair.of(employeeId, day)), true);
		// remove approval State from workflow
//		this.appRootStateConfirmAdapter.deleteApprovalByEmployeeIdAndDate(employeeId, day);
		this.anyItemValueOfDailyRepo.deleteAnyItemValueOfDaily(employeeId, day);
		
		this.remarksOfDailyPerformRepo.removeWithJdbc(employeeId, day);
	}

}
