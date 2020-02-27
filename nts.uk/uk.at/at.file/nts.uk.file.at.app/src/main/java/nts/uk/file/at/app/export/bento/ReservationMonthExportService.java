package nts.uk.file.at.app.export.bento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservation;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationDetail;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationRepository;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationRegisterInfo;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.Bento;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenuRepository;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.totalfee.BentoAmountTotal;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.totalfee.BentoDetailsAmountTotal;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCard;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCardRepository;
import nts.uk.query.pub.employee.EmployeeInformationExport;
import nts.uk.query.pub.employee.EmployeeInformationPub;
import nts.uk.query.pub.employee.EmployeeInformationQueryDto;
import nts.uk.query.pub.workplace.WorkplaceExport;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.company.CompanyInfor;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ReservationMonthExportService extends ExportService<ReservationMonthQuery>{
	
	private static final String DATE_FORMAT = "yyyy/MM/dd";
	
	@Inject
	private ReservationMonthGenerator reservationMonthGenerator;
	
	@Inject
	private CompanyAdapter company;
	
	@Inject
	private BentoReservationRepository bentoReservationRepository;
	
	@Inject
	private BentoMenuRepository bentoMenuRepository;
	
	@Inject
	private RegulationInfoEmployeeAdapter regulationInfoEmployeeAdapter;
	
	@Inject
	private EmployeeInformationPub employeeInformationPub;
	
	@Inject
	private StampCardRepository stampCardRepository;

	@Override
	protected void handle(ExportServiceContext<ReservationMonthQuery> context) {
		ReservationMonthQuery query = context.getQuery();
		List<String> empLst = query.getEmpLst();
		DatePeriod period = new DatePeriod(
				GeneralDate.fromString(query.getStartDate(), DATE_FORMAT), 
				GeneralDate.fromString(query.getEndDate(), DATE_FORMAT)); 
		boolean ordered = query.isOrdered();
		String title = query.getTitle();

		// generate file
		reservationMonthGenerator.generate(context.getGeneratorContext(), createReservationMonthLedger(empLst, period, ordered, title));
		
	}
	
	private ReservationMonthDataSource createReservationMonthLedger(List<String> empLst, DatePeriod period, boolean ordered, String title) {
		String companyID = AppContexts.user().companyId();
		
		List<StampCard> stampCardLst = stampCardRepository.getLstStampCardByLstSidAndContractCd(empLst, AppContexts.user().contractCode());
		
		// get*(対象社員リスト,期間,注文済み)
		List<BentoReservation> bentoReservationLst = bentoReservationRepository.findByOrderedPeriodEmpLst(
				stampCardLst.stream().map(x -> new ReservationRegisterInfo(x.getStampNumber().v())).collect(Collectors.toList()), 
				period, 
				ordered);
		
		if(CollectionUtil.isEmpty(bentoReservationLst)) {
			throw new BusinessException("Msg_741");
		}
		
		// get(年月日)
		BentoMenu bentoMenu = bentoMenuRepository.getBentoMenu(companyID, period.end());
		
		/*
		// 社員を並べ替える
		List<String> sortEmpLst = regulationInfoEmployeeAdapter.sortEmployee(companyID, empLst, 2, null, 1, 
				GeneralDateTime.fromString(period.end().toString("yyyy/MM/dd") + " 00:00", "yyyy/MM/dd HH:mm"));
		*/
		List<String> sortEmpLst = empLst;
		// <<Public>> 社員の情報を取得する
		List<EmployeeInformationExport> empInfoLst = employeeInformationPub.find(
				EmployeeInformationQueryDto
				.builder()
				.employeeIds(sortEmpLst)
				.referenceDate(period.end())
				.toGetWorkplace(true)
				.toGetDepartment(false)
				.toGetPosition(false)
				.toGetEmployment(false)
				.toGetClassification(false)
				.toGetEmploymentCls(false)
				.build());
				
		// [RQ622]会社IDから会社情報を取得する
		CompanyInfor companyInfo = company.getCurrentCompany().orElseGet(() -> {
			throw new RuntimeException("System Error: Company Info");
		});
		
		return createDataSource(period, bentoReservationLst, bentoMenu, empInfoLst, companyInfo, stampCardLst, title);
	}
	
	private ReservationMonthDataSource createDataSource(DatePeriod period, List<BentoReservation> bentoReservationLst, BentoMenu bentoMenu, 
			List<EmployeeInformationExport> empInfoLst, CompanyInfor companyInfo, List<StampCard> stampCardLst, String title) {
		ReservationMonthDataSource dataSource = new ReservationMonthDataSource();
		dataSource.setCompanyName(companyInfo.getCompanyName());
		dataSource.setTitle(title);
		dataSource.setPeriod(period);
		Map<WorkplaceExport, List<EmployeeInformationExport>> wkpMap = empInfoLst.stream().collect(Collectors.groupingBy(EmployeeInformationExport::getWorkplace));
		List<ReservationWkpLedger> reservationWkpLedgerLst = wkpMap.entrySet().stream()
				.map(x -> createReservationWkpLedger(x, bentoReservationLst, bentoMenu, stampCardLst))
				.filter(x -> !CollectionUtil.isEmpty(x.getEmpLedgerLst()))
				.collect(Collectors.toList());
		dataSource.setWkpLedgerLst(reservationWkpLedgerLst);
		return dataSource;
	}
	
	private ReservationWkpLedger createReservationWkpLedger(Entry<WorkplaceExport, List<EmployeeInformationExport>> wkpEntry, 
			List<BentoReservation> bentoReservationLst, BentoMenu bentoMenu, List<StampCard> stampCardLst) {
		ReservationWkpLedger wkpLedger = new ReservationWkpLedger();
		wkpLedger.setWkpCD(wkpEntry.getKey().getWorkplaceCode());
		wkpLedger.setWkpName(wkpEntry.getKey().getWorkplaceName());
		Map<String, List<EmployeeInformationExport>> empMap = wkpEntry.getValue().stream().collect(Collectors.groupingBy(EmployeeInformationExport::getEmployeeId));
		List<ReservationEmpLedger> reservationEmpLedgerLst = empMap.entrySet().stream()
				.map(x -> createReservationEmpLedger(
						x, 
						bentoReservationLst, 
						bentoMenu,
						stampCardLst.stream().filter(y -> y.getEmployeeId().equals(x.getKey())).findAny().get().getStampNumber().toString()))
				.filter(x -> !CollectionUtil.isEmpty(x.getBentoLedgerLst()))
				.collect(Collectors.toList());
		wkpLedger.setEmpLedgerLst(reservationEmpLedgerLst);
		return wkpLedger;
	}
	
	private ReservationEmpLedger createReservationEmpLedger(Entry<String, List<EmployeeInformationExport>> empEntry, 
			List<BentoReservation> bentoReservationLst, BentoMenu bentoMenu, String cardNo) {
		ReservationEmpLedger empLedger = new ReservationEmpLedger();
		empLedger.setEmpID(empEntry.getKey());
		empLedger.setEmpCD(empEntry.getValue().get(0).getEmployeeCode());
		empLedger.setEmpName(empEntry.getValue().get(0).getBusinessName());
		List<BentoReservation> reservationEmpLst = bentoReservationLst.stream()
				.filter(x -> x.getRegisterInfor().getReservationCardNo().equals(cardNo)).collect(Collectors.toList());
		Integer totalBentoQuantity = 0;
		Integer totalBentoAmount1 = 0;
		Integer totalBentoAmount2 = 0;
		Map<Pair<GeneralDate, Integer>, Integer> dateFrameQuantityMap = new HashMap<>();
		Map<Integer, String> frameMap = new HashMap<>();
		
		Map<GeneralDate, List<BentoReservation>> reservationDateMap = reservationEmpLst.stream()
				.collect(Collectors.groupingBy(x -> x.getReservationDate().getDate()));
		
		for(Entry<GeneralDate, List<BentoReservation>> x : reservationDateMap.entrySet()) {
			for(BentoReservation y : x.getValue()) {
				Map<Integer, Integer> bentoDetails = y.getBentoReservationDetails().stream().collect(Collectors.toMap(z -> z.getFrameNo(), z -> z.getBentoCount().v()));
				BentoAmountTotal bentoAmountTotal = bentoMenu.calculateTotalAmount(bentoDetails);
				totalBentoAmount1 += bentoAmountTotal.getTotalAmount1();
				totalBentoAmount2 += bentoAmountTotal.getTotalAmount2();
				for(BentoReservationDetail z : y.getBentoReservationDetails()) {
					totalBentoQuantity += z.getBentoCount().v();
					int currentQuantity = 0;
					if(dateFrameQuantityMap.containsKey(Pair.of(x.getKey(), z.getFrameNo()))) {
						currentQuantity = dateFrameQuantityMap.get(Pair.of(x.getKey(), z.getFrameNo()));
					} 
					dateFrameQuantityMap.put(Pair.of(x.getKey(), z.getFrameNo()), currentQuantity + z.getBentoCount().v());
					frameMap.put(z.getFrameNo(), bentoMenu.getMenu().stream().filter(t -> t.getFrameNo()==z.getFrameNo()).findAny().get().getName().toString());
				}
				
			}
		}
		empLedger.setTotalBentoAmount1(totalBentoAmount1);
		empLedger.setTotalBentoAmount2(totalBentoAmount2);
		empLedger.setTotalBentoQuantity(totalBentoQuantity);
		
		List<ReservationBentoLedger> bentoLedgerLst = new ArrayList<>();
		
		for(Entry<Integer, String> frame : frameMap.entrySet()) {
			List<Entry<Pair<GeneralDate, Integer>, Integer>> frameData = dateFrameQuantityMap.entrySet().stream()
					.filter(x -> x.getKey().getRight()==frame.getKey()).collect(Collectors.toList());
			ReservationBentoLedger reservationBentoLedger = new ReservationBentoLedger();
			reservationBentoLedger.setBentoNumber(frame.getKey());
			reservationBentoLedger.setBentoName(frame.getValue());
			Integer totalBentoDetailQuantity = 0;
			Integer totalBentoDetailAmount1 = 0;
			Integer totalBentoDetailAmount2 = 0;
			
			Map<GeneralDate, Integer> quantityDateMap = new HashMap<>();
			for(Entry<Pair<GeneralDate, Integer>, Integer> entry : frameData) {
				Bento bento = bentoMenu.getMenu().stream().filter(x -> x.getFrameNo()==frame.getKey()).findAny().get();
				BentoDetailsAmountTotal bentoDetailsAmountTotal = bento.calculateAmount(entry.getValue());
				totalBentoDetailAmount1 += bentoDetailsAmountTotal.getAmount1();
				totalBentoDetailAmount2 += bentoDetailsAmountTotal.getAmount2();
				totalBentoDetailQuantity += entry.getValue();
				quantityDateMap.put(entry.getKey().getLeft(), entry.getValue());
			}
			
			reservationBentoLedger.setTotalAmount1(totalBentoDetailAmount1);
			reservationBentoLedger.setTotalAmount2(totalBentoDetailAmount2);
			reservationBentoLedger.setTotalQuantity(totalBentoDetailQuantity);
			reservationBentoLedger.setQuantityDateMap(quantityDateMap);
			bentoLedgerLst.add(reservationBentoLedger);
		}
		
		empLedger.setBentoLedgerLst(bentoLedgerLst);
		
		return empLedger;
	}
	
}
