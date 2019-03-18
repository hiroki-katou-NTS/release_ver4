package nts.uk.ctx.at.function.dom.alarm.sendemail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BusinessException;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.mail.send.MailAttachedFileItf;
import nts.gul.mail.send.MailAttachedFilePath;
import nts.gul.mail.send.MailContents;
import nts.uk.ctx.at.function.dom.adapter.alarm.EmployeePubAlarmAdapter;
import nts.uk.ctx.at.function.dom.adapter.alarm.EmployeeSprPubAlarmAdapter;
import nts.uk.ctx.at.function.dom.adapter.alarm.IMailDestinationAdapter;
import nts.uk.ctx.at.function.dom.adapter.alarm.MailDestinationAlarmImport;
import nts.uk.ctx.at.function.dom.adapter.alarm.OutGoingMailAlarm;
import nts.uk.ctx.at.function.dom.alarm.export.AlarmExportDto;
import nts.uk.ctx.at.function.dom.alarm.export.AlarmListGenerator;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.mail.MailSender;
import nts.uk.shr.com.mail.SendMailFailedException;

@Stateless
public class AlarmSendEmailService implements SendEmailService {
	@Inject
	private IMailDestinationAdapter iMailDestinationAdapter;
	
	@Inject
	private MailSender mailSender;
	
	@Inject
	private EmployeeSprPubAlarmAdapter employeeSprPubAlarmAdapter;
	
	@Inject
	private EmployeePubAlarmAdapter employeePubAlarmAdapter;
	
	@Inject
	private AlarmListGenerator alarmListGenerator;
	
	public String alarmSendEmail(String companyID, GeneralDate executeDate, List<String> employeeTagetIds,
			List<String> managerTagetIds, List<ValueExtractAlarmDto> valueExtractAlarmDtos,
			MailSettingsParamDto mailSettingsParamDto,String currentAlarmCode) {
		return process(companyID, executeDate, employeeTagetIds, managerTagetIds, valueExtractAlarmDtos,mailSettingsParamDto,currentAlarmCode);
	}
	
	private String process(String companyID, GeneralDate executeDate, List<String> employeeTagetIds,
			List<String> managerTagetIds, List<ValueExtractAlarmDto> valueExtractAlarmDtos,
			MailSettingsParamDto mailSettingsParamDto,String currentAlarmCode){
		List<String> errors = new ArrayList<>();
		Integer functionID = 9; //function of Alarm list = 9
		
		boolean isErrorSendMailEmp = false;
		//Send mail for employee
		// get address email
		if (!CollectionUtil.isEmpty(employeeTagetIds)) {

			for (String employeeId : employeeTagetIds) {
				// 本人送信対象のアラーム抽出結果を抽出する
				List<ValueExtractAlarmDto> valueExtractAlarmEmpDtos = valueExtractAlarmDtos.stream()
						.filter(c -> employeeId.equals(c.getEmployeeID())).collect(Collectors.toList());
				try {
					// Do send email
					boolean isSucess = sendMail(companyID, employeeId, functionID,
							valueExtractAlarmEmpDtos, mailSettingsParamDto.getSubject(),
							mailSettingsParamDto.getText(), currentAlarmCode);
					if (!isSucess) {
						errors.add(employeeId);
					}
				} catch (SendMailFailedException e) {
					throw e;
				}
			}
		}
		// Send mail for Manager
		// get list employeeId of manager
		if (!isErrorSendMailEmp && !CollectionUtil.isEmpty(managerTagetIds)) {
			// 管理者送信対象のアラーム抽出結果を抽出する
			List<ValueExtractAlarmDto> valueExtractAlarmManagerDtos = new ArrayList<>();
			Map<String, List<ValueExtractAlarmDto>> mapCheckAlarm = new HashMap<>();
			List<ValueExtractAlarmDto> listTemp = null;
			// Get list workplace to send
			List<String> workplaceIds = new ArrayList<>();
			for (ValueExtractAlarmDto obj : valueExtractAlarmDtos) {
				if(managerTagetIds.contains(obj.getEmployeeID())){
					String workplaceID = obj.getWorkplaceID();
					// 管理者送信対象のアラーム抽出結果を職場でグループ化する
					if(!mapCheckAlarm.containsKey(workplaceID)){
						workplaceIds.add(workplaceID);
						// New list alarm to send
						listTemp = new ArrayList<>();
					}else{
						listTemp = mapCheckAlarm.get(workplaceID);
					}
					listTemp.add(obj);
					mapCheckAlarm.put(workplaceID, listTemp);
				}
			}
			
			for (String workplaceId : workplaceIds) {
				// Call request list 218 return list employee Id
				List<String> listEmployeeId = employeePubAlarmAdapter.getListEmployeeId(workplaceId,executeDate);
				// 抽出結果：ループ中の職場単位のアラーム抽出結果 
				valueExtractAlarmManagerDtos = mapCheckAlarm.get(workplaceId);
				if (!CollectionUtil.isEmpty(listEmployeeId)) {
					// loop send mail
					for (String employeeId : listEmployeeId) {
						try {
							// Get subject , body mail
							boolean isSucess = sendMail(companyID, employeeId, functionID,
									valueExtractAlarmManagerDtos,mailSettingsParamDto.getSubjectAdmin(),
									mailSettingsParamDto.getTextAdmin(),currentAlarmCode);
							if (!isSucess) {
								errors.add(employeeId);
							}
						} catch (SendMailFailedException e) {
							throw e;
						}
					}
				}
			}
		}
		String empployeeNameError = isErrorSendMailEmp + ";";// return status check display alert error

		if (!CollectionUtil.isEmpty(errors)) {
			int index = 0;
			int errorsSize = errors.size();
			for (String sId : errors) {
				// save using request list 346
				String empNames =  employeeSprPubAlarmAdapter.getEmployeeNameBySId(sId);
				if (!StringUtils.isEmpty(empNames)) {
					empployeeNameError += empNames;
				}
				index++;
				if(index != errorsSize){
					empployeeNameError += "<br/>";        
                }   
			}
		}
		
		return empployeeNameError;
	}
	
	/**
	 * Send mail flow employeeId
	 * @param companyID
	 * @param employeeId
	 * @param functionID
	 * @param listDataAlarmExport
	 * @param subjectEmail
	 * @param bodyEmail
	 * @return true : send mail successful/false : send mail error
	 */
	private boolean sendMail(String companyID, String employeeId, Integer functionID,
			List<ValueExtractAlarmDto> listDataAlarmExport, String subjectEmail,
			String bodyEmail,String currentAlarmCode) throws BusinessException {
		// call request list 397 return email address
		MailDestinationAlarmImport mailDestinationAlarmImport = iMailDestinationAdapter
				.getEmpEmailAddress(companyID, employeeId, functionID);
		if (mailDestinationAlarmImport != null) {
			// Get all mail address
			List<OutGoingMailAlarm> emails = mailDestinationAlarmImport.getOutGoingMails();
			if (CollectionUtil.isEmpty(emails)) {
				return true;
			} else {
				if(StringUtils.isEmpty(subjectEmail)){
					subjectEmail = TextResource.localize("KAL010_300");
				}
				// Genarate excel
				AlarmExportDto alarmExportDto = alarmListGenerator.generate(new FileGeneratorContext(), listDataAlarmExport,currentAlarmCode);
				// Create file attach
				List<MailAttachedFileItf> attachedFiles = new ArrayList<MailAttachedFileItf>();
				attachedFiles.add(new MailAttachedFilePath(alarmExportDto.getPath(), alarmExportDto.getFileName()));
				
				// Create mail content
				MailContents mailContent = new MailContents(subjectEmail, bodyEmail,attachedFiles);
				
				for (OutGoingMailAlarm outGoingMailAlarm : emails) {
					// If not email to return false
					if (StringUtils.isEmpty(outGoingMailAlarm.getEmailAddress())) {
						return false;
					}
					// Do send mail
					try {
						mailSender.sendFromAdmin(outGoingMailAlarm.getEmailAddress(), mailContent);
					} catch (SendMailFailedException e) {
						throw e;
					}
				}
			}
		}
		return true;
	}
}