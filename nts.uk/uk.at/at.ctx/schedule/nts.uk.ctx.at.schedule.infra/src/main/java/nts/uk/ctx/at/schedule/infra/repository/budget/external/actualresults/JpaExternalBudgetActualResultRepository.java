package nts.uk.ctx.at.schedule.infra.repository.budget.external.actualresults;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.budget.external.acceptance.ExtBudgetNumberPerson;
import nts.uk.ctx.at.schedule.dom.budget.external.acceptance.ExtBudgetNumericalVal;
import nts.uk.ctx.at.schedule.dom.budget.external.actualresults.ExternalBudgetMoneyValue;
import nts.uk.ctx.at.schedule.dom.budget.external.actualresults.ExternalBudgetTimeValue;
import nts.uk.ctx.at.schedule.dom.budget.external.result.ExtBudgetActItemCode;
import nts.uk.ctx.at.schedule.dom.budget.external.result.ExtBudgetActualValues;
import nts.uk.ctx.at.schedule.dom.budget.external.result.ExtBudgetDaily;
import nts.uk.ctx.at.schedule.dom.budget.external.result.ExtBudgetDailyRepository;
import nts.uk.ctx.at.schedule.infra.entity.budget.external.actualresults.KscdtExtBudgetDailyNew;
import nts.uk.ctx.at.schedule.infra.entity.budget.external.actualresults.KscdtExtBudgetDailyPkNew;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrganizationUnit;
import nts.uk.shr.com.context.AppContexts;



/**
 * 日次の外部予算実績Repository
 * @author HieuLt
 *
 */
@Stateless
public class JpaExtBudgetDailyRepository extends JpaRepository implements ExtBudgetDailyRepository{
	
			private static final String GetDaily = "SELECT c FROM KscdtExtBudgetDailyNew c WHERE c.pk.targetUnit = :targetUnit "
												+ " AND c.pk.itemCd = :itemCode " 							
					                            + " AND c.pk.ymd >= :startDate"
												+ " AND c.pk.ymd <= :endDate"
					                            + " ORDER BY c.pk.ymd ASC ";
			private static final String Getdata = "SELECT c FROM KscdtExtBudgetDailyNew c WHERE c.pk.targetUnit = :targetUnit "
					                              + " AND c.pk.itemCd = :itemCode " 	
					                              + " AND c.pk.ymd = :ymd ";
			private static final String GetByKey = 	" SELECT c FROM KscdtExtBudgetDailyNew c WHERE c.pk.targetUnit = :targetUnit "	
													+ " AND c.pk.companyId = :companyId "
													+ " AND c.pk.targetID = :targetID "
													+ " AND c.pk.ymd = :ymd "
													+ " AND c.pk.itemCd = :itemCd ";
			private static final String DELETE = " DELETE FROM KscdtExtBudgetDailyNew k "
											   + " WHERE k.pk.targetID = :targetID " 
											   + " AND k.pk.itemCd = :itemCd "
											   + " AND k.pk.ymd = :ymd ";
			private static final String GetDailyByPeriod = "SELECT c FROM KscdtExtBudgetDailyNew c WHERE c.pk.targetUnit = :targetUnit "
															+ " AND c.pk.targetID = :targetID "
										                    + " AND c.pk.ymd >= :startDate"
															+ " AND c.pk.ymd <= :endDate"
										                    + " ORDER BY c.pk.ymd ASC ";
			
			private static final String GetDailyByListTarget= "SELECT c FROM KscdtExtBudgetDailyNew c WHERE c.pk.targetUnit IN :listTargetUnit "
																+ " AND c.pk.targetID IN :listTargetID "
											                    + " AND c.pk.ymd >= :startDate"
																+ " AND c.pk.ymd <= :endDate"
											                    + " ORDER BY c.pk.ymd ASC ";
			
			
	@Override
	public List<ExtBudgetDaily> getDailyExtBudgetResults(TargetOrgIdenInfor targetOrg, ExtBudgetActItemCode itemCode,
			GeneralDate ymd) {
		
		List<ExtBudgetDaily> data = this.queryProxy().query(Getdata, KscdtExtBudgetDailyNew.class)
											.setParameter("targetUnit", targetOrg.getUnit().value)
											.setParameter("itemCode", itemCode)
											.setParameter("ymd", ymd)
											.getList( c ->toDomain(c));
		return data;
	}

	@Override
	public List<ExtBudgetDaily> getDailyExtBudgetResultsForPeriod(TargetOrgIdenInfor targetOrg, DatePeriod datePeriod,
			ExtBudgetActItemCode itemCode) {
		

		return this.queryProxy().query(GetDaily, KscdtExtBudgetDailyNew.class)
				.setParameter("targetUnit", targetOrg.getUnit().value)
				.setParameter("itemCode", itemCode)
				.setParameter("startDate", datePeriod.start())
				.setParameter("endDate", datePeriod.end())
				.getList(c -> toDomain(c));
		
	}
	
	@Override
	public List<ExtBudgetDaily> getAllExtBudgetDailyByPeriod(TargetOrgIdenInfor targetOrg, DatePeriod datePeriod) {
		
		return this.queryProxy().query(GetDailyByPeriod,  KscdtExtBudgetDailyNew.class)
				.setParameter("targetUnit", targetOrg.getUnit().value)
				.setParameter("targetID", targetOrg.getTargetId())
				.setParameter("startDate", datePeriod.start())
				.setParameter("endDate", datePeriod.end())
				.getList(c -> toDomain(c));
				
	}
	
	@Override
	public List<ExtBudgetDaily> getAllExtBudgetDailyByPeriod(List<TargetOrgIdenInfor> lstTargetOrg,
			DatePeriod datePeriod) {
		List<Integer> listTargetUnit = lstTargetOrg.stream().map(c -> c.getUnit().value).collect(Collectors.toList());
		List<String> listTargetID = lstTargetOrg.stream().map(c -> c.getTargetId()).collect(Collectors.toList());
		
		return this.queryProxy().query(GetDailyByListTarget,  KscdtExtBudgetDailyNew.class)
				.setParameter("targetUnit", listTargetUnit)
				.setParameter("targetID", listTargetID)
				.setParameter("startDate", datePeriod.start())
				.setParameter("endDate", datePeriod.end())
				.getList(c -> toDomain(c));		
	}

	@Override
	public void insert(ExtBudgetDaily extBudgetDaily) {
		if(extBudgetDaily.getActualValue() == null)
			return;
		this.commandProxy().insert(toEntity(extBudgetDaily));
	}

	@Override
	public void update(ExtBudgetDaily extBudgetDaily) {
		String target = "";
		if(extBudgetDaily.getTargetOrg().getUnit().value == 0){
			 target = extBudgetDaily.getTargetOrg().getWorkplaceId().get();
		}
		else{
			 target = extBudgetDaily.getTargetOrg().getWorkplaceGroupId().get();
		}
		
		KscdtExtBudgetDailyNew oldData  = this.queryProxy().query(GetByKey, KscdtExtBudgetDailyNew.class)
										.setParameter("targetUnit", extBudgetDaily.getTargetOrg().getUnit().value)
										.setParameter("companyId", AppContexts.user().companyId())
										.setParameter("targetID", target)
										.setParameter("ymd", extBudgetDaily.getYmd())
										.setParameter("itemCd", extBudgetDaily.getItemCode().v()).getSingle().get();
		KscdtExtBudgetDailyNew newData = toEntity(extBudgetDaily);
		oldData.budgetATR = newData.budgetATR;
		oldData.val = newData.val;
		this.commandProxy().update(oldData);
	}

	@Override
	public void delete(TargetOrgIdenInfor targetOrg, ExtBudgetActItemCode itemCode, GeneralDate ymd) {
		String targetID = "";
		if(targetOrg.getUnit().value == 0){
			targetID = targetOrg.getWorkplaceId().get();
		}
		else{
			targetID = targetOrg.getWorkplaceGroupId().get();
		}
		this.getEntityManager().createQuery(DELETE)
						.setParameter("targetID", targetID)
						.setParameter("itemCd", itemCode.v())
						.setParameter("ymd", ymd)
						.executeUpdate();
	}

	private static ExtBudgetDaily toDomain (KscdtExtBudgetDailyNew entity){
		ExternalBudgetValues val = null;
		if(entity.budgetATR == 0)
			val = new ExternalBudgetTimeValue(entity.val);
		 else if(entity.budgetATR == 1)
			val = new ExtBudgetNumberPerson(entity.val);
		 else if(entity.budgetATR == 2)
			val = new ExternalBudgetMoneyValue(entity.val);
		 else if(entity.budgetATR == 3)
			val = new ExtBudgetNumericalVal(entity.val);
		ExtBudgetDaily domain = new ExtBudgetDaily(
				new TargetOrgIdenInfor(EnumAdaptor.valueOf( entity.pk.targetUnit, TargetOrganizationUnit.class),
						entity.pk.targetUnit == 0 ? Optional.ofNullable(entity.pk.targetID): Optional.empty(),
						entity.pk.targetUnit == 0 ? Optional.empty() : Optional.ofNullable(entity.pk.targetID)),
				new ExtBudgetActItemCode(entity.pk.itemCd),
				entity.pk.ymd,
				val) ;
		
		return domain;
	}
	private static KscdtExtBudgetDailyNew toEntity(ExtBudgetDaily dom){
		int budgetATR = 0;
		int val = 0;
		if(dom.getActualValue() instanceof ExternalBudgetTimeValue) {
			budgetATR = 0;
			ExternalBudgetTimeValue value = (ExternalBudgetTimeValue) dom.getActualValue(); 
			val = value.v();
		}
		else if(dom.getActualValue() instanceof ExtBudgetNumberPerson) {
			budgetATR = 1;
			ExtBudgetNumberPerson value = (ExtBudgetNumberPerson) dom.getActualValue(); 
			val = value.v();
		}
		else if(dom.getActualValue() instanceof ExternalBudgetMoneyValue) {
			budgetATR = 2;
			ExternalBudgetMoneyValue value = (ExternalBudgetMoneyValue) dom.getActualValue();
			val = value.v();
		}
		else if(dom.getActualValue() instanceof ExtBudgetNumericalVal) {
			budgetATR = 3;
			ExtBudgetNumericalVal value = (ExtBudgetNumericalVal) dom.getActualValue();
			val = value.v();
		}
		
		
		KscdtExtBudgetDailyPkNew pk =  new KscdtExtBudgetDailyPkNew(
				AppContexts.user().companyId(),
				dom.getTargetOrg().getUnit().value,
				dom.getTargetOrg().getUnit().value == 0 ? dom.getTargetOrg().getWorkplaceId().get() : dom.getTargetOrg().getWorkplaceGroupId().get(),
				dom.getYmd(),
				dom.getItemCode().v());
	
		KscdtExtBudgetDailyNew entity = new KscdtExtBudgetDailyNew(
				pk, 
				budgetATR, 
				val);
		
		return entity;
		
	}



}
