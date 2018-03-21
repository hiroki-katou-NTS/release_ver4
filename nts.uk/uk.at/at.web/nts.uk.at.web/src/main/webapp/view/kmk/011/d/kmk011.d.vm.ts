module nts.uk.at.view.kmk011.d {

    import viewModelScreenE = nts.uk.at.view.kmk011.e.viewmodel;

    import CompanyDivergenceReferenceTimeHistoryDto = nts.uk.at.view.kmk011.d.model.CompanyDivergenceReferenceTimeHistoryDto;
    import ComDivergenceTimeSettingDto = nts.uk.at.view.kmk011.d.model.ComDivergenceTimeSettingDto;
    import DivergenceTimeDto = nts.uk.at.view.kmk011.d.model.DivergenceTimeDto;
    import ComDivergenceRefTimeSaveCommand = nts.uk.at.view.kmk011.d.model.ComDivergenceRefTimeSaveCommand;
    import ComDivergenceRefTimeSaveDto = nts.uk.at.view.kmk011.d.model.ComDivergenceRefTimeSaveDto;

    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import blockUI = nts.uk.ui.block;

    export module viewmodel {

        export class ScreenModel {
            screenE: KnockoutObservable<any>;

            useUnitSetting: KnockoutObservable<boolean>;
            enableSaveDivergenceRefSetting: KnockoutObservable<boolean>;

            //divergence time setting
            roundingRules: KnockoutObservableArray<any>;
            required: KnockoutObservable<boolean>;
            enable: KnockoutObservable<boolean>;
            mapObj: Map<number, ComDivergenceTimeSettingDto>;
            mapObj2: Map<number, DivergenceTimeDto>;

            //history screen
            enable_button_creat: KnockoutObservable<boolean>;
            enable_button_edit: KnockoutObservable<boolean>;
            enable_button_delete: KnockoutObservable<boolean>;
            histList: KnockoutObservableArray<CompanyDivergenceReferenceTimeHistoryDto[]>;
            histName: KnockoutObservable<string>;
            selectedHist: KnockoutObservable<string>;
            isEnableListHist: KnockoutObservable<boolean>;

            constructor() {
                var _self = this;
                _self.screenE = ko.observable(new viewModelScreenE.ScreenModel());

                _self.useUnitSetting = ko.observable(true);
                _self.enableSaveDivergenceRefSetting = ko.observable(true);

                //divergence time setting
                _self.roundingRules = ko.observableArray([
                    { code: 0, name: nts.uk.resource.getText('Enum_UseAtr_NotUse') },
                    { code: 1, name: nts.uk.resource.getText('Enum_UseAtr_Use') }
                ]);
                _self.enable = ko.observable(true);
                _self.required = ko.observable(true);
                _self.mapObj = new Map<number, ComDivergenceTimeSettingDto>();
                _self.mapObj2 = new Map<number, DivergenceTimeDto>();

                //history screen
                _self.enable_button_creat = ko.observable(true);
                _self.enable_button_edit = ko.observable(true);
                _self.enable_button_delete = ko.observable(true);
                _self.histList = ko.observableArray([]);
                _self.histName = ko.observable('');
                _self.selectedHist = ko.observable(null)
                _self.isEnableListHist = ko.observable(true);

                _self.selectedHist.subscribe((value) => {
                    if (nts.uk.text.isNullOrEmpty(value)) {
                        _self.enable_button_edit(false);
                        _self.enable_button_delete(false);
                        _self.enableSaveDivergenceRefSetting(false);
                    } else {
                        _self.enable_button_edit(true);
                        _self.enable_button_delete(true);
                        _self.isEnableListHist(true);
                        _self.enableSaveDivergenceRefSetting(true);
                    }
                    _self.fillListItemSetting(value).done(() => {

                    });
                });
            }

            public start_page(typeStart: number): JQueryPromise<any> {
                let _self = this;
                var dfd = $.Deferred<any>();

                // load all
                if (typeStart == SideBarTabIndex.FIRST) {
                    nts.uk.ui.errors.clearAll()
                    blockUI.grayout();
                    $.when(_self.fillListHistory(), _self.findAllManageUseUnit(), _self.fillListDivergenceTime()).done(function() {
                        dfd.resolve(_self);
                        blockUI.clear();

                    });
                } else {
                    // Process for screen E (Mother of all screen)
                    nts.uk.ui.errors.clearAll()
                    blockUI.grayout();
                    _self.screenE().start_page().done(function() {
                        dfd.resolve(_self);
                        blockUI.clear();
                    });
                }
                return dfd.promise();
            }

            /**
             * save divergence reference setting
             */
            public saveDivergenceRefSetting() {
                let _self = this;
                var dfd = $.Deferred<any>();

                for (let i = 1; i <= 10; i++) {
                    if (_self.mapObj.get(i).notUseAtr() == DivergenceTimeUseSet.USE) {
                        $('#alarm_time_' + i).ntsError('set', { messageId: "Msg_913" });
                        //                        $('#error_time_' + i).ntsEditor("validate");   
                    }

                }

                if (_self.hasError()) {
                    return;
                }

                let arrDto: any = [];

                _self.mapObj.forEach((value: ComDivergenceTimeSettingDto, key: number) => {

                    if (_self.isDisableAllRow(key)) {
                        let commandDto = new ComDivergenceRefTimeSaveDto(
                            value.divergenceTimeNo(),
                            value.notUseAtr(),
                            _self.selectedHist(),
                            value.alarmTime(),
                            value.errorTime()
                        );
                        arrDto.push(commandDto);
                    }
                });

                var data = new ComDivergenceRefTimeSaveCommand(arrDto);

                service.save(data).done(() => {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                }).fail((res: any) => {
                    _self.showMessageError(res);
                });
            }

            /**
             * check enable or disable divergence reference time setting
             */
            public isDisableAllRow(diverNo: number): boolean {
                let _self = this;
                if (_self.mapObj2.get(diverNo).divergenceTimeUseSet == DivergenceTimeUseSet.NOT_USE || _self.selectedHist() == null) {
                    return false;
                }
                return true;
            }

            /**
             * check enable or disable alarm time && error time
             */
            public checkStatusEnable(diverNo: number): boolean {
                let _self = this;
                if (_self.mapObj2.get(diverNo).divergenceTimeUseSet == DivergenceTimeUseSet.NOT_USE) {
                    return false;
                } else {
                    if (_self.mapObj.get(diverNo).notUseAtr() == DivergenceTimeUseSet.NOT_USE) {
                        return false;
                    }
                    return true;
                }
            }

            /**
             * showMessageError
             */
            public showMessageError(res: any) {
                let dfd = $.Deferred<any>();

                // check error business exception
                if (!res.businessException) {
                    return;
                }

                // show error message
                if (Array.isArray(res.errors)) {
                    nts.uk.ui.dialog.bundledErrors(res);
                } else {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                }
            }

            /**
             * Check Errors all input.
             */
            private hasError(): boolean {
                let _self = this;
                _self.clearErrors();
                for (let i = 1; i <= 10; i++) {
                    if (_self.mapObj.get(i).notUseAtr() == DivergenceTimeUseSet.USE) {
                        $('#alarm_time_' + i).ntsEditor("validate");
                        $('#error_time_' + i).ntsEditor("validate");
                    }

                }
                if ($('.nts-input').ntsError('hasError')) {
                    return true;
                }
                return false;
            }

            /**
             * Clear Errors
             */
            private clearErrors(): void {
                let _self = this;
                // Clear errors
                for (let i = 1; i <= 10; i++) {
                    if (_self.mapObj.get(i).notUseAtr() == DivergenceTimeUseSet.USE) {
                        $('#alarm_time_' + i).ntsEditor("clear");
                        $('#error_time_' + i).ntsEditor("clear");
                    }
                }

                // Clear error inputs
                $('.nts-input').ntsError('clear');
            }

            /**
             * find list divergence reference time by his
             */
            private fillListItemSetting(value: string): JQueryPromise<any> {
                let _self = this;
                var dfd = $.Deferred<any>();
                let dto: ComDivergenceTimeSettingDto;
                service.getAllItemSetting(value).done((response: any) => {
                    if (response != null) {
                        if (_self.mapObj.size == 0) {
                            response.forEach((item: any) => {
                                dto = new ComDivergenceTimeSettingDto();
                                dto.updateData(item);
                                _self.mapObj.set(item.divergenceTimeNo, dto);
                            });
                        } else {
                            response.forEach((item: any) => {
                                _self.mapObj.get(item.divergenceTimeNo).notUseAtr(item.notUseAtr);
                                _self.mapObj.get(item.divergenceTimeNo).alarmTime(item.divergenceReferenceTimeValue.alarmTime);
                                _self.mapObj.get(item.divergenceTimeNo).errorTime(item.divergenceReferenceTimeValue.errorTime);
                            });
                        }
                    } else {
                        _self.fillListItemSettingDefault();
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
             * fill list divergence time
             */
            private fillListDivergenceTime(): JQueryPromise<any> {
                let _self = this;
                var dfd = $.Deferred<any>();
                let objTemp2: DivergenceTimeDto;
                service.getAllDivergenceTime().done((response: any) => {
                    if (response != null) {
                        response.forEach((item1: any) => {
                            objTemp2 = new DivergenceTimeDto(item1.divergenceTimeNo, item1.divergenceTimeName, item1.divergenceTimeUseSet);
                            _self.mapObj2.set(item1.divergenceTimeNo, objTemp2);
                        });
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
             * fill list history 
             */
            private fillListHistory(): JQueryPromise<any> {
                let _self = this;
                var dfd = $.Deferred<any>();
                var historyData: any = [];
                var textDisplay = "";

                //fill list history
                service.getAllHistory().done((response: any) => {
                    if (response != null) {
                        response.forEach(function(item: CompanyDivergenceReferenceTimeHistoryDto) {
                            textDisplay = item.startDate + " " + nts.uk.resource.getText("CMM011_26") + " " + item.endDate;
                            historyData.push(new HistModel(item.historyId, textDisplay));
                        });
                        _self.selectedHist(historyData[0].historyId)
                        _self.isEnableListHist(true);
                        _self.enable_button_edit(true);
                        _self.enable_button_delete(true);
                        _self.fillListItemSetting(historyData[0].historyId).done(() => {
                            dfd.resolve();
                        });
                    } else {
                        _self.enable_button_edit(false);
                        _self.enable_button_delete(false);
                        _self.isEnableListHist(false);
                        _self.enableSaveDivergenceRefSetting(false);
                        _self.fillListItemSettingDefault();
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_1058" });
                        blockUI.clear();
                        dfd.resolve();
                    }

                    _self.histList(historyData);

                });

                return dfd.promise();
            }

            /**
             * create list divergence reference time default
             */
            private fillListItemSettingDefault(): void {
                let _self = this;

                let objTemp1: ComDivergenceTimeSettingDto;

                for (let i = 1; i <= 10; i++) {
                    objTemp1 = new ComDivergenceTimeSettingDto();
                    let item = {
                        divergenceTimeNo: i,
                        notUseAtr: 0,
                        divergenceReferenceTimeValue: {
                            errorTime: 0,
                            alarmTime: 0
                        }
                    };
                    objTemp1.updateData(item);
                    _self.mapObj.set(i, objTemp1);
                }

            }

            private findAllManageUseUnit(): JQueryPromise<any> {
                let _self = this;
                var dfd = $.Deferred<any>();
                service.getUseUnitSetting().done((response) => {
                    _self.useUnitSetting(response.workTypeUseSet);
                });
                dfd.resolve();
                return dfd.promise();
            }

            private isDisableTab(): boolean {
                return false;
            }

            /**
            * on select tab handle
            */
            public onSelectTabOne(): void {
                $("#sidebar").ntsSideBar("init", {
                    active: SideBarTabIndex.FIRST,
                    activate: (event: any, info: any) => {
                        let _self = this;
                        _self.start_page(SideBarTabIndex.FIRST);
                    }
                });
            }
            public onSelectTabTwo(): void {
                let _self = this;
                if (_self.isDisableTab() == false) {
                    $("#sidebar").ntsSideBar("init", {
                        active: SideBarTabIndex.SECOND,
                        activate: (event: any, info: any) => {
                            _self.start_page(SideBarTabIndex.SECOND);
                        }
                    });
                }
            }

            // history mode
            public createMode(): void {
                let _self = this;
                nts.uk.ui.windows.setShared('settingMode', viewModelScreenE.HistorySettingMode.COMPANY);
                nts.uk.ui.windows.sub.modal("/view/kmk/011/f/index.xhtml").onClosed(function() {
                    _self.start_page(SideBarTabIndex.FIRST);
                });
            }
            public editMode(): void {
                let _self = this;
                nts.uk.ui.windows.setShared('settingMode', viewModelScreenE.HistorySettingMode.COMPANY);
                nts.uk.ui.windows.setShared('history', _self.selectedHist());
                nts.uk.ui.windows.sub.modal("/view/kmk/011/g/index.xhtml").onClosed(function() {
                    _self.start_page(SideBarTabIndex.FIRST);
                });
            }
            public deleteMode(): void {
                let _self = this;
                var command: any = {};
                command.historyId = _self.selectedHist()
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.deleteHistory(command).done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                            _self.start_page(SideBarTabIndex.FIRST);
                        });
                    });
                });
            }
        }

        export enum SideBarTabIndex {
            FIRST = 0,
            SECOND = 1,
        }

        export enum DivergenceTimeUseSet {
            NOT_USE = 0,
            USE = 1,
        }

        export class HistModel {
            historyId: string;
            textDisplay: string;

            constructor(historyId: string, textDisplay: string) {
                this.historyId = historyId;
                this.textDisplay = textDisplay;
            }
        }

    }
}