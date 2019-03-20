module nts.uk.at.view.kal001.b {
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import textUK = nts.uk.text;
    import block = nts.uk.ui.block;
    import errors = nts.uk.ui.errors;
    export module viewmodel {
        export class ScreenModel {

            columns: KnockoutObservableArray<any>;
            currentSelectedRow: KnockoutObservable<any>;

            dataSource : Array<model.ValueExtractAlarmDto>=[];
            flgActive : KnockoutObservable<boolean>;
            constructor() {
                let self = this;
                self.currentSelectedRow = ko.observable(null);
                self.flgActive = ko.observable(true);
                self.columns = ko.observableArray([
                    { headerText: '', key: 'guid', width: 1 ,hidden :true },
                    { headerText: getText('KAL001_20'), key: 'workplaceName', width: 100 },
                    { headerText: getText('KAL001_13'), key: 'employeeCode', width: 110 },
                    { headerText: getText('KAL001_14'), key: 'employeeName', width: 150 },
                    { headerText: getText('KAL001_15'), key: 'alarmValueDate', width: 190},
                    { headerText: getText('KAL001_16'), key: 'categoryName', width: 120},
                    { headerText: getText('KAL001_17'), key: 'alarmItem', width: 150 },
                    { headerText: getText('KAL001_18'), key: 'alarmValueMessage', width: 200 },
                    { headerText: getText('KAL001_19'), key: 'comment', width: 200 }
                ]);

            }

            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                $("#grid").igGrid({ 
                        height: '500px',
                        dataSource: self.dataSource,
                        primaryKey: 'guid',
                        columns: self.columns(), 
                        features: [
                            { name: 'Paging', type: 'local', pageSize: 20 },
                            {
                              name: "Tooltips",
                              columnSettings: [ 
                                                { columnKey: "workplaceName", allowTooltips: true }
                                              ]
                            }
                        ],
                        enableTooltip : true
                        });
                                

                dfd.resolve();

                return dfd.promise();
            }

            exportExcel(): void {
                let self = this;
                block.invisible();
                let params = {
                    data: self.dataSource,
                    currentAlarmCode : nts.uk.ui.windows.getShared("extractedAlarmData").currentAlarmCode
                };
                service.exportAlarmData(params).done(() => {

                }).fail((errExcel) =>{
                    alertError(errExcel);
                }).always(()=>{
                    block.clear();    
                });
            }
            
            sendEmail(): void {
                let self = this;
                let shareEmployee = _.map(self.dataSource, (item) =>{
                   return {
                    employeeID: item.employeeID, 
                    workplaceID: item.workplaceID,
                    workplaceName:item.workplaceName
                    }; 
                });
                nts.uk.ui.windows.setShared("employeeList", _.uniqWith(shareEmployee, _.isEqual));
                //nts.uk.ui.windows.setShared("employeeList", _.uniqWith(_.filter(shareEmployee,function(x){return x.workplaceID !=null;} ), _.isEqual));
                modal("/view/kal/001/c/index.xhtml").onClosed(() => {
                    
                });
                
            }
            
            closeDialog(){
                 nts.uk.ui.windows.close();
            }


        }
    }
    
            



    export module model {

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
    }
    

}