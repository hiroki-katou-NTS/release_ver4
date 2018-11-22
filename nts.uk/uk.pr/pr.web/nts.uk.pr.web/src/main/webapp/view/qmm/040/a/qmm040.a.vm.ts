module nts.uk.pr.view.qmm040.a.viewmodel {
    import getText = nts.uk.resource.getText;
    import dialog = nts.uk.ui.dialog;

    export class ScreenModel {

        //SalIndAmountName
        // change logic, get data after click button
        ccg001ReturnData: any;



        salIndAmountNames: KnockoutObservableArray<SalIndAmountName>;
        salIndAmountNamesSelectedCode: KnockoutObservable<string>;

        personalAmount:KnockoutObservableArray<PersonalAmount>;
        personalDisplay:KnockoutObservableArray<PersonalAmount>;

        //onSelected
        cateIndicator: KnockoutObservable<number>;
        salBonusCate: KnockoutObservable<number>;
        //ccg001
        employeeList:any;
        employeeInfoImports:any;
        referenceDate: KnockoutObservable<string> = ko.observable('');
        ccgcomponent: GroupOption;
        tilteTable: KnockoutObservableArray<any>;
        selectedEmployeeCode: KnockoutObservable<string>;


        yearMonthFilter: KnockoutObservable<number>;
        onTab: KnockoutObservable<number> = ko.observable(0);
        titleTab: KnockoutObservable<string> = ko.observable('');
        itemClassification: KnockoutObservable<string> = ko.observable('');
        individualPriceCode: KnockoutObservable<string>;
        individualPriceName: KnockoutObservable<string>;


        constructor() {
            var self = this;
            if (/Chrome/.test(navigator.userAgent) && !/Edge/.test(navigator.userAgent)) {
                $("#A5_9").ntsFixedTable({height: 344, width: 720});
            } else {
                $("#A5_9").ntsFixedTable({height: 340, width: 720});
            }
            self.tilteTable = ko.observableArray([
                {headerText: getText('QMM040_8'), key: 'individualPriceCode', width: 100},
                {headerText: getText('QMM040_9'), key: 'individualPriceName', width: 200}


            ]);
            self.personalAmount =ko.observableArray([]);
            self.personalDisplay =ko.observableArray([]);



            self.yearMonthFilter = ko.observable(parseInt(moment(Date.now()).format("YYYYMM")));
            self.cateIndicator = ko.observable(0);
            self.salBonusCate = ko.observable(0);

            self.salIndAmountNames = ko.observableArray([]);
            self.salIndAmountNamesSelectedCode = ko.observable('');



            self.selectedEmployeeCode = ko.observable('1');
            self.individualPriceCode = ko.observable('');
            self.individualPriceName = ko.observable('');
            self.onSelectTab(self.onTab);


            self.salIndAmountNamesSelectedCode.subscribe(function (data) {
                self.personalAmount.removeAll();
                self.personalDisplay.removeAll();
                nts.uk.ui.errors.clearAll();
                if(self.personalDisplay().length<=10){
                    if (/Edge/.test(navigator.userAgent)) {
                        $('.scroll-header').removeClass('edge_scroll_header');
                    } else {
                        $('.scroll-header').removeClass('ci_scroll_header');
                    }
                }

                if (!data)
                    return;
                let temp = _.find(self.salIndAmountNames(), function (o) {
                    return o.individualPriceCode == data;
                });
                if (temp) {
                    self.individualPriceCode(temp.individualPriceCode);
                    self.individualPriceName(temp.individualPriceName);

                }
            });





        }

        startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.employeeReferenceDate().done(function (data) {
                if(data)
                    self.reloadCcg001(data.empExtraRefeDate);
                else
                    self.reloadCcg001(moment(Date.now()).format("YYYY/MM/DD"));

                $('#A5_7').focus();
            })

            dfd.resolve(self);
            return dfd.promise();
        }


        onSelectTab(param) {
            let self = this;
            nts.uk.ui.errors.clearAll();
            switch (param) {
                case 0:
                    //TODO
                    self.onTab(0);
                    self.titleTab(getText('QMM040_3'));
                    self.itemClassification(getText('QMM040_3'));
                    self.loadSalIndAmountName(PerValueCateCls.SUPPLY);
                    self.cateIndicator(CategoryIndicator.PAYMENT);
                    self.salBonusCate(SalBonusCate.SALARY);
                    $("#sidebar").ntsSideBar("active", param);
                    nts.uk.ui.errors.clearAll();
                    self.yearMonthFilter = ko.observable(parseInt(moment(Date.now()).format("YYYYMM")));
                    $('#A5_7').focus();
                    break;
                case 1:
                    //TODO
                    self.onTab(1);
                    self.titleTab(getText('QMM040_4'));
                    self.itemClassification(getText('QMM040_4'));
                    self.loadSalIndAmountName(PerValueCateCls.DEDUCTION);
                    self.cateIndicator(CategoryIndicator.DEDUCTION);
                    self.salBonusCate(SalBonusCate.SALARY);
                    $("#sidebar").ntsSideBar("active", param);
                    nts.uk.ui.errors.clearAll();
                    self.yearMonthFilter = ko.observable(parseInt(moment(Date.now()).format("YYYYMM")));
                    $('#A5_7').focus();
                    break;
                case 2:
                    //TODO
                    self.onTab(2);
                    self.loadSalIndAmountName(PerValueCateCls.SUPPLY);
                    self.cateIndicator(CategoryIndicator.PAYMENT);
                    self.salBonusCate(SalBonusCate.BONUSES);
                    self.titleTab(getText('QMM040_5'));
                    self.itemClassification(getText('QMM040_5'));
                    nts.uk.ui.errors.clearAll();
                    self.yearMonthFilter = ko.observable(parseInt(moment(Date.now()).format("YYYYMM")));
                    $("#sidebar").ntsSideBar("active", param);
                    nts.uk.ui.errors.clearAll();
                    self.yearMonthFilter = ko.observable(parseInt(moment(Date.now()).format("YYYYMM")));
                    $('#A5_7').focus();
                    break;
                case 3:
                    //TODO
                    self.onTab(3);
                    self.titleTab(getText('QMM040_6'));
                    self.itemClassification(getText('QMM040_6'));
                    self.loadSalIndAmountName(PerValueCateCls.DEDUCTION);
                    self.cateIndicator(CategoryIndicator.DEDUCTION);
                    self.salBonusCate(SalBonusCate.BONUSES);
                    $("#sidebar").ntsSideBar("active", param);
                    nts.uk.ui.errors.clearAll();
                    self.yearMonthFilter = ko.observable(parseInt(moment(Date.now()).format("YYYYMM")));
                    $('#A5_7').focus();
                    break;
                default:
                    //TODO
                    self.onTab(0);
                    self.titleTab(getText('QMM040_3'));
                    self.itemClassification(getText('QMM040_3'));
                    self.loadSalIndAmountName(PerValueCateCls.SUPPLY);
                    $("#sidebar").ntsSideBar("active", 0);
                    break;
            }
        }

        public loadSalIndAmountName(cateIndicator: number): void {
            let self = this;
            service.salIndAmountNameByCateIndicator(cateIndicator).done((data) => {

                if (data) {
                    self.salIndAmountNames(_.sortBy(data,function (o) {
                        return o.individualPriceCode;
                    }));
                    if (data.length > 0) {

                        self.salIndAmountNamesSelectedCode(self.salIndAmountNames()[0].individualPriceCode);
                        self.salIndAmountNamesSelectedCode.valueHasMutated();
                    }
                    else {
                        nts.uk.ui.dialog.alertError({messageId: "MsgQ_169"});
                        self.individualPriceCode('');
                        self.individualPriceName('');
                    }


                }

            });
        }




        filterData(): void {
            let self = this;
            self.yearMonthFilter();
            $('#A5_7').ntsError('check');
            if (nts.uk.ui.errors.hasError()) {
                return;
            }
            if (!self.employeeList) return;
            let temp = new Array();
            service.salIndAmountHisByPeValCode({
                //     self.cateIndicator = ko.observable(0);
                // self.salBonusCate = ko.observable(0);
                perValCode: self.individualPriceCode(),
                cateIndicator: self.cateIndicator(),
                salBonusCate: self.salBonusCate(),
                employeeIds: self.employeeList.map(x => x.employeeId)

            }).done(function (dataNameAndAmount) {
                self.employeeInfoImports=dataNameAndAmount.employeeInfoImports;
                let personalAmountData:Array<any>=new Array();
                personalAmountData=dataNameAndAmount.personalAmount.map(x => new PersonalAmount(x));

                console.log(dataNameAndAmount);
                self.personalAmount(personalAmountData);
                for(let i=0;i<self.personalAmount().length;i++){
                    let index=_.findIndex(self.employeeInfoImports,function (o) {
                        return o.sid==self.personalAmount()[i].empId
                    });
                    if(index != -1){
                        self.personalAmount()[i].employeeCode(self.employeeInfoImports[index].scd);
                        self.personalAmount()[i].businessName(self.employeeInfoImports[index].businessName);
                    }
                }
                personalAmountData= personalAmountData.sort(function(a, b){
                    return a.employeeCode() > b.employeeCode();
                })
                self.personalDisplay(personalAmountData);
                setTimeout(function () {
                    if( self.personalDisplay().length > 10) {
                        if (/Edge/.test(navigator.userAgent)) {
                            $('.scroll-header').addClass('edge_scroll_header');
                            $('.nts-fixed-body-container').addClass('edge_scroll_body');
                        } else {
                            $('.scroll-header').addClass('ci_scroll_header');
                            $('.nts-fixed-body-container').addClass('ci_scroll_body');
                        }

                    }
                }, 100);

            })
            // for(let i=0;i<self.personalAmount().length;i++){
            //     if(self.personalAmount()[i].startYearMonth <= this.yearMonthFilter() && self.personalAmount()[i].endYearMonth >= this.yearMonthFilter()){
            //         temp.push(self.personalAmount()[i])
            //     }
            // }
            //
            // //self.workIndividualPricesDisplay(_.sortBy(temp, ['employeeCode', 'startYaerMonth']));
            //
            // self.personalDisplay(_.sortBy(temp, ['employeeCode', 'startYearMonth']));
            // if(self.personalDisplay().length<=10){
            //     if (/Edge/.test(navigator.userAgent)) {
            //         $('.scroll-header').removeClass('edge_scroll_header');
            //     } else {
            //         $('.scroll-header').removeClass('ci_scroll_header');
            //     }
            // }


        }

        registerAmount(): void {
            let self = this;

            service.salIndAmountUpdateAll({
                salIndAmountUpdateCommandList: ko.toJS(self.personalAmount)}).done(function () {
                dialog.info({messageId: "Msg_15"});
            })
        }


        public reloadCcg001(empExtraRefeDate: string): void {
            let self = this;

            self.ccgcomponent = {
                /** Common properties */
                systemType: 1, // システム区分
                showEmployeeSelection: true,
                showQuickSearchTab: true,
                showAdvancedSearchTab: true,
                showBaseDate: true,
                showClosure: false,
                showAllClosure: false,
                showPeriod: false,
                periodFormatYM: false,

                /** Required parameter */
                baseDate: moment(new Date(empExtraRefeDate)).format("YYYY-MM-DD"), // 基準日
                periodStartDate: moment(new Date('06/05/1990')).format("YYYY-MM-DD"), // 対象期間開始日
                periodEndDate: moment(new Date('06/05/2018')).format("YYYY-MM-DD"), // 対象期間終了日
                inService: true,
                leaveOfAbsence: true,
                closed: true,
                retirement: true,

                /** Quick search tab options */
                showAllReferableEmployee: true,
                showOnlyMe: false,
                showSameWorkplace: false,
                showSameWorkplaceAndChild: false,

                /** Advanced search properties */
                showEmployment: false,
                showWorkplace: false,
                showClassification: false,
                showJobTitle: false,
                showWorktype: false,
                isMutipleCheck: true,
                showOnStart: true,
                tabindex: 2,
                /** Return data */
                returnDataFromCcg001: function (data: Ccg001ReturnedData) {
                    nts.uk.ui.errors.clearAll();
                    $('#A5_7').ntsError('check');
                    //self.selectedEmployee(data.listEmployee);
                    if (data && data.listEmployee.length>0) {
                        self.employeeList=data.listEmployee;
                        console.log(data.listEmployee);
                    }
                }
            }
            $('#com-ccg001').ntsGroupComponent(self.ccgcomponent);
        }
    }


    export interface GroupOption {

        /** Common properties */
        showEmployeeSelection?: boolean; // 検索タイプ
        systemType: number; // システム区分
        showQuickSearchTab?: boolean; // クイック検索
        showAdvancedSearchTab?: boolean; // 詳細検索
        showBaseDate?: boolean; // 基準日利用
        showClosure?: boolean; // 就業締め日利用
        showAllClosure?: boolean; // 全締め表示
        showPeriod?: boolean; // 対象期間利用
        periodFormatYM?: boolean; // 対象期間精度
        isInDialog?: boolean;

        /** Required parameter */
        baseDate?: string; // 基準日
        periodStartDate?: string; // 対象期間開始日
        periodEndDate?: string; // 対象期間終了日
        inService: boolean; // 在職区分
        leaveOfAbsence: boolean; // 休職区分
        closed: boolean; // 休業区分
        retirement: boolean; // 退職区分

        /** Quick search tab options */
        showAllReferableEmployee?: boolean; // 参照可能な社員すべて
        showOnlyMe?: boolean; // 自分だけ
        showSameWorkplace?: boolean; // 同じ職場の社員
        showSameWorkplaceAndChild?: boolean; // 同じ職場とその配下の社員

        /** Advanced search properties */
        showEmployment?: boolean; // 雇用条件
        showWorkplace?: boolean; // 職場条件
        showClassification?: boolean; // 分類条件
        showJobTitle?: boolean; // 職位条件
        showWorktype?: boolean; // 勤種条件
        isMutipleCheck?: boolean; // 選択モード
        isTab2Lazy?: boolean;

        /** Data returned */
        returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
    }

    export interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceId: string;
        workplaceName: string;
    }

    export interface Ccg001ReturnedData {
        baseDate: string; // 基準日
        closureId?: number; // 締めID
        periodStart: string; // 対象期間（開始)
        periodEnd: string; // 対象期間（終了）
        listEmployee: Array<EmployeeSearchDto>; // 検索結果
    }

    export interface TargetEmployee {
        code: string;
        name?: string;
        workplaceName?: string;
        isAlreadySetting?: boolean;
        sid: string;
        scd: string;
        businessname: string;
    }


    export interface ISalIndAmountName {
        individualPriceCode: string,
        individualPriceName: string
    }


    export class SalIndAmountName {
        individualPriceCode: string;
        individualPriceName: string;

        constructor(param: ISalIndAmountName) {
            this.individualPriceCode = param.individualPriceCode;
            this.individualPriceName = param.individualPriceName;
        }
    }


    export enum PerValueCateCls {
        SUPPLY = 0,
        DEDUCTION = 1
    }

    export enum SalBonusCate {
        //給与 = 0
        //賞与 = 1
        SALARY = 0,
        BONUSES = 1
    }

    export enum CategoryIndicator {
        //支給 = 0
        //控除 = 1
        PAYMENT = 0,
        DEDUCTION = 1
    }

    export interface IPersonalAmount {
        empId: string,
        historyId: string,
        employeeCode: string,
        businessName: string,
        startYearMonth: number,
        endYearMonth: number,
        amount: number
    }

    export class PersonalAmount {
        empId: string;
        historyId: string;
        employeeCode:KnockoutObservable<string>=ko.observable('');
        businessName: KnockoutObservable<string>=ko.observable('');
        startYearMonth: number;
        endYearMonth: number;
        period:string;
        amount: KnockoutObservable<number>=ko.observable(0);

        constructor(param:IPersonalAmount){
            let self=this;
            self.empId=param.empId;
            self.historyId=param.historyId;
            self.employeeCode=ko.observable('');
            self.businessName=ko.observable('');
            self.startYearMonth=param.startYearMonth;
            self.endYearMonth=param.endYearMonth;

            self.period=nts.uk.time.formatYearMonth(param.startYearMonth) + ' ~ ' + nts.uk.time.formatYearMonth(param.endYearMonth);
            self.amount(param.amount);
        }


    }


}