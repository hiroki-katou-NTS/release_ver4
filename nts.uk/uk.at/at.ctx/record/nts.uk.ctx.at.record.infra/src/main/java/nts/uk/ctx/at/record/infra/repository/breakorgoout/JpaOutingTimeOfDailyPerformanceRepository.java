package nts.uk.ctx.at.record.infra.repository.breakorgoout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.infra.entity.breakorgoout.KrcdtDaiOutingTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class JpaOutingTimeOfDailyPerformanceRepository extends JpaRepository
		implements OutingTimeOfDailyPerformanceRepository {

	private static final String REMOVE_BY_EMPLOYEE;
	
	private static final String DEL_BY_LIST_KEY;
	
	private static final String SELECT_BY_EMPLOYEE_AND_DATE;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		REMOVE_BY_EMPLOYEE = builderString.toString();
		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE a.krcdtDaiOutingTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd = :ymd ");
		SELECT_BY_EMPLOYEE_AND_DATE = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiOutingTime a ");
		builderString.append("WHERE WHERE a.krcdtDaiOutingTimePK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiOutingTimePK.ymd IN :ymds ");
		DEL_BY_LIST_KEY = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).executeUpdate();
	}

	@Override
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds) {
		this.getEntityManager().createQuery(DEL_BY_LIST_KEY).setParameter("employeeIds", employeeIds)
		.setParameter("ymds", ymds).executeUpdate();	
	}

	@Override
	public Optional<OutingTimeOfDailyPerformance> findByEmployeeIdAndDate(String employeeId, GeneralDate ymd) {
		List<KrcdtDaiOutingTime> lstKrcdtDaiOutingTime = this.queryProxy().query(SELECT_BY_EMPLOYEE_AND_DATE,KrcdtDaiOutingTime.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getList();
		 if(lstKrcdtDaiOutingTime==null || lstKrcdtDaiOutingTime.isEmpty()){
			 return Optional.empty();
		 }
		 List<OutingTimeSheet> lstOutingTimeSheet = new ArrayList<OutingTimeSheet>();
		 
		 lstKrcdtDaiOutingTime.forEach(x->{
			 WorkStamp outStamp  = new WorkStamp(x.outStampRoundingTimeDay!=null? new TimeWithDayAttr(x.outStampRoundingTimeDay.intValue()):null,x.outStampTime!=null?new TimeWithDayAttr(x.outStampTime.intValue()):null,x.outStampPlaceCode!=null?new WorkLocationCD(x.outStampPlaceCode):null ,x.outStampSourceInfo!=null? EnumAdaptor.valueOf(x.outStampSourceInfo.intValue(), StampSourceInfo.class):null);
			 WorkStamp outActualStamp = new WorkStamp(x.outActualRoundingTimeDay!=null?new TimeWithDayAttr(x.outActualRoundingTimeDay.intValue()):null,x.outActualTime!=null?new TimeWithDayAttr(x.outActualTime.intValue()):null,x.outActualPlaceCode!=null?new WorkLocationCD(x.outActualPlaceCode):null ,x.outActualSourceInfo!=null? EnumAdaptor.valueOf(x.outActualSourceInfo.intValue(), StampSourceInfo.class):null);
			 TimeActualStamp goOut = new TimeActualStamp(outActualStamp, outStamp, x.outNumberStamp.intValue());
			 WorkStamp backStamp  = new WorkStamp(x.backStampRoundingTimeDay!=null?new TimeWithDayAttr(x.backStampRoundingTimeDay.intValue()):null,x.backStampTime!=null? new TimeWithDayAttr(x.backStampTime.intValue()):null,x.backStampPlaceCode!=null? new WorkLocationCD(x.backStampPlaceCode):null ,x.backStampSourceInfo!=null? EnumAdaptor.valueOf(x.backStampSourceInfo.intValue(), StampSourceInfo.class):null);
			 WorkStamp backActualStamp = new WorkStamp(x.backActualRoundingTimeDay!=null?new TimeWithDayAttr(x.backActualRoundingTimeDay.intValue()):null,x.backActualTime!=null?new TimeWithDayAttr(x.backActualTime.intValue()):null,x.backActualPlaceCode!=null?new WorkLocationCD(x.backActualPlaceCode):null ,x.backActualSourceInfo!=null? EnumAdaptor.valueOf(x.backActualSourceInfo.intValue(), StampSourceInfo.class):null);
			 TimeActualStamp comeBack = new TimeActualStamp(backActualStamp, backStamp, x.backNumberStamp.intValue());
			 GoingOutReason reasonForGoOut = EnumAdaptor.valueOf(x.outingReason.intValue(), GoingOutReason.class);
			 AttendanceTime outingTimeCalculation = new AttendanceTime(x.outingTimeCalculation.intValue());
			 AttendanceTime outingTime = new AttendanceTime(x.outingTime.intValue());
			 OutingTimeSheet outingTimeSheet = new OutingTimeSheet(new OutingFrameNo(x.krcdtDaiOutingTimePK.outingFrameNo),goOut,outingTimeCalculation, outingTime, reasonForGoOut , comeBack);
			 lstOutingTimeSheet.add(outingTimeSheet);
		 });
		 return Optional.ofNullable(new OutingTimeOfDailyPerformance(employeeId, ymd, lstOutingTimeSheet));
	}
	
	

}
