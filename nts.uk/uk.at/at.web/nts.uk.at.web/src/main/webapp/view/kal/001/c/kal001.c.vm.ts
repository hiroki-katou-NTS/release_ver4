module nts.uk.at.view.kal001.c {
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    export module viewmodel {
        export class ScreenModel {
            //table
            columns: KnockoutObservableArray<any>;//nts.uk.ui.NtsGridListColumn
            currentSelectedRow: KnockoutObservable<any>;
            //data
            listEmployee: KnockoutObservableArray<modeldto.EmployeeSendEmail> = ko.observableArray([]);
            listEmployeeChecked: KnockoutObservableArray<modeldto.EmployeeSendEmail>;
            shareEmployees : KnockoutObservableArray<modeldto.ShareEmployee>; 
            isSendToMe: KnockoutObservable<boolean>;
            isSendToManager: KnockoutObservable<boolean>;
            isSendToManager: KnockoutObservable<boolean>;
            
            mailSettingsParamDto: MailSettingsParamDto;
            listEmployeeSendTaget : KnockoutObservableArray<string>; 
            listManagerSendTaget : KnockoutObservableArray<string>; 
            listValueExtractAlarmDto : KnockoutObservableArray<ValueExtractAlarmDto>; 
            
            constructor( shareEmployees: Array<modeldto.ShareEmployee>) {
                let self = this;
                self.currentSelectedRow = ko.observable(null);
                self.shareEmployees  = ko.observableArray(shareEmployees);
                self.isSendToMe = ko.observable(true);
                self.isSendToManager = ko.observable(true);                                              
                self.listEmployeeSendTaget = ko.observableArray([]);                                              
                self.listManagerSendTaget = ko.observableArray([]);
                let paramFromb = nts.uk.ui.windows.getShared("extractedAlarmData");    
                self.listValueExtractAlarmDto = paramFromb.listAlarmExtraValueWkReDto;                                         
            }

            /**
             * functiton start pagea
             */
            startPage(): JQueryPromise<any>  {
                let self = this;
                let dfd = $.Deferred(); 
                self.columns = ko.observableArray([
                    { headerText: '',  dataType: 'string', key: 'GUID' },
                    { headerText: getText('KAL001_23'),  dataType: 'boolean', key: 'isSendToMe', showHeaderCheckbox: true, width: 100, ntsControl: 'isSendToMe' },
                    { headerText: getText('KAL001_24'),  dataType: 'boolean',  key: 'isSendToManager', showHeaderCheckbox: true, width: 100, ntsControl: 'isSendToManager' },
                    { headerText: getText('KAL001_27'), key: 'workplaceName', width: 170 },
                    { headerText: getText('KAL001_25'), key: 'employeeCode', width: 170 },
                    { headerText: getText('KAL001_26'), key: 'employeeName', width: 170 }
                ]);
                
                service.getEmployeeSendEmail(self.shareEmployees()).done((listEmployeeDto: Array<modeldto.EmployeeDto>) => {
                    _.map(listEmployeeDto, (e) => {
                        self.listEmployee.push(
                            new modeldto.EmployeeSendEmail(e));
                    });
                    // create table
                    $("#grid").ntsGrid({
                        height: '450px',
                        dataSource: self.listEmployee(),
                        hidePrimaryKey: true,
                        primaryKey: 'GUID',
                        virtualization: true,
                        rowVirtualization: true,
                        virtualizationMode: 'continuous',
                        columns: self.columns(),
                        features:
                        [],

                        ntsControls: [
                            { name: 'isSendToMe', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                            { name: 'isSendToManager', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                        ]
                    });
                    dfd.resolve();
                }).fail((error) => {
                    alertError(error);
                });
                return dfd.promise();                
            }//end start page 

             closeDialog(){
                 nts.uk.ui.windows.close();
            }
             sendEmail(){
                 let self = this;
                  var MailSettingsDefault = ({subject : "", text : "", mailAddressCC : [], mailAddressBCC : [], mailRely : ""});
                 service.getAllMailSet().done(function(data: MailAutoAndNormalDto) {
                     if (data) {
                         data.mailSettingNormalDto.mailSettings = data.mailSettingNormalDto.mailSettings == null ? MailSettingsDefault : data.mailSettingNormalDto.mailSettings;
                         data.mailSettingNormalDto.mailSettingAdmins = data.mailSettingNormalDto.mailSettingAdmins == null ? MailSettingsDefault : data.mailSettingNormalDto.mailSettingAdmins;
                         data.mailSettingAutomaticDto.mailSettings = data.mailSettingAutomaticDto.mailSettings == null ? MailSettingsDefault : data.mailSettingAutomaticDto.mailSettings;
                         data.mailSettingAutomaticDto.mailSettingAdmins = data.mailSettingAutomaticDto.mailSettingAdmins == null ? MailSettingsDefault : data.mailSettingAutomaticDto.mailSettingAdmins;
                         // setting subject , body mail
                         self.mailSettingsParamDto = new MailSettingsParamDto(data.mailSettingNormalDto.mailSettings.subject,data.mailSettingNormalDto.mailSettings.text,data.mailSettingNormalDto.mailSettingAdmins.subject,data.mailSettingNormalDto.mailSettingAdmins.text);
                         
                         // check status
                         let isHaveChecked = false;
                         self.listEmployeeChecked = ko.observableArray([]);
                         self.listEmployeeSendTaget = ko.observableArray([]);
                         self.listManagerSendTaget = ko.observableArray([]);
                         _.forEach(self.listEmployee(), function(item: modeldto.EmployeeSendEmail) {
                             if (item.isSendToMe == true && item.isSendToManager == true)
                             {
                                 isHaveChecked = true;
                                 self.listEmployeeChecked.push(item);
                                 //set to employeee list taget
                                 self.listEmployeeSendTaget.push(item.employeeId);
                                 self.listManagerSendTaget.push(item.workplaceId);
                                     
                             }
                             else if (item.isSendToMe == true )
                             {
                                 isHaveChecked = true;
                                 self.listEmployeeChecked.push(item);
                                 
                                 //set to employeee list taget
                                 self.listEmployeeSendTaget.push(item.employeeId);
                             }
                             else if (item.isSendToManager == true)
                             {
                                 isHaveChecked = true;
                                 self.listEmployeeChecked.push(item);
                                 
                                 //set to Manager list taget
                                 self.listManagerSendTaget.push(item.workplaceId);
                             }
                         });
                         
                         if(isHaveChecked){
                             let params ={
                               listEmployeeSendTaget: self.listEmployeeSendTaget(),
                               listManagerSendTaget: self.listManagerSendTaget(),
                               listValueExtractAlarmDto: self.listValueExtractAlarmDto,
                               mailSettingsParamDto: self.mailSettingsParamDto
                             };
                             // call service send mail
                             service.alarmListSendEmail(params).done(function(data: string) {
                                 info({ messageId: 'Msg_207' }).then(() => {
                                     if (data.length > 0) {
                                         let strDisplay = nts.uk.resource.getMessage('Msg_965')+"<br/>"+data;
                                         info({ message: strDisplay });
                                     }
                                 });
                             });
                         } else {
                             alertError({ messageId: 'Msg_657' });
                         }
                         
                     } else {
                         alertError({ messageId: 'Msg_1169' });
                     }
                     
                 });       
                 return;
            }



        }//end screenModel
    }//end viewmodel  

    //module model
    export module modeldto {

        export class EmployeeSendEmail {
            GUID: string;
            isSendToMe:  KnockoutObservable<boolean>;
            isSendToManager: KnockoutObservable<boolean>;
            workplaceId: string;
            workplaceName: string;
            employeeId: string;
            employeeCode: string;
            employeeName: string;
            constructor(e: EmployeeDto ) {
                this.GUID = nts.uk.util.randomId();
                this.isSendToMe = ko.observable(false);
                this.isSendToManager = ko.observable(false);
                this.workplaceId = e.workplaceId;
                this.workplaceName = e.workplaceName;
                this.employeeId = e.employeeId;
                this.employeeCode = e.employeeCode;
                this.employeeName = e.employeeName;
            }

        }
        
        export interface EmployeeDto{
            employeeId: string;
            employeeCode: string;
            employeeName: string;
            workplaceId: string;
            workplaceName: string;   
        }

        export class ShareEmployee{
            employeeID: string;
            workplaceID: string;
        }
        
        
        export interface MailSettingsDto {
            subject?: string;
            text?: string;
            mailAddressCC: Array<string>;
            mailAddressBCC: Array<string>;
            mailRely?: string;
        }
         export interface MailSettingNormalDto {
             mailSettings?: MailSettingsDto;
             mailSettingAdmins?: MailSettingsDto;
         }
         export interface MailSettingAutomaticDto {
             mailSettings?: MailSettingsDto;
             mailSettingAdmins?: MailSettingsDto;
             senderAddress?: string;
         }
         export interface MailAutoAndNormalDto {
             mailSettingAutomaticDto: MailSettingAutomaticDto;
             mailSettingNormalDto: MailSettingNormalDto;
         }
    }//end module model
    
    export interface MailSettingsParamDto {
        subject : string;
        text : string;
        subjectAdmin : string;
        textAdmin : string;
    }
    
    export interface ValueExtractAlarmDto{
            guid: string;
            workplaceID : string;
            hierarchyCd : string;
            workplaceName : string;
            employeeID : string;
            employeeCode : string;
            employeeName : string;
            alarmValueDate : string;
            category : number;
            categoryName: string;
            alarmItem : string;
            alarmValueMessage : string;
            comment : string;            
        } 

}//end module