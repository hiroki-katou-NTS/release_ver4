module nts.uk.pr.view.qmm008.e {

    export module viewmodel {
        import InsuranceOfficeItemDto = qmm008.b.service.model.finder.InsuranceOfficeItemDto;
        import bservice = nts.uk.pr.view.qmm008.b.service;
        import postcodeService = nts.uk.pr.view.base.postcode.service;
        import OfficeItemDto = qmm008.e.service.model.finder.OfficeItemDto;

        export class ScreenModel {
            // for left list
            officeItems: KnockoutObservableArray<ItemModel>;
            columns2: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;

            //for tab panel
            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;

            isTransistReturnData: KnockoutObservable<boolean>;

            // for group radio button
            listOptions: KnockoutObservableArray<optionsModel>;
            selectedValue: KnockoutObservable<optionsModel>;
            //model
            officeModel: KnockoutObservable<SocialInsuranceOfficeModel>;
            textInputOption: KnockoutObservable<nts.uk.ui.option.TextEditorOption>;
            selectedOfficeCode: KnockoutObservable<string>;
            previousSelectedOfficeCode : KnockoutObservable<string>;
            showConfirmDialog : KnockoutObservable<boolean>;
            enabled: KnockoutObservable<boolean>;
            deleteButtonControll: KnockoutObservable<boolean>;
            errorList: KnockoutObservableArray<any>;
            dirty: nts.uk.ui.DirtyChecker;
            constructor() {
                var self = this;
                self.enabled = ko.observable(true);
                self.deleteButtonControll = ko.observable(true);

                self.officeItems = ko.observableArray([]);
                self.columns2 = ko.observableArray([
                    { headerText: 'コード', key: 'code', width: 100 },
                    { headerText: '名称', key: 'name', width: 150 }
                ]);

                self.listOptions = ko.observableArray([new optionsModel(1, "基本情報"), new optionsModel(2, "保険料マスタの情報")]);
                self.selectedValue = ko.observable(new optionsModel(1, ""));

                self.isTransistReturnData = ko.observable(nts.uk.ui.windows.getShared("isTransistReturnData"));


                //panel
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: '基本情報', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: '保険料マスタの情報', content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                ]);
                self.selectedTab = ko.observable('tab-1');
                self.officeModel = ko.observable(new SocialInsuranceOfficeModel());
                //text input options
                self.textInputOption = ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    width: "100",
                    textalign: "center"
                }));
                self.selectedOfficeCode = ko.observable('');
                self.previousSelectedOfficeCode = ko.observable('');
                self.showConfirmDialog = ko.observable(true);
                self.errorList = ko.observableArray([
                    { messageId: "ER001", message: "＊が入力されていません。" },
                    { messageId: "ER007", message: "＊が選択されていません。" },
                    { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                    { messageId: "ER008", message: "選択された＊は使用されているため削除できません。" },
                    { messageId: "AL001", message: "変更された内容が登録されていません。\r\n よろしいですか。" },
                    { messageId: "AL002", message: "データを削除します。\r\nよろしいですか？" },
                ]);
                //dirty check
                self.dirty = new nts.uk.ui.DirtyChecker(ko.observable(''));

                self.selectedOfficeCode.subscribe(function(selectedOfficeCode: string) {
                    if (selectedOfficeCode!='') {
                        if (self.dirty.isDirty() && self.showConfirmDialog() && selectedOfficeCode != self.previousSelectedOfficeCode()) {
                            nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function() {
                                self.showConfirmDialog(false);
                                self.dirty.reset();
                                self.loadItemOffice(selectedOfficeCode);
                            }).ifNo(function() {
                                self.selectedOfficeCode(self.previousSelectedOfficeCode());
                            });
                        }
                        else {
                            if (selectedOfficeCode != self.previousSelectedOfficeCode()) {
                                self.dirty.reset();
                                self.loadItemOffice(selectedOfficeCode);
                            }
                        }
                    }
                });
            }
            private loadItemOffice(selectedOfficeCode: string) {
                var self = this;
                $('.save-error').ntsError('clear');
                if (selectedOfficeCode != null && selectedOfficeCode != undefined && selectedOfficeCode != "") {
                    self.enabled(false);
                    self.deleteButtonControll(true);
                    $.when(self.load(selectedOfficeCode)).done(function() {
                        //load data success
                        self.previousSelectedOfficeCode(selectedOfficeCode);
                        self.showConfirmDialog(true);
                    }).fail(function(res) {
                        //when load data error
                    });
                }
            }
            // start
            public start(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                //first load
                self.loadAllInsuranceOfficeData().done(function() {
                    // Load first result.
                    if (self.officeItems().length > 0) {
                        //load first office
//                        self.selectedOfficeCode(self.officeItems()[0].code);
                    } else {
                        //register new office mode
                        self.addNew();
                    }
                    // Resolve
                    dfd.resolve();
                });
                // Return.
                return dfd.promise();
            }
            //
            public loadAllInsuranceOfficeData(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred<any>();
                // find all insurance office 
                bservice.findInsuranceOffice('').done(function(data: Array<InsuranceOfficeItemDto>) {
                    //check list office is empty
                    if (data != null) {
                        self.officeItems([]);
                        // Set data get from service to list.
                        data.forEach((item, index) => {
                            self.officeItems.push(new ItemModel(item.code, item.name));
                        });
                        dfd.resolve(data);
                    }
                    //list is empty
                    else {
                        dfd.resolve(null);
                    }
                });
                // Return.
                return dfd.promise();
            }
            public load(officeCode: string): JQueryPromise<any> {
                if (officeCode != null && officeCode != '') {
                    var self = this;
                    service.getOfficeItemDetail(officeCode).done(function(data: OfficeItemDto) {
                        //Convert data get from service to screen
                        self.officeModel().officeCode(data.code);
                        self.officeModel().officeName(data.name);
                        self.officeModel().shortName(data.shortName);
                        self.officeModel().PicName(data.picName);
                        self.officeModel().PicPosition(data.picPosition);
                        self.officeModel().potalCode(data.potalCode);
                        self.officeModel().address1st(data.address1st);
                        self.officeModel().kanaAddress1st(data.kanaAddress1st);
                        self.officeModel().address2nd(data.address2nd);
                        self.officeModel().kanaAddress2nd(data.kanaAddress2nd);
                        self.officeModel().phoneNumber(data.phoneNumber);
                        self.officeModel().healthInsuOfficeRefCode1st(data.healthInsuOfficeRefCode1st);
                        self.officeModel().healthInsuOfficeRefCode2nd(data.healthInsuOfficeRefCode2nd);
                        self.officeModel().pensionOfficeRefCode1st(data.pensionOfficeRefCode1st);
                        self.officeModel().pensionOfficeRefCode2nd(data.pensionOfficeRefCode2nd);
                        self.officeModel().welfarePensionFundCode(data.welfarePensionFundCode);
                        self.officeModel().officePensionFundCode(data.officePensionFundCode);
                        self.officeModel().healthInsuCityCode(data.healthInsuCityCode);
                        self.officeModel().healthInsuOfficeSign(data.healthInsuOfficeSign);
                        self.officeModel().pensionCityCode(data.pensionCityCode);
                        self.officeModel().pensionOfficeSign(data.pensionOfficeSign);
                        self.officeModel().healthInsuOfficeCode(data.healthInsuOfficeCode);
                        self.officeModel().healthInsuAssoCode(data.healthInsuAssoCode);
                        self.officeModel().memo(data.memo);
                        self.dirty = new nts.uk.ui.DirtyChecker(self.officeModel);
                    });
                }
                return;
            }
            public convertDatatoList(data: Array<InsuranceOfficeItemDto>): Array<ItemModel> {
                var OfficeItemList: Array<ItemModel> = [];
                // 
                data.forEach((item, index) => {
                    OfficeItemList.push(new ItemModel(item.code, item.name));
                });
                return OfficeItemList;
            }

            //save (mode: update or create new)
            public save() {
                var self = this;
                //if update office
                if (!self.enabled())
                    self.updateOffice();
                //if register new office
                else {
                    self.registerOffice();
                }
            }

            //update office
            private updateOffice() {
                var self = this;
                service.update(self.collectData()).done(function() {
                    //when update done
                    self.loadAllInsuranceOfficeData().done(function() {
                        //focus add new item
                        self.selectedOfficeCode(self.officeModel().officeCode());
                    });
                    self.dirty.reset();
                }).fail(function() {
                    //update fail
                });
            }

            //create new Office
            private registerOffice() {
                var self = this;
                service.register(self.collectData()).done(function() {
                    // when register done
                    self.loadAllInsuranceOfficeData().done(function() {
                        //focus add new item
                        self.selectedOfficeCode(self.officeModel().officeCode());
                    });
                    self.dirty.reset();
                }).fail(function(res) {
                    if (res.messageId == self.errorList()[2].messageId) {
                        $('#officeCode').ntsError('set', self.errorList()[2].message);
                    }
                    if (res.messageId == self.errorList()[0].messageId) {
                        if(!self.officeModel().officeCode())
                        $('#officeCode').ntsError('set', self.errorList()[0].message);
                        if(!self.officeModel().officeName())
                        $('#officeName').ntsError('set', self.errorList()[0].message);
                        if(!self.officeModel().PicPosition())
                        $('#picPosition').ntsError('set', self.errorList()[0].message);
                    }
                });
            }
            
            private removeWithDirtyCheck() {
                var self = this;
                nts.uk.ui.dialog.confirm(self.errorList()[5].message).ifYes(function() {
                    self.dirty.reset();
                    self.remove();
                }).ifNo(function() {
                });
            }
            //remove office  by office Code
            private remove() {
                var self = this;
                if (self.selectedOfficeCode() != '') {
                    service.remove(self.selectedOfficeCode()).done(function() {
                        //if remove success
                            
                    }).fail(function() {
                        // if remove fail    
                    });
                    
                    //reload list
                    self.loadAllInsuranceOfficeData().done(function() {
                        // if empty list -> add new mode
                        if (self.officeItems().length == 0) {
                            self.addNew();
                        } else {
                            self.selectedOfficeCode(self.officeItems()[0].code);
                        }
                    });
                }
            }

            //collect all data
            public collectData() {
                var self = this;
                var a = new service.model.finder.OfficeItemDto(
                    "company code",
                    self.officeModel().officeCode(),
                    self.officeModel().officeName(),
                    self.officeModel().shortName(),
                    self.officeModel().PicName(),
                    self.officeModel().PicPosition(),
                    self.officeModel().potalCode(),
                    self.officeModel().address1st(),
                    self.officeModel().address2nd(),
                    self.officeModel().kanaAddress1st(),
                    self.officeModel().kanaAddress2nd(),
                    self.officeModel().phoneNumber(),
                    self.officeModel().healthInsuOfficeRefCode1st(),
                    self.officeModel().healthInsuOfficeRefCode2nd(),
                    self.officeModel().pensionOfficeRefCode1st(),
                    self.officeModel().pensionOfficeRefCode2nd(),
                    self.officeModel().welfarePensionFundCode(),
                    self.officeModel().officePensionFundCode(),
                    self.officeModel().healthInsuCityCode(),
                    self.officeModel().healthInsuOfficeSign(),
                    self.officeModel().pensionCityCode(),
                    self.officeModel().pensionOfficeSign(),
                    self.officeModel().healthInsuOfficeCode(),
                    self.officeModel().healthInsuAssoCode(),
                    self.officeModel().memo()
                );
                return a;
            }
            public addNewWithDirtyCheck() {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function() {
                        self.addNew();
                        self.dirty.reset();
                    }).ifNo(function() {
                    });
                }
                else {
                    self.addNew();
                    self.showConfirmDialog(true);
                }
            }
            //reset all field when click add new office button
            public addNew() {
                var self = this;
                //reset all input fields to blank
                self.officeModel().officeCode('');
                self.officeModel().officeName('');
                self.officeModel().shortName('');
                self.officeModel().PicName('');
                self.officeModel().PicPosition('');
                self.officeModel().potalCode('');
                self.officeModel().address1st('');
                self.officeModel().kanaAddress1st('');
                self.officeModel().address2nd('');
                self.officeModel().kanaAddress2nd('');
                self.officeModel().phoneNumber('');
                self.officeModel().healthInsuOfficeRefCode1st('');
                self.officeModel().healthInsuOfficeRefCode2nd('');
                self.officeModel().pensionOfficeRefCode1st('');
                self.officeModel().pensionOfficeRefCode2nd('');
                self.officeModel().welfarePensionFundCode('');
                self.officeModel().officePensionFundCode('');
                self.officeModel().healthInsuCityCode('');
                self.officeModel().healthInsuOfficeSign('');
                self.officeModel().pensionCityCode('');
                self.officeModel().pensionOfficeSign('');
                self.officeModel().healthInsuOfficeCode('');
                self.officeModel().healthInsuAssoCode('');
                self.officeModel().memo('');
                //set enabled code input
                self.enabled(true);
                //disable remove
                self.deleteButtonControll(false);
                //reset selected officeCode
                self.selectedOfficeCode('');
                self.showConfirmDialog(false);
                self.previousSelectedOfficeCode('');
                self.dirty = new nts.uk.ui.DirtyChecker(self.officeModel);
            }
            
            closeDialogWithDirtyCheck() {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function() {
                        self.dirty.reset();
                        nts.uk.ui.windows.setShared("codeOfNewOffice", null, self.isTransistReturnData());
                        nts.uk.ui.windows.close();
                    }).ifNo(function() {
                    });
                }
                else {
                    self.closeDialog();
                }
            }
            
            closeDialog() {
                var self = this;
                // Set child value
                nts.uk.ui.windows.setShared("codeOfNewOffice", self.officeModel().officeCode(), this.isTransistReturnData());
                nts.uk.ui.windows.close();
            }
        }

        //Models
        export class SocialInsuranceOfficeModel {
            officeCode: KnockoutObservable<string>;
            officeName: KnockoutObservable<string>;
            shortName: KnockoutObservable<string>;
            PicName: KnockoutObservable<string>;
            PicPosition: KnockoutObservable<string>;

            potalCode: KnockoutObservable<string>;
            address1st: KnockoutObservable<string>;
            kanaAddress1st: KnockoutObservable<string>;
            address2nd: KnockoutObservable<string>;
            kanaAddress2nd: KnockoutObservable<string>;
            phoneNumber: KnockoutObservable<string>;

            //insurance info input 
            healthInsuOfficeRefCode1st: KnockoutObservable<string>;
            healthInsuOfficeRefCode2nd: KnockoutObservable<string>;
            pensionOfficeRefCode1st: KnockoutObservable<string>;
            pensionOfficeRefCode2nd: KnockoutObservable<string>;
            welfarePensionFundCode: KnockoutObservable<string>;
            officePensionFundCode: KnockoutObservable<string>;

            healthInsuCityCode: KnockoutObservable<string>;
            healthInsuOfficeSign: KnockoutObservable<string>;
            pensionCityCode: KnockoutObservable<string>;
            pensionOfficeSign: KnockoutObservable<string>;
            healthInsuOfficeCode: KnockoutObservable<string>;
            healthInsuAssoCode: KnockoutObservable<string>;
            memo: KnockoutObservable<string>;

            constructor() {
                //basic info input
                this.officeCode = ko.observable('');
                this.officeName = ko.observable('');
                this.shortName = ko.observable('');
                this.PicName = ko.observable('');
                this.PicPosition = ko.observable('');

                this.potalCode = ko.observable('');
                this.address1st = ko.observable('');
                this.kanaAddress1st = ko.observable('');
                this.address2nd = ko.observable('');
                this.kanaAddress2nd = ko.observable('');
                this.phoneNumber = ko.observable('');

                //insurance info input 
                this.healthInsuOfficeRefCode1st = ko.observable('');
                this.healthInsuOfficeRefCode2nd = ko.observable('');
                this.pensionOfficeRefCode1st = ko.observable('');
                this.pensionOfficeRefCode2nd = ko.observable('');
                this.welfarePensionFundCode = ko.observable('');
                this.officePensionFundCode = ko.observable('');

                this.healthInsuCityCode = ko.observable('');
                this.healthInsuOfficeSign = ko.observable('');
                this.pensionCityCode = ko.observable('');
                this.pensionOfficeSign = ko.observable('');
                this.healthInsuOfficeCode = ko.observable('');
                this.healthInsuAssoCode = ko.observable('');
                this.memo = ko.observable('');
            }

            private setPostCode(postcode: postcodeService.model.PostCode) {
                var self = this;
                self.potalCode(postcode.postcode);
                self.address1st(postcodeService.toAddress(postcode));
                self.kanaAddress1st(postcodeService.toKana(postcode));
            }

            private searchPostCode() {
                var self = this;
                var messageList = [
                    { messageId: "ER001", message: "＊が入力されていません。" },
                    { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                    { messageId: "ER010", message: "対象データがありません。" }
                ];
                postcodeService.findPostCodeZipCodeToRespone(self.potalCode()).done(data => {
                  if (data.errorCode == '0') {
                        for (var datamessage of messageList) {
                            if (datamessage.messageId == data.message) {
                                $('#inp_postCode').ntsError('set', datamessage.message);
                            }
                        }
                    }
                    else if (data.errorCode == '1') {
                        self.setPostCode(data.postcode);
                        $('#inp_postCode').ntsError('clear');
                    } else {
                        postcodeService.findPostCodeZipCodeSelection(self.potalCode()).done(res => {
                            if (res.errorCode == '0') {
                                for (var datamessage of messageList) {
                                    if (datamessage.messageId == res.message) {
                                        $('#inp_postCode').ntsError('set', datamessage.message);
                                    }
                                }
                            }
                            else if (res.errorCode == '1') {
                                self.setPostCode(res.postcode);
                                $('#inp_postCode').ntsError('clear');
                            }
                        }).fail(function(error) {
                            console.log(error);
                        });
                    }
                }).fail(function(error) {
                    console.log(error);
                });
            }
        }
        export class ItemModel {
            code: string;
            name: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }

        export class optionsModel {
            id: number;
            name: string;
            constructor(id: number, name :string) {
                var self = this;
                self.id = id;
                self.name = name;
            }
        }
    }
}