module nts.uk.pr.view.qmm011.a {

    //import data class .... BEGIN
    import option = nts.uk.ui.option;
    import RoundingMethodDto = service.model.RoundingMethodDto;
    import HistoryInsuranceInDto = service.model.HistoryInsuranceInDto;
    import MonthRange = service.model.MonthRange;
    import YearMonth = service.model.YearMonth;
    import UnemployeeInsuranceRateItemSettingDto = service.model.UnemployeeInsuranceRateItemSettingDto;
    import UnemployeeInsuranceRateItemDto = service.model.UnemployeeInsuranceRateItemDto;
    import UnemployeeInsuranceHistoryUpdateDto = service.model.UnemployeeInsuranceHistoryUpdateDto;
    import AccidentInsuranceHistoryUpdateDto = service.model.AccidentInsuranceHistoryUpdateDto;
    import UnemployeeInsuranceDeleteDto = service.model.UnemployeeInsuranceDeleteDto;
    import AccidentInsuranceRateDeleteDto = service.model.AccidentInsuranceRateDeleteDto;
    import CareerGroupDto = service.model.CareerGroupDto;
    import AccidentInsuranceRateDto = service.model.AccidentInsuranceRateDto;
    import BusinessTypeEnumDto = service.model.BusinessTypeEnumDto;
    import InsuranceBusinessType = service.model.InsuranceBusinessType;
    import TypeHistory = service.model.TypeHistory;
    import UnemployeeInsuranceRateDto = service.model.UnemployeeInsuranceRateDto;
    import UnemployeeInsuranceRateCopyDto = service.model.UnemployeeInsuranceRateCopyDto;
    import AccidentInsuranceHistoryDto = service.model.AccidentInsuranceHistoryDto;
    import AccidentInsuranceRateHistoryFindInDto = service.model.AccidentInsuranceRateHistoryFindInDto;
    import AccidentInsuranceRateCopyDto = service.model.AccidentInsuranceRateCopyDto;
    import InsuBizRateItemDto = service.model.InsuBizRateItemDto;
    import TypeActionInsuranceRate = service.model.TypeActionInsuranceRate;
    import InsuranceBusinessTypeDto = service.model.InsuranceBusinessTypeDto;
    import InsuranceBusinessTypeUpdateModel = nts.uk.pr.view.qmm011.e.viewmodel.InsuranceBusinessTypeUpdateModel;
    import NewHistoryScreenOption = nts.uk.pr.view.base.simplehistory.newhistory.viewmodel.NewHistoryScreenOption;
    import UpdateHistoryScreenOption = nts.uk.pr.view.base.simplehistory.updatehistory.viewmodel.UpdateHistoryScreenOption;
    import HistoryModel = nts.uk.pr.view.base.simplehistory.model.HistoryModel;
    import ScreenMode = nts.uk.pr.view.base.simplehistory.dialogbase.ScreenMode;
    //import data class ... END

    export module viewmodel {

        export class ScreenModel {

            selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>;
            //Update or Add To service 1 add 2 update
            typeActionUnemployeeInsurance: KnockoutObservable<number>;
            //雇用保険 detail B
            lstUnemployeeInsuranceRateHistory: KnockoutObservableArray<HistoryInsuranceInDto>;
            unemployeeInsuranceRateModel: KnockoutObservable<UnemployeeInsuranceRateModel>;
            rateInputOptions: KnockoutObservable<nts.uk.ui.option.NumberEditorOption>;
            selectionUnemployeeInsuranceRateHistory: KnockoutObservable<string>;
            preSelectUnemployeeInsuranceRateHistory: KnockoutObservable<string>;
            beginHistoryStartUnemployeeInsuranceRate: KnockoutObservable<string>;
            isEmptyUnemployee: KnockoutObservable<boolean>;
            //労災保険 detail C
            lstAccidentInsuranceRateHistory: KnockoutObservableArray<AccidentInsuranceHistoryDto>;
            selectionAccidentInsuranceRateHistory: KnockoutObservable<string>;
            preSelectAccidentInsuranceRateHistory: KnockoutObservable<string>;
            beginHistoryStartAccidentInsuranceRate: KnockoutObservable<string>;
            //detail D
            accidentInsuranceRateModel: KnockoutObservable<AccIRModel>;
            //Update or Add  typeAccidentInsurance: KnockoutObservable<number>;
            typeActionAccidentInsurance: KnockoutObservable<number>;
            textEditorOption: KnockoutObservable<nts.uk.ui.option.TextEditorOption>;
            isEnable: KnockoutObservable<boolean>;
            isEnableSaveUnemployeeInsurance: KnockoutObservable<boolean>;
            isShowDirtyUnemployeeInsurance: KnockoutObservable<boolean>;
            isEnableEditUnemployeeInsurance: KnockoutObservable<boolean>;
            isEnableSaveActionAccidentInsurance: KnockoutObservable<boolean>;
            isEnableEditActionAccidentInsurance: KnockoutObservable<boolean>;
            isShowDirtyActionAccidentInsurance: KnockoutObservable<boolean>;
            isEmptyAccident: KnockoutObservable<boolean>;
            messageList: KnockoutObservableArray<any>;
            dirtyUnemployeeInsurance: nts.uk.ui.DirtyChecker;
            dirtyAccidentInsurance: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                self.selectionRoundingMethod = ko.observableArray<RoundingMethodDto>([new RoundingMethodDto(0, "切り捨て"),//Truncation 
                    new RoundingMethodDto(1, "切り上げ"),//RoundUp
                    new RoundingMethodDto(2, "四捨五入"),//Down4_Up5
                    new RoundingMethodDto(3, "五捨六入"),//Down5_Up6
                    new RoundingMethodDto(4, "五捨五超入")]);//Round Down
                self.rateInputOptions = ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                    grouplength: 3,
                    decimallength: 2
                }));
                self.isEnable = ko.observable(true);
                self.textEditorOption = ko.mapping.fromJS(new option.TextEditorOption());
                self.typeActionUnemployeeInsurance = ko.observable(TypeActionInsuranceRate.add);
                self.typeActionAccidentInsurance = ko.observable(TypeActionInsuranceRate.add);
                self.isEmptyUnemployee = ko.observable(true);
                self.isEmptyAccident = ko.observable(true);
                self.accidentInsuranceRateModel = ko.observable(new AccIRModel(self.rateInputOptions, self.selectionRoundingMethod));
                self.unemployeeInsuranceRateModel = ko.observable(new UnemployeeInsuranceRateModel(self.rateInputOptions, self.selectionRoundingMethod));
                self.lstUnemployeeInsuranceRateHistory = ko.observableArray<HistoryInsuranceInDto>([]);
                self.lstAccidentInsuranceRateHistory = ko.observableArray<AccidentInsuranceHistoryDto>([]);
                self.selectionUnemployeeInsuranceRateHistory = ko.observable('');
                self.selectionAccidentInsuranceRateHistory = ko.observable('');
                self.isEnableSaveUnemployeeInsurance = ko.observable(true);
                self.isEnableEditUnemployeeInsurance = ko.observable(true);
                self.isEnableSaveActionAccidentInsurance = ko.observable(true);
                self.isEnableEditActionAccidentInsurance = ko.observable(true);
                self.beginHistoryStartUnemployeeInsuranceRate = ko.observable('');
                self.beginHistoryStartAccidentInsuranceRate = ko.observable('');
                self.messageList = ko.observableArray([
                    { messageId: "ER001", message: "＊が入力されていません。" },
                    { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                    { messageId: "AL001", message: "変更された内容が登録されていません。\r\n よろしいですか。" },
                    { messageId: "ER010", message: "対象データがありません。" },
                    { messageId: "AL002", message: "データを削除します。\r\n よろしいですか？。" }
                ]);
                self.dirtyUnemployeeInsurance = new nts.uk.ui.DirtyChecker(self.unemployeeInsuranceRateModel);
                self.dirtyAccidentInsurance = new nts.uk.ui.DirtyChecker(self.accidentInsuranceRateModel);
                self.isShowDirtyUnemployeeInsurance = ko.observable(true);
                self.isShowDirtyActionAccidentInsurance = ko.observable(true);
                self.preSelectUnemployeeInsuranceRateHistory = ko.observable('');
                self.preSelectAccidentInsuranceRateHistory = ko.observable('');
            }

            //open dialog edit UnemployeeInsuranceRateHistory => show view model xhtml (action event add)
            private openEditUnemployeeInsuranceRateHistory() {
                //set info
                var self = this;
                if (self.dirtyUnemployeeInsurance.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                        self.onShowEditUnemployeeInsuranceRateHistory();
                    }).ifNo(function() {
                        //No action
                    });
                    return;
                }
                self.onShowEditUnemployeeInsuranceRateHistory();
            }

            //open dialog edit UnemployeeInsuranceRateHistory => show view model xhtml (action event add)
            private onShowEditUnemployeeInsuranceRateHistory() {

                var self = this;
                service.findUnemployeeInsuranceRateHistory(self.selectionUnemployeeInsuranceRateHistory()).done(data => {
                    var history: HistoryModel;
                    var endMonth: number = data.endMonth;
                    var name: string = '雇用保険料率';
                    history = {
                        uuid: self.selectionUnemployeeInsuranceRateHistory(),
                        start: data.startMonth,
                        end: endMonth
                    };

                    var newHistoryOptions: UpdateHistoryScreenOption = {
                        screenMode: ScreenMode.MODE_HISTORY_ONLY,
                        name: name,
                        master: null,
                        history: history,
                        removeMasterOnLastHistoryRemove: false,

                        // Delete callback.
                        onDeleteCallBack: (data) => {
                            var unemployeeInsuranceDeleteDto: UnemployeeInsuranceDeleteDto;
                            unemployeeInsuranceDeleteDto = new UnemployeeInsuranceDeleteDto();
                            unemployeeInsuranceDeleteDto.code = data.historyId;
                            unemployeeInsuranceDeleteDto.version = 0;
                            service.deleteUnemployeeInsurance(unemployeeInsuranceDeleteDto).done(data => {
                                self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                                self.reloadDataUnemployeeInsuranceRateByAction();
                            }).fail(function(error) {
                                self.showMessageSaveUnemployeeInsurance(error.message)
                            });
                        },

                        // Update call back.
                        onUpdateCallBack: (data) => {
                            var dfd = $.Deferred<any>();
                            var unemployeeInsuranceHistoryUpdateDto: UnemployeeInsuranceHistoryUpdateDto;
                            unemployeeInsuranceHistoryUpdateDto = new UnemployeeInsuranceHistoryUpdateDto();
                            unemployeeInsuranceHistoryUpdateDto.historyId = data.historyId;
                            unemployeeInsuranceHistoryUpdateDto.startMonth = data.startYearMonth;
                            unemployeeInsuranceHistoryUpdateDto.endMonth = endMonth;
                            service.updateUnemployeeInsuranceRateHistory(unemployeeInsuranceHistoryUpdateDto).done(() => {
                                self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.update);
                                self.reloadDataUnemployeeInsuranceRateByAction();
                                dfd.resolve();
                            }).fail(function(error) {
                                dfd.reject(error);
                            });
                            return dfd.promise();
                        }
                    };

                    nts.uk.ui.windows.setShared('options', newHistoryOptions);
                    var ntsDialogOptions = {
                        title: nts.uk.text.format('{0}の登録 > 履歴の編集', name),
                        dialogClass: 'no-close'
                    };
                    nts.uk.ui.windows.sub.modal('/view/base/simplehistory/updatehistory/index.xhtml', ntsDialogOptions);

                });
            }

            //open dialog add UnemployeeInsuranceRateHistory => show view model xhtml (action event add)
            private openAddUnemployeeInsuranceRateHistory() {
                //set info
                var self = this;
                if (self.dirtyUnemployeeInsurance.isDirty() && !self.isEmptyUnemployee()) {
                    nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                        self.onShowAddUnemployeeInsuranceRateHistory();
                    }).ifNo(function() {
                        //No action
                    });
                    return;
                }
                self.onShowAddUnemployeeInsuranceRateHistory();
            }

            //open dialog add UnemployeeInsuranceRateHistory => show view model xhtml (action event add)
            private onShowAddUnemployeeInsuranceRateHistory() {
                var self = this;
                var lastest: HistoryModel;
                var name: string = '雇用保険料率';
                lastest = {
                    uuid: self.selectionUnemployeeInsuranceRateHistory(),
                    start: self.unemployeeInsuranceRateModel().unemployeeInsuranceHistoryModel.startMonth(),
                    end: self.unemployeeInsuranceRateModel().unemployeeInsuranceHistoryModel.endMonth()
                };
                if (self.isEmptyUnemployee()) {
                    lastest = undefined;
                }
                //set info history
                var unemployeeInsuranceRateCopyDto: UnemployeeInsuranceRateCopyDto;
                unemployeeInsuranceRateCopyDto = new UnemployeeInsuranceRateCopyDto();
                unemployeeInsuranceRateCopyDto.historyIdCopy = self.selectionUnemployeeInsuranceRateHistory();
                //set info option
                var newHistoryOptions: NewHistoryScreenOption = {
                    screenMode: ScreenMode.MODE_HISTORY_ONLY,
                    name: name,
                    master: null,
                    lastest: lastest,

                    // Copy.
                    onCopyCallBack: (data) => {
                        var dfd = $.Deferred<any>();
                        unemployeeInsuranceRateCopyDto.startMonth = data.startYearMonth;
                        unemployeeInsuranceRateCopyDto.addNew = false;
                        service.copyUnemployeeInsuranceRate(unemployeeInsuranceRateCopyDto).done(function() {
                            self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                            self.reloadDataUnemployeeInsuranceRateByAction();
                            self.clearErrorSaveUnemployeeInsurance();
                            dfd.resolve();
                        }).fail(function(error) {
                            dfd.reject(error);
                        });
                        return dfd.promise();
                    },

                    // Init.
                    onCreateCallBack: (data) => {
                        var dfd = $.Deferred<any>();
                        unemployeeInsuranceRateCopyDto.startMonth = data.startYearMonth;
                        unemployeeInsuranceRateCopyDto.addNew = true;
                        service.copyUnemployeeInsuranceRate(unemployeeInsuranceRateCopyDto).done(function() {
                            self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                            self.reloadDataUnemployeeInsuranceRateByAction();
                            self.clearErrorSaveUnemployeeInsurance();
                            dfd.resolve();
                        }).fail(function(error) {
                            dfd.reject(error);
                        });
                        return dfd.promise();
                    }
                };

                nts.uk.ui.windows.setShared('options', newHistoryOptions);
                var ntsDialogOptions = {
                    title: nts.uk.text.format('{0}の登録 > 履歴の追加', name),
                    dialogClass: 'no-close'
                };
                nts.uk.ui.windows.sub.modal('/view/base/simplehistory/newhistory/index.xhtml', ntsDialogOptions);
            }

            //open dialog edit InsuranceBusinessType => show view model xhtml (action event edit)
            private openEditInsuranceBusinessType() {
                var self = this;
                //call service get all insurance business type
                service.findAllInsuranceBusinessType().done(data => {
                    //set data fw to /e
                    nts.uk.ui.windows.setShared("InsuranceBusinessTypeDto", data);
                    //open dialog /e/index.xhtml
                    nts.uk.ui.windows.sub.modal("/view/qmm/011/e/index.xhtml", { height: 630, width: 425, title: "事業種類の登録", dialogClass: 'no-close' }).onClosed(() => {
                        //OnClose => call
                        //get fw e => respone 
                        var insuranceBusinessTypeUpdateModel = nts.uk.ui.windows.getShared("insuranceBusinessTypeUpdateModel");

                        if (insuranceBusinessTypeUpdateModel) {
                            //reload insurance business type by call service
                            service.findAllInsuranceBusinessType().done(data => {
                                //update model view 
                                self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                            });
                        }
                    });
                });
            }

            //open dialog edit AccidentInsuranceHistory => show view model xhtml (action event edit)
            private openEditAccidentInsuranceRateHistory() {

                var self = this;
                if (self.dirtyAccidentInsurance.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                        self.onShowEditAccidentInsuranceRateHistory();
                    }).ifNo(function() {
                        //No action
                    });
                    return;
                }
                self.onShowEditAccidentInsuranceRateHistory();
            }

            //open dialog edit AccidentInsuranceHistory => show view model xhtml (action event edit)
            private onShowEditAccidentInsuranceRateHistory() {

                var self = this;
                service.findAccidentInsuranceRateHistory(self.selectionAccidentInsuranceRateHistory()).done(data => {
                    var history: HistoryModel;
                    var endMonth: number = data.endMonth;
                    var name: string = '労働保険料率';
                    history = {
                        uuid: data.historyId,
                        start: data.startMonth,
                        end: endMonth
                    };

                    var newHistoryOptions: UpdateHistoryScreenOption = {
                        screenMode: ScreenMode.MODE_HISTORY_ONLY,
                        name: name,
                        master: null,
                        history: history,
                        removeMasterOnLastHistoryRemove: false,

                        // Delete callback.
                        onDeleteCallBack: (data) => {
                            var accidentInsuranceRateDeleteDto: AccidentInsuranceRateDeleteDto;
                            accidentInsuranceRateDeleteDto = new AccidentInsuranceRateDeleteDto();
                            accidentInsuranceRateDeleteDto.code = data.historyId;
                            accidentInsuranceRateDeleteDto.version = 0;
                            service.deleteAccidentInsuranceRate(accidentInsuranceRateDeleteDto).done(data => {
                                self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                                self.reloadDataAccidentInsuranceRateByAction();
                            }).fail(function(error) {
                                self.showMessageSaveAccidentInsurance(error.messageId)
                            });
                        },

                        // Update call back.
                        onUpdateCallBack: (data) => {
                            var dfd = $.Deferred<any>();
                            var accidentInsuranceHistoryUpdateDto: AccidentInsuranceHistoryUpdateDto;
                            accidentInsuranceHistoryUpdateDto = new AccidentInsuranceHistoryUpdateDto();
                            accidentInsuranceHistoryUpdateDto.historyId = data.historyId;
                            accidentInsuranceHistoryUpdateDto.startMonth = data.startYearMonth;
                            accidentInsuranceHistoryUpdateDto.endMonth = endMonth;
                            service.updateAccidentInsuranceRateHistory(accidentInsuranceHistoryUpdateDto).done(() => {
                                self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                                self.reloadDataAccidentInsuranceRateByAction();
                                dfd.resolve();
                            }).fail(function(error: any) {
                                dfd.reject(error);
                            });
                            return dfd.promise();
                        }
                    };

                    nts.uk.ui.windows.setShared('options', newHistoryOptions);
                    var ntsDialogOptions = {
                        title: nts.uk.text.format('{0}の登録 > 履歴の編集', name),
                        dialogClass: 'no-close'
                    };
                    nts.uk.ui.windows.sub.modal('/view/base/simplehistory/updatehistory/index.xhtml', ntsDialogOptions);

                });
            }

            //open dialog add AccidentInsuranceRateHistory => show view model xhtml (action event add)
            private openAddAccidentInsuranceRateHistory() {

                var self = this;
                if (self.dirtyAccidentInsurance.isDirty() && !self.isEmptyAccident()) {
                    nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                        self.onShowAddAccidentInsuranceRateHistory();
                    }).ifNo(function() {
                        //No action
                    });
                    return;
                }
                self.onShowAddAccidentInsuranceRateHistory();
            }

            //open dialog add AccidentInsuranceRateHistory => show view model xhtml (action event add)
            private onShowAddAccidentInsuranceRateHistory() {
                //set info
                var self = this;
                var lastest: HistoryModel;
                var name: string = '労働保険料率';
                lastest = {
                    uuid: self.selectionAccidentInsuranceRateHistory(),
                    start: self.accidentInsuranceRateModel().accidentInsuranceRateHistoryModel.startMonth(),
                    end: self.accidentInsuranceRateModel().accidentInsuranceRateHistoryModel.endMonth()
                };

                if (self.isEmptyAccident()) {
                    lastest = undefined;
                }
                //set info history
                var accidentInsuranceRateCopyDto: AccidentInsuranceRateCopyDto;
                accidentInsuranceRateCopyDto = new AccidentInsuranceRateCopyDto();
                accidentInsuranceRateCopyDto.historyIdCopy = self.selectionAccidentInsuranceRateHistory();
                //set info option
                var newHistoryOptions: NewHistoryScreenOption = {
                    screenMode: ScreenMode.MODE_HISTORY_ONLY,
                    name: name,
                    master: null,
                    lastest: lastest,

                    // Copy.
                    onCopyCallBack: (data) => {
                        var dfd = $.Deferred<any>();
                        accidentInsuranceRateCopyDto.startMonth = data.startYearMonth;
                        accidentInsuranceRateCopyDto.addNew = false;
                        service.copyAccidentInsuranceRate(accidentInsuranceRateCopyDto).done(data => {
                            self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                            self.reloadDataAccidentInsuranceRateByAction();
                            self.clearErrorSaveAccidentInsurance();
                            dfd.resolve();
                        }).fail(function(error) {
                            dfd.reject(error);
                        });
                        return dfd.promise();
                    },

                    // Init.
                    onCreateCallBack: (data) => {
                        var dfd = $.Deferred<any>();
                        accidentInsuranceRateCopyDto.startMonth = data.startYearMonth;
                        accidentInsuranceRateCopyDto.addNew = true;
                        service.copyAccidentInsuranceRate(accidentInsuranceRateCopyDto).done(data => {
                            self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                            self.reloadDataAccidentInsuranceRateByAction();
                            self.clearErrorSaveAccidentInsurance();
                            dfd.resolve();
                        }).fail(function(error) {
                            dfd.reject(error);
                        });
                        return dfd.promise();
                    }
                };

                nts.uk.ui.windows.setShared('options', newHistoryOptions);
                var ntsDialogOptions = {
                    title: nts.uk.text.format('{0}の登録 > 履歴の追加', name),
                    dialogClass: 'no-close'
                };
                nts.uk.ui.windows.sub.modal('/view/base/simplehistory/newhistory/index.xhtml', ntsDialogOptions);
            }
            //show UnemployeeInsuranceHistory (change event)
            private showchangeUnemployeeInsuranceHistory(selectionUnemployeeInsuranceRateHistory: string) {
                if (selectionUnemployeeInsuranceRateHistory != null
                    && selectionUnemployeeInsuranceRateHistory != undefined
                    && selectionUnemployeeInsuranceRateHistory != '') {
                    var self = this;
                    if (self.dirtyUnemployeeInsurance.isDirty() && self.isShowDirtyUnemployeeInsurance()) {
                        if (selectionUnemployeeInsuranceRateHistory !== self.preSelectUnemployeeInsuranceRateHistory()
                            && self.typeActionUnemployeeInsurance() == TypeActionInsuranceRate.update) {
                            nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                                self.isShowDirtyUnemployeeInsurance(false);
                                self.clearErrorSaveUnemployeeInsurance();
                                self.detailUnemployeeInsuranceRateHistory(selectionUnemployeeInsuranceRateHistory);
                            }).ifNo(function() {
                                self.selectionUnemployeeInsuranceRateHistory(self.preSelectUnemployeeInsuranceRateHistory());
                            });
                        }
                    } else {
                        self.clearErrorSaveUnemployeeInsurance();
                        self.detailUnemployeeInsuranceRateHistory(selectionUnemployeeInsuranceRateHistory);
                    }
                }
            }

            //Clear show message error connection by server
            private clearErrorSaveUnemployeeInsurance() {
                var self = this;
                $('.save-error').ntsError('clear');
                $('#btn_saveUnemployeeInsuranceHistory').ntsError('clear');
            }

            //show message save UnemployeeInsurance 
            private showMessageSaveUnemployeeInsurance(messageId: string) {

                var self = this;
                if (self.messageList()[0].messageId === messageId) {
                    //001
                    var message = self.messageList()[0].message;

                    if (!self.unemployeeInsuranceRateModel().unemployeeInsuranceRateItemAgroforestryModel) {
                        $('#inp_code').ntsError('set', message);
                    }
                }

                if (self.messageList()[1].messageId === messageId) {
                    message = self.messageList()[1].message;
                    $('#inp_code').ntsError('set', message);
                }

                if (self.messageList()[3].messageId === messageId) {
                    message = self.messageList()[3].message;
                    $('#btn_saveUnemployeeInsuranceHistory').ntsError('set', message);
                }

            }

            //Clear show message error connection by server
            private clearErrorSaveAccidentInsurance() {
                var self = this;
                $('.save-error').ntsError('clear');
                $('#btn_saveAccidentInsuranceHistory').ntsError('clear');
            }

            //show message save AccidentInsurance 
            private showMessageSaveAccidentInsurance(messageId: string) {

                var self = this;
                if (self.messageList()[0].messageId === messageId) {
                    //001
                    var message = self.messageList()[0].message;

                    if (!self.unemployeeInsuranceRateModel().unemployeeInsuranceRateItemAgroforestryModel) {
                        $('#inp_code').ntsError('set', message);
                    }
                }

                if (self.messageList()[1].messageId === messageId) {
                    message = self.messageList()[1].message;
                    $('#inp_code').ntsError('set', message);
                }

                if (self.messageList()[3].messageId === messageId) {
                    message = self.messageList()[3].message;
                    $('#btn_saveAccidentInsuranceHistory').ntsError('set', message);
                }
            }

            //action save UnemployeeInsuranceHistory Onlick connection service
            private saveUnemployeeInsuranceHistory() {
                var self = this;

                // get type action (ismode)
                if (self.typeActionUnemployeeInsurance() == TypeActionInsuranceRate.add) {
                    //type action is add
                    //call service add UnemployeeInsuranceHistory
                    service.addUnemployeeInsuranceRate(self.unemployeeInsuranceRateModel()).done(function() {
                        //reload viewmodel
                        self.reloadDataUnemployeeInsuranceRateByAction();
                        //clear error viewmodel
                        self.clearErrorSaveUnemployeeInsurance();
                    }).fail(function(res) {
                        //show message by exception message
                        self.showMessageSaveUnemployeeInsurance(res.messageId);
                        self.reloadDataUnemployeeInsuranceRateByAction();
                    })
                } else {
                    //type action is update
                    //call service update UnemployeeInsuranceHistory
                    service.updateUnemployeeInsuranceRate(self.unemployeeInsuranceRateModel()).done(function() {
                        //reload viewmodel
                        self.reloadDataUnemployeeInsuranceRateByAction();
                        //clear error viewmodel
                        self.clearErrorSaveUnemployeeInsurance();
                    }).fail(function(res) {
                        //show message by exception message
                        self.showMessageSaveUnemployeeInsurance(res.messageId);
                        self.reloadDataUnemployeeInsuranceRateByAction();
                    })
                }

                return true;
            }

            //action save AccidentInsuranceHistory Onlick connection service
            private saveAccidentInsuranceHistory() {
                var self = this;
                //get type action (ismode)
                if (self.typeActionAccidentInsurance() == TypeActionInsuranceRate.add) {
                    //type action is add
                    //call service add AccidentInsuranceRate
                    service.addAccidentInsuranceRate(self.accidentInsuranceRateModel()).done(function() {
                        //reload viewmodel
                        self.reloadDataAccidentInsuranceRateByAction();
                        //clear error viewmodel
                        self.clearErrorSaveAccidentInsurance();
                    }).fail(function(res) {
                        //show message by exception message
                        self.showMessageSaveAccidentInsurance(res.messageId);
                    })
                } else {
                    //type action is update
                    //call service update AccidentInsuranceRate
                    service.updateAccidentInsuranceRate(self.accidentInsuranceRateModel()).done(function() {
                        //reload viewmodel
                        self.detailAccidentInsuranceRateHistory(self.accidentInsuranceRateModel().accidentInsuranceRateHistoryModel.historyId());
                        //clear error viewmodel
                        self.clearErrorSaveAccidentInsurance();
                    }).fail(function(res) {
                        //show message by exception message
                        self.showMessageSaveAccidentInsurance(res.messageId);
                    })
                }

            }

            //show AccidentInsuranceHistory (change event)
            private showchangeAccidentInsuranceHistory(selectionAccidentInsuranceRateHistory: string) {
                if (selectionAccidentInsuranceRateHistory && selectionAccidentInsuranceRateHistory != '') {
                    var self = this;
                    if (self.dirtyAccidentInsurance.isDirty() && self.isShowDirtyActionAccidentInsurance()) {
                        if (selectionAccidentInsuranceRateHistory !== self.preSelectAccidentInsuranceRateHistory()
                            && self.typeActionAccidentInsurance() == TypeActionInsuranceRate.update) {
                            nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                                self.isShowDirtyActionAccidentInsurance(false);
                                self.detailAccidentInsuranceRateHistory(selectionAccidentInsuranceRateHistory);
                                return;
                            }).ifNo(function() {
                                //No action
                            });
                            self.selectionAccidentInsuranceRateHistory(self.preSelectAccidentInsuranceRateHistory());
                            return;
                        }
                    }
                    self.detailAccidentInsuranceRateHistory(selectionAccidentInsuranceRateHistory);
                }
            }

            // startPage => show view model xhtml (constructor)
            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();

                //findAll history Unemployee Insurance Rate
                self.findAllUnemployeeInsuranceRateHistory().done(data => {
                    //find All History Accident Insurance Rate
                    self.findAllAccidentInsuranceRateHistory().done(data => {
                        dfd.resolve(self);
                    });
                });

                return dfd.promise();
            }

            //find add UnemployeeInsuranceRateHistory => show view model xhtml (constructor)
            private findAllUnemployeeInsuranceRateHistory(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();

                //call service get all history unemployee insuranceRate
                service.findAllUnemployeeInsuranceRateHistory().done(data => {
                    //get data respone
                    if (data != null && data.length > 0) {
                        //data[] is length > 0
                        //set List history unemployee insurance rate by data
                        self.lstUnemployeeInsuranceRateHistory(data);
                        //set selection by fisrt data select
                        self.selectionUnemployeeInsuranceRateHistory(data[0].historyId);
                        //subscribe history selection
                        if (self.isEmptyUnemployee()) {
                            self.selectionUnemployeeInsuranceRateHistory.subscribe(function(selectionUnemployeeInsuranceRateHistory: string) {
                                self.showchangeUnemployeeInsuranceHistory(selectionUnemployeeInsuranceRateHistory);
                            });
                        }
                        //set isEmptyUnemployee false (not isEmptyUnemployee)
                        self.isEmptyUnemployee(false);
                        //call detail history unemployee insurance rate
                        self.detailUnemployeeInsuranceRateHistory(data[0].historyId).done(function() {
                            //fw to start page
                            dfd.resolve();
                        });
                    } else {
                        //set history unemployee insurance rate is empty
                        self.newmodelEmptyDataUnemployeeInsuranceRate();
                        //fw to start page
                        dfd.resolve();
                    }
                });

                return dfd.promise();
            }

            //reload action
            private reloadDataUnemployeeInsuranceRateByAction() {
                var self = this;

                //call service get all history unemployee insurance rate
                service.findAllUnemployeeInsuranceRateHistory().done(data => {
                    if (data != null && data.length > 0) {
                        //get data respone is data not null length > 0
                        //reset selection history unemployee insurance rate
                        self.selectionUnemployeeInsuranceRateHistory('');
                        //reset List history unemployee insurance rate
                        self.lstUnemployeeInsuranceRateHistory([]);
                        //get historyId by selection viewmodel
                        var historyId = self.unemployeeInsuranceRateModel().unemployeeInsuranceHistoryModel.historyId();

                        if (self.typeActionUnemployeeInsurance() == TypeActionInsuranceRate.add) {
                            //type action is add => set historyId (selection first data)
                            historyId = data[0].historyId;
                        }

                        if (self.isEmptyUnemployee()) {
                            //empty history unemployee insurance rate => set subscribe
                            self.selectionUnemployeeInsuranceRateHistory.subscribe(function(selectionUnemployeeInsuranceRateHistory: string) {
                                self.showchangeUnemployeeInsuranceHistory(self.selectionUnemployeeInsuranceRateHistory());
                            });
                            //set is empty 
                            self.isEmptyUnemployee(false);
                        }

                        //set data List history unemployee insurance rate
                        self.selectionUnemployeeInsuranceRateHistory(historyId);
                        self.lstUnemployeeInsuranceRateHistory(data);
                        //call detail history unemployee insurance rate
                        self.detailUnemployeeInsuranceRateHistory(historyId).done(data => {
                            //set enable input viewmodel
                            self.isEnableSaveUnemployeeInsurance(true);
                            self.isEnableEditUnemployeeInsurance(true);
                        });

                    } else {
                        //set history unemployee insurance rate is empty
                        self.newmodelEmptyDataUnemployeeInsuranceRate();
                    }
                });
            }

            //new model data = []
            private newmodelEmptyDataUnemployeeInsuranceRate() {
                var self = this;
                //reset selection history unemployee insurance rate
                self.selectionUnemployeeInsuranceRateHistory('');
                //reset List history unemployee insurance rate
                self.lstUnemployeeInsuranceRateHistory([]);
                //reset value history unemployee insurance rate
                self.resetValueUnemployeeInsuranceRate();
                //set is empty history unemployee insurance rate
                self.isEmptyUnemployee(true);
                self.isEnableSaveUnemployeeInsurance(false);
                self.isEnableEditUnemployeeInsurance(false);
            }

            //reset value UnemployeeInsuranceRate
            private resetValueUnemployeeInsuranceRate() {
                var self = this;
                //reset view model history unemployee insurance rate
                self.unemployeeInsuranceRateModel().resetValue(self.rateInputOptions, self.selectionRoundingMethod);
                //reset enable input view model
                self.unemployeeInsuranceRateModel().setEnable(false);
                //set is mode type action add
                self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                //reset selection history un employee insurance rate
                self.selectionUnemployeeInsuranceRateHistory('');
                self.dirtyUnemployeeInsurance.reset();
            }

            //detail UnemployeeInsuranceRateHistory => show view model xhtml (action event)
            private detailUnemployeeInsuranceRateHistory(historyId: string): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();

                if (historyId != null && historyId != undefined && historyId != '') {
                    //call service detail history un employee insurance rate by historyId
                    service.detailUnemployeeInsuranceRateHistory(historyId).done(data => {
                        //set view mode by data
                        self.unemployeeInsuranceRateModel().setListItem(data.rateItems);
                        self.unemployeeInsuranceRateModel().setHistoryData(data.historyInsurance);
                        //set is mode type action is update
                        self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.update);
                        self.beginHistoryStartUnemployeeInsuranceRate(nts.uk.time.formatYearMonth(data.historyInsurance.startMonth));
                        self.unemployeeInsuranceRateModel().setEnable(true);
                        self.dirtyUnemployeeInsurance.reset();
                        self.isShowDirtyUnemployeeInsurance(true);
                        self.preSelectUnemployeeInsuranceRateHistory(historyId);
                        dfd.resolve();
                    });
                }
                return dfd.promise();
            }

            //find All AccidentInsuranceRateHistory => Show View model xhtml (constructor)
            private findAllAccidentInsuranceRateHistory(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();

                //call service find all history accident insurance rate
                service.findAllAccidentInsuranceRateHistory().done(data => {
                    //get data respone
                    if (data != null && data.length > 0) {
                        //data not null length > 0 => update List history accident insurance rate
                        self.lstAccidentInsuranceRateHistory = ko.observableArray<AccidentInsuranceHistoryDto>(data);
                        self.selectionAccidentInsuranceRateHistory(data[0].historyId);
                        //subscribe history accident insurance rate
                        self.selectionAccidentInsuranceRateHistory.subscribe(function(selectionAccidentInsuranceRateHistory: string) {
                            self.showchangeAccidentInsuranceHistory(selectionAccidentInsuranceRateHistory);
                        });
                        //set is emmpty history accident insurance rate
                        self.isEmptyAccident(false);
                        //call detail history accident insurance rate by historyId
                        self.detailAccidentInsuranceRateHistory(data[0].historyId).done(data => {
                            //call service get all insurance business type
                            service.findAllInsuranceBusinessType().done(data => {
                                //update insurance business type
                                self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                                dfd.resolve(self);
                            });
                        });
                        self.isEnableSaveActionAccidentInsurance(true);
                    } else {
                        //reset viewmode is empty
                        self.newmodelEmptyDataAccidentInsuranceRate();
                        dfd.resolve(self);
                    }
                });

                return dfd.promise();
            }

            //reload action
            private reloadDataAccidentInsuranceRateByAction() {
                var self = this;
                //call service find all  history accident insurance rate
                service.findAllAccidentInsuranceRateHistory().done(data => {
                    //get data by respone
                    if (data != null && data.length > 0) {
                        //data not null length > 0
                        //reset List history accident insurance rate
                        self.selectionAccidentInsuranceRateHistory('');
                        self.lstAccidentInsuranceRateHistory([]);
                        //set historyId
                        var historyId = self.accidentInsuranceRateModel().accidentInsuranceRateHistoryModel.historyId();
                        if (self.typeActionAccidentInsurance() == TypeActionInsuranceRate.add) {
                            historyId = data[0].historyId;
                        }
                        self.selectionAccidentInsuranceRateHistory(historyId);
                        //update List history accident insurance rate
                        self.lstAccidentInsuranceRateHistory(data);
                        //call detail history accident insurance rate
                        self.detailAccidentInsuranceRateHistory(historyId).done(data => {
                            //call service get all insurance business type
                            service.findAllInsuranceBusinessType().done(data => {
                                //update insurance business type
                                self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                            });
                        });
                        if (self.isEmptyAccident()) {
                            self.selectionAccidentInsuranceRateHistory.subscribe(function(selectionAccidentInsuranceRateHistory: string) {
                                self.showchangeAccidentInsuranceHistory(selectionAccidentInsuranceRateHistory);
                            });
                            self.isEmptyAccident(false);
                        }
                        self.isEnableSaveActionAccidentInsurance(true);
                        self.isEnableEditActionAccidentInsurance(true);
                    } else {
                        //reset viewmode is empty
                        self.newmodelEmptyDataAccidentInsuranceRate();
                    }
                });
            }

            //new model data = []
            private newmodelEmptyDataAccidentInsuranceRate() {
                var self = this;
                self.selectionAccidentInsuranceRateHistory('');
                self.lstAccidentInsuranceRateHistory([]);
                self.resetValueAccidentInsuranceRate();
                service.findAllInsuranceBusinessType().done(data => {
                    self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                });
                self.isEmptyAccident(true);
                self.isEnableSaveActionAccidentInsurance(false);
                self.isEnableEditActionAccidentInsurance(false);
                self.accidentInsuranceRateModel().setEnable(false);
            }

            //reset value AccidentInsuranceRate
            private resetValueAccidentInsuranceRate() {
                var self = this;
                self.selectionAccidentInsuranceRateHistory('');
                self.accidentInsuranceRateModel().resetValue(self.rateInputOptions, self.selectionRoundingMethod);
                self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                self.dirtyAccidentInsurance.reset();
            }

            //detail AccidentInsuranceRateHistory => show view model xhtml (action event)
            private detailAccidentInsuranceRateHistory(historyId: string): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();

                if (historyId != null && historyId != undefined && historyId != '') {
                    //call service find  accident insurance rate by historyId
                    service.findAccidentInsuranceRate(historyId).done(data => {
                        self.accidentInsuranceRateModel().setListItem(data.rateItems);
                        self.accidentInsuranceRateModel().setVersion(data.version);
                        self.typeActionAccidentInsurance(TypeActionInsuranceRate.update);
                        self.accidentInsuranceRateModel().setHistoryData(data.historyInsurance);
                        self.accidentInsuranceRateModel().setEnable(true);
                        self.beginHistoryStartAccidentInsuranceRate(nts.uk.time.formatYearMonth(data.historyInsurance.startMonth));
                        self.dirtyAccidentInsurance.reset();
                        dfd.resolve(null);
                    });
                }

                return dfd.promise();
            }

            //update InsuranceBusinessType by Model viewmodel
            private updateInsuranceBusinessTypeAccidentInsurance
                (insuranceBusinessTypeUpdateModel: InsuranceBusinessTypeUpdateModel) {
                var self = this;
                self.accidentInsuranceRateModel()
                    .biz1StModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz1St());
                self.accidentInsuranceRateModel()
                    .biz2NdModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz2Nd());
                self.accidentInsuranceRateModel()
                    .biz3RdModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz3Rd());
                self.accidentInsuranceRateModel()
                    .biz4ThModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz4Th());
                self.accidentInsuranceRateModel()
                    .biz5ThModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz5Th());
                self.accidentInsuranceRateModel()
                    .biz6ThModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz6Th());
                self.accidentInsuranceRateModel()
                    .biz7ThModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz7Th());
                self.accidentInsuranceRateModel()
                    .biz8ThModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz8Th());
                self.accidentInsuranceRateModel()
                    .biz9ThModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz9Th());
                self.accidentInsuranceRateModel()
                    .biz10ThModel
                    .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz10Th());
            }

            //update insurance business type accident insurance
            private updateInsuranceBusinessTypeAccidentInsuranceDto(InsuranceBusinessTypeDto: InsuranceBusinessTypeDto) {
                var self = this;
                self.accidentInsuranceRateModel()
                    .biz1StModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz1St);
                self.accidentInsuranceRateModel()
                    .biz2NdModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz2Nd);
                self.accidentInsuranceRateModel()
                    .biz3RdModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz3Rd);
                self.accidentInsuranceRateModel()
                    .biz4ThModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz4Th);
                self.accidentInsuranceRateModel()
                    .biz5ThModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz5Th);
                self.accidentInsuranceRateModel()
                    .biz6ThModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz6Th);
                self.accidentInsuranceRateModel()
                    .biz7ThModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz7Th);
                self.accidentInsuranceRateModel()
                    .biz8ThModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz8Th);
                self.accidentInsuranceRateModel()
                    .biz9ThModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz9Th);
                self.accidentInsuranceRateModel()
                    .biz10ThModel
                    .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz10Th);
            }
        }

        export class UnemployeeInsuranceRateItemSettingModel {

            roundAtr: KnockoutObservable<number>;
            rate: KnockoutObservable<number>;
            isEnable: KnockoutObservable<boolean>;

            constructor() {
                this.roundAtr = ko.observable(0);
                this.rate = ko.observable(0);
                this.isEnable = ko.observable(true);
            }

            resetValue() {
                if (this.roundAtr == null || this.roundAtr == undefined) {
                    this.roundAtr = ko.observable(0);
                } else {
                    this.roundAtr(0);
                }
                if (this.rate == null || this.rate == undefined) {
                    this.rate = ko.observable(0);
                } else {
                    this.rate(0);
                }
                if (this.isEnable == null || this.isEnable == undefined) {
                    this.isEnable = ko.observable(false);
                } else {
                    this.isEnable(false);
                }
            }

            setItem(unemployeeInsuranceRateItemSetting: UnemployeeInsuranceRateItemSettingDto) {
                this.roundAtr(unemployeeInsuranceRateItemSetting.roundAtr);
                this.rate(unemployeeInsuranceRateItemSetting.rate);
            }

            setEnable(isEnable: boolean) {
                this.isEnable(isEnable);
            }
        }

        export class UnemployeeInsuranceRateItemModel {

            companySetting: UnemployeeInsuranceRateItemSettingModel;
            personalSetting: UnemployeeInsuranceRateItemSettingModel;
            rateInputOptions: any; selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>;

            constructor(rateInputOptions: any, selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>) {
                this.rateInputOptions = rateInputOptions;
                this.selectionRoundingMethod = selectionRoundingMethod;
                this.companySetting = new UnemployeeInsuranceRateItemSettingModel();
                this.personalSetting = new UnemployeeInsuranceRateItemSettingModel();
            }

            resetValue() {
                if (this.companySetting == null || this.companySetting == undefined) {
                    this.companySetting = new UnemployeeInsuranceRateItemSettingModel();
                    this.companySetting.resetValue();
                } else {
                    this.companySetting.resetValue();
                }
                if (this.personalSetting == null || this.personalSetting == undefined) {
                    this.personalSetting = new UnemployeeInsuranceRateItemSettingModel();
                    this.personalSetting.resetValue();
                } else {
                    this.personalSetting.resetValue();
                }
            }

            setItem(unemployeeInsuranceRateItemDto: UnemployeeInsuranceRateItemDto) {
                this.companySetting.setItem(unemployeeInsuranceRateItemDto.companySetting);
                this.personalSetting.setItem(unemployeeInsuranceRateItemDto.personalSetting);
            }

            setEnable(isEnable: boolean) {
                this.companySetting.setEnable(isEnable);
                this.personalSetting.setEnable(isEnable);
            }
        }

        export class UnemployeeInsuranceHistoryModel {

            historyId: KnockoutObservable<string>;
            startJapStartMonth: KnockoutObservable<string>;
            startMonth: KnockoutObservable<number>;
            endMonth: KnockoutObservable<number>;
            endMonthRage: KnockoutObservable<string>;

            constructor() {
                this.historyId = ko.observable('');
                this.startJapStartMonth = ko.observable('');
                this.endMonthRage = ko.observable('');
                this.endMonth = ko.observable(0);
                this.startMonth = ko.observable(0);
            }

            resetValue() {
                if (this.historyId != null && this.historyId != undefined) {
                    this.historyId('');
                } else {
                    this.historyId = ko.observable('');
                }
                if (this.startJapStartMonth != null && this.startJapStartMonth != undefined) {
                    this.startJapStartMonth('');
                } else {
                    this.startJapStartMonth = ko.observable('');
                }
                if (this.endMonthRage != null && this.endMonthRage != undefined) {
                    this.endMonthRage('9999/12');
                } else {
                    this.endMonthRage = ko.observable('9999/12');
                }
                if (this.endMonth != null && this.endMonth != undefined) {
                    this.endMonth(999912);
                } else {
                    this.endMonth = ko.observable(999912);
                }
                if (this.startMonth != null && this.startMonth != undefined) {
                    this.endMonth(0);
                } else {
                    this.startMonth = ko.observable(0);
                }
            }

            updateData(unemployeeInsuranceHistory: HistoryInsuranceInDto) {
                this.historyId(unemployeeInsuranceHistory.historyId);
                this.startJapStartMonth(nts.uk.time.formatYearMonth(unemployeeInsuranceHistory.startMonth)
                    + ' (' + nts.uk.time.yearmonthInJapanEmpire(unemployeeInsuranceHistory.startMonth).toString() + ') ');
                this.endMonthRage(nts.uk.time.formatYearMonth(unemployeeInsuranceHistory.endMonth));
                this.startMonth(unemployeeInsuranceHistory.startMonth);
                this.endMonth(unemployeeInsuranceHistory.endMonth);
            }

            setMonthRage(startDate: number, endDate: number) {
                this.startMonth(startDate);
                this.endMonth(endDate);
            }
        }

        export class UnemployeeInsuranceRateModel {

            unemployeeInsuranceRateItemAgroforestryModel: UnemployeeInsuranceRateItemModel;
            unemployeeInsuranceRateItemContructionModel: UnemployeeInsuranceRateItemModel;
            unemployeeInsuranceRateItemOtherModel: UnemployeeInsuranceRateItemModel;
            version: KnockoutObservable<number>;
            unemployeeInsuranceHistoryModel: UnemployeeInsuranceHistoryModel;

            constructor(rateInputOptions: any, selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>) {
                this.unemployeeInsuranceRateItemAgroforestryModel
                    = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                this.unemployeeInsuranceRateItemContructionModel
                    = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                this.unemployeeInsuranceRateItemOtherModel
                    = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                this.version = ko.observable(0);
                this.unemployeeInsuranceHistoryModel = new UnemployeeInsuranceHistoryModel();
            }

            resetValue(rateInputOptions: any, selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>) {
                if (this.unemployeeInsuranceRateItemAgroforestryModel == null
                    || this.unemployeeInsuranceRateItemAgroforestryModel == undefined) {
                    this.unemployeeInsuranceRateItemAgroforestryModel
                        = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                    this.unemployeeInsuranceRateItemAgroforestryModel.resetValue();
                } else {
                    this.unemployeeInsuranceRateItemAgroforestryModel.resetValue();
                }
                if (this.unemployeeInsuranceRateItemContructionModel == null
                    || this.unemployeeInsuranceRateItemContructionModel == undefined) {
                    this.unemployeeInsuranceRateItemContructionModel
                        = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                    this.unemployeeInsuranceRateItemContructionModel.resetValue();
                } else {
                    this.unemployeeInsuranceRateItemContructionModel.resetValue();
                }
                if (this.unemployeeInsuranceRateItemOtherModel == null
                    || this.unemployeeInsuranceRateItemOtherModel == undefined) {
                    this.unemployeeInsuranceRateItemOtherModel
                        = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                    this.unemployeeInsuranceRateItemOtherModel.resetValue();
                } else {
                    this.unemployeeInsuranceRateItemOtherModel.resetValue();
                }
                if (this.unemployeeInsuranceHistoryModel == null
                    || this.unemployeeInsuranceHistoryModel == undefined) {
                    this.unemployeeInsuranceHistoryModel = new UnemployeeInsuranceHistoryModel();
                } else {
                    this.unemployeeInsuranceHistoryModel.resetValue();
                }
            }

            setHistoryData(UnemployeeInsuranceHistoryDto: HistoryInsuranceInDto) {
                this.unemployeeInsuranceHistoryModel.updateData(UnemployeeInsuranceHistoryDto);
            }

            setListItem(rateItems: UnemployeeInsuranceRateItemDto[]) {
                if (rateItems != null && rateItems.length > 0) {
                    for (var rateItem of rateItems) {
                        //Agroforestry
                        if (rateItem.careerGroup == CareerGroupDto.Agroforestry) {
                            this.unemployeeInsuranceRateItemAgroforestryModel.setItem(rateItem);
                        }
                        //Contruction
                        else if (rateItem.careerGroup == CareerGroupDto.Contruction) {
                            this.unemployeeInsuranceRateItemContructionModel.setItem(rateItem);
                        }
                        //Other
                        else if (rateItem.careerGroup == CareerGroupDto.Other) {
                            this.unemployeeInsuranceRateItemOtherModel.setItem(rateItem);
                        }
                    }
                }
            }
            setVersion(version: number) {
                this.version(version);
            }
            setEnable(isEnable: boolean) {
                this.unemployeeInsuranceRateItemAgroforestryModel.setEnable(isEnable);
                this.unemployeeInsuranceRateItemContructionModel.setEnable(isEnable);
                this.unemployeeInsuranceRateItemOtherModel.setEnable(isEnable);
            }
        }

        export class AccIRDetailModel {

            insuRate: KnockoutObservable<number>;
            insuRound: KnockoutObservable<number>;
            insuranceBusinessType: KnockoutObservable<string>;
            rateInputOptions: any;
            selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>;
            isEnable: KnockoutObservable<boolean>;

            constructor(rateInputOptions: any, selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>) {
                this.rateInputOptions = rateInputOptions;
                this.selectionRoundingMethod = selectionRoundingMethod;
                this.insuranceBusinessType = ko.observable('');
                this.insuRate = ko.observable(0);
                this.insuRound = ko.observable(0);
                this.isEnable = ko.observable(true);
            }

            //Fuction update value insuranceBusinessType
            updateInsuranceBusinessType(insuranceBusinessType: string) {
                if (this.insuranceBusinessType() != null && this.insuranceBusinessType() != undefined) {
                    this.insuranceBusinessType(insuranceBusinessType);
                }
            }

            setItem(insuBizRateItem: InsuBizRateItemDto) {
                this.insuRate(insuBizRateItem.insuRate);
                this.insuRound(insuBizRateItem.insuRound);
            }

            resetValue() {
                if (this.insuRate == null || this.insuRate == undefined) {
                    this.insuRate = ko.observable(0);
                } else {
                    this.insuRate(0);
                }
                if (this.insuRound == null || this.insuRound == undefined) {
                    this.insuRound = ko.observable(0);
                } else {
                    this.insuRate(0);
                }
                if (this.isEnable == null || this.isEnable == undefined) {
                    this.isEnable = ko.observable(true);
                } else {
                    this.isEnable(true);
                }
            }

            setEnable(isEnable: boolean) {
                this.isEnable(isEnable);
            }
        }

        export class AccIRHistoryModel {

            historyId: KnockoutObservable<string>;
            startMonthRage: KnockoutObservable<string>;
            startMonth: KnockoutObservable<number>;
            endMonthRage: KnockoutObservable<string>;
            endMonth: KnockoutObservable<number>;

            constructor() {
                this.historyId = ko.observable('');
                this.startMonthRage = ko.observable('');
                this.endMonthRage = ko.observable('');
                this.endMonth = ko.observable(0);
                this.startMonth = ko.observable(0);
            }

            resetValue() {
                if (this.historyId != null && this.historyId != undefined) {
                    this.historyId('');
                } else {
                    this.historyId = ko.observable('');
                }
                if (this.startMonthRage != null && this.startMonthRage != undefined) {
                    this.startMonthRage('');
                } else {
                    this.startMonthRage = ko.observable('');
                }
                if (this.endMonthRage != null && this.endMonthRage != undefined) {
                    this.endMonthRage('9999/12');
                } else {
                    this.endMonthRage = ko.observable('9999/12');
                }

                if (this.startMonth != null && this.startMonth != undefined) {
                    this.startMonth = ko.observable(0);
                } else {
                    this.startMonth(0);
                }

                if (this.endMonth != null && this.endMonth != undefined) {
                    this.endMonth = ko.observable(0);
                } else {
                    this.endMonth(0);
                }
            }

            updateData(historyDto: HistoryInsuranceInDto) {
                this.resetValue();
                this.historyId(historyDto.historyId);
                this.startMonth(historyDto.startMonth);
                this.endMonth(historyDto.endMonth);
                this.startMonthRage(nts.uk.time.formatYearMonth(historyDto.startMonth)
                    + ' (' + nts.uk.time.yearmonthInJapanEmpire(historyDto.startMonth).toString() + ') ');
                this.endMonthRage(nts.uk.time.formatYearMonth(historyDto.endMonth));
            }
        }

        export class AccIRModel {

            biz1StModel: AccIRDetailModel;
            biz2NdModel: AccIRDetailModel;
            biz3RdModel: AccIRDetailModel;
            biz4ThModel: AccIRDetailModel;
            biz5ThModel: AccIRDetailModel;
            biz6ThModel: AccIRDetailModel;
            biz7ThModel: AccIRDetailModel;
            biz8ThModel: AccIRDetailModel;
            biz9ThModel: AccIRDetailModel;
            biz10ThModel: AccIRDetailModel;
            accidentInsuranceRateHistoryModel: AccIRHistoryModel;
            version: KnockoutObservable<number>;

            constructor(rateInputOptions: any,
                selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>) {
                this.biz1StModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz2NdModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz3RdModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz4ThModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz5ThModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz6ThModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz7ThModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz8ThModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz9ThModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.biz10ThModel =
                    new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                this.accidentInsuranceRateHistoryModel = new AccIRHistoryModel();
            }

            setListItem(lstInsuBizRateItem: InsuBizRateItemDto[]) {
                for (var rateItem of lstInsuBizRateItem) {
                    //Biz1St
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz1St) {
                        this.biz1StModel.setItem(rateItem);
                    }
                    //Biz2Nd
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz2Nd) {
                        this.biz2NdModel.setItem(rateItem);
                    }
                    //Biz3Rd
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz3Rd) {
                        this.biz3RdModel.setItem(rateItem);
                    }
                    //Biz4Th
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz4Th) {
                        this.biz4ThModel.setItem(rateItem);
                    }
                    //Biz5Th
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz5Th) {
                        this.biz5ThModel.setItem(rateItem);
                    }
                    //Biz6Th
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz6Th) {
                        this.biz6ThModel.setItem(rateItem);
                    }
                    //Biz7Th
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz7Th) {
                        this.biz7ThModel.setItem(rateItem);
                    }
                    //Biz8Th
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz8Th) {
                        this.biz8ThModel.setItem(rateItem);
                    }
                    //Biz9Th
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz9Th) {
                        this.biz9ThModel.setItem(rateItem);
                    }
                    //Biz10Th
                    if (rateItem.insuBizType == BusinessTypeEnumDto.Biz10Th) {
                        this.biz10ThModel.setItem(rateItem);
                    }
                }
            }

            setVersion(version: number) {
                if (this.version == null || this.version == undefined) {
                    this.version = ko.observable(version);
                } else {
                    this.version(version);
                }
            }

            setHistoryData(historyDto: HistoryInsuranceInDto) {
                this.accidentInsuranceRateHistoryModel.updateData(historyDto);
            }

            resetValue(rateInputOptions: any,
                selectionRoundingMethod: KnockoutObservableArray<RoundingMethodDto>) {
                if (this.biz1StModel == null
                    || this.biz1StModel == undefined) {
                    this.biz1StModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz1StModel.resetValue();
                } else {
                    this.biz1StModel.resetValue();
                }
                if (this.biz2NdModel == null
                    || this.biz2NdModel == undefined) {
                    this.biz2NdModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz2NdModel.resetValue();
                } else {
                    this.biz2NdModel.resetValue();
                }
                if (this.biz3RdModel == null
                    || this.biz3RdModel == undefined) {
                    this.biz3RdModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz3RdModel.resetValue();
                } else {
                    this.biz3RdModel.resetValue();
                }
                if (this.biz4ThModel == null
                    || this.biz4ThModel == undefined) {
                    this.biz4ThModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz4ThModel.resetValue();
                } else {
                    this.biz4ThModel.resetValue();
                }
                if (this.biz5ThModel == null
                    || this.biz5ThModel == undefined) {
                    this.biz5ThModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz5ThModel.resetValue();
                } else {
                    this.biz5ThModel.resetValue();
                }
                if (this.biz6ThModel == null
                    || this.biz6ThModel == undefined) {
                    this.biz6ThModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz6ThModel.resetValue();
                } else {
                    this.biz6ThModel.resetValue();
                }
                if (this.biz7ThModel == null
                    || this.biz7ThModel == undefined) {
                    this.biz7ThModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz7ThModel.resetValue();
                } else {
                    this.biz7ThModel.resetValue();
                }
                if (this.biz8ThModel == null
                    || this.biz8ThModel == undefined) {
                    this.biz8ThModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz8ThModel.resetValue();
                } else {
                    this.biz8ThModel.resetValue();
                }
                if (this.biz9ThModel == null
                    || this.biz9ThModel == undefined) {
                    this.biz9ThModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz9ThModel.resetValue();
                } else {
                    this.biz9ThModel.resetValue();
                }
                if (this.biz10ThModel == null
                    || this.biz10ThModel == undefined) {
                    this.biz10ThModel
                        = new AccIRDetailModel(rateInputOptions, selectionRoundingMethod);
                    this.biz10ThModel.resetValue();
                } else {
                    this.biz10ThModel.resetValue();
                }
                if (this.version == null || this.version == undefined) {
                    this.version = ko.observable(0);
                } else {
                    this.version(0);
                }
                if (this.accidentInsuranceRateHistoryModel == null
                    || this.accidentInsuranceRateHistoryModel == undefined) {
                    this.accidentInsuranceRateHistoryModel
                        = new AccIRHistoryModel();
                } else {
                    this.accidentInsuranceRateHistoryModel.resetValue();
                }
            }
            setEnable(isEnable: boolean) {
                this.biz1StModel.setEnable(isEnable);
                this.biz2NdModel.setEnable(isEnable);
                this.biz3RdModel.setEnable(isEnable);
                this.biz4ThModel.setEnable(isEnable);
                this.biz5ThModel.setEnable(isEnable);
                this.biz6ThModel.setEnable(isEnable);
                this.biz7ThModel.setEnable(isEnable);
                this.biz8ThModel.setEnable(isEnable);
                this.biz9ThModel.setEnable(isEnable);
                this.biz10ThModel.setEnable(isEnable);
            }
        }
    }
}