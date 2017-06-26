module cmm014.a.viewmodel {

    export class ScreenModel {

        dataSource: KnockoutObservableArray<viewmodel.model.ClassificationDto>;
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode: KnockoutObservable<string>;
        currentCodeList: KnockoutObservableArray<any>;
        currentItem: KnockoutObservable<viewmodel.model.InputField>;
        multilineeditor: KnockoutObservable<any>;
        hasCellphone: KnockoutObservable<boolean>;
        index_of_itemDelete: any;
        itemdata_add: any;
        itemdata_update: any;
        dirty: nts.uk.ui.DirtyChecker;
        notAlert: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.dataSource = ko.observableArray([]);
            self.columns = ko.observableArray([
                { headerText: 'コード', key: 'classificationCode', width: 100 },
                { headerText: '名称', key: 'classificationName', width: 80 }

            ]);

            self.currentCode = ko.observable(null);
            self.currentCodeList = ko.observableArray([]);
            self.currentItem = ko.observable(new viewmodel.model.InputField(new viewmodel.model.ClassificationDto(), true));
            self.multilineeditor = ko.observable(null);
            self.itemdata_add = ko.observable(null);
            self.itemdata_update = ko.observable(null);
            self.hasCellphone = ko.observable(true);
            self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
            self.notAlert = ko.observable(true);

            self.currentCode.subscribe((function(codeChanged) {
                //self.currentItem(self.findObj(codeChanged));
                if (codeChanged == null) {
                    return;
                }
                if (!self.notAlert()) {
                    self.notAlert(true);
                    return;
                }
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                        self.currentItem(new viewmodel.model.InputField(self.findObj(codeChanged), false));
                        self.dirty.reset();
                    }).ifNo(function() {
                        self.notAlert(false);
                        self.currentCode(self.currentItem().INP_002_code());
                    });
                } else {
                    self.currentItem(new viewmodel.model.InputField(self.findObj(codeChanged), false));
                    self.dirty.reset();
                }

                if (self.currentItem() != null) {
                    self.hasCellphone(true);
                }
            }));
        }

        findObj(value: string): any {
            let self = this;
            var itemModel = null;
            _.find(self.dataSource(), function(obj: viewmodel.model.ClassificationDto) {
                if (obj.classificationCode == value) {
                    itemModel = obj;
                }
            })
            return itemModel;
        }

        initRegisterClassification() {
            var self = this;
            if (self.dirty.isDirty()) {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                    self.currentItem().refresh();
                    self.dirty.reset();
                    self.currentCode(null);
                    $("#A_INP_002").focus();
                    $("#test input").val("");
                    self.hasCellphone(false);
                }).ifNo(function() {
                });
            } else {
                self.currentItem().refresh();
                self.dirty.reset();
                self.currentCode(null);
                $("#A_INP_002").focus();
                $("#test input").val("");
                self.hasCellphone(false);
            }
        }

        checkInput(): boolean {
            var self = this;
            if (!self.currentItem().INP_002_code()) {
                alert("コードが入力されていません。");
                $("#A_INP_002").focus();
                return false;
            } else if (!self.currentItem().INP_003_name()) {
                alert("名称が入力されていません。");
                $("#A_INP_003").focus();
                return false;
            }
            return true;
        }

        RegisterClassification() {
            var self = this;
            var dfd = $.Deferred<any>();
            if (self.checkInput()) {
                if (self.dataSource().length === 0) {
                    let classification = new viewmodel.model.ClassificationDto(self.currentItem().INP_002_code(), self.currentItem().INP_003_name(), self.currentItem().INP_004_notes());
                    service.addClassification(classification).done(function() {
                        self.getClassificationList_first();
                    }).fail(function(res) {
                        if (res.messageId == "ER05") {
                            alert("入力したコードは既に存在しています。\r\n コードを確認してください。 ");
                        }
                        dfd.reject(res);
                    })
                }
                for (let i = 0; i < self.dataSource().length; i++) {
                    if (self.currentItem().INP_002_code() == self.dataSource()[i].classificationCode && self.currentItem().INP_002_enable() == false) {
                        var classification_old = self.dataSource()[i];
                        var classification_update = new viewmodel.model.ClassificationDto(self.currentItem().INP_002_code(), self.currentItem().INP_003_name(), self.currentItem().INP_004_notes());
                        service.updateClassification(classification_update).done(function() {
                            self.itemdata_update(classification_update);
                            self.getClassificationList_afterUpdateClassification();
                        }).fail(function(res) {
                            if (res.messageId == "ER026") {
                                alert("更新対象のデータが存在しません。");
                            }
                            dfd.reject(res);
                        })
                        break;
                    } else if (self.currentItem().INP_002_code() != self.dataSource()[i].classificationCode
                        && i == self.dataSource().length - 1
                        && self.currentItem().INP_002_enable() == true) {
                        var classification_new = new viewmodel.model.ClassificationDto(self.currentItem().INP_002_code(), self.currentItem().INP_003_name(), self.currentItem().INP_004_notes());
                        service.addClassification(classification_new).done(function() {
                            self.itemdata_add(classification_new);
                            self.dirty.reset();
                            self.getClassificationList_afterAddClassification();
                        }).fail(function(res) {
                            if (res.messageId == "ER05") {
                                alert("入力したコードは既に存在しています。\r\n コードを確認してください。");
                            }
                            dfd.reject(res);
                        })
                        break;
                    } else if (self.currentItem().INP_002_code() == self.dataSource()[i].classificationCode && self.currentItem().INP_002_enable() == true) {
                        alert("入力したコードは既に存在しています。\r\n コードを確認してください。  ");
                        break;
                    }
                }
                self.dirty.reset();
                self.hasCellphone(true);
            }
        }

        DeleteClassification() {
            var self = this;
            var dfd = $.Deferred<any>();
            if (self.dataSource().length > 0) {
                var item = new model.RemoveClassificationCommand(self.currentItem().INP_002_code());
                self.index_of_itemDelete = self.dataSource().indexOf(self.findObj(self.currentItem().INP_002_code()));
                nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                    service.removeClassification(item).done(function(res) {
                        self.getClassificationList_aftefDelete();
                    }).fail(function(res) {
                        if (res.messageId == "ER06") {
                            alert("対象データがありません。");
                        }
                        dfd.reject(res);
                    })
                    dfd.resolve();
                    return dfd.promise();
                }).ifNo(function() { });
            } else { }
            self.dirty.reset();
        }

        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                if (classification_arr.length > 0) {
                    self.dataSource(classification_arr);
                    self.currentCode(self.dataSource()[0].classificationCode);
                } else if (classification_arr.length === 0) {
                    self.currentItem().INP_002_enable(true);
                    $("#A_INP_002").focus();
                }
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();
        }

        getClassificationList(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                self.dataSource(classification_arr);
                self.currentItem().INP_002_code(self.dataSource()[0].classificationCode);
                self.currentItem().INP_003_name(self.dataSource()[0].classificationName);
                self.currentItem().INP_004_notes(self.dataSource()[0].memo);
                if (self.dataSource().length > 0) {
                    self.currentCode(self.dataSource()[0].classificationCode);
                }
                self.notAlert(true);
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();

        }
        // get list Classification after insert
        getClassificationList_afterUpdateClassification(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                self.dataSource(classification_arr);

                if (self.dataSource().length > 1) {
                    self.currentCode(self.itemdata_update().classificationCode);
                }

                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
            })
            self.notAlert(true);
            dfd.resolve();
            return dfd.promise();

        }
        // get list Classification after insert
        getClassificationList_first(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                self.dataSource(classification_arr);
                self.currentCode(self.dataSource()[0].classificationCode);
                let i = self.dataSource().length;
                if (i > 0) {
                    self.currentItem().INP_002_enable(false);
                    self.currentItem().INP_002_code(self.dataSource()[0].classificationCode);
                    self.currentItem().INP_003_name(self.dataSource()[0].classificationName);
                    self.currentItem().INP_004_notes(self.dataSource()[0].memo);
                }
                self.notAlert(true);
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();

        }


        // get list Classification after insert
        getClassificationList_afterAddClassification(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                self.dataSource(classification_arr);

                self.currentCode(self.itemdata_add().classificationCode);
                dfd.resolve();
            }).fail(function(error) {
                alert(error.messageId);
            })
            return dfd.promise();

        }

        // get list Classification after remove
        getClassificationList_aftefDelete(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                self.dataSource(classification_arr);

                if (self.dataSource().length > 0) {
                    if (self.index_of_itemDelete === self.dataSource().length) {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete - 1].classificationCode)
                    } else {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete].classificationCode)
                    }

                } else {
                    self.initRegisterClassification();
                }
                self.notAlert(true);
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();

        }


    }

    /**
    *  model
    */
    export module model {

        export class InputField {
            INP_002_enable: KnockoutObservable<boolean>;
            INP_003_name: KnockoutObservable<string>;
            INP_004_notes: KnockoutObservable<string>;
            INP_002_code: KnockoutObservable<string>;
            constructor(classification: ClassificationDto, enable) {
                this.INP_002_code = ko.observable(classification.classificationCode);
                this.INP_003_name = ko.observable(classification.classificationName);
                this.INP_004_notes = ko.observable(classification.memo);
                this.INP_002_enable = ko.observable(enable);
            }

            refresh() {
                var self = this;
                self.INP_002_enable(true);
                self.INP_002_code("");
                self.INP_003_name("");
                self.INP_004_notes("");
            }
        }

        export class ClassificationDto {
            classificationCode: string;
            classificationName: string;
            memo: string;
            constructor(classificationCode?: string, classificationName?: string, memo?: string) {
                this.classificationCode = classificationCode;
                this.classificationName = classificationName;
                this.memo = memo;
            }
        }

        export class RemoveClassificationCommand {
            classificationCode: string;
            constructor(classificationCode: string) {
                this.classificationCode = classificationCode;
            }

        }
    }


}