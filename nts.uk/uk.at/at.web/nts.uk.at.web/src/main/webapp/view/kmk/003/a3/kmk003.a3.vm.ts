module a3 {
    import TimeRoundingSettingDto = nts.uk.at.view.kmk003.a.service.model.common.TimeRoundingSettingDto;
    import TimeZoneRoundingDto = nts.uk.at.view.kmk003.a.service.model.common.TimeZoneRoundingDto;
    import OverTimeOfTimeZoneSetDto = nts.uk.at.view.kmk003.a.service.model.common.OverTimeOfTimeZoneSetDto;
    import DiffTimeOTTimezoneSetDto = nts.uk.at.view.kmk003.a.service.model.difftimeset.DiffTimeOTTimezoneSetDto;
    import FlOTTimezoneDto = nts.uk.at.view.kmk003.a.service.model.flowset.FlOTTimezoneDto;
    import FlTimeSettingDto = nts.uk.at.view.kmk003.a.service.model.flowset.FlTimeSettingDto;
    import WorkTimeSettingEnumDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeSettingEnumDto;
    import FlowOTTimezoneModel = nts.uk.at.view.kmk003.a.viewmodel.flowset.FlowOTTimezoneModel;
    import OverTimeOfTimeZoneSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.OverTimeOfTimeZoneSetModel;
    import MainSettingModel = nts.uk.at.view.kmk003.a.viewmodel.MainSettingModel;
    import OvertimeWorkFrameFindDto = nts.uk.at.view.kmk003.a3.service.model.OvertimeWorkFrameFindDto;
    import SettingMethod = nts.uk.at.view.kmk003.a.viewmodel.SettingMethod;
    class ScreenModel {

        // fixed table options Fixed Detail
        fixTableOptionOnedayFixed: any;
        fixTableOptionMorningFixed: any;
        fixTableOptionAfternoonFixed: any;

        // fixed table options Fixed Simple
        fixTableOptionOnedayFixedSimple: any;

        // fixed table options DiffTime Detail
        fixTableOptionOnedayDiffTime: any;
        fixTableOptionMorningDiffTime: any;
        fixTableOptionAfternoonDiffTime: any;

        // fixed table options DiffTime Simple
        fixTableOptionOnedayDiffTimeSimple: any;

        // fixed table options Flex
        fixTableOptionOnedayFlex: any;
        fixTableOptionMorningFlex: any;
        fixTableOptionAfternoonFlex: any;

        // fixed table options Flow
        fixTableOptionOvertimeFlow: any;
        fixTableOptionOvertimeFlowSimple: any;

        // Flags
        isFlowMode: KnockoutObservable<boolean>;
        isFlexMode: KnockoutObservable<boolean>;
        isFixedMode: KnockoutObservable<boolean>;
        isDiffTimeMode: KnockoutObservable<boolean>;
        isDetailMode: KnockoutObservable<boolean>;
        isUseHalfDay: KnockoutObservable<boolean>;

        // fixed table datasource
        dataSourceOnedayFixed: KnockoutObservableArray<any>;
        dataSourceMorningFixed: KnockoutObservableArray<any>;
        dataSourceAfternoonFixed: KnockoutObservableArray<any>;

        dataSourceOnedayDiffTime: KnockoutObservableArray<any>;
        dataSourceMorningDiffTime: KnockoutObservableArray<any>;
        dataSourceAfternoonDiffTime: KnockoutObservableArray<any>;

        dataSourceOnedayFlex: KnockoutObservableArray<any>;
        dataSourceMorningFlex: KnockoutObservableArray<any>;
        dataSourceAfternoonFlex: KnockoutObservableArray<any>;

        dataSourceOvertimeFlow: KnockoutObservableArray<any>;

        // miscellaneous
        autoCalUseAttrs: KnockoutObservableArray<any>;
        selectedCodeAutoCalUse: KnockoutObservable<any>;
        settingEnum: WorkTimeSettingEnumDto;
        mainSettingModel: MainSettingModel;
        lstOvertimeWorkFrame: OvertimeWorkFrameFindDto[];
        
        //define for 精算�primitive value
        lstSettlementOrder: any[];
        screenSettingMode: KnockoutObservable<number>;
        isNewMode: KnockoutObservable<boolean>;
        
        /**
        * Constructor.
        */
        constructor(settingEnum: WorkTimeSettingEnumDto, mainSettingModel: MainSettingModel, isDetailMode: KnockoutObservable<boolean>,
            isUseHalfDay: KnockoutObservable<boolean>, isNewMode: KnockoutObservable<boolean>) {
            let self = this;
            self.isNewMode = isNewMode;
            self.screenSettingMode = ko.observable(0);
            self.settingEnum = settingEnum;
            self.mainSettingModel = mainSettingModel;
            self.isDetailMode = isDetailMode;
            self.isUseHalfDay = isUseHalfDay;
            self.isFlexMode = self.mainSettingModel.workTimeSetting.isFlex;
            self.isFlowMode = self.mainSettingModel.workTimeSetting.isFlow;
            self.isFixedMode = self.mainSettingModel.workTimeSetting.isFixed;
            self.isDiffTimeMode = self.mainSettingModel.workTimeSetting.isDiffTime;
            
            self.autoCalUseAttrs = ko.observableArray([
                { code: 1, name: nts.uk.resource.getText("KMK003_142") },
                { code: 0, name: nts.uk.resource.getText("KMK003_143") }
            ]);
            
            self.lstSettlementOrder = [];
            //init list order
            for (let i = 1; i <= 10; i++) {
                self.lstSettlementOrder.push({
                    settlementOrder: i,
                    settlementOrderName: i.toString()
                });
            }

            self.selectedCodeAutoCalUse = ko.observable('1');
            
            self.isNewMode.subscribe((v) => {
                if (v) {
                    self.mainSettingModel.workTimeSetting.workTimeDivision.workTimeMethodSet.valueHasMutated();
                }
            });
            self.mainSettingModel.workTimeSetting.workTimeDivision.workTimeMethodSet.subscribe((v) => {
                if (self.isNewMode()) {
                    self.screenSettingMode(v);
                    self.isDetailMode.notifySubscribers(self.isDetailMode());
                    if (v == SettingMethod.FLOW) {
                        self.dataSourceOvertimeFlow.valueHasMutated();
                    }
                }
            });
            
            self.mainSettingModel.workTimeSetting.workTimeDivision.workTimeDailyAtr.subscribe((v) => {
                if (self.isNewMode()) {
                    self.screenSettingMode(v);
                    if(v == SettingMethod.FLOW) {
                        self.dataSourceOvertimeFlow.valueHasMutated();
                    }
                    self.isDetailMode.notifySubscribers(self.isDetailMode());
                }
            });

            self.setDatasource();
        }

        /**
         * Start page
         */
        public startPage(): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();
            nts.uk.at.view.kmk003.a3.service.findAllOvertimeWorkFrame().done(data => {
                self.lstOvertimeWorkFrame = data;
                self.setFixedTableOption();
                dfd.resolve();
            });

            return dfd.promise();
        }
        
        public setFixedTableOption(): void {
            let self = this;
            // fixed one day detail
            self.fixTableOptionOnedayFixed = self.getDefaultFixedTableOption();
            self.fixTableOptionOnedayFixed.dataSource = self.dataSourceOnedayFixed;
            self.fixTableOptionOnedayFixed.columns = self.columnSettingFixedAndDiffTime();
            self.fixTableOptionOnedayFixed.tabindex = 56;

            // fixed one day simple
            self.fixTableOptionOnedayFixedSimple = self.getDefaultFixedTableOption();
            self.fixTableOptionOnedayFixedSimple.dataSource = self.dataSourceOnedayFixed;
            self.fixTableOptionOnedayFixedSimple.columns = self.columnSettingFlex();
            self.fixTableOptionOnedayFixedSimple.tabindex = 56;

            // fixed morning detail
            self.fixTableOptionMorningFixed = self.getDefaultFixedTableOption();
            self.fixTableOptionMorningFixed.dataSource = self.dataSourceMorningFixed;
            self.fixTableOptionMorningFixed.columns = self.columnSettingFixedAndDiffTime();
            self.fixTableOptionMorningFixed.tabindex = 57;

            // fixed afternoon detail
            self.fixTableOptionAfternoonFixed = self.getDefaultFixedTableOption();
            self.fixTableOptionAfternoonFixed.dataSource = self.dataSourceAfternoonFixed;
            self.fixTableOptionAfternoonFixed.columns = self.columnSettingFixedAndDiffTime();
            self.fixTableOptionAfternoonFixed.tabindex = 58;

            // difftime one day detail
            self.fixTableOptionOnedayDiffTime = self.getDefaultFixedTableOption();
            self.fixTableOptionOnedayDiffTime.dataSource = self.dataSourceOnedayDiffTime;
            self.fixTableOptionOnedayDiffTime.columns = self.columnSettingFixedAndDiffTime();
            self.fixTableOptionOnedayDiffTime.tabindex = 56;

            // difftime one day simple
            self.fixTableOptionOnedayDiffTimeSimple = self.getDefaultFixedTableOption();
            self.fixTableOptionOnedayDiffTimeSimple.dataSource = self.dataSourceOnedayDiffTime;
            self.fixTableOptionOnedayDiffTimeSimple.columns = self.columnSettingFlex();
            self.fixTableOptionOnedayDiffTimeSimple.tabindex = 56;

            // difftime morning detail
            self.fixTableOptionMorningDiffTime = self.getDefaultFixedTableOption();
            self.fixTableOptionMorningDiffTime.dataSource = self.dataSourceMorningDiffTime;
            self.fixTableOptionMorningDiffTime.columns = self.columnSettingFixedAndDiffTime();
            self.fixTableOptionMorningDiffTime.tabindex = 57;

            // difftime afternoon detail
            self.fixTableOptionAfternoonDiffTime = self.getDefaultFixedTableOption();
            self.fixTableOptionAfternoonDiffTime.dataSource = self.dataSourceAfternoonDiffTime;
            self.fixTableOptionAfternoonDiffTime.columns = self.columnSettingFixedAndDiffTime();
            self.fixTableOptionAfternoonDiffTime.tabindex = 58;

            // flex one day
            self.fixTableOptionOnedayFlex = self.getDefaultFixedTableOption();
            self.fixTableOptionOnedayFlex.dataSource = self.dataSourceOnedayFlex;
            self.fixTableOptionOnedayFlex.columns = self.columnSettingFlex();
            self.fixTableOptionOnedayFlex.tabindex = 56;

            // flex morning
            self.fixTableOptionMorningFlex = self.getDefaultFixedTableOption();
            self.fixTableOptionMorningFlex.dataSource = self.dataSourceMorningFlex;
            self.fixTableOptionMorningFlex.columns = self.columnSettingFlex();
            self.fixTableOptionMorningFlex.tabindex = 57;

            // flex afternoon
            self.fixTableOptionAfternoonFlex = self.getDefaultFixedTableOption();
            self.fixTableOptionAfternoonFlex.dataSource = self.dataSourceAfternoonFlex;
            self.fixTableOptionAfternoonFlex.columns = self.columnSettingFlex();
            self.fixTableOptionAfternoonFlex.tabindex = 58;

            // flow detail
            self.fixTableOptionOvertimeFlow = self.getDefaultFixedTableOption();
            self.fixTableOptionOvertimeFlow.dataSource = self.dataSourceOvertimeFlow;
            self.fixTableOptionOvertimeFlow.columns = self.columnSettingOvertimeFlow();
            self.fixTableOptionOvertimeFlow.tabindex = 59;

            // flow simple
            self.fixTableOptionOvertimeFlowSimple = self.getDefaultFixedTableOption();
            self.fixTableOptionOvertimeFlowSimple.dataSource = self.dataSourceOvertimeFlow;
            self.fixTableOptionOvertimeFlowSimple.columns = self.columnSettingFlowSimple();
            self.fixTableOptionOvertimeFlowSimple.tabindex = 59;

        }

        /**
         * Get default fixed table option
         */
        private getDefaultFixedTableOption(): any {
            return {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 10,
                isShowButton: true,
                dataSource: null,
                isMultipleSelect: true,
                columns: null,
                tabindex: null
            };
        }

        
        /**
         * function convert dto to model
         */
        private toModelFixedColumnSetting(dataDTO: OverTimeOfTimeZoneSetDto): any {
            return {
                timezone: ko.observable({ startTime: dataDTO.timezone.start, endTime: dataDTO.timezone.end }),
                rounding: ko.observable(dataDTO.timezone.rounding.rounding),
                roundingTime: ko.observable(dataDTO.timezone.rounding.roundingTime),
                otFrameNo: ko.observable(dataDTO.otFrameNo),
                earlyOTUse: ko.observable(dataDTO.earlyOTUse),
                legalOTframeNo: ko.observable(dataDTO.legalOTframeNo),
                settlementOrder: ko.observable(dataDTO.settlementOrder)
            }
        }
        /**
         * function convert dto to model
         */
        private toModelDiffTimeColumnSetting(dataDTO: DiffTimeOTTimezoneSetDto): any {
            return {
                timezone: ko.observable({ startTime: dataDTO.timezone.start, endTime: dataDTO.timezone.end }),
                rounding: ko.observable(dataDTO.timezone.rounding.rounding),
                roundingTime: ko.observable(dataDTO.timezone.rounding.roundingTime),
                otFrameNo: ko.observable(dataDTO.otFrameNo),
                earlyOTUse: ko.observable(dataDTO.earlyOTUse),
                legalOTframeNo: ko.observable(dataDTO.legalOTframeNo),
                settlementOrder: ko.observable(dataDTO.settlementOrder)
            }
        }
        
        /**
         * function convert data model of client to parent
         */
        private toModelDiffTimeDto(dataModel: any, workTimezoneNo: number): DiffTimeOTTimezoneSetDto {
            var rounding: TimeRoundingSettingDto = {
                roundingTime: dataModel.roundingTime(),
                rounding: dataModel.rounding()
            };
            var timezone: TimeZoneRoundingDto = {
                rounding: rounding,
                start: dataModel.timezone().startTime,
                end: dataModel.timezone().endTime
            };
            var dataDTO: DiffTimeOTTimezoneSetDto = {
                workTimezoneNo: workTimezoneNo,
                restraintTimeUse: false,
                timezone: timezone,
                otFrameNo: dataModel.otFrameNo(),
                earlyOTUse: dataModel.earlyOTUse(),
                legalOTframeNo: dataModel.legalOTframeNo?dataModel.legalOTframeNo():1,
                settlementOrder: dataModel.settlementOrder?dataModel.settlementOrder():1,
                isUpdateStartTime: false
            };
            return dataDTO;
        }

        private setDatasource(): void {
            let self = this;

            const fixed = self.mainSettingModel.fixedWorkSetting;
            const difftime = self.mainSettingModel.diffWorkSetting;
            const flex = self.mainSettingModel.flexWorkSetting;
            const flow = self.mainSettingModel.flowWorkSetting;
            
            // one day
            self.dataSourceOnedayFixed = fixed.getHDWtzOneday().workTimezone.convertedList;
            self.dataSourceOnedayDiffTime = difftime.getHDWtzOneday().workTimezone.convertedList;
            self.dataSourceOnedayFlex =  flex.getHDWtzOneday().workTimezone.convertedList;

            // morning
            self.dataSourceMorningFixed = fixed.getHDWtzMorning().workTimezone.convertedList;
            self.dataSourceMorningDiffTime = difftime.getHDWtzMorning().workTimezone.convertedList;
            self.dataSourceMorningFlex = flex.getHDWtzMorning().workTimezone.convertedList;

            // afternoon 
            self.dataSourceAfternoonFixed = fixed.getHDWtzAfternoon().workTimezone.convertedList;
            self.dataSourceAfternoonDiffTime = difftime.getHDWtzAfternoon().workTimezone.convertedList;
            self.dataSourceAfternoonFlex = flex.getHDWtzAfternoon().workTimezone.convertedList;

            // flow
            self.dataSourceOvertimeFlow = flow.halfDayWorkTimezone.workTimeZone.convertedList;

        }

        /**
         * init array setting column option overtime flow mode
         */
        private columnSettingOvertimeFlow(): Array<any> {
            let self = this;
            let stringColumns: Array<any> = self.columnSettingFlowSimple();
            stringColumns.push(
                {
                    headerText: nts.uk.resource.getText("KMK003_186"),
                    key: "inLegalOTFrameNo",
                    dataSource: self.lstOvertimeWorkFrame,
                    defaultValue: ko.observable(1),
                    width: 120,
                    template: `<div data-key="overtimeWorkFrNo" class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'overtimeWorkFrNo',
                                    visibleItemsCount: 10,
                                    optionsText: 'overtimeWorkFrName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'overtimeWorkFrName', length: 12 }]}">
                                </div>`
                });
            stringColumns.push({
                headerText: nts.uk.resource.getText("KMK003_187"),
                key: "settlementOrder",
                dataSource: self.lstSettlementOrder,
                defaultValue: ko.observable(1),
                width: 100,
                template: `<div data-key="settlementOrder" class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'settlementOrder',
                                    visibleItemsCount: 10,
                                    optionsText: 'settlementOrderName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'settlementOrderName', length: 12 }]}">
                                </div>`
            });
            return stringColumns;
        }
        
        private columnSettingFlowSimple(): Array<any> {
            let self = this;
            return [
                 {
                     headerText: nts.uk.resource.getText("KMK003_174"),
                     key: "elapsedTime",
                     defaultValue: ko.observable(0), 
                     width: 100, 
                     template: `<input data-bind="ntsTimeEditor: {
                            constraint: 'AttendanceTime',
                            mode: 'time',
                            inputFormat: 'time',
                            required: true }" />`
                 },
                 {
                     headerText: nts.uk.resource.getText("KMK003_56"),
                     key: "roundingTime",
                     dataSource: self.settingEnum.roundingTime,
                     defaultValue: ko.observable(0),
                     width: 80,
                     template: `<div data-key="value" class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'value',
                                    visibleItemsCount: 8,
                                    optionsText: 'localizedName',
                                    name: '#[KMK003_201]',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'localizedName', length: 10 }]}">
                                </div>`
                 },
                 {
                     headerText: nts.uk.resource.getText("KMK003_57"),
                     key: "rounding",
                     isRoudingColumn: true,
                     unitAttrName: 'roundingTime',
                     dataSource: self.settingEnum.rounding,
                     defaultValue: ko.observable(0),
                     width: 160,
                     template: `<div data-key="value" class="column-combo-box" data-bind="ntsComboBox: {
                                    name: '#[KMK003_202]',
                                    optionsValue: 'value',
                                    visibleItemsCount: 3,
                                    optionsText: 'localizedName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'localizedName', length: 10 }]}">
                                </div>`
                 },
                 {
                     headerText: nts.uk.resource.getText("KMK003_58"),
                     key: "otFrameNo",
                     dataSource: self.lstOvertimeWorkFrame,
                     defaultValue: ko.observable(1),
                     width: 150,
                     template: `<div data-key="overtimeWorkFrNo" class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'overtimeWorkFrNo',
                                    visibleItemsCount: 10,
                                    optionsText: 'overtimeWorkFrName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'overtimeWorkFrName', length: 12 }]}">
                                </div>`
                 }];
            }
        /**
         * function get column setting fixed and diff time
         */
         private columnSettingFixedAndDiffTime(): Array<any> {
            let self = this;
             var arraySettingFlex : Array<any> = self.columnSettingFlex();
             arraySettingFlex.push({
                 headerText: nts.uk.resource.getText("KMK003_186"),
                 key: "legalOTframeNo",
                 dataSource: self.lstOvertimeWorkFrame,
                 defaultValue: ko.observable(1),
                 width: 130,
                 template: `<div data-key="overtimeWorkFrNo" class="column-combo-box" data-bind="ntsComboBox: {
                                    name: '#[KMK003_204]',
                                    optionsValue: 'overtimeWorkFrNo',
                                    visibleItemsCount: 10,
                                    optionsText: 'overtimeWorkFrName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'overtimeWorkFrName', length: 12 }]}">
                                </div>`
             });
             arraySettingFlex.push({
                 headerText: nts.uk.resource.getText("KMK003_187"),
                 key: "settlementOrder",
                 dataSource: self.lstSettlementOrder,
                 defaultValue: ko.observable(1),
                 width: 50,
                 template: `<div data-key="settlementOrder" class="column-combo-box" data-bind="ntsComboBox: {
                                    name: '#[KMK003_187]',
                                    optionsValue: 'settlementOrder',
                                    visibleItemsCount: 10,
                                    optionsText: 'settlementOrderName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'settlementOrderName', length: 12 }]}">
                                </div>`
             });
            return arraySettingFlex;
        }
        
        /**
         * init array setting column option morning flex
         */
         private columnSettingFlex(): Array<any> {
            let self = this;
            return [
                {
                    headerText: nts.uk.resource.getText("KMK003_54"), 
                    key: "timezone",
                    defaultValue: ko.observable({ startTime: 0, endTime: 0 }), 
                    width: 250, 
                    template: `<div data-bind="ntsTimeRangeEditor: { 
                        startConstraint: 'TimeWithDayAttr', endConstraint: 'TimeWithDayAttr',
                        required: true, enable: true, inputFormat: 'time',  startTimeNameId: '#[KMK003_166]', endTimeNameId: '#[KMK003_167]',paramId:'KMK003_89'}"/>`
                },
                {
                    headerText: nts.uk.resource.getText("KMK003_56"),
                    key: "roundingTime",
                    dataSource: self.settingEnum.roundingTime,
                    defaultValue: ko.observable(0),
                    width: 80,
                    template: `<div data-key="value" class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'value',
                                    visibleItemsCount: 8,
                                    optionsText: 'localizedName',
                                    name: '#[KMK003_201]',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'localizedName', length: 10 }]}">
                                </div>`
                },
                {
                    headerText: nts.uk.resource.getText("KMK003_57"),
                    key: "rounding",
                    isRoudingColumn: true,
                    unitAttrName: 'roundingTime',
                    dataSource: self.settingEnum.rounding,
                    defaultValue: ko.observable(0),
                    width: 170,
                    template: `<div data-key="value" class="column-combo-box" data-bind="ntsComboBox: {
                                    name: '#[KMK003_202]',
                                    optionsValue: 'value',
                                    visibleItemsCount: 3,
                                    optionsText: 'localizedName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'localizedName', length: 10 }]}">
                                </div>`
                },
                {
                    headerText: nts.uk.resource.getText("KMK003_58"), 
                    key: "otFrameNo",
                    dataSource: self.lstOvertimeWorkFrame,
                    defaultValue: ko.observable(1),
                    width: 130,
                    template: `<div data-key="overtimeWorkFrNo" class="column-combo-box" data-bind="ntsComboBox: {
                                    name: '#[KMK003_203]',
                                    optionsValue: 'overtimeWorkFrNo',
                                    visibleItemsCount: 10,
                                    optionsText: 'overtimeWorkFrName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'overtimeWorkFrName', length: 12 }]}">
                                </div>`
                },
                {
                    headerText: nts.uk.resource.getText("KMK003_182"),
                    key: "earlyOTUse",
                    defaultValue: ko.observable(false),
                    width: 50,
                    template: `<div data-bind="ntsCheckBox: { enable: true }">`
                }
            ];
        }

    }

    class KMK003A3BindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        private getData() {
            let self = this;
            // service.findWorkTimeSetByCode()
        }

        /**
         * Update
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                .mergeRelativePath('/view/kmk/003/a3/index.xhtml').serialize();
            //get data
            let input = valueAccessor();
            var settingEnum: WorkTimeSettingEnumDto = input.enum;
            var mainSettingModel: MainSettingModel = input.mainModel;
            var isDetailMode:  KnockoutObservable<boolean> = input.isDetailMode;
            var useHalfDay:  KnockoutObservable<boolean> = input.useHalfDay;
            var isNewMode: KnockoutObservable<boolean> = input.isNewMode;
            let screenModel = new ScreenModel(settingEnum, mainSettingModel, isDetailMode, useHalfDay,isNewMode);
            screenModel.startPage().done(() => {
                $(element).load(webserviceLocator, function() {
                    ko.cleanNode($(element)[0]);
                    ko.applyBindingsToDescendants(screenModel, $(element)[0]);
                });
            });
        }

    }
    ko.bindingHandlers['ntsKMK003A3'] = new KMK003A3BindingHandler();

}
