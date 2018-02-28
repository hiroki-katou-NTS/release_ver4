module nts.uk.com.view.ccg.share.ccg {

    import ListType = kcp.share.list.ListType;
    import ComponentOption = kcp.share.list.ComponentOption;
    import TreeComponentOption = kcp.share.tree.TreeComponentOption;
    import TreeType = kcp.share.tree.TreeType;
    import SelectType = kcp.share.list.SelectType;
    import UnitModel = kcp.share.list.UnitModel;
    import EmployeeSearchDto = service.model.EmployeeSearchDto;
    import Ccg001ReturnedData = service.model.Ccg001ReturnedData;
    import GroupOption = service.model.GroupOption;
    import EmployeeRangeSelection = service.model.EmployeeRangeSelection;
    import EmployeeQueryParam = service.model.EmployeeQueryParam;
    import EmployeeDto = service.model.EmployeeDto;

    export module viewmodel {
        
        /**
        * Screen Model.
        */
        
        export class ListGroupScreenModel {
            /** Domain characteristic */
            employeeRangeSelection: EmployeeRangeSelection;

            /** Common properties */
            showEmployeeSelection: boolean; // 検索タイプ
            systemType: number; // システム区分
            showQuickSearchTab: boolean; // クイック検索
            showAdvancedSearchTab: boolean; // 詳細検索
            showBaseDate: boolean; // 基準日利用
            showClosure: boolean; // 就業締め日利用
            showAllClosure: boolean; // 全締め表示
            showPeriod: boolean; // 対象期間利用
            showPeriodYM: boolean; // 対象期間精度

            /** Required parameter */
            baseDate: KnockoutObservable<moment.Moment>;
            periodStart: KnockoutObservable<moment.Moment>;
            periodEnd: KnockoutObservable<moment.Moment>;

            /** Quick search tab options */
            showAllReferableEmployee: boolean; // 参照可能な社員すべて
            showOnlyMe: boolean; // 自分だけ
            showSameWorkplace: boolean; // 同じ職場の社員
            showSameWorkplaceAndChild: boolean; // 同じ職場とその配下の社員

            /** Advanced search properties */
            showEmployment: boolean; // 雇用条件
            showWorkplace: boolean; // 職場条件
            showClassification: boolean; // 分類条件
            showJobTitle: boolean; // 職位条件
            showWorktype: boolean; // 勤種条件
            isMultiple: boolean; // 選択モード

            isShow: KnockoutObservable<boolean>;
            isOpenStatusOfEmployeeList: KnockoutObservable<boolean>;
            isOpenEmploymentList: KnockoutObservable<boolean>;
            isOpenClassificationList: KnockoutObservable<boolean>;
            isOpenJoptitleList: KnockoutObservable<boolean>;
            isOpenWorkplaceList: KnockoutObservable<boolean>;
            isOpenWorkTypeList: KnockoutObservable<boolean>;

            // tabs
            tabs: KnockoutObservableArray<any>;
            selectedTab: KnockoutObservable<string>;

            // selected code
            selectedCodeEmployment: KnockoutObservableArray<string>;
            selectedCodeClassification: KnockoutObservableArray<string>;
            selectedCodeJobtitle: KnockoutObservableArray<string>;
            selectedCodeWorkplace: KnockoutObservableArray<string>;
            selectedCodeEmployee: KnockoutObservableArray<string>;
            selectedCodeWorkType: KnockoutObservableArray<string>;

            // params
            employments: ComponentOption;
            classifications: ComponentOption;
            jobtitles: ComponentOption;
            workplaces: TreeComponentOption;
            employeeinfo: ComponentOption;
            closureList: KnockoutObservableArray<any>;
            selectedClosure: KnockoutObservable<number>;
            
            //QueryParam
            queryParam: EmployeeQueryParam;
            referenceRange: number;
            
            //params Status Of Employee
            statusPeriodStart: KnockoutObservable<moment.Moment>;
            statusPeriodEnd: KnockoutObservable<moment.Moment>;

            incumbentDatasource: KnockoutObservableArray<any>;
            selectedIncumbent: KnockoutObservable<boolean>; // 在職区分
            
            closedDatasource: KnockoutObservableArray<any>;
            selectedClosed: KnockoutObservable<boolean>; // 休業区分
            
            leaveOfAbsenceDatasource: KnockoutObservableArray<any>;
            selectedLeave: KnockoutObservable<boolean>; // 休職区分
            
            retirementDatasource: KnockoutObservableArray<any>;
            selectedRetirement: KnockoutObservable<boolean>; // 退職区分

            // return function
            returnDataFromCcg001: (data: Ccg001ReturnedData) => void;

            // List WorkType
            listWorkType: KnockoutObservableArray<WorkType>;
            selectedWorkTypeCode: KnockoutObservableArray<string>;
            workTypeColumns: KnockoutObservableArray<any>;

            // first time show
            isFirstTime = true;
            isHeightFixed = false;
            showApplyBtn: KnockoutComputed<boolean>;
            isExpanded: KnockoutObservable<boolean>;

            /**
             * Init screen model
             */
            constructor() {
                var self = this;
                self.initQueryParam();

                // init datasource
                self.initDatasource();

                // init selected values
                self.selectedTab = ko.observable('tab-1');
                self.selectedCodeEmployment = ko.observableArray([]);
                self.selectedCodeClassification = ko.observableArray([]);
                self.selectedCodeJobtitle = ko.observableArray([]);
                self.selectedCodeWorkplace = ko.observableArray([]);
                self.selectedCodeEmployee = ko.observableArray([]);
                self.closureList = ko.observableArray([]);
                self.selectedClosure = ko.observable(null);

                // status of employment period
                self.statusPeriodStart = ko.observable(moment.utc("1900/01/01", "YYYY/MM/DD"));
                self.statusPeriodEnd = ko.observable(moment());

                // flags
                self.isExpanded = ko.observable(false);
                self.isShow = ko.observable(false);
                self.isOpenStatusOfEmployeeList = ko.observable(false);
                self.isOpenEmploymentList = ko.observable(false);
                self.isOpenClassificationList = ko.observable(false);
                self.isOpenJoptitleList = ko.observable(false);
                self.isOpenWorkplaceList = ko.observable(false);
                self.isOpenWorkTypeList = ko.observable(false);

                // search reference date & period
                self.baseDate = ko.observable(moment());
                self.periodStart = ko.observable(moment());
                self.periodEnd = ko.observable(moment());

                // status of employee
                self.selectedIncumbent = ko.observable(false);
                self.selectedClosed = ko.observable(false);
                self.selectedLeave = ko.observable(false);
                self.selectedRetirement = ko.observable(false);
                
                //WorkType
                self.listWorkType = ko.observableArray([]);
                self.selectedWorkTypeCode = ko.observableArray([]);
                
                //check show button Apply
                self.showApplyBtn = ko.computed(() => {
                    return self.baseDate() && self.periodStart() && self.periodEnd() ? true : false;
                });
            }

            /**
             * Init datasource
             */
            private initDatasource(): void {
                let self = this;
                self.tabs = ko.observableArray([
                    {
                        id: 'tab-1',
                        title: nts.uk.resource.getText("CCG001_3"),
                        content: '.tab-content-1',
                        enable: ko.observable(true),
                        visible: ko.observable(true)
                    },
                    {
                        id: 'tab-2',
                        title: nts.uk.resource.getText("CCG001_4"),
                        content: '.tab-content-2',
                        enable: ko.observable(true),
                        visible: ko.observable(true)
                    }
                ]);
                self.incumbentDatasource = ko.observableArray([
                    { code: true, name: nts.uk.resource.getText("CCG001_40") },
                    { code: false, name: nts.uk.resource.getText("CCG001_41") }
                ]);
                self.closedDatasource = ko.observableArray([
                    { code: true, name: nts.uk.resource.getText("CCG001_40") },
                    { code: false, name: nts.uk.resource.getText("CCG001_41") }
                ]);
                self.leaveOfAbsenceDatasource = ko.observableArray([
                    { code: true, name: nts.uk.resource.getText("CCG001_40") },
                    { code: false, name: nts.uk.resource.getText("CCG001_41") }
                ]);
                self.retirementDatasource = ko.observableArray([
                    { code: true, name: nts.uk.resource.getText("CCG001_40") },
                    { code: false, name: nts.uk.resource.getText("CCG001_41") }
                ]);
                // Define gridlist's columns
                self.workTypeColumns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('CCG001_60'), prop: 'workTypeCode', width: 100 },
                    { headerText: nts.uk.resource.getText('CCG001_61'), prop: 'name', width: 200 }
                ]);
            }
            
            /**
             * Set QuickSearchParam
             */
            private setQuickSearchParam(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;
                let param = self.queryParam;
                param.filterByEmployment = self.showClosure;
                param.employmentCodes = [];
                param.filterByDepartment = false;
                param.departmentCodes = [];
                param.filterByWorkplace = false;
                param.workplaceCodes = [];
                param.filterByClassification = false;
                param.classificationCodes = [];
                param.filterByJobTitle = false;
                param.jobTitleCodes = [];
                param.filterByWorktype = false;
                param.worktypeCodes = [];
                param.includeIncumbents = true;
                param.includeWorkersOnLeave = false;
                param.includeOccupancy = false;
                param.includeRetirees = false;
                param.systemType = self.systemType;
                param.sortOrderNo = 1; // 並び順NO＝1
                param.nameType = 1; // ビジネスネーム（日本語）

                // set employments code condition
                if (self.showClosure && self.selectedClosure() != ConfigEnumClosure.CLOSURE_ALL) {
                    service.getEmploymentCodeByClosureId(self.selectedClosure()).done(data => {
                        param.employmentCodes = data;
                        dfd.resolve();
                    });
                } else {
                    dfd.resolve();
                }
                return dfd.promise();
            }

            private initQueryParam(): void {
                let self = this;
                self.queryParam = <EmployeeQueryParam>{};
                self.queryParam.sortOrderNo = 1; // 並び順NO＝1
                self.queryParam.nameType = 1; // ビジネスネーム（日本語）
            }

            /**
             * update select tabs
             */
             
            public updateTabs(): Array<any> {
                let self = this;
                let arrTabs = [];
                // is quick search tab
                if (self.showQuickSearchTab) {
                    // push tab 1
                    arrTabs.push({
                        id: 'tab-1',
                        title: nts.uk.resource.getText("CCG001_22"),
                        content: '.tab-content-1',
                        enable: ko.observable(true),
                        visible: ko.observable(true)
                    });
                }
                // is advanced search tab
                if (self.showAdvancedSearchTab) {
                    // push tab 2
                    arrTabs.push({
                        id: 'tab-2',
                        title: nts.uk.resource.getText("CCG001_23"),
                        content: '.tab-content-2',
                        enable: ko.observable(true),
                        visible: ko.observable(true)
                    });
                }
                // => data res
                return arrTabs;
            }

            /**
             * get tab by update selected 
             */

            public updateSelectedTab(): string {
                var self = this;
                // res tab 1
                if (self.showQuickSearchTab) {
                    return 'tab-1';
                }
                // res tab 2
                if (self.showAdvancedSearchTab) {
                    return 'tab-2';
                }
                // res none tab
                return '';
            }

            /**
             * init next tab
             */
            public initNextTabFeature() {
                var self = this;
                // Auto next tab when press tab key.
                $('#tab-2').find('#StatusOfEmployeeList').find('.ui-accordion-header').on('click', function() {
                    self.isOpenStatusOfEmployeeList(!self.isOpenStatusOfEmployeeList());
                });
                $('#tab-2').find('#EmploymentList').find('.ui-accordion-header').on('click', function() {
                    self.isOpenEmploymentList(!self.isOpenEmploymentList());
                });
                $('#tab-2').find('#ClassificationList').find('.ui-accordion-header').on('click', function() {
                    self.isOpenClassificationList(!self.isOpenClassificationList());
                });
                $('#tab-2').find('#JoptitleList').find('.ui-accordion-header').on('click', function() {
                    self.isOpenJoptitleList(!self.isOpenJoptitleList());
                });
                $('#tab-2').find('#WorkplaceList').find('.ui-accordion-header').on('click', function() {
                    self.isOpenWorkplaceList(!self.isOpenWorkplaceList());
                });
                $('#tab-2').find('#WorkTypeList').find('.ui-accordion-header').on('click', function() {
                    self.isOpenWorkTypeList(!self.isOpenWorkTypeList());
                });
                $("[tabindex='10']").on('keydown', function(e) {
                    if (e.which == 9 && self.showAdvancedSearchTab) {
                        self.selectedTab('tab-2');
                        if (!self.isOpenStatusOfEmployeeList()) {
                            $('#tab-2').find('#StatusOfEmployeeList').find('.ui-accordion-header').click();
                        }
                        $("[tabindex='11']").on('keydown', function(e) {
                            if (e.which == 9) {
                                if (!self.isOpenEmploymentList()) {
                                    $('#tab-2').find('#EmploymentList').find('.ui-accordion-header').click();
                                }
                            }
                            $("[tabindex='12']").on('keydown', function(e) {
                                if (e.which == 9) {
                                    if (!self.isOpenClassificationList()) {
                                        $('#tab-2').find('#ClassificationList').find('.ui-accordion-header').click();
                                    }
                                }
                                $("[tabindex='13']").on('keydown', function(e) {
                                    if (e.which == 9) {
                                        if (!self.isOpenJoptitleList()) {
                                            $('#tab-2').find('#JoptitleList').find('.ui-accordion-header').click();
                                        }
                                    }
                                    $("[tabindex='14']").on('keydown', function(e) {
                                        if (e.which == 9) {
                                            if (!self.isOpenWorkplaceList()) {
                                                $('#tab-2').find('#WorkplaceList').find('.ui-accordion-header').click();
                                            }
                                        }
                                        $("[tabindex='15']").on('keydown', function(e) {
                                            if (e.which == 9) {
                                                if (!self.isOpenWorkTypeList()) {
                                                    $('#tab-2').find('#WorkTypeList').find('.ui-accordion-header').click();
                                                }
                                            }
                                        });     
                                    });
                                });
                            });
                        });
                    }
                });
                $("[tabindex='6']").on('keydown', function(e) {
                    if (e.which == 9 && self.selectedTab() == 'tab-2' && !$(e.target).parents("[tabindex='6']")[0]) {
                        self.selectedTab('tab-1');
                    }
                });
                
            }

            
            /**
             * Init component.
             */
            
            public init($input: JQuery, data: GroupOption): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;

                // set component properties
                self.setProperties(data);

                // start component
                nts.uk.ui.block.invisible(); // block ui
                self.startComponent().done(() => {
                    // set advanced search tab flag
                    self.showAdvancedSearchTab = data.showAdvancedSearchTab &&
                        (self.referenceRange != ConfigEnumReferenceRange.ONLY_MYSELF);

                    // Initial tab panel
                    self.tabs(self.updateTabs());
                    self.selectedTab(self.updateSelectedTab());

                    // init view
                    let webserviceLocator = nts.uk.request.location.siteRoot
                        .mergeRelativePath(nts.uk.request.WEB_APP_NAME["com"] + '/')
                        .mergeRelativePath('/view/ccg/share/index.xhtml').serialize();
                    $input.load(webserviceLocator, function() {
                        ko.cleanNode($input[0]);
                        ko.applyBindings(self, $input[0]);
                        // Set tabindex
                        self.initNextTabFeature();
                        let tabindex = $input.attr('tabindex');
                        $input.attr('tabindex', -1);
                        $input.find('.btn_showhide').attr('tabindex', tabindex);

                        // init ccg show/hide event
                        self.initCcgEvent();
                        // set component height
                        self.setComponentHeight();

                        nts.uk.ui.block.clear(); // clear block UI

                        _.defer(() => self.applyDataSearch().always(() => {
                            // Set acquired base date to status period end date
                            self.statusPeriodEnd(moment.utc(self.queryParam.baseDate, CcgDateFormat.DEFAULT_FORMAT));
                            dfd.resolve(); 
                        }));
                    });
                });

                return dfd.promise();
            }

            /**
             * Start component
             */
            private startComponent(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;
                $.when(service.getRefRangeBySysType(self.systemType),
                    self.loadClosure()
                ).done((refRange, noValue) => {
                    self.referenceRange = refRange;
                    dfd.resolve();
                }).fail(err => nts.uk.ui.dialog.alertError(err));

                return dfd.promise();
            }

            /**
             * Set advanced search param
             */
            private setAdvancedSearchParam(): void {
                let self = this;
                let param = this.queryParam;
                param.referenceRange = ConfigEnumReferenceRange.ALL_EMPLOYEE;

                // filter param
                param.filterByEmployment = self.showEmployment;
                // not covered param.filterByDepartment = options.showDepartment;
                param.filterByWorkplace = self.showWorkplace;
                param.filterByClassification = self.showClassification;
                param.filterByJobTitle = self.showJobTitle;
                // only consider show worktype if sytemType = employment
                param.filterByWorktype = self.systemType == ConfigEnumSystemType.EMPLOYMENT ? self.showWorktype : false;

                // filter status of employment
                param.includeIncumbents = self.selectedIncumbent();
                param.includeWorkersOnLeave = self.selectedLeave();
                param.includeOccupancy = self.selectedClosed();
                param.includeRetirees = self.selectedRetirement();
                param.retireStart = self.statusPeriodStart().format(CcgDateFormat.DEFAULT_FORMAT);
                param.retireEnd = self.statusPeriodEnd().format(CcgDateFormat.DEFAULT_FORMAT);
                param.systemType = self.systemType;

                self.queryParam.employmentCodes = self.showEmployment ? self.selectedCodeEmployment() : [];
                self.queryParam.classificationCodes = self.showClassification ? self.selectedCodeClassification() : [];
                self.queryParam.jobTitleCodes = self.showJobTitle ? self.selectedCodeJobtitle() : [];
                self.queryParam.workplaceCodes = self.showWorkplace ? self.selectedCodeWorkplace() : [];
                // only consider list worktype if sytemType = employment
                self.queryParam.worktypeCodes = self.systemType ==
                    ConfigEnumSystemType.EMPLOYMENT && self.showWorktype ? self.selectedWorkTypeCode() : [];
            }

            /**
             * Set component properties
             */
            private setProperties(options: GroupOption): void {
                let self = this;

                /** Common properties */
                self.showEmployeeSelection = options.showEmployeeSelection;
                self.systemType = options.systemType;
                // always show quick search if advanced search is hidden
                self.showQuickSearchTab = options.showAdvancedSearchTab ? options.showQuickSearchTab : true;
                self.showBaseDate = !options.showBaseDate && !options.showPeriod ? true : options.showBaseDate;
                self.showClosure = options.showClosure;
                self.showAllClosure = options.showAllClosure;
                self.showPeriod = options.showPeriod;
                self.showPeriodYM = options.periodFormatYM;

                /** Required parameter */
                self.baseDate(moment.utc(options.baseDate));
                self.periodStart(options.periodFormatYM ?
                    moment.utc(options.periodStartDate).startOf('month') : moment.utc(options.periodStartDate).startOf('day'));
                self.periodEnd(options.periodFormatYM ?
                    moment.utc(options.periodEndDate).startOf('month') : moment.utc(options.periodEndDate).startOf('day'));
                self.selectedIncumbent(options.inService);
                self.selectedLeave(options.leaveOfAbsence);
                self.selectedClosed(options.closed);
                self.selectedRetirement(options.retirement);

                /** Quick search tab options */
                self.showAllReferableEmployee = options.showAllReferableEmployee;
                self.showOnlyMe = options.showOnlyMe;
                self.showSameWorkplace = options.showSameWorkplace;
                self.showSameWorkplaceAndChild = options.showSameWorkplaceAndChild;

                /** Advanced search properties */
                self.showEmployment = options.showEmployment;
                self.showWorkplace = options.showWorkplace;
                self.showClassification = options.showClassification;
                self.showJobTitle = options.showJobTitle;
                self.showWorktype = options.showWorktype;
                self.isMultiple = options.isMutipleCheck;

                // return data function
                self.returnDataFromCcg001 = options.returnDataFromCcg001;
            }

            /**
             * Set component height
             */
            private setComponentHeight(): void {
                // set component height
                const headerHeight = $('#header').outerHeight();
                const functionAreaHeight = $('#functions-area').length > 0 ? $('#functions-area').outerHeight() : 0;
                const componentHeight = window.innerHeight - headerHeight - functionAreaHeight - 15;
                $('#component-ccg001').outerHeight(componentHeight);
                $('#hor-scroll-button-hide').outerHeight(componentHeight);
                $('#ccg001-btn-search-drawer').outerHeight(componentHeight / 2);

                // set tab panel height.
                const tabpanelHeight = componentHeight - $('#ccg001-header').outerHeight(true) - 10;
                const tabpanelNavHeight = 85;
                $('.ccg-tabpanel.pull-left').outerHeight(tabpanelHeight);
                $('.ccg-tabpanel>#tab-1').css('height', tabpanelHeight - tabpanelNavHeight);
                $('.ccg-tabpanel>#tab-2').css('height', tabpanelHeight - tabpanelNavHeight);
            }

            /**
             * Load ListClosure 
             */
            private loadClosure(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
                if (self.showClosure) {
                    service.getClosuresByBaseDate(self.baseDate().format(CcgDateFormat.DEFAULT_FORMAT)).done(data => {
                        if (self.showAllClosure) {
                            data.unshift({
                                closureId: ConfigEnumClosure.CLOSURE_ALL,
                                closureName: ConfigEnumClosure.CLOSURE_ALL_NAME
                            });
                        }
                        self.closureList(data);
                        self.getSelectedClosure().done(selected => {
                            self.selectedClosure(selected);
                            self.selectedClosure.subscribe(vl => self.applyDataSearch());
                            dfd.resolve();
                        });
                    });
                } else {
                    dfd.resolve();
                }
                return dfd.promise();
            }

            /**
             * Get selected closure id
             */
            private getSelectedClosure(): JQueryPromise<number> {
                let dfd = $.Deferred<number>();
                let self = this;
                service.getEmployeeRangeSelection().done((data: EmployeeRangeSelection) => {
                    if (data) {
                        self.employeeRangeSelection = data;
                        switch (self.systemType) {
                            case ConfigEnumSystemType.PERSONAL_INFORMATION:
                                dfd.resolve(data.personalInfo.selectedClosureId);
                                break;
                            case ConfigEnumSystemType.EMPLOYMENT:
                                dfd.resolve(data.employmentInfo.selectedClosureId);
                                break;
                            case ConfigEnumSystemType.SALARY:
                                dfd.resolve(data.salaryInfo.selectedClosureId);
                                break;
                            case ConfigEnumSystemType.HUMAN_RESOURCES:
                                dfd.resolve(data.humanResourceInfo.selectedClosureId);
                                break;
                            default: break; // systemType not found
                        }
                    } else {
                        service.getCurrentHistoryItem().done(item => {
                            service.getClosureTiedByEmployment(item.employmentCode).done(id => dfd.resolve(id));
                        });
                    }
                });
                return dfd.promise();
            }

            /**
             * Initial ccg event
             */
            private initCcgEvent(): void {
                let self = this;
                $('#component-ccg001').parent('#component-ccg001').on('hover', e => {
                    console.log('hovered');
                });
                $(window).on('click', function(e) {
                    // Check is click to inside component.
                    if (e.target.id == "component-ccg001" || $(e.target).parents("#component-ccg001")[0]) {
                        return;
                    }
                    // click when block ui
                    if ($(e.target).hasClass('ui-widget-overlay ui-front')) {
                        return;
                    }
                    if ($(e.target).hasClass('blockUI blockOverlay')) {
                        return;
                    }
                    // check is click to errors notifier
                    if (e.target.id == 'func-notifier-errors') {
                        return;
                    }
                    // Check is click to dialog.
                    if ($(e.target).parents("[role='dialog']")[0]) {
                        return;
                    }
                    // Check is click to ignite combo-box.
                    if ($(e.target).parents().hasClass('ui-igcombo-dropdown')) {
                        return;
                    }
                    if (e.target.id == "hor-scroll-button-hide" || $(e.target).parents("#hor-scroll-button-hide")[0]) {
                        return;
                    }
                    self.hideComponent();
                });
            }
            
            /**
             * Hide component
             */
            public hideComponent(): void {
                let self = this;
                if (self.isShow()) {
                    $('#component-ccg001').toggle("slide");
                    self.isShow(false);
                }
            }

            /**
             * Show component
             */
            public showComponent(): void {
                let self = this;
                if (self.isShow()) {
                    return;
                }
                let componentElement = document.getElementById('component-ccg001');
                if (componentElement.style.visibility == 'hidden') {
                    componentElement.style.removeProperty('visibility');
                    componentElement.style.display = 'none';
                }
                $('#component-ccg001').toggle("slide");
                if (self.showAdvancedSearchTab && self.showEmployeeSelection && self.isFirstTime) {
                    self.loadKcp005();
                }
                self.isShow(true);
            }

            /**
             * Load component KCP005
             */
            private loadKcp005(): void {
                let self = this;

                // set KCP005 rows
                const tabContentHeight = parseInt(document.querySelector('.ccg-tabpanel>#tab-2').style.height);
                const kcp005HeaderHeight = 70;
                let rows = (tabContentHeight - kcp005HeaderHeight) / 24;

                // set KCP005 options
                self.employeeinfo = {
                    isShowAlreadySet: false,
                    isMultiSelect: self.isMultiple,
                    isMultipleUse: true,
                    listType: ListType.EMPLOYEE,
                    employeeInputList: ko.observableArray([]),
                    selectType: SelectType.SELECT_BY_SELECTED_CODE,
                    selectedCode: self.selectedCodeEmployee,
                    isDialog: true,
                    isShowNoSelectRow: false,
                    maxRows: rows
                }

                // Show KCP005
                $('#employeeinfo').ntsListComponent(self.employeeinfo).done(() => self.fixComponentWidth());

                // update flag isFirstTime
                if (self.isFirstTime) {
                    self.isFirstTime = false;
                }
            }

            /**
             * Fix component width according to screen width
             */
            private fixComponentWidth(): void {
                let self = this;
                // update tab 2 width
                let totalWidth = 0;
                $('#ccg001-tab-content-2').children('div.pull-left.height-maximum').each((i, e) => totalWidth += $(e).outerWidth(true));
                $('#ccg001-tab-content-2').outerWidth(totalWidth);

                // Fix component width if screen width is smaller than component
                const componentWidth = window.innerWidth - $('#hor-scroll-button-hide').offset().left;
                if (componentWidth <= $('#ccg001-tab-content-2').outerWidth()) {
                    const margin = 30;
                    // fix width and show scrollbar
                    $('.tab-content-2.height-maximum').outerWidth(componentWidth - margin);
                    $('.tab-content-2.height-maximum').css('overflow-x', 'auto');

                    // fix height
                    if (!self.isHeightFixed) {
                        const fixedTabHeight = parseInt(document.querySelector('.ccg-tabpanel>#tab-2').style.height) + 15;
                        $('.ccg-tabpanel>#tab-2').css('height', fixedTabHeight);
                        self.isHeightFixed = true;
                    }
                }
            }

            /**
             * Check base date and period whether is future or not
             */
            private isNotFutureDate(acquiredBaseDate: string): boolean {
                let self = this;
                if (self.isFutureDate(moment.utc(acquiredBaseDate))) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_853" });
                    return false;
                }
                if (self.isFutureDate(self.periodEnd())) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_860" });
                    return false;
                }
                return true;
            }

            /**
             * Check future date
             */
            private isFutureDate(date: moment.Moment): boolean {
                return date.isAfter(moment());
            }

            /**
             * function click by apply data search employee (init tab 2)
             * get base date
             */
            applyDataSearch(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;

                // validate input base date
                if (self.isInvalidBaseDate()) {
                    dfd.reject();
                    return dfd.promise();
                }

                nts.uk.ui.block.invisible(); // block ui

                // Check future reference permission
                $.when(self.acquireBaseDate(), self.getFuturePermit())
                    .done((acquiredDate, permit) => {
                        if (!permit || self.isNotFutureDate(acquiredDate)) {
                            // has permission or acquiredDate is not future
                            self.queryParam.baseDate = acquiredDate;
                            if (self.showAdvancedSearchTab) {
                                self.reloadAdvanceSearchTab().done(() => nts.uk.ui.block.clear()); // clear block UI
                            } else {
                                nts.uk.ui.block.clear(); // clear block UI
                            }
                            dfd.resolve();
                        } else {
                            // no permission and acquiredDate is future
                            dfd.reject();
                            nts.uk.ui.block.clear(); // clear block UI
                        }
                    }).fail(err => nts.uk.ui.dialog.alertError(err));

                return dfd.promise();
            }

            /**
             * Reload advanced search tab
             */
            private reloadAdvanceSearchTab(): JQueryPromise<void> {
                let dfd = $.Deferred<void>();
                let self = this;
                // set advanced search param
                self.queryParam.periodStart = self.periodStart().format(CcgDateFormat.DEFAULT_FORMAT);
                self.queryParam.periodEnd = self.periodEnd().format(CcgDateFormat.DEFAULT_FORMAT);
                self.queryParam.retireStart = self.statusPeriodStart().format(CcgDateFormat.DEFAULT_FORMAT);
                self.queryParam.retireEnd = self.statusPeriodEnd().format(CcgDateFormat.DEFAULT_FORMAT);

                // reload advanced search tab.
                $.when(service.searchWorkplaceOfEmployee(moment.utc(self.queryParam.baseDate, CcgDateFormat.DEFAULT_FORMAT).toDate()),
                    service.searchAllWorkType())
                    .done((selectedCodes, workTypeList: Array<WorkType>) => {
                        self.selectedCodeWorkplace(selectedCodes);
                        self.listWorkType(workTypeList);
                        self.selectedWorkTypeCode(_.map(workTypeList, vl => vl.workTypeCode));

                        self.reloadDataSearch();

                        if (self.showEmployment) {
                            $('#employmentList').ntsListComponent(self.employments);
                        }
                        if (self.showClassification) {
                            $('#classificationList').ntsListComponent(self.classifications);
                        }
                        if (self.showJobTitle) {
                            $('#jobtitleList').ntsListComponent(self.jobtitles);
                        }
                        if (self.showWorkplace) {
                            $('#workplaceList').ntsTreeComponent(self.workplaces);
                        }
                        dfd.resolve();

                    });
                return dfd.promise();
            }

            /**
             * function click by button detail work place (open dialog)
             */
            detailWorkplace(): void {
                let self = this;
                let inputCDL008 = {
                    baseDate: moment.utc(self.queryParam.baseDate, 'YYYY-MM-DD').toDate(),
                    isMultiple: true,
                    selectedCodes: self.selectedCodeWorkplace()
                };
                nts.uk.ui.windows.setShared('inputCDL008', inputCDL008);
                nts.uk.ui.windows.sub.modal('com',"/view/cdl/008/a/index.xhtml").onClosed(() => {
                    if (nts.uk.ui.windows.getShared('CDL008Cancel')) {
                        return;
                    }
                    // reload KCP004
                    self.selectedCodeWorkplace(nts.uk.ui.windows.getShared('outputCDL008'));
                    $('#workplaceList').ntsTreeComponent(self.workplaces);
                });
            }

            /**
             * Expand KCP004
             */
            public expand(): void {
                let self = this;
                $('#workplaceList').fullView();
                const KCP004Width = $('#multiple-tree-grid_scroll').outerWidth();
                const KCPMargin = 20;
                const expandedWidth = KCP004Width + KCPMargin;

                // update accordion width
                $('.accordion').width(expandedWidth);

                // fix component width
                self.fixComponentWidth();

                _.defer(() => {
                    const btnCollapseMargin = KCP004Width - $('#btnDetailWorkplace').outerWidth() - 110;

                    // update flag
                    self.isExpanded(true);

                    // set button collapse margin
                    $('#ccg-001-btn-collapse').css('margin-left', btnCollapseMargin);
                });
            }

            /**
             * Collapse KCP004
             */
            public collapse(): void {
                let self = this;
                $('#workplaceList').scrollView();

                // update accordion width
                $('.accordion').width(380);

                // fix component width
                self.fixComponentWidth();

                // update flag
                _.defer(() => self.isExpanded(false));
            }

            /**
             * function click by button search employee
             */
            extractSelectedEmployees(): void {
                var self = this;
                if (nts.uk.util.isNullOrEmpty(self.getSelectedCodeEmployee())) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_758" });
                    return;
                }
                nts.uk.ui.block.invisible(); // block ui
                service.getOfSelectedEmployee(
                    moment.utc(self.queryParam.baseDate, CcgDateFormat.DEFAULT_FORMAT).toDate(), self.getSelectedCodeEmployee())
                    .done(selectedEmps => {
                        self.returnDataFromCcg001(self.combineData(selectedEmps));
                        // Hide component.
                        self.hideComponent();
                        nts.uk.ui.block.clear(); // clear block UI
                    });
            }

            /**
             * clear validate client
             */
            clearValiate() {
                $('#inp_baseDate').ntsError('clear');
            }

            /**
             * Validate base date
             */
            isInvalidBaseDate(): boolean {
                let self = this;
                $("#inp_baseDate").ntsEditor("validate");
                $("#inp-period-startYMD").ntsEditor("validate");
                $("#inp-period-endYMD").ntsEditor("validate");

                if ($('#inp_baseDate').ntsError('hasError') ||
                    $('#inp-period-startYMD').ntsError('hasError') ||
                    $('#inp-period-endYMD').ntsError('hasError')) {
                    return true;
                }

                if (self.showPeriod && self.showBaseDate && !self.isBaseDateInTargetPeriod()) {
                    return true;
                }
                return false;
            }

            // validate input
            private isStatusEmployeePeriodInvalid(): boolean {
                let self = this;
                $("#ccg001-partg-start").ntsEditor("validate");
                $("#ccg001-partg-end").ntsEditor("validate");
                return $("#ccg001-partg-start").ntsError('hasError') || $("#ccg001-partg-start").ntsError('hasError');
            }

            /**
             * function click by button employee login
             */
            getEmployeeLogin(): void {
                var self = this;
                if (self.isInvalidBaseDate()) {
                    return;
                }
                nts.uk.ui.block.invisible(); // block ui
                service.searchEmployeeByLogin(self.baseDate().toDate()).done(data => {
                    self.returnDataFromCcg001(self.combineData(data));
                    self.hideComponent();
                    nts.uk.ui.block.clear(); // clear block UI
                }).fail(function(error) {
                    nts.uk.ui.dialog.alertError(error);
                });
            }

            /**
             * Combine return data
             */
            private combineData(listEmployee: Array<EmployeeSearchDto>): Ccg001ReturnedData {
                let self = this;
                let dto = <Ccg001ReturnedData>{};
                dto.baseDate = moment.utc(self.queryParam.baseDate, CcgDateFormat.DEFAULT_FORMAT).toISOString();
                dto.closureId = self.showClosure ? self.selectedClosure() : undefined;
                dto.periodStart = moment.utc(self.queryParam.periodStart).toISOString();
                dto.periodEnd = moment.utc(self.queryParam.periodEnd).toISOString();
                dto.listEmployee = listEmployee;
                return dto;
            }

            /**
             * Acquire base date
             */
            public acquireBaseDate(): JQueryPromise<string> { // format: YYYY-MM-DD
                let dfd = $.Deferred<String>();
                let self = this;
                if (self.showBaseDate) {
                    dfd.resolve(self.baseDate().format(CcgDateFormat.DEFAULT_FORMAT));
                } else {
                    if (self.showPeriodYM) { // Period accuracy is YM 
                        service.calculatePeriod(self.selectedClosure(), parseInt(self.periodEnd().format('YYYYMM')))
                            .done(date => {
                                return dfd.resolve(date[0]);
                            });
                    } else { // Period accuracy is YMD
                        dfd.resolve(self.periodEnd().format(CcgDateFormat.DEFAULT_FORMAT));
                    }
                }
                return dfd.promise();
            }

            /**
             * Get future reference permission
             */
            public getFuturePermit(): JQueryPromise<boolean> {
                let self = this;
                switch (self.systemType) {
                    case ConfigEnumSystemType.PERSONAL_INFORMATION:
                        return service.getPersonalRoleFuturePermit();
                    case ConfigEnumSystemType.EMPLOYMENT:
                        return service.getEmploymentRoleFuturePermit();
                    default: 
                        let dfd = $.Deferred<boolean>();
                        dfd.reject();
                        return dfd.promise();// systemType not found
                }
            }

            /**
             * Validate basedate & target period
             */
            public isBaseDateInTargetPeriod(): boolean {
                let self = this;
                let baseDate = self.baseDate();
                if(self.showPeriodYM){
                    baseDate = moment.utc((self.baseDate()).format("YYYY/MM"), "YYYY/MM"); 
                } 
                
                if (baseDate.isBefore(self.periodStart()) || baseDate.isAfter(self.periodEnd())) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_765" });
                    return false;
                }
                return true;
            }

            /**
             * function click apply search employee
             */
            advancedSearchEmployee(): void {
                let self = this;
                if (self.isInvalidBaseDate() || self.isStatusEmployeePeriodInvalid()) {
                    return;
                }
                // set param
                self.setAdvancedSearchParam();

                nts.uk.ui.block.invisible(); // block ui
                if (self.showClosure) { // save EmployeeRangeSelection if show closure
                    let empRangeSelection = self.employeeRangeSelection ?
                        self.employeeRangeSelection : new EmployeeRangeSelection();
                    switch (self.systemType) {
                        case ConfigEnumSystemType.PERSONAL_INFORMATION:
                            empRangeSelection.personalInfo.selectedClosureId = self.selectedClosure();
                            break;
                        case ConfigEnumSystemType.EMPLOYMENT:
                            empRangeSelection.employmentInfo.selectedClosureId = self.selectedClosure();
                            break;
                        case ConfigEnumSystemType.SALARY:
                            empRangeSelection.salaryInfo.selectedClosureId = self.selectedClosure();
                            break;
                        case ConfigEnumSystemType.HUMAN_RESOURCES:
                            empRangeSelection.humanResourceInfo.selectedClosureId = self.selectedClosure();
                            break;
                        default: break; // systemType not found
                    }
                    service.saveEmployeeRangeSelection(empRangeSelection).done(() => {
                        self.findAndReturnListEmployee(true);
                    });
                } else {
                    self.findAndReturnListEmployee(true);
                }
            }

            /**
             * function get selected employee to
             */
            public getSelectedCodeEmployee(): string[]{
                var self = this;
                if(self.isMultiple){
                    return self.selectedCodeEmployee();    
                }
                var employeeIds: string[] = [];
                employeeIds.push(self.selectedCodeEmployee() + "");
                return employeeIds;
            }        
            
            /**
             * function convert dto to model init data 
             */
            public toUnitModelList(dataList: EmployeeSearchDto[]): Array<UnitModel> {
                var dataRes: UnitModel[] = [];

                _.forEach(dataList, item => {
                    dataRes.push({
                        code: item.employeeId,
                        name: item.employeeName
                    });
                });
                return dataRes;
            }
            
            /**
             * search all Employee
             */
            public searchAllListEmployee(): void {
                var self = this;
                self.queryParam.referenceRange = ConfigEnumReferenceRange.ALL_EMPLOYEE;
                self.quickSearchEmployee();
            }
            
            /**
             * search Employee of Departmant_only
             */
            public searchEmployeeOfDepOnly(): void {
                var self = this;
                self.queryParam.referenceRange = ConfigEnumReferenceRange.DEPARTMENT_ONLY;
                self.quickSearchEmployee();
            }
            
            /**
             * search Employee of Departmant and child
             */
            public searchEmployeeOfDepAndChild(): void {
                var self = this;
                self.queryParam.referenceRange = ConfigEnumReferenceRange.DEPARTMENT_AND_CHILD;
                self.quickSearchEmployee();
            }

            /**
             * Quick search employee
             */
            private quickSearchEmployee(): void {
                let self = this;
                if (self.isInvalidBaseDate()) {
                    return;
                }
                nts.uk.ui.block.invisible(); // block ui
                self.setQuickSearchParam().done(() => {
                    self.findAndReturnListEmployee(false);
                });
            }
            /**
             * Find and return list employee for caller screen.
             */
            public findAndReturnListEmployee(isAdvancedSearch: boolean): void {
                let self = this;
                service.findRegulationInfoEmployee(self.queryParam).done(data => {
                    // Data not found
                    if (nts.uk.util.isNullOrEmpty(data)) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_317" });
                        nts.uk.ui.block.clear(); // clear block UI
                        return;
                    }

                    // Data found
                    if (isAdvancedSearch && self.showEmployeeSelection) {
                        // Load list employee to KCP005
                        self.employeeinfo.employeeInputList(self.toUnitModelList(data));

                        // Reset selected employees on KCP005
                        self.selectedCodeEmployee([]);
                    } else {
                        self.returnDataFromCcg001(self.combineData(data));
                        // Hide component.
                        self.hideComponent();
                    }
                    nts.uk.ui.block.clear(); // clear block UI
                });
            }
            

            /**
             * function reload page (init tab 2)
             */
            public reloadDataSearch(): void {
                var self = this;
                if (self.showAdvancedSearchTab) {
                    self.employments = {
                        isShowAlreadySet: false,
                        isMultiSelect: true,
                        isMultipleUse: true,
                        selectType: SelectType.SELECT_ALL,
                        listType: ListType.EMPLOYMENT,
                        selectedCode: self.selectedCodeEmployment,
                        isDialog: true,
                        isShowNoSelectRow: false,
                        maxRows: ConfigCCGKCP.MAX_ROWS_EMPLOYMENT,
                        selectedClosureId: self.showClosure ? self.selectedClosure : undefined
                    };

                    self.classifications = {
                        isShowAlreadySet: false,
                        isMultiSelect: true,
                        isMultipleUse: true,
                        listType: ListType.Classification,
                        selectType: SelectType.SELECT_ALL,
                        selectedCode: self.selectedCodeClassification,
                        isDialog: true,
                        isShowNoSelectRow: false,
                        maxRows: ConfigCCGKCP.MAX_ROWS_CLASSIFICATION
                    }

                    self.jobtitles = {
                        isShowAlreadySet: false,
                        isMultiSelect: true,
                        isMultipleUse: true,
                        listType: ListType.JOB_TITLE,
                        selectType: SelectType.SELECT_ALL,
                        selectedCode: self.selectedCodeJobtitle,
                        isDialog: true,
                        baseDate: ko.observable(moment.utc(self.queryParam.baseDate, CcgDateFormat.DEFAULT_FORMAT).toDate()),
                        isShowNoSelectRow: false,
                        maxRows: ConfigCCGKCP.MAX_ROWS_JOBTITLE
                    }

                    self.workplaces = {
                        isShowAlreadySet: false,
                        systemType: self.systemType,
                        isMultipleUse: true,
                        isMultiSelect: true,
                        treeType: TreeType.WORK_PLACE,
                        selectType: SelectType.SELECT_BY_SELECTED_CODE,
                        isShowSelectButton: true,
                        selectedWorkplaceId: self.selectedCodeWorkplace,
                        baseDate: ko.observable(moment.utc(self.queryParam.baseDate, CcgDateFormat.DEFAULT_FORMAT).toDate()),
                        maxRows: ConfigCCGKCP.MAX_ROWS_WORKPLACE,
                        isDialog: true
                    }

                }
            }
        }
        
        export class ConfigCCGKCP{
            static MAX_ROWS_EMPLOYMENT = 10;
            static MAX_ROWS_CLASSIFICATION = 10;
            static MAX_ROWS_JOBTITLE = 10;
            static MAX_ROWS_WORKPLACE = 10;
        }
        
        export class ConfigEnumSystemType{
            static PERSONAL_INFORMATION = 1;
            static EMPLOYMENT = 2;
            static SALARY = 3;
            static HUMAN_RESOURCES = 4;
            static ADMINISTRATOR = 5;
        }

        export class CcgDateFormat {
            static DEFAULT_FORMAT = 'YYYY-MM-DD';
        }
        
         export class ConfigEnumClosure{
            static CLOSURE_ALL = 0;
            static CLOSURE_ALL_NAME = nts.uk.resource.getText("CCG001_64");
        }
        
        export class ConfigEnumReferenceRange{
            static ALL_EMPLOYEE = 0;
            static DEPARTMENT_AND_CHILD = 1;
            static DEPARTMENT_ONLY = 2;
            static ONLY_MYSELF = 3;
        }

        interface WorkType {
            abbreviationName: string;
            companyId: string;
            displayAtr: number;
            memo: string;
            name: string;
            sortOrder: number;
            symbolicName: string;
            workTypeCode: string;
        }
    }
}
/**
 * Defined Jquery interface.
 */
interface JQuery {

   ntsGroupComponent(option: nts.uk.com.view.ccg.share.ccg.service.model.GroupOption): JQueryPromise<void>;
}

(function($: any) {
    $.fn.ntsGroupComponent = function(option: nts.uk.com.view.ccg.share.ccg.service.model.GroupOption): JQueryPromise<void> {

        // Return.
        return new nts.uk.com.view.ccg.share.ccg.viewmodel.ListGroupScreenModel().init(this, option);
    }
} (jQuery));