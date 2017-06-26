module qmm023.a.viewmodel {
    export class ScreenModel {

        items: KnockoutObservableArray<TaxModel>;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;
        currentTax: KnockoutObservable<TaxModel>;
        isUpdate: KnockoutObservable<boolean>;
        allowEditCode: KnockoutObservable<boolean>;
        isEnableDeleteBtn: KnockoutObservable<boolean>;
        currentTaxDirty: nts.uk.ui.DirtyChecker;
        flatDirty: boolean;
        constructor() {
            let self = this;
            self._init();

            self.currentCode.subscribe(function(codeChanged) {
                if (nts.uk.text.isNullOrEmpty(codeChanged)) {
                    if (self.isUpdate() === true) {
                        self.refreshLayout();
                    }
                    return;
                }
                let oldCode = ko.mapping.toJS(self.currentTax().code);
                if (ko.mapping.toJS(codeChanged) === oldCode && self.flatDirty === false) {
                    return;
                }

                if (self.flatDirty == true) {
                    self.clearError();
                    self.currentTax(ko.mapping.fromJS(self.getTax(codeChanged)));
                    self.allowEditCode(false);
                    self.isUpdate(true);
                    self.isEnableDeleteBtn(true);
                    self.currentTaxDirty.reset();
                    self.flatDirty = false;
                    return;
                }
                self.alertCheckDirty(oldCode, codeChanged);
            });


        }

        _init(): void {
            let self = this;
            self.items = ko.observableArray([]);
            this.columns = ko.observableArray([
                { headerText: 'コード', prop: 'code', width: 75 },
                { headerText: '名称', prop: 'name', width: 175 },
                { headerText: '限度額', prop: 'taxLimit', width: 115 }
            ]);
            self.currentTax = ko.observable(new TaxModel('', '', 0));
            self.currentCode = ko.observable(null);
            self.isUpdate = ko.observable(true);
            self.allowEditCode = ko.observable(false);
            self.isEnableDeleteBtn = ko.observable(true);
            self.flatDirty = true;
            self.currentTaxDirty = new nts.uk.ui.DirtyChecker(self.currentTax)
            if (self.items.length <= 0) {
                self.allowEditCode = ko.observable(true);
                self.isUpdate = ko.observable(false);
                self.isEnableDeleteBtn = ko.observable(false);
            }

        }

        getTax(codeNew: string): TaxModel {
            let self = this;
            let tax: TaxModel = _.find(self.items(), function(item) {
                return item.code === codeNew;
            });
            return tax;
        }

        isError(): boolean {
            let self = this;
            $('#INP_002').ntsEditor("validate");
            $('#INP_003').ntsEditor("validate");
            if ($('.nts-editor').ntsError("hasError")) {
                return true;
            }
            return false;
        }
        clearError(): void {
            if ($('.nts-editor').ntsError("hasError")) {
                $('.save-error').ntsError('clear');
            }
        }

        alertCheckDirty(oldCode: string, codeChanged: string) {
            let self = this;
            if (!self.currentTaxDirty.isDirty()) {
                self.clearError();
                self.currentTax(ko.mapping.fromJS(self.getTax(codeChanged)));
                self.allowEditCode(false);
                self.isUpdate(true);
                self.isEnableDeleteBtn(true);
                self.currentTaxDirty.reset();
                self.flatDirty = false;
            } else {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。?").ifYes(function() {
                    self.clearError();
                    self.currentTax(ko.mapping.fromJS(self.getTax(codeChanged)));
                    self.allowEditCode(false);
                    self.isUpdate(true);
                    self.isEnableDeleteBtn(true);
                    self.currentTaxDirty.reset();
                }).ifCancel(function() {
                    self.currentCode(oldCode);
                })
            }
        }

        refreshLayout(): void {
            let self = this;
            self.allowEditCode(true);
            self.clearError();
            self.currentTax(ko.mapping.fromJS(new TaxModel('', '', 0)));
            self.currentCode(null);
            self.isUpdate(false);
            self.isEnableDeleteBtn(false);
            self.currentTaxDirty.reset();
        }

        createButtonClick(): void {
            let self = this;
            if (self.currentTaxDirty.isDirty()) {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。?").ifYes(function() {
                    self.refreshLayout();
                }).ifCancel(function() {
                    return;
                })
            } else {
                self.refreshLayout();
            }
        }

        insertUpdateData(): void {
            let self = this;
            if (self.isError()) {
                return;
            }
            let newCode = ko.mapping.toJS(self.currentTax().code);
            let newName = ko.mapping.toJS(self.currentTax().name);
            let newTaxLimit = ko.mapping.toJS(self.currentTax().taxLimit);
            let insertUpdateModel = new InsertUpdateModel(nts.uk.text.padLeft(newCode, '0', 2), newName, newTaxLimit);
            service.insertUpdateData(self.isUpdate(), insertUpdateModel).done(function() {
                self.reload(false, nts.uk.text.padLeft(newCode, '0', 2));
                self.flatDirty = true;
                self.currentTaxDirty.reset();
                if (self.isUpdate() === false) {
                    self.isUpdate(true);
                    self.allowEditCode(false);
                    return;
                }
            }).fail(function(error) {
                if (error.message === '3') {
                    let _message = "入力した{0}は既に存在しています。\r\n {1}を確認してください。";
                    nts.uk.ui.dialog.alert(nts.uk.text.format(_message, 'コード', 'コード'));
                } else if (error.message === '4') {
                    nts.uk.ui.dialog.alert("対象データがありません。").then(function() {
                        self.reload(true);
                    })
                }
            });

        }

        deleteData(): void {
            let self = this;
            let deleteCode = ko.mapping.toJS(self.currentTax().code);
            service.deleteData(new DeleteModel(deleteCode)).done(function() {
                let indexItemDelete = _.findIndex(self.items(), function(item) { return item.code == deleteCode; });
                $.when(self.reload(false)).done(function() {
                    self.flatDirty = true;
                    if (self.items().length === 0) {
                        self.refreshLayout();
                        return;
                    }
                    if (self.items().length == indexItemDelete) {
                        self.currentCode(self.items()[indexItemDelete - 1].code);
                        return;
                    }

                    if (self.items().length < indexItemDelete) {
                        self.currentCode(self.items()[0].code);
                        return;
                    }

                    if (self.items().length > indexItemDelete) {
                        self.currentCode(self.items()[indexItemDelete].code);
                        return;
                    }
                });

            }).fail(function(error) {
                if (error.message === '1') {
                    nts.uk.ui.dialog.alert("対象データがありません。").then(function() {
                        self.reload(true);
                    })
                }
            });
        }

        alertDelete() {
            let self = this;
            nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                self.deleteData();
            })

        }

        // startpage
        startPage(): any {
            let self = this;
            return self.reload(true);
        }

        reload(isReload: boolean, reloadCode?: string) {
            let self = this;
            let dfd = $.Deferred();
            service.getCommutelimitsByCompanyCode().done(function(data) {
                self.items([]);
                _.forEach(data, function(item) {
                    self.items.push(new TaxModel(item.commuNoTaxLimitCode, item.commuNoTaxLimitName, item.commuNoTaxLimitValue));
                });
                self.flatDirty = true;
                if (self.items().length <= 0) {
                    self.refreshLayout();
                    dfd.resolve();
                    return;
                }
                if (isReload) {
                    self.currentCode(self.items()[0].code)
                } else if (!nts.uk.text.isNullOrEmpty(reloadCode)) {
                    self.currentCode(reloadCode)
                }
                dfd.resolve(data);
            })
            return dfd.promise();
        }
    }

    class TaxModel {
        code: string;
        name: string;
        taxLimit: number;
        constructor(code: string, name: string, taxLimit: number) {
            this.code = code;
            this.name = name;
            this.taxLimit = taxLimit;
        }
    }

    export class InsertUpdateModel {
        commuNoTaxLimitCode: string;
        commuNoTaxLimitName: string;
        commuNoTaxLimitValue: number;
        constructor(commuNoTaxLimitCode: string, commuNoTaxLimitName: string,
            commuNoTaxLimitValue: number) {
            this.commuNoTaxLimitCode = commuNoTaxLimitCode;
            this.commuNoTaxLimitName = commuNoTaxLimitName;
            this.commuNoTaxLimitValue = commuNoTaxLimitValue;
        }
    }

    export class DeleteModel {
        commuNoTaxLimitCode: string;
        constructor(commuNoTaxLimitCode: string) {
            this.commuNoTaxLimitCode = commuNoTaxLimitCode;
        }
    }
}