module nts.uk.at.view.kmk009.a.viewmodel {

    import EnumUse = service.model.Enum;
    import Enum = service.model.Enum;
    import WorkTypeDto = service.model.WorkTypeDto;
    import WorkTimeDto = service.model.WorkTimeDto;
    import TotalTimesDto = service.model.TotalTimesDto;
    import TotalConditionDto = service.model.TotalConditionDto;
    import TotalSubjectsDto = service.model.TotalSubjectsDto;
    import TotalTimesDetailDto = service.model.TotalTimesDetailDto;


    export class ScreenModel {
        itemTotalTimes: KnockoutObservableArray<TotalTimesModel>;
        itemTotalTimesDetail: TotalTimesDetailModel;
        stash: TotalTimesDetailModel;
        totalClsEnums: Array<Enum>;
        totalClsEnumsUse: Array<EnumUse>;
        valueEnum: KnockoutObservable<number>;
        currentCode: KnockoutObservable<string>;
        columns: KnockoutObservableArray<any>;
        useSet: KnockoutObservableArray<any>;
        selectUse: KnockoutObservable<number>;
        enableUse: KnockoutObservable<boolean>;
        enableUpper: KnockoutObservable<boolean>;
        selectUppper: KnockoutObservable<number>;
        selectUnder: KnockoutObservable<number>;
        enableUnder: KnockoutObservable<boolean>;
        enableWorkType: KnockoutObservable<boolean>;
        enableWorkTime: KnockoutObservable<boolean>;
        enableSave: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.itemTotalTimes = ko.observableArray([]);
            self.itemTotalTimesDetail = new TotalTimesDetailModel();
            self.stash = new TotalTimesDetailModel();
            self.totalClsEnums = [];
            self.totalClsEnumsUse = [];
            self.valueEnum = ko.observable(null);
            self.currentCode = ko.observable(null);
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMK009_4'), key: 'totalCountNo', formatter: _.escape, width: 50 },
                { headerText: nts.uk.resource.getText('KMK009_5'), key: 'useAtrName', formatter: _.escape, width: 80 },
                { headerText: nts.uk.resource.getText('KMK009_6'), key: 'totalTimesName', formatter: _.escape, width: 150 },
                { headerText: nts.uk.resource.getText('KMK009_14'), key: 'summaryAtrName', formatter: _.escape, width: 100 }
            ]);
            self.useSet = ko.observableArray([
                { code: '1', name: nts.uk.resource.getText("KMK009_12") },
                { code: '0', name: nts.uk.resource.getText("KMK009_13") },
            ]);
            self.selectUse = ko.observable(0);
            self.selectUppper = ko.observable(0);
            self.selectUnder = ko.observable(0);
            self.enableUse = ko.observable(false);
            self.enableUpper = ko.observable(false);
            self.enableUnder = ko.observable(false);
            self.enableSave = ko.observable(true);
            self.enableWorkType = ko.computed(function() {
                return self.enableUse() && ((self.valueEnum() == self.totalClsEnums[0].value) || (self.valueEnum() == self.totalClsEnums[2].value));
            });

            self.enableWorkTime = ko.computed(function() {
                return self.enableUse() && ((self.valueEnum() == self.totalClsEnums[1].value) || (self.valueEnum() == self.totalClsEnums[2].value));
            });

            //subscribe currentCode
            self.currentCode.subscribe(function(codeChanged) {
                if (!codeChanged || codeChanged < 1) {
                    self.enableSave(false);
                    self.resetData();
                    return;
                }
                self.enableSave(true);
                self.clearError();
                if (codeChanged == 0) { return; }
                self.selectUse(null);
                self.loadAllTotalTimesDetail(codeChanged);
                $('#switch-use').focus();
            });
            //subscribe selectUse
            self.selectUse.subscribe(function(codeChanged) {
                self.loadBySelectUse(self.checkSelectUse(), self.selectUnder(), self.selectUppper());
            });

            //subscribe upper Limit
            self.selectUppper.subscribe(function(isUpper) {
                self.loadBySelectUse(self.checkSelectUse(), self.selectUnder(), isUpper);
            });

            //subscribe under Limit
            self.selectUnder.subscribe(function(isUnder) {
                self.loadBySelectUse(self.checkSelectUse(), isUnder, self.selectUppper());
            });

        }

        /**
         * check select use by use
         */


        private checkSelectUse(): boolean {
            var self = this;
            return ((self.selectUse() === "1") || self.selectUse() === 1);
        }
        /**
         * load data by select use
         */
        private loadBySelectUse(isUse: boolean, isUnder: boolean, isUpper: boolean): void {
            var self = this;
            if (isUse) {
                self.enableUse(true);
                self.itemTotalTimesDetail.useAtr(1);
            }
            else {
                self.enableUse(false);
                self.itemTotalTimesDetail.useAtr(0);
            }
            if (isUnder && self.checkSelectUse()) {
                self.enableUnder(true);
                self.itemTotalTimesDetail.totalCondition.lowerLimitSettingAtr(1);
            } else {
                self.enableUnder(false);
                self.itemTotalTimesDetail.totalCondition.lowerLimitSettingAtr(0);
            }
            if (isUpper && self.checkSelectUse()) {
                self.enableUpper(true);
                self.itemTotalTimesDetail.totalCondition.upperLimitSettingAtr(1);
            } else {
                self.enableUpper(false);
                self.itemTotalTimesDetail.totalCondition.upperLimitSettingAtr(0);
            }
        }

        /**
         * start page
         * get all divergence time
         * get all divergence name
         */
        public startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            // load all data  Enum
            self.loadTotalClsEnum().done(function() {
                self.loadTotalUseEnum().done(function() {
                    self.loadAllTotalTimes().done(() => {
                        self.currentCode(self.itemTotalTimes()[0].totalCountNo);
                        self.loadAllTotalTimesDetail(self.currentCode()).done(() => {
                            if (self.totalClsEnums.length > 0) {
                                self.valueEnum(self.totalClsEnums[self.itemTotalTimesDetail.summaryAtr()].value);
                            }
                            dfd.resolve();
                        });
                    });

                });
            });
            return dfd.promise();
        }

        private loadAllTotalTimes(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            nts.uk.ui.block.invisible();


            service.getAllTotalTimes().done(function(data) {
                nts.uk.ui.block.clear();
                self.itemTotalTimes([]);
                var models: TotalTimesModel[] = [];
                for (var dto of data) {
                    var model = new TotalTimesModel();
                    model.updateData(dto);
                    model.summaryAtrName = self.totalClsEnums[dto.summaryAtr].localizedName;
                    model.useAtrName = self.totalClsEnumsUse[dto.useAtr].localizedName;
                    models.push(model);
                }
                self.itemTotalTimes(models);

                self.itemTotalTimes.valueHasMutated();

                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res);
            }).always(function() {
                nts.uk.ui.block.clear();
            });

            return dfd.promise();
        }

        // loadAllTotalTimesDetail
        private loadAllTotalTimesDetail(codeChanged): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            //            nts.uk.ui.block.invisible();

            service.getAllTotalTimesDetail(codeChanged).done(function(data) {
                //                nts.uk.ui.block.clear();
                if (data) {
                    self.stash.updateData(data);
                    self.itemTotalTimesDetail.updateData(data);
                    self.selectUse(self.itemTotalTimesDetail.useAtr());
                    // disable or enable Upper limit and under linit
                    self.selectUppper(self.itemTotalTimesDetail.totalCondition.upperLimitSettingAtr());
                    if (self.selectUppper() == 1) {
                        self.enableUpper(true);
                    } else {
                        self.enableUpper(false);
                    }
                    self.selectUnder(self.itemTotalTimesDetail.totalCondition.lowerLimitSettingAtr());
                    if (self.selectUnder() == 1) {
                        self.enableUnder(true);
                    } else {
                        self.enableUnder(false);
                    }
                    self.loadListWorkType().done(function() {
                        self.loadListWorkTimes().done(function() {
                            // load all data  Enum
                            self.loadTotalClsEnum().done(function() {
                                if (self.totalClsEnums.length > 0) {
                                    self.valueEnum(self.totalClsEnums[self.itemTotalTimesDetail.summaryAtr()].value);
                                }
                                dfd.resolve();
                            });
                        });
                    });
                }
            });


            return dfd.promise();
        }

        // loadListWorkTypes
        private loadListWorkType(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            nts.uk.ui.block.invisible();

            let lstWorkTypeCd: Array<string> = _.filter(self.itemTotalTimesDetail.listTotalSubjects(), (item) => item.workTypeAtr() == 0)
                .map((item) => item.workTypeCode());

            service.findListByIdWorkTypes(lstWorkTypeCd).done(function(res: Array<WorkTypeDto>) {
                nts.uk.ui.block.clear();

                if (res && res.length > 0) {
                    self.itemTotalTimesDetail.workTypeInfo(res.map(item => item.workTypeCode + ' ' + item.name).join(" ＋ "));
                    self.stash.workTypeInfo(res.map(item => item.workTypeCode + ' ' + item.name).join(" ＋ "));
                } else {
                    self.itemTotalTimesDetail.workTypeInfo(lstWorkTypeCd.join(" ＋ "));
                    self.stash.workTypeInfo(lstWorkTypeCd.join(" ＋ "));
                }
                dfd.resolve();
            });

            return dfd.promise();
        }


        // loadListWorkTimes
        private loadListWorkTimes(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            nts.uk.ui.block.invisible();

            let lstWorkTypeCd: Array<string> = _.filter(self.itemTotalTimesDetail.listTotalSubjects(), (item) => item.workTypeAtr() == 1)
                .map((item) => item.workTypeCode());

            service.findListByIdWorkTimes(lstWorkTypeCd).done(function(res: Array<WorkTimeDto>) {
                nts.uk.ui.block.clear();

                if (res && res.length > 0) {
                    self.itemTotalTimesDetail.workingInfo(res.map(item => item.code + ' ' + item.name).join(" ＋ "));
                    self.stash.workingInfo(res.map(item => item.code + ' ' + item.name).join(" ＋ "));
                } else {
                    self.itemTotalTimesDetail.workingInfo(lstWorkTypeCd.join(" ＋ "));
                    self.stash.workingInfo(lstWorkTypeCd.join(" ＋ "));
                }
                dfd.resolve();
            });

            return dfd.promise();
        }

        // load enum
        private loadTotalClsEnum(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            nts.uk.ui.block.invisible();

            // get setting
            service.getTotalClsEnum().done(function(dataRes: Array<Enum>) {

                self.totalClsEnums = dataRes;

                nts.uk.ui.block.clear();

                dfd.resolve();
            });

            return dfd.promise();
        }

        // load enum enum
        private loadTotalUseEnum(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            nts.uk.ui.block.invisible();

            // get setting
            service.getTotalUseEnum().done(function(dataRes: Array<EnumUse>) {

                self.totalClsEnumsUse = dataRes;

                nts.uk.ui.block.clear();

                dfd.resolve();
            });

            return dfd.promise();
        }



        // save Daily Pattern in database
        public save() {
            let self = this;
            if ($('.nts-input').ntsError('hasError')) {
                return;
            };
            nts.uk.ui.block.invisible();
            //trim() name
            self.itemTotalTimesDetail.totalTimesName($.trim(self.itemTotalTimesDetail.totalTimesName()));
            self.itemTotalTimesDetail.totalTimesABName($.trim(self.itemTotalTimesDetail.totalTimesABName()));
            if (self.itemTotalTimesDetail.countAtr()) {
                self.itemTotalTimesDetail.countAtr(1);
            } else {
                self.itemTotalTimesDetail.countAtr(0);
            }
            // save enum
            self.itemTotalTimesDetail.summaryAtr(self.valueEnum());
            // define dataDto

            // Get data to save
            self.getSaveData();

            service.saveAllTotalTimes(self.itemTotalTimesDetail.toDto()).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    // Focus grid list
                    $('#single-list-dataSource_container').focus();
                    self.loadAllTotalTimes().done(function() {
                        self.loadAllTotalTimesDetail(self.currentCode()).done(function() {
                        });
                    });
                });


            }).fail(function(res) {
                nts.uk.ui.dialog.alertError(res);
            }).always(function() {
                nts.uk.ui.block.clear();
            });
        }
        // openKDL001Dialog
        public openKDL001Dialog() {
            var self = this;
            nts.uk.ui.block.invisible();
            // check worktype or worktime send to KDL001Dialog
            var listWorkType = [];
            var listWorkCode = [];
            for (let i = 0; i < self.itemTotalTimesDetail.listTotalSubjects().length; i++) {
                if (self.itemTotalTimesDetail.listTotalSubjects()[i].workTypeAtr() == 0) {
                    listWorkType[i] = self.itemTotalTimesDetail.listTotalSubjects()[i].workTypeCode();
                } else {
                    listWorkCode[i] = self.itemTotalTimesDetail.listTotalSubjects()[i].workTypeCode();
                }
            }
            service.findAllWorkTimes().done(function(dataRes: Array<WorkTimeDto>) {
                //list All workTime
                let list: Array<string> = dataRes.map(item => item.code);

                nts.uk.ui.windows.setShared('kml001multiSelectMode', true);
                nts.uk.ui.windows.setShared('kml001selectAbleCodeList', list);
                nts.uk.ui.windows.setShared('kml001selectedCodeList', listWorkCode, true);
                nts.uk.ui.windows.sub.modal('/view/kdl/001/a/index.xhtml', { title: nts.uk.resource.getText('KDL001') }).onClosed(function(): any {
                    nts.uk.ui.block.clear();
                    var shareWorkCocde: Array<string> = nts.uk.ui.windows.getShared('kml001selectedCodeList');
                    // deleted data worktype
                    self.itemTotalTimesDetail.listTotalSubjects(_.filter(self.itemTotalTimesDetail.listTotalSubjects(), (item) => item.workTypeAtr() == 0));
                    // insert data worktype
                    for (var item of shareWorkCocde) {
                        self.itemTotalTimesDetail.listTotalSubjects().push(self.toSubjectModel(item, 1));
                    }
                    self.loadListWorkType();
                    self.loadListWorkTimes();
                    if ($('#inpDialog').ntsError("hasError") == true) {
                        $('#inpDialog').ntsError('clear');
                    }
                    $("#itemname").focus();
                });
            });
        }
        //        open KDL002Dialog()
        public openKDL002Dialog() {
            var self = this;
            nts.uk.ui.block.invisible();
            // check worktype or worktime send to KDL002Dialog
            var listWorkType = [];
            var listWorkCode = [];
            for (let i = 0; i < self.itemTotalTimesDetail.listTotalSubjects().length; i++) {
                if (self.itemTotalTimesDetail.listTotalSubjects()[i].workTypeAtr() == 0) {
                    listWorkType[i] = self.itemTotalTimesDetail.listTotalSubjects()[i].workTypeCode();
                } else {
                    listWorkCode[i] = self.itemTotalTimesDetail.listTotalSubjects()[i].workTypeCode();
                }
            }

            service.findAllWorkTypes().done(function(dataRes: Array<WorkTypeDto>) {
                //list All workType
                let list: Array<string> = dataRes.map(item => item.workTypeCode);
                nts.uk.ui.windows.setShared('KDL002_Multiple', true);
                nts.uk.ui.windows.setShared('KDL002_AllItemObj', list);
                nts.uk.ui.windows.setShared('KDL002_SelectedItemId', listWorkType, true);
                nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml', { title: nts.uk.resource.getText('KDL002') }).onClosed(function(): any {
                    nts.uk.ui.block.clear();
                    var shareWorkType: Array<any> = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                    // deleted data worktype
                    self.itemTotalTimesDetail.listTotalSubjects(_.filter(self.itemTotalTimesDetail.listTotalSubjects(), (item) => item.workTypeAtr() == 1));
                    // insert data worktype
                    for (let i = 0; i < shareWorkType.length; i++) {
                        self.itemTotalTimesDetail.listTotalSubjects().push(self.toSubjectModel(shareWorkType[i].code, 0));
                    }
                    self.loadListWorkType();
                    self.loadListWorkTimes();
                    if ($('#inpDialog').ntsError("hasError") == true) {
                        $('#inpDialog').ntsError('clear');
                    }
                    $("#itemname").focus();
                });
            });
        }

        clearError(): void {
            if ($('.nts-validate').ntsError("hasError") == true) {
                $('.nts-validate').ntsError('clear');
            }
            if ($('.nts-editor').ntsError("hasError") == true) {
                $('.nts-input').ntsError('clear');
            }
        }
        /**
         * to init model
         */

        private toSubjectModel(workTypeCode: string, workTypeAtr): TotalSubjectsModel {
            var model: TotalSubjectsModel = new TotalSubjectsModel();
            var dto: TotalSubjectsDto = { workTypeCode: workTypeCode, workTypeAtr: workTypeAtr };
            model.updateData(dto);
            return model;
        }

        /**
         * reset data by not selected 
         */
        private resetData(): void {
            var self = this;
            self.enableUse(false);
            self.itemTotalTimesDetail.resetData();
        }

        /**
         * Get save data
         */
        private getSaveData(): void {
            let self = this;

            // Get data from current data model.
            let saveData: TotalTimesDetailModel = self.itemTotalTimesDetail;

            // Get reserved data from stash if input is disabled.
            if (!self.enableWorkType()) {

                let lstWorkCd: Array<TotalSubjectsModel> = _.filter(saveData.listTotalSubjects(), (item) => item.workTypeAtr() == 1);

                saveData.listTotalSubjects(_.filter(self.stash.listTotalSubjects(), (item) => item.workTypeAtr() == 0));

                for (var i = 0; i < lstWorkCd.length; i++) {
                    saveData.listTotalSubjects.push(lstWorkCd[i]);
                }
                saveData.workTypeInfo(self.stash.workTypeInfo());
            }
            if (!self.enableWorkTime()) {
                let lstWorkCd: Array<TotalSubjectsModel> = _.filter(saveData.listTotalSubjects(), (item) => item.workTypeAtr() == 0);

                saveData.listTotalSubjects(_.filter(self.stash.listTotalSubjects(), (item) => item.workTypeAtr() == 1));

                for (var i = 0; i < lstWorkCd.length; i++) {
                    saveData.listTotalSubjects.push(lstWorkCd[i]);
                }

                saveData.workingInfo(self.stash.workingInfo());
            }
            if (!self.enableUpper()) {
                saveData.totalCondition.thresoldUpperLimit(self.stash.totalCondition.thresoldUpperLimit());
            }
            if (!self.enableUnder()) {
                saveData.totalCondition.thresoldLowerLimit(self.stash.totalCondition.thresoldLowerLimit());
            }
            if (!self.enableUse()) {
                saveData.updateData(self.stash.toDto());
                saveData.useAtr(0);
            }
        }

    }





    export class TotalTimesModel {
        totalCountNo: number;
        summaryAtr: number;
        useAtr: number;
        totalTimesName: string;
        summaryAtrName: string;
        useAtrName: string
        constructor() {
            this.totalCountNo = 1;
            this.summaryAtr = 1;
            this.useAtr = 1;
            this.totalTimesName = null;
            this.summaryAtrName = null;
            this.useAtrName = null;
        }

        updateData(dto: TotalTimesDto) {
            this.totalCountNo = dto.totalCountNo;
            this.summaryAtr = dto.summaryAtr;
            this.useAtr = dto.useAtr;
            this.totalTimesName = dto.totalTimesName;
        }
    }

    export class TotalTimesDetailModel {
        totalCountNo: number;
        countAtr: KnockoutObservable<number>;
        useAtr: KnockoutObservable<number>;
        totalTimesName: KnockoutObservable<string>;
        totalTimesABName: KnockoutObservable<string>;
        summaryAtr: KnockoutObservable<number>;
        totalCondition: TotalConditionModel;
        listTotalSubjects: KnockoutObservableArray<TotalSubjectsModel>;
        workTypeInfo: KnockoutObservable<string>;
        workingInfo: KnockoutObservable<string>;


        constructor() {
            this.totalCountNo = 1;
            this.countAtr = ko.observable(1);
            this.useAtr = ko.observable(1);
            this.totalTimesName = ko.observable('');
            this.totalTimesABName = ko.observable('');
            this.summaryAtr = ko.observable(1);
            this.totalCondition = new TotalConditionModel();
            this.listTotalSubjects = ko.observableArray([]);
            this.workTypeInfo = ko.observable(null);
            this.workingInfo = ko.observable(null);

        }

        updateData(dto: TotalTimesDetailDto) {
            this.totalCountNo = dto.totalCountNo;
            this.countAtr(dto.countAtr);
            this.useAtr(dto.useAtr);
            this.totalTimesName(dto.totalTimesName);
            this.totalTimesABName(dto.totalTimesABName);
            this.totalCondition.updateData(dto.totalCondition);
            this.summaryAtr(dto.summaryAtr);
            this.listTotalSubjects([]);
            var listTotalSubjectsUpdate: TotalSubjectsModel[] = [];
            for (var item of dto.listTotalSubjects) {
                var model: TotalSubjectsModel = new TotalSubjectsModel();
                model.updateData(item);
                listTotalSubjectsUpdate.push(model);
            }
            this.listTotalSubjects(listTotalSubjectsUpdate);

        }

        toDto(): TotalTimesDetailDto {
            var listTotalSubjectsDto: TotalSubjectsDto[] = [];
            for (var model of this.listTotalSubjects()) {
                listTotalSubjectsDto.push(model.toDto());
            }
            var dto: TotalTimesDetailDto = {
                totalCountNo: this.totalCountNo,
                countAtr: this.countAtr(),
                useAtr: this.useAtr(),
                totalTimesName: this.totalTimesName(),
                totalTimesABName: this.totalTimesABName(),
                totalCondition: this.totalCondition.toDto(),
                summaryAtr: this.summaryAtr(),
                listTotalSubjects: listTotalSubjectsDto
            };
            return dto;
        }
        resetData() {
            this.totalCountNo = 0;
            this.countAtr(0);
            this.useAtr(0);
            this.totalTimesName('');
            this.totalTimesABName('');
            this.totalCondition.resetData();
            this.summaryAtr(0);
            this.listTotalSubjects([]);
            this.workTypeInfo('');
            this.workingInfo('');
        }
    }

    export class TotalConditionModel {
        upperLimitSettingAtr: KnockoutObservable<number>;
        lowerLimitSettingAtr: KnockoutObservable<number>;
        thresoldUpperLimit: KnockoutObservable<number>;
        thresoldLowerLimit: KnockoutObservable<number>;
        constructor() {
            this.upperLimitSettingAtr = ko.observable(1);
            this.lowerLimitSettingAtr = ko.observable(1);
            this.thresoldUpperLimit = ko.observable(1);
            this.thresoldLowerLimit = ko.observable(1);
        }
        updateData(dto: TotalConditionDto) {
            this.upperLimitSettingAtr(dto.upperLimitSettingAtr);
            this.lowerLimitSettingAtr(dto.lowerLimitSettingAtr);
            this.thresoldUpperLimit(dto.thresoldUpperLimit);
            this.thresoldLowerLimit(dto.thresoldLowerLimit);
        }

        toDto(): TotalConditionDto {
            var dto: TotalConditionDto = {
                upperLimitSettingAtr: this.upperLimitSettingAtr(),
                lowerLimitSettingAtr: this.lowerLimitSettingAtr(),
                thresoldUpperLimit: this.thresoldUpperLimit(),
                thresoldLowerLimit: this.thresoldLowerLimit()
            };
            return dto;
        }
        resetData() {
            this.upperLimitSettingAtr(0);
            this.lowerLimitSettingAtr(0);
            this.thresoldUpperLimit(0);
            this.thresoldLowerLimit(0);
        }
    }

    export class TotalSubjectsModel {
        workTypeCode: KnockoutObservable<string>;
        workTypeAtr: KnockoutObservable<number>;

        constructor() {
            this.workTypeCode = ko.observable('');
            this.workTypeAtr = ko.observable(1);
        }
        updateData(dto: TotalSubjectsDto) {
            this.workTypeCode(dto.workTypeCode);
            this.workTypeAtr(dto.workTypeAtr);
        }
        toDto(): TotalSubjectsDto {
            var dto: TotalSubjectsDto = {
                workTypeCode: this.workTypeCode(),
                workTypeAtr: this.workTypeAtr()
            };
            return dto;
        }
    }

}