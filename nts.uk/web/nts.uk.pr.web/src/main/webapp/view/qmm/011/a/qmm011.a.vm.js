var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var qmm011;
                (function (qmm011) {
                    var a;
                    (function (a) {
                        //import data class .... BEGIN
                        var option = nts.uk.ui.option;
                        var RoundingMethodDto = a.service.model.RoundingMethodDto;
                        var UnemployeeInsuranceHistoryUpdateDto = a.service.model.UnemployeeInsuranceHistoryUpdateDto;
                        var AccidentInsuranceHistoryUpdateDto = a.service.model.AccidentInsuranceHistoryUpdateDto;
                        var UnemployeeInsuranceDeleteDto = a.service.model.UnemployeeInsuranceDeleteDto;
                        var AccidentInsuranceRateDeleteDto = a.service.model.AccidentInsuranceRateDeleteDto;
                        var CareerGroupDto = a.service.model.CareerGroupDto;
                        var BusinessTypeEnumDto = a.service.model.BusinessTypeEnumDto;
                        var UnemployeeInsuranceRateCopyDto = a.service.model.UnemployeeInsuranceRateCopyDto;
                        var AccidentInsuranceRateCopyDto = a.service.model.AccidentInsuranceRateCopyDto;
                        var TypeActionInsuranceRate = a.service.model.TypeActionInsuranceRate;
                        var ScreenMode = nts.uk.pr.view.base.simplehistory.dialogbase.ScreenMode;
                        //import data class ... END
                        var viewmodel;
                        (function (viewmodel) {
                            var ScreenModel = (function () {
                                function ScreenModel() {
                                    var self = this;
                                    self.selectionRoundingMethod = ko.observableArray([new RoundingMethodDto(0, "切り捨て"),
                                        new RoundingMethodDto(1, "切り上げ"),
                                        new RoundingMethodDto(2, "四捨五入"),
                                        new RoundingMethodDto(3, "五捨六入"),
                                        new RoundingMethodDto(4, "五捨五超入")]); //Round Down
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
                                    self.accidentInsuranceRateModel = ko.observable(new AccidentInsuranceRateModel(self.rateInputOptions, self.selectionRoundingMethod));
                                    self.unemployeeInsuranceRateModel = ko.observable(new UnemployeeInsuranceRateModel(self.rateInputOptions, self.selectionRoundingMethod));
                                    self.lstUnemployeeInsuranceRateHistory = ko.observableArray([]);
                                    self.lstAccidentInsuranceRateHistory = ko.observableArray([]);
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
                                ScreenModel.prototype.openEditUnemployeeInsuranceRateHistory = function () {
                                    //set info
                                    var self = this;
                                    if (self.dirtyUnemployeeInsurance.isDirty()) {
                                        nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function () {
                                            self.onShowEditUnemployeeInsuranceRateHistory();
                                        }).ifNo(function () {
                                            //No action
                                        });
                                        return;
                                    }
                                    self.onShowEditUnemployeeInsuranceRateHistory();
                                };
                                //open dialog edit UnemployeeInsuranceRateHistory => show view model xhtml (action event add)
                                ScreenModel.prototype.onShowEditUnemployeeInsuranceRateHistory = function () {
                                    var self = this;
                                    a.service.findUnemployeeInsuranceRateHistory(self.selectionUnemployeeInsuranceRateHistory()).done(function (data) {
                                        var history;
                                        var endMonth = data.endMonth;
                                        var name = '雇用保険料率';
                                        history = {
                                            uuid: self.selectionUnemployeeInsuranceRateHistory(),
                                            start: data.startMonth,
                                            end: endMonth
                                        };
                                        var newHistoryOptions = {
                                            screenMode: ScreenMode.MODE_HISTORY_ONLY,
                                            name: name,
                                            master: null,
                                            history: history,
                                            removeMasterOnLastHistoryRemove: false,
                                            // Delete callback.
                                            onDeleteCallBack: function (data) {
                                                var unemployeeInsuranceDeleteDto;
                                                unemployeeInsuranceDeleteDto = new UnemployeeInsuranceDeleteDto();
                                                unemployeeInsuranceDeleteDto.code = data.historyId;
                                                unemployeeInsuranceDeleteDto.version = 0;
                                                a.service.deleteUnemployeeInsurance(unemployeeInsuranceDeleteDto).done(function (data) {
                                                    self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                                                    self.reloadDataUnemployeeInsuranceRateByAction();
                                                }).fail(function (error) {
                                                    self.showMessageSaveUnemployeeInsurance(error.message);
                                                });
                                            },
                                            // Update call back.
                                            onUpdateCallBack: function (data) {
                                                var dfd = $.Deferred();
                                                var unemployeeInsuranceHistoryUpdateDto;
                                                unemployeeInsuranceHistoryUpdateDto = new UnemployeeInsuranceHistoryUpdateDto();
                                                unemployeeInsuranceHistoryUpdateDto.historyId = data.historyId;
                                                unemployeeInsuranceHistoryUpdateDto.startMonth = data.startYearMonth;
                                                unemployeeInsuranceHistoryUpdateDto.endMonth = endMonth;
                                                a.service.updateUnemployeeInsuranceRateHistory(unemployeeInsuranceHistoryUpdateDto).done(function () {
                                                    self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.update);
                                                    self.reloadDataUnemployeeInsuranceRateByAction();
                                                    dfd.resolve();
                                                }).fail(function (error) {
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
                                };
                                //open dialog add UnemployeeInsuranceRateHistory => show view model xhtml (action event add)
                                ScreenModel.prototype.openAddUnemployeeInsuranceRateHistory = function () {
                                    //set info
                                    var self = this;
                                    if (self.dirtyUnemployeeInsurance.isDirty() && !self.isEmptyUnemployee()) {
                                        nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function () {
                                            self.onShowAddUnemployeeInsuranceRateHistory();
                                        }).ifNo(function () {
                                            //No action
                                        });
                                        return;
                                    }
                                    self.onShowAddUnemployeeInsuranceRateHistory();
                                };
                                //open dialog add UnemployeeInsuranceRateHistory => show view model xhtml (action event add)
                                ScreenModel.prototype.onShowAddUnemployeeInsuranceRateHistory = function () {
                                    var self = this;
                                    var lastest;
                                    var name = '雇用保険料率';
                                    lastest = {
                                        uuid: self.selectionUnemployeeInsuranceRateHistory(),
                                        start: self.unemployeeInsuranceRateModel().unemployeeInsuranceHistoryModel.startMonth(),
                                        end: self.unemployeeInsuranceRateModel().unemployeeInsuranceHistoryModel.endMonth()
                                    };
                                    if (self.isEmptyUnemployee()) {
                                        lastest = undefined;
                                    }
                                    //set info history
                                    var unemployeeInsuranceRateCopyDto;
                                    unemployeeInsuranceRateCopyDto = new UnemployeeInsuranceRateCopyDto();
                                    unemployeeInsuranceRateCopyDto.historyIdCopy = self.selectionUnemployeeInsuranceRateHistory();
                                    //set info option
                                    var newHistoryOptions = {
                                        screenMode: ScreenMode.MODE_HISTORY_ONLY,
                                        name: name,
                                        master: null,
                                        lastest: lastest,
                                        // Copy.
                                        onCopyCallBack: function (data) {
                                            var dfd = $.Deferred();
                                            unemployeeInsuranceRateCopyDto.startMonth = data.startYearMonth;
                                            unemployeeInsuranceRateCopyDto.addNew = false;
                                            a.service.copyUnemployeeInsuranceRate(unemployeeInsuranceRateCopyDto).done(function (data) {
                                                self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                                                self.reloadDataUnemployeeInsuranceRateByAction();
                                                self.clearErrorSaveUnemployeeInsurance();
                                                dfd.resolve();
                                            }).fail(function (error) {
                                                dfd.reject(error);
                                            });
                                            return dfd.promise();
                                        },
                                        // Init.
                                        onCreateCallBack: function (data) {
                                            var dfd = $.Deferred();
                                            unemployeeInsuranceRateCopyDto.startMonth = data.startYearMonth;
                                            unemployeeInsuranceRateCopyDto.addNew = true;
                                            a.service.copyUnemployeeInsuranceRate(unemployeeInsuranceRateCopyDto).done(function (data) {
                                                self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                                                self.reloadDataUnemployeeInsuranceRateByAction();
                                                self.clearErrorSaveUnemployeeInsurance();
                                                dfd.resolve();
                                            }).fail(function (error) {
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
                                };
                                //open dialog edit InsuranceBusinessType => show view model xhtml (action event edit)
                                ScreenModel.prototype.openEditInsuranceBusinessType = function () {
                                    var self = this;
                                    //call service get all insurance business type
                                    a.service.findAllInsuranceBusinessType().done(function (data) {
                                        //set data fw to /e
                                        nts.uk.ui.windows.setShared("InsuranceBusinessTypeDto", data);
                                        //open dialog /e/index.xhtml
                                        nts.uk.ui.windows.sub.modal("/view/qmm/011/e/index.xhtml", { height: 630, width: 425, title: "事業種類の登録", dialogClass: 'no-close' }).onClosed(function () {
                                            //OnClose => call
                                            //get fw e => respone 
                                            var insuranceBusinessTypeUpdateModel = nts.uk.ui.windows.getShared("insuranceBusinessTypeUpdateModel");
                                            if (insuranceBusinessTypeUpdateModel != null && insuranceBusinessTypeUpdateModel != undefined) {
                                                //reload insurance business type by call service
                                                a.service.findAllInsuranceBusinessType().done(function (data) {
                                                    //update model view 
                                                    self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                                                });
                                            }
                                        });
                                    });
                                };
                                //open dialog edit AccidentInsuranceHistory => show view model xhtml (action event edit)
                                ScreenModel.prototype.openEditAccidentInsuranceRateHistory = function () {
                                    var self = this;
                                    if (self.dirtyAccidentInsurance.isDirty()) {
                                        nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function () {
                                            self.onShowEditAccidentInsuranceRateHistory();
                                        }).ifNo(function () {
                                            //No action
                                        });
                                        return;
                                    }
                                    self.onShowEditAccidentInsuranceRateHistory();
                                };
                                //open dialog edit AccidentInsuranceHistory => show view model xhtml (action event edit)
                                ScreenModel.prototype.onShowEditAccidentInsuranceRateHistory = function () {
                                    var self = this;
                                    a.service.findAccidentInsuranceRateHistory(self.selectionAccidentInsuranceRateHistory()).done(function (data) {
                                        var history;
                                        var endMonth = data.endMonth;
                                        var name = '労働保険料率';
                                        history = {
                                            uuid: data.historyId,
                                            start: data.startMonth,
                                            end: endMonth
                                        };
                                        var newHistoryOptions = {
                                            screenMode: ScreenMode.MODE_HISTORY_ONLY,
                                            name: name,
                                            master: null,
                                            history: history,
                                            removeMasterOnLastHistoryRemove: false,
                                            // Delete callback.
                                            onDeleteCallBack: function (data) {
                                                var accidentInsuranceRateDeleteDto;
                                                accidentInsuranceRateDeleteDto = new AccidentInsuranceRateDeleteDto();
                                                accidentInsuranceRateDeleteDto.code = data.historyId;
                                                accidentInsuranceRateDeleteDto.version = 0;
                                                a.service.deleteAccidentInsuranceRate(accidentInsuranceRateDeleteDto).done(function (data) {
                                                    self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                                                    self.reloadDataAccidentInsuranceRateByAction();
                                                }).fail(function (error) {
                                                    self.showMessageSaveAccidentInsurance(error.messageId);
                                                });
                                            },
                                            // Update call back.
                                            onUpdateCallBack: function (data) {
                                                var dfd = $.Deferred();
                                                var accidentInsuranceHistoryUpdateDto;
                                                accidentInsuranceHistoryUpdateDto = new AccidentInsuranceHistoryUpdateDto();
                                                accidentInsuranceHistoryUpdateDto.historyId = data.historyId;
                                                accidentInsuranceHistoryUpdateDto.startMonth = data.startYearMonth;
                                                accidentInsuranceHistoryUpdateDto.endMonth = endMonth;
                                                a.service.updateAccidentInsuranceRateHistory(accidentInsuranceHistoryUpdateDto).done(function () {
                                                    self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                                                    self.reloadDataAccidentInsuranceRateByAction();
                                                    dfd.resolve();
                                                }).fail(function (error) {
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
                                };
                                //open dialog add AccidentInsuranceRateHistory => show view model xhtml (action event add)
                                ScreenModel.prototype.openAddAccidentInsuranceRateHistory = function () {
                                    var self = this;
                                    if (self.dirtyAccidentInsurance.isDirty() && !self.isEmptyAccident()) {
                                        nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function () {
                                            self.onShowAddAccidentInsuranceRateHistory();
                                        }).ifNo(function () {
                                            //No action
                                        });
                                        return;
                                    }
                                    self.onShowAddAccidentInsuranceRateHistory();
                                };
                                //open dialog add AccidentInsuranceRateHistory => show view model xhtml (action event add)
                                ScreenModel.prototype.onShowAddAccidentInsuranceRateHistory = function () {
                                    //set info
                                    var self = this;
                                    var lastest;
                                    var name = '労働保険料率';
                                    lastest = {
                                        uuid: self.selectionAccidentInsuranceRateHistory(),
                                        start: self.accidentInsuranceRateModel().accidentInsuranceRateHistoryModel.startMonth(),
                                        end: self.accidentInsuranceRateModel().accidentInsuranceRateHistoryModel.endMonth()
                                    };
                                    if (self.isEmptyAccident()) {
                                        lastest = undefined;
                                    }
                                    //set info history
                                    var accidentInsuranceRateCopyDto;
                                    accidentInsuranceRateCopyDto = new AccidentInsuranceRateCopyDto();
                                    accidentInsuranceRateCopyDto.historyIdCopy = self.selectionAccidentInsuranceRateHistory();
                                    //set info option
                                    var newHistoryOptions = {
                                        screenMode: ScreenMode.MODE_HISTORY_ONLY,
                                        name: name,
                                        master: null,
                                        lastest: lastest,
                                        // Copy.
                                        onCopyCallBack: function (data) {
                                            var dfd = $.Deferred();
                                            accidentInsuranceRateCopyDto.startMonth = data.startYearMonth;
                                            accidentInsuranceRateCopyDto.addNew = false;
                                            a.service.copyAccidentInsuranceRate(accidentInsuranceRateCopyDto).done(function (data) {
                                                self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                                                self.reloadDataAccidentInsuranceRateByAction();
                                                self.clearErrorSaveAccidentInsurance();
                                                dfd.resolve();
                                            }).fail(function (error) {
                                                dfd.reject(error);
                                            });
                                            return dfd.promise();
                                        },
                                        // Init.
                                        onCreateCallBack: function (data) {
                                            var dfd = $.Deferred();
                                            accidentInsuranceRateCopyDto.startMonth = data.startYearMonth;
                                            accidentInsuranceRateCopyDto.addNew = true;
                                            a.service.copyAccidentInsuranceRate(accidentInsuranceRateCopyDto).done(function (data) {
                                                self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                                                self.reloadDataAccidentInsuranceRateByAction();
                                                self.clearErrorSaveAccidentInsurance();
                                                dfd.resolve();
                                            }).fail(function (error) {
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
                                };
                                //show UnemployeeInsuranceHistory (change event)
                                ScreenModel.prototype.showchangeUnemployeeInsuranceHistory = function (selectionUnemployeeInsuranceRateHistory) {
                                    if (selectionUnemployeeInsuranceRateHistory != null
                                        && selectionUnemployeeInsuranceRateHistory != undefined
                                        && selectionUnemployeeInsuranceRateHistory != '') {
                                        var self = this;
                                        if (self.dirtyUnemployeeInsurance.isDirty() && self.isShowDirtyUnemployeeInsurance()) {
                                            if (selectionUnemployeeInsuranceRateHistory !== self.preSelectUnemployeeInsuranceRateHistory()
                                                && self.typeActionUnemployeeInsurance() == TypeActionInsuranceRate.update) {
                                                nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function () {
                                                    self.isShowDirtyUnemployeeInsurance(false);
                                                    self.detailUnemployeeInsuranceRateHistory(selectionUnemployeeInsuranceRateHistory);
                                                }).ifNo(function () {
                                                    self.selectionUnemployeeInsuranceRateHistory(self.preSelectUnemployeeInsuranceRateHistory());
                                                });
                                            }
                                        }
                                        else {
                                            self.detailUnemployeeInsuranceRateHistory(selectionUnemployeeInsuranceRateHistory);
                                        }
                                    }
                                };
                                //Clear show message error connection by server
                                ScreenModel.prototype.clearErrorSaveUnemployeeInsurance = function () {
                                    var self = this;
                                    $('.save-error').ntsError('clear');
                                    $('#btn_saveUnemployeeInsuranceHistory').ntsError('clear');
                                };
                                //show message save UnemployeeInsurance 
                                ScreenModel.prototype.showMessageSaveUnemployeeInsurance = function (messageId) {
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
                                };
                                //Clear show message error connection by server
                                ScreenModel.prototype.clearErrorSaveAccidentInsurance = function () {
                                    var self = this;
                                    $('.save-error').ntsError('clear');
                                    $('#btn_saveAccidentInsuranceHistory').ntsError('clear');
                                };
                                //show message save AccidentInsurance 
                                ScreenModel.prototype.showMessageSaveAccidentInsurance = function (messageId) {
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
                                };
                                ScreenModel.prototype.validateServer = function () {
                                    var self = this;
                                };
                                //action save UnemployeeInsuranceHistory Onlick connection service
                                ScreenModel.prototype.saveUnemployeeInsuranceHistory = function () {
                                    var self = this;
                                    if (nts.uk.ui._viewModel.errors.isEmpty()) {
                                    }
                                    // get type action (ismode)
                                    if (self.typeActionUnemployeeInsurance() == TypeActionInsuranceRate.add) {
                                        //type action is add
                                        //call service add UnemployeeInsuranceHistory
                                        a.service.addUnemployeeInsuranceRate(self.unemployeeInsuranceRateModel()).done(function (data) {
                                            //reload viewmodel
                                            self.reloadDataUnemployeeInsuranceRateByAction();
                                            //clear error viewmodel
                                            self.clearErrorSaveUnemployeeInsurance();
                                        }).fail(function (res) {
                                            //show message by exception message
                                            self.showMessageSaveUnemployeeInsurance(res.messageId);
                                            self.reloadDataUnemployeeInsuranceRateByAction();
                                        });
                                    }
                                    else {
                                        //type action is update
                                        //call service update UnemployeeInsuranceHistory
                                        a.service.updateUnemployeeInsuranceRate(self.unemployeeInsuranceRateModel()).done(function (data) {
                                            //reload viewmodel
                                            self.reloadDataUnemployeeInsuranceRateByAction();
                                            //clear error viewmodel
                                            self.clearErrorSaveUnemployeeInsurance();
                                        }).fail(function (res) {
                                            //show message by exception message
                                            self.showMessageSaveUnemployeeInsurance(res.messageId);
                                            self.reloadDataUnemployeeInsuranceRateByAction();
                                        });
                                    }
                                    return true;
                                };
                                //action save AccidentInsuranceHistory Onlick connection service
                                ScreenModel.prototype.saveAccidentInsuranceHistory = function () {
                                    var self = this;
                                    //get type action (ismode)
                                    if (self.typeActionAccidentInsurance() == TypeActionInsuranceRate.add) {
                                        //type action is add
                                        //call service add AccidentInsuranceRate
                                        a.service.addAccidentInsuranceRate(self.accidentInsuranceRateModel()).done(function (data) {
                                            //reload viewmodel
                                            self.reloadDataAccidentInsuranceRateByAction();
                                            //clear error viewmodel
                                            self.clearErrorSaveAccidentInsurance();
                                        }).fail(function (res) {
                                            //show message by exception message
                                            self.showMessageSaveAccidentInsurance(res.messageId);
                                        });
                                    }
                                    else {
                                        //type action is update
                                        //call service update AccidentInsuranceRate
                                        a.service.updateAccidentInsuranceRate(self.accidentInsuranceRateModel()).done(function (data) {
                                            //reload viewmodel
                                            self.reloadDataAccidentInsuranceRateByAction();
                                            //clear error viewmodel
                                            self.clearErrorSaveAccidentInsurance();
                                        }).fail(function (res) {
                                            //show message by exception message
                                            self.showMessageSaveAccidentInsurance(res.messageId);
                                        });
                                    }
                                    return true;
                                };
                                //show AccidentInsuranceHistory (change event)
                                ScreenModel.prototype.showchangeAccidentInsuranceHistory = function (selectionAccidentInsuranceRateHistory) {
                                    if (selectionAccidentInsuranceRateHistory != null
                                        && selectionAccidentInsuranceRateHistory != undefined
                                        && selectionAccidentInsuranceRateHistory != '') {
                                        var self = this;
                                        if (self.dirtyAccidentInsurance.isDirty() && self.isShowDirtyActionAccidentInsurance()) {
                                            if (selectionAccidentInsuranceRateHistory !== self.preSelectAccidentInsuranceRateHistory()
                                                && self.typeActionAccidentInsurance() == TypeActionInsuranceRate.update) {
                                                nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function () {
                                                    self.isShowDirtyActionAccidentInsurance(false);
                                                    self.detailAccidentInsuranceRateHistory(selectionAccidentInsuranceRateHistory);
                                                    return;
                                                }).ifNo(function () {
                                                    //No action
                                                });
                                                self.selectionAccidentInsuranceRateHistory(self.preSelectAccidentInsuranceRateHistory());
                                                return;
                                            }
                                        }
                                        self.detailAccidentInsuranceRateHistory(selectionAccidentInsuranceRateHistory);
                                    }
                                };
                                // startPage => show view model xhtml (constructor)
                                ScreenModel.prototype.startPage = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    //findAll history Unemployee Insurance Rate
                                    self.findAllUnemployeeInsuranceRateHistory().done(function (data) {
                                        //find All History Accident Insurance Rate
                                        self.findAllAccidentInsuranceRateHistory().done(function (data) {
                                            dfd.resolve(self);
                                        });
                                    });
                                    return dfd.promise();
                                };
                                //find add UnemployeeInsuranceRateHistory => show view model xhtml (constructor)
                                ScreenModel.prototype.findAllUnemployeeInsuranceRateHistory = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    //call service get all history unemployee insuranceRate
                                    a.service.findAllUnemployeeInsuranceRateHistory().done(function (data) {
                                        //get data respone
                                        if (data != null && data.length > 0) {
                                            //data[] is length > 0
                                            //set List history unemployee insurance rate by data
                                            self.lstUnemployeeInsuranceRateHistory(data);
                                            //set selection by fisrt data select
                                            self.selectionUnemployeeInsuranceRateHistory(data[0].historyId);
                                            //subscribe history selection
                                            if (self.isEmptyUnemployee()) {
                                                self.selectionUnemployeeInsuranceRateHistory.subscribe(function (selectionUnemployeeInsuranceRateHistory) {
                                                    self.showchangeUnemployeeInsuranceHistory(selectionUnemployeeInsuranceRateHistory);
                                                });
                                            }
                                            //set isEmptyUnemployee false (not isEmptyUnemployee)
                                            self.isEmptyUnemployee(false);
                                            //call detail history unemployee insurance rate
                                            self.detailUnemployeeInsuranceRateHistory(data[0].historyId).done(function () {
                                                //fw to start page
                                                dfd.resolve();
                                            });
                                        }
                                        else {
                                            //set history unemployee insurance rate is empty
                                            self.newmodelEmptyDataUnemployeeInsuranceRate();
                                            //fw to start page
                                            dfd.resolve();
                                        }
                                    });
                                    return dfd.promise();
                                };
                                //reload action
                                ScreenModel.prototype.reloadDataUnemployeeInsuranceRateByAction = function () {
                                    var self = this;
                                    //call service get all history unemployee insurance rate
                                    a.service.findAllUnemployeeInsuranceRateHistory().done(function (data) {
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
                                                self.selectionUnemployeeInsuranceRateHistory.subscribe(function (selectionUnemployeeInsuranceRateHistory) {
                                                    self.showchangeUnemployeeInsuranceHistory(self.selectionUnemployeeInsuranceRateHistory());
                                                });
                                                //set is empty 
                                                self.isEmptyUnemployee(false);
                                            }
                                            //set data List history unemployee insurance rate
                                            self.selectionUnemployeeInsuranceRateHistory(historyId);
                                            self.lstUnemployeeInsuranceRateHistory(data);
                                            //call detail history unemployee insurance rate
                                            self.detailUnemployeeInsuranceRateHistory(historyId).done(function (data) {
                                                //set enable input viewmodel
                                                self.unemployeeInsuranceRateModel().setEnable(true);
                                            });
                                            self.isEnableSaveUnemployeeInsurance(true);
                                            self.isEnableEditUnemployeeInsurance(true);
                                        }
                                        else {
                                            //set history unemployee insurance rate is empty
                                            self.newmodelEmptyDataUnemployeeInsuranceRate();
                                        }
                                    });
                                };
                                //new model data = []
                                ScreenModel.prototype.newmodelEmptyDataUnemployeeInsuranceRate = function () {
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
                                };
                                //reset value UnemployeeInsuranceRate
                                ScreenModel.prototype.resetValueUnemployeeInsuranceRate = function () {
                                    var self = this;
                                    //reset view model history unemployee insurance rate
                                    self.unemployeeInsuranceRateModel().resetValue(self.rateInputOptions, self.selectionRoundingMethod);
                                    //reset enable input viewmodel
                                    self.unemployeeInsuranceRateModel().setEnable(false);
                                    //set ismode type action add
                                    self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.add);
                                    //reset selection history unemployee insurance rate
                                    self.selectionUnemployeeInsuranceRateHistory('');
                                    self.dirtyUnemployeeInsurance.reset();
                                };
                                //detail UnemployeeInsuranceRateHistory => show view model xhtml (action event)
                                ScreenModel.prototype.detailUnemployeeInsuranceRateHistory = function (historyId) {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    if (historyId != null && historyId != undefined && historyId != '') {
                                        //call service detail history unemployee insurance rate by historyId
                                        a.service.detailUnemployeeInsuranceRateHistory(historyId).done(function (data) {
                                            //set viewmode by data
                                            self.unemployeeInsuranceRateModel().setListItem(data.rateItems);
                                            self.unemployeeInsuranceRateModel().setHistoryData(data.historyInsurance);
                                            //set ismode type action is update
                                            self.typeActionUnemployeeInsurance(TypeActionInsuranceRate.update);
                                            self.beginHistoryStartUnemployeeInsuranceRate(nts.uk.time.formatYearMonth(data.historyInsurance.startMonth));
                                            self.dirtyUnemployeeInsurance.reset();
                                            self.isShowDirtyUnemployeeInsurance(true);
                                            self.preSelectUnemployeeInsuranceRateHistory(historyId);
                                            dfd.resolve();
                                        });
                                    }
                                    return dfd.promise();
                                };
                                //find All AccidentInsuranceRateHistory => Show View model xhtml (constructor)
                                ScreenModel.prototype.findAllAccidentInsuranceRateHistory = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    //call service find all history accident insurance rate
                                    a.service.findAllAccidentInsuranceRateHistory().done(function (data) {
                                        //get data respone
                                        if (data != null && data.length > 0) {
                                            //data not null length > 0 => update List history accident insurance rate
                                            self.lstAccidentInsuranceRateHistory = ko.observableArray(data);
                                            self.selectionAccidentInsuranceRateHistory(data[0].historyId);
                                            //subscribe history accident insurance rate
                                            self.selectionAccidentInsuranceRateHistory.subscribe(function (selectionAccidentInsuranceRateHistory) {
                                                self.showchangeAccidentInsuranceHistory(selectionAccidentInsuranceRateHistory);
                                            });
                                            //set is emmpty history accident insurance rate
                                            self.isEmptyAccident(false);
                                            //call detail history accident insurance rate by historyId
                                            self.detailAccidentInsuranceRateHistory(data[0].historyId).done(function (data) {
                                                //call service get all insurance business type
                                                a.service.findAllInsuranceBusinessType().done(function (data) {
                                                    //update insurance business type
                                                    self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                                                    dfd.resolve(self);
                                                });
                                            });
                                            self.isEnableSaveActionAccidentInsurance(true);
                                        }
                                        else {
                                            //reset viewmode is empty
                                            self.newmodelEmptyDataAccidentInsuranceRate();
                                            dfd.resolve(self);
                                        }
                                    });
                                    return dfd.promise();
                                };
                                //reload action
                                ScreenModel.prototype.reloadDataAccidentInsuranceRateByAction = function () {
                                    var self = this;
                                    //call service find all  history accident insurance rate
                                    a.service.findAllAccidentInsuranceRateHistory().done(function (data) {
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
                                            self.detailAccidentInsuranceRateHistory(historyId).done(function (data) {
                                                //call service get all insurance business type
                                                a.service.findAllInsuranceBusinessType().done(function (data) {
                                                    //update insurance business type
                                                    self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                                                });
                                            });
                                            if (self.isEmptyAccident()) {
                                                self.selectionAccidentInsuranceRateHistory.subscribe(function (selectionAccidentInsuranceRateHistory) {
                                                    self.showchangeAccidentInsuranceHistory(selectionAccidentInsuranceRateHistory);
                                                });
                                                self.isEmptyAccident(false);
                                            }
                                            self.isEnableSaveActionAccidentInsurance(true);
                                            self.isEnableEditActionAccidentInsurance(true);
                                        }
                                        else {
                                            //reset viewmode is empty
                                            self.newmodelEmptyDataAccidentInsuranceRate();
                                        }
                                    });
                                };
                                //new model data = []
                                ScreenModel.prototype.newmodelEmptyDataAccidentInsuranceRate = function () {
                                    var self = this;
                                    self.selectionAccidentInsuranceRateHistory('');
                                    self.lstAccidentInsuranceRateHistory([]);
                                    self.resetValueAccidentInsuranceRate();
                                    a.service.findAllInsuranceBusinessType().done(function (data) {
                                        self.updateInsuranceBusinessTypeAccidentInsuranceDto(data);
                                    });
                                    self.isEmptyAccident(true);
                                    self.isEnableSaveActionAccidentInsurance(false);
                                    self.isEnableEditActionAccidentInsurance(false);
                                    self.accidentInsuranceRateModel().setEnable(false);
                                };
                                //reset value AccidentInsuranceRate
                                ScreenModel.prototype.resetValueAccidentInsuranceRate = function () {
                                    var self = this;
                                    self.selectionAccidentInsuranceRateHistory('');
                                    self.accidentInsuranceRateModel().resetValue(self.rateInputOptions, self.selectionRoundingMethod);
                                    self.typeActionAccidentInsurance(TypeActionInsuranceRate.add);
                                    self.dirtyAccidentInsurance.reset();
                                };
                                //detail AccidentInsuranceRateHistory => show view model xhtml (action event)
                                ScreenModel.prototype.detailAccidentInsuranceRateHistory = function (historyId) {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    if (historyId != null && historyId != undefined && historyId != '') {
                                        //call service find  accident insurance rate by historyId
                                        a.service.findAccidentInsuranceRate(historyId).done(function (data) {
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
                                };
                                //update InsuranceBusinessType by Model viewmodel
                                ScreenModel.prototype.updateInsuranceBusinessTypeAccidentInsurance = function (insuranceBusinessTypeUpdateModel) {
                                    var self = this;
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz1StModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz1St());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz2NdModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz2Nd());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz3RdModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz3Rd());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz4ThModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz4Th());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz5ThModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz5Th());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz6ThModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz6Th());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz7ThModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz7Th());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz8ThModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz8Th());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz9ThModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz9Th());
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz10ThModel
                                        .updateInsuranceBusinessType(insuranceBusinessTypeUpdateModel.bizNameBiz10Th());
                                };
                                //update insurance business type accident insurance
                                ScreenModel.prototype.updateInsuranceBusinessTypeAccidentInsuranceDto = function (InsuranceBusinessTypeDto) {
                                    var self = this;
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz1StModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz1St);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz2NdModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz2Nd);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz3RdModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz3Rd);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz4ThModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz4Th);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz5ThModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz5Th);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz6ThModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz6Th);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz7ThModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz7Th);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz8ThModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz8Th);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz9ThModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz9Th);
                                    self.accidentInsuranceRateModel()
                                        .accidentInsuranceRateBiz10ThModel
                                        .updateInsuranceBusinessType(InsuranceBusinessTypeDto.bizNameBiz10Th);
                                };
                                return ScreenModel;
                            }());
                            viewmodel.ScreenModel = ScreenModel;
                            var UnemployeeInsuranceRateItemSettingModel = (function () {
                                function UnemployeeInsuranceRateItemSettingModel() {
                                    this.roundAtr = ko.observable(0);
                                    this.rate = ko.observable(0);
                                    this.isEnable = ko.observable(true);
                                }
                                UnemployeeInsuranceRateItemSettingModel.prototype.resetValue = function () {
                                    if (this.roundAtr == null || this.roundAtr == undefined) {
                                        this.roundAtr = ko.observable(0);
                                    }
                                    else {
                                        this.roundAtr(0);
                                    }
                                    if (this.rate == null || this.rate == undefined) {
                                        this.rate = ko.observable(0);
                                    }
                                    else {
                                        this.rate(0);
                                    }
                                    if (this.isEnable == null || this.isEnable == undefined) {
                                        this.isEnable = ko.observable(false);
                                    }
                                    else {
                                        this.isEnable(false);
                                    }
                                };
                                UnemployeeInsuranceRateItemSettingModel.prototype.setItem = function (unemployeeInsuranceRateItemSetting) {
                                    this.roundAtr(unemployeeInsuranceRateItemSetting.roundAtr);
                                    this.rate(unemployeeInsuranceRateItemSetting.rate);
                                };
                                UnemployeeInsuranceRateItemSettingModel.prototype.setEnable = function (isEnable) {
                                    this.isEnable(isEnable);
                                };
                                return UnemployeeInsuranceRateItemSettingModel;
                            }());
                            viewmodel.UnemployeeInsuranceRateItemSettingModel = UnemployeeInsuranceRateItemSettingModel;
                            var UnemployeeInsuranceRateItemModel = (function () {
                                function UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod) {
                                    this.rateInputOptions = rateInputOptions;
                                    this.selectionRoundingMethod = selectionRoundingMethod;
                                    this.companySetting = new UnemployeeInsuranceRateItemSettingModel();
                                    this.personalSetting = new UnemployeeInsuranceRateItemSettingModel();
                                }
                                UnemployeeInsuranceRateItemModel.prototype.resetValue = function () {
                                    if (this.companySetting == null || this.companySetting == undefined) {
                                        this.companySetting = new UnemployeeInsuranceRateItemSettingModel();
                                        this.companySetting.resetValue();
                                    }
                                    else {
                                        this.companySetting.resetValue();
                                    }
                                    if (this.personalSetting == null || this.personalSetting == undefined) {
                                        this.personalSetting = new UnemployeeInsuranceRateItemSettingModel();
                                        this.personalSetting.resetValue();
                                    }
                                    else {
                                        this.personalSetting.resetValue();
                                    }
                                };
                                UnemployeeInsuranceRateItemModel.prototype.setItem = function (unemployeeInsuranceRateItemDto) {
                                    this.companySetting.setItem(unemployeeInsuranceRateItemDto.companySetting);
                                    this.personalSetting.setItem(unemployeeInsuranceRateItemDto.personalSetting);
                                };
                                UnemployeeInsuranceRateItemModel.prototype.setEnable = function (isEnable) {
                                    this.companySetting.setEnable(isEnable);
                                    this.personalSetting.setEnable(isEnable);
                                };
                                return UnemployeeInsuranceRateItemModel;
                            }());
                            viewmodel.UnemployeeInsuranceRateItemModel = UnemployeeInsuranceRateItemModel;
                            var UnemployeeInsuranceHistoryModel = (function () {
                                function UnemployeeInsuranceHistoryModel() {
                                    this.historyId = ko.observable('');
                                    this.startJapStartMonth = ko.observable('');
                                    this.endMonthRage = ko.observable('');
                                    this.endMonth = ko.observable(0);
                                    this.startMonth = ko.observable(0);
                                }
                                UnemployeeInsuranceHistoryModel.prototype.resetValue = function () {
                                    if (this.historyId != null && this.historyId != undefined) {
                                        this.historyId('');
                                    }
                                    else {
                                        this.historyId = ko.observable('');
                                    }
                                    if (this.startJapStartMonth != null && this.startJapStartMonth != undefined) {
                                        this.startJapStartMonth('');
                                    }
                                    else {
                                        this.startJapStartMonth = ko.observable('');
                                    }
                                    if (this.endMonthRage != null && this.endMonthRage != undefined) {
                                        this.endMonthRage('9999/12');
                                    }
                                    else {
                                        this.endMonthRage = ko.observable('9999/12');
                                    }
                                    if (this.endMonth != null && this.endMonth != undefined) {
                                        this.endMonth(999912);
                                    }
                                    else {
                                        this.endMonth = ko.observable(999912);
                                    }
                                    if (this.startMonth != null && this.startMonth != undefined) {
                                        this.endMonth(0);
                                    }
                                    else {
                                        this.startMonth = ko.observable(0);
                                    }
                                };
                                UnemployeeInsuranceHistoryModel.prototype.updateData = function (unemployeeInsuranceHistory) {
                                    this.historyId(unemployeeInsuranceHistory.historyId);
                                    this.startJapStartMonth(nts.uk.time.formatYearMonth(unemployeeInsuranceHistory.startMonth)
                                        + ' (' + nts.uk.time.yearmonthInJapanEmpire(unemployeeInsuranceHistory.startMonth).toString() + ') ');
                                    this.endMonthRage(nts.uk.time.formatYearMonth(unemployeeInsuranceHistory.endMonth));
                                    this.startMonth(unemployeeInsuranceHistory.startMonth);
                                    this.endMonth(unemployeeInsuranceHistory.endMonth);
                                };
                                UnemployeeInsuranceHistoryModel.prototype.setMonthRage = function (startDate, endDate) {
                                    this.startMonth(startDate);
                                    this.endMonth(endDate);
                                };
                                return UnemployeeInsuranceHistoryModel;
                            }());
                            viewmodel.UnemployeeInsuranceHistoryModel = UnemployeeInsuranceHistoryModel;
                            var UnemployeeInsuranceRateModel = (function () {
                                function UnemployeeInsuranceRateModel(rateInputOptions, selectionRoundingMethod) {
                                    this.unemployeeInsuranceRateItemAgroforestryModel
                                        = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                                    this.unemployeeInsuranceRateItemContructionModel
                                        = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                                    this.unemployeeInsuranceRateItemOtherModel
                                        = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                                    this.version = ko.observable(0);
                                    this.unemployeeInsuranceHistoryModel = new UnemployeeInsuranceHistoryModel();
                                }
                                UnemployeeInsuranceRateModel.prototype.resetValue = function (rateInputOptions, selectionRoundingMethod) {
                                    if (this.unemployeeInsuranceRateItemAgroforestryModel == null
                                        || this.unemployeeInsuranceRateItemAgroforestryModel == undefined) {
                                        this.unemployeeInsuranceRateItemAgroforestryModel
                                            = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                                        this.unemployeeInsuranceRateItemAgroforestryModel.resetValue();
                                    }
                                    else {
                                        this.unemployeeInsuranceRateItemAgroforestryModel.resetValue();
                                    }
                                    if (this.unemployeeInsuranceRateItemContructionModel == null
                                        || this.unemployeeInsuranceRateItemContructionModel == undefined) {
                                        this.unemployeeInsuranceRateItemContructionModel
                                            = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                                        this.unemployeeInsuranceRateItemContructionModel.resetValue();
                                    }
                                    else {
                                        this.unemployeeInsuranceRateItemContructionModel.resetValue();
                                    }
                                    if (this.unemployeeInsuranceRateItemOtherModel == null
                                        || this.unemployeeInsuranceRateItemOtherModel == undefined) {
                                        this.unemployeeInsuranceRateItemOtherModel
                                            = new UnemployeeInsuranceRateItemModel(rateInputOptions, selectionRoundingMethod);
                                        this.unemployeeInsuranceRateItemOtherModel.resetValue();
                                    }
                                    else {
                                        this.unemployeeInsuranceRateItemOtherModel.resetValue();
                                    }
                                    if (this.unemployeeInsuranceHistoryModel == null
                                        || this.unemployeeInsuranceHistoryModel == undefined) {
                                        this.unemployeeInsuranceHistoryModel = new UnemployeeInsuranceHistoryModel();
                                    }
                                    else {
                                        this.unemployeeInsuranceHistoryModel.resetValue();
                                    }
                                };
                                UnemployeeInsuranceRateModel.prototype.setHistoryData = function (UnemployeeInsuranceHistoryDto) {
                                    this.unemployeeInsuranceHistoryModel.updateData(UnemployeeInsuranceHistoryDto);
                                };
                                UnemployeeInsuranceRateModel.prototype.setListItem = function (rateItems) {
                                    if (rateItems != null && rateItems.length > 0) {
                                        for (var _i = 0, rateItems_1 = rateItems; _i < rateItems_1.length; _i++) {
                                            var rateItem = rateItems_1[_i];
                                            //Agroforestry
                                            if (rateItem.careerGroup == CareerGroupDto.Agroforestry) {
                                                this.unemployeeInsuranceRateItemAgroforestryModel.setItem(rateItem);
                                            }
                                            else if (rateItem.careerGroup == CareerGroupDto.Contruction) {
                                                this.unemployeeInsuranceRateItemContructionModel.setItem(rateItem);
                                            }
                                            else if (rateItem.careerGroup == CareerGroupDto.Other) {
                                                this.unemployeeInsuranceRateItemOtherModel.setItem(rateItem);
                                            }
                                        }
                                    }
                                };
                                UnemployeeInsuranceRateModel.prototype.setVersion = function (version) {
                                    this.version(version);
                                };
                                UnemployeeInsuranceRateModel.prototype.setEnable = function (isEnable) {
                                    this.unemployeeInsuranceRateItemAgroforestryModel.setEnable(isEnable);
                                    this.unemployeeInsuranceRateItemContructionModel.setEnable(isEnable);
                                    this.unemployeeInsuranceRateItemOtherModel.setEnable(isEnable);
                                };
                                return UnemployeeInsuranceRateModel;
                            }());
                            viewmodel.UnemployeeInsuranceRateModel = UnemployeeInsuranceRateModel;
                            var AccidentInsuranceRateDetailModel = (function () {
                                function AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod) {
                                    this.rateInputOptions = rateInputOptions;
                                    this.selectionRoundingMethod = selectionRoundingMethod;
                                    this.insuranceBusinessType = ko.observable('');
                                    this.insuRate = ko.observable(0);
                                    this.insuRound = ko.observable(0);
                                    this.isEnable = ko.observable(true);
                                }
                                //Fuction update value insuranceBusinessType
                                AccidentInsuranceRateDetailModel.prototype.updateInsuranceBusinessType = function (insuranceBusinessType) {
                                    if (this.insuranceBusinessType() != null && this.insuranceBusinessType() != undefined) {
                                        this.insuranceBusinessType(insuranceBusinessType);
                                    }
                                };
                                AccidentInsuranceRateDetailModel.prototype.setItem = function (insuBizRateItem) {
                                    this.insuRate(insuBizRateItem.insuRate);
                                    this.insuRound(insuBizRateItem.insuRound);
                                };
                                AccidentInsuranceRateDetailModel.prototype.resetValue = function () {
                                    if (this.insuRate == null || this.insuRate == undefined) {
                                        this.insuRate = ko.observable(0);
                                    }
                                    else {
                                        this.insuRate(0);
                                    }
                                    if (this.insuRound == null || this.insuRound == undefined) {
                                        this.insuRound = ko.observable(0);
                                    }
                                    else {
                                        this.insuRate(0);
                                    }
                                    if (this.isEnable == null || this.isEnable == undefined) {
                                        this.isEnable = ko.observable(true);
                                    }
                                    else {
                                        this.isEnable(true);
                                    }
                                };
                                AccidentInsuranceRateDetailModel.prototype.setEnable = function (isEnable) {
                                    this.isEnable(isEnable);
                                };
                                return AccidentInsuranceRateDetailModel;
                            }());
                            viewmodel.AccidentInsuranceRateDetailModel = AccidentInsuranceRateDetailModel;
                            var AccidentInsuranceRateHistoryModel = (function () {
                                function AccidentInsuranceRateHistoryModel() {
                                    this.historyId = ko.observable('');
                                    this.startMonthRage = ko.observable('');
                                    this.endMonthRage = ko.observable('');
                                    this.endMonth = ko.observable(0);
                                    this.startMonth = ko.observable(0);
                                }
                                AccidentInsuranceRateHistoryModel.prototype.resetValue = function () {
                                    if (this.historyId != null && this.historyId != undefined) {
                                        this.historyId('');
                                    }
                                    else {
                                        this.historyId = ko.observable('');
                                    }
                                    if (this.startMonthRage != null && this.startMonthRage != undefined) {
                                        this.startMonthRage('');
                                    }
                                    else {
                                        this.startMonthRage = ko.observable('');
                                    }
                                    if (this.endMonthRage != null && this.endMonthRage != undefined) {
                                        this.endMonthRage('9999/12');
                                    }
                                    else {
                                        this.endMonthRage = ko.observable('9999/12');
                                    }
                                    if (this.startMonth != null && this.startMonth != undefined) {
                                        this.startMonth = ko.observable(0);
                                    }
                                    else {
                                        this.startMonth(0);
                                    }
                                    if (this.endMonth != null && this.endMonth != undefined) {
                                        this.endMonth = ko.observable(0);
                                    }
                                    else {
                                        this.endMonth(0);
                                    }
                                };
                                AccidentInsuranceRateHistoryModel.prototype.updateData = function (historyDto) {
                                    this.resetValue();
                                    this.historyId(historyDto.historyId);
                                    this.startMonth(historyDto.startMonth);
                                    this.endMonth(historyDto.endMonth);
                                    this.startMonthRage(nts.uk.time.formatYearMonth(historyDto.startMonth)
                                        + ' (' + nts.uk.time.yearmonthInJapanEmpire(historyDto.startMonth).toString() + ') ');
                                    this.endMonthRage(nts.uk.time.formatYearMonth(historyDto.endMonth));
                                };
                                return AccidentInsuranceRateHistoryModel;
                            }());
                            viewmodel.AccidentInsuranceRateHistoryModel = AccidentInsuranceRateHistoryModel;
                            var AccidentInsuranceRateModel = (function () {
                                function AccidentInsuranceRateModel(rateInputOptions, selectionRoundingMethod) {
                                    this.accidentInsuranceRateBiz1StModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz2NdModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz3RdModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz4ThModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz5ThModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz6ThModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz7ThModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz8ThModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz9ThModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateBiz10ThModel =
                                        new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                    this.accidentInsuranceRateHistoryModel = new AccidentInsuranceRateHistoryModel();
                                }
                                AccidentInsuranceRateModel.prototype.setListItem = function (lstInsuBizRateItem) {
                                    for (var _i = 0, lstInsuBizRateItem_1 = lstInsuBizRateItem; _i < lstInsuBizRateItem_1.length; _i++) {
                                        var rateItem = lstInsuBizRateItem_1[_i];
                                        //Biz1St
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz1St) {
                                            this.accidentInsuranceRateBiz1StModel.setItem(rateItem);
                                        }
                                        //Biz2Nd
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz2Nd) {
                                            this.accidentInsuranceRateBiz2NdModel.setItem(rateItem);
                                        }
                                        //Biz3Rd
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz3Rd) {
                                            this.accidentInsuranceRateBiz3RdModel.setItem(rateItem);
                                        }
                                        //Biz4Th
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz4Th) {
                                            this.accidentInsuranceRateBiz4ThModel.setItem(rateItem);
                                        }
                                        //Biz5Th
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz5Th) {
                                            this.accidentInsuranceRateBiz5ThModel.setItem(rateItem);
                                        }
                                        //Biz6Th
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz6Th) {
                                            this.accidentInsuranceRateBiz6ThModel.setItem(rateItem);
                                        }
                                        //Biz7Th
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz7Th) {
                                            this.accidentInsuranceRateBiz7ThModel.setItem(rateItem);
                                        }
                                        //Biz8Th
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz8Th) {
                                            this.accidentInsuranceRateBiz8ThModel.setItem(rateItem);
                                        }
                                        //Biz9Th
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz9Th) {
                                            this.accidentInsuranceRateBiz9ThModel.setItem(rateItem);
                                        }
                                        //Biz10Th
                                        if (rateItem.insuBizType == BusinessTypeEnumDto.Biz10Th) {
                                            this.accidentInsuranceRateBiz10ThModel.setItem(rateItem);
                                        }
                                    }
                                };
                                AccidentInsuranceRateModel.prototype.setVersion = function (version) {
                                    if (this.version == null || this.version == undefined) {
                                        this.version = ko.observable(version);
                                    }
                                    else {
                                        this.version(version);
                                    }
                                };
                                AccidentInsuranceRateModel.prototype.setHistoryData = function (historyDto) {
                                    this.accidentInsuranceRateHistoryModel.updateData(historyDto);
                                };
                                AccidentInsuranceRateModel.prototype.resetValue = function (rateInputOptions, selectionRoundingMethod) {
                                    if (this.accidentInsuranceRateBiz1StModel == null
                                        || this.accidentInsuranceRateBiz1StModel == undefined) {
                                        this.accidentInsuranceRateBiz1StModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz1StModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz1StModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz2NdModel == null
                                        || this.accidentInsuranceRateBiz2NdModel == undefined) {
                                        this.accidentInsuranceRateBiz2NdModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz2NdModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz2NdModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz3RdModel == null
                                        || this.accidentInsuranceRateBiz3RdModel == undefined) {
                                        this.accidentInsuranceRateBiz3RdModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz3RdModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz3RdModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz4ThModel == null
                                        || this.accidentInsuranceRateBiz4ThModel == undefined) {
                                        this.accidentInsuranceRateBiz4ThModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz4ThModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz4ThModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz5ThModel == null
                                        || this.accidentInsuranceRateBiz5ThModel == undefined) {
                                        this.accidentInsuranceRateBiz5ThModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz5ThModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz5ThModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz6ThModel == null
                                        || this.accidentInsuranceRateBiz6ThModel == undefined) {
                                        this.accidentInsuranceRateBiz6ThModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz6ThModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz6ThModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz7ThModel == null
                                        || this.accidentInsuranceRateBiz7ThModel == undefined) {
                                        this.accidentInsuranceRateBiz7ThModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz7ThModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz7ThModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz8ThModel == null
                                        || this.accidentInsuranceRateBiz8ThModel == undefined) {
                                        this.accidentInsuranceRateBiz8ThModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz8ThModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz8ThModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz9ThModel == null
                                        || this.accidentInsuranceRateBiz9ThModel == undefined) {
                                        this.accidentInsuranceRateBiz9ThModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz9ThModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz9ThModel.resetValue();
                                    }
                                    if (this.accidentInsuranceRateBiz10ThModel == null
                                        || this.accidentInsuranceRateBiz10ThModel == undefined) {
                                        this.accidentInsuranceRateBiz10ThModel
                                            = new AccidentInsuranceRateDetailModel(rateInputOptions, selectionRoundingMethod);
                                        this.accidentInsuranceRateBiz10ThModel.resetValue();
                                    }
                                    else {
                                        this.accidentInsuranceRateBiz10ThModel.resetValue();
                                    }
                                    if (this.version == null || this.version == undefined) {
                                        this.version = ko.observable(0);
                                    }
                                    else {
                                        this.version(0);
                                    }
                                    if (this.accidentInsuranceRateHistoryModel == null
                                        || this.accidentInsuranceRateHistoryModel == undefined) {
                                        this.accidentInsuranceRateHistoryModel
                                            = new AccidentInsuranceRateHistoryModel();
                                    }
                                    else {
                                        this.accidentInsuranceRateHistoryModel.resetValue();
                                    }
                                };
                                AccidentInsuranceRateModel.prototype.setEnable = function (isEnable) {
                                    this.accidentInsuranceRateBiz1StModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz2NdModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz3RdModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz4ThModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz5ThModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz6ThModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz7ThModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz8ThModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz9ThModel.setEnable(isEnable);
                                    this.accidentInsuranceRateBiz10ThModel.setEnable(isEnable);
                                };
                                return AccidentInsuranceRateModel;
                            }());
                            viewmodel.AccidentInsuranceRateModel = AccidentInsuranceRateModel;
                        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
                    })(a = qmm011.a || (qmm011.a = {}));
                })(qmm011 = view.qmm011 || (view.qmm011 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=qmm011.a.vm.js.map