module nts.uk.at.view.kdl003.a {
    export module viewmodel {
        export class ScreenModel {

            // Search options.
            startTimeOption: KnockoutObservable<number>;
            startTime: KnockoutObservable<number>;
            endTimeOption: KnockoutObservable<number>;
            endTime: KnockoutObservable<number>;
            
            readonly: KnockoutObservable<boolean>;
            enable1: KnockoutObservable<boolean>;
            enable2: KnockoutObservable<boolean>;
            
            currentCode: KnockoutObservable<any>;
            currentCode2: KnockoutObservable<any>;
            currentCode3: KnockoutObservable<any>;
            currentCode4: KnockoutObservable<any>;
            
            startBTime: KnockoutObservable<number>;
            endBTime: KnockoutObservable<number>;
            
            startBBTime: KnockoutObservable<number>;
            endBBTime: KnockoutObservable<number>;
            
            timeZone1: KnockoutObservable<any>;
            timeZone2: KnockoutObservable<any>;

            // Data list & selected code.
            listWorkTime: KnockoutObservableArray<WorkTimeSet>;
            selectedWorkTimeCode: KnockoutObservable<string>;
            listWorkType: KnockoutObservableArray<WorkType>;
            selectedWorkTypeCode: KnockoutObservable<string>;
            
            listTimeBreak1: KnockoutObservableArray<BreakTime>;
            listTimeBreakBreak1: KnockoutObservableArray<BreakTime>;
            listTimeBreak2: KnockoutObservableArray<BreakTime>;
            listTimeBreakBreak2: KnockoutObservableArray<BreakTime>;
            
            styleWorkTime: KnockoutObservable<string>;

            // Define columns.
            workTimeColumns: KnockoutObservableArray<NtsGridListColumn>;
            workTypeColumns: KnockoutObservableArray<NtsGridListColumn>;
            
            breakColumns1: KnockoutObservableArray<NtsGridListColumn>;
            breakBreakColumns1: KnockoutObservableArray<NtsGridListColumn>;
            breakColumns2: KnockoutObservableArray<NtsGridListColumn>;
            breakBreakColumns2: KnockoutObservableArray<NtsGridListColumn>;

            // Initial work time code list..
            initialWorkTimeCodes: Array<String>;

            // Parameter from caller screen.
            callerParameter: CallerParameter;

            constructor(parentData: CallerParameter) {
                var self = this;
                
                self.readonly = ko.observable(true);
                self.enable1 = ko.observable(true);
                self.enable2 = ko.observable(true);

                // Search options
                self.startTimeOption = ko.observable(1);
                self.startTime = ko.observable(null);
                self.endTimeOption = ko.observable(1);
                self.endTime = ko.observable(null);
                
                self.startBTime = ko.observable(null);
                self.endBTime = ko.observable(null);
                self.startBBTime = ko.observable(null);
                self.endBBTime = ko.observable(null);
                
                self.timeZone1 = ko.observable(null);
                self.timeZone2 = ko.observable(null);
                
                self.styleWorkTime = ko.observable(null);

                self.listWorkTime = ko.observableArray([]);
                self.selectedWorkTimeCode = ko.observable('');
                self.listWorkType = ko.observableArray([]);
                self.selectedWorkTypeCode = ko.observable('');
                
                self.listTimeBreak1 = ko.observableArray([]);
                self.listTimeBreakBreak1 = ko.observableArray([]);
                self.listTimeBreak2 = ko.observableArray([]);
                self.listTimeBreakBreak2 = ko.observableArray([]);
                
                self.currentCode = ko.observable();
                self.currentCode2 = ko.observable();
                self.currentCode3 = ko.observable();
                self.currentCode4 = ko.observable();

                // Define gridlist's columns
                self.workTypeColumns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KDL003_5'), prop: 'workTypeCode', width: 50 },
                    { headerText: nts.uk.resource.getText('KDL003_6'), prop: 'name', width: 100 },
                    { headerText: nts.uk.resource.getText('KDL003_7'), prop: 'memo', width: 130 }
                ]);
                self.workTimeColumns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KDL003_5'), prop: 'code', width: 50 },
                    { headerText: nts.uk.resource.getText('KDL003_6'), prop: 'name', width: 100 },
                    { headerText: nts.uk.resource.getText('KDL003_19'), prop: 'workTime1', width: 200 },
                    { headerText: nts.uk.resource.getText('KDL003_20'), prop: 'workTime2', width: 200 },
                    { headerText: nts.uk.resource.getText('KDL003_21'), prop: 'workAtr', width: 100 },
                    { headerText: nts.uk.resource.getText('KDL003_7'), prop: 'remark', template: '<span>${remark}</span>' }
                ]);
                self.breakColumns1 = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KDL003_33'), prop: 'code', width: 30 },
                    { headerText: nts.uk.resource.getText('KDL003_34'), prop: 'name', width: 170 }
                ]);
                self.breakBreakColumns1 = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KDL003_33'), prop: 'code', width: 30 },
                    { headerText: nts.uk.resource.getText('KDL003_34'), prop: 'name', width: 170 }
                ]);         
                self.breakColumns2 = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KDL003_33'), prop: 'code', width: 30 },
                    { headerText: nts.uk.resource.getText('KDL003_34'), prop: 'name', width: 170 }
                ]);
                self.breakBreakColumns2 = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KDL003_33'), prop: 'code', width: 30 },
                    { headerText: nts.uk.resource.getText('KDL003_34'), prop: 'name', width: 170 }
                ]);     
                
                // On selectedWorkTimeCode changed event.
                self.selectedWorkTimeCode.subscribe(code => {
                    self.getListTimeBySeleckedCode(code);
                    self.loadWorkTime(code);
                }); 
                
                //parent data
                self.callerParameter = parentData;
            }

            /**
             * Start page.
             */
            private startPage(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                nts.uk.ui.block.invisible();
                $.when(self.loadWorkTime(self.callerParameter.workTimeCodes), self.loadWorkType())
                    .done(() => {
                        // Set initial selection.
                        self.initWorkTypeSelection();
                        self.setWorkTimeSelection();

                        // On selectedWorkTypeCode changed event.
                        self.selectedWorkTypeCode.subscribe(code => {
                            if (nts.uk.util.isNullOrEmpty(self.listWorkTime())) {
                                return;
                            }
                            if (!code) {
                                return;
                            }
                            service.isWorkTimeSettingNeeded(code).done(val => {
                                switch (val) {
                                    case SetupType.NOT_REQUIRED:
                                        self.selectedWorkTimeCode('000');
                                        break;
                                    default: // Do nothing.
                                }
                            });
                        });

                        // Set initial work time list.
                        self.initialWorkTimeCodes = _.map(self.listWorkTime(), function(item) { return item.code })

                        dfd.resolve();
                    })
                    .fail(function(res) {
                        nts.uk.ui.dialog.alertError(res);
                    }).always(() => {
                        nts.uk.ui.block.clear();
                    });
                return dfd.promise();
            }
            
            

            /**
             * Load work time.
             */
            private loadWorkTime(code: string): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // Find work time by list code if caller's parameters exist.
                if (!nts.uk.util.isNullOrEmpty(self.callerParameter.workTimeCodes)) {
                    service.findWorkTimeByCodes(self.callerParameter.workTimeCodes)
                        .done(function(data) {
                            self.getTimeBySeleckedCode(data, code);
                            self.addFirstItem(data);
                            self.listWorkTime(data);
                            dfd.resolve();
                        });
                } else {
                    // Find all work time
                    service.findAllWorkTime()
                        .done(function(data) {
                            self.getTimeBySeleckedCode(data, code);
                            self.addFirstItem(data);
                            self.listWorkTime(data);
                            dfd.resolve();
                        });
                }
                
                return dfd.promise();
            }

            /**
             * Load work type.
             */
            private loadWorkType(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // Find work type by list code if caller's parameters exist.
                if (!nts.uk.util.isNullOrEmpty(self.callerParameter.workTypeCodes)) {
                    service.findWorkTypeByCodes(self.callerParameter.workTypeCodes)
                        .done(function(workTypeList: Array<WorkType>) {
                            self.listWorkType(workTypeList);
                            dfd.resolve();
                    });
                } else {
                    // Find all work type.
                    service.findAllWorkType()
                        .done(function(workTypeList: Array<WorkType>) {
                            self.listWorkType(workTypeList);
                            dfd.resolve();
                    });
                }
                return dfd.promise();
            }
            
            private getTimeBySeleckedCode(data: Array<WorkTimeSet>, code: string): void{
                let self = this;
                $.each(data, function( key, value ) {
                    if (data[key].code == code){
                        if (!nts.uk.util.isNullOrEmpty(data[key].workTime1)){
                            let codeTime = data[key].workTime1;
                            let array = codeTime.split(' ~ ');
                            self.startBTime(array[0].substring(2));
                            self.endBTime(array[1].substring(2));
                            self.enable1(true);
                            self.timeZone1(codeTime);
                        } else {
                            self.startBTime(":");
                            self.endBTime(":");
                            self.enable1(false);
                            self.timeZone1('');
                        };
                        if (!nts.uk.util.isNullOrEmpty(data[key].workTime2)){
                            let codeTime = data[key].workTime2;
                            let array = codeTime.split(' ~ ');
                            self.startBBTime(array[0].substring(2));
                            self.endBBTime(array[1].substring(2));
                            self.enable2(true);
                            self.timeZone2(codeTime);
                        } else {
                            self.startBBTime(":");
                            self.endBBTime(":");
                            self.enable2(false);
                            self.timeZone2('');
                        };
                        
                        self.styleWorkTime(data[key].workAtr);
                    }
                });
            }
            
            private getListTimeBySeleckedCode(code: string): void{
                let self = this;
                let emptyArray = {code: "", name: ""};
                let dataBB = [];
                let dataB = [];
                let dataEmpty = [];
                if (code === "000"){
                    for (let i = 0; i < 5; i++){
                        dataEmpty.push(emptyArray);
                    }
                    
                    self.listTimeBreak1(dataEmpty);
                    self.listTimeBreak2(dataEmpty);
                    self.listTimeBreakBreak1(dataEmpty);
                    self.listTimeBreakBreak2(dataEmpty);
                    self.startBBTime(":");
                    self.endBBTime(":");
                    self.enable2(false);
                    self.startBTime(":");
                    self.endBTime(":");
                    self.enable1(false);
                } else {
                    service.findBreakByCodes(code).done(function(data){
                        dataBB = data.breakBreakTimeDto;
                        dataB = data.breakTimeDto;
                        if (dataB.length <= 5 && dataBB.length <= 5){
                            for (let i = 0; i < 5; i++){
                                dataEmpty.push(emptyArray);
                            }
                            for (let i = dataB.length; i < 5; i++){
                                dataB.push(emptyArray);
                            }
                            for (let i = dataBB.length; i < 5; i++){
                                dataBB.push(emptyArray);
                            }
                            
                            self.listTimeBreak1(dataB);
                            self.listTimeBreak2(dataEmpty);
                            self.listTimeBreakBreak1(dataBB);
                            self.listTimeBreakBreak2(dataEmpty);
                        } else {
                            if(dataB.length > 5){
                                for (let i = 0; i < 5 ; i++){
                                    self.listTimeBreak1(dataB[i]);
                                }
                                for (let i = 5; i < dataB.length ; i++){
                                    self.listTimeBreak2(dataB[i]);
                                }
                            } else {
                                for (let i = 0; i < 5 ; i++){
                                    self.listTimeBreakBreak1(dataBB[i]);
                                }
                                for (let i = 5; i < dataB.length ; i++){
                                    self.listTimeBreakBreak2(dataBB[i]);
                                }
                            }
                        }
                    });
                }
            }
            
            /**
             * Add first work time item.
             */
            private addFirstItem(data: Array<WorkTimeSet>): void {
                let self = this;
                 //add item　なし
                data.unshift({
                    code: "000",
                    name: "なし",
                    workTime1: "",
                    workTime2: "",
                    workAtr: "",
                    remark: ""
                });
                self.listWorkTime(data);
            }
            
            /**
             * Initial work type selection 
             */
            private initWorkTypeSelection(): void {
                let self = this;

                // Return if list work type is null or empty
                if (nts.uk.util.isNullOrEmpty(self.listWorkType())) {
                    return;
                };

                // Selected code from caller screen.
                let selectedWorkTypeCode = self.callerParameter.selectedWorkTypeCode;
                let isInSelectableCodes = selectedWorkTypeCode ? _.find(self.listWorkType(), item => selectedWorkTypeCode == item.workTypeCode) : false;
                if (selectedWorkTypeCode && isInSelectableCodes) {
                    self.selectedWorkTypeCode(selectedWorkTypeCode);
                } else {
                    // Select first item.
                    self.selectedWorkTypeCode(_.first(self.listWorkType()).workTypeCode);
                }
            }

            /**
             * Set work time selection.
             */
            private setWorkTimeSelection(): void {
                let self = this;

                // Return if list work time is null or empty
                if (nts.uk.util.isNullOrEmpty(self.listWorkTime())) {
                    return;
                };

                // Selected code from caller screen.
                let selectedWorkTimeCode = self.callerParameter.selectedWorkTimeCode;
                let isInSelectableCodes = selectedWorkTimeCode ? _.find(self.listWorkTime(), item => selectedWorkTimeCode == item.code) : false;
                if (selectedWorkTimeCode && isInSelectableCodes) {
                    self.selectedWorkTimeCode(selectedWorkTimeCode);
                } else {
                    // Select first item.
                    self.selectedWorkTimeCode(_.first(self.listWorkTime()).code);
                }
            }

            /**
             * Search work time.
             */
            public search(): void {
                var self = this;
                if (!self.startTime() && !self.endTime()) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_53" });
                    return;
                }
                if ($('#inputEndTime').ntsError('hasError') ||
                    $('#inputStartTime').ntsError('hasError')) {
                    return;
                }
                nts.uk.ui.block.invisible();

                // Search command.
                let command = {
                    codelist: self.initialWorkTimeCodes,
                    startAtr: self.startTimeOption(),
                    startTime: nts.uk.util.isNullOrEmpty(self.startTime()) ? -1 : self.startTime(),
                    endAtr: self.endTimeOption(),
                    endTime: nts.uk.util.isNullOrEmpty(self.endTime()) ? -1 : self.endTime()
                }

                // Search & display data.
                service.findByTime(command)
                    .done(function(data) {
                        self.listWorkTime(data);
                        if (!nts.uk.util.isNullOrEmpty(data)) {
                            self.selectedWorkTimeCode(data[0].code);
                        }
                    })
                    .fail(function(res) {
                        nts.uk.ui.dialog.alertError(res);
                    }).always(() => {
                        // Set focus.
                        $("[tabindex='10']").focus();

                        nts.uk.ui.block.clear();
                    });
            }

            /**
             * Clear search condition.
             */
            public clearSearchCondition(): void {
                nts.uk.ui.block.invisible();
                var self = this;

                // Reset search conditions
                self.startTimeOption(1);
                self.startTime(null);
                self.endTimeOption(1);
                self.endTime(null);

                // Clear errors.
                $('#inputEndTime').ntsError('clear');
                $('#inputStartTime').ntsError('clear');

                // Reload list work time.
                self.loadWorkTime(self.callerParameter.workTimeCodes).always(() => {
                    nts.uk.ui.block.clear();
                });

                // Set focus.
                $("[tabindex='10']").focus();
            }

            /**
             * Submit.
             */
            public submit() {
                let self = this;
                let dfd = $.Deferred<void>();

                let workTypeCode = self.selectedWorkTypeCode();
                let workTimeCode = self.selectedWorkTimeCode();
                let time1 =  self.timeZone1();
                let time2 =  self.timeZone2();

                if (!workTypeCode) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_10" });
                    return;
                }

                // Loading, block ui.
                nts.uk.ui.block.invisible();

                // Set work time = ã�ªã�— if list work time is empty.
                if (!workTimeCode || nts.uk.util.isNullOrEmpty(self.listWorkTime())) {
                    workTimeCode = '000';
                }

                // Check pair work type & work time.
                service.checkPairWorkTypeWorkTime(workTypeCode, workTimeCode).done(() => {

                    if (workTimeCode === '000') {
                        workTimeCode = '';
                    }

                    // Set shared data.
                    let workTypeName = self.getWorkTypeName(workTypeCode);
                    let workTimeName = self.getWorkTimeName(workTimeCode);
                    let returnedData = {
                        selectedWorkTypeCode: workTypeCode,
                        selectedWorkTypeName: workTypeName,
                        selectedWorkTimeCode: workTimeCode,
                        selectedWorkTimeName: workTimeName,
                        firstTime: time1,
                        secondTime: time2
                    };
                    nts.uk.ui.windows.setShared("childData", returnedData, false);

                    // Close dialog.
                    self.closeDialog();

                }).fail(error => {
                    nts.uk.ui.dialog.alertError(error);
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
                return dfd.promise();
            }

            /**
             * Get work type name.
             */
            private getWorkTypeName(workTypeCode: string): string {
                let self = this;
                let name: string = '';
                if (self.listWorkTime()) {
                    let workType = _.find(self.listWorkType(), workType => workType.workTypeCode == workTypeCode);
                    name = workType ? workType.name : '';
                }
                return name;
            }

            /**
             * Get work time name.
             */
            private getWorkTimeName(workTimeCode: string): string {
                let self = this;
                let name = '';
                if (!nts.uk.util.isNullOrEmpty(self.listWorkTime())) {
                    let workTime = _.find(self.listWorkTime(), workTime => workTime.code == workTimeCode);
                    name = workTime ? workTime.name : '';
                }
                return name;
            }

            /**
             * Close dialog.
             */
            public closeDialog(): void {
                nts.uk.ui.windows.close();
            }
        }
        
        interface BreakTime {
            code: string;
            name: string;
        }

        interface WorkTimeSet {
            code: string;
            name: string;
            workTime1: string;
            workTime2: string;
            workAtr: string;
            remark: string;
        }
        export class WorkType {
            abbreviationName: string;
            companyId: string;
            displayAtr: number;
            memo: string;
            name: string;
            sortOrder: number;
            symbolicName: string;
            workTypeCode: string;
        }
        interface CallerParameter {
            workTypeCodes: Array<string>;
            selectedWorkTypeCode: string;
            workTimeCodes: Array<string>;
            selectedWorkTimeCode: string;
        }
        interface ReturnedData {
            selectedWorkTypeCode: string;
            selectedWorkTypeName: string;
            selectedWorkTimeCode: string;
            selectedWorkTimeName: string;
            firstTime: string;
            secondTime: string;
        }
        enum SetupType {
            REQUIRED = 0,
            OPTIONAL = 1,
            NOT_REQUIRED = 2
        }
    }
}