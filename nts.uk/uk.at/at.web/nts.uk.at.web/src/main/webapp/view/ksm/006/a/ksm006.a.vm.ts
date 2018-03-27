module nts.uk.at.view.ksm006.a {
    import CompanyBasicWorkFindDto = service.model.CompanyBasicWorkFindDto;
    import BasicWorkSettingDto = service.model.BasicWorkSettingDto;
    import WorkplaceBasicWorkFindDto = service.model.WorkplaceBasicWorkFindDto;
    import WorkplaceBasicWorkDto = service.model.WorkplaceBasicWorkDto;
    import ClassifiBasicWorkFindDto = service.model.ClassifiBasicWorkFindDto;
    import ClassificationBasicWorkDto = service.model.ClassificationBasicWorkDto;
    import blockUI = nts.uk.ui.block;
    export module viewmodel {
        export class ScreenModel {
            selectedWorkplaceId: KnockoutObservable<string>;
            baseDate: KnockoutObservable<Date>;
            workplaceAlreadySetList: KnockoutObservableArray<UnitAlreadySettingModel>;
            workplaceList: KnockoutObservableArray<UnitModel>;
            workplaceGrid: any;

            classificationGrid: any;
            selectedClassifi: KnockoutObservable<string>;
            classificationList: KnockoutObservableArray<UnitModel>;
            classifiAlreadySetList: KnockoutObservableArray<any>;

            // on Company Tab
            companyBWWorkingDay: KnockoutObservable<BasicWorkModel>;
            companyBWNonInLaw: KnockoutObservable<BasicWorkModel>;
            companyBWNonExtra: KnockoutObservable<BasicWorkModel>;

            // on Workplace Tab
            workplaceBWWorkingDay: KnockoutObservable<BasicWorkModel>;
            workplaceBWNonInLaw: KnockoutObservable<BasicWorkModel>;
            workplaceBWNonExtra: KnockoutObservable<BasicWorkModel>;

            // on Classification Tab
            classifyBWWorkingDay: KnockoutObservable<BasicWorkModel>;
            classifyBWNonInLaw: KnockoutObservable<BasicWorkModel>;
            classifyBWNonExtra: KnockoutObservable<BasicWorkModel>;

            isShowCompanyTab: KnockoutObservable<boolean>;
            isShowWorkplaceTab: KnockoutObservable<boolean>;
            isShowClassifyTab: KnockoutObservable<boolean>;

            companyId: string;
            isUpdateModeWorkplace: KnockoutObservable<boolean>;
            isUpdateModeClassify: KnockoutObservable<boolean>;
            workplaceName: KnockoutObservable<string>;
            classificationName: KnockoutObservable<string>;

            // Dirty checker
            dirtyChecker: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                self.selectedWorkplaceId = ko.observable("");
                self.baseDate = ko.observable(new Date());
                self.workplaceAlreadySetList = ko.observableArray([]);
                self.workplaceGrid = {
                    isShowAlreadySet: true,
                    isMultiSelect: false,
                    treeType: ListType.WORK_PLACE,
                    selectedWorkplaceId: self.selectedWorkplaceId,
                    baseDate: self.baseDate,
                    selectType: SelectionType.SELECT_FIRST_ITEM,
                    isShowSelectButton: false,
                    isDialog: false,
                    alreadySettingList: self.workplaceAlreadySetList,
                    maxRows: 10,
                    systemType: 2
                };
                $('#workplace-list').ntsTreeComponent(self.workplaceGrid);

                self.selectedClassifi = ko.observable("");
                self.classifiAlreadySetList = ko.observableArray([]);
                self.classificationGrid = {
                    isShowAlreadySet: true,
                    isMultiSelect: false,
                    listType: ListType.CLASSIFICATION,
                    selectType: SelectionType.SELECT_FIRST_ITEM,
                    selectedCode: self.selectedClassifi,
                    isDialog: false,
                    isShowNoSelectRow: false,
                    alreadySettingList: self.classifiAlreadySetList,
                    maxRows: 12
                };
//                $('#classification-list').ntsListComponent(self.classificationGrid);

                // Initialize on Company Tab
                self.companyBWWorkingDay = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));
                self.companyBWNonInLaw = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));
                self.companyBWNonExtra = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));

                // Initialize on Workplace Tab
                self.workplaceBWWorkingDay = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));
                self.workplaceBWNonInLaw = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));
                self.workplaceBWNonExtra = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));

                // Initialize on Classification Tab
                self.classifyBWWorkingDay = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));
                self.classifyBWNonInLaw = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));
                self.classifyBWNonExtra = ko.observable<BasicWorkModel>(new BasicWorkModel(null, null, null, null));

                self.isShowCompanyTab = ko.observable(true);
                self.isShowWorkplaceTab = ko.observable(false);
                self.isShowClassifyTab = ko.observable(false);

                self.isUpdateModeWorkplace = ko.computed(function() {
                    var existItem = self.workplaceAlreadySetList().filter((item) => {
                        return item.workplaceId == self.selectedWorkplaceId();
                    })[0];
                    if (existItem) {
                        return true;
                    } else {
                        return false;
                    }
                });

                self.isUpdateModeClassify = ko.computed(function() {
                    var existItem = self.classifiAlreadySetList().filter((item) => {
                        return item.code == self.selectedClassifi();
                    })[0];
                    if (existItem) {
                        return true;
                    } else {
                        return false;
                    }
                });

                self.workplaceName = ko.observable(null);
                self.classificationName = ko.observable(null);
            }

            /**
             * Start Page
             */
            public startPage(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                blockUI.invisible();
                self.clearError();
                self.findCompanyBasicWork().done(function() {
                    blockUI.clear();
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                    blockUI.clear();
                });
                return dfd.promise();
            }

            /**
             * Set Workplace Name
             */
            private setWorkplaceName(treeData: Array<any>, workPlaceId: string) {
                let self = this;
                for (let data of treeData) {
                    // Found!
                    if (data.workplaceId == workPlaceId) {
                        self.workplaceName(data.name);
                    }
                    // Continue to find in childs.
                    if (data.childs.length > 0) {
                        this.setWorkplaceName(data.childs, workPlaceId);
                    }
                }
            }


            /**
             * Find CompanyBasicWork
             */
            private findCompanyBasicWork(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.clearError();
                service.findCompanyBasicWork().done(function(data: CompanyBasicWorkFindDto) {
                    if (!data) {
                        self.companyBWWorkingDay(new BasicWorkModel(null, null, null, null));
                        self.companyBWNonInLaw(new BasicWorkModel(null, null, null, null));
                        self.companyBWNonExtra(new BasicWorkModel(null, null, null, null));
                        dfd.resolve();
                        return;
                    }
                    // ForEach BasicWorkSetting
                    data.basicWorkSetting.forEach(function(item, index) {
                        switch (item.workDayDivision) {
                            case WorkingDayDivision.WORKING_DAY:
                                self.companyBWWorkingDay(new BasicWorkModel(item.workTypeCode,
                                    item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                                break;

                            case WorkingDayDivision.NON_WORK_INLAW:
                                self.companyBWNonInLaw(new BasicWorkModel(item.workTypeCode,
                                    item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                                break;

                            case WorkingDayDivision.NON_WORK_EXTR:
                                self.companyBWNonExtra(new BasicWorkModel(item.workTypeCode,
                                    item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                                break;
                        }
                    });
                    self.companyId = data.companyId;
                    // Focus on 
                    $('#companyBWWorkingDayBtn').focus();
                    dfd.resolve();
                    return;
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
                return dfd.promise();
            }


            /**
             * Find WorkplaceBasicWork by WorkplaceId
             */
            private findWorkplaceBasicWork(workplaceId: string): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.clearError();
                service.findWorkplaceBasicWork(workplaceId).done(function(data: WorkplaceBasicWorkFindDto) {
                    if (!data) {
                        self.companyBWWorkingDay(new BasicWorkModel(null, null, null, null));
                        self.companyBWNonInLaw(new BasicWorkModel(null, null, null, null));
                        self.companyBWNonExtra(new BasicWorkModel(null, null, null, null));
                        dfd.resolve();
                        return;
                    }
                    // If Data is not null
                    data.basicWorkSetting.forEach(function(item, index) {
                        switch (item.workDayDivision) {
                            case WorkingDayDivision.WORKING_DAY:
                                self.companyBWWorkingDay(new BasicWorkModel(item.workTypeCode,
                                    item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                                break;

                            case WorkingDayDivision.NON_WORK_INLAW:
                                self.companyBWNonInLaw(new BasicWorkModel(item.workTypeCode,
                                    item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                                break;

                            case WorkingDayDivision.NON_WORK_EXTR:
                                self.companyBWNonExtra(new BasicWorkModel(item.workTypeCode,
                                    item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                                break;
                        }
                    });
                    dfd.resolve();
                    return;

                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
                return dfd.promise();
            }

            // clear Error
            private clearError(): void {
                if ($('button').ntsError("hasError")) {
                    $('button').ntsError('clear');
                }

                if ($('#companyWide-content').ntsError("hasError")) {
                    $('#companyWide-content').ntsError('clear');
                }

                if ($('#workplace-content').ntsError("hasError")) {
                    $('#workplace-content').ntsError('clear');
                }

                if ($('#classification-content').ntsError("hasError")) {
                    $('#classification-content').ntsError('clear');
                }
            }

            /**
             * Register Basic Work By Company
             */
            public registerByCompany(): void {
                var self = this;
                blockUI.invisible();
                self.clearError();
                service.saveCompanyBasicWork(self.collectCompanyData()).done(function(data) {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                    blockUI.clear();
                }).fail((res) => {
                    if (res.messageId == "Msg_178") {
                        let errDetails = res.supplements;
                        let keys = Object.keys(errDetails);
                        // Sort keys
                        keys.sort(function(left, right) {
                            return left == right ?
                                0 : (left < right ? -1 : 1)
                        });
                        keys.forEach(function(item) {
                            switch (item) {
                                case "KSM006_6": {
                                    $("#companyBWWorkingDayBtn").ntsError('set', { messageId: "Msg_178", messageParams: [nts.uk.resource.getText("KSM006_6")] });
                                    break;
                                }
                                case "KSM006_7": {
                                    $("#companyBWNonInLawBtn").ntsError('set', { messageId: "Msg_179", messageParams: [nts.uk.resource.getText("KSM006_7")] });
                                    break;
                                }
                                case "KSM006_8": {
                                    $("#companyBWNonExtraBtn").ntsError('set', { messageId: "Msg_179", messageParams: [nts.uk.resource.getText("KSM006_8")] });
                                    break;
                                }
                                default: { break; }
                            }
                        });
                    } else if (res.messageId == "Msg_389") {
                        $('#companyWide-content').ntsError('set', { messageId: "Msg_389" });
                    } else if (res.messageId == "Msg_390") {
                        $('#companyWide-content').ntsError('set', { messageId: "Msg_390" });
                    } else if (res.messageId == "Msg_416") {
                        $('#companyWide-content').ntsError('set', { messageId: "Msg_416" });
                    } else if (res.messageId == "Msg_417") {
                        $('#companyWide-content').ntsError('set', { messageId: "Msg_417" });
                    } else if (res.messageId == "Msg_434") {
                        $('#companyWide-content').ntsError('set', { messageId: "Msg_434" });
                    } else if (res.messageId == "Msg_435") {
                        $('#companyWide-content').ntsError('set', { messageId: "Msg_435" });
                    } else {
                        nts.uk.ui.dialog.alertError(res.message);
                    }
                }).always(function() {
                    nts.uk.ui.block.clear();
                });
            }


            /**
             * Register Basic Work By Workplace
             */
            public registerByWorkplace(): void {
                var self = this;

                self.clearError();
                if (!self.selectedWorkplaceId()) {
                    nts.uk.ui.dialog.info({ messageId: "Msg_339" });
                    return;
                }

                blockUI.invisible();

                service.saveWorkplaceBasicWork(self.collectWorkplaceData()).done(function(data) {

                    var existItem = self.workplaceAlreadySetList().filter((item) => {
                        return item.workplaceId == self.workplaceGrid.selectedWorkplaceId();
                    })[0];
                    // Set AlreadySetting
                    if (!existItem) {
                        self.workplaceAlreadySetList.push(new UnitAlreadySettingModel(self.selectedWorkplaceId(), true));
                    }

                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });

                    blockUI.clear();
                }).fail((res) => {
                    if (res.messageId == "Msg_178") {
                        let errDetails = res.supplements;
                        let keys = Object.keys(errDetails);
                        // Sort keys
                        keys.sort(function(left, right) {
                            return left == right ?
                                0 : (left < right ? -1 : 1)
                        });
                        keys.forEach(function(item) {
                            switch (item) {
                                case "KSM006_6": {
                                    $("#workplaceBWWorkingDayBtn").ntsError('set', { messageId: "Msg_178", messageParams: [nts.uk.resource.getText("KSM006_6")] });
                                    break;
                                }
                                case "KSM006_7": {
                                    $("#workplaceBWNonInLawBtn").ntsError('set', { messageId: "Msg_179", messageParams: [nts.uk.resource.getText("KSM006_7")] });
                                    break;
                                }
                                case "KSM006_8": {
                                    $("#workplaceBWNonExtraBtn").ntsError('set', { messageId: "Msg_179", messageParams: [nts.uk.resource.getText("KSM006_8")] });
                                    break;
                                }
                                default: { break; }
                            }
                        });
                    } else if (res.messageId == "Msg_389") {
                        $('#workplace-content').ntsError('set', { messageId: "Msg_389" });
                    } else if (res.messageId == "Msg_390") {
                        $('#workplace-content').ntsError('set', { messageId: "Msg_390" });
                    } else if (res.messageId == "Msg_416") {
                        $('#workplace-content').ntsError('set', { messageId: "Msg_416" });
                    } else if (res.messageId == "Msg_417") {
                        $('#workplace-content').ntsError('set', { messageId: "Msg_417" });
                    } else if (res.messageId == "Msg_434") {
                        $('#workplace-content').ntsError('set', { messageId: "Msg_434" });
                    } else if (res.messageId == "Msg_435") {
                        $('#workplace-content').ntsError('set', { messageId: "Msg_435" });
                    } else {
                        nts.uk.ui.dialog.alertError(res.message);
                    }
                }).always(function() {
                    nts.uk.ui.block.clear();
                });
            }


            /**
             * Register Basic Work By Classification 
             */
            registerByClassification(): void {
                var self = this;

                self.clearError();
                if (!self.selectedClassifi()) {
                    nts.uk.ui.dialog.info({ messageId: "Msg_339" });
                    return;
                }

                blockUI.invisible();

                service.saveClassifyBasicWork(self.collectClassifyData()).done(function(data) {
                    // Check if exist alreadysetting of selectedItem
                    var existItem = self.classifiAlreadySetList().filter((item) => {
                        return item.code == self.classificationGrid.selectedCode();
                    })[0];
                    // Set AlreadySetting
                    if (!existItem) {
                        self.classifiAlreadySetList.push({ "code": self.selectedClassifi(), "isAlreadySetting": true });
                    }

                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                    blockUI.clear();
                }).fail((res) => {
                    if (res.messageId == "Msg_178") {
                        let errDetails = res.supplements;
                        let keys = Object.keys(errDetails);
                        // Sort keys
                        keys.sort(function(left, right) {
                            return left == right ?
                                0 : (left < right ? -1 : 1)
                        });
                        keys.forEach(function(item) {
                            switch (item) {
                                case "KSM006_6": {
                                    $("#classifyBWWorkingDayBtn").ntsError('set', { messageId: "Msg_178", messageParams: [nts.uk.resource.getText("KSM006_6")] });
                                    break;
                                }
                                case "KSM006_7": {
                                    $("#classifyBWNonInLawBtn").ntsError('set', { messageId: "Msg_179", messageParams: [nts.uk.resource.getText("KSM006_7")] });
                                    break;
                                }
                                case "KSM006_8": {
                                    $("#classifyBWNonExtraBtn").ntsError('set', { messageId: "Msg_179", messageParams: [nts.uk.resource.getText("KSM006_8")] });
                                    break;
                                }
                                default: { break; }
                            }
                        });
                    } else if (res.messageId == "Msg_389") {
                        $('#classification-content').ntsError('set', { messageId: "Msg_389" });
                    } else if (res.messageId == "Msg_390") {
                        $('#classification-content').ntsError('set', { messageId: "Msg_390" });
                    } else if (res.messageId == "Msg_416") {
                        $('#classification-content').ntsError('set', { messageId: "Msg_416" });
                    } else if (res.messageId == "Msg_417") {
                        $('#classification-content').ntsError('set', { messageId: "Msg_417" });
                    } else if (res.messageId == "Msg_434") {
                        $('#classification-content').ntsError('set', { messageId: "Msg_434" });
                    } else if (res.messageId == "Msg_435") {
                        $('#classification-content').ntsError('set', { messageId: "Msg_435" });
                    } else {
                        nts.uk.ui.dialog.alertError(res.message);
                    }
                }).always(function() {
                    nts.uk.ui.block.clear();
                });
            }
            /**
             *  Remove Classification Basic Work
             */
            removeByClassification(): void {
                var self = this;
                blockUI.invisible();
                self.clearError();
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.removeClassifyBasicWork(self.selectedClassifi()).done(function() {
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" });

                        // Remove AlreadySetting from Classification BW Grid List 
                        self.classifiAlreadySetList.remove(self.classifiAlreadySetList().filter((item) => {
                            return item.code == self.selectedClassifi();
                        })[0]);

                        // Find ClassificationBasicWork
                        service.findClassifyBasicWork(self.selectedClassifi()).done(function(classifyBasicWork: ClassifiBasicWorkFindDto) {
                            self.bindClassifyBasicWork(classifyBasicWork);
                        });
                        blockUI.clear();
                    }).fail((res) => {
                        nts.uk.ui.dialog.alertError(res.message).then(() => { nts.uk.ui.block.clear(); });
                    });
                }).then(() => { 
                    blockUI.clear();
                });

            }

            /**
             *  Remove Workplace Basic Work
             */
            removeByWorkplace(): void {
                var self = this;
                blockUI.invisible();
                self.clearError();
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.removeWorkplaceBasicWork(self.selectedWorkplaceId()).done(function() {
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" });

                        // Remove AlreadySetting from Workplace BW Grid List
                        self.workplaceAlreadySetList.remove(self.workplaceAlreadySetList().filter((item) => {
                            return item.workplaceId == self.selectedWorkplaceId();
                        })[0]);

                        // Find WorkplaceBasicWork by WorkplaceId
                        service.findWorkplaceBasicWork(self.selectedWorkplaceId()).done(function(workplaceBasicWork: WorkplaceBasicWorkFindDto) {
                            self.bindWorkplaceBasicWork(workplaceBasicWork);
                        });
                        blockUI.clear();
                    }).fail((res) => {
                        nts.uk.ui.dialog.alertError(res.message).then(() => { nts.uk.ui.block.clear(); });
                    });
                }).then(() => { 
                    blockUI.clear();
                });
            }


            /**
             * Switch To Company Tab
             */
            switchToCompanyTab(): void {
                var self = this;
                blockUI.invisible();
                self.clearError();
                self.isShowCompanyTab(true);
                self.isShowClassifyTab(false);
                self.isShowWorkplaceTab(false);
                self.findCompanyBasicWork();
                $('#companyBWWorkingDayBtn').focus();
                blockUI.clear();
            }


            /**
             * SwitchTo Workplace Tab
             */
            switchToWorkplaceTab(): void {
                var self = this;
                blockUI.invisible();
                self.clearError();
                self.isShowWorkplaceTab(true);
                self.isShowCompanyTab(false);
                self.isShowClassifyTab(false);
                // Find ardeadySetting
                service.findWorkplaceSetting().done(function(data: any) {
                    var alreadySettingList: Array<UnitAlreadySettingModel> = [];
                    for (var i = 0; i < data.length; i++) {
                        alreadySettingList.push(new UnitAlreadySettingModel(data[i], true));
                    }
                    self.workplaceAlreadySetList(alreadySettingList);
                }).then(function() {
                    // Reload Workplace List
                    $('#workplace-list').ntsTreeComponent(self.workplaceGrid)
                        .done(function() {
                            // Selected Item subscribe
                            self.selectedWorkplaceId.subscribe(function(data: string) {
                                // clear errors
                                self.clearError();
                                
                                blockUI.invisible();
                                if (data) {
                                    // Find WorkplaceBasicWork by WorkplaceId
                                    service.findWorkplaceBasicWork(data).done(function(workplaceBasicWork: WorkplaceBasicWorkFindDto) {
                                        self.bindWorkplaceBasicWork(workplaceBasicWork);
                                    });
                                    // Set Workplace Name.
                                    let tree = $('#workplace-list').getDataList();
                                    self.setWorkplaceName(tree, self.selectedWorkplaceId());
                                } else {
                                    self.workplaceBWWorkingDay(new BasicWorkModel(null, null, null, null));
                                    self.workplaceBWNonInLaw(new BasicWorkModel(null, null, null, null));
                                    self.workplaceBWNonExtra(new BasicWorkModel(null, null, null, null));
                                }
                                blockUI.clear();
                            });

                            $('#workplace-list').focus();
                        });
                });
                
                blockUI.clear();
            }


            /**
             * Switch To Classification Tab
             */
            switchToClassTab(): void {
                var self = this;
                blockUI.invisible();
                self.clearError();
                self.isShowClassifyTab(true);
                self.isShowCompanyTab(false);
                self.isShowWorkplaceTab(false);
                // Find ardeadySetting
                service.findClassifySetting().done(function(data: any) {
                    var areadySettingList: Array<any> = [];
                    for (var i = 0; i < data.length; i++) {
                        areadySettingList.push({ "code": data[i], "isAlreadySetting": true });
                    }
                    self.classifiAlreadySetList(areadySettingList);
                }).then(function() {
                   // Reload Classification List
                        $('#classification-list').ntsListComponent(self.classificationGrid)
                            .done(function() {
                                $('#classification-list').focus();
                            });
                        // SelectedClassification Subscribe
                        self.selectedClassifi.subscribe(function(data: string) {
                            // clear errors
                            self.clearError();
                            
                            blockUI.invisible();
                            if (data) {
                                // Find ClassificationBasicWork
                                service.findClassifyBasicWork(data).done(function(classifyBasicWork: ClassifiBasicWorkFindDto) {
                                    self.bindClassifyBasicWork(classifyBasicWork);
                                });

                                // Set Classification Name
                                var classifyDataList = $('#classification-list').getDataList();
                                var classify = classifyDataList.filter((item) => {
                                    return item.code == self.selectedClassifi();
                                })[0];
                                self.classificationName(classify.name);
                            } else {
                                // If data is null
                                self.classifyBWWorkingDay(new BasicWorkModel(null, null, null, null));
                                self.classifyBWNonInLaw(new BasicWorkModel(null, null, null, null));
                                self.classifyBWNonExtra(new BasicWorkModel(null, null, null, null));
                            }
                            blockUI.clear();
                        });

                    });
                
                blockUI.clear();
            }


            /**
             * Bind Classification Basic Work
             */
            private bindClassifyBasicWork(data: ClassifiBasicWorkFindDto): void {
                var self = this;
                if (!data) {
                    // If data is null
                    self.classifyBWWorkingDay(new BasicWorkModel(null, null, null, null));
                    self.classifyBWNonInLaw(new BasicWorkModel(null, null, null, null));
                    self.classifyBWNonExtra(new BasicWorkModel(null, null, null, null));
                    return;
                }
                // If data is not null
                data.basicWorkSetting.forEach(function(item, index) {
                    switch (item.workDayDivision) {
                        case WorkingDayDivision.WORKING_DAY:
                            self.classifyBWWorkingDay(new BasicWorkModel(item.workTypeCode,
                                item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                            break;

                        case WorkingDayDivision.NON_WORK_INLAW:
                            self.classifyBWNonInLaw(new BasicWorkModel(item.workTypeCode,
                                item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                            break;

                        case WorkingDayDivision.NON_WORK_EXTR:
                            self.classifyBWNonExtra(new BasicWorkModel(item.workTypeCode,
                                item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                            break;
                    }
                });
                self.selectedClassifi(data.classificationCode);
                return;
            }


            /**
             * Bind Workplace Basic Work
             */
            private bindWorkplaceBasicWork(data: WorkplaceBasicWorkFindDto): void {
                var self = this;
                if (!data) {
                    self.workplaceBWWorkingDay(new BasicWorkModel(null, null, null, null));
                    self.workplaceBWNonInLaw(new BasicWorkModel(null, null, null, null));
                    self.workplaceBWNonExtra(new BasicWorkModel(null, null, null, null));
                    return;
                }
                // If Data is not null
                data.basicWorkSetting.forEach(function(item, index) {
                    switch (item.workDayDivision) {
                        case WorkingDayDivision.WORKING_DAY:
                            self.workplaceBWWorkingDay(new BasicWorkModel(item.workTypeCode,
                                item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                            break;

                        case WorkingDayDivision.NON_WORK_INLAW:
                            self.workplaceBWNonInLaw(new BasicWorkModel(item.workTypeCode,
                                item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                            break;

                        case WorkingDayDivision.NON_WORK_EXTR:
                            self.workplaceBWNonExtra(new BasicWorkModel(item.workTypeCode,
                                item.workTypeDisplayName, item.workingCode, item.workingDisplayName));
                            break;
                    }
                });
                self.selectedWorkplaceId(data.workplaceId);
                return;
            }


            /**
             * collect company Basic Work data to save
             */
            private collectCompanyData(): Array<BasicWorkSettingDto> {
                var self = this;
                var basicWorkSettingArray: Array<BasicWorkSettingDto> = [];
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.WORKING_DAY,
                    self.companyBWWorkingDay().worktypeCode(), self.companyBWWorkingDay().workingCode()));
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.NON_WORK_INLAW,
                    self.companyBWNonInLaw().worktypeCode(), self.companyBWNonInLaw().workingCode()));
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.NON_WORK_EXTR,
                    self.companyBWNonExtra().worktypeCode(), self.companyBWNonExtra().workingCode()));
                return basicWorkSettingArray;
            }


            /**
             * collect Workplace Basic Work data to save
             */
            private collectWorkplaceData(): WorkplaceBasicWorkDto {
                var self = this;
                var dto: WorkplaceBasicWorkDto = new WorkplaceBasicWorkDto();
                dto.workplaceId = self.selectedWorkplaceId();
                var basicWorkSettingArray: Array<BasicWorkSettingDto> = [];
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.WORKING_DAY,
                    self.workplaceBWWorkingDay().worktypeCode(), self.workplaceBWWorkingDay().workingCode()));
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.NON_WORK_INLAW,
                    self.workplaceBWNonInLaw().worktypeCode(), self.workplaceBWNonInLaw().workingCode()));
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.NON_WORK_EXTR,
                    self.workplaceBWNonExtra().worktypeCode(), self.workplaceBWNonExtra().workingCode()));
                dto.basicWorkSetting = basicWorkSettingArray;
                return dto;
            }


            /**
             * collect Classification Basic Work data to save
             */
            private collectClassifyData(): ClassificationBasicWorkDto {
                var self = this;
                var dto: ClassificationBasicWorkDto = new ClassificationBasicWorkDto();
                dto.classificationCode = self.selectedClassifi();
                var basicWorkSettingArray: Array<BasicWorkSettingDto> = [];
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.WORKING_DAY,
                    self.classifyBWWorkingDay().worktypeCode(), self.classifyBWWorkingDay().workingCode()));
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.NON_WORK_INLAW,
                    self.classifyBWNonInLaw().worktypeCode(), self.classifyBWNonInLaw().workingCode()));
                basicWorkSettingArray.push(new BasicWorkSettingDto(WorkingDayDivision.NON_WORK_EXTR,
                    self.classifyBWNonExtra().worktypeCode(), self.classifyBWNonExtra().workingCode()));
                dto.basicWorkSetting = basicWorkSettingArray;
                return dto;
            }
        }


        /**
         * Class BasicWorkModel
         */
        export class BasicWorkModel {
            division: number;
            worktypeCode: KnockoutObservable<string>;
            worktypeDisplayName: KnockoutObservable<string>;
            workingCode: KnockoutObservable<string>;
            workingDisplayName: KnockoutObservable<string>;

            selectableWorktypeList: KnockoutObservableArray<string>;
            selectableWorkingList: KnockoutObservableArray<string>;
            worktypeCodes: KnockoutObservableArray<number>;

            constructor(worktypeCode: string, worktypeDisplayName: string, workingCode: string, workingDisplayName: string) {
                this.worktypeCode = ko.observable(worktypeCode);
                this.worktypeDisplayName = ko.observable(worktypeDisplayName);
                this.workingCode = ko.observable(workingCode);
                this.workingDisplayName = ko.observable(workingDisplayName);
                this.worktypeCodes = ko.observableArray([]);
            }

            /**
             * Go to KDL003 
             */
            public gotoDialog(enumVal: number, data): void {
                let self = this;
                blockUI.invisible();
                // Set worktypeCodes
                switch (enumVal) {
                    case 1:
                        self.worktypeCodes([WorkStyle.MORNING_BREAK, WorkStyle.AFTERNOON_BREAK, WorkStyle.ONE_DAY_WORK]);
                        break;

                    case 2:
                        self.worktypeCodes([WorkStyle.MORNING_BREAK, WorkStyle.AFTERNOON_BREAK, WorkStyle.ONE_DAY_REST]);
                        break;

                    case 3:
                        self.worktypeCodes([WorkStyle.MORNING_BREAK, WorkStyle.AFTERNOON_BREAK, WorkStyle.ONE_DAY_REST]);
                        break;

                }
                // Find worktypeCode List and worktimeCode List to Set Parameters to open Dialog KDL003
                $.when(service.findWorktypeCodeList(self.worktypeCodes()), service.findWorktimeCodeList())
                    .done(function(workTypeCodes, workTimes) {
                        let workTimeCodes = workTimes.map(item => item.code);
                        nts.uk.ui.windows.setShared('parentCodes', {
                            selectedWorkTypeCode: self.worktypeCode(),
                            selectedWorkTimeCode: self.workingCode(),
                            workTypeCodes: workTypeCodes,
                            workTimeCodes: workTimeCodes
                        }, true);
                        // onClosed Dialog
                        nts.uk.ui.windows.sub.modal("/view/kdl/003/a/index.xhtml").onClosed(function() {
                            var childData = nts.uk.ui.windows.getShared('childData');
                            // If childData is undefined or selectedWorkTypeCode is undefined
                            if (!childData || !childData.selectedWorkTypeCode) {
                                return;
                            }
                            self.worktypeCode(childData.selectedWorkTypeCode);
                            self.worktypeDisplayName(childData.selectedWorkTypeName);
                            if (childData.selectedWorkTimeCode == '000' || !childData.selectedWorkTimeCode) {
                                self.workingCode(null);
                                self.workingDisplayName(null);
                            } else {
                                self.workingCode(childData.selectedWorkTimeCode);
                                self.workingDisplayName(childData.selectedWorkTimeName);
                            }

                        });
                    });
                blockUI.clear();
            }
        }
        /**
             * Class WorkStyle
             */
        export class WorkStyle {
            static ONE_DAY_REST = 0;
            static MORNING_BREAK = 1;
            static AFTERNOON_BREAK = 2;
            static ONE_DAY_WORK = 4;
        }

        /**
         * Class WorkingDayDivision
         */
        export class WorkingDayDivision {
            static WORKING_DAY = 0;
            static NON_WORK_INLAW = 1;
            static NON_WORK_EXTR = 2;
        }

        /**
         *  Class UnitAlreadySettingModel
         */
        export class UnitAlreadySettingModel {
            workplaceId: string;
            isAlreadySetting: boolean;

            constructor(code: string, isAlreadySetting: boolean) {
                this.workplaceId = code;
                this.isAlreadySetting = isAlreadySetting;
            }
        }

        /**
         * Class ListType
         */
        export class ListType {
            static WORK_PLACE = 1;
            static CLASSIFICATION = 2;
        }

        /**
         * Class SelectionType
         */
        export class SelectionType {
            static SELECT_BY_SELECTED_CODE = 1;
            static SELECT_ALL = 2;
            static SELECT_FIRST_ITEM = 3;
            static NO_SELECT = 4;
        }

        /**
         * Interface UnitModel
         */
        export interface UnitModel {
            code: string;
            name?: string;
            workplaceName?: string;
            isAlreadySetting?: boolean;
        }
    }
}