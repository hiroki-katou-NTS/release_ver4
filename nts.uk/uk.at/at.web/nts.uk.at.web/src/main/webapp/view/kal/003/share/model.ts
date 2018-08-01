module nts.uk.at.view.kal003.share.model {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;
    import getText = nts.uk.resource.getText;

    export function getListCategory(): Array<ItemModel> {
        return [
//            new ItemModel(0, getText('Enum_AlarmCategory_SCHEDULE_DAILY')),
//            new ItemModel(1, getText('Enum_AlarmCategory_SCHEDULE_WEEKLY')),
            new ItemModel(2, getText('Enum_AlarmCategory_SCHEDULE_4WEEK')),
//            new ItemModel(3, getText('Enum_AlarmCategory_SCHEDULE_MONTHLY')),
//            new ItemModel(4, getText('Enum_AlarmCategory_SCHEDULE_YEAR')),
            new ItemModel(5, getText('Enum_AlarmCategory_DAILY')),
//            new ItemModel(6, getText('Enum_AlarmCategory_WEEKLY')),
            new ItemModel(7, getText('Enum_AlarmCategory_MONTHLY')),
//            new ItemModel(8, getText('Enum_AlarmCategory_APPLICATION_APPROVAL')),
            new ItemModel(9, getText('Enum_AlarmCategory_MULTIPLE_MONTH')),
//            new ItemModel(10, getText('Enum_AlarmCategory_ANY_PERIOD')),
//            new ItemModel(11, getText('Enum_AlarmCategory_ATTENDANCE_RATE_FOR_HOLIDAY')),
            new ItemModel(12, getText('Enum_AlarmCategory_AGREEMENT')),
//            new ItemModel(13, getText('Enum_AlarmCategory_MAN_HOUR_CHECK'))
        ];
    }

    export function getConditionToExtractDaily(): Array<ItemModel> {
        return [
            new model.ItemModel(0, getText('Enum_ConExtractedDaily_ALL')),
            new model.ItemModel(1, getText('Enum_ConExtractedDaily_CONFIRMED_DATA')),
            new model.ItemModel(2, getText('Enum_ConExtractedDaily_UNCONFIRMER_DATA'))
        ];
    }

    export function getSchedule4WeekAlarmCheckCondition(): Array<ItemModel> {
        return [
            new model.ItemModel(0, '実績のみで4週4休をチェックする'),
            new model.ItemModel(1, 'スケジュールと実績で4週4休をチェックする')
        ];
    }

    export function getErrorClassification(): Array<ItemModel> {
        return [
            new model.ItemModel(0, getText('Enum_ErrorAlarmClassification_Error')),
            new model.ItemModel(1, getText('Enum_ErrorAlarmClassification_Alarm')),
            new model.ItemModel(2, getText('Enum_ErrorAlarmClassification_Other'))
        ];
    }

    export class AlarmCheckConditionByCategory {
        code: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        category: KnockoutObservable<number>;
        displayCode: string;
        displayName: string;
        displayCategory: string;
        availableRoles: KnockoutObservableArray<string>;
        targetCondition: KnockoutObservable<AlarmCheckTargetCondition>;
        displayAvailableRoles: KnockoutObservable<string>;
        dailyAlarmCheckCondition: KnockoutObservable<DailyAlarmCheckCondition> = ko.observable(new DailyAlarmCheckCondition(DATA_CONDITION_TO_EXTRACT.ALL, false, [], [], []));
        schedule4WeekAlarmCheckCondition: KnockoutObservable<Schedule4WeekAlarmCheckCondition> = ko.observable(new Schedule4WeekAlarmCheckCondition(SCHEDULE_4_WEEK_CHECK_CONDITION.FOR_ACTUAL_RESULTS_ONLY));
        action: KnockoutObservable<number> = ko.observable(0);
        condAgree36: KnockoutObservable<Agreement36> = ko.observable(new Agreement36([], []));
        monAlarmCheckCon: KnockoutObservable<MonAlarmCheckCon> = ko.observable(new MonAlarmCheckCon([]));
        constructor(code: string, name: string, category: ItemModel, availableRoles: Array<string>, targetCondition: AlarmCheckTargetCondition) {
            this.code = ko.observable(code);
            this.name = ko.observable(name);
            this.category = ko.observable(category != null ? category.code : null);
            this.displayCode = code;
            this.displayName = name;
            this.displayCategory = category != null ? category.name : "";
            this.availableRoles = ko.observableArray(availableRoles);
            this.targetCondition = ko.observable(targetCondition);
            this.displayAvailableRoles = ko.computed(function() {
                return this.availableRoles().join(", ");
            }, this);
        }
    }

    export class AlarmCheckTargetCondition {
        filterByEmployment: KnockoutObservable<boolean>;
        filterByClassification: KnockoutObservable<boolean>;
        filterByJobTitle: KnockoutObservable<boolean>;
        filterByBusinessType: KnockoutObservable<boolean>;
        targetEmployment: KnockoutObservableArray<string>;
        targetClassification: KnockoutObservableArray<string>;
        targetJobTitle: KnockoutObservableArray<string>;
        targetBusinessType: KnockoutObservableArray<string>;
        displayTargetEmployment: KnockoutObservable<string>;
        displayTargetClassification: KnockoutObservable<string>;
        displayTargetJobTitle: KnockoutObservable<string>;
        displayTargetBusinessType: KnockoutObservable<string>;

        constructor(fbe: boolean, fbc: boolean, fbjt: boolean, fbbt: boolean, targetEmp: Array<string>, targetCls: Array<string>, targetJob: Array<string>, targetBus: Array<string>) {
            this.filterByEmployment = ko.observable(fbe);
            this.filterByClassification = ko.observable(fbc);
            this.filterByJobTitle = ko.observable(fbjt);
            this.filterByBusinessType = ko.observable(fbbt);
            this.targetEmployment = ko.observableArray(targetEmp);
            this.targetClassification = ko.observableArray(targetCls);
            this.targetJobTitle = ko.observableArray(targetJob);
            this.targetBusinessType = ko.observableArray(targetBus);
            this.displayTargetEmployment = ko.observable("");
            this.displayTargetClassification = ko.observable("");
            this.displayTargetJobTitle = ko.observable("");
            this.displayTargetBusinessType = ko.observable("");

            this.targetEmployment.subscribe((data) => {
                kal003.a.service.getEmpNameByCodes(data).done((result: Array<string>) => {
                    this.displayTargetEmployment(result.join(", "));
                }).fail(() => {
                    this.displayTargetEmployment(this.targetClassification().join(", "));
                });
            });

            this.targetClassification.subscribe((data) => {
                kal003.a.service.getClsNameByCodes(data).done((result: Array<string>) => {
                    this.displayTargetClassification(result.join(", "));
                }).fail(() => {
                    this.displayTargetClassification(this.targetClassification().join(", "));
                });
            });

            this.targetJobTitle.subscribe((data) => {
                kal003.a.service.getJobNamesByIds(data).done((result: Array<string>) => {
                    this.displayTargetJobTitle(result.join(", "));
                }).fail(() => {
                    this.displayTargetJobTitle(this.targetClassification().join(", "));
                });
            });

            this.targetBusinessType.subscribe((data) => {
                kal003.a.service.getBusTypeNamesByCodes(data).done((result: Array<string>) => {
                    this.displayTargetBusinessType(result.join(", "));
                }).fail(() => {
                    this.displayTargetBusinessType(this.targetClassification().join(", "));
                });
            });
        }

        // Open Dialog CDL002
        private openCDL002Dialog() {
            let self = this;
            setShared('CDL002Params', {
                isMultiple: true,
                selectedCodes: self.targetEmployment(),
                showNoSelection: false,
            }, true);

            modal("com", "/view/cdl/002/a/index.xhtml").onClosed(() => {
                var output = getShared('CDL002Output');
                if (output) {
                    self.targetEmployment(output);
                }
            });
        }

        // Open Dialog CDL003
        private openCDL003Dialog() {
            let self = this;
            setShared('inputCDL003', {
                selectedCodes: self.targetClassification(),
                showNoSelection: false,
                isMultiple: true
            }, true);

            modal("com", "/view/cdl/003/a/index.xhtml").onClosed(() => {
                var output = getShared('outputCDL003');
                if (output) {
                    self.targetClassification(output);
                }
            })
        }

        // Open Dialog CDL004
        private openCDL004Dialog(): void {
            let self = this;
            setShared('inputCDL004', {
                baseDate: new Date(),
                selectedCodes: self.targetJobTitle(),
                showNoSelection: false,
                isMultiple: true
            }, true);

            modal("com", "/view/cdl/004/a/index.xhtml").onClosed(() => {
                var output = getShared('outputCDL004');
                if (output) {
                    self.targetJobTitle(output);
                }
            })
        }

        // Open Dialog CDL024
        private openCDL024Dialog() {
            let self = this;
            setShared("CDL024", { codeList: self.targetBusinessType() });

            modal("com", "/view/cdl/024/index.xhtml").onClosed(() => {
                var output = getShared("currentCodeList");
                if (output) {
                    self.targetBusinessType(output);
                }
            });
        }

    }

    export class ItemModel {
        code: number;
        name: string;
        description: string = "";

        constructor(code: number, name: string, description?: string) {
            this.code = code;
            this.name = name;
            if (description) {
                this.description = description;
            }
        }
    }

    export class DailyAlarmCheckCondition {
        conditionToExtractDaily: KnockoutObservable<number>;//main screen
        addApplication: KnockoutObservable<boolean>;//tab daily
        listErrorAlarmCode: KnockoutObservableArray<string>;//tab daily
        listExtractConditionWorkRecork: KnockoutObservableArray<WorkRecordExtractingCondition>;//tab check condition
        listFixedExtractConditionWorkRecord: KnockoutObservableArray<FixedConditionWorkRecord>;//tab  fixed

        constructor(conditionToExtractDaily: number, addApplication: boolean, listErrorAlarmCode: Array<string>, listWorkRecordExtractingConditions: Array<WorkRecordExtractingCondition>, listFixedConditionWorkRecord: Array<FixedConditionWorkRecord>) {
            this.conditionToExtractDaily = ko.observable(conditionToExtractDaily);
            this.addApplication = ko.observable(addApplication);
            this.listErrorAlarmCode = ko.observableArray(listErrorAlarmCode);
            this.listExtractConditionWorkRecork = ko.observableArray(listWorkRecordExtractingConditions);
            this.listFixedExtractConditionWorkRecord = ko.observableArray(listFixedConditionWorkRecord);
        }
    }

    export class Schedule4WeekAlarmCheckCondition {
        schedule4WeekCheckCondition: KnockoutObservable<number>;

        constructor(schedule4WeekCheckCondition: number) {
            this.schedule4WeekCheckCondition = ko.observable(schedule4WeekCheckCondition);
        }
    }

    export class Agreement36 {
        listCondOt: KnockoutObservableArray<AgreeCondOt>;//tab agreement hour
        listCondError: KnockoutObservableArray<AgreeConditionErrorDto>;//tab agreement error

        constructor(listCondOt: Array<AgreeCondOt>, listCondError: Array<AgreeConditionErrorDto>) {
            this.listCondOt = ko.observableArray(listCondOt);
            this.listCondError = ko.observableArray(listCondError);
        }
    }

    export enum CATEGORY {
        SCHEDULE_DAILY = 0,
        SCHEDULE_WEEKLY = 1,
        SCHEDULE_4_WEEK = 2,
        SCHEDULE_MONTHLY = 3,
        SCHEDULE_YEAR = 4,
        DAILY = 5,
        WEEKLY = 6,
        MONTHLY = 7,
        APPLICATION_APPROVAL = 8,
        MULTIPLE_MONTHS = 9,
        ANY_PERIOD = 10,
        ATTENDANCE_RATE_FOR_ANNUAL_HOLIDAYS = 11,
        _36_AGREEMENT = 12,
        MAN_HOUR_CHECK = 13
    }

    export enum DATA_CONDITION_TO_EXTRACT {
        ALL = 0,
        CONFIRMED = 1,
        UNCONFIRMED = 2
    }

    export enum SCHEDULE_4_WEEK_CHECK_CONDITION {
        FOR_ACTUAL_RESULTS_ONLY = 0,
        WITH_SCHEDULE_AND_ACTUAL_RESULTS = 1
    }

    export enum SCREEN_MODE {
        NEW = 0,
        UPDATE = 1
    }

    export enum ERROR_CLASSIFICATION {
        ERROR = 0,
        ALARM = 1,
        OTHER = 2
    }
    // class and interface : ExtraResultMonthly
//    export interface IExtraResultMonthly{
//        errorAlarmCheckID : string;
//        sortBy : number;
//        nameAlarmExtraCon : string;
//        useAtr : boolean;
//        typeCheckItem : number;
//        messageBold : boolean;
//        messageColor : string;
//        displayMessage : string;
//        checkConMonthly : IAttendanceItemCondition;
//        rowId: number;
//        specHolidayCheckCon : ISpecHolidayCheckCon;
//        checkRemainNumberMon : ICheckRemainNumberMon;
//        agreementCheckCon36 : IAgreementCheckCon36;
//        
//    }
    
    export class ExtraResultMonthly {
        errorAlarmCheckID : KnockoutObservable<string>;//
        sortBy : KnockoutObservable<number>;//
        nameAlarmExtraCon : KnockoutObservable<string>;//
        useAtr : KnockoutObservable<boolean>;//
        typeCheckItem : KnockoutObservable<number>;//
        messageBold : KnockoutObservable<boolean>;//
        messageColor : KnockoutObservable<string>;//
        displayMessage : KnockoutObservable<string>;
        rowId: KnockoutObservable<number>;//common
        conditions: KnockoutObservableArray<ExtractCondition>;     
        currentConditions: KnockoutObservableArray<ExtractCondition>;  
        constructor(param: any) {
            let self = this;
            self.typeCheckItem=ko.observable(undefined);    
            self.currentConditions = ko.observableArray([]);    
            self.conditions = ko.observableArray([]);
            self.typeCheckItem.subscribe((v) => {
                    nts.uk.ui.errors.clearAll();
                let current = (_.filter(self.conditions(), (con: ExtractCondition) => {
                    return con.typeCheckItem() === v;
                }));
                self.currentConditions(current);
            });
            if(!nts.uk.util.isNullOrUndefined(param)){
                self.errorAlarmCheckID = ko.observable(param.errorAlarmCheckID || '');
                self.sortBy = ko.observable(param.sortBy || 0);
                self.nameAlarmExtraCon = ko.observable(param.nameAlarmExtraCon);
                self.useAtr = ko.observable(param.useAtr || false);
                self.messageBold = ko.observable(param.messageBold);
                self.messageColor = ko.observable(param.messageColor);
                self.displayMessage = ko.observable(param.displayMessage || '');
                self.rowId = ko.observable(param.rowId || 0);
                self.currentConditions = ko.observableArray([]);
                if(param.noinit !== true){
                    let temp = [];
                    temp.push(new SpecHolidayCheckCon(ko.toJS(param.specHolidayCheckCon)));
                    temp.push(new CheckRemainNumberMon (ko.toJS(param.checkRemainNumberMon)));
                    let ag36 = new AgreementCheckCon36(ko.toJS(param.agreementCheckCon36));
                    temp.push(ag36);
                    temp.push(ag36.setupDefaultOtherType(ko.toJS(ag36)));
                    let attdItem: AttdItemConCommon =  _.isNil(param.checkConMonthly.group2) ? 
                                                            new AttdItemConCommon(ko.toJS(param.checkConMonthly), param.typeCheckItem) : 
                                                            new AttdItemConCommonWithGroup2(ko.toJS(param.checkConMonthly), param.typeCheckItem);
                    temp.push(attdItem);
                    _.forEach(AttdItemConCommon.initDefaultOtherTypes(attdItem.typeCheckItem()), (x) => {
                        temp.push(x);
                    });
                    
                    self.conditions(temp);
                    self.typeCheckItem(param.typeCheckItem || 0);   
                } 
            }
        }
        
        public static  clone(data: any):ExtraResultMonthly{
            var x = new ExtraResultMonthly({noinit: true});
            x.errorAlarmCheckID(data.errorAlarmCheckID);
            x.sortBy(data.sortBy);
            x.nameAlarmExtraCon(data.nameAlarmExtraCon);
            x.useAtr(data.useAtr);
            x.messageBold(data.messageBold);
            x.messageColor(data.messageColor);
            x.displayMessage(data.displayMessage);
            x.rowId(data.rowId);
            x.conditions(mapExtraCondition(data.conditions));
            x.typeCheckItem(data.typeCheckItem);
//            ko.mapping.fromJS(data, mapping, x);
            //x.typeCheckItem.valueHasMutated(); 
            return x;
        }
        
         
    }
    function mapExtraCondition(data: Array<ExtractCondition>){
        return _.map(data, (d) => {
                if(d.typeCheckItem === 0){
                    return SpecHolidayCheckCon.clone(d);
                }else if(d.typeCheckItem === 1 || d.typeCheckItem === 2){
                    return AgreementCheckCon36.clone(d);
                }else if(d.typeCheckItem === 3){
                    return CheckRemainNumberMon.clone(d);
                } else if(d.typeCheckItem === 8){
                    return AttdItemConCommonWithGroup2.clone(d);    
                }else{
                    return AttdItemConCommon.clone(d);
                }
            });    
    }
    function mapInputs(data: Array<InputModel>){
        return ko.observableArray(_.map(data, (d) => {
                    return InputModel.clone(d);
                }));   
    }
    function mapErAlAtdCon(data: Array<ErAlAtdItemCondition>) {
        return ko.observableArray(_.map(data, (d) => {
            return ErAlAtdItemCondition.clone(d);
        }));
    }
    
    var mapping = {'compareRangeEx': {
            create: function(options) {
                if(_.isNil(options.data)){
                    return ko.observable({});    
                }
                return ko.observable(CompareRangeImport.clone(options.data));
            }
        },'compareSingleValueEx': {
            create: function(options) {
                if(_.isNil(options.data)){
                    return ko.observable({});     
                }
                return ko.observable(CompareSingleValueImport.clone(options.data));
            }
        },
        'startValue': {
            create: function(options) {
                return ko.observable(CheckConValueRemainNumberImport.clone(options.data));
            }
        },'endValue': {
            create: function(options) {
                return ko.observable(CheckConValueRemainNumberImport.clone(options.data));
            }
        },'value': {
            create: function(options) {
                return ko.observable(CheckConValueRemainNumberImport.clone(options.data));
            }
            
        }
    }
    
    export class ExtractCondition {
        errorAlarmCheckID : KnockoutObservable<string> ;
        operator: KnockoutObservable<number>; 
        extractType: KnockoutObservable<number>;
        textLabel: KnockoutObservable<string>;
        haveTypeVacation :KnockoutObservable<boolean>;
        haveCombobox: KnockoutObservable<boolean>;
        haveComboboxFrame :KnockoutObservable<boolean>;
        haveSelect: KnockoutObservable<boolean>;
        haveGroup: KnockoutObservable<boolean>;
        haveInput: KnockoutObservable<number>;
        inputs: KnockoutObservableArray<InputModel>;
        typeCheckItem : KnockoutObservable<number>; //Set other type
        
        static clone(data: any): ExtractCondition{
            var x = new ExtractCondition();
            x.errorAlarmCheckID= ko.observable(data.errorAlarmCheckID);
            x.operator= ko.observable(data.operator);
            x.extractType= ko.observable(data.extractType);
            x.textLabel= ko.observable(data.textLabel);
            x.haveTypeVacation= ko.observable(data.haveTypeVacation);
            x.haveCombobox= ko.observable(data.haveCombobox);
            x.haveComboboxFrame = ko.observable(data.haveComboboxFrame);
            x.haveSelect= ko.observable(data.haveSelect);
            x.haveGroup= ko.observable(data.haveGroup);
            x.haveInput= ko.observable(data.haveInput);
            x.inputs= mapInputs(data.inputs);
            x.typeCheckItem =ko.observable( data.typeCheckItem);
//            ko.mapping.fromJS(data, mapping, x);
            x.setupScrible(true);
            return x;
        }
        
        
        setupScrible(){
            let self = this;
            self.setupInputs(); 
            self.extractType.subscribe((v) => {
                nts.uk.ui.errors.clearAll();
            });     
        }
        
        setupInputs() {
            let self = this;
            self.inputs = ko.observableArray([]);
        }
        
        customValidateInput(){
            let self = this;
            if(self.typeCheckItem() === 1 || self.typeCheckItem() === 2 || self.typeCheckItem() === 8){
                return;
            }
            let pair = 2, pairNext = 1;
            //if enum == 3
            if(self.typeCheckItem() === 3){//sua day nay,ngay xua m viet chung, h t tach ra lam th rieng
                for(let idx = 0; idx < 4; idx++){
                    self.inputs()[idx].value.subscribe((newVal) => {
                        let startDayValue: InputModel = self.inputs()[0];
                        let endDayValue: InputModel = self.inputs()[2];
                        let startTimeValue: InputModel = self.inputs()[1];
                        let endTimeValue: InputModel = self.inputs()[3];
                        self.validateDayTimePair(startDayValue, endDayValue, 
                                startTimeValue, endTimeValue, idx <= 1);
                    });    //t muoons set name cho tung input nay thi ls?
                }
            //end if enum = 3
            }else{
                let x = 0, maxInputs = self.inputs().length,
                    pairCount = maxInputs/pair;
                for(let idx = 0; idx < pairCount; idx++){
                    for(let idy = 0; idy < pairNext; idy++){
                        let currentIdx = idx + idy;
                        let pairTargetIdx = idx + idy + pairNext;
                        self.inputs()[currentIdx].value.subscribe((newVal) => {
                            let startPairValue: InputModel = self.inputs()[currentIdx];
                            let endPairValue: InputModel = self.inputs()[pairTargetIdx];
                            if(endPairValue.enable()){
                                //validate start and end pair;
                                if(parseInt(newVal)>=parseInt(endPairValue.value())){
                                    nts.uk.ui.errors.removeByCode($('#' + endPairValue.inputId), "Msg_927");
                                    $('#' + endPairValue.inputId).ntsError('set', { messageId: "Msg_927" });    
                                }
                                    
                                // set error;    
                            }
                        });
                        self.inputs()[pairTargetIdx].value.subscribe((newVal) => {
                            let startPairValue: InputModel = self.inputs()[currentIdx];
                            let endPairValue: InputModel = self.inputs()[pairTargetIdx];
                            if(startPairValue.enable()){
                                if(parseInt(newVal)<=parseInt(startPairValue.value())){
                                    nts.uk.ui.errors.removeByCode($('#' + endPairValue.inputId), "Msg_927");
                                    $('#' + endPairValue.inputId).ntsError('set', { messageId: "Msg_927" });    
                                }   
                            }
                        });
                    } 
                    idx += pair;
                }
            }//end else
             
        }
        
        validateDayTimePair(startDayValue: InputModel, endDayValue: InputModel, startTimeValue: InputModel,
                endTimeValue: InputModel, startFlag: boolean){
            
            if(endDayValue.enable()){
                nts.uk.ui.errors.removeByCode($('#' + startDayValue.inputId), "Msg_927");
                nts.uk.ui.errors.removeByCode($('#' + endDayValue.inputId), "Msg_927");
                
                let startDay: number = parseInt(startDayValue.value());
                let endDay: number = parseInt(endDayValue.value());
                let startTime: number = 0;
                let endTime: number = 0;
                if(!_.isNil(startTimeValue.value())){
                    nts.uk.ui.errors.removeByCode($('#' + startTimeValue.inputId), "Msg_927");
                    startTime = parseInt(startTimeValue.value());
                    
                }
                if(!_.isNil(endTimeValue.value())){
                    endTime = parseInt(endTimeValue.value());
                    nts.uk.ui.errors.removeByCode($('#' + endTimeValue.inputId), "Msg_927");
                }
                let fullStart = startDay*1440 + startTime;
                let fullEnd = endDay*1440 + endTime;
                //validate start and end pair;
                if(fullStart >= fullEnd){
                    let day = startFlag ? startDayValue : endDayValue;
                    let time = startFlag ? startTimeValue : endTimeValue;
                    if(day.enable()) {
                        $('#' + day.inputId).ntsError('set', { messageId: "Msg_927" });  
                    }
                    if(time.enable()) {
                        $('#' + time.inputId).ntsError('set', { messageId: "Msg_927" }); 
                    }
                }
                    
                // set error;    
            }
        }
        
        
        
        openSelect(viewmodel: ScreenModel){
              
        }
    }
    
    export class InputModel{
        typeInput : KnockoutObservable<number>; //0 : number, 1 : time
        required: KnockoutObservable<boolean>;
        value: KnockoutObservable<number>;
        enable: KnockoutObservable<boolean>;
        visible: KnockoutObservable<boolean>;
        inputId: string;
        inputName : string;
        constructor(typeInput:number,required: boolean,value: number,enable: boolean,visible: boolean,inputName : string){
            this.typeInput = ko.observable(typeInput);
            this.required= ko.observable(required);
            this.value= ko.observable(value);
            this.enable= ko.observable(enable);
            this.visible= ko.observable(visible);
            this.inputId = nts.uk.util.randomId();
            this.inputName = inputName;
        }
        
        public static  clone(data: any):InputModel{
            var x = new InputModel();
            x.inputId = data.inputId;
            x.typeInput(data.typeInput);
            x.required(data.required);
            x.value(data.value);
            x.enable(data.enable);
            x.visible(data.visible);
            x.inputName = data.inputName;
            return x;
        }
    }
    
//    export interface ISpecHolidayCheckCon{
//        errorAlarmCheckID : string ;
//        compareOperator : number;
//        numberDayDiffHoliday1 : number;
//        numberDayDiffHoliday2 : number;
//    }
    
    export class SpecHolidayCheckCon extends ExtractCondition{
        numberDayDiffHoliday1 : KnockoutObservable<number>;
        numberDayDiffHoliday2 : KnockoutObservable<number>;
        constructor(data : any){
            
            super();
            let self = this;
            this.extractType=ko.observable(0);
            this.textLabel=ko.observable(nts.uk.resource.getText("KAL003_78"));
            this.haveTypeVacation=ko.observable(false);
            this.haveCombobox=ko.observable(true);
            this.haveComboboxFrame = ko.observable(false);
            this.haveSelect=ko.observable(false);
            this.haveGroup=ko.observable(false);
            this.haveInput=ko.observable(0);
            this.typeCheckItem = ko.observable(0);
            this.operator=ko.computed(() => {
                return self.extractType();    
            });
            if(!nts.uk.util.isNullOrUndefined(data)){
                this.errorAlarmCheckID=ko.observable(data.errorAlarmCheckID);
                this.extractType(data.compareOperator);
                this.numberDayDiffHoliday1=ko.observable(data.numberDayDiffHoliday1 || 0);
                this.numberDayDiffHoliday2=ko.observable(data.numberDayDiffHoliday2 || 0); 
                this.setupScrible();  
            }else{
                this.errorAlarmCheckID=ko.observable("");
//                this.operator=ko.observable(0);
                this.numberDayDiffHoliday1=ko.observable(0);
                this.numberDayDiffHoliday2=ko.observable(0);
                this.setupScrible();
            }
        }
        
        
        setupScrible(){
            let self = this;
            self.setupInputs();  
            self.extractType.subscribe((v) => {
                nts.uk.ui.errors.clearAll();
                if(v > 5){
                    self.inputs()[1].enable(true);
                    self.inputs()[1].required(true);
                } else {
                    self.inputs()[1].enable(false);
                    self.inputs()[1].value(0);
                    self.inputs()[1].required(false);
                }
            });  
            self.extractType.valueHasMutated();
        }
        
        setupInputs(){
            let self = this;
            self.inputs = ko.observableArray([new InputModel(0,true,self.numberDayDiffHoliday1(),true,true,nts.uk.resource.getText("KAL003_80")),
                                            new InputModel(0,true,self.numberDayDiffHoliday2(),true,true,nts.uk.resource.getText("KAL003_83"))]);    
        }
        public static  clone(data: any) : SpecHolidayCheckCon{
            var x = new SpecHolidayCheckCon();
            x.extractType(data.extractType);
            x.numberDayDiffHoliday1(data.numberDayDiffHoliday1);
            x.numberDayDiffHoliday2(data.numberDayDiffHoliday2);
            x.errorAlarmCheckID(data.errorAlarmCheckID);
//            x.operator(data.operator);
            x.textLabel(data.textLabel);
            x.haveTypeVacation(data.haveTypeVacation);
            x.haveCombobox(data.haveCombobox);
            x.haveComboboxFrame(data.haveComboboxFrame);
            x.haveSelect(data.haveSelect);
            x.haveGroup(data.haveGroup);
            x.typeCheckItem(data.typeCheckItem);//t moi them doan nay
            let inputs = _.cloneDeep(data.inputs);
            data.inputs = [];
            //ko.mapping.fromJS(data, mapping, x);
            x.setupScrible();
            if(inputs){
                x.inputs(mapInputs(inputs)());        
            }
            x.customValidateInput();
            return x;
        }
    }
    
//    export interface ICheckRemainNumberMon{
//        errorAlarmCheckID : string ;
//        checkVacation : number;
//        checkOperatorType : number;
//        compareRangeEx : ICompareRangeImport;
//        compareSingleValueEx : ICompareSingleValueImport;
//    }
    
    export class CheckRemainNumberMon extends ExtractCondition{
        checkOperatorType : KnockoutObservable<number>;
        checkVacation : KnockoutObservable<number>;
        compareRangeEx : KnockoutObservable<CompareRangeImport>; 
        compareSingleValueEx : KnockoutObservable<CompareSingleValueImport>;
        listItemID : KnockoutObservableArray<number>;
        constructor(data : any){
            super();
            let self = this;
            this.extractType=ko.observable(0);
            this.textLabel=ko.observable("");
            this.haveTypeVacation=ko.observable(true);
            this.haveCombobox=ko.observable(true);
            this.haveComboboxFrame = ko.observable(true);
            this.haveSelect=ko.observable(false);
            this.haveGroup=ko.observable(false);
            this.haveInput=ko.observable(3);
            this.typeCheckItem =ko.observable(3);
            this.operator=ko.computed(() => {
                return  self.extractType();   
            });
            if(!nts.uk.util.isNullOrUndefined(data)){
                this.errorAlarmCheckID=ko.observable(data.errorAlarmCheckID);
                this.checkOperatorType=ko.observable(data.checkOperatorType || 0);
                this.checkVacation=ko.observable(data.checkVacation || 0);
                if(data.noinit !== true){
                    this.extractType(data.compareRangeEx ? data.compareRangeEx.compareOperator : data.compareSingleValueEx.compareOperator);
                    this.compareRangeEx=ko.observable(data.compareRangeEx?new CompareRangeImport(data.compareRangeEx) : null);
                    this.compareSingleValueEx=ko.observable(data.compareSingleValueEx? new CompareSingleValueImport( data.compareSingleValueEx) : null);
                    this.setupScrible();
                }
                this.listItemID=ko.observableArray(data.listItemID? data.listItemID : null);    
                this.checkVacation.subscribe((v) => {
                    if (v !=6) {
                        this.listItemID([0]);
                    }
                });
            }else{
                this.errorAlarmCheckID=ko.observable("");
//                this.operator=ko.observable(0);
                this.checkOperatorType=ko.observable(data.extractType >5 ?1:0);
                this.checkVacation=ko.observable(0);
                this.compareRangeEx=ko.observable(null);
                this.compareSingleValueEx=ko.observable(null);
                this.listItemID=ko.observableArray([]);
                this.setupScrible();
            }
            
            
        }
        
        public static  clone(data: any):CheckRemainNumberMon{
            var x = new CheckRemainNumberMon({noinit: true});
            x.checkVacation(data.checkVacation);
            x.errorAlarmCheckID(data.errorAlarmCheckID);
            x.extractType(data.extractType);
            x.textLabel(data.textLabel);
            x.haveTypeVacation(data.haveTypeVacation);
            x.haveCombobox(data.haveCombobox);
            x.haveComboboxFrame(data.haveComboboxFrame);
            x.checkOperatorType(data.checkOperatorType);
            x.haveSelect(data.haveSelect);
            x.haveGroup(data.haveGroup);
            x.haveInput(data.haveInput);
            x.typeCheckItem(data.typeCheckItem);
            x.listItemID(data.listItemID);
            let inputs = _.cloneDeep(data.inputs);
            //x.inputs= mapInputs(data.inputs);
            data.inputs = [];
            ko.mapping.fromJS(data, mapping, x);
            x.setupScrible();
            if(inputs){
                x.inputs(mapInputs(inputs)());        
            }
            x.customValidateInput();
            return x;
        }

        setupScrible(){
            let self = this;
            self.setupInputs();
            self.checkVacation.subscribe((v) => {
                nts.uk.ui.errors.clearAll();
                if((v ===1 ||v ===3 || v  ===4)){
                        self.inputs()[1].enable(true);
                        self.inputs()[1].required(true);  
                        if(self.extractType()>5){  
                            self.inputs()[3].enable(true);
                            self.inputs()[3].required(true);
                            self.inputs()[3].value.valueHasMutated();
                        }else{
                            self.inputs()[3].enable(false);
                            self.inputs()[3].required(false);
                        }    
                }else{
                    self.inputs()[1].enable(false);
                    self.inputs()[1].required(false);  
                    self.inputs()[1].value(null);
                    self.inputs()[3].enable(false);
                    self.inputs()[3].required(false);  
                    self.inputs()[3].value(null);
                    if(self.extractType()>5){
                        self.inputs()[2].value.valueHasMutated();
                    }
                }
            });
            self.extractType.subscribe((v) => {
                nts.uk.ui.errors.clearAll();
                if(self.checkVacation() ===1 ||self.checkVacation() ===3 ||self.checkVacation() ===4){
                        self.inputs()[1].enable(true);
                        self.inputs()[1].required(true);    
                }else{
                    self.inputs()[1].enable(false);
                    self.inputs()[1].required(false);  
                    self.inputs()[1].value(null);
                }
                if(v > 5){
                    self.checkOperatorType(1);
                    self.inputs()[2].enable(true);
                    self.inputs()[2].required(true);
                    self.inputs()[2].value.valueHasMutated();
                    if(self.checkVacation() ===1 ||self.checkVacation() ===3 ||self.checkVacation() ===4){
                        self.inputs()[3].enable(true);
                        self.inputs()[3].required(true);
                        self.inputs()[3].value.valueHasMutated();
                    }
                } else {
                    self.checkOperatorType(0);
                    self.inputs()[2].enable(false);
                    self.inputs()[2].required(false);
                    self.inputs()[2].value(null);
                    if(self.checkVacation() ===1 ||self.checkVacation() ===3 ||self.checkVacation() ===4){
                        self.inputs()[3].enable(false);
                        self.inputs()[3].required(false);
                        self.inputs()[3].value(null);
                    }
                    
                }
            });  
        }

        setupInputs(){
            let self = this;
            if(self.checkOperatorType()===0){
                self.inputs = ko.observableArray([new InputModel(0,true,self.compareSingleValueEx().value()==null?null:self.compareSingleValueEx().value().daysValue(),true,true,nts.uk.resource.getText("KAL003_102")),
                                                  new InputModel(1,true,self.compareSingleValueEx().value()==null?null:self.compareSingleValueEx().value().timeValue(),false,true,nts.uk.resource.getText("KAL003_104")),
                                                  new InputModel(0,false,null,false,true,nts.uk.resource.getText("KAL003_106")),
                                                  new InputModel(1,false,null,false,true,nts.uk.resource.getText("KAL003_108"))]);    
            }else{
                self.inputs = ko.observableArray([new InputModel(0,true,self.compareRangeEx().startValue()==null?null:self.compareRangeEx().startValue().daysValue(),true,true,nts.uk.resource.getText("KAL003_102")),
                                                  new InputModel(1,true,self.compareRangeEx().startValue()==null?null:self.compareRangeEx().startValue().timeValue(),false,true,nts.uk.resource.getText("KAL003_104")),
                                                  new InputModel(0,true,self.compareRangeEx().endValue()==null?null:self.compareRangeEx().endValue().daysValue(),false,true,nts.uk.resource.getText("KAL003_106")),
                                                  new InputModel(1,true,self.compareRangeEx().endValue()==null?null:self.compareRangeEx().endValue().timeValue(),false,true,nts.uk.resource.getText("KAL003_108")),
                                                  ]);  
            }
        }
    }
//    
//    export interface ICompareRangeImport{
//        compareOperator : number;
//        startValue : ICheckConValueRemainNumberImport;
//        endValue :  ICheckConValueRemainNumberImport;
//    }
    
    export class CompareRangeImport{
        compareOperator : KnockoutObservable<number>;
        startValue : KnockoutObservable<CheckConValueRemainNumberImport>;
        endValue : KnockoutObservable<CheckConValueRemainNumberImport>;   
        constructor(data : any ){
            if(!nts.uk.util.isNullOrUndefined(data)){
                this.compareOperator=ko.observable(data.compareOperator ||0);
                if(data.noinit !== true){
                    this.startValue=ko.observable(data && data.startValue?new CheckConValueRemainNumberImport(data.startValue) : null);    
                    this.endValue=ko.observable(data && data.endValue?new CheckConValueRemainNumberImport(data.endValue) : null);
                }    
            }else{
                this.compareOperator=ko.observable(0);    
                this.startValue=ko.observable( null);    
                this.endValue=ko.observable(null);
            }
        }
        
        public static  clone(data: any) : CompareRangeImport{
            var x = new CompareRangeImport({noinit: true});
            x.compareOperator(data.compareOperator);
            ko.mapping.fromJS(data, mapping, x);
            return x;
        }
    }
    
//    export interface ICompareSingleValueImport{
//        compareOperator : number;
//        value : ICheckConValueRemainNumberImport;
//    }
    
    export class CompareSingleValueImport{
        compareOperator : KnockoutObservable<number>;
        value : KnockoutObservable<CheckConValueRemainNumberImport>;
        constructor(data : any){
            if(!nts.uk.util.isNullOrUndefined(data)){
                this.compareOperator=ko.observable(data.compareOperator ||0);
                if(data.noinit !== true){
                    this.value=ko.observable(data && data.value?new CheckConValueRemainNumberImport(data.value) : null);
                }
            }else{
                this.compareOperator=ko.observable(0);
                this.value=ko.observable(null);
            }
//            this.operator=ko.computed(() => {
//                return  self.extractType();   
//            });
        }
        
        public static  clone(data: any) : CompareSingleValueImport{
            var x = new CompareSingleValueImport({noinit: true});
            x.compareOperator(data.compareOperator);
            ko.mapping.fromJS(data, mapping, x);
            return x;
        }
    }
    
//    export interface ICheckConValueRemainNumberImport{
//        daysValue : number;
//        timeValue :number;
//    }
//    
    export class CheckConValueRemainNumberImport {
        daysValue  : KnockoutObservable<number>;
        timeValue : KnockoutObservable<number>;
        constructor(data : any){
            if(!nts.uk.util.isNullOrUndefined(data)){
                this.daysValue=ko.observable(data.daysValue != null?data.daysValue: null);
                this.timeValue=ko.observable(data.timeValue != null?data.timeValue: null);    
            }else{
                this.daysValue=ko.observable(null);
                this.timeValue=ko.observable(null);
            }
             
        }
        
        public static  clone(data: any) : CheckConValueRemainNumberImport{
            var x = new CheckConValueRemainNumberImport();
            x.daysValue(nts.uk.util.isNullOrUndefined(data)?null:data.daysValue);
            x.timeValue(nts.uk.util.isNullOrUndefined(data)?null:data.timeValue);
            return x;
        }
    }
    
//    export interface IAgreementCheckCon36{
//        errorAlarmCheckID: string;
//        classification : number;
//        compareOperator : number;
//        eralBeforeTime : number;
//    }
    
    export class AgreementCheckCon36 extends ExtractCondition {
        classification : KnockoutObservable<number>;
        eralBeforeTime : KnockoutObservable<number>;
        constructor(data : any){
            super();
            
            this.haveTypeVacation=ko.observable(false);
            this.haveCombobox=ko.observable(true);
            this.haveComboboxFrame = ko.observable(false); 
            this.haveSelect=ko.observable(false);
            this.haveGroup=ko.observable(false);
            if(!nts.uk.util.isNullOrUndefined(data)){
                this.errorAlarmCheckID=ko.observable(data.errorAlarmCheckID);
                this.extractType=ko.observable(data.compareOperator || 0);
                this.textLabel=ko.observable(data.classification == 0 ? nts.uk.resource.getText("KAL003_159"): nts.uk.resource.getText("KAL003_158"));
                this.operator=ko.observable(data.compareOperator || 0);
                this.haveInput=ko.observable(data.classification == 0 ? 1 : 2);
                this.typeCheckItem =ko.observable(data.classification == 0 ? 1 : 2 );
                
                this.classification=ko.observable(data.classification || 0);
                this.eralBeforeTime=ko.observable(data.eralBeforeTime || 0);
                this.setupScrible();    
            }else{
                
                this.classification=ko.observable(0);
                this.eralBeforeTime=ko.observable(0);   
                this.extractType=ko.observable(0);
                this.setupScrible(); 
            }
            
        }
        
        setupDefaultOtherType(data : any): AgreementCheckCon36 {
            data.classification = data.classification === 0 ? 1 : 0;
            data.compareOperator = 0;
            data.eralBeforeTime = 0;
            return new AgreementCheckCon36(data);
        }
        
        
        public static  clone(data: any): AgreementCheckCon36{
            var x = new AgreementCheckCon36();
            x.classification(data.classification);
            x.eralBeforeTime(data.eralBeforeTime);
            x.errorAlarmCheckID=ko.observable(data.errorAlarmCheckID);
            x.operator=ko.observable(data.operator);
            x.extractType=ko.observable(data.extractType);
            x.textLabel=ko.observable(data.textLabel);
            x.haveTypeVacation(data.haveTypeVacation);
            x.haveCombobox(data.haveCombobox);
            x.haveComboboxFrame(data.haveComboboxFrame);
            x.haveSelect(data.haveSelect);
            x.haveGroup(data.haveGroup);
            x.haveInput=ko.observable(data.haveInput);
            x.typeCheckItem=ko.observable(data.typeCheckItem);
//            x.inputs= mapInputs(data.inputs);
            x.setupScrible();
//            x.input(mapInputs(data.inputs));
            x.customValidateInput();
            return x;
        }
    }
    
    export class AttdItemConCommon extends ExtractCondition{
        group1: KnockoutObservable<ErAlConditionsAttendanceItem>;
        selectText: KnockoutObservable<string>;
        
        constructor(param: any, typecheck) {
            super();
            let self = this;
            self.extractType=ko.observable(0);
            self.textLabel=ko.observable("");
            self.haveTypeVacation=ko.observable(false);
            self.haveCombobox=ko.observable(true);
            self.haveComboboxFrame =ko.observable(false);
            self.haveSelect=ko.observable(true);
            self.haveGroup=ko.observable(false);   
            self.haveInput=ko.observable(4);
            self.typeCheckItem=ko.observable(self.getTypeCheckOrDefault(typecheck));
            self.selectText = ko.observable("");
            
            if(!nts.uk.util.isNullOrUndefined(param)){
                self.errorAlarmCheckID=ko.observable(param.errorAlarmCheckID);
                self.operator=ko.observable(param ? param.operatorBetweenGroups || 0 : 0);
                
                if(param.noinit !== true){
                    self.group1=ko.observable(param ? new ErAlConditionsAttendanceItem(param.group1,1) : null);    
                    self.extractType(param.group1.lstErAlAtdItemCon.length ==0? 0 : param.group1.lstErAlAtdItemCon[0].compareOperator);
                    self.getTextAttdName(undefined);
                    self.setupScrible();
                }
            } else {
                self.errorAlarmCheckID=ko.observable("");
                self.operator=ko.observable(0);
                self.group1=ko.observable(kal003utils.getDefaultAttdItemGroup3Item());//set default group 1 for type 4, 5, 6, 7
                self.setupScrible(); 
            }
        }
        
        getTypeCheckOrDefault(typecheck: number){
            if(typecheck >= 4 && typecheck <= 8){
                return typecheck;
            }    
            return 4;
        }
        
        public static  clone(data: any): AttdItemConCommon{
            var x = new AttdItemConCommon({noinit: true});
            x.errorAlarmCheckID(data.errorAlarmCheckID);
            x.operator(data.operator);
            x.extractType(data.extractType);
            x.textLabel(data.textLabel);
            x.haveTypeVacation(data.haveTypeVacation);
            x.haveCombobox(data.haveCombobox);
            x.haveComboboxFrame(data.haveComboboxFrame);
            x.haveSelect(data.haveSelect);
            x.haveGroup(data.haveGroup);
            x.haveInput(data.haveInput);
            x.typeCheckItem(data.typeCheckItem);
            x.group1=ko.observable(ErAlConditionsAttendanceItem.clone(data.group1));
            x.getTextAttdName(undefined);
            //ko.mapping.fromJS(data, mapping, x);
            
            x.setupScrible();
            if(data.inputs){
                x.inputs(mapInputs(data.inputs)());
            }
            x.customValidateInput();
            return x;
        }
        //
        setupInputs(){
            let self = this;
            let temp = [];
            let inputType = self.typeCheckItem()===4 ? 1 : 0;
            let inputName1 = "";
            let inputName2 = "";
            switch(self.typeCheckItem()){
                case 4:
                    inputName1 = nts.uk.resource.getText("KAL003_92");
                    inputName2 = nts.uk.resource.getText("KAL003_93");
                break;
                case 5:
                    inputName1 = nts.uk.resource.getText("KAL003_94");
                    inputName2 = nts.uk.resource.getText("KAL003_95");
                break;
                case 6:
                    inputName1 = nts.uk.resource.getText("KAL003_96");
                    inputName2 = nts.uk.resource.getText("KAL003_97");
                break;
                case 7:
                    inputName1 = nts.uk.resource.getText("KAL003_98");
                    inputName2 = nts.uk.resource.getText("KAL003_99");
                break;
                case 8:
                break;
                default:break;    
            }
            temp.push(new InputModel(inputType,true,self.group1().lstErAlAtdItemCon()[0].compareStartValue(),true,true,inputName1));
            if(self.extractType() < 6){
                temp.push(new InputModel(inputType,true,self.group1().lstErAlAtdItemCon()[0].compareEndValue(),false,true,inputName2));
            }else{
                temp.push(new InputModel(inputType,true,self.group1().lstErAlAtdItemCon()[0].compareEndValue(),true,true,inputName2));  
            }
            self.inputs = ko.observableArray(temp);
            
        }
        
        setupScrible(){
            let self = this;
            self.setupInputs();
            self.extractType.subscribe((v) => {
                nts.uk.ui.errors.clearAll();
                if(v > 5){
                    self.inputs()[1].enable(true);
                    self.inputs()[1].required(true);
                    self.inputs()[1].value.valueHasMutated();
                } else {
                    self.inputs()[1].enable(false);
                    self.inputs()[1].required(false);
                    self.inputs()[1].value(null);
                }
            });  
        }
        
        public static initDefaultOtherTypes(typecheck: any): Array<AttdItemConCommon>{
            let allType = [4, 5, 6, 7, 8];
            let result: Array<AttdItemConCommon> = [];
            _.forEach(allType, (cType) => {
               if(typecheck !== cType){
                    if(cType === 8){
                        result.push(new AttdItemConCommonWithGroup2(undefined, 8));    
                    } else {
                        result.push(new AttdItemConCommon(undefined, cType));        
                    }
               } 
            });
            return result
        }
        
        getTextAttdName(sourceName: Array<any>){
            let self: AttdItemConCommon = this;
            if(self.typeCheckItem() === 8){
                return;
            }
            let countableAddAtdItems= self.group1().lstErAlAtdItemCon()[0].countableAddAtdItems(),
                countableSubAtdItems = self.group1().lstErAlAtdItemCon()[0].countableSubAtdItems();
            if(sourceName){
                self.convertToText(sourceName, countableAddAtdItems, countableSubAtdItems);
                return;   
            }
            let viewmodel = new nts.uk.at.view.kal003.b.viewmodel.ScreenModel(true);
            let itemIds = _.concat(countableAddAtdItems, countableSubAtdItems);
            if(itemIds.length === 0){
                return;
            }
            self.group1().lstErAlAtdItemCon()[0].getAttendanceItemMonthlyByCodes(itemIds).done((lstItems) => {
                self.convertToText(lstItems, countableAddAtdItems, countableSubAtdItems);
            });
//            viewmodel.getListItemByAtrDailyAndMonthly(self.typeCheckItem() ,1).done((lstItem) => {
//                 self.convertToText(lstItem);
//            });               
        }
        
        convertToText(sourceName: Array<any>, countableAddAtdItems: Array<number>, countableSubAtdItems: Array<number>){
            let self: AttdItemConCommon = this;
            let addText = "", subText = "";
            if(countableAddAtdItems.length > 0){
                addText = "" + _.map(countableAddAtdItems, (id) => {
                    let finded = _.find(sourceName, (item) => { return id === item.attendanceItemId; });
                    return finded === undefined ? "" : finded.attendanceItemName
                }).join("+");    
            }
            if(countableSubAtdItems.length > 0){
                subText = '-' + _.map(countableSubAtdItems, (id) => {
                    let finded = _.find(sourceName, (item) => { return id === item.attendanceItemId; });
                    return finded === undefined ? "" : finded.attendanceItemName
                }).join("-");    
            }
            self.selectText(addText + subText);
        }
        
        openSelect(viewmodel: nts.uk.at.view.kal003.b.viewmodel.ScreenModel){
            let self: AttdItemConCommon = this;
            let currentAtdItemConMon = self.group1().lstErAlAtdItemCon()[0];
            viewmodel.getListItemByAtrDailyAndMonthly(self.typeCheckItem(), 1).done((lstItem) => {
                let lstItemCode = lstItem.map((item) => { return item.attendanceItemId; });
                //Open dialog KDW007C
                let param = {
                    attr: 1,
                    lstAllItems: lstItemCode,
                    lstAddItems: currentAtdItemConMon.countableAddAtdItems(),
                    lstSubItems: currentAtdItemConMon.countableSubAtdItems()
                };

                nts.uk.ui.windows.setShared("KDW007Params", param);
                nts.uk.ui.windows.sub.modal("at", "/view/kdw/007/c/index.xhtml").onClosed(() => {
                    $(".nts-input").ntsError("clear");
                    let output = nts.uk.ui.windows.getShared("KDW007CResults");
                    if (output) {
                        currentAtdItemConMon.countableAddAtdItems(output.lstAddItems.map((item) => { return parseInt(item); }));
                        currentAtdItemConMon.countableSubAtdItems(output.lstSubItems.map((item) => { return parseInt(item); }));
                        self.getTextAttdName(lstItem);
                    }
                });
                
            });    
        }
    }
    
    
    
    export class AttdItemConCommonWithGroup2 extends AttdItemConCommon{
        group2UseAtr: KnockoutObservable<boolean>;
        group2: KnockoutObservable<ErAlConditionsAttendanceItem>;
        
        constructor(param: any, typecheck: any) {
            super(param, typecheck);
            let self = this;
            self.haveInput(8);
            self.typeCheckItem(8);
            self.haveCombobox=ko.observable(false);
            self.haveSelect=ko.observable(false);
            self.haveGroup=ko.observable(true);
            if(!nts.uk.util.isNullOrUndefined(param)){
                self.group2UseAtr=ko.observable(param ? param.group2UseAtr || false : false);
                //self.group1(kal003utils.getDefaultAttdItemGroup3Item());
                if(param.noinit !== true){
                    self.group2=ko.observable(param ? new ErAlConditionsAttendanceItem(param.group2,1) : null);
                }
                this.setupScrible();
            } else {
                self.group2UseAtr=ko.observable(false);
                //self.group1(kal003utils.getDefaultAttdItemGroup3Item());
                self.group2=ko.observable(kal003utils.getDefaultAttdItemGroup3Item());
                this.setupScrible();
            }
        }
        public static clone(data: any): AttdItemConCommonWithGroup2{
            let x: AttdItemConCommonWithGroup2 = super.clone(data);
            x.group2UseAtr=ko.observable(data.group2UseAtr)
            x.group2 = ko.observable(ErAlConditionsAttendanceItem.clone(data.group2));
            return x;
        }
        
    }

    export interface IWorkRecordExtractingCondition {
        errorAlarmCheckID: string;
        checkItem: number;
        messageBold: boolean;
        messageColor: string;
        sortOrderBy: number;
        useAtr: boolean;
        nameWKRecord: string;
        errorAlarmCondition: ErrorAlarmCondition;
        rowId: number;
    }

    export class WorkRecordExtractingCondition {
        errorAlarmCheckID: string;
        checkItem: KnockoutObservable<number> = ko.observable(0);
        messageBold: KnockoutObservable<boolean> = ko.observable(false);
        messageColor: KnockoutObservable<string> = ko.observable('');
        sortOrderBy: number;
        useAtr: KnockoutObservable<boolean> = ko.observable(false);
        nameWKRecord: KnockoutObservable<string> = ko.observable('');
        errorAlarmCondition: KnockoutObservable<ErrorAlarmCondition>;
        rowId: KnockoutObservable<number> = ko.observable(0);
        constructor(param: IWorkRecordExtractingCondition) {
            let self = this;
            self.errorAlarmCheckID = param.errorAlarmCheckID || '';
            self.checkItem(param.checkItem || 0);
            self.messageBold(param.messageBold);
            self.messageColor(param.messageColor);
            self.sortOrderBy = param.sortOrderBy || 0;
            self.useAtr(param.useAtr || false);
            self.nameWKRecord(param.nameWKRecord || '');
            self.errorAlarmCondition = ko.observable(param.errorAlarmCondition);
            self.rowId(param.rowId || 0);
        }
    }
    
    

    //---------------- KAL003 - B begin----------------//
    //Condition of group (C screen) ErAlAtdItemCondition
    // 勤怠項目のエラーアラーム条件
    export interface IErAlAtdItemCondition {
        targetNO: number;
        conditionAtr: number;
        useAtr: boolean;
        uncountableAtdItem: number;
        countableAddAtdItems: Array<number>;
        countableSubAtdItems: Array<number>;
        conditionType: number;
        compareOperator: number;
        singleAtdItem: number;
        compareStartValue: number;
        compareEndValue: number;

        displayLeftCompare: string;
        displayLeftOperator: string;
        displayTarget: string;
        displayRightCompare: string;
        displayRightOperator: string;
        inputCheckCondition :number;
        
    }

    export class ErAlAtdItemCondition {

        targetNO: KnockoutObservable<number>;
        conditionAtr: KnockoutObservable<number>;
        useAtr: KnockoutObservable<boolean>;
        uncountableAtdItem: KnockoutObservable<number>;
        countableAddAtdItems: KnockoutObservableArray<number>;
        countableSubAtdItems: KnockoutObservableArray<number>;
        conditionType: KnockoutObservable<number>;
        compareOperator: KnockoutObservable<number>;
        singleAtdItem: KnockoutObservable<number>;
        compareStartValue: KnockoutObservable<number>;
        compareEndValue: KnockoutObservable<number>;

        displayLeft: KnockoutObservable<any>;
        displayRight: KnockoutObservable<any>;
        displayCenter: KnockoutObservable<any>;
        displayLeftCompare: KnockoutObservable<any>;
        displayLeftOperator: KnockoutObservable<any>;
        displayTarget: KnockoutObservable<any>;
        displayRightCompare: KnockoutObservable<any>;
        displayRightOperator: KnockoutObservable<any>;
        
        inputCheckCondition :KnockoutObservable<number>;
        constructor(NO, param: IErAlAtdItemCondition,modeX :number) {
            let self = this;
            if(!nts.uk.util.isNullOrUndefined(param)){
                self.targetNO=ko.observable(NO);
                self.conditionAtr=ko.observable(param.conditionAtr);
                self.useAtr=ko.observable(param.useAtr);
                self.uncountableAtdItem=ko.observable(param.uncountableAtdItem);
                self.countableAddAtdItems=ko.observableArray(_.values(param.countableAddAtdItems));
                self.countableSubAtdItems=ko.observableArray(_.values(param.countableSubAtdItems));
                self.conditionType=ko.observable(param.conditionType);
                self.singleAtdItem=ko.observable(param.singleAtdItem);
                self.compareStartValue=ko.observable(param.compareStartValue);
                self.compareEndValue=ko.observable(param.compareEndValue);
                self.compareOperator=ko.observable(param.compareOperator);
                self.displayLeftCompare=ko.observable("");
                self.displayLeftOperator=ko.observable("");
                self.displayTarget=ko.observable("");
                self.displayRightCompare=ko.observable("");
                self.displayRightOperator=ko.observable("");
                self.inputCheckCondition = ko.observable(0);
                self.setTextDisplay(modeX);    
            }else{
                self.targetNO=ko.observable(NO);
                self.conditionAtr=ko.observable(0);
                self.useAtr=ko.observable(false);
                self.uncountableAtdItem=ko.observable(0);
                self.countableAddAtdItems=ko.observableArray([]);
                self.countableSubAtdItems=ko.observableArray([]);
                self.conditionType=ko.observable(0);
                self.singleAtdItem=ko.observable(0);
                self.compareStartValue=ko.observable(null);
                self.compareEndValue=ko.observable(null);
                self.compareOperator=ko.observable(0);
                self.displayLeftCompare=ko.observable("");
                self.displayLeftOperator=ko.observable("");
                self.displayTarget=ko.observable("");
                self.displayRightCompare=ko.observable("");
                self.displayRightOperator=ko.observable("");
                self.inputCheckCondition = ko.observable(0);
            }
            self.displayLeft = ko.computed(() => {
                let compareOp = self.compareOperator();
                let case1 = self.displayLeftCompare();
                let case2 = self.displayTarget();
                if(compareOp === 6 || compareOp === 7){
                    return case1;
                } else {
                    return case2;
                }  
            });
            self.displayRight = ko.computed(() => {
                let compareOp = self.compareOperator();
                let case1 = self.displayRightCompare();
                let case2 = self.displayTarget();
                if(compareOp === 6 || compareOp === 7){
                    return case1;
                } else if(compareOp === 8 || compareOp === 9){
                    return case2;
                } else {
                    return "";
                }
            });
            self.displayCenter = ko.computed(() => {
                let compareOp = self.compareOperator();
                let case1 = self.displayLeftCompare();
                let case2 = self.displayTarget();
                if(compareOp === 6 || compareOp === 7){
                    return case2;
                } else {
                    return case1;
                }
            });
            
        }
       
        
        public static  clone(data: any) : ErAlAtdItemCondition{
            var x = new ErAlAtdItemCondition(data.targetNO, null, 1);
            delete data['displayLeft'];
            delete data['displayRight'];
            delete data['displayCenter'];
            x.targetNO(data.targetNO);
            x.conditionAtr(data.conditionAtr);
            x.useAtr(data.useAtr);
            x.uncountableAtdItem(data.uncountableAtdItem);
            x.countableAddAtdItems(_.values(data.countableAddAtdItems));
            x.countableSubAtdItems(_.values(data.countableSubAtdItems));
            x.conditionType(data.conditionType);
            x.singleAtdItem(data.singleAtdItem);
            x.compareStartValue(data.compareStartValue);
            x.compareEndValue(data.compareEndValue);
            x.compareOperator(data.compareOperator);
            x.displayLeftCompare(data.displayLeftCompare);
            x.displayLeftOperator(data.displayLeftOperator);
            x.displayTarget(data.displayTarget);
            x.displayRightCompare(data.displayRightCompare);    
            x.displayRightOperator(data.displayRightOperator);
            x.inputCheckCondition(0);
            x.setTextDisplay(1);    
//            x.setTextDisplay=ko.observable(data.setTextDisplay);
           // ko.mapping.fromJS(data, mapping, x);
            return x;
        }

        setTextDisplay(modeX) {
            let self = this;
            if (self.useAtr()) {
                self.setDisplayTarget(modeX);
                self.setDisplayOperator();
                self.setDisplayCompare(modeX);
            } else {
                self.displayLeftCompare("");
                self.displayLeftOperator("");
                self.displayTarget("");
                self.displayRightCompare("");
                self.displayRightOperator("");
            }
        }

        setDisplayOperator() {
            let self = this;
            switch (self.compareOperator()) {
                case 0:
                    self.displayLeftOperator("＝");
                    self.displayRightOperator("");
                    break;
                case 1:
                    self.displayLeftOperator("≠");
                    self.displayRightOperator("");
                    break;
                case 2:
                    self.displayLeftOperator("＞");
                    self.displayRightOperator("");
                    break;
                case 3:
                    self.displayLeftOperator("≧");
                    self.displayRightOperator("");
                    break;
                case 4:
                    self.displayLeftOperator("＜");
                    self.displayRightOperator("");
                    break;
                case 5:
                    self.displayLeftOperator("≦");
                    self.displayRightOperator("");
                    break;
                case 6:
                    self.displayLeftOperator("＜");
                    self.displayRightOperator("＜");
                    break;
                case 7:
                    self.displayLeftOperator("≦");
                    self.displayRightOperator("≦");
                    break;
                case 8:
                    self.displayLeftOperator("＜");
                    self.displayRightOperator("＜");
                    break;
                case 9:
                    self.displayLeftOperator("≦");
                    self.displayRightOperator("≦");
                    break;
                default: 
                    self.displayLeftOperator("");
                    self.displayRightOperator("");
            }
        }

        setDisplayCompare(modeX) {
            let self = this;
            let conditionAtr = self.conditionAtr();
            if (self.compareOperator() > 5) {
                // Compare with a range
                let rawStartValue = self.compareStartValue();
                let rawEndValue = self.compareEndValue();
                let textDisplayLeftCompare = (conditionAtr !== 1 ) ? rawStartValue : nts.uk.time.parseTime(rawStartValue, true).format();
                let textDisplayRightCompare = (conditionAtr !== 1) ? rawEndValue : nts.uk.time.parseTime(rawEndValue, true).format();
                if(self.compareOperator() > 7){
                    self.displayLeftCompare(textDisplayLeftCompare + ", " + textDisplayRightCompare);
                    self.displayRightCompare("");    
                } else {
                    self.displayLeftCompare(textDisplayLeftCompare);
                    self.displayRightCompare(textDisplayRightCompare);    
                }
            } else {
                // Compare with single value
                if (self.conditionType() === 0) {
                    // If is compare with a fixed value
                    let rawValue = self.compareStartValue();
                    let textDisplayLeftCompare = ( conditionAtr !== 1) ? rawValue : nts.uk.time.parseTime(rawValue, true).format();
                    self.displayLeftCompare(textDisplayLeftCompare);
                    self.displayRightCompare("");
                } else {
                    // If is compare with a attendance item
                    if (self.singleAtdItem()) {
                        //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes([self.singleAtdItem()]).done((lstItems) => {
                        if(modeX==1){
                            self.getAttendanceItemMonthlyByCodes([self.singleAtdItem()]).done((lstItems) => {
                                if (lstItems && lstItems.length > 0) {
                                    self.displayLeftCompare(lstItems[0].attendanceItemName);
                                    self.displayRightCompare("");
                                }
                            }); 
                        }else{
                            self.getAttendanceItemByCodes([self.singleAtdItem()]).done((lstItems) => {
                                if (lstItems && lstItems.length > 0) {
                                    self.displayLeftCompare(lstItems[0].attendanceItemName);
                                    self.displayRightCompare("");
                                }
                            });     
                        }
                        
                    }
                }
            }
        }

        setDisplayTarget(modeX) {
            let self = this;
            if(modeX ==1){//monthly
                if (self.conditionAtr() === 2) {
                    if (self.uncountableAtdItem()) {
                        //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes([self.uncountableAtdItem()]).done((lstItems) => {
                        self.getAttendanceItemMonthlyByCodes([self.uncountableAtdItem()]).done((lstItems) => {
                            if (lstItems && lstItems.length > 0) {
                                self.displayTarget(lstItems[0].attendanceItemName);
                            }
                        });
                    }
                } else {
                    if (self.countableAddAtdItems().length > 0) {
                        let addText = ""; 
                        //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes(self.countableAddAtdItems()).done((lstItems) => {
                        self.getAttendanceItemMonthlyByCodes(self.countableAddAtdItems()).done((lstItems) => {
    //                        if (lstItems && lstItems.length > 0) {
    //                            for (let i = 0; i < lstItems.length; i++) {
    //                                let operator = (i === (lstItems.length - 1)) ? "" : " + ";
    //                                self.displayTarget(self.displayTarget() + lstItems[i].attendanceItemName + operator);
    //                            }
    //                        }
                            addText += _.map(lstItems, (item) => {
                                    return item.attendanceItemName;
                                }).join("+");  
                        }).then(() => {
                            if (self.countableSubAtdItems().length > 0) {
                                //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
                                self.getAttendanceItemMonthlyByCodes(self.countableSubAtdItems()).done((lstItems) => {
    //                                if (lstItems && lstItems.length > 0) {
    //                                    for (let i = 0; i < lstItems.length; i++) {
    //                                        let operator = (i === (lstItems.length - 1)) ? "" : " - ";
    //                                        let beforeOperator = (i === 0) ? " - " : "";
    //                                        self.displayTarget(self.displayTarget() + beforeOperator + lstItems[i].attendanceItemName + operator);
    //                                    }
    //                                }
                                    addText += "-" + _.map(lstItems, (item) => {
                                            return item.attendanceItemName;
                                        }).join("-");  
                                    self.displayTarget(addText);
                                })
                                
                            } else {
                                self.displayTarget(addText);    
                            }
                        });
                    } else if (self.countableSubAtdItems().length > 0) {
                        //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
                        self.getAttendanceItemMonthlyByCodes(self.countableSubAtdItems()).done((lstItems) => {
                            let addText = _.map(lstItems, (item) => {
                                    return item.attendanceItemName;
                                }).join("-");  
                            self.displayTarget(addText);
                        })
                    }
    
                }
            } else {//daily
                if (self.conditionAtr() === 2) {
                    if (self.uncountableAtdItem()) {
                        //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes([self.uncountableAtdItem()]).done((lstItems) => {
                        self.getAttendanceItemByCodes([self.uncountableAtdItem()]).done((lstItems) => {
                            if (lstItems && lstItems.length > 0) {
                                self.displayTarget(lstItems[0].attendanceItemName);
                            }
                        });
                    }
                } else {
                    if (self.countableAddAtdItems().length > 0) {
                        let addText = ""; 
                        //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes(self.countableAddAtdItems()).done((lstItems) => {
                        self.getAttendanceItemByCodes(self.countableAddAtdItems()).done((lstItems) => {
    //                        if (lstItems && lstItems.length > 0) {
    //                            for (let i = 0; i < lstItems.length; i++) {
    //                                let operator = (i === (lstItems.length - 1)) ? "" : " + ";
    //                                self.displayTarget(self.displayTarget() + lstItems[i].attendanceItemName + operator);
    //                            }
    //                        }
                            addText += _.map(lstItems, (item) => {
                                    return item.attendanceItemName;
                                }).join("+");  
                        }).then(() => {
                            if (self.countableSubAtdItems().length > 0) {
                                //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
                                self.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
    //                                if (lstItems && lstItems.length > 0) {
    //                                    for (let i = 0; i < lstItems.length; i++) {
    //                                        let operator = (i === (lstItems.length - 1)) ? "" : " - ";
    //                                        let beforeOperator = (i === 0) ? " - " : "";
    //                                        self.displayTarget(self.displayTarget() + beforeOperator + lstItems[i].attendanceItemName + operator);
    //                                    }
    //                                }
                                    addText += "-" + _.map(lstItems, (item) => {
                                            return item.attendanceItemName;
                                        }).join("-");  
                                    self.displayTarget(addText);
                                })
                                
                            } else {
                                self.displayTarget(addText);    
                            }
                        });
                    } else if (self.countableSubAtdItems().length > 0) {
                        //nts.uk.at.view.kal003.b.service.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
                        self.getAttendanceItemByCodes(self.countableSubAtdItems()).done((lstItems) => {
                            let addText = _.map(lstItems, (item) => {
                                    return item.attendanceItemName;
                                }).join("-");  
                            self.displayTarget(addText);
                        })
                    }
    
                }
            }
        }

        public openAtdItemConditionDialog(modeX: number) {
            let self = this;
            let param = ko.mapping.toJS(self);
            if(modeX ==1){
                //KAL003C
                nts.uk.ui.windows.setShared("KAL003CParams", {mode: modeX, data: param}, true);
                nts.uk.ui.windows.sub.modal("at", "/view/kal/003/c/index.xhtml").onClosed(() => {
                    let output = getShared("KAL003CResult");
                    if (output) {
                        self.targetNO(output.targetNO);
                        self.conditionAtr(output.conditionAtr);
                        self.useAtr(true);
                        self.uncountableAtdItem(output.uncountableAtdItem);
                        self.countableAddAtdItems(output.countableAddAtdItems);
                        self.countableSubAtdItems(output.countableSubAtdItems);
                        self.conditionType(output.conditionType);
                        self.singleAtdItem(output.singleAtdItem);
                        self.compareStartValue(output.compareStartValue);
                        self.compareEndValue(output.compareEndValue);
                        self.compareOperator(output.compareOperator);
                    }
                    self.setTextDisplay(modeX);
                });
            }else{
                nts.uk.ui.windows.setShared("KDW007BParams", {mode: modeX, data: param}, true);
                nts.uk.ui.windows.sub.modal("at", "/view/kdw/007/b/index.xhtml").onClosed(() => {
                    let output = getShared("KDW007BResult");
                    if (output) {
                        self.targetNO(output.targetNO);
                        self.conditionAtr(output.conditionAtr);
                        self.useAtr(true);
                        self.uncountableAtdItem(output.uncountableAtdItem);
                        self.countableAddAtdItems(output.countableAddAtdItems);
                        self.countableSubAtdItems(output.countableSubAtdItems);
                        self.conditionType(output.conditionType);
                        self.singleAtdItem(output.singleAtdItem);
                        self.compareStartValue(output.compareStartValue);
                        self.compareEndValue(output.compareEndValue);
                        self.compareOperator(output.compareOperator);
                    }
                    self.setTextDisplay(modeX);
                });
             }
        }

        setData(NO, param) {
            let self = this;
            self.targetNO(NO);
            self.conditionAtr(param ? param.conditionAtr : 0);
            self.useAtr(param ? param.useAtr : false);
            self.uncountableAtdItem(param ? param.uncountableAtdItem : null);
            self.countableAddAtdItems(param && param.countableAddAtdItems ? param.countableAddAtdItems : []);
            self.countableSubAtdItems(param && param.countableSubAtdItems ? param.countableSubAtdItems : []);
            self.conditionType(param ? param.conditionType : 0);
            self.singleAtdItem(param ? param.singleAtdItem : null);
            self.compareStartValue(param && param.compareStartValue ? param.compareStartValue : 0);
            self.compareEndValue(param && param.compareEndValue ? param.compareEndValue : 0);
            self.compareOperator(param ? param.compareOperator : 0);
            self.setTextDisplay(modeX);
        }
        //the same kdw007
        getAttendanceItemByCodes(codes): JQueryPromise<any> {
            return nts.uk.request.ajax("at", "at/record/divergencetime/AttendanceDivergenceName", codes);
        }
        
        //the same kdw007
        getAttendanceItemMonthlyByCodes(codes): JQueryPromise<any> {
            return nts.uk.request.ajax("at", "at/record/divergencetime/getMonthlyAttendanceDivergenceName", codes);
        }
    }
    // group condition
    export interface IErAlConditionsAttendanceItem {
        atdItemConGroupId: string;
        conditionOperator: number; //0: OR|1: AND
        lstErAlAtdItemCon: Array<IErAlAtdItemCondition>;// max 3
        modeX :number;
    }

    export class ErAlConditionsAttendanceItem {
        atdItemConGroupId: KnockoutObservable<string>  = ko.observable('');
        conditionOperator: KnockoutObservable<number> = ko.observable(0); //OR|AND B15-3, B17-3
        lstErAlAtdItemCon: KnockoutObservableArray<ErAlAtdItemCondition>;// max 3 item, B16-1 -> B16-4
        constructor(param: IErAlConditionsAttendanceItem) {
            let self = this;
            if(!nts.uk.util.isNullOrUndefined(param)){
                self.atdItemConGroupId(param ? param.atdItemConGroupId || '' : '');
                self.conditionOperator(param ? param.conditionOperator || 0 : 0);
                if(param.noinit !== true){
                    let data = ko.mapping.toJS(param.lstErAlAtdItemCon);
                    let mapped = [];
                    for(var i = 0; i< 3; i++){
                        let findedNo = _.find(data, function(d){ 
                            let js = ko.mapping.toJS(d);
                            return js.targetNO === i; 
                        });
                        if(_.isNil(findedNo)){
                            mapped.push(new ErAlAtdItemCondition(i, null,param.modeX));
                        } else {
                            let js = ko.mapping.toJS(findedNo);
                            mapped.push(new ErAlAtdItemCondition(i, js,param.modeX));    
                        }
                    }
                    self.lstErAlAtdItemCon = ko.observableArray(mapped);
                }
            }
        }
        
        public static  clone(data: any) : ErAlConditionsAttendanceItem{
            var x = new ErAlConditionsAttendanceItem({noinit: true});
            x.atdItemConGroupId(data.atdItemConGroupId);
            x.conditionOperator(data.conditionOperator);
            x.lstErAlAtdItemCon = mapErAlAtdCon(data.lstErAlAtdItemCon);
            //ko.mapping.fromJS(data, mapping, x);
            return x;
        }
    }

    // BA2_3 - group1 and group2UseAtr is false
    export interface IAttendanceItemCondition {
        group1: IErAlConditionsAttendanceItem;
        group2UseAtr: boolean; // B17-1
        group2: IErAlConditionsAttendanceItem;
        operatorBetweenGroups: number; // B18-2: 0: OR, 1: AND
    }
    export class AttendanceItemCondition {
        group1: KnockoutObservable<ErAlConditionsAttendanceItem>;
        group2UseAtr: KnockoutObservable<boolean> = ko.observable(false);
        group2: KnockoutObservable<ErAlConditionsAttendanceItem>;
        operatorBetweenGroups: KnockoutObservable<number> = ko.observable(0);
        constructor(param: IAttendanceItemCondition) {
            let self = this;
            self.group1 = ko.observable(param ? new ErAlConditionsAttendanceItem(ko.mapping.toJS(param.group1)) : null);
            self.group2UseAtr(param ? param.group2UseAtr || false : false);
            self.group2 = ko.observable(param ? new ErAlConditionsAttendanceItem(ko.mapping.toJS(param.group2)) : null);
            self.operatorBetweenGroups(param ? param.operatorBetweenGroups || 0 : 0);
        }
    }

    // アラームチェック対象者の条件
    export interface IAlCheckTargetCondition {
        filterByBusinessType: boolean;
        filterByJobTitle: boolean;
        filterByEmployment: boolean;
        filterByClassification: boolean;
        lstBusinessTypeCode: Array<string>;
        lstJobTitleId: Array<string>;
        lstEmploymentCode: Array<string>;
        lstClassificationCode: Array<string>;
    }

    export class AlCheckTargetCondition {
        filterByBusinessType: boolean;
        filterByJobTitle: boolean;
        filterByEmployment: boolean;
        filterByClassification: boolean;
        lstBusinessTypeCode: Array<string>;
        lstJobTitleId: Array<string>;
        lstEmploymentCode: Array<string>;
        lstClassificationCode: Array<string>;
        constructor(param) {
            let self = this;
            self.filterByBusinessType = param.filterByBusinessType || false;
            self.filterByJobTitle = param.filterByJobTitle || false;
            self.filterByEmployment = param.filterByEmployment || false;
            self.filterByClassification = param.filterByClassification || false;
            self.lstBusinessTypeCode = param.lstBusinessTypeCode || [];
            self.lstJobTitleId = param.lstJobTitleId || [];
            self.lstEmploymentCode = param.lstEmploymentCode || [];
            self.lstClassificationCode = param.lstClassificationCode || [];
        }
    }

    //BA5_3
    export interface IWorkTimeCondition {
        useAtr: boolean;
        comparePlanAndActual: number;
        planFilterAtr: boolean;
        planLstWorkTime: Array<string>;
        actualFilterAtr: boolean;
        actualLstWorkTime: Array<string>;
    }
    export class WorkTimeCondition {
        useAtr: boolean;
        comparePlanAndActual: KnockoutObservable<number> = ko.observable(0);
        planFilterAtr: boolean;
        planLstWorkTime: KnockoutObservableArray<string> = ko.observableArray([]);
        actualFilterAtr: boolean;
        actualLstWorkTime: Array<string>;

        constructor(param: IWorkTimeCondition) {
            let self = this;
            self.useAtr = param && param.useAtr ? param.useAtr : false;
            self.comparePlanAndActual(param && param.comparePlanAndActual ? param.comparePlanAndActual : 0);
            self.planFilterAtr = param && param.planFilterAtr ? param.planFilterAtr : false;
            self.planLstWorkTime(param && param.planLstWorkTime ? param.planLstWorkTime : []);
            self.actualFilterAtr = param && param.actualFilterAtr ? param.actualFilterAtr : false;
            self.actualLstWorkTime = param && param.actualLstWorkTime ? param.actualLstWorkTime : [];
        }
    }
    //BA1-4
    export interface IWorkTypeCondition {
        useAtr: boolean;
        comparePlanAndActual: number;
        planFilterAtr: boolean;
        planLstWorkType: Array<string>;
        actualFilterAtr: boolean;
        actualLstWorkType: Array<string>;
    }

    export class WorkTypeCondition {
        useAtr: boolean;
        comparePlanAndActual: KnockoutObservable<number> = ko.observable(0);
        planFilterAtr: boolean;
        planLstWorkType: KnockoutObservableArray<string> = ko.observableArray([]);
        actualFilterAtr: boolean;
        actualLstWorkType: Array<string>;
        constructor(param: IWorkTypeCondition) {
            let self = this;
            self.useAtr = param && param.useAtr ? param.useAtr : false;
            self.comparePlanAndActual(param ? (param.comparePlanAndActual == 0 ? 0 : param.comparePlanAndActual || 1) : 1); //default is 1
            self.planFilterAtr = param && param.planFilterAtr ? param.planFilterAtr : false;
            self.planLstWorkType(param && param.planLstWorkType ? param.planLstWorkType : []);
            self.actualFilterAtr = param && param.actualFilterAtr ? param.actualFilterAtr : false;
            self.actualLstWorkType = param && param.actualLstWorkType ? param.actualLstWorkType : [];
        }
    }

    //勤務実績のエラーアラームチェック
    export interface IErrorAlarmCondition {
        errorAlarmCheckID: string;
        displayMessage: string;
        alCheckTargetCondition: AlCheckTargetCondition;
        workTypeCondition: WorkTypeCondition; // 勤務種類の条件
        workTimeCondition: WorkTimeCondition; // 勤怠項目のエラーアラーム条件
        atdItemCondition: AttendanceItemCondition;
        continuousPeriod: number; //連続期間
    }

    export class ErrorAlarmCondition {
        errorAlarmCheckID: KnockoutObservable<string> = ko.observable('');
        displayMessage: KnockoutObservable<string> = ko.observable('');
        alCheckTargetCondition: KnockoutObservable<AlCheckTargetCondition>;
        workTypeCondition: KnockoutObservable<WorkTypeCondition>;
        workTimeCondition: KnockoutObservable<WorkTimeCondition>;
        atdItemCondition: KnockoutObservable<AttendanceItemCondition>;
        continuousPeriod: KnockoutObservable<number> = ko.observable(null); //連続期間
        constructor(param: IErrorAlarmCondition) {
            let self = this;
            self.errorAlarmCheckID(param && param.errorAlarmCheckID ? param.errorAlarmCheckID : '')
            self.displayMessage(param && param.displayMessage ? param.displayMessage : '');
            self.alCheckTargetCondition = ko.observable(param && param.alCheckTargetCondition ? param.alCheckTargetCondition : null);
            self.workTypeCondition = ko.observable(param && param.workTypeCondition ? param.workTypeCondition : null);
            self.workTimeCondition = ko.observable(param && param.workTimeCondition ? param.workTimeCondition : null);
            self.atdItemCondition = ko.observable(param && param.atdItemCondition ? param.atdItemCondition : null);
            self.continuousPeriod(param.continuousPeriod || 0); //連続期間
        }
    }

    //---------------- KAL003 - B end------------------//

    export class DailyErrorAlarmCheck {
        code: string;
        name: string;
        classification: number;
        displayClassification: string;
        message: string;
        constructor(code: string, name: string, classification: number, message: string) {
            this.code = code;
            this.name = name;
            let item = _.find(ko.toJS(getErrorClassification()), (x: model.ItemModel) => x.code == classification);
            this.classification = item.code;
            this.displayClassification = item.name;
            this.message = message;
        }
    }

    //interface FixedConditionWorkRecord
    export interface IFixedConditionWorkRecord {
        dailyAlarmConID: string;
        checkName: string;
        fixConWorkRecordNo: number;
        message: string;
        useAtr: boolean;
    }



    //class FixedConditionWorkRecord
    export class FixedConditionWorkRecord {
        dailyAlarmConID: string;
        fixConWorkRecordNo: KnockoutObservable<number>;
        checkName: string;
        message: KnockoutObservable<string>;
        useAtr: KnockoutObservable<boolean>;
        constructor(data: IFixedConditionWorkRecord) {
            this.dailyAlarmConID = data.dailyAlarmConID;
            this.fixConWorkRecordNo = ko.observable(data.fixConWorkRecordNo);
            this.message = ko.observable(data.message);
            this.useAtr = ko.observable(data.useAtr);
            this.checkName = data.checkName;
        }
    }

    export interface IAgreeCondOt {
        category: number;
        code: string;
        id: string;
        no: number;
        ot36: number;
        excessNum: number;
        messageDisp: string;
    }

    export class AgreeCondOt {
        category: number;
        code: string;
        id: string;
        no: number;
        ot36: KnockoutObservable<number>;
        excessNum: KnockoutObservable<number>;
        messageDisp: KnockoutObservable<string>;

        constructor(param: IAgreeCondOt) {
            this.category = param.category;
            this.code = param.code;
            this.id = param.id;
            this.no = param.no;
            this.ot36 = ko.observable(param.ot36);
            this.excessNum = ko.observable(param.excessNum);
            this.messageDisp = ko.observable(param.messageDisp);
        }
    }

    export interface IAgreeConditionErrorDto {
        category: number;
        code: string;
        id: string;
        useAtr: number;
        period: number;
        errorAlarm: number;
        messageDisp: string;
        name: string;
    }

    export class AgreeConditionErrorDto {
        category: number;
        code: string;
        id: string;
        useAtr: KnockoutObservable<number>;
        period: number;
        errorAlarm: number;
        messageDisp: KnockoutObservable<string>;
        name: string;

        constructor(param: IAgreeConditionErrorDto) {
            this.category = param.category;
            this.code = param.code;
            this.id = param.id;
            this.useAtr = ko.observable(param.useAtr);
            this.period = param.period;
            this.errorAlarm = param.errorAlarm;
            this.messageDisp = ko.observable(param.messageDisp);
            this.name = param.name;
        }
    }

    //monthly
    export class MonAlarmCheckCon {
        listFixExtraMon: KnockoutObservableArray<FixedExtraMonFun>;
        arbExtraCon : KnockoutObservableArray<ExtraResultMonthly>;
        constructor(listFixExtraMon: KnockoutObservableArray<FixedExtraMonFun>,arbExtraCon : KnockoutObservableArray<ExtraResultMonthly>
            ) {
            this.listFixExtraMon = ko.observableArray(listFixExtraMon);
            this.arbExtraCon = ko.observableArray(arbExtraCon);
        }
    }

    //interface FixedExtraMonFun
    export interface IFixedExtraMonFun {
        monAlarmCheckID: string;
        monAlarmCheckName: string;
        fixedExtraItemMonNo: number;
        message: string;
        useAtr: boolean;
    }

    //class FixedExtraMonFun
    export class FixedExtraMonFun {
        monAlarmCheckID: string;
        fixedExtraItemMonNo: KnockoutObservable<number>;
        monAlarmCheckName: string;
        message: KnockoutObservable<string>;
        useAtr: KnockoutObservable<boolean>;
        constructor(data: IFixedExtraMonFun) {
            this.monAlarmCheckID = data.monAlarmCheckID;
            this.fixedExtraItemMonNo = ko.observable(data.fixedExtraItemMonNo);
            this.message = ko.observable(data.message);
            this.useAtr = ko.observable(data.useAtr);
            this.monAlarmCheckName = data.monAlarmCheckName;
        }
    }

}