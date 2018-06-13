package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.eclipse.persistence.jpa.jpql.parser.WhenClause;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.primitive.TimeAsMinutesPrimitiveValue;
import nts.arc.task.schedule.ScheduledJobUserData;
import nts.arc.task.schedule.cron.CronSchedule;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.RepeatMonthDayRepository;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.OneDayRepeatInterval;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.RepeatDetailSetting;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.TaskEndDate;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.TaskEndTime;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.DailyDaySetting;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatDetailSettingDaily;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatDetailSettingMonthly;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatDetailSettingWeekly;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatMonthDaysSelect;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatMonthSelect;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatWeekDaysSelect;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.WeeklyWeekSetting;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.EndDateClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.EndTimeClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.OneDayRepeatClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.RepeatContentItem;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.EndTime;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.OneDayRepeatIntervalDetail;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.StartTime;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.task.schedule.UkJobScheduleOptions;
import nts.uk.shr.com.task.schedule.UkJobScheduler;
import nts.uk.shr.sample.task.schedule.SampleScheduledJob;

@Stateless
public class SaveExecutionTaskSettingCommandHandler extends CommandHandlerWithResult<SaveExecutionTaskSettingCommand, String> {

	@Inject
	private ExecutionTaskSettingRepository execTaskSettingRepo;
	
	@Inject
	private RepeatMonthDayRepository repMonthDayRepo;
	
	@Inject
	private UkJobScheduler scheduler;

	@Override
	protected String handle(CommandHandlerContext<SaveExecutionTaskSettingCommand> context) {
		SaveExecutionTaskSettingCommand command = context.getCommand();
 		String companyId = AppContexts.user().companyId();
		// 終了時刻 
		TaskEndTime endTime =
				new TaskEndTime(
						EnumAdaptor.valueOf(command.getEndTimeCls(), EndTimeClassification.class),
						command.getEndTime() == null ? null : new EndTime(command.getEndTime()));
				
		// 繰り返し間隔
		OneDayRepeatInterval oneDayRepInr =
				new OneDayRepeatInterval(
						command.getOneDayRepInterval() == null ? null : EnumAdaptor.valueOf(command.getOneDayRepInterval(),OneDayRepeatIntervalDetail.class),
						EnumAdaptor.valueOf(command.getOneDayRepCls(), OneDayRepeatClassification.class));
		
		// 終了日日付指定
		TaskEndDate endDate = new TaskEndDate(EnumAdaptor.valueOf(command.getEndDateCls(), EndDateClassification.class), command.getEndDate());
		
		
		// 繰り返し詳細設定(毎週)
		RepeatDetailSettingWeekly weekly =
				new RepeatDetailSettingWeekly(
						new RepeatWeekDaysSelect(command.isMonday(), command.isTuesday(), command.isWednesday(),
												 command.isThursday(), command.isFriday(), command.isSaturday(), command.isSunday()));
		
		// 繰り返し詳細設定(毎月)
		List<RepeatMonthDaysSelect> days =
				command.getRepeatMonthDateList()
						.stream()
							.map(x -> EnumAdaptor.valueOf(x, RepeatMonthDaysSelect.class))
								.collect(Collectors.toList());
		RepeatMonthSelect months =
				new RepeatMonthSelect(command.isJanuary(), command.isFebruary(), command.isMarch(),
										command.isApril(), command.isMay(), command.isJune(),
										command.isJuly(), command.isAugust(), command.isSeptember(),
										command.isOctober(), command.isNovember(), command.isDecember());
		RepeatDetailSettingMonthly monthly = new RepeatDetailSettingMonthly(days, months);
		
		// 繰り返し詳細設定
		RepeatDetailSetting detailSetting = new RepeatDetailSetting( weekly, monthly);
		
		ExecutionTaskSetting taskSetting = new ExecutionTaskSetting(oneDayRepInr,
									new ExecutionCode(command.getExecItemCd()),
									companyId,
									command.isEnabledSetting(),
									null, 
									endDate,
									endTime,
									command.isRepeatCls(),
									EnumAdaptor.valueOf(command.getRepeatContent(), RepeatContentItem.class),
									detailSetting,
									command.getStartDate(),
									new StartTime(command.getStartTime()));
		/*
		// Calculate next execution date time
		taskSetting.setNextExecDateTime();
		*/
		 List<String> lstcron = this.getCron(command);
		val cron = new CronSchedule(Arrays.asList(lstcron.get(0)));
		// 登録チェック処理
		taskSetting.validate();
		GeneralDate startDate = command.getStartDate();
		GeneralDate endDate2 = command.getEndDate();
		
		val scheduletimeData = new ScheduledJobUserData();
		scheduletimeData.put("companyId", command.getCompanyId());
		scheduletimeData.put("execItemCd", command.getExecItemCd());
		UkJobScheduleOptions options ;
		UkJobScheduleOptions options2=null ;
		UkJobScheduleOptions options3=null ;
		UkJobScheduleOptions optionsEnd =null ;
		// repeat day, week month
		if(command.isRepeatCls()){
			if(command.getEndTimeCls()==1 && command.getEndDateCls()==1){
				 options = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, cron)
							.userData(scheduletimeData)
							.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
							.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
							.startClock(new StartTime(command.getStartTime()))
							.endClock(new EndTime(command.getEndTime()))
							.build();
				 if(lstcron.size()>=2){
				 		options2 = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, new CronSchedule(Arrays.asList(lstcron.get(1))))
				 				.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
								.startClock(new StartTime(command.getStartTime()))
								.endClock(new EndTime(command.getEndTime()))
								.build();
				 	}
				 	if(lstcron.size()==3){
				 		options3 = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, new CronSchedule(Arrays.asList(lstcron.get(2))))
				 				.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
								.startClock(new StartTime(command.getStartTime()))
								.endClock(new EndTime(command.getEndTime()))
								.build();
				 	}
				 
				 optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 "+(command.getEndTime()%60)+" "+(command.getEndTime()/60)+" * * ? ")))
							.userData(scheduletimeData)
							.startDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
							.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
							.startClock(new StartTime(command.getEndTime()))
							.endClock(new EndTime(command.getEndTime()+1))
							.build();
			}else if(command.getEndTimeCls()==1 && command.getEndDateCls()!=1){
				if(command.getOneDayRepCls()==1){
					options = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class,cron)
							.userData(scheduletimeData)
							.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
							.startClock(new StartTime(command.getStartTime()))
							.build();
					if(lstcron.size()>=2){
				 		options2 = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, new CronSchedule(Arrays.asList(lstcron.get(1))))
								.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.startClock(new StartTime(command.getStartTime()))
								.build();
				 	}
				 	if(lstcron.size()==3){
				 		options3 = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, new CronSchedule(Arrays.asList(lstcron.get(2))))
								.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.startClock(new StartTime(command.getStartTime()))
								.build();
				 	}
				 	
				}else{
					options = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, cron)
							.userData(scheduletimeData)
							.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
							.startClock(new StartTime(command.getStartTime()))
							.build();
				}
			}else if(command.getEndTimeCls()!=1 && command.getEndDateCls()==1){
				options = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, cron)
						.userData(scheduletimeData)
						.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
						.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
						.startClock(new StartTime(command.getStartTime()))
						.endClock(new EndTime(1439))
						.build();
				//loop minute
				if(command.getOneDayRepCls()==1){
					optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 0 0 * * ? ")))
							.userData(scheduletimeData)
							.startDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()+1))
							.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()+1))
							.startClock(new StartTime(0))
							.endClock(new EndTime(1))
							.build();
				}else{
					int minuteEnd =  command.getStartTime()%60 +10;
					int hourEnd = command.getStartTime()/60;
					if(minuteEnd/60==1){
						hourEnd = hourEnd+1;
						minuteEnd = minuteEnd-60;
					}
					boolean inCreaseDay =false;
					if(hourEnd/24==1){
						hourEnd=0;
						inCreaseDay=true;
					}
					if(inCreaseDay){
						optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 "+(minuteEnd)+" "+(hourEnd)+" * * ? ")))
								.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()+1))
								.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()+1))
								.startClock(new StartTime(command.getStartTime()+10-1440))
								.endClock(new EndTime(command.getStartTime()+10-1440+1))
								.build();
					}else{
						optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 "+(minuteEnd)+" "+(hourEnd)+" * * ? ")))
								.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
								.endDate(GeneralDate.ymd(endDate2.year(), endDate2.month(), endDate2.day()))
								.startClock(new StartTime(command.getStartTime()+10))
								.endClock(new EndTime(command.getStartTime()+11))
								.build();	
					}
					
				}
				
			}else{
				options = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, cron)
						.userData(scheduletimeData)
						.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
						.startClock(new StartTime(command.getStartTime()))
						.build();
			}	
			
		}else{
		// not repeat day, week month	
			if(command.getEndTimeCls()==1){
				 options = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, cron)
							.userData(scheduletimeData)
							.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
							.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
							.startClock(new StartTime(command.getStartTime()))
							.endClock(new EndTime(command.getEndTime()))
							.build();
				 	if(lstcron.size()>=2){
				 		options2 = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, new CronSchedule(Arrays.asList(lstcron.get(1))))
								.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.startClock(new StartTime(command.getStartTime()))
								.endClock(new EndTime(command.getEndTime()))
								.build();
				 	}
				 	if(lstcron.size()==3){
				 		options3 = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, new CronSchedule(Arrays.asList(lstcron.get(2))))
								.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
								.startClock(new StartTime(command.getStartTime()))
								.endClock(new EndTime(command.getEndTime()))
								.build();
				 	}
				 
				 optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 "+(command.getEndTime()%60)+" "+(command.getEndTime()/60)+" * * ? ")))
					.userData(scheduletimeData)
					.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
					.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
					.startClock(new StartTime(command.getEndTime()))
					.endClock(new EndTime(command.getEndTime()+1))
					.build();
			}else{
				 options = UkJobScheduleOptions.builder(SortingProcessScheduleJob.class, cron)
							.userData(scheduletimeData)
							.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
							.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()+1))
							.startClock(new StartTime(command.getStartTime()))
							.endClock(new EndTime(0))
							.build();
				
				//loop minute
					if(command.getOneDayRepCls()==1){
						optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 0 0 * * ? ")))
								.userData(scheduletimeData)
								.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()+1))
								.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()+1))
								.startClock(new StartTime(0))
								.endClock(new EndTime(1))
								.build();
					}else{
						int minuteEnd =  command.getStartTime()%60 +10;
						int hourEnd = command.getStartTime()/60;
						if(minuteEnd/60==1){
							hourEnd = hourEnd+1;
							minuteEnd = minuteEnd-60;
						}
						boolean inCreaseDay =false;
						if(hourEnd/24==1){
							hourEnd=0;
							inCreaseDay=true;
						}
						if(inCreaseDay){
							optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 "+minuteEnd+" "+hourEnd+" * * ? ")))
									.userData(scheduletimeData)
									.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()+1))
									.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()+1))
									.startClock(new StartTime(command.getStartTime()+10-1440))
									.endClock(new EndTime(command.getStartTime()+11-1440))
									.build();	
						}else{
							optionsEnd = UkJobScheduleOptions.builder(SortingProcessEndScheduleJob.class,new CronSchedule(Arrays.asList("0 "+minuteEnd+" "+hourEnd+" * * ? ")))
									.userData(scheduletimeData)
									.startDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
									.endDate(GeneralDate.ymd(startDate.year(), startDate.month(), startDate.day()))
									.startClock(new StartTime(command.getStartTime()+10))
									.endClock(new EndTime(command.getStartTime()+11))
									.build();
						}
					}
			}
		}
		String scheduleId = this.scheduler.scheduleOnCurrentCompany(options).getScheduleId();
		taskSetting.setScheduleId(scheduleId);
		Optional<GeneralDateTime> nextFireTime = this.scheduler.getNextFireTime(SortingProcessScheduleJob.class, scheduleId);
		taskSetting.setNextExecDateTime(nextFireTime);
		String endScheduleId=null;
		if(optionsEnd!=null){
			endScheduleId = this.scheduler.scheduleOnCurrentCompany(optionsEnd).getScheduleId();
		}
		taskSetting.setEndScheduleId(endScheduleId);
		
		if (command.isNewMode()) {
			try {
				this.execTaskSettingRepo.insert(taskSetting);
				this.repMonthDayRepo.insert(companyId, command.getExecItemCd(), days);
			} catch (Exception e) {
				this.scheduler.unscheduleOnCurrentCompany(SortingProcessScheduleJob.class,scheduleId);
				if(endScheduleId!=null){
					this.scheduler.unscheduleOnCurrentCompany(SortingProcessEndScheduleJob.class,endScheduleId);
				}
				throw new BusinessException("Msg_1110");
			}
			
		} else {
			ExecutionTaskSetting executionTaskSetting = this.execTaskSettingRepo.getByCidAndExecCd(companyId, command.getExecItemCd()).get();
			String oldScheduleId = executionTaskSetting.getScheduleId();
			Optional<String> oldEndScheduleIdOpt = executionTaskSetting.getEndScheduleId();
			if(oldEndScheduleIdOpt.isPresent()){
				this.scheduler.unscheduleOnCurrentCompany(SortingProcessScheduleJob.class,oldEndScheduleIdOpt.get());
			}
			this.scheduler.unscheduleOnCurrentCompany(SortingProcessScheduleJob.class,oldScheduleId);
			try {
				this.execTaskSettingRepo.remove(companyId,  command.getExecItemCd());
				this.repMonthDayRepo.removeAllByCidAndExecCd(companyId, command.getExecItemCd());
				this.execTaskSettingRepo.insert(taskSetting);
				this.repMonthDayRepo.insert(companyId, command.getExecItemCd(), days);
				//this.execTaskSettingRepo.update(taskSetting);
			} catch (Exception e) {
				this.scheduler.unscheduleOnCurrentCompany(SortingProcessScheduleJob.class,scheduleId);
				if(endScheduleId!=null){
					this.scheduler.unscheduleOnCurrentCompany(SortingProcessEndScheduleJob.class,endScheduleId);
				}
				throw new BusinessException("Msg_1110");
			}
			
		}
		return taskSetting.getExecItemCd().v();
	}
	
	private List<String> getCron(SaveExecutionTaskSettingCommand command){
		List<String> lstCron = new ArrayList<String>();
		
		Integer  repeatMinute = null;
		Integer startTime = command.getStartTime();
		 int startHours = startTime/60;
		 int startMinute = startTime%60;
		 
		 Integer endTime =null;
		 Integer endMinute =null;
		 Integer endHour =null;
		 if(command.getEndTimeCls()==1){
			  endTime = command.getEndTime();
			   endMinute = endTime%60 ;
			   endHour = endTime/60;
		 }
		 Integer startTimeRun = null;
		 
		if(command.getOneDayRepInterval()!=null){
			switch (command.getOneDayRepInterval().intValue()) {
			case 0:
				  repeatMinute = 1;
				break;
			case 1:
				  repeatMinute = 5;
				break;
			case 2:
				  repeatMinute = 10;
				break;
			case 3:
				  repeatMinute = 15;
				break;
			case 4:
				  repeatMinute = 20;
				break;
			case 5:
				  repeatMinute = 30;
				break;
			case 6:
				  repeatMinute = 60;
				break;	
			default:
				repeatMinute = 1;
				break;
			}	
		}
		
		if(repeatMinute!=null){
			startTimeRun = 	startMinute%repeatMinute;
		}
		
		command.getOneDayRepInterval();
		
		StringBuilder cronExpress = new StringBuilder();
		StringBuilder cronExpress2 = null;
		StringBuilder cronExpress3 = null;
		switch (command.getRepeatContent().intValue()) {
		case 0: //day
			if(repeatMinute==null){
				cronExpress.append("0 "+startMinute+" "+startHours+" * * ? ");
			}else{
				if(repeatMinute ==60){
					if(command.getEndTimeCls()==1){
						if(command.getEndTime()%60>startMinute){
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+command.getEndTime()/60+"/1 * * ? ");
						}else{
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+(command.getEndTime()/60-1)+"/1 * * ? ");
						}
					}else{
						cronExpress.append("0 "+startMinute+" "+startHours+"/1 * * ? ");
					}
				}else{
					if(command.getEndTimeCls()==1){
						if(endHour-startHours ==0){
							cronExpress = this.getCron(startMinute, endMinute, repeatMinute, startHours);
							cronExpress.append(" * * ? ");
						}else if(endHour-startHours==1){
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" * * ? ");
							cronExpress2.append(" * * ? ");
							
						}else{
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCronRangeMoreTwo(startTimeRun, 60, repeatMinute,startHours, endHour);
							cronExpress3 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" * * ? ");
							cronExpress2.append(" * * ? ");
							cronExpress3.append(" * * ? ");
						}
					}else{
						cronExpress.append("0 0/"+repeatMinute+" * * * ? ");	
					}
				}
				
			}
				break;
		case 1: //week
			
			if(!command.isSunday()&&!command.isMonday()&& !command.isTuesday()&&!command.isWednesday()&&!command.isThursday()&&!command.isFriday()&&!command.isSaturday())
			{
				if(repeatMinute==null){
					cronExpress.append("0 "+startMinute+" "+startHours+" * * ? ");
				}else{
					if(repeatMinute ==60){
						if(command.getEndTimeCls()==1){
							if(command.getEndTime()%60>startMinute){
								cronExpress.append("0 "+startMinute+" "+startHours+"-"+command.getEndTime()/60+"/1 * * ? ");
							}else{
								cronExpress.append("0 "+startMinute+" "+startHours+"-"+(command.getEndTime()/60-1)+"/1 * * ? ");
							}
						}else{
							cronExpress.append("0 "+startMinute+" "+startHours+"/1 * * ? ");
						}
					}else{
						if(command.getEndTimeCls()==1){
							if(endHour-startHours ==0){
								cronExpress = this.getCron(startMinute, endMinute, repeatMinute, startHours);
								cronExpress.append(" ? * * ");
							}else if(endHour-startHours==1){
								cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
								cronExpress2 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
								cronExpress.append(" ? * * ");
								cronExpress2.append(" ? * * ");
								
							}else{
								cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
								cronExpress2 = this.getCronRangeMoreTwo(startTimeRun, 60, repeatMinute,startHours, endHour);
								cronExpress3 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
								cronExpress.append(" ? * * ");
								cronExpress2.append(" ? * * ");
								cronExpress3.append(" ? * * ");
							}
						}else{
							cronExpress.append("0 0/"+repeatMinute+" * ? * * ");
						}
					}
					
				}
				break;
			}
			
			if(repeatMinute==null){
				cronExpress.append("0 "+startMinute+" "+startHours+" ? ");
			}else{
				if(repeatMinute ==60){
					if(command.getEndTimeCls()==1){
						if(command.getEndTime()%60>startMinute){
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+command.getEndTime()/60+"/1 ? ");
						}else{
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+(command.getEndTime()/60-1)+"/1 ? ");
						}
					}else{
						cronExpress.append("0 "+startMinute+" "+startHours+"/1 ? ");
					}
					
				}else{
					if(command.getEndTimeCls()==1){
						if(endHour-startHours ==0){
							cronExpress = this.getCron(startMinute, endMinute, repeatMinute, startHours);
							cronExpress.append(" ? ");
						}else if(endHour-startHours==1){
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" ? ");
							cronExpress2.append(" ? ");
							
						}else{
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCronRangeMoreTwo(startTimeRun, 60, repeatMinute,startHours, endHour);
							cronExpress3 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" ? ");
							cronExpress2.append(" ? ");
							cronExpress3.append(" ? ");
						}
					}else{
						cronExpress.append("0 0/"+repeatMinute+" *	 ? " );
					}
				}
				
			}
			cronExpress.append(" * ");
			if(command.isSunday()){
				cronExpress.append("SUN,");	
			}
			if(command.isMonday()){
				cronExpress.append("MON,");	
			}
			if(command.isTuesday()){
				cronExpress.append("TUE,");	
			}
			if(command.isWednesday()){
				cronExpress.append("WED,");	
			}
			if(command.isThursday()){
				cronExpress.append("THU,");
			}
			if(command.isFriday()){
				cronExpress.append("FRI,");
			}
			if(command.isSaturday()){
				cronExpress.append("SAT");
			}
			if(cronExpress.toString().endsWith(",")){
				cronExpress.deleteCharAt(cronExpress.length()-1);
			}
			break;
		case 2: //month	
			List<Integer> repeatMonthDateList = command.getRepeatMonthDateList();
			/*
			if(command.getRepeatMonthDateList().isEmpty()){
				if(repeatMinute==null){
					cronExpress.append("0 "+startMinute+" "+startHours+" ? ");
				}else
				{
					if(command.getEndTimeCls()==1){
						if(endHour-startHours ==0){
							cronExpress = Test.getCron(startMinute, endMinute, repeatMinute, startHours);
							cronExpress.append(" ? ");
						}else if(endHour-startHours==1){
							cronExpress = Test.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = Test.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" ? ");
							cronExpress2.append(" ? ");
							
						}else{
							cronExpress = Test.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = Test.getCronRangeMoreTwo(startTimeRun, 60, repeatMinute,startHours, endHour);
							cronExpress3 = Test.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" ? ");
							cronExpress2.append(" ? ");
							cronExpress3.append(" ? ");
						}
					}else{
						cronExpress.append("0 0/"+repeatMinute+" * ? ");
					}	
				}
				
				if(command.isJanuary()){
					cronExpress.append("JAN,");
					if(cronExpress2!=null){
						cronExpress2.append("JAN,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("JAN,");
					}
				}
				if(command.isFebruary()){
					cronExpress.append("FEB,");
					if(cronExpress2!=null){
						cronExpress2.append("FEB,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("FEB,");
					}
				}
				if(command.isMarch()){
					cronExpress.append("MAR,");
					if(cronExpress2!=null){
						cronExpress2.append("MAR,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("MAR,");
					}
				}
				if(command.isApril()){
					cronExpress.append("APR,");
					if(cronExpress2!=null){
						cronExpress2.append("APR,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("APR,");
					}
				}
				if(command.isMay()){
					cronExpress.append("MAY,");
					if(cronExpress2!=null){
						cronExpress2.append("MAY,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("MAY,");
					}
				}
				if(command.isJune()){
					cronExpress.append("JUN,");
					if(cronExpress2!=null){
						cronExpress2.append("JUN,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("JUN,");
					}
				}
				if(command.isJuly()){
					cronExpress.append("JUL,");
					if(cronExpress2!=null){
						cronExpress2.append("JUL,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("JUL,");
					}
				}
				if(command.isAugust()){
					cronExpress.append("AUG,");
					if(cronExpress2!=null){
						cronExpress2.append("AUG,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("AUG,");
					}
				}
				if(command.isSeptember()){
					cronExpress.append("SEP,");
					if(cronExpress2!=null){
						cronExpress2.append("SEP,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("SEP,");
					}
				}
				if(command.isOctober()){
					cronExpress.append("OCT,");
					if(cronExpress2!=null){
						cronExpress2.append("OCT,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("OCT,");
					}
				}
				if(command.isNovember()){
					cronExpress.append("NOV,");
					if(cronExpress2!=null){
						cronExpress2.append("NOV,");
					}
					if(cronExpress3!=null){
						cronExpress3.append("NOV,");
					}
				}
				if(command.isDecember()){
					cronExpress.append("DEC");
					if(cronExpress2!=null){
						cronExpress2.append("DEC");
					}
					if(cronExpress3!=null){
						cronExpress3.append("DEC");
					}
				}
				if(cronExpress.toString().endsWith(",")){
					cronExpress.deleteCharAt(cronExpress.length()-1);
					if(cronExpress2!=null){
						cronExpress2.deleteCharAt(cronExpress.length()-1);
					}
					if(cronExpress3!=null){
						cronExpress3.deleteCharAt(cronExpress.length()-1);
					}
				}
				cronExpress.append(" * *");
				if(cronExpress2!=null){
					cronExpress2.append(" * *");
				}
				if(cronExpress3!=null){
					cronExpress3.append(" * *");
				}
				break;
			}
			*/
			
			if(repeatMinute==null){
				cronExpress.append("0 "+startMinute+" "+startHours+" ");
			}else{
				if(repeatMinute ==60){
					if(command.getEndTimeCls()==1){
						if(command.getEndTime()%60>startMinute){
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+command.getEndTime()/60+"/1 ");
						}else{
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+(command.getEndTime()/60-1)+"/1 ");
						}
					}else{
						cronExpress.append("0 "+startMinute+" "+startHours+"/1 ");
					}
				}else{
					if(command.getEndTimeCls()==1){
						if(endHour-startHours ==0){
							cronExpress = this.getCron(startMinute, endMinute, repeatMinute, startHours);
						}else if(endHour-startHours==1){
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							
						}else{
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCronRangeMoreTwo(startTimeRun, 60, repeatMinute,startHours, endHour);
							cronExpress3 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
						}
					}else{
						cronExpress.append("0 0/"+repeatMinute+" * ");
					}
				}
			}
			
			for (int dayOfMonth : repeatMonthDateList) {
				cronExpress.append(dayOfMonth+ ",");
			}
			
			if(cronExpress.toString().endsWith(",")){
				cronExpress.deleteCharAt(cronExpress.length()-1);
			}
			cronExpress.append(" ");
			if(cronExpress2!=null){
				for (int dayOfMonth : repeatMonthDateList) {
					cronExpress2.append(dayOfMonth+ ",");
				}
				if(cronExpress2.toString().endsWith(",")){
					cronExpress2.deleteCharAt(cronExpress2.length()-1);
				}
				cronExpress2.append(" ");
			}
			if(cronExpress3!=null){
				for (int dayOfMonth : repeatMonthDateList) {
					cronExpress3.append(dayOfMonth+ ",");
				}
				if(cronExpress3.toString().endsWith(",")){
					cronExpress3.deleteCharAt(cronExpress3.length()-1);
				}
				cronExpress3.append(" ");
			}
			
			if(command.isJanuary()){
				cronExpress.append("JAN,");
				if(cronExpress2!=null){
					cronExpress2.append("JAN,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("JAN,");
				}
			}
			if(command.isFebruary()){
				cronExpress.append("FEB,");
				if(cronExpress2!=null){
					cronExpress2.append("FEB,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("FEB,");
				}
			}
			if(command.isMarch()){
				cronExpress.append("MAR,");
				if(cronExpress2!=null){
					cronExpress2.append("MAR,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("MAR,");
				}
			}
			if(command.isApril()){
				cronExpress.append("APR,");
				if(cronExpress2!=null){
					cronExpress2.append("APR,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("APR,");
				}
			}
			if(command.isMay()){
				cronExpress.append("MAY,");
				if(cronExpress2!=null){
					cronExpress2.append("MAY,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("MAY,");
				}
			}
			if(command.isJune()){
				cronExpress.append("JUN,");
				if(cronExpress2!=null){
					cronExpress2.append("JUN,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("JUN,");
				}
			}
			if(command.isJuly()){
				cronExpress.append("JUL,");
				if(cronExpress2!=null){
					cronExpress2.append("JUL,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("JUL,");
				}
			}
			if(command.isAugust()){
				cronExpress.append("AUG,");
				if(cronExpress2!=null){
					cronExpress2.append("AUG,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("AUG,");
				}
			}
			if(command.isSeptember()){
				cronExpress.append("SEP,");
				if(cronExpress2!=null){
					cronExpress2.append("SEP,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("SEP,");
				}
			}
			if(command.isOctober()){
				cronExpress.append("OCT,");
				if(cronExpress2!=null){
					cronExpress2.append("OCT,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("OCT,");
				}
			}
			if(command.isNovember()){
				cronExpress.append("NOV,");
				if(cronExpress2!=null){
					cronExpress2.append("NOV,");
				}
				if(cronExpress3!=null){
					cronExpress3.append("NOV,");
				}
			}
			if(command.isDecember()){
				cronExpress.append("DEC");
				if(cronExpress2!=null){
					cronExpress2.append("DEC");
				}
				if(cronExpress3!=null){
					cronExpress3.append("DEC");
				}
			}
			if(cronExpress.toString().endsWith(",")){
				cronExpress.deleteCharAt(cronExpress.length()-1);
			}
			cronExpress.append(" ?");
			if(cronExpress2!=null){
				if(cronExpress2.toString().endsWith(",")){
					cronExpress2.deleteCharAt(cronExpress2.length()-1);
				}
				cronExpress2.append(" ?");
			}
			if(cronExpress3!=null){
				if(cronExpress3.toString().endsWith(",")){
					cronExpress3.deleteCharAt(cronExpress3.length()-1);
				}
				cronExpress3.append(" ?");
			}
			
			break;
		default:
			if(repeatMinute==null){
				cronExpress.append("0 "+startMinute+" "+startHours+" * * ?");
			}else{
				if(repeatMinute ==60){
					if(command.getEndTimeCls()==1){
						if(command.getEndTime()%60>startMinute){
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+command.getEndTime()/60+"/1 * * ?");
						}else{
							cronExpress.append("0 "+startMinute+" "+startHours+"-"+(command.getEndTime()/60-1)+"/1 * * ?");
						}
					}else{
						cronExpress.append("0 "+startMinute+" "+startHours+"/1 * * ?");	
					}
				}else{
					if(command.getEndTimeCls()==1){
						
						if(endHour-startHours ==0){
							cronExpress = this.getCron(startMinute, endMinute, repeatMinute, startHours);
							cronExpress.append(" * * ? ");
						}else if(endHour-startHours==1){
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" * * ? ");
							cronExpress2.append(" * * ? ");
							
						}else{
							cronExpress = this.getCron(startMinute, 60, repeatMinute, startHours);
							cronExpress2 = this.getCronRangeMoreTwo(startTimeRun, 60, repeatMinute,startHours, endHour);
							cronExpress3 = this.getCron(startTimeRun, endMinute, repeatMinute, endHour);
							cronExpress.append(" * * ? ");
							cronExpress2.append(" * * ? ");
							cronExpress3.append(" * * ? ");
						}
					}else{
						cronExpress.append("0 0/"+repeatMinute+" * * * ? ");	
					}
				}
			}
				break;
		}
		lstCron.add(cronExpress.toString());
		if(cronExpress2!=null){
			lstCron.add(cronExpress2.toString());
		}
		if(cronExpress3!=null){
			lstCron.add(cronExpress3.toString());
		}
		
		return lstCron;
	}
	private  StringBuilder  getCron(int startTemp, int endTemp, int loopTemp, int hourTemp  ){
		StringBuilder cronExpress = new StringBuilder();
		cronExpress.append("0 ");
		int startMinuteTemp = startTemp;
		while(startMinuteTemp<=endTemp){
			if(startMinuteTemp!=60){
				cronExpress.append(startMinuteTemp);
				cronExpress.append(",");	
			}
			startMinuteTemp = startMinuteTemp + loopTemp ;
		}
		if(cronExpress.toString().endsWith(",")){
			cronExpress.deleteCharAt(cronExpress.length()-1);
		}
		cronExpress.append(" "+hourTemp+" ");
		return cronExpress;
	}
	
	private  StringBuilder  getCronRangeMoreTwo(int startTemp, int endTemp, int loopTemp, int startHourTemp , int endHourTemp  ){
		StringBuilder cronExpress = new StringBuilder();
		cronExpress.append("0 ");
		int startMinuteTemp = startTemp;
		while(startMinuteTemp<=endTemp){
			if(startMinuteTemp!=60){
			cronExpress.append(startMinuteTemp);
			cronExpress.append(",");
			}
			startMinuteTemp = startMinuteTemp + loopTemp ;
		}
		if(cronExpress.toString().endsWith(",")){
			cronExpress.deleteCharAt(cronExpress.length()-1);
		}
		if(endHourTemp-startHourTemp==2){
			cronExpress.append(" "+(endHourTemp-1)+" ");
		}else{
			cronExpress.append(" "+(startHourTemp+1)+"-"+(endHourTemp-1)+" ");
		}
		return cronExpress;
	}
	
}
