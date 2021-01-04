/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kal013.b {
    import errors = nts.uk.ui.errors;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import NumberEditorOption = nts.uk.ui.option.NumberEditorOption;

    const PATH_API = {
       GET_ENUM_OPERATOR: "at/record/alarmwrkp/screen/getEnumCompareType",
    };

    @bean()
    export class KAL013BViewModel extends ko.ViewModel {
        listTypeCheck    : KnockoutObservableArray<ItemEnumModel> = ko.observableArray([]);
        pattern : KnockoutObservable<Pattern> = ko.observable(new Pattern());
        listSingleValueCompareTypes: KnockoutObservableArray<EnumModel> = ko.observableArray([]);
        contrastTypeList:  KnockoutObservableArray<ItemEnumModel> = ko.observableArray([]);
        category: KnockoutObservable<number> = ko.observable(null);
        enableMaxValue: KnockoutObservable<boolean> = ko.observable(true);
        switchPatternA: KnockoutObservable<boolean> = ko.observable(true);
        timeControl: KnockoutObservable<boolean> = ko.observable(true);
        constraint : KnockoutObservable<string> = ko.observable("");
        numberEditorOption: KnockoutObservable<NumberEditorOption> = ko.observable(new NumberEditorOption({
            grouplength: 0,
            decimallength: 0,
            placeholder: ''
        }));
        listZeroDecimalNum: Array<string> = ["NumberOfPeople","Amount","RatioComparison","AverageNumberTimes","AverageRatio"];
        listOneDecimalNum: Array<string> = ["AverageNumberDays"];
        monthlyTimeControl: Array<number> = [1,5];
        dailyTimeControl: Array<number> = [2];
        dailyTimeControlB : Array<number> = [2, 5, 8];
        dailyContraint: Map<number,string> =new Map([
            [1, "NumberOfPeople"],
            [2, "Time"],
            [3, "Amount"],
            [4, "RatioComparison"]
        ]);
        monthlyContraint: Map<number,string> = new Map([
            [1, "AverageTime"], // AVERAGE_TIME(1, "平均時間"),
            [2, "AverageNumberDays"], // AVERAGE_NUMBER_DAY(2, "平均日数"),
            [3, "AverageNumberTimes"], //AVERAGE_NUMBER_TIME(3, "平均回数"),
            [4, "AverageRatio"], // AVERAGE_RATIO(4, "平均比率"),
            [5, "AverageTime"], // TIME_FREEDOM(5, "平均時間自由"),
            [6, "AverageNumberDays"], // AVERAGE_DAY_FREE(6, "平均日数自由")
            [7, "AverageNumberTimes"], // AVERAGE_TIME_FREE(7, "平均回数自由")
            [8, "AverageRatio"]
        ]);

        constructor(params: IParentParams) {
            super();
            const vm = this;
            vm.category(params.category);
            if (params.category == WorkplaceCategory.MONTHLY) {
                vm.listTypeCheck(__viewContext.enums.CheckMonthlyItemsType);
            } else {
                vm.listTypeCheck(__viewContext.enums.CheckDayItemsType);
            }

            vm.pattern().checkItem.subscribe((value)=>{
                // Clear error
                nts.uk.ui.errors.clearAll();
                vm.pattern().clearCheckCod();

                // Kind of control
                vm.checkKindOfConrol(value);

                // Constraint
               vm.createConstraint(value);

                // Change pattern

                vm.createcontrastList(vm.category(), value);
            });

            vm.pattern().checkCondB.subscribe((value)=>{
                if (!value || vm.switchPatternA() || vm.category() != WorkplaceCategory.SCHEDULE_DAILY){
                    return;
                }
                nts.uk.ui.errors.clearAll();
                vm.pattern().clearCheckCodB();
                //Kind of control
                vm.checkKindOfConrol(value,false);
                // Constraint
                vm.createConstraint(value,false);
            })



            vm.constraint.subscribe((value)=>{
                const vm = this;
                if (_.indexOf(vm.listOneDecimalNum,value) != -1){
                    vm.numberEditorOption(new NumberEditorOption({
                        grouplength: 0,
                        decimallength: 1,
                        placeholder: ''
                    }));
                } else {
                    vm.numberEditorOption(new NumberEditorOption({
                        grouplength: 0,
                        decimallength: 0,
                        placeholder: ''
                    }));
                }

            });

        }

        created(params: IParentParams) {
            const vm = this;
            if (_.isNil(params.condition.checkItem)){
                params = vm.initParam(params);
            }
            vm.getEnum().done(()=>{
                vm.createcontrastList(params.category, params.condition.checkItem);
                vm.pattern().update(params.condition);
                vm.initScreen(params.condition.checkItem,params.condition.checkCondB,vm.switchPatternA());
            });

            vm.pattern().checkCond.subscribe((value)=>{
                if (vm.switchPatternA()){
                    // vm.$errors("clear","#check-condition");
                    $("#check-condition").ntsError("clear");

                }

            });

            vm.pattern().operator.subscribe((value)=>{
                vm.$errors("clear",".endValue");
                if (_.indexOf([6,7,8,9],value) != -1 ){
                    vm.enableMaxValue(true);
                } else {
                    vm.enableMaxValue(false);
                    vm.pattern().clearMaxValue();
                }
            });
            vm.pattern().minValue.subscribe(()=>{
                vm.$errors("clear",".endValue");
            });
        }

        initParam(params: IParentParams): IParentParams{
            const vm = this;

            if (params.category == WorkplaceCategory.MONTHLY) {
                params = {
                    category: params.category, condition: {
                        checkItem: 1,
                        checkCond: null,
                        checkCondB: null,
                        operator: 0,
                        minValue: null,
                        maxValue: null,
                        displayMessage: null
                    }
                };
                vm.switchPatternA(true);
            } else {
                params = {
                    category: params.category, condition: {
                        checkItem: 0,
                        checkCond: null,
                        checkCondB: 1,
                        operator: 0,
                        minValue: null,
                        maxValue: null,
                        displayMessage: null
                    }
                };
                vm.contrastTypeList(__viewContext.enums.ContrastType);
                vm.switchPatternA(false);
            }
            return params;
        }

        initScreen(value: number, valueCheck?: number, isPatternA: boolean = false){
            const vm = this;
            vm.checkKindOfConrol(value);

            // Constraint
            vm.createConstraint(value);

            // Change pattern
            vm.createcontrastList(vm.category(), value);

            if (!isPatternA){
                //vm.pattern().clearCheckCod();
                vm.checkKindOfConrol(valueCheck,false);
                // Constraint
                vm.createConstraint(valueCheck,false);
            }
        }

        createcontrastList(category: number, value: number):void {
            const vm = this;
            vm.switchPatternA(true)
            // 対比の場合 - スケジュール／日次
            if (category == WorkplaceCategory.SCHEDULE_DAILY && value == 0) {
                vm.switchPatternA(false);
                vm.contrastTypeList(__viewContext.enums.ContrastType);
            } else if (category == WorkplaceCategory.MONTHLY
                && _.indexOf([CheckMonthlyItemsType.TIME_FREEDOM, CheckMonthlyItemsType.AVERAGE_DAY_FREE,
                    CheckMonthlyItemsType.AVERAGE_TIME_FREE, CheckMonthlyItemsType.AVERAGE_RATIO_FREE],value) != -1 ){
                vm.switchPatternA(false);
                switch (value){
                    case CheckMonthlyItemsType.TIME_FREEDOM:
                        vm.contrastTypeList(__viewContext.enums.AverageTime);
                        break;
                    case CheckMonthlyItemsType.AVERAGE_DAY_FREE:
                        vm.contrastTypeList(__viewContext.enums.AverageNumberOfDays);
                        break;
                    case CheckMonthlyItemsType.AVERAGE_TIME_FREE:
                        vm.contrastTypeList(__viewContext.enums.AverageNumberOfTimes);
                        break;
                    case CheckMonthlyItemsType.AVERAGE_RATIO_FREE:
                        vm.contrastTypeList(__viewContext.enums.AverageRatio);
                        break;
                    default:
                        vm.contrastTypeList([]);
                        break;
                }
            }
        }

        checkKindOfConrol(value: number, isPatternA: boolean = true){
            const vm = this;
            // Kind of control
            if (isPatternA){
                // 月次 - 平均時間 ||  スケジュール／日次 - 時間対比
                if ((vm.category() == WorkplaceCategory.MONTHLY && _.indexOf(vm.monthlyTimeControl,value) != -1)
                    || (vm.category() == WorkplaceCategory.SCHEDULE_DAILY
                        && (_.indexOf(vm.dailyTimeControl,value) != -1
                            || _.indexOf(vm.dailyTimeControlB, vm.pattern().checkCondB()) != -1))){
                    vm.timeControl(true);
                } else{
                    vm.timeControl(false);
                }
            } else
            {
                if (vm.category() == WorkplaceCategory.SCHEDULE_DAILY ) {
                    vm.timeControl(false);
                    if (_.indexOf(vm.dailyTimeControlB, value) != -1) {
                        vm.timeControl(true);
                    }
                }
            }
        }

        createConstraint(value: number, isPatternA: boolean = true){
            const vm = this;
            // Contraint
            if (vm.category() == WorkplaceCategory.SCHEDULE_DAILY){
                vm.constraint(_.isNil(vm.dailyContraint.get(value))? vm.dailyContraint.get(1) : vm.dailyContraint.get(value));
            } else{
                vm.constraint(vm.monthlyContraint.get(value));
            }

            if (!isPatternA && vm.category() == WorkplaceCategory.SCHEDULE_DAILY){
                if (_.indexOf([1, 4, 7], value) != -1) {
                    vm.constraint(vm.dailyContraint.get(1));
                } else if (_.indexOf(vm.dailyTimeControlB, value) != -1) {
                    vm.constraint(vm.dailyContraint.get(2));
                } else if (_.indexOf([3, 6, 9], value) != -1) {
                    vm.constraint(vm.dailyContraint.get(3));
                }
            }

        }
        mounted() {
            const vm = this;
            $('#cbxTypeCheckWorkRecordcategory5').focus();
        }

        //openSelectAtdItemDialogTarget() {
        btnSettingB2_2_click(params: any) {
            const vm = this;
            let param: any = {
                isMulti: false,
                selecteds: [
                    params.checkCond()
                ]
            }
            setShared('KDL007_PARAM', param, true);
            nts.uk.ui.windows.sub.modal('/view/kdl/007/a/index.xhtml').onClosed(() => {
                let listResult = getShared('KDL007_VALUES');

                vm.pattern().updateCheckCond(listResult.selecteds[0].length > 0 ? listResult.selecteds: "");
            });
        }

        getEnum(): JQueryPromise<any>{
            const vm = this;
            let dfd = $.Deferred();
            vm.$ajax(PATH_API.GET_ENUM_OPERATOR).done((
                listSingleValueCompareTypse: Array<IEnumModel>) => {
                vm.listSingleValueCompareTypes(vm.getLocalizedNameForEnum(listSingleValueCompareTypse));
                dfd.resolve();

            }).fail(()=>{
                dfd.reject();
            }).always(() => {
            });
            return dfd.promise();
        }

        private getLocalizedNameForEnum(listEnum: Array<IEnumModel>): Array<EnumModel> {
            const vm = this;
            let result = _.map(listEnum,(item) =>{
                let enumValue: IEnumModel = {value: item.value, fieldName: item.fieldName,
                    localizedName: vm.$i18n(item.localizedName)};
                return new EnumModel(enumValue);
            });
            return result;
        }

        validate(): boolean{
            const vm = this;
            let result: boolean = true;
            if (vm.switchPatternA() && _.isEmpty(vm.pattern().checkCond())){
                vm.$errors("#check-condition",{ messageId: 'MsgB_1', messageParams: [vm.$i18n("KAL003_25")] });
                result = false;
            }

            if  (!vm.validateStartEnd())
            {
                vm.$errors(".endValue", "Msg_927");
                result = false;
            }
            return result;
        }

        validateStartEnd(): boolean{
            const vm = this;
            if (_.isNil(vm.pattern().maxValue()) ){
                return true;
            }

            if  (( _.indexOf([RangeCompareType.BETWEEN_RANGE_OPEN, RangeCompareType.OUTSIDE_RANGE_OPEN],vm.pattern().operator()) != -1
                    && vm.pattern().minValue() >= vm.pattern().maxValue() )
                || ( _.indexOf([RangeCompareType.BETWEEN_RANGE_OPEN, RangeCompareType.OUTSIDE_RANGE_OPEN],vm.pattern().operator()) == -1
                    && vm.pattern().minValue() > vm.pattern().maxValue() ))
            {
                return false;
            }
            return true;
        }


        btnDecision() {
            const vm = this;
            $('.nts-input').filter(":enabled").trigger("validate");
            if (errors.hasError() === true || !vm.validate()) {
                return;
            }
            let shareParam: IPattern = ko.toJS(vm.pattern());
            console.log(shareParam);
            vm.$window.close({
                shareParam
            });
        }

        closeDialog() {
            const vm = this;
            vm.$window.close({
            });
        }
    }

    class Pattern{
        checkItem: KnockoutObservable<number> = ko.observable(0);
        checkCond: KnockoutObservable<string> = ko.observable("");
        checkCondDis: KnockoutObservable<string> = ko.observable("");
        checkCondB: KnockoutObservable<number> = ko.observable(0);
        operator: KnockoutObservable<number> = ko.observable(0);
        minValue: KnockoutObservable<number> = ko.observable(0);
        maxValue: KnockoutObservable<number> = ko.observable(0);
        displayMessage: KnockoutObservable<string> = ko.observable("");
        constructor(){}

        update(params: IPattern){
            this.checkItem(_.isNil(params.checkItem) ? 0: params.checkItem);
            this.checkCond(params.checkCond);
            this.checkCondDis(params.checkCond);
            this.checkCondB(_.isNil(params.checkCondB) ? 1: params.checkCondB);
            this.operator(params.operator);
            this.minValue(params.minValue);
            this.maxValue(params.maxValue);
            this.displayMessage(params.displayMessage);
        }

        updateCheckCond(checkCondItem: string){
            this.checkCond(checkCondItem);
            this.checkCondDis(checkCondItem);
        }

        clearMaxValue(){
            this.maxValue(null);
        }
        clearCheckCod(){
            this.checkCond(null);
            this.checkCondB(null);
            this.checkCondDis("");
            this.minValue(null);
            this.maxValue(null);
        }
        clearCheckCodB(){
            this.checkCond(null);
            this.checkCondDis("");
            this.minValue(null);
            this.maxValue(null);
        }
    }

    export interface IPattern{
        checkItem: number;
        checkCond: string;
        checkCondB: number;
        operator: number;
        minValue: number;
        maxValue: number;
        displayMessage: string;
    }


    class EnumModel {
        value: KnockoutObservable<number>;
        fieldName: string;
        localizedName: string;
        constructor(param: IEnumModel) {
            let self = this;
            self.value = ko.observable(param.value);
            self.fieldName = param.fieldName || '';
            self.localizedName = param.localizedName || '';
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

    class ItemEnumModel {
        value: number;
        name: string;

        constructor(code: number, name: string, description?: string) {
            this.value = code;
            this.name = name;
        }
    }
    interface IEnumModel {
        value: number;
        fieldName: string;
        localizedName: string;
    }

    interface IParentParams {
        category: number;
        condition: IPattern;
    }

    enum RangeCompareType{
        // 範囲の間（境界値を含まない）（＜＞）
        BETWEEN_RANGE_OPEN = 6,
        /* 範囲の間（境界値を含む）（≦≧） */
        BETWEEN_RANGE_CLOSED = 7,
        /* 範囲の外（境界値を含まない）（＞＜） */
        OUTSIDE_RANGE_OPEN = 8,
        /* 範囲の外（境界値を含む）（≧≦） */
        OUTSIDE_RANGE_CLOSED = 9,
    }

    enum WorkplaceCategory {
        // スケジュール／日次
        SCHEDULE_DAILY = 3, // 月次
        MONTHLY = 4
    }

    enum CheckMonthlyItemsType {

        /* 平均時間 */
        AVERAGE_TIME  = 1,
        /* 平均日数 */
        AVERAGE_NUMBER_DAY = 2,
        /* 平均回数 */
        AVERAGE_NUMBER_TIME = 3,
        /* 平均比率 */
        AVERAGE_RATIO = 4,
        /* 平均時間自由 */
        TIME_FREEDOM = 5,
        /* 平均日数自由 */
        AVERAGE_DAY_FREE = 6,
        /* 平均回数自由 */
        AVERAGE_TIME_FREE = 7,
        /*平均比率自由 */
        AVERAGE_RATIO_FREE = 8

    }
}