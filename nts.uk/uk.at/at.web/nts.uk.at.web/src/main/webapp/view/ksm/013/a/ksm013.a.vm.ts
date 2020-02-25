module nts.uk.at.view.ksm013.a {
    // Import
    export module viewmodel {

        export class ScreenModel {

            columns: KnockoutObservableArray<any>;
            selectedCode: KnockoutObservable<string>;
            lstNurseCl: KnockoutObservableArray<NurseClassification> = ko.observableArray([]);
            nurseClModel: NurseClassificationModel;
            isEditting: KnockoutObservable<boolean>;
            lstLicense: KnockoutObservableArray<any>;
            //selectedLicense: KnockoutObservable<number>;


            constructor() {
                var self = this;
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KSM013_5'), key: 'code', formatter: _.escape, width: 100 },
                    { headerText: nts.uk.resource.getText('KSM013_6'), key: 'name', formatter: _.escape, width: 200 }
                ]);
                self.nurseClModel = new NurseClassificationModel("", "", 0, false);
                self.isEditting = ko.observable(false);
                self.selectedCode = ko.observable('');
                self.selectedCode.subscribe(function(codeChanged: string) {
                    let dfd = $.Deferred();
                    if (_.isEmpty(codeChanged)) {
                        dfd.resolve();
                    } else {
                        service.findDetail(codeChanged).done((dataDetail: NurseDetailClassification) => {
                            self.isEditting(true);
                            self.nurseClModel.createDataModel(dataDetail);
                            self.clearErrorAll();
                            dfd.resolve();
                        })
                    }
                    return dfd.promise();
                });

                self.lstLicense = ko.observableArray([
                    { licenseCode: '0', licenseName: nts.uk.resource.getText('Enum_LicenseClassification_NURSE') },
                    { licenseCode: '1', licenseName: nts.uk.resource.getText('Enum_LicenseClassification_NURSE_ASSOCIATE') },
                    { licenseCode: '2', licenseName: nts.uk.resource.getText('Enum_LicenseClassification_NURSE_ASSIST') }
                ]);
                //self.selectedLicense = ko.observable(0);
            }

            public startPage(): JQueryPromise<any> {
                let self = this,
                    dfd = $.Deferred();
                nts.uk.ui.block.grayout();
                service.findAll().done((dataAll: Array<NurseClassification>) => {
                    if (!_.isEmpty(dataAll)) {
                        self.lstNurseCl(_.sortBy(dataAll, [function(o) { return o.code; }]));
                        self.selectedCode(self.lstNurseCl()[0].code);
                    } else {
                        self.isEditting(false);
                        $('[tabindex= 5]').focus();
                        self.nurseClModel.resetModel();
                    }
                    nts.uk.ui.block.clear();
                    dfd.resolve();
                })

                return dfd.promise();
            }

            public newCreate() {
                let self = this;
                self.isEditting(false);
                self.selectedCode("");
                $('[tabindex= 5]').focus();
                self.clearErrorAll();
                self.nurseClModel.resetModel();
            }

            public register() {
                let self = this,
                    dfd = $.Deferred();
                if (self.validateAll()) {
                    return;
                };
                nts.uk.ui.block.grayout();
                if (!self.isEditting()) {
                    // register
                    service.register(ko.toJSON(self.nurseClModel)).done(() => {
                        service.findAll().done((dataAll: Array<NurseClassification>) => {
                            self.lstNurseCl(_.sortBy(dataAll, [function(o) { return o.code; }]));
                            self.selectedCode(self.nurseClModel.nurseClassificationCode());
                            nts.uk.ui.block.clear();
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                            self.isEditting(true);
                            dfd.resolve();
                        })
                    }).fail((res) => {
                        nts.uk.ui.dialog.alertError(res).then(function() {
                            nts.uk.ui.block.clear();
                            if (res.messageId == "Msg_3") {
                                self.selectedCode("");
                                $('#nurseClassificationCode').ntsError('set', { messageId: "Msg_3" });
                            }
                        });
                    });
                } else {
                    // update
                    service.update(ko.toJSON(self.nurseClModel)).done(() => {
                        self.lstNurseCl(_.map(self.lstNurseCl(), function(el: NurseClassification) {
                            if (el.code == self.nurseClModel.nurseClassificationCode()) {
                                return new NurseClassification(self.nurseClModel.nurseClassificationCode(), self.nurseClModel.nurseClassificationName());
                            }
                            return el;

                        }));
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                        nts.uk.ui.block.clear();
                        self.isEditting(true);
                        dfd.resolve();
                    });
                }
                return dfd.promise();
            }

            public remove() {
                let self = this,
                    dfd = $.Deferred();
                nts.uk.ui.block.grayout();
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                    let param = {
                        nurseClassificationCode: self.nurseClModel.nurseClassificationCode()
                    }
                    service.remove(param).done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                        if (self.lstNurseCl().length == 1) {
                            self.lstNurseCl([]);
                            self.newCreate();
                        } else {
                            let indexSelected: number;
                            for (let index: number = 0; index < self.lstNurseCl().length; index++) {
                                if (self.lstNurseCl()[index].code == self.selectedCode()) {
                                    indexSelected = (index == self.lstNurseCl().length - 1) ? index - 1 : index;
                                    self.lstNurseCl().splice(index, 1);
                                    break;
                                }
                            }
                            self.selectedCode(self.lstNurseCl()[indexSelected].code);
                        }
                        nts.uk.ui.block.clear();
                        dfd.resolve();
                    });
                }).ifNo(function() {
                    nts.uk.ui.block.clear();
                    dfd.resolve();
                    return;
                });
                return dfd.promise()
            }

            private validateAll(): boolean {
                $('#nurseClassificationCode').ntsEditor('validate');
                $('#nurseClassificationName').ntsEditor('validate');
                $('#license').ntsEditor('validate');

                if ($('#nurseClassificationCode').ntsError("hasError") || $('#nurseClassificationCode').ntsError("hasError") || $('#license').ntsError("hasError")) {
                    return true;
                }
                return false;
            }

            private clearErrorAll(): void {
                $('#nurseClassificationCode').ntsError('clear');
                $('#nurseClassificationName').ntsError('clear');
                $('#license').ntsError('clear');
            }

        }


        export class NurseClassificationModel {
            nurseClassificationCode: KnockoutObservable<string>;
            nurseClassificationName: KnockoutObservable<string>;
            license: KnockoutObservable<number>;
            officeWorker: KnockoutObservable<boolean>;
            constructor(nurseClassificationCode: string, nurseClassificationName: string, license: number, officeWorker: boolean) {
                let self = this;
                self.nurseClassificationCode = ko.observable(nurseClassificationCode);
                self.nurseClassificationName = ko.observable(nurseClassificationName);
                self.license = ko.observable(license);
                self.officeWorker = ko.observable(officeWorker);
                self.license.subscribe(function(codeChanged: any) {
                    if (codeChanged != 2) self.officeWorker(false);
                });
            }

            public createDataModel(data: NurseDetailClassification) {
                let self = this;
                self.nurseClassificationCode(data.code);
                self.nurseClassificationName(data.name);
                self.license(data.license);
                self.officeWorker(data.officeWorker);
            }

            public resetModel() {
                let self = this;
                self.nurseClassificationCode("");
                self.nurseClassificationName("");
                self.license(0);
                self.officeWorker(false);

            }
        }

        export class NurseDetailClassification {

            /**
             * 看護区分コード
             */
            nurseClassificationCode: string;

            /**
             * 看護区分名称   
             */
            nurseClassificationName: string;

            /**
             *  免許区分    
             */
            license: number;

            /**
             *  事務的業務従事者か
             */
            officeWorker: boolean;

            constructor(nurseClassificationCode: string, nurseClassificationName: string, license: number, officeWorker: boolean) {
                this.nurseClassificationCode = nurseClassificationCode;
                this.nurseClassificationName = nurseClassificationName;
                this.license = license;
                this.officeWorker = officeWorker;
            }
        }

        export class NurseClassification {

            /**
             * 看護区分コード
             */
            code: string;

            /**
             * 看護区分名称   
             */
            name: string;

            constructor(nurseClassificationCode: string, nurseClassificationName: string) {
                this.code = nurseClassificationCode;
                this.name = nurseClassificationName;

            }
        }
    }
}