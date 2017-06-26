module nts.uk.pr.view.qmm010.b {

    import option = nts.uk.ui.option;
    import SocialInsuranceOfficeImportDto = service.model.SocialInsuranceOfficeImportDto;

    export module viewmodel {
        export class ScreenModel {

            textSearch: any;
            lstSocialInsuranceOffice: KnockoutObservableArray<SocialInsuranceOfficeImportDto>;
            selectLstSocialInsuranceOffice: KnockoutObservable<string>;
            columnsLstSocialInsuranceOffice: KnockoutObservableArray<any>;

            constructor() {
                var self = this;
                self.selectLstSocialInsuranceOffice = ko.observable('');
                self.columnsLstSocialInsuranceOffice = ko.observableArray([
                    { headerText: 'コード', prop: 'code', width: 100 },
                    { headerText: '名称', prop: 'name', width: 150 }
                ]);
            }

            //start page
            startPage(): JQueryPromise<this> {
                var self = this;
                var dfd = $.Deferred<this>();
                self.findAllInsuranceOffice().done(data => {
                    dfd.resolve(self);
                });
                return dfd.promise();
            }

            //Connection service find All InsuranceOffice
            findAllInsuranceOffice(): JQueryPromise<this> {
                var self = this;
                var dfd = $.Deferred<this>();
                var data = nts.uk.ui.windows.getShared("dataInsuranceOffice");
                self.lstSocialInsuranceOffice = ko.observableArray<SocialInsuranceOfficeImportDto>(data);
                dfd.resolve(self);
                return dfd.promise();
            }

            //Find code data lst by check import data
            private findCode(code: string): SocialInsuranceOfficeImportDto {
                var self = this;
                for (var itemOfLst of self.lstSocialInsuranceOffice()) {
                    if (itemOfLst.code === code) {
                        return itemOfLst;
                    }
                }
                return null;
            }

            //check dulicate code
            private importData() {
                var self = this;
                if (self.selectLstSocialInsuranceOffice() != null) {
                    var socialInsuranceOfficeImport: SocialInsuranceOfficeImportDto;
                    socialInsuranceOfficeImport = self.findCode(self.selectLstSocialInsuranceOffice());
                    nts.uk.ui.windows.setShared('importData', socialInsuranceOfficeImport);
                    nts.uk.ui.windows.close();
                }
            }
        }
    }
}
