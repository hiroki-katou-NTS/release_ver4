module a3 {
    import TimeRoundingSettingDto = nts.uk.at.view.kmk003.a.service.model.common.TimeRoundingSettingDto;
    import TimeZoneRoundingDto = nts.uk.at.view.kmk003.a.service.model.common.TimeZoneRoundingDto;
    import OverTimeOfTimeZoneSetDto = nts.uk.at.view.kmk003.a.service.model.common.OverTimeOfTimeZoneSetDto;
    import FlOTTimezoneDto = nts.uk.at.view.kmk003.a.service.model.flowset.FlOTTimezoneDto;
    import FlTimeSettingDto = nts.uk.at.view.kmk003.a.service.model.flowset.FlTimeSettingDto;
    import WorkTimeSettingEnumDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeSettingEnumDto;
    import FlOTTimezoneModel = nts.uk.at.view.kmk003.a.viewmodel.flowset.FlOTTimezoneModel;
    import OverTimeOfTimeZoneSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.OverTimeOfTimeZoneSetModel;
    import MainSettingModel = nts.uk.at.view.kmk003.a.viewmodel.MainSettingModel;
    class ScreenModel {

        fixTableOptionOnedayFixed: any;
        fixTableOptionMorningFixed: any;
        fixTableOptionAfternoonFixed: any;
        fixTableOptionOnedayFlex: any;
        fixTableOptionMorningFlex: any;
        fixTableOptionAfternoonFlex: any;
        fixTableOptionOvertimeFlow: any;
        isFlowMode: KnockoutObservable<boolean>;
        isFlexMode: KnockoutObservable<boolean>;
        isFixedMode: KnockoutObservable<boolean>;
        isLoading: KnockoutObservable<boolean>;
        dataSourceOnedayFixed: KnockoutObservableArray<any>;
        dataSourceMorningFixed: KnockoutObservableArray<any>;
        dataSourceAfternoonFixed: KnockoutObservableArray<any>;
        dataSourceOnedayFlex: KnockoutObservableArray<any>;
        dataSourceMorningFlex: KnockoutObservableArray<any>;
        dataSourceAfternoonFlex: KnockoutObservableArray<any>;
        dataSourceOvertimeFlow: KnockoutObservableArray<any>;
        autoCalUseAttrs: KnockoutObservableArray<any>;
        selectedCodeAutoCalUse: KnockoutObservable<any>;
        settingEnum: WorkTimeSettingEnumDto;
        mainSettingModel: MainSettingModel;
        lstSelectOrderModel: SettlementOrder[];

        /**
        * Constructor.
        */
        constructor(settingEnum: WorkTimeSettingEnumDto, mainSettingModel: MainSettingModel, isLoading: KnockoutObservable<boolean>) {
            let self = this;
            self.settingEnum = settingEnum;
            self.mainSettingModel = mainSettingModel;
            self.isLoading = isLoading;
            self.isFlexMode = self.mainSettingModel.workTimeSetting.isFlex;
            self.isFlowMode = self.mainSettingModel.workTimeSetting.isFlow;
            self.isFixedMode = self.mainSettingModel.workTimeSetting.isFixed;
            self.autoCalUseAttrs = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText("KMK003_142") },
                { code: 1, name: nts.uk.resource.getText("KMK003_143") }
            ]);
            self.dataSourceOvertimeFlow = ko.observableArray([]);
            self.dataSourceOnedayFixed = ko.observableArray([]);
            self.dataSourceMorningFixed = ko.observableArray([]);
            self.dataSourceAfternoonFixed = ko.observableArray([]);
            self.dataSourceOnedayFlex = ko.observableArray([]);
            self.dataSourceMorningFlex = ko.observableArray([]);
            self.dataSourceAfternoonFlex = ko.observableArray([]);
            self.isLoading.subscribe(function(isLoading: boolean) {
                if (isLoading) {
                    self.updateDataModel();
                }
            });

           
            self.lstSelectOrderModel = [];
            for (var i: number = 1; i <= 10; i++) {
                self.lstSelectOrderModel.push({ code: i, name: '' + i });
            }
            self.selectedCodeAutoCalUse = ko.observable('1');
            self.fixTableOptionOnedayFixed = {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 5,
                isShowButton: true,
                dataSource: self.dataSourceOnedayFixed,
                isMultipleSelect: true,
                columns: self.columnSettingFixed(),
                tabindex: -1
            };
            self.fixTableOptionMorningFixed = {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 5,
                isShowButton: true,
                dataSource: self.dataSourceMorningFixed,
                isMultipleSelect: true,
                columns: self.columnSettingFixed(),
                tabindex: -1
            };
            self.fixTableOptionAfternoonFixed = {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 5,
                isShowButton: true,
                dataSource: self.dataSourceAfternoonFixed,
                isMultipleSelect: true,
                columns: self.columnSettingFixed(),
                tabindex: -1
            };
            self.fixTableOptionOnedayFlex = {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 5,
                isShowButton: true,
                dataSource: self.dataSourceOnedayFlex,
                isMultipleSelect: true,
                columns: self.columnSettingFlex(),
                tabindex: -1
            };
            self.fixTableOptionMorningFlex = {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 5,
                isShowButton: true,
                dataSource: self.dataSourceMorningFlex,
                isMultipleSelect: true,
                columns: self.columnSettingFlex(),
                tabindex: -1
            };
            self.fixTableOptionAfternoonFlex = {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 5,
                isShowButton: true,
                dataSource: self.dataSourceAfternoonFlex,
                isMultipleSelect: true,
                columns: self.columnSettingFlex(),
                tabindex: -1
            };
            self.fixTableOptionOvertimeFlow = {
                maxRow: 10,
                minRow: 0,
                maxRowDisplay: 10,
                isShowButton: true,
                dataSource: self.dataSourceOvertimeFlow,
                isMultipleSelect: true,
                columns: self.columnSettingOvertimeFlow(),
                tabindex: -1
            };
            
            // update time zone flow
            self.dataSourceOvertimeFlow.subscribe(function(dataFlow: any[]) {
                var lstTimezone: FlOTTimezoneDto[] = [];
                var worktimeNo: number = 0;
                for (var dataModel of dataFlow) {
                    worktimeNo++;
                    lstTimezone.push(self.toModelFlowDto(dataModel, worktimeNo));
                }
                self.mainSettingModel.flowWorkSetting.halfDayWorkTimezone.workTimeZone.updateTimezone(lstTimezone);
            });

            // update time zone fixed 
            self.dataSourceOnedayFixed.subscribe(function(dataFixed: any[]) {
                var lstOTTimezone: OverTimeOfTimeZoneSetDto[] = [];
                var workTimezoneNo: number = 0;
                for (var dataModel of dataFixed) {
                    workTimezoneNo++;
                    lstOTTimezone.push(self.toModelFixedDto(dataModel, workTimezoneNo));
                }
                self.mainSettingModel.fixedWorkSetting.getHDWtzOneday().workTimezone.updateOvertimeZone(lstOTTimezone);
            });
            
            self.dataSourceMorningFixed.subscribe(function(dataFixed: any[]) {
                var lstOTTimezone: OverTimeOfTimeZoneSetDto[] = [];
                var workTimezoneNo: number = 0;
                for (var dataModel of dataFixed) {
                    workTimezoneNo++;
                    lstOTTimezone.push(self.toModelFixedDto(dataModel, workTimezoneNo));
                }
                self.mainSettingModel.fixedWorkSetting.getHDWtzMorning().workTimezone.updateOvertimeZone(lstOTTimezone);
            });
            
            self.dataSourceAfternoonFixed.subscribe(function(dataFixed: any[]) {
                var lstOTTimezone: OverTimeOfTimeZoneSetDto[] = [];
                var workTimezoneNo: number = 0;
                for (var dataModel of dataFixed) {
                    workTimezoneNo++;
                    lstOTTimezone.push(self.toModelFixedDto(dataModel, workTimezoneNo));
                }
                self.mainSettingModel.fixedWorkSetting.getHDWtzAfternoon().workTimezone.updateOvertimeZone(lstOTTimezone);
            });
            
            // update time zone flexset
            self.dataSourceOnedayFlex.subscribe(function(dataFlex: any[]) {
                var lstOTTimezone: OverTimeOfTimeZoneSetDto[] = [];
                var workTimezoneNo: number = 0;
                for (var dataModel of dataFlex) {
                    workTimezoneNo++;
                    lstOTTimezone.push(self.toModelFlexDto(dataModel, workTimezoneNo));
                }
                self.mainSettingModel.flexWorkSetting.getHDWtzOneday().workTimezone.updateOvertimeZone(lstOTTimezone);
            });
            
            self.dataSourceMorningFlex.subscribe(function(dataFlex: any[]) {
                var lstOTTimezone: OverTimeOfTimeZoneSetDto[] = [];
                var workTimezoneNo: number = 0;
                for (var dataModel of dataFlex) {
                    workTimezoneNo++;
                    lstOTTimezone.push(self.toModelFlexDto(dataModel, workTimezoneNo));
                }
                self.mainSettingModel.flexWorkSetting.getHDWtzMorning().workTimezone.updateOvertimeZone(lstOTTimezone);
            });
            
            self.dataSourceAfternoonFlex.subscribe(function(dataFlex: any[]) {
                var lstOTTimezone: OverTimeOfTimeZoneSetDto[] = [];
                var workTimezoneNo: number = 0;
                for (var dataModel of dataFlex) {
                    workTimezoneNo++;
                    lstOTTimezone.push(self.toModelFlexDto(dataModel, workTimezoneNo));
                }
                self.mainSettingModel.flexWorkSetting.getHDWtzAfternoon().workTimezone.updateOvertimeZone(lstOTTimezone);
            });
        }
        
        private updateDataModel(): void {
            var self = this;
            if (self.isFlowMode()) {
                var dataFlow: any[] = [];
                for (var dataModelFlow of self.mainSettingModel.flowWorkSetting.halfDayWorkTimezone.workTimeZone.lstOTTimezone) {
                    dataFlow.push(self.toModelFlowColumnSetting(dataModelFlow.toDto()));
                }
                self.dataSourceOvertimeFlow(dataFlow);
            }

            if (self.isFixedMode()) {
                var dataFixedOneday: any[] = [];
                for (var dataModelFixed of self.mainSettingModel.fixedWorkSetting.getHDWtzOneday().workTimezone.lstOTTimezone) {
                    dataFixedOneday.push(self.toModelFixedColumnSetting(dataModelFixed.toDto()));
                }
                self.dataSourceOnedayFixed(dataFixedOneday);
                var dataFixedMorning: any[] = [];
                for (var dataModelMorningFixed of self.mainSettingModel.fixedWorkSetting.getHDWtzMorning().workTimezone.lstOTTimezone) {
                    dataFixedMorning.push(self.toModelFixedColumnSetting(dataModelMorningFixed.toDto()));
                }
                self.dataSourceMorningFixed(dataFixedMorning);
                var dataFixedAfternoon: any[] = [];
                for (var dataModelAfternoonFixed of self.mainSettingModel.fixedWorkSetting.getHDWtzAfternoon().workTimezone.lstOTTimezone) {
                    dataFixedAfternoon.push(self.toModelFixedColumnSetting(dataModelAfternoonFixed.toDto()));
                }
                self.dataSourceAfternoonFixed(dataFixedAfternoon);
            }

            if (self.isFlexMode()) {
                var dataFlexOneday: any[] = [];
                for (var dataModelOnedayFlex of self.mainSettingModel.flexWorkSetting.getHDWtzOneday().workTimezone.lstOTTimezone) {
                    dataFlexOneday.push(self.toModelFlexColumnSetting(dataModelOnedayFlex.toDto()));
                }
                self.dataSourceOnedayFlex(dataFlexOneday);
                var dataFlexMorning: any[] = [];
                if (self.mainSettingModel.flexWorkSetting.getHDWtzMorning().workTimezone.lstOTTimezone) {
                    for (var dataModelMorningFlex of self.mainSettingModel.flexWorkSetting.getHDWtzMorning().workTimezone.lstOTTimezone) {
                        dataFlexMorning.push(self.toModelFlexColumnSetting(dataModelMorningFlex.toDto()));
                    }
                }
                self.dataSourceMorningFlex(dataFlexMorning);
                var dataFlexAfternoon: any[] = [];
                for (var dataModelAfternoonFlex of self.mainSettingModel.flexWorkSetting.getHDWtzAfternoon().workTimezone.lstOTTimezone) {
                    dataFlexAfternoon.push(self.toModelFlexColumnSetting(dataModelAfternoonFlex.toDto()));
                }
                self.dataSourceAfternoonFlex(dataFlexAfternoon);
            }

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
         * function convert data model of client to parent
         */
        private toModelFixedDto(dataModel: any, workTimezoneNo: number): OverTimeOfTimeZoneSetDto {
            var rounding: TimeRoundingSettingDto = {
                roundingTime: dataModel.roundingTime(),
                rounding: dataModel.rounding()
            };
            var timezone: TimeZoneRoundingDto = {
                rounding: rounding,
                start: dataModel.timezone().startTime,
                end: dataModel.timezone().endTime
            };
            var dataDTO: OverTimeOfTimeZoneSetDto = {
                workTimezoneNo: workTimezoneNo,
                restraintTimeUse: false,
                timezone: timezone,
                otFrameNo: dataModel.otFrameNo(),
                earlyOTUse: dataModel.earlyOTUse(),
                legalOTframeNo: dataModel.legalOTframeNo(),
                settlementOrder: dataModel.settlementOrder()
            };
            return dataDTO;
        }
        /**
         * function convert dto to model
         */
        private toModelFlexColumnSetting(dataDTO: OverTimeOfTimeZoneSetDto): any {
            return {
                timezone: ko.observable({ startTime: dataDTO.timezone.start, endTime: dataDTO.timezone.end }),
                rounding: ko.observable(dataDTO.timezone.rounding.rounding),
                roundingTime: ko.observable(dataDTO.timezone.rounding.roundingTime),
                otFrameNo: ko.observable(dataDTO.otFrameNo),
                earlyOTUse: ko.observable(dataDTO.earlyOTUse)
            }
        }
        
        /**
         * function convert data model of client to parent
         */
        private toModelFlexDto(dataModel: any, workTimezoneNo: number): OverTimeOfTimeZoneSetDto {
            var rounding: TimeRoundingSettingDto = {
                roundingTime: dataModel.roundingTime(),
                rounding: dataModel.rounding()
            };
            var timezone: TimeZoneRoundingDto = {
                rounding: rounding,
                start: dataModel.timezone().startTime,
                end: dataModel.timezone().endTime
            };
            var dataDTO: OverTimeOfTimeZoneSetDto = {
                workTimezoneNo: workTimezoneNo,
                restraintTimeUse: false,
                timezone: timezone,
                otFrameNo: dataModel.otFrameNo(),
                earlyOTUse: dataModel.earlyOTUse(),
                legalOTframeNo: 1,
                settlementOrder: 1
            };
            return dataDTO;
        }
        /**
         * function convert dto to model
         */
        private toModelFlowColumnSetting(dataDTO: FlOTTimezoneDto): any {
            return {
                elapsedTime: ko.observable(dataDTO.flowTimeSetting.elapsedTime),
                rounding: ko.observable(dataDTO.flowTimeSetting.rounding.rounding),
                roundingTime: ko.observable(dataDTO.flowTimeSetting.rounding.roundingTime),
                inLegalOTFrameNo: ko.observable(dataDTO.inLegalOTFrameNo),
                settlementOrder: ko.observable(dataDTO.settlementOrder)
            }
        }
        
        /**
         * function convert data model of client to parent
         */
        private toModelFlowDto(dataModel: any, worktimeNo: number): FlOTTimezoneDto {
            var rounding: TimeRoundingSettingDto = {
                roundingTime: dataModel.roundingTime(),
                rounding: dataModel.rounding()
            };
            var flowTimeSetting: FlTimeSettingDto = {
                rounding: rounding,
                elapsedTime: dataModel.elapsedTime(),
            };
            var dataDTO: FlOTTimezoneDto = {
                worktimeNo: worktimeNo,
                restrictTime: false,
                overtimeFrameNo: 1,
                flowTimeSetting: flowTimeSetting,
                inLegalOTFrameNo: dataModel.inLegalOTFrameNo(),
                settlementOrder: dataModel.settlementOrder()
            };
            return dataDTO;
        }

        //bind data to screen items
        public bindDataToScreen(data: any) {
            let self = this;
        }
        
        /**
         * init array setting column option overtime flow mode
         */
         private columnSettingOvertimeFlow(): Array<any> {
             let self = this;
             return [
                 {
                     headerText: nts.uk.resource.getText("KMK003_174"),
                     key: "elapsedTime",
                     defaultValue: ko.observable(0), 
                     width: 100, 
                     template: `<input data-bind="ntsTimeEditor: {
                        inputFormat: 'time'}" />`
                 },
                 {
                     headerText: nts.uk.resource.getText("KMK003_56"),
                     key: "roundingTime",
                     dataSource: self.settingEnum.roundingTime,
                     defaultValue: ko.observable(0),
                     width: 120,
                     template: `<div class="column-combo-box" data-bind="ntsComboBox: {
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
                     dataSource: self.settingEnum.rounding,
                     defaultValue: ko.observable(0),
                     width: 150,
                     template: `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'value',
                                    visibleItemsCount: 5,
                                    optionsText: 'localizedName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'localizedName', length: 10 }]}">
                                </div>`
                 },
                 {
                     headerText: nts.uk.resource.getText("KMK003_58"),
                     key: "otFrameNo",
                     dataSource: self.lstSelectOrderModel,
                     defaultValue: ko.observable(1),
                     width: 120,
                     template: `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'code',
                                    visibleItemsCount: 5,
                                    optionsText: 'name',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'name', length: 2 }]}">
                                </div>`
                 },
                 {
                     headerText: nts.uk.resource.getText("KMK003_186"),
                     key: "inLegalOTFrameNo",
                     dataSource: self.lstSelectOrderModel,
                     defaultValue: ko.observable(1),
                     width: 120,
                     template:  `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'code',
                                    visibleItemsCount: 5,
                                    optionsText: 'name',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'name', length: 2 }]}">
                                </div>`
                 },
                 {
                     headerText: nts.uk.resource.getText("KMK003_187"),
                     key: "settlementOrder",
                     dataSource: self.lstSelectOrderModel,
                     defaultValue: ko.observable(1),
                     width: 100,
                     template:  `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'code',
                                    visibleItemsCount: 5,
                                    optionsText: 'name',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'name', length: 2 }]}">
                                </div>`
                 }
             ];
         }
        
        /**
         * function get column setting fixed
         */
         private columnSettingFixed(): Array<any> {
            let self = this;
             var arraySettingFlex : Array<any> = self.columnSettingFlex();
             arraySettingFlex.push({
                 headerText: nts.uk.resource.getText("KMK003_186"),
                 key: "legalOTframeNo",
                 dataSource: self.lstSelectOrderModel,
                 defaultValue: ko.observable(1),
                 width: 75,
                 template: `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'code',
                                    visibleItemsCount: 5,
                                    optionsText: 'name',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'name', length: 2 }]}">
                                </div>`
             });
             arraySettingFlex.push({
                 headerText: nts.uk.resource.getText("KMK003_187"),
                 key: "settlementOrder",
                 dataSource: self.lstSelectOrderModel,
                 defaultValue: ko.observable(1),
                 width: 75,
                 template: `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'code',
                                    visibleItemsCount: 5,
                                    optionsText: 'name',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'name', length: 2 }]}">
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
                    template: `<div class= "fixtable" data-bind="ntsTimeRangeEditor: { 
                        startConstraint: 'TimeWithDayAttr', endConstraint: 'TimeWithDayAttr',
                        required: true, enable: true, inputFormat: 'time',  startTimeNameId: '#[KMK003_166]', endTimeNameId: '#[KMK003_167]'}"/>`
                },
                {
                    headerText: nts.uk.resource.getText("KMK003_56"),
                    key: "roundingTime",
                    dataSource: self.settingEnum.roundingTime,
                    defaultValue: ko.observable(0),
                    width: 120,
                    template: `<div class="column-combo-box" data-bind="ntsComboBox: {
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
                    dataSource: self.settingEnum.rounding,
                    defaultValue: ko.observable(0),
                    width: 150,
                    template: `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'value',
                                    visibleItemsCount: 5,
                                    optionsText: 'localizedName',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'localizedName', length: 10 }]}">
                                </div>`
                },
                {
                    headerText: nts.uk.resource.getText("KMK003_58"), 
                    key: "otFrameNo",
                    dataSource: self.lstSelectOrderModel,
                    defaultValue: ko.observable(1),
                    width: 80,
                    template: `<div class="column-combo-box" data-bind="ntsComboBox: {
                                    optionsValue: 'code',
                                    visibleItemsCount: 5,
                                    optionsText: 'name',
                                    editable: false,
                                    enable: true,
                                    columns: [{ prop: 'name', length: 2 }]}">
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
    export interface SettlementOrder {
        code: number;
        name: string;
    }

    class KMK003A3BindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
        }

        private getData() {
            let self = this;
            // service.findWorkTimeSetByCode()
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                .mergeRelativePath('/view/kmk/003/a3/index.xhtml').serialize();
            //get data
            let input = valueAccessor();
            var settingEnum: WorkTimeSettingEnumDto = input.enum;
            var mainSettingModel: MainSettingModel = input.mainModel;
            var isLoading:  KnockoutObservable<boolean> = input.isLoading; 

            let screenModel = new ScreenModel(settingEnum, mainSettingModel, isLoading);
            $(element).load(webserviceLocator, function() {
                ko.cleanNode($(element)[0]);
                ko.applyBindingsToDescendants(screenModel, $(element)[0]);
            });
        }

    }
    ko.bindingHandlers['ntsKMK003A3'] = new KMK003A3BindingHandler();

}
