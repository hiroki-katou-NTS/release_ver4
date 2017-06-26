module nts.uk.pr.view.qmm010.a {

    import option = nts.uk.ui.option;
    import LaborInsuranceOfficeDto = service.model.LaborInsuranceOfficeDto;
    import LaborInsuranceOfficeFindOutDto = service.model.LaborInsuranceOfficeFindOutDto;
    import TypeActionLaborInsuranceOffice = service.model.TypeActionLaborInsuranceOffice;
    import LaborInsuranceOfficeDeleteDto = service.model.LaborInsuranceOfficeDeleteDto;
    import SocialInsuranceOfficeImportDto = nts.uk.pr.view.qmm010.b.service.model.SocialInsuranceOfficeImportDto;
    import PostCode = nts.uk.pr.view.base.postcode.service.model.PostCode;

    export module viewmodel {

        export class ScreenModel {

            //ojbect value binding
            laborInsuranceOfficeModel: KnockoutObservable<LaborInsuranceOfficeModel>;
            lstlaborInsuranceOfficeModel: KnockoutObservableArray<LaborInsuranceOfficeFindOutDto>
            columnsLstlaborInsuranceOffice: KnockoutObservableArray<any>;
            selectCodeLstlaborInsuranceOffice: KnockoutObservable<string>;
            beginSelectlaborInsuranceOffice: KnockoutObservable<string>;
            enableButton: KnockoutObservable<boolean>;
            isEmpty: KnockoutObservable<boolean>;
            isEnableDelete: KnockoutObservable<boolean>;
            isShowDirty: KnockoutObservable<boolean>;
            //update add LaborInsuranceOffice
            typeAction: KnockoutObservable<number>;
            messageList: KnockoutObservableArray<any>;
            dirty: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                self.columnsLstlaborInsuranceOffice = ko.observableArray([
                    { headerText: 'コード', prop: 'code', width: 120 },
                    { headerText: '名称', prop: 'name', width: 120 }
                ]);
                self.enableButton = ko.observable(true);
                self.typeAction = ko.observable(TypeActionLaborInsuranceOffice.update);
                self.isEmpty = ko.observable(true);
                self.laborInsuranceOfficeModel = ko.observable(new LaborInsuranceOfficeModel());
                self.selectCodeLstlaborInsuranceOffice = ko.observable('');
                self.beginSelectlaborInsuranceOffice = ko.observable('');
                self.isEnableDelete = ko.observable(true);
                self.messageList = ko.observableArray([
                    { messageId: "ER001", message: "＊が入力されていません。" },
                    { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                    { messageId: "AL001", message: "変更された内容が登録されていません。\r\n よろしいですか。" },
                    { messageId: "AL002", message: "データを削除します。\r\n よろしいですか？。" },
                    { messageId: "ER010", message: "対象データがありません。" }
                ]);
                self.dirty = new nts.uk.ui.DirtyChecker(self.laborInsuranceOfficeModel);
                self.isShowDirty = ko.observable(true);
            }

            //function reset value view model
            private resetValueLaborInsurance() {
                var self = this;
                if (self.dirty.isDirty() && self.isShowDirty()) {
                    nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                        self.isShowDirty(false);
                        self.onResetValueLaborInsurance();
                    }).ifNo(function() {
                        //No action
                    });
                    return;
                }
                self.onResetValueLaborInsurance();

            }

            private onResetValueLaborInsurance() {
                var self = this;
                self.laborInsuranceOfficeModel().resetAllValue();
                //set type action (ismode) add
                self.typeAction(TypeActionLaborInsuranceOffice.add);
                //reset value model
                self.selectCodeLstlaborInsuranceOffice('');
                self.beginSelectlaborInsuranceOffice('');
                if (!self.isEmpty()) self.clearErrorSave();
                self.dirty.reset();
                self.isEnableDelete(false);
                self.isShowDirty(true);
            }

            //function clear message error
            private clearErrorSave() {
                $('.save-error').ntsError('clear');
            }

            //function read all SocialTnsuranceOffice
            private readFromSocialTnsuranceOffice() {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                        self.onReadFromSocialTnsuranceOffice();
                    }).ifNo(function() {
                        //No action
                    });
                    return;
                }
                self.onReadFromSocialTnsuranceOffice();

            }

            private onReadFromSocialTnsuranceOffice() {
                var self = this;

                self.enableButton(false);
                //call service find all SocialTnsuranceOffice
                service.findAllSocialInsuranceOffice().done(data => {
                    if (data != null && data.length > 0) {
                        //set data fw /b
                        nts.uk.ui.windows.setShared("dataInsuranceOffice", data);
                        //open dialog /b/index.xhtml
                        nts.uk.ui.windows.sub.modal("/view/qmm/010/b/index.xhtml", {
                            height: 700, width: 450,
                            title: "社会保険事業所から読み込み",
                            dialogClass: 'no-close'
                        }).onClosed(() => {
                            self.enableButton(true);
                            var importData: SocialInsuranceOfficeImportDto;
                            importData = nts.uk.ui.windows.getShared('importData');
                            if (importData) {
                                self.laborInsuranceOfficeModel().setDataImport(importData);
                            }
                        });
                    } else {
                        //show message
                        alert("ER010");
                        self.enableButton(true);
                    }
                });
            }

            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                self.findAllInsuranceOffice().done(data => {
                    dfd.resolve(data);
                });
                return dfd.promise();
            }

            //Connection service find All InsuranceOffice
            private findAllInsuranceOffice(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                service.findAllLaborInsuranceOffice().done(data => {
                    if (data != null && data.length > 0) {
                        //data not null length > 0
                        //reset List Labor Insurance Office
                        self.lstlaborInsuranceOfficeModel = ko.observableArray<LaborInsuranceOfficeFindOutDto>(data);
                        self.selectCodeLstlaborInsuranceOffice(data[0].code);
                        self.selectCodeLstlaborInsuranceOffice.subscribe(function(code: string) {
                            self.showchangeLaborInsuranceOffice(code);
                        });
                        self.isEmpty(false);
                        self.detailLaborInsuranceOffice(data[0].code).done(function() {
                            self.isEnableDelete(true);
                            dfd.resolve(self);
                        });
                    } else {
                        //new reset data value
                        self.newmodelEmptyData();
                        dfd.resolve(self);
                    }
                });
                return dfd.promise();
            }

            //Function show message error message
            private showMessageSave(messageId: string) {
                var self = this;
                if (self.messageList()[0].messageId === messageId) {
                    //001
                    var message = self.messageList()[0].message;

                    if (!self.laborInsuranceOfficeModel().code()) {
                        $('#inp_code').ntsError('set', message);
                    }

                    if (!self.laborInsuranceOfficeModel().name()) {
                        $('#inp_name').ntsError('set', message);
                    }

                    if (!self.laborInsuranceOfficeModel().picPosition()) {
                        $('#inp_picPosition').ntsError('set', message);
                    }
                }

                if (self.messageList()[1].messageId === messageId) {
                    var message = self.messageList()[1].message;
                    $('#inp_code').ntsError('set', message);
                }
            }

            private validateData(): boolean {
                $("#inp_code").ntsEditor("validate");
                $("#inp_name").ntsEditor("validate");
                $("#inp_shortName").ntsEditor("validate");
                $("#inp_picName").ntsEditor("validate");
                $("#inp_picPosition").ntsEditor("validate");
                $("#inp_postalCode").ntsEditor("validate");
                $("#inp_address1st").ntsEditor("validate");
                $("#inp_address2nd").ntsEditor("validate");
                $("#inp_kanaAddress2nd").ntsEditor("validate");
                $("#inp_phoneNumber").ntsEditor("validate");
                $("#inp_citySign").ntsEditor("validate");
                $("#inp_officeMark").ntsEditor("validate");
                $("#inp_officeNoA").ntsEditor("validate");
                $("#inp_officeNoB").ntsEditor("validate");
                $("#inp_officeNoC").ntsEditor("validate");
                if ($('.nts-editor').ntsError("hasError")) {
                    return true;
                }
                return false;
            }
            //Function action button save on click
            private saveLaborInsuranceOffice() {
                var self = this;

                self.clearErrorSave();
                if (self.validateData()) {
                    return;
                }

                //get ismode
                if (self.typeAction() == TypeActionLaborInsuranceOffice.add) {
                    //is mode is add
                    //call service add labor insurance office
                    service.addLaborInsuranceOffice(self.collectData()).done(function() {
                        //reload labor insurance office
                        self.reloadDataByAction();
                        //clear error
                        self.clearErrorSave();
                    }).fail(function(res) {
                        //show error message error
                        self.showMessageSave(res.messageId);
                    })
                } else {
                    //is mode is update
                    //call service update labor insurance office
                    service.updateLaborInsuranceOffice(self.collectData()).done(function() {
                        self.reloadDataByAction();
                    });
                }
            }

            //Function show view by change selection
            private showchangeLaborInsuranceOffice(code: string) {
                var self = this;
                if (code && code != '') {
                    // type action add (new mode)
                    if (self.typeAction() == TypeActionLaborInsuranceOffice.add) {
                        if (self.dirty.isDirty() && self.isShowDirty()) {
                            nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                                self.isShowDirty(false);
                                self.typeAction(TypeActionLaborInsuranceOffice.update);
                                self.detailLaborInsuranceOffice(code);
                            }).ifNo(function() {
                                self.isShowDirty(false);
                                self.selectCodeLstlaborInsuranceOffice(self.beginSelectlaborInsuranceOffice());
                                self.isShowDirty(true);
                            });
                        } else {
                            self.typeAction(TypeActionLaborInsuranceOffice.update);
                            self.detailLaborInsuranceOffice(code);
                        }
                    } else {
                        // type action update (update mode)
                        if (self.dirty.isDirty() && self.isShowDirty()) {
                            if (code !== self.selectCodeLstlaborInsuranceOffice()) {
                                nts.uk.ui.dialog.confirm(self.messageList()[2].message).ifYes(function() {
                                    self.isShowDirty(false);
                                    self.typeAction(TypeActionLaborInsuranceOffice.update);
                                    self.detailLaborInsuranceOffice(code);
                                }).ifNo(function() {
                                    self.selectCodeLstlaborInsuranceOffice(self.beginSelectlaborInsuranceOffice());
                                });
                            }
                        } else {
                            self.typeAction(TypeActionLaborInsuranceOffice.update);
                            self.detailLaborInsuranceOffice(code);
                        }
                    }
                }
            }

            //Function detail
            private detailLaborInsuranceOffice(code: string): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                if (code && code != '') {
                    var self = this;
                    //call service find labor insurance office
                    service.findLaborInsuranceOffice(code).done(data => {
                        if (self.isEmpty()) {
                            self.selectCodeLstlaborInsuranceOffice.subscribe(function(code: string) {
                                self.showchangeLaborInsuranceOffice(code);
                            });
                            self.isEmpty(false);
                        }
                        //set data labor insurance office
                        self.laborInsuranceOfficeModel().updateData(data);
                        self.laborInsuranceOfficeModel().setEnable(false);
                        self.isEnableDelete(true);
                        self.clearErrorSave();
                        self.beginSelectlaborInsuranceOffice(code);
                        self.dirty.reset();
                        self.isShowDirty(true);
                    });
                }
                dfd.resolve();
                return dfd.promise();
            }

            //reload action
            private reloadDataByAction() {
                var self = this;
                //call service findAll
                service.findAllLaborInsuranceOffice().done(data => {
                    //reset list data
                    if (self.lstlaborInsuranceOfficeModel == null || self.lstlaborInsuranceOfficeModel == undefined) {
                        self.lstlaborInsuranceOfficeModel = ko.observableArray<LaborInsuranceOfficeFindOutDto>(data);
                    } else {
                        self.lstlaborInsuranceOfficeModel(data);
                    }
                    //set data view
                    var code: string = self.selectCodeLstlaborInsuranceOffice();
                    if (self.typeAction() == TypeActionLaborInsuranceOffice.add) {
                        if (data != null && data.length > 0) {
                            self.isEmpty(false);
                            self.typeAction(TypeActionLaborInsuranceOffice.update);
                            self.selectCodeLstlaborInsuranceOffice(self.laborInsuranceOfficeModel().code());
                            self.detailLaborInsuranceOffice(self.laborInsuranceOfficeModel().code());
                        } else {
                            self.newmodelEmptyData();
                        }
                    } else {
                        self.detailLaborInsuranceOffice(code);
                    }
                });
            }

            //reload data by delete Labor
            private reloadByDelete(code: string) {
                var self = this;
                self.lstlaborInsuranceOfficeModel
                var datapre: LaborInsuranceOfficeFindOutDto[];
                datapre = self.lstlaborInsuranceOfficeModel();
                var findcode: number = self.findCodeByDelete(code, datapre);

                if (findcode == -1) {
                    return;
                }

                if (findcode == 0 && datapre.length == 1) {
                    self.newmodelEmptyData();
                    return;
                }
                var codenew: string = '';
                //exist under find code
                if (findcode + 1 < datapre.length) {
                    codenew = datapre[findcode + 1].code;
                    //remove
                }
                else if (findcode - 1 >= 0) {
                    //exist begin find code
                    codenew = datapre[findcode - 1].code;
                }
                // find .... 
                datapre.splice(findcode, 1);
                self.lstlaborInsuranceOfficeModel(datapre);
                self.selectCodeLstlaborInsuranceOffice(codenew);
            }

            private findCodeByDelete(code: string, datapre: LaborInsuranceOfficeFindOutDto[]): number {
                var find: number = -1;
                for (var i: number = 0; i < datapre.length; i++) {
                    if (datapre[i].code === code) {
                        find = i;
                        break;
                    }
                }
                return find;
            }

            //Function empty data respone
            private newmodelEmptyData() {
                var self = this;
                //reset list data
                if (self.lstlaborInsuranceOfficeModel == null || self.lstlaborInsuranceOfficeModel == undefined) {
                    self.lstlaborInsuranceOfficeModel = ko.observableArray<LaborInsuranceOfficeFindOutDto>([]);
                } else {
                    self.lstlaborInsuranceOfficeModel([]);
                }
                //reset value
                self.resetValueLaborInsurance();
                self.selectCodeLstlaborInsuranceOffice('');
                self.isEmpty(true);
            }

            private deleteLaborInsuranceOffice() {
                var self = this;
                var laborInsuranceOfficeDeleteDto: LaborInsuranceOfficeDeleteDto = new LaborInsuranceOfficeDeleteDto();
                laborInsuranceOfficeDeleteDto.code = self.laborInsuranceOfficeModel().code();
                laborInsuranceOfficeDeleteDto.version = 11;
                if (self.selectCodeLstlaborInsuranceOffice != null && self.selectCodeLstlaborInsuranceOffice() != '') {
                    nts.uk.ui.dialog.confirm(self.messageList()[3].message).ifYes(function() {
                        service.deleteLaborInsuranceOffice(laborInsuranceOfficeDeleteDto).done(function() {
                            self.reloadByDelete(self.selectCodeLstlaborInsuranceOffice());
                        }).fail(function(error) {
                            if (error.messageId == 'ER010') {
                                $('#btn_delete').ntsError('set', self.messageList()[4].message);
                            }
                        });
                    }).ifNo(function() {
                        self.reloadDataByAction();
                    })
                }
            }

            //Convert Model => DTO
            public collectData(): LaborInsuranceOfficeDto {
                var self = this;
                var laborInsuranceOffice: LaborInsuranceOfficeDto;
                laborInsuranceOffice = new LaborInsuranceOfficeDto();
                laborInsuranceOffice.code = self.laborInsuranceOfficeModel().code();
                laborInsuranceOffice.name = self.laborInsuranceOfficeModel().name();
                laborInsuranceOffice.shortName = self.laborInsuranceOfficeModel().shortName();
                laborInsuranceOffice.picName = self.laborInsuranceOfficeModel().picName();
                laborInsuranceOffice.picPosition = self.laborInsuranceOfficeModel().picPosition();
                laborInsuranceOffice.potalCode = self.laborInsuranceOfficeModel().postalCode();
                laborInsuranceOffice.address1st = self.laborInsuranceOfficeModel().address1st();
                laborInsuranceOffice.address2nd = self.laborInsuranceOfficeModel().address2nd();
                laborInsuranceOffice.kanaAddress1st = self.laborInsuranceOfficeModel().kanaAddress1st();
                laborInsuranceOffice.kanaAddress2nd = self.laborInsuranceOfficeModel().kanaAddress2nd();
                laborInsuranceOffice.phoneNumber = self.laborInsuranceOfficeModel().phoneNumber();
                laborInsuranceOffice.citySign = self.laborInsuranceOfficeModel().citySign();
                laborInsuranceOffice.officeMark = self.laborInsuranceOfficeModel().officeMark();
                laborInsuranceOffice.officeNoA = self.laborInsuranceOfficeModel().officeNoA();
                laborInsuranceOffice.officeNoB = self.laborInsuranceOfficeModel().officeNoB();
                laborInsuranceOffice.officeNoC = self.laborInsuranceOfficeModel().officeNoC();
                laborInsuranceOffice.memo = self.laborInsuranceOfficeModel().multilineeditor().memo();
                return laborInsuranceOffice;
            }
        }

        export class LaborInsuranceOfficeModel {

            code: KnockoutObservable<string>;
            name: KnockoutObservable<string>;
            shortName: KnockoutObservable<string>;
            picName: KnockoutObservable<string>;
            picPosition: KnockoutObservable<string>;
            postalCode: KnockoutObservable<string>;
            address1st: KnockoutObservable<string>;
            kanaAddress1st: KnockoutObservable<string>;
            address2nd: KnockoutObservable<string>;
            kanaAddress2nd: KnockoutObservable<string>;
            phoneNumber: KnockoutObservable<string>;
            citySign: KnockoutObservable<string>;
            officeMark: KnockoutObservable<string>;
            officeNoA: KnockoutObservable<string>;
            officeNoB: KnockoutObservable<string>;
            officeNoC: KnockoutObservable<string>;
            textEditorOption: KnockoutObservable<any>;
            multilineeditor: KnockoutObservable<any>;
            isEnable: KnockoutObservable<boolean>;

            constructor() {
                this.code = ko.observable('');
                this.name = ko.observable('');
                this.shortName = ko.observable('');
                this.picName = ko.observable('');
                this.picPosition = ko.observable('');
                this.postalCode = ko.observable('');
                this.address1st = ko.observable('');
                this.kanaAddress1st = ko.observable('');
                this.address2nd = ko.observable('');
                this.kanaAddress2nd = ko.observable('');
                this.phoneNumber = ko.observable('');
                this.citySign = ko.observable('');
                this.officeMark = ko.observable('');
                this.officeNoA = ko.observable('');
                this.officeNoB = ko.observable('');
                this.officeNoC = ko.observable('');
                this.textEditorOption = ko.mapping.fromJS(new option.TextEditorOption());
                this.multilineeditor = ko.observable({
                    memo: ko.observable(''),
                    readonly: false,
                    constraint: 'ResidenceCode',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                        resizeable: true,
                        placeholder: "",
                        width: "",
                        textalign: "left"
                    })),
                });
                this.isEnable = ko.observable(true);
            }

            //Reset value in view Model
            resetAllValue() {
                this.code('');
                this.name('');
                this.shortName('');
                this.picName('');
                this.picPosition('');
                this.postalCode('');
                this.address1st('');
                this.kanaAddress1st('');
                this.address2nd('');
                this.kanaAddress2nd('');
                this.phoneNumber('');
                this.citySign('');
                this.officeMark('');
                this.officeNoA('');
                this.officeNoB('');
                this.officeNoC('');
                this.textEditorOption = ko.mapping.fromJS(new option.TextEditorOption());
                this.multilineeditor({
                    memo: ko.observable(''),
                    readonly: false,
                    constraint: 'ResidenceCode',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                        resizeable: true,
                        placeholder: "",
                        width: "",
                        textalign: "left"
                    })),
                });
                this.isEnable(true);
            }
            
            setEnable(isEnable: boolean ){
                this.isEnable(isEnable);   
            }

            updateData(officeInfo: LaborInsuranceOfficeDto) {
                if (officeInfo != null) {
                    this.code(officeInfo.code);
                    this.name(officeInfo.name);
                    this.shortName(officeInfo.shortName);
                    this.picName(officeInfo.picName);
                    this.picPosition(officeInfo.picPosition);
                    this.postalCode(officeInfo.potalCode);
                    this.address1st(officeInfo.address1st);
                    this.kanaAddress1st(officeInfo.kanaAddress1st);
                    this.address2nd(officeInfo.address2nd);
                    this.kanaAddress2nd(officeInfo.kanaAddress2nd);
                    this.phoneNumber(officeInfo.phoneNumber);
                    this.citySign(officeInfo.citySign);
                    this.officeMark(officeInfo.officeMark);
                    this.officeNoA(officeInfo.officeNoA);
                    this.officeNoB(officeInfo.officeNoB);
                    this.officeNoC(officeInfo.officeNoC);
                    this.textEditorOption = ko.mapping.fromJS(new option.TextEditorOption());
                    this.multilineeditor({
                        memo: ko.observable(officeInfo.memo),
                        readonly: false,
                        constraint: 'ResidenceCode',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                            resizeable: true,
                            placeholder: "",
                            width: "",
                            textalign: "left"
                        })),
                    });
                }
            }

            setPostCode(postcode: PostCode) {
                this.address1st(nts.uk.pr.view.base.postcode.service.toAddress(postcode));
                this.kanaAddress1st(nts.uk.pr.view.base.postcode.service.toKana(postcode));
                this.postalCode(postcode.postcode);
            }

            private searchZipCode() {
                var self = this;
                var messageList = [
                    { messageId: "ER001", message: "＊が入力されていません。" },
                    { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                    { messageId: "ER010", message: "対象データがありません。" }
                ];
                nts.uk.pr.view.base.postcode.service.findPostCodeZipCodeToRespone(self.postalCode()).done(data => {
                    if (data.errorCode == '0') {
                        for (var datamessage of messageList) {
                            if (datamessage.messageId == data.message) {
                                $('#inp_postalCode').ntsError('set', datamessage.message);
                            }
                        }
                    }
                    else if (data.errorCode == '1') {
                        self.setPostCode(data.postcode);
                        $('#inp_postalCode').ntsError('clear');
                    } else {
                        nts.uk.pr.view.base.postcode.service.findPostCodeZipCodeSelection(self.postalCode()).done(res => {
                            if (res.errorCode == '0') {
                                for (var datamessage of messageList) {
                                    if (datamessage.messageId == res.message) {
                                        $('#inp_postalCode').ntsError('set', datamessage.message);
                                    }
                                }
                            }
                            else if (res.errorCode == '1') {
                                self.setPostCode(res.postcode);
                                $('#inp_postalCode').ntsError('clear');
                            }
                        }).fail(function(error) {
                            console.log(error);
                        });
                    }
                }).fail(function(error) {
                    console.log(error);
                });
            }

            setDataImport(socialInsuranceOfficeImportDto: SocialInsuranceOfficeImportDto) {
                this.picName(socialInsuranceOfficeImportDto.picName);
                this.shortName(socialInsuranceOfficeImportDto.shortName);
                this.address1st(socialInsuranceOfficeImportDto.address1st);
                this.address2nd(socialInsuranceOfficeImportDto.address2nd);
                this.kanaAddress1st(socialInsuranceOfficeImportDto.kanaAddress1st);
                this.kanaAddress2nd(socialInsuranceOfficeImportDto.kanaAddress2nd);
                this.postalCode(socialInsuranceOfficeImportDto.potalCode);
                this.picPosition(socialInsuranceOfficeImportDto.picPosition);
            }
        }
    }
}
