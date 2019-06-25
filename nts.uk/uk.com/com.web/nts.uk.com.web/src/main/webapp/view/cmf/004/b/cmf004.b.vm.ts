module nts.uk.com.view.cmf004.b.viewmodel {
    import getText = nts.uk.resource.getText;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import dialog = nts.uk.ui.dialog;
    import block = nts.uk.ui.block;
    export class ScreenModel {
        stepList: Array<NtsWizardStep> = [
            { content: '.step-1' },
            { content: '.step-2' },
            { content: '.step-3' },
            { content: '.step-4' },
            { content: '.step-5' }
        ];
        stepSelected: KnockoutObservable<NtsWizardStep> = ko.observable({ id: 'step-1', content: '.step-1' });
        activeStep: KnockoutObservable<number> = ko.observable(0);
        selectedId: KnockoutObservable<number> = ko.observable(1);
        options: KnockoutObservableArray<any> = ko.observableArray([
            { value: 1, text: getText('CMF004_30') },
            { value: 2, text: getText('CMF004_32') }
        ]);
        //ScreenB
        startDateString: KnockoutObservable<string> = ko.observable("");
        endDateString: KnockoutObservable<string> = ko.observable("");
        dataRecoverySelection: KnockoutObservable<DataRecoverySelection> = ko.observable(new DataRecoverySelection(1, 0, {}, [], null));
        //upload file
        fileId: KnockoutObservable<string> = ko.observable(null);
        fileName: KnockoutObservable<string> = ko.observable(null);
        //Screen E, F, G, H
        dataRecoveryProcessId: KnockoutObservable<string> = ko.observable(null);
        recoverySourceFile: KnockoutObservable<string> = ko.observable("");
        recoverySourceCode: KnockoutObservable<string> = ko.observable("");
        recoverySourceName: KnockoutObservable<string> = ko.observable("");
        supplementaryExplanation: KnockoutObservable<string> = ko.observable("");
        saveForm: KnockoutObservable<string> = ko.observable("");
        //Screen E
        recoveryMethodOptions: KnockoutObservableArray<any> = ko.observableArray([
            { value: RecoveryMethod.RESTORE_ALL, text: getText('CMF004_92') },
            { value: RecoveryMethod.SELECTED_RANGE, text: getText('CMF004_93') }
        ]);
        dataContentConfirm: KnockoutObservable<DataContentConfirm> = ko.observable(new DataContentConfirm([], 0));
        //Screen F
        changeDataRecoveryPeriod: KnockoutObservable<ChangeDataRecoveryPeriod> = ko.observable(new ChangeDataRecoveryPeriod([]));
        selectedEmployee: KnockoutObservableArray<EmployeeSearchDto> = ko.observableArray([]);
        //KCP005
        kcp005ComponentOptionScreenG: any;
        selectedEmployeeCodeScreenG: KnockoutObservableArray<string> = ko.observableArray([]);
        employeeListScreenG: KnockoutObservableArray<UnitModel> = ko.observableArray([]);
        //Screen H
        buton_I_enable: KnockoutObservable<boolean> = ko.observable(true);
        recoveryMethodDescription1: KnockoutObservable<string> = ko.observable("");
        recoveryMethodDescription2: KnockoutObservable<string> = ko.observable("");
        dataRecoverySummary: KnockoutObservable<DataRecoverySummary> = ko.observable(new DataRecoverySummary([], 0, [], []));
        kcp005ComponentOptionScreenH: any;
        selectedEmployeeCodeScreenH: KnockoutObservableArray<string> = ko.observableArray([]);
        employeeListScreenH: KnockoutObservableArray<UnitModel> = ko.observableArray([]);

        categoryListOld: Array<any> = [];
        recoveryProcessingId: string = nts.uk.util.randomId();
        constructor() {
            let self = this;
            //Fixed table
            if (/Chrome/.test(navigator.userAgent)) {
                $("#E4_1").ntsFixedTable({ height: 164, width: 715 });
                $("#F4_1").ntsFixedTable({ height: 184, width: 715 });
                $("#H4_1").ntsFixedTable({ height: 164, width: 700 });
            } else {
                $("#E4_1").ntsFixedTable({ height: 165, width: 715 });
                $("#F4_1").ntsFixedTable({ height: 184, width: 715 });
                $("#H4_1").ntsFixedTable({ height: 164, width: 700 });
            }
            //_____KCP005G________
            self.kcp005ComponentOptionScreenG = {
                isShowAlreadySet: false,
                isMultiSelect: true,
                listType: ListType.EMPLOYEE,
                employeeInputList: self.employeeListScreenG,
                selectType: SelectType.SELECT_BY_SELECTED_CODE,
                selectedCode: self.selectedEmployeeCodeScreenG,
                isDialog: false,
                isShowNoSelectRow: false,
                alreadySettingList: ko.observableArray([]),
                isShowWorkPlaceName: false,
                isShowSelectAllButton: true,
                maxRows: 15,
                tabindex: -1
            };

            //_____KCP005H________
            self.kcp005ComponentOptionScreenH = {
                isShowAlreadySet: false,
                isMultiSelect: false,
                listType: ListType.EMPLOYEE,
                employeeInputList: self.employeeListScreenH,
                selectType: SelectType.SELECT_BY_SELECTED_CODE,
                selectedCode: self.selectedEmployeeCodeScreenH,
                isDialog: false,
                isShowNoSelectRow: false,
                alreadySettingList: ko.observableArray([]),
                isShowWorkPlaceName: false,
                isShowSelectAllButton: false,
                maxRows: 20,
                tabindex: -1
            };

            self.startDateString.subscribe(value => {
                self.dataRecoverySelection().executePeriodInput().startDate = value;
                self.dataRecoverySelection().executePeriodInput.valueHasMutated();
            });

            self.endDateString.subscribe(value => {
                self.dataRecoverySelection().executePeriodInput().endDate = value;
                self.dataRecoverySelection().executePeriodInput.valueHasMutated();
            });

            //New code
            self.dataRecoverySelection().selectedUploadCls.subscribe(value => {
                if (value == 1) {
                    nts.uk.ui.errors.clearAll();
                }
            });
            //End new code

            self.dataContentConfirm().dataContentcategoryList.subscribe(value => {
                self.setWidthScrollHeader('.contentE', value);
            });

            self.changeDataRecoveryPeriod().changeDataCategoryList.subscribe(value => {
                self.setWidthScrollHeader('.contentF', value);
            });

            self.dataRecoverySummary().recoveryCategoryList.subscribe(value => {
                self.setWidthScrollHeader('.contentH', value);
            });
        }
        openHandleFileDialog(continueShowHandleDialog) {
            let self = this;
            if (!continueShowHandleDialog) {
                $('#E4_1').focus();
                return;
            }
            nts.uk.ui.windows.sub.modal('../c/index.xhtml').onClosed(() => {
                setShared("CMF004_D_PARAMS", getShared("CMF004_D_PARAMS"));
                nts.uk.ui.windows.sub.modal('../d/index.xhtml').onClosed(() => {
                    if (getShared("CMF004_E_PARAMS")) {
                        let recoveryInfo = getShared("CMF004_E_PARAMS");
                        if (recoveryInfo) {
                            let self = this;
                            if (recoveryInfo.continuteProcessing) {
                                self.recoveryProcessingId = recoveryInfo.processingId;
                                self.initScreenE();
                                $('#data-recovery-wizard').ntsWizard("next");
                                $('#E4_1').focus();
                                return;
                            } else {
                                if (recoveryInfo.continueShowHandleDialog)
                                    self.openHandleFileDialog(true);
                            }
                        }
                    }
                    $('#E4_1').focus();
                });
                $('#E4_1').focus();
            });
        }
        finished(fileInfo: any) {
            let self = this;
            console.log(fileInfo);
            if (fileInfo.id != null && fileInfo.originalName != null) {
                setShared("CMF004lParams", {
                    fileId: fileInfo.id,
                    fileName: fileInfo.originalName
                }, true);
                self.openHandleFileDialog(true);
            }
        }

        setWidthScrollHeader(frame, value): void {
            if (value.length > 5) {
                $(frame + ' .scroll-header.nts-fixed-header').css('width', '17px');
            } else {
                $(frame + ' .scroll-header.nts-fixed-header').css('width', '0px');
            }
        }

        initWizardScreen(): void {
            let self = this;
            self.initScreenB();
        }

        /**
         * B: データ復旧選択
         */
        initScreenB(): void {
            let self = this;
            block.invisible();
            self.startDateString(moment.utc().subtract(1, "M").add(1, "d").format("YYYY/MM/DD"));
            self.endDateString(moment.utc().format("YYYY/MM/DD"));
            let paramSearch = {
                startDate: moment.utc(self.dataRecoverySelection().executePeriodInput().startDate, "YYYY/MM/DD hh:mm:ss").toISOString(),
                endDate: moment.utc(self.dataRecoverySelection().executePeriodInput().endDate, "YYYY/MM/DD hh:mm:ss").add(1, "d").add(-1, "s").toISOString()
            };
            service.findDataRecoverySelection(paramSearch).done(function(data: Array<any>) {
                if (data && data.length) {
                    let recoveryFileList: Array<any> = [];
                    for (let i = 0; i < data.length; i++) {
                        let itemTarget =
                            {
                                saveSetCode: data[i].code == null ? '' : data[i].code,
                                saveSetName: data[i].name,
                                supplementaryExplanation: data[i].suppleExplanation,
                                storageStartDate: moment.utc(data[i].saveStartDatetime).format('YYYY/MM/DD HH:mm:ss'),
                                executeCategory: (data[i].saveForm) == 0 ? getText('CMF004_300') : getText('CMF004_301'),
                                targetNumber: data[i].targetNumberPeople + "人",
                                saveFileName: data[i].saveFileName + ".zip",
                                fileId: data[i].fileId,
                                storeProcessingId: data[i].storeProcessingId
                            };
                        recoveryFileList.push(itemTarget);
                    }
                    self.dataRecoverySelection().recoveryFileList(recoveryFileList);
                }
            }).always(() => {
                block.clear();
            });
        }

        searchDataRecovery(): void {
            let self = this;
            $("#daterangepicker_b4_3 .ntsDatepicker").trigger("validate");
            if (!nts.uk.ui.errors.hasError()) {
                block.invisible();
                let paramSearch = {
                    startDate: moment.utc(self.dataRecoverySelection().executePeriodInput().startDate, "YYYY/MM/DD hh:mm:ss").toISOString(),
                    endDate: moment.utc(self.dataRecoverySelection().executePeriodInput().endDate, "YYYY/MM/DD hh:mm:ss").add(1, "d").add(-1, "s").toISOString()
                };
                self.dataRecoverySelection().recoveryFileList.removeAll();
                service.findDataRecoverySelection(paramSearch).done(function(data: Array<any>) {
                    if (data && data.length) {
                        let recoveryFileList: Array<any> = [];
                        for (let i = 0; i < data.length; i++) {
                            let itemTarget =
                                {
                                    saveSetCode: data[i].code ? data[i].code : '' ,
                                    saveSetName: data[i].name,
                                    supplementaryExplanation: data[i].suppleExplanation,
                                    storageStartDate: moment.utc(data[i].saveStartDatetime).format('YYYY/MM/DD HH:mm:ss'),
                                    executeCategory: (data[i].saveForm) == 0 ? getText('CMF004_300') : getText('CMF004_301'),
                                    targetNumber: data[i].targetNumberPeople + "人",
                                    saveFileName: data[i].saveFileName + ".zip",
                                    fileId: data[i].fileId,
                                    storeProcessingId: data[i].storeProcessingId
                                };
                            recoveryFileList.push(itemTarget);
                        }
                        self.dataRecoverySelection().recoveryFileList(recoveryFileList);
                    }
                    self.dataRecoverySelection().selectedRecoveryFile("");
                }).always(() => {
                    block.clear();
                });
            }
        }

        /**
         * E:データ内容確認
         */
        initScreenE(): void {
            let self = this;
            block.invisible();
            //Get Data TableList for Screen E
            service.findTableList(self.recoveryProcessingId).done(function(data: Array<any>) {
                let listCategory: Array<CategoryInfo> = [];
                if (data && data.length) {
                    _.each(data, (x, i) => {
                        let rowNumber = i + 1;
                        let iscanNotBeOld: boolean = (x.canNotBeOld == 1);
                        let isRecover: boolean     = (x.canNotBeOld == 1);
                        let categoryName = x.categoryName;
                        let categoryId     = x.categoryId;
                        let recoveryPeriod = x.retentionPeriodCls;
                        let startOfPeriod  = x.saveDateFrom;
                        let endOfPeriod    = x.saveDateTo;
                        let recoveryMethod = x.storageRangeSaved == 1 ? getText('CMF004_305') : getText('CMF004_306');
                        listCategory.push(new CategoryInfo(rowNumber, isRecover, categoryId, categoryName, recoveryPeriod, startOfPeriod, endOfPeriod, recoveryMethod, iscanNotBeOld));
                    });
                    self.dataContentConfirm().dataContentcategoryList(listCategory);
                    self.recoverySourceFile(data[0].compressedFileName + '.zip');
                    self.recoverySourceCode(data[0].saveSetCode);
                    self.recoverySourceName(data[0].saveSetName);
                    self.saveForm(data[0].saveForm);
                    self.supplementaryExplanation(data[0].supplementaryExplanation);
                }

            }).always(() => {
                block.clear();
            });
        }

        getTextRecovery(recoveryPeriod): string {
            if (recoveryPeriod() === 0) return getText("Enum_TimeStore_FULL_TIME");
            if (recoveryPeriod() === 3) return getText("Enum_TimeStore_ANNUAL");
            if (recoveryPeriod() === 2) return getText("Enum_TimeStore_MONTHLY");
            if (recoveryPeriod() === 1) return getText("Enum_TimeStore_DAILY");
        }

        /**
         * F:データ復旧期間変更
         */
        initScreenF(): void {
            let self = this;

            // check recovery method
            let recoveryMethod = self.dataContentConfirm().selectedRecoveryMethod();
            let _listCategory = self.dataContentConfirm().dataContentcategoryList();
            let _itemList: Array<CategoryInfo> = [];
            _.forEach(_listCategory, (x, i) => {
                let isRecover = true;
                if (!x.iscanNotBeOld || !x.isRecover()) {
                    isRecover = false;
                }
                let isEnablePeriod = recoveryMethod == RecoveryMethod.SELECTED_RANGE ? true : false ;
                _itemList.push(new CategoryInfo(i + 1, isRecover, x.categoryId(), x.categoryName(), x.recoveryPeriod(), x.startOfPeriod(), x.endOfPeriod(), x.recoveryMethod(), isEnablePeriod));
            });
            self.changeDataRecoveryPeriod().changeDataCategoryList(_itemList);
            self.categoryListOld = ko.toJS(_itemList);
        }

        initScreenG(): void {
            let self = this;
            block.invisible();
            //Get Data PerformDataRecover for Screen KCP 005
            service.findPerformDataRecover(self.recoveryProcessingId).done(function(data: any) {
               console.log(); 
                if (data.targets) {
                    self.employeeListScreenG.removeAll();
                    let employeeData: Array<any> = [];
                    _.forEach(data.targets, x => {
                        employeeData.push({ code: x.scd, name: x.bussinessName, id: x.sid });
                    });
                    employeeData = _.sortBy(employeeData, ["code"]);
                    self.employeeListScreenG(employeeData);
                    $('#kcp005component .nts-gridlist').attr('tabindex', -1);
                    if(self.dataContentConfirm().selectedRecoveryMethod() == 0){
                        self.kcp005ComponentOptionScreenG.selectType = 2;
                        self.kcp005ComponentOptionScreenG.disableSelection = true;
                        $('#kcp005component').ntsListComponent(self.kcp005ComponentOptionScreenG);
                    }else{
                        self.kcp005ComponentOptionScreenG.selectType = 2;
                        self.kcp005ComponentOptionScreenG.disableSelection = false;
                        $('#kcp005component').ntsListComponent(self.kcp005ComponentOptionScreenG);
                    }
                }
            }).always(() => {
                block.clear();
            });
            $('#kcp005component .ntsSearchBox').attr('tabindex', -1);
            $('#kcp005component').find(':button').each(function() {
                $(this).attr('tabindex', -1);
            });
        }

        /**
         * H:データ復旧サマリ
         */
        initScreenH(): void {
            let self = this;
            $('#kcp005component1 div[id*="horizontalScrollContainer"]').remove();
            let _categoryList = (self.getRecoveryCategory(self.changeDataRecoveryPeriod().changeDataCategoryList()));
            _.forEach(_categoryList, categoryItem => {
                let a = categoryItem;
                categoryItem.startOfPeriod(self.formatDate(categoryItem.recoveryPeriod, categoryItem.startOfPeriod()));
                categoryItem.endOfPeriod(self.formatDate(categoryItem.recoveryPeriod, categoryItem.endOfPeriod()));
            });
            let _employeeList = self.getRecoveryEmployee(self.employeeListScreenG(), self.selectedEmployeeCodeScreenG());
            _employeeList = _.sortBy(_employeeList, ["code"]);
            self.employeeListScreenH(_employeeList);
            let _recoveryMethod = self.dataContentConfirm().selectedRecoveryMethod();
            let _recoveryMethodDescription1 = self.getRecoveryMethodDescription1(_recoveryMethod);
            let _recoveryMethodDescription2 = self.getRecoveryMethodDescription2(_recoveryMethod);
            self.dataRecoverySummary().recoveryCategoryList(_categoryList);
            self.dataRecoverySummary().recoveryMethod(_recoveryMethod);
            self.recoveryMethodDescription1(_recoveryMethodDescription1);
            self.recoveryMethodDescription2(_recoveryMethodDescription2);
        }

        /**
         * Get recovery category
         */
        getRecoveryCategory(selectedCategory: Array<CategoryInfo>): Array<any> {
            let self = this;
            let _listCategory = _.filter(selectedCategory, x => { return x.isRecover() == true; });
            let _itemList: Array<CategoryInfo> = [];
            _.forEach(_listCategory, (x, i) => {
                _itemList.push(new CategoryInfo(i + 1, x.isRecover(), x.categoryId(), x.categoryName(), x.recoveryPeriod(), x.startOfPeriod(), x.endOfPeriod(), x.recoveryMethod(), x.iscanNotBeOld()));
            });
            return _itemList;
        }

        formatDate(recoveryPeriod, dateFormat): string {
            if (recoveryPeriod() == PeriodEnum.DAY) {
                return moment.utc(dateFormat).format("YYYY/MM/DD");
            }
            if (recoveryPeriod() == PeriodEnum.MONTH) {
                return nts.uk.time.formatYearMonth(parseInt(dateFormat));
            }
            return dateFormat;
        }

        /**
         * Get recovery employee
         */
        getRecoveryEmployee(dataEmployeeList: Array<UnitModel>, selectedEmployeeList: Array<string>): Array<UnitModel> {
            return _.filter(dataEmployeeList, item => _.includes(selectedEmployeeList, item.code));
        }

        getRecoveryMethodDescription1(recoveryMethod: number): string {
            if (recoveryMethod == RecoveryMethod.RESTORE_ALL) return getText('CMF004_92');
            return getText('CMF004_93');
        }

        getRecoveryMethodDescription2(recoveryMethod: number): string {
            if (recoveryMethod == RecoveryMethod.RESTORE_ALL) return getText('CMF004_94');
            return getText('CMF004_95');
        }

        nextToScreenE(): void {
            let self = this;
            self.recoveryProcessingId = nts.uk.util.randomId();
            let paramObtainRecovery = {
                storeProcessingId: self.dataRecoverySelection().selectedRecoveryFile(),
                dataRecoveryProcessId: self.recoveryProcessingId
            };
            nts.uk.ui.block.grayout();
            service.obtainRecovery(paramObtainRecovery).done((res) => {
                if (res) {
                    if (res.status) {
                        self.initScreenE();
                        $('#data-recovery-wizard').ntsWizard("next");
                        $('#E4_1').focus();
                    } else {
                        if (res.message.length > 0) {
                            dialog.alertError({ messageId: res.message });
                        }
                    }
                }
            }).fail((err) => {
                dialog.alertError(err);
                block.clear();
            }).always((err) => {
                block.clear();
            });
            $('#E4_1').focus();
        }

        backToScreenA(): void {
            nts.uk.request.jump("/view/cmf/004/a/index.xhtml");
        }

        nextToScreenF(): void {
            let self = this;
            self.initScreenF();
            nts.uk.ui.errors.clearAll();
            let checkItemE = _.filter(self.dataContentConfirm().dataContentcategoryList(), x => { return x.isRecover() == true; }).length;
            if (checkItemE == 0) {
                dialog.alertError({ messageId: "Msg_1265" });
            } else {
                
                $('#data-recovery-wizard').ntsWizard("next");
                $("#F5_5:first-child .start-date input:first-child").focus();
                
            }

        }

        nextToScreenG(): void {
            nts.uk.ui.errors.clearAll();
            let self = this;
            $("#F5_5 .ntsDatepicker").trigger("validate");
            if (!nts.uk.ui.errors.hasError()) {
                for (let checkRow of ko.toJS(self.changeDataRecoveryPeriod().changeDataCategoryList())) {
                    if (checkRow.isRecover) {
                        if (checkRow.startOfPeriod > checkRow.endOfPeriod) {
                            $('tr[data-id=' + checkRow.rowNumber + ']').find('.ntsDatepicker').eq(0).ntsError('set', { messageId: 'Msg_1320', messageParams: [checkRow.rowNumber] });
                        }
                        let oldData = _.find(self.categoryListOld, x => {
                            return x.categoryId == checkRow.categoryId;
                        });
                        if (oldData.startOfPeriod > checkRow.startOfPeriod) {
                            $('tr[data-id=' + checkRow.rowNumber + ']').find('.ntsDatepicker').eq(0).ntsError('set', { messageId: 'Msg_1319', messageParams: [checkRow.rowNumber] });
                        }
                        if (oldData.endOfPeriod < checkRow.endOfPeriod) {
                            $('tr[data-id=' + checkRow.rowNumber + ']').find('.ntsDatepicker').eq(1).ntsError('set', { messageId: 'Msg_1319', messageParams: [checkRow.rowNumber] });
                        }
                    }
                }
            }
            if (!nts.uk.ui.errors.hasError()) {
                self.initScreenG();
                $('#data-recovery-wizard').ntsWizard("next");
            }

            $('#kcp005component').focus();
        }

        nextToScreenH(): void {
            let self = this;
            self.initScreenH();
            $('#data-recovery-wizard').ntsWizard("next");
            $('#H9_2').focus();
        }

        restoreData_click(): void {
            let self = this;
            setShared("CMF004IParams", {
                saveForm: self.saveForm(),
                employeeList: ko.toJS(self.employeeListScreenH),
                recoveryCategoryList: ko.toJS(self.dataRecoverySummary().recoveryCategoryList),
                recoveryFile: self.recoverySourceFile(),
                recoverySourceCode: self.recoverySourceCode(),
                recoverySourceName: self.recoverySourceName(),
                supplementaryExplanation: self.supplementaryExplanation(),
                recoveryMethodOptions: self.dataContentConfirm().selectedRecoveryMethod(),
                recoveryProcessingId: self.recoveryProcessingId,
                store_del_ProcessingId: self.dataRecoverySelection().selectedRecoveryFile()
            });
            nts.uk.ui.windows.sub.modal("/view/cmf/004/i/index.xhtml").onClosed(() => {
                self.buton_I_enable(false);
            });
        }

        backToPreviousScreen(): void {
            $('#data-recovery-wizard').ntsWizard("prev");
            nts.uk.ui.errors.clearAll();
        }

        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            self.initWizardScreen();
            dfd.resolve(self);
            return dfd.promise();
        }
    }

    export enum PeriodEnum {
        FULLTIME = 0, //全期間一律
        YEAR = 3, //日次
        MONTH = 2, //月次
        DAY = 1  //年次
    }

    export enum RecoveryMethod {
        RESTORE_ALL = 0, //全件復旧
        SELECTED_RANGE = 1 //選択した範囲で復旧
    }

    export class CategoryInfo {
        rowNumber: KnockoutObservable<number>;
        isRecover: KnockoutObservable<boolean>;
        categoryId: KnockoutObservable<string>;
        categoryName: KnockoutObservable<string>;
        recoveryPeriod: KnockoutObservable<string>;
        recoveryMethod: KnockoutObservable<string>;
        startOfPeriod: KnockoutObservable<string>;
        endOfPeriod: KnockoutObservable<string>;
        iscanNotBeOld: KnockoutObservable<boolean>;
        isEnablePeriod: KnockoutObservable<boolean>;
        constructor(rowNumber: number, isRecover: boolean, categoryId: string, categoryName: string, recoveryPeriod: string, startOfPeriod: string, endOfPeriod: string, recoveryMethod: string, isEnablePeriod: boolean) {
            let self = this;
            self.rowNumber = ko.observable(rowNumber);
            self.isRecover = ko.observable(isRecover);
            self.categoryId = ko.observable(categoryId);
            self.categoryName = ko.observable(categoryName);
            self.recoveryPeriod = ko.observable(recoveryPeriod);
            self.startOfPeriod = ko.observable(startOfPeriod);
            self.endOfPeriod = ko.observable(endOfPeriod);
            self.recoveryMethod = ko.observable(recoveryMethod);
            self.iscanNotBeOld = ko.observable(isEnablePeriod);
            self.isEnablePeriod = ko.observable(isEnablePeriod);
        }
    }


    export interface IRecoveryFileInfo {
        saveSetCode: string;
        saveSetName: string;
        supplementaryExplanation: string;
        storageStartDate: string;
        executeCategory: string;
        targetNumber: string;
        saveFileName: string;
    }

    export class RecoveryFileInfo {
        saveSetCode: string;
        saveSetName: string;
        supplementaryExplanation: string;
        storageStartDate: string;
        executeCategory: string;
        targetNumber: string;
        saveFileName: string;
        constructor(input: IRecoveryFileInfo) {
            let self = this;
            self.saveSetCode = input.saveSetCode;
            self.saveSetName = input.saveSetName;
            self.supplementaryExplanation = input.supplementaryExplanation;
            self.storageStartDate = input.storageStartDate;
            self.executeCategory = input.executeCategory;
            self.targetNumber = input.targetNumber;
            self.saveFileName = input.saveFileName;
        }
    }

    /**
     * B: データ復旧選択
     */
    export class DataRecoverySelection {
        selectedUploadCls: KnockoutObservable<number>;
        selectedSaveFileCls: KnockoutObservable<number>;
        executePeriodInput: KnockoutObservable<any>;
        recoveryFileList: KnockoutObservableArray<IRecoveryFileInfo>;
        selectedRecoveryFile: KnockoutObservable<string>;
        constructor(selectedUploadCls: number, selectedSaveFileCls: number, executePeriodInput: any, recoveryFileList: Array<any>, selectedRecoveryFile: string) {
            let self = this;
            self.selectedUploadCls = ko.observable(selectedUploadCls);
            self.selectedSaveFileCls = ko.observable(selectedSaveFileCls);
            self.executePeriodInput = ko.observable(executePeriodInput);
            self.recoveryFileList = ko.observableArray(recoveryFileList);
            self.selectedRecoveryFile = ko.observable(selectedRecoveryFile);
        }
    }

    /**
     * E:データ内容確認
     */
    export class DataContentConfirm {
        dataContentcategoryList: KnockoutObservableArray<CategoryInfo>;
        selectedRecoveryMethod: KnockoutObservable<number>;
        constructor(categoryList: Array<any>, selectedRecoveryMethod: number) {
            let self = this;
            self.dataContentcategoryList = ko.observableArray(categoryList);
            self.selectedRecoveryMethod = ko.observable(selectedRecoveryMethod);
        }
    }

    /**
     * F:データ復旧期間変更
     */
    export class ChangeDataRecoveryPeriod {
        changeDataCategoryList: KnockoutObservableArray<CategoryInfo>;
        constructor(categoryList: Array<any>) {
            let self = this;
            self.changeDataCategoryList = ko.observableArray(categoryList);
        }
    }

    /**
     * H:データ復旧サマリ
     */
    export class DataRecoverySummary {
        recoveryCategoryList: KnockoutObservableArray<CategoryInfo>;
        recoveryMethod: KnockoutObservable<number>;
        recoveryEmployee: KnockoutObservableArray<EmployeeSearchDto>;
        selectedEmployee: KnockoutObservableArray<any>;
        constructor(recoveryCategoryList: Array<any>, recoveryMethod: number, recoveryEmployee: Array<any>, selectedEmployee: Array<any>) {
            let self = this;
            self.recoveryCategoryList = ko.observableArray(recoveryCategoryList);
            self.recoveryMethod = ko.observable(recoveryMethod);
            self.recoveryEmployee = ko.observableArray(recoveryEmployee);
            self.selectedEmployee = ko.observableArray(selectedEmployee);
        }
    }

    export interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceId: string;
        workplaceName: string;
    }

    /**
     * KCP005
     */
    export class ListType {
        static EMPLOYMENT = 1;
        static CLASSIFICATION = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }

    export interface UnitModel {
        code: string;
        id: string;
        name?: string;
        workplaceName?: string;
        isAlreadySetting?: boolean;
    }

    export class SelectType {
        static SELECT_BY_SELECTED_CODE = 1;
        static SELECT_ALL = 2;
        static SELECT_FIRST_ITEM = 3;
        static NO_SELECT = 4;
    }
}