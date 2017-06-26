module nts.uk.pr.view.qmm011.e {

    import option = nts.uk.ui.option;
    import InsuranceBusinessType = nts.uk.pr.view.qmm011.a.service.model.InsuranceBusinessType;
    import InsuBizRateItemDto = nts.uk.pr.view.qmm011.a.service.model.InsuBizRateItemDto;
    import AccidentInsuranceRateModel = nts.uk.pr.view.qmm011.a.viewmodel.AccIRModel;
    import InsuranceBusinessTypeDto = nts.uk.pr.view.qmm011.a.service.model.InsuranceBusinessTypeDto;

    export module viewmodel {

        export class ScreenModel {

            insuranceBusinessTypeUpdateModel: KnockoutObservable<InsuranceBusinessTypeUpdateModel>;
            textEditorOption: KnockoutObservable<option.TextEditorOption>;

            constructor() {
                var self = this;
                self.textEditorOption = ko.mapping.fromJS(new option.TextEditorOption());
                var insuranceBusinessTypeDto = nts.uk.ui.windows.getShared("InsuranceBusinessTypeDto");
                self.insuranceBusinessTypeUpdateModel
                    = ko.observable(new InsuranceBusinessTypeUpdateModel(insuranceBusinessTypeDto));
            }

            updateInsuranceBusinessType() {
                var self = this;
                var insuranceBusinessType: InsuranceBusinessTypeDto;
                //set up data resquest
                insuranceBusinessType =
                    new InsuranceBusinessTypeDto(self.insuranceBusinessTypeUpdateModel().bizNameBiz1St(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz2Nd(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz3Rd(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz4Th(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz5Th(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz6Th(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz7Th(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz8Th(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz9Th(),
                        self.insuranceBusinessTypeUpdateModel().bizNameBiz10Th(),
                        self.insuranceBusinessTypeUpdateModel().version());
                //call service update
                service.updateInsuranceBusinessType(insuranceBusinessType).done(data => {
                    nts.uk.ui.windows.setShared("insuranceBusinessTypeUpdateModel", self.insuranceBusinessTypeUpdateModel());
                    nts.uk.ui.windows.close();
                });
            }
        }

        export class InsuranceBusinessTypeUpdateModel {

            bizNameBiz1St: KnockoutObservable<string>;
            bizNameBiz2Nd: KnockoutObservable<string>;
            bizNameBiz3Rd: KnockoutObservable<string>;
            bizNameBiz4Th: KnockoutObservable<string>;
            bizNameBiz5Th: KnockoutObservable<string>;
            bizNameBiz6Th: KnockoutObservable<string>;
            bizNameBiz7Th: KnockoutObservable<string>;
            bizNameBiz8Th: KnockoutObservable<string>;
            bizNameBiz9Th: KnockoutObservable<string>;
            bizNameBiz10Th: KnockoutObservable<string>;
            version: KnockoutObservable<number>;

            constructor(insuranceBusinessTypeDto: InsuranceBusinessTypeDto) {
                this.bizNameBiz1St = ko.observable(insuranceBusinessTypeDto.bizNameBiz1St);
                this.bizNameBiz2Nd = ko.observable(insuranceBusinessTypeDto.bizNameBiz2Nd);
                this.bizNameBiz3Rd = ko.observable(insuranceBusinessTypeDto.bizNameBiz3Rd);
                this.bizNameBiz4Th = ko.observable(insuranceBusinessTypeDto.bizNameBiz4Th);
                this.bizNameBiz5Th = ko.observable(insuranceBusinessTypeDto.bizNameBiz5Th);
                this.bizNameBiz6Th = ko.observable(insuranceBusinessTypeDto.bizNameBiz6Th);
                this.bizNameBiz7Th = ko.observable(insuranceBusinessTypeDto.bizNameBiz7Th);
                this.bizNameBiz8Th = ko.observable(insuranceBusinessTypeDto.bizNameBiz8Th);
                this.bizNameBiz9Th = ko.observable(insuranceBusinessTypeDto.bizNameBiz9Th);
                this.bizNameBiz10Th = ko.observable(insuranceBusinessTypeDto.bizNameBiz10Th);
                this.version = ko.observable(insuranceBusinessTypeDto.version);
            }
        }
    }
}
