module nts.uk.pr.view.qmm006.a.viewmodel {
    
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        
        listSourceBank: KnockoutObservableArray<any>;
        selectedSourceBankCode: KnockoutObservable<string>;
        selectedSourceBank: KnockoutObservable<SourceBank>;
        updateMode: KnockoutObservable<boolean> = ko.observable(false);
        accountTypes: KnockoutObservableArray<any> = ko.observableArray([
            { code: 0, name: getText("QMM006_46") },
            { code: 1, name: getText("QMM006_47") }
        ]);

        constructor() {
            let self = this;
            self.listSourceBank = ko.observableArray([]);
            self.selectedSourceBankCode = ko.observable("");
            self.selectedSourceBank = ko.observable(new SourceBank(null));
            self.selectedSourceBankCode.subscribe(val => {
                nts.uk.ui.errors.clearAll();
                if (_.isEmpty(val)) { //select parent node
                    self.setData(null);
                    self.updateMode(false);
                    $("#A3_2").focus();
                } else {
                    block.invisible();
                    service.getSourceBank(val).done(data => {
                        self.setData(data);
                        self.updateMode(true);
                        $("#A3_3").focus();
                    }).fail(error => {
                        alertError(error);
                    }).always(() => {
                        block.clear();
                    });
                }
            });
        }
        
        startPage(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            block.invisible();
            self.selectedSourceBankCode("");
            self.listSourceBank([]);
            service.getAllSourceBank().done((data: Array<any>) => {
                let listSB = _.map(data, sb => {
                    return {code: sb.code, name: sb.name, accountType: self.accountTypes()[sb.accountType].name, accountNumber: sb.accountNumber};
                });
                self.listSourceBank(listSB);
                if (self.listSourceBank().length > 0)
                    self.selectedSourceBankCode(self.listSourceBank()[0].code);
                else 
                    self.selectedSourceBankCode.valueHasMutated()
                dfd.resolve();
            }).fail(error => {
                alertError(error);
                dfd.reject();
            }).always(() => {
                block.clear();
            });
            return dfd.promise();
        }

        createNew() {
            let self = this;
            if (self.selectedSourceBankCode() == "")
                self.selectedSourceBankCode.valueHasMutated();
            else
                self.selectedSourceBankCode("");
        }

        register() {
            let self = this;
            $(".nts-input").trigger("validate");
            $("#switch-acc-type").trigger("validate");
            if (!nts.uk.ui.errors.hasError()) {
                block.invisible();
                let command = ko.toJS(self.selectedSourceBank());
                ko.utils.extend(command, {
                    updateMode: self.updateMode()
                });
                service.register(command).done(() => {
                    self.startPage().done(() => {
                        info({ messageId: "Msg_15" }).then(() => {
                            if (self.selectedSourceBankCode() == command.code)
                                self.selectedSourceBankCode.valueHasMutated();
                            else
                                self.selectedSourceBankCode(command.code);
                        });
                    });
                }).fail(error => {
                    alertError(error);
                }).always(() => {
                    block.clear();
                });
            }
        }

        remove() {
            let self = this;
            block.invisible();
            service.checkBeforeDelete(self.selectedSourceBankCode()).done(() => {
                confirm({ messageId: "Msg_18" }).ifYes(() => {
                    let deletedIndex = _.findIndex(self.listSourceBank(), r => {return r.code == self.selectedSourceBankCode()});
                    let nextSelectCode = "";
                    if (self.listSourceBank().length > 1) {
                        if (deletedIndex == self.listSourceBank().length - 1) {
                            nextSelectCode = self.listSourceBank()[deletedIndex - 1].code;
                        } else {
                            nextSelectCode = self.listSourceBank()[deletedIndex + 1].code;
                        }
                    }
                    service.remove(self.selectedSourceBankCode()).done(() => {
                        self.startPage().done(() => {
                            info({ messageId: "Msg_16" }).then(() => {
                                if (self.selectedSourceBankCode() == nextSelectCode)
                                    self.selectedSourceBankCode.valueHasMutated();
                                else
                                    self.selectedSourceBankCode(nextSelectCode);
                            });
                        });
                    }).fail(error => {
                        alertError(error);
                    }).always(() => {
                        block.clear();
                    });
                }).ifNo(() => {
                });
            }).fail(error => {
                alertError(error);
            }).always(() => {
                block.clear();
            });
        }
        
        openDialogQmm006c() {
            modal("/view/qmm/006/c/index.xhtml");
        }
        
        setData(data: any) {
            let self = this;
            self.selectedSourceBank().code(data == null ? null : data.code);
            self.selectedSourceBank().name(data == null ? null : data.name);
            self.selectedSourceBank().branchId(data == null ? null : data.branchId);
            self.selectedSourceBank().bankCode(data == null ? null : data.bankCode);
            self.selectedSourceBank().bankName(data == null ? null : data.bankName);
            self.selectedSourceBank().branchCode(data == null ? null : data.branchCode);
            self.selectedSourceBank().branchName(data == null ? null : data.branchName);
            self.selectedSourceBank().accountType(data == null ? 0 : data.accountType);
            self.selectedSourceBank().accountNumber(data == null ? null : data.accountNumber);
            self.selectedSourceBank().transferRequesterName(data == null ? null : data.transferRequesterName);
            self.selectedSourceBank().memo(data == null ? null : data.memo);
            self.selectedSourceBank().entrustorInforCode1(data != null ? data.entrustorInforCode1 : null);
            self.selectedSourceBank().entrustorInforUse1(data != null ? data.entrustorInforUse1 : null);
            self.selectedSourceBank().entrustorInforCode2(data != null ? data.entrustorInforCode2 : null);
            self.selectedSourceBank().entrustorInforUse2(data != null ? data.entrustorInforUse2 : null);
            self.selectedSourceBank().entrustorInforCode3(data != null ? data.entrustorInforCode3 : null);
            self.selectedSourceBank().entrustorInforUse3(data != null ? data.entrustorInforUse3 : null);
            self.selectedSourceBank().entrustorInforCode4(data != null ? data.entrustorInforCode4 : null);
            self.selectedSourceBank().entrustorInforUse4(data != null ? data.entrustorInforUse4 : null);
            self.selectedSourceBank().entrustorInforCode5(data != null ? data.entrustorInforCode5 : null);
            self.selectedSourceBank().entrustorInforUse5(data != null ? data.entrustorInforUse5 : null);
        }

    }

    class SourceBank {
        code: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        branchId: KnockoutObservable<string>;
        branchCode: KnockoutObservable<string>;
        branchName: KnockoutObservable<string>;
        bankCode: KnockoutObservable<string>;
        bankName: KnockoutObservable<string>;
        accountType: KnockoutObservable<number>;
        accountNumber: KnockoutObservable<string>;
        transferRequesterName: KnockoutObservable<string>;
        memo: KnockoutObservable<string>;
        entrustorInforCode1: KnockoutObservable<string> = ko.observable(null);
        entrustorInforUse1: KnockoutObservable<string> = ko.observable(null);
        entrustorInforCode2: KnockoutObservable<string> = ko.observable(null);
        entrustorInforUse2: KnockoutObservable<string> = ko.observable(null);
        entrustorInforCode3: KnockoutObservable<string> = ko.observable(null);
        entrustorInforUse3: KnockoutObservable<string> = ko.observable(null);
        entrustorInforCode4: KnockoutObservable<string> = ko.observable(null);
        entrustorInforUse4: KnockoutObservable<string> = ko.observable(null);
        entrustorInforCode5: KnockoutObservable<string> = ko.observable(null);
        entrustorInforUse5: KnockoutObservable<string> = ko.observable(null);
        
        constructor(data: {
                code: string, 
                name: string, 
                branchId: string,
                branchCode: string,
                branchName: string,
                bankCode: string,
                bankName: string, 
                accountType: number, 
                accountNumber: string, 
                transferRequesterName: string, 
                memo: string, 
                entrustorInforCode1: string,
                entrustorInforUse1: string,
                entrustorInforCode2: string,
                entrustorInforUse2: string,
                entrustorInforCode3: string,
                entrustorInforUse3: string,
                entrustorInforCode4: string,
                entrustorInforUse4: string,
                entrustorInforCode5: string,
                entrustorInforUse5: string
            }) {
            this.code = ko.observable(data == null ? null : data.code);
            this.name = ko.observable(data == null ? null : data.name);
            this.branchId = ko.observable(data == null ? null : data.branchId);
            this.bankCode = ko.observable(data == null ? null : data.bankCode);
            this.bankName = ko.observable(data == null ? null : data.bankName);
            this.branchCode = ko.observable(data == null ? null : data.branchCode);
            this.branchName = ko.observable(data == null ? null : data.branchName);
            this.accountType = ko.observable(data == null ? null : data.accountType);
            this.accountNumber = ko.observable(data == null ? null : data.accountNumber);
            this.transferRequesterName = ko.observable(data == null ? null : data.transferRequesterName);
            this.memo = ko.observable(data == null ? null : data.memo);
            this.entrustorInforCode1(data != null ? data.entrustorInforCode1 : null);
            this.entrustorInforUse1(data != null ? data.entrustorInforUse1 : null);
            this.entrustorInforCode2(data != null ? data.entrustorInforCode2 : null);
            this.entrustorInforUse2(data != null ? data.entrustorInforUse2 : null);
            this.entrustorInforCode3(data != null ? data.entrustorInforCode3 : null);
            this.entrustorInforUse3(data != null ? data.entrustorInforUse3 : null);
            this.entrustorInforCode4(data != null ? data.entrustorInforCode4 : null);
            this.entrustorInforUse4(data != null ? data.entrustorInforUse4 : null);
            this.entrustorInforCode5(data != null ? data.entrustorInforCode5 : null);
            this.entrustorInforUse5(data != null ? data.entrustorInforUse5 : null);
        }
        
        openDialogQmm006b() {
            let self = this;
            setShared("QMM006BParam", self.branchId());
            modal("/view/qmm/006/b/index.xhtml").onClosed(() => {
                let selected = getShared("QMM006BResult");
                if (selected) {
                    self.branchId(selected.branchId);
                    self.bankCode(selected.bankCode);
                    self.bankName(selected.bankName);
                    self.branchCode(selected.branchCode);
                    self.branchName(selected.branchName);
                }
            });
        }
        
    }
    
}

