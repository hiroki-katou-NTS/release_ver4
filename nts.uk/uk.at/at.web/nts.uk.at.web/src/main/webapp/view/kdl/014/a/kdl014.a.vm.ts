module kdl014.a.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<StampModel>;
        columns: KnockoutObservableArray<NtsGridListColumn>;
        employeeCD: string;
        employeeName: string;
        startDate: string;
        endDate: string;

        constructor() {
            var self = this;
            self.items = ko.observableArray([]);
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KDL014_4"), key: 'date', width: 130 },
                { headerText: nts.uk.resource.getText("KDL014_5"), key: 'attendanceTime', width: 100 },
                { headerText: nts.uk.resource.getText("KDL014_6"), key: 'stampAtrName', width: 80 },
                { headerText: nts.uk.resource.getText("KDL014_11"), key: 'stampMethodName', width: 120 },
                { headerText: nts.uk.resource.getText("KDL014_13"), key: 'stampReasonName', width: 80 },
                { headerText: nts.uk.resource.getText("KDL014_7"), key: 'workLocationName', width: 170 },
                { headerText: nts.uk.resource.getText("KDL014_12"), key: 'stampCombinationName', width: 100 }
            ]);
            self.employeeCode = '';
            self.employeeName = '';
            self.startDate = '';
            self.endDate = '';
            self.employeeCD = '';
            $("#igGridStamp").igGrid({
                width: '810px',
                height: '400px',
                dataSource: self.items(),
                columns: self.columns()
            });
        }

        /** Start page */
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            // Get list stamp
            let startTemp = nts.uk.ui.windows.getShared("kdl014startDateA");
            let endTemp = nts.uk.ui.windows.getShared("kdl014endDateA");
            self.startDate = moment(startTemp, 'YYYYMMDD').format('YYYY/MM/DD (ddd)') + '  ~';
            self.endDate = moment(endTemp, 'YYYYMMDD').format('YYYY/MM/DD (ddd)');
            self.employeeCD = nts.uk.ui.windows.getShared("kdl014employeeCodeA");
            
            //demo
            self.employeeName = "name" + self.employeeCD;
            let lstCardNumber: Array<string> = [];
            let lstSource: Array<StampModel> = [];
            //get list Card Number
            service.getPersonIdByEmployee(self.employeeCD).done(function(employeeInfo: any) {
                if (employeeInfo !== undefined) {
                    let personId: string = employeeInfo.personId;
                    //get list Card Number
                    service.getStampNumberByPersonId(personId).done(function(lstStampNumber: any) {
                        _.forEach(lstStampNumber, function(value) {
                            lstCardNumber.push(value.cardNumber.toString());
                        });
                        //get list Stamp 
                        service.getStampByCode(lstCardNumber, startTemp, endTemp).done(function(lstStamp: any) {
                            if (lstStamp.length > 0) {
                                _.forEach(lstStamp, function(item) {
                                    lstSource.push(new StampModel(moment(item.date, 'YYYY/MM/DD').format('YYYY/MM/DD (ddd)'), _.padStart(nts.uk.time.parseTime(item.attendanceTime, true).format(), 5, '0'), item.stampReasonName, item.stampAtrName, item.stampMethodName, item.workLocationName, item.stampCombinationName));
                                });
                            };
                            //set list data source
                            self.items(_.orderBy(lstSource, ['date', 'attendanceTime'], ['asc', 'asc']));
                            $("#igGridStamp").igGrid({ dataSource: self.items() });
                            dfd.resolve();
                        }).fail(function(res) {
                            dfd.reject();
                        });
                        dfd.resolve();
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alertError(res.message);
                        dfd.reject();
                    });
                }
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject();
            });
            //get PersonId
            let employeeCode:string ='00003       ';
            service.getPersonByEmpCode(employeeCode).done(function(employeeInfo: any) {

                console.log(employeeInfo);
                
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res.message);
                dfd.reject();
            });
            
            return dfd.promise();

        }

        /**Close function*/
        close() {
            nts.uk.ui.windows.close();
        }
    }


    class StampModel {
        date: string;
        attendanceTime: string;
        stampReasonName: string;
        stampAtrName: string;
        stampMethodName: string;
        workLocationName: string;
        stampCombinationName: string;
        constructor(date: string, attendanceTime: string, stampReasonName: string, stampAtrName: string, stampMethodName: string, workLocationName: string, stampCombinationName: string) {
            var self = this;
            self.date = date;
            self.attendanceTime = attendanceTime;
            self.stampReasonName = stampReasonName;
            self.stampAtrName = stampAtrName;
            self.stampMethodName = stampMethodName;
            self.workLocationName = workLocationName;
            self.stampCombinationName = stampCombinationName;
        }
    }
}