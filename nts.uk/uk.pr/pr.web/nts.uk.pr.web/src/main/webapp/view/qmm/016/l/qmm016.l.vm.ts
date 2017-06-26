module nts.uk.pr.view.qmm016.l {

    import option = nts.uk.ui.option;
    import MultipleTargetSettingDto = service.model.MultipleTargetSettingDto;
    import MultipleTargetSetting = service.model.MultipleTargetSetting;
    import CertificationFindInDto = service.model.CertificationFindInDto;
    import CertifyGroupFindOutDto = service.model.CertifyGroupFindOutDto;
    import CertifyGroupDto = service.model.CertifyGroupDto;
    import TypeActionCertifyGroup = service.model.TypeActionCertifyGroup;
    import CertifyGroupDeleteDto = service.model.CertifyGroupDeleteDto;

    export module viewmodel {

        export class ScreenModel {

            showDelete: KnockoutObservable<boolean>;
            typeAction: KnockoutObservable<number>;
            selectedMultipleTargetSetting: KnockoutObservable<number>;
            selectLstCodeLstCertification: KnockoutObservableArray<string>;
            lstCertifyGroup: KnockoutObservableArray<CertifyGroupFindOutDto>;
            columnsLstCertifyGroup: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            selectCodeLstLstCertifyGroup: KnockoutObservable<string>;
            selectLstCodeCertifyGroupnPre: KnockoutObservable<string>;
            lstCertification: CertificationFindInDto[];
            //Info CertifyGroup (DTO View)
            certifyGroupModel: KnockoutObservable<CertifyGroupModel>;
            textEditorOption: KnockoutObservable<option.TextEditorOption>;
            isEmpty: KnockoutObservable<boolean>;
            isShowDirty: KnockoutObservable<boolean>;
            messageList: KnockoutObservableArray<any>;
            dirty: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                self.columnsLstCertifyGroup = ko.observableArray<nts.uk.ui.NtsGridListColumn>([
                    { headerText: 'コード', key: 'code', width: 120 },
                    { headerText: '名称', key: 'name', width: 120 }
                ]);
                self.selectedMultipleTargetSetting = ko.observable(MultipleTargetSetting.BigestMethod);
                self.selectCodeLstLstCertifyGroup = ko.observable('');
                self.selectLstCodeLstCertification = ko.observableArray([]);
                self.typeAction = ko.observable(TypeActionCertifyGroup.update);
                self.textEditorOption = ko.mapping.fromJS(new option.TextEditorOption());
                self.isEmpty = ko.observable(true);
                self.messageList = ko.observableArray([
                    { messageId: "ER001", message: "＊が入力されていません。" },
                    { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                    { messageId: "AL001", message: "変更された内容が登録されていません。\r\n よろしいですか。" },
                    { messageId: "ER010", message: "対象データがありません。" },
                    { messageId: "AL002", message: "データを削除します。\r\n よろしいですか？。" },
                    { messageId: "ER026", message: "データを削除します。\r\n よろしいですか？。" }
                ]);
                self.showDelete = ko.observable(true);
                self.certifyGroupModel = ko.observable(new CertifyGroupModel(new CertifyGroupDto()));
                self.dirty = new nts.uk.ui.DirtyChecker(self.certifyGroupModel);
                self.selectLstCodeCertifyGroupnPre = ko.observable('');
                self.isShowDirty = ko.observable(true);
            }

            //start page init data begin load page
            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                self.findAllCertifyGroup().done(function() {
                    self.findAllCertification().done(data => {
                        dfd.resolve(data);
                    });

                });
                return dfd.promise();
            }

            // find all Certification connection service
            private findAllCertification(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                service.findAllCertification().done(data => {
                    self.lstCertification = data;
                    dfd.resolve(self);
                });
                return dfd.promise();
            }

            //find all CertifyGroup connection service
            private findAllCertifyGroup(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                service.findAllCertifyGroup().done(data => {
                    if (data != null && data.length > 0) {
                        self.lstCertifyGroup = ko.observableArray<CertifyGroupFindOutDto>(data);
                        self.selectCodeLstLstCertifyGroup(data[0].code);
                        if (self.isEmpty()) {
                            self.selectCodeLstLstCertifyGroup.subscribe(function(code: string) {
                                self.showchangeCertifyGroup(code);
                            });
                            self.isEmpty(false);
                        }
                        self.detailCertifyGroup(data[0].code);
                        dfd.resolve(self);
                    } else {
                        self.newmodeEmptyData();
                        self.lstCertifyGroup = ko.observableArray([]);
                        dfd.resolve(self);
                    }
                });
                return dfd.promise();
            }

            //detail CertifyGroup by code
            private findCertifyGroup(code: string): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                service.findCertifyGroup(code).done(data => {
                    self.certifyGroupModel().updateData(data);
                    service.findAllCertification().done(data => {
                        self.certifyGroupModel().lstCertification(data);
                        self.lstCertification = data;
                        dfd.resolve(self);
                    });

                });
                return dfd.promise();
            }

            private detailCertifyGroup(code: string) {
                if (code && code != '') {
                    var self = this;
                    service.findCertifyGroup(code).done(data => {
                        if (self.isEmpty()) {
                            self.selectCodeLstLstCertifyGroup(code);
                            self.selectCodeLstLstCertifyGroup.subscribe(function(code: string) {
                                self.showchangeCertifyGroup(code);
                            });
                            self.isEmpty(false);
                        }
                        self.certifyGroupModel().updateData(data);
                        self.typeAction(TypeActionCertifyGroup.update);
                        self.certifyGroupModel().setReadOnly(true);
                        self.showDelete(true);
                        service.findAllCertification().done(dataCertification => {
                            self.certifyGroupModel().setLstCertification(dataCertification);
                            self.isShowDirty(true);
                            self.dirty.reset();
                            self.selectCodeLstLstCertifyGroup(code);
                            self.selectLstCodeCertifyGroupnPre(code);
                        }).fail(function() {
                            self.dirty.reset();
                        });
                    });
                }
            }

            //show CertifyGroup (change event)
            private showchangeCertifyGroup(selectionCodeLstLstCertifyGroup: string) {
                var self = this;
                // type action add (new mode)
                debugger;
                if (selectionCodeLstLstCertifyGroup) {
                    if (self.typeAction() == TypeActionCertifyGroup.add) {
                        if (self.dirty.isDirty() && self.isShowDirty()) {
                            nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                                self.isShowDirty(false);
                                self.typeAction(TypeActionCertifyGroup.update);
                                self.detailCertifyGroup(selectionCodeLstLstCertifyGroup);
                            }).ifNo(function() {
                                self.isShowDirty(false);
                                self.selectCodeLstLstCertifyGroup(self.selectLstCodeCertifyGroupnPre());
                                self.isShowDirty(true);
                            });
                        } else {
                            self.typeAction(TypeActionCertifyGroup.update);
                            self.detailCertifyGroup(selectionCodeLstLstCertifyGroup);
                        }
                    } else {
                        // type action update (update mode)
                        if (self.dirty.isDirty() && self.isShowDirty()) {
                            if (selectionCodeLstLstCertifyGroup !== self.selectLstCodeCertifyGroupnPre()) {
                                nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                                    self.isShowDirty(false);
                                    self.typeAction(TypeActionCertifyGroup.update);
                                    self.detailCertifyGroup(selectionCodeLstLstCertifyGroup);
                                }).ifNo(function() {
                                    self.selectCodeLstLstCertifyGroup(self.selectLstCodeCertifyGroupnPre());
                                });
                            }
                        } else {
                            self.typeAction(TypeActionCertifyGroup.update);
                            self.detailCertifyGroup(selectionCodeLstLstCertifyGroup);
                        }
                    }
                }
            }

            //reset value => begin add button
            private resetValueCertifyGroup() {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                        self.onResetValueCertifyGroup();
                    }).ifNo(function() {
                        //No action
                    });
                } else {
                    self.onResetValueCertifyGroup();
                }
            }


            //reset value => begin add button
            private onResetValueCertifyGroup() {
                var self = this;
                if (self.certifyGroupModel == null || self.certifyGroupModel == undefined) {
                    self.certifyGroupModel = ko.observable(new CertifyGroupModel(new CertifyGroupDto()));
                }
                self.certifyGroupModel().code('');
                self.certifyGroupModel().name('');
                self.certifyGroupModel().multiApplySet(MultipleTargetSetting.BigestMethod);
                self.typeAction(TypeActionCertifyGroup.add);
                self.selectCodeLstLstCertifyGroup('');
                self.certifyGroupModel().setReadOnly(false);
                self.certifyGroupModel().certifies([]);
                self.showDelete(false);
                self.clearErrorSave();
                service.findAllCertification().done(data => {
                    self.certifyGroupModel().lstCertification(data);
                    self.dirty.reset();
                });
            }

            private saveCertifyGroup() {
                var self = this;
                if (self.validateData()) {
                    return;
                }

                if (self.typeAction() == TypeActionCertifyGroup.add) {
                    service.addCertifyGroup(self.convertDataModel()).done(function() {
                        self.reloadDataByAction(self.certifyGroupModel().code());
                    }).fail(function(error) {
                        self.showMessageSave(error.messageId);
                    })
                } else {
                    service.updateCertifyGroup(self.convertDataModel()).done(function() {
                        self.reloadDataByAction(self.certifyGroupModel().code());
                    }).fail(function(error) {
                        self.clearErrorSave();
                        self.showMessageSave(error.messageId);
                    })
                }
            }

            //reload action
            private reloadDataByAction(code: string) {
                var self = this;
                service.findAllCertifyGroup().done(data => {
                    self.lstCertifyGroup(data);
                    if (code && code != '') {
                        self.detailCertifyGroup(code);
                    } else {
                        if (data != null && data.length > 0) {
                            self.detailCertifyGroup(data[0].code);
                        } else {
                            self.newmodeEmptyData();
                            self.lstCertifyGroup = ko.observableArray([]);
                        }
                    }
                });
            }

            //new mode empty data
            private newmodeEmptyData() {
                var self = this;
                service.findAllCertification().done(data => {
                    self.lstCertification = data;
                    self.onResetValueCertifyGroup();
                    self.isEmpty(true);
                    self.certifyGroupModel().setReadOnly(false);
                    self.showDelete(false);
                });
            }

            private deleteCertifyGroup() {
                var self = this;
                nts.uk.ui.dialog.confirm(self.messageList()[4].message).ifYes(function() {
                    var certifyGroupDeleteDto: CertifyGroupDeleteDto = new CertifyGroupDeleteDto();
                    certifyGroupDeleteDto.groupCode = self.certifyGroupModel().code();
                    certifyGroupDeleteDto.version = 12;
                    service.deleteCertifyGroup(certifyGroupDeleteDto).done(function() {
                        self.reloadDataByAction('');
                    });
                }).ifNo(function() {
                    self.reloadDataByAction(self.selectCodeLstLstCertifyGroup());
                }).ifCancel(function() {
                    self.reloadDataByAction(self.selectCodeLstLstCertifyGroup());
                }).then(function() {
                    self.reloadDataByAction(self.selectCodeLstLstCertifyGroup());
                })
            }

            private closeCertifyGroup() {
                nts.uk.ui.windows.close();
            }

            //convert data model => Dto
            private convertDataModel(): CertifyGroupDto {
                var self = this;
                var certifyGroupDto: CertifyGroupDto = new CertifyGroupDto();
                certifyGroupDto.code = self.certifyGroupModel().code();
                certifyGroupDto.name = self.certifyGroupModel().name();
                certifyGroupDto.multiApplySet = self.certifyGroupModel().multiApplySet();
                certifyGroupDto.certifies = self.certifyGroupModel().certifies();
                return certifyGroupDto;
            }

            //show message by connection service => respone error
            private showMessageSave(messageId: string) {
                var self = this;
                if (messageId == self.messageList()[0].messageId) {
                    if (!self.certifyGroupModel().code()) {
                        $('#inp_code').ntsError('set', self.messageList()[0].message);
                    }
                    if (!self.certifyGroupModel().name()) {
                        $('#inp_name').ntsError('set', self.messageList()[0].message);
                    }
                }
                if (messageId == self.messageList()[1].messageId) {
                    $('#inp_code').ntsError('set', self.messageList()[1].message);
                }
                if (messageId == self.messageList()[5].messageId) {
                    $('#inp_code').ntsError('set', self.messageList()[5].message);
                }
            }

            //clear error view 
            private clearErrorSave() {
                $('.save-error').ntsError('clear');
                $('#btn_saveCertifyGroup').ntsError('clear');
            }

            //validate client
            private validateData(): boolean {
                $("#inp_code").ntsEditor("validate");
                $("#inp_name").ntsEditor("validate");
                if ($('.nts-editor').ntsError("hasError")) {
                    return true;
                }
                return false;
            }
        }

        export class CertifyGroupModel {

            code: KnockoutObservable<string>;
            name: KnockoutObservable<string>;
            isEnable: KnockoutObservable<boolean>;
            multiApplySet: KnockoutObservable<number>;
            certifies: KnockoutObservableArray<CertificationFindInDto>;
            lstCertification: KnockoutObservableArray<CertificationFindInDto>;
            columnsCertification: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            selectionMultipleTargetSetting: KnockoutObservableArray<MultipleTargetSettingDto>;

            constructor(certifyGroupDto: CertifyGroupDto) {
                this.code = ko.observable(certifyGroupDto.code);
                this.name = ko.observable(certifyGroupDto.name);
                this.multiApplySet = ko.observable(certifyGroupDto.multiApplySet);
                this.columnsCertification = ko.observableArray<nts.uk.ui.NtsGridListColumn>([
                    { headerText: 'コード', key: 'code', width: 60 },
                    { headerText: '名称', key: 'name', width: 180 }
                ]);
                this.selectionMultipleTargetSetting = ko.observableArray<MultipleTargetSettingDto>(
                    [new MultipleTargetSettingDto(MultipleTargetSetting.BigestMethod, "一番高い手当を1つだけ支給する"),
                        new MultipleTargetSettingDto(MultipleTargetSetting.TotalMethod, "複数該当した金額を加算する")
                    ]);
                this.certifies = ko.observableArray<CertificationFindInDto>(certifyGroupDto.certifies);
                this.lstCertification = ko.observableArray<CertificationFindInDto>([]);
                this.isEnable = ko.observable(false);
            }

            updateData(certifyGroupDto: CertifyGroupDto) {
                this.code(certifyGroupDto.code);
                this.name(certifyGroupDto.name);
                this.multiApplySet(certifyGroupDto.multiApplySet);
                this.certifies(certifyGroupDto.certifies);
                this.lstCertification([]);
                this.isEnable(false);
            }

            setLstCertification(lstCertification: CertificationFindInDto[]) {
                this.lstCertification(lstCertification);
            }

            setReadOnly(readonly: boolean) {
                this.isEnable(!readonly);
            }
        }
    }
}