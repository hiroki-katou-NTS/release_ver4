module nts.uk.pr.view.qmm008.d {
    export module viewmodel {
        import text = nts.uk.resource.getText;
        import confirm = nts.uk.ui.dialog.confirm;
        export class ScreenModel {
            items: KnockoutObservableArray<SocialOfficeOverView>;
            columns2: KnockoutObservableArray<NtsGridListColumn>;
            currentCode: KnockoutObservable<any> = ko.observable('');
            count: number = 100;
            switchOptions: KnockoutObservableArray<any>;

            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;

            simpleValue: KnockoutObservable<string>;

            detail: KnockoutObservable<SocialOfficeDetail> = ko.observable(null);

            // combobox
            itemList: KnockoutObservableArray<ItemModelComBoBox>;
            selectedNoD38: KnockoutObservable<any> = ko.observable('');
            selectedNoD35: KnockoutObservable<any> = ko.observable('');
            isEnable: KnockoutObservable<boolean>;
            isEditable: KnockoutObservable<boolean>;
            
            isEnableCode: KnockoutObservable<boolean> = ko.observable(false);

            values: KnockoutObservable<string>;

            constructor() {
                let self = this;
                self.items = ko.observableArray([]);
                self.itemList = ko.observableArray([]);
                nts.uk.pr.view.qmm008.d.service.defaultData().done(function(response) {
                    for (let i = 0; i < response.listCodeName.length; i++) {
                        self.items.push(new SocialOfficeOverView(response.listCodeName[i].code, response.listCodeName[i].name));
                    }
                    self.detail(new SocialOfficeDetail(response.sociaInsuOfficeDetail));
                    self.currentCode(response.sociaInsuOfficeDetail.code);
                    for (let i = 0; i < response.sociaInsuPreInfos.length; i++) {
                        self.itemList.push(new ItemModelComBoBox(response.sociaInsuPreInfos[i].no, response.sociaInsuPreInfos[i].prefectureName));
                        if (response.sociaInsuOfficeDetail.healthInsurancePrefectureNo == response.sociaInsuPreInfos[i].no) {
                            self.selectedNoD35(response.sociaInsuPreInfos[i].no);
                        }
                        if (response.sociaInsuOfficeDetail.welfarePensionPrefectureNo == response.sociaInsuPreInfos[i].no) {
                            self.selectedNoD38(response.sociaInsuPreInfos[i].no);
                        }
                    }
                    
                    $( "#D4_2" ).append( '"disabled="disabled""' );
                    
                });

                this.columns2 = ko.observableArray([
                    { headerText: text('QMM008_110'), key: 'code', width: 100 },
                    { headerText: text('QMM008_111'), key: 'name', width: 200 }
                ]);

                this.switchOptions = ko.observableArray([
                    { code: "1", name: '四捨五入' },
                    { code: "2", name: '切り上げ' },
                    { code: "3", name: '切り捨て' }
                ]);

                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: text('QMM008_112'), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: text('QMM008_113'), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) }
                ]);
                self.selectedTab = ko.observable('tab-1');

                self.isEnable = ko.observable(true);
                self.isEditable = ko.observable(true);

                self.values = ko.observable('');

                self.currentCode.subscribe(function(codeId) {
                    if (codeId) {
                        nts.uk.pr.view.qmm008.d.service.findByCode(codeId).done(function(response) {
                            self.detail(new SocialOfficeDetail(response));
                            let selectedNo35 = _.find(self.itemList(), { no: response.healthInsurancePrefectureNo });
                            if(response.healthInsurancePrefectureNo)
                            self.selectedNoD35(selectedNo35.no);
                            let selectedNo38 = _.find(self.itemList(), { no: response.welfarePensionPrefectureNo });
                            if(response.welfarePensionPrefectureNo)
                            self.selectedNoD38(selectedNo38.no);
                            self.isEnableCode(false);
                        });
                    }
                });

            }

            /**
             * update 
            */
            private update(): void {
                let self = this;
                self.detail().healthInsurancePrefectureNo(self.selectedNoD35);
                self.detail().welfarePensionPrefectureNo(self.selectedNoD38);
                if(self.currentCode() == null) {
                    nts.uk.pr.view.qmm008.d.service.create(ko.toJS(self.detail)).done(function(response) {
                        if(response[0] == 'Msg_3') {
                            nts.uk.ui.dialog.error({ messageId: "Msg_3" }).then(function() {
                            });
                        } else {
                            self.items.push(new SocialOfficeOverView(response[0], response[1]));
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                                self.currentCode(response[0]);
                                self.isEnableCode(false);           
                            });
                        }
                    });
                } else {
                    nts.uk.pr.view.qmm008.d.service.update(ko.toJS(self.detail)).done(function(response) {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                           for (let i = 0; i < self.items().length; i++) {
                               if (self.items()[i].code == response[0]) {
                                   self.items()[i].name = response[1];
                                   self.items.valueHasMutated()
                               }
                           }
                            self.isEnableCode(false);
                        });
                    });
                }
                
            }

            /**
             * create
             */
            private create(): void {
                let self = this;
                self.detail(new SocialOfficeDetail());
                self.isEnableCode(true);
                self.currentCode(null);
                self.selectedNoD35(self.itemList()[0].code);
                self.selectedNoD38(self.itemList()[0].code);
            }

            /**
             * delete
             */
            private deleteOffice(): void {
                confirm({ messageId: "Msg_18" }).ifYes(() => {
                    let self = this;
                    let command = { code: self.currentCode() };
                    nts.uk.pr.view.qmm008.d.service.deleteOffice(command).done(function(response) {
                         nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                             for (let i = 0; i < self.items().length; i++) {
                                 if(self.items()[i].code == response[0]) {
                                     delete self.items()[i];
                                     self.items.valueHasMutated();
                                     if(self.items().length == 1) {
                                        self.detail(new SocialOfficeDetail());
                                     } else if(i == self.items().length -1 ) {
                                         let parameter = i-1 ;
                                     } else {
                                         let parameter = i+1 ;
                                     }
                                     nts.uk.pr.view.qmm008.d.service.findByCode(self.items()[parameter].code).done(function(response) {
                                         self.detail(new SocialOfficeDetail(response));
                                         let selectedNo35 = _.find(self.itemList(), { no: response.healthInsurancePrefectureNo });
                                         if(response.healthInsurancePrefectureNo)
                                         self.selectedNoD35(selectedNo35.no);
                                         let selectedNo38 = _.find(self.itemList(), { no: response.welfarePensionPrefectureNo });
                                         if(response.welfarePensionPrefectureNo)
                                         self.selectedNoD38(selectedNo38.no);
                                         self.currentCode(self.items()[parameter].code);
                                         self.isEnableCode(false);
                                     });
                                     
                                 }
                             }
                        });
                    });
                }).ifNo(() => {

                });
            }

        }

        class SocialOfficeOverView {
            code: KnockoutObservable<string> = ko.observable(null);
            name: KnockoutObservable<string> = ko.observable(null);
            constructor(code: KnockoutObservable<string>, name: KnockoutObservable<string>) {
                this.code = code;
                this.name = name;
            }
        }

        class ItemModelComBoBox {
            no: KnockoutObservable<string> = ko.observable(null);
            prefectureName: KnockoutObservable<string> = ko.observable(null);

            constructor(no: KnockoutObservable<string>, prefectureName: KnockoutObservable<string>) {
                this.no = no;
                this.prefectureName = prefectureName;
            }
        }

        interface ISocialOfficeDetail {
            code: KnockoutObservable<string>, name: KnockoutObservable<string>, shortName: KnockoutObservable<string>, representativeName: KnockoutObservable<string>,
            representativePosition: KnockoutObservable<string>, memo: KnockoutObservable<string>, postalCode: KnockoutObservable<string>, address1: KnockoutObservable<string>,
            addressKana1: KnockoutObservable<string>, address2: KnockoutObservable<string>, addressKana2: KnockoutObservable<string>, phoneNumber: KnockoutObservable<string>,
            welfarePensionFundNumber: KnockoutObservable<string>, welfarePensionOfficeNumber: KnockoutObservable<string>, healthInsuranceOfficeNumber: KnockoutObservable<string>,
            healthInsuranceUnionOfficeNumber: KnockoutObservable<string>, healthInsuranceOfficeNumber1: KnockoutObservable<string>, healthInsuranceOfficeNumber2: KnockoutObservable<string>,
            welfarePensionOfficeNumber1: KnockoutObservable<string>, welfarePensionOfficeNumber2: KnockoutObservable<string>, healthInsuranceCityCode: KnockoutObservable<string>,
            healthInsuranceOfficeCode: KnockoutObservable<string>, welfarePensionCityCode: KnockoutObservable<string>, welfarePensionOfficeCode: KnockoutObservable<string>, healthInsurancePrefectureNo: KnockoutObservable<string>,
            welfarePensionPrefectureNo: KnockoutObservable<string>
        }

        class SocialOfficeDetail {
            code: KnockoutObservable<string> = ko.observable(null);
            name: KnockoutObservable<string> = ko.observable(null);
            shortName: KnockoutObservable<string> = ko.observable(null);
            representativeName: KnockoutObservable<string> = ko.observable(null);
            representativePosition: KnockoutObservable<string> = ko.observable(null);
            memo: KnockoutObservable<string> = ko.observable(null);
            postalCode: KnockoutObservable<string> = ko.observable(null);
            address1: KnockoutObservable<string> = ko.observable(null);
            addressKana1: KnockoutObservable<string> = ko.observable(null);
            address2: KnockoutObservable<string> = ko.observable(null);
            addressKana2: KnockoutObservable<string> = ko.observable(null);
            phoneNumber: KnockoutObservable<string> = ko.observable(null);
            welfarePensionFundNumber: KnockoutObservable<string> = ko.observable(null);
            welfarePensionOfficeNumber: KnockoutObservable<string> = ko.observable(null);
            healthInsuranceOfficeNumber: KnockoutObservable<string> = ko.observable(null);
            healthInsuranceUnionOfficeNumber: KnockoutObservable<string> = ko.observable(null);
            healthInsuranceOfficeNumber1: KnockoutObservable<string> = ko.observable(null);
            healthInsuranceOfficeNumber2: KnockoutObservable<string> = ko.observable(null);
            welfarePensionOfficeNumber1: KnockoutObservable<string> = ko.observable(null);
            welfarePensionOfficeNumber2: KnockoutObservable<string> = ko.observable(null);
            healthInsuranceCityCode: KnockoutObservable<string> = ko.observable(null);
            healthInsuranceOfficeCode: KnockoutObservable<string> = ko.observable(null);
            welfarePensionCityCode: KnockoutObservable<string> = ko.observable(null);
            welfarePensionOfficeCode: KnockoutObservable<string> = ko.observable(null);
            healthInsurancePrefectureNo: KnockoutObservable<string> = ko.observable(null);
            welfarePensionPrefectureNo: KnockoutObservable<string> = ko.observable(null);
            constructor(parameter?: ISocialOfficeDetail) {
                this.code(parameter ? parameter.code : '');
                this.name(parameter ? parameter.name : '');
                this.shortName(parameter ? parameter.shortName : '');
                this.representativeName(parameter ? parameter.representativeName : '');
                this.representativePosition(parameter ? parameter.representativePosition : '');
                this.memo(parameter ? parameter.memo : '');
                this.postalCode(parameter ? parameter.postalCode : '');
                this.address1(parameter ? parameter.address1 : '');
                this.addressKana1(parameter ? parameter.addressKana1 : '');
                this.address2(parameter ? parameter.address2 : '');
                this.addressKana2(parameter ? parameter.addressKana2 : '');
                this.phoneNumber(parameter ? parameter.phoneNumber : '');
                this.welfarePensionFundNumber(parameter ? parameter.welfarePensionFundNumber : '');
                this.welfarePensionOfficeNumber(parameter ? parameter.welfarePensionOfficeNumber : '');
                this.healthInsuranceOfficeNumber(parameter ? parameter.healthInsuranceOfficeNumber : '');
                this.healthInsuranceUnionOfficeNumber(parameter ? parameter.healthInsuranceUnionOfficeNumber : '');
                this.healthInsuranceOfficeNumber1(parameter ? parameter.healthInsuranceOfficeNumber1 : '');
                this.healthInsuranceOfficeNumber2(parameter ? parameter.healthInsuranceOfficeNumber2 : '');
                this.welfarePensionOfficeNumber1(parameter ? parameter.welfarePensionOfficeNumber1 : '');
                this.welfarePensionOfficeNumber2(parameter ? parameter.welfarePensionOfficeNumber2 : '');
                this.healthInsuranceCityCode(parameter ? parameter.healthInsuranceCityCode : '');
                this.healthInsuranceOfficeCode(parameter ? parameter.healthInsuranceOfficeCode : '');
                this.welfarePensionCityCode(parameter ? parameter.welfarePensionCityCode : '');
                this.welfarePensionOfficeCode(parameter ? parameter.welfarePensionOfficeCode : '');
                this.healthInsurancePrefectureNo(parameter ? parameter.healthInsurancePrefectureNo : '');
                this.welfarePensionPrefectureNo(parameter ? parameter.welfarePensionPrefectureNo : '');
            }

        }


    }
}