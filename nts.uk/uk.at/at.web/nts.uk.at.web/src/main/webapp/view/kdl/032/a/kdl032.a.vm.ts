module nts.uk.at.view.kdl032.a.viewmodel {
    export class ScreenModel {
        deviationTimeList: KnockoutObservableArray<any>;
        columns: KnockoutObservableArray<NtsGridListColumn>;
        companyID: KnockoutObservable<string>;
        deviationTimeID: KnockoutObservable<string>;
        selectCode: KnockoutObservable<any>;

        constructor() {
            let self = this;
            self.deviationTimeList = ko.observableArray([]);
            self.selectCode = ko.observable([]);


            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KDL032_3"), prop: 'divergenceReasonCode', width: 40 },
                { headerText: nts.uk.resource.getText("KDL032_4"), prop: 'reason', width: 450 }

            ]);



        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var parameter = nts.uk.ui.windows.getShared("KDL032");

            var dfd = $.Deferred();

            if (parameter != null) {
                 service.getData(parameter.divergenceTimeID).done(function(deviationTimeList: Array<DeviationTime>) {
                    deviationTimeList = _.orderBy(deviationTimeList, ["divergenceReasonCode"], ["asc"]);
                   self.deviationTimeList(deviationTimeList);
                   self.deviationTimeList.unshift(new DeviationTime("", nts.uk.resource.getText("KDL032_7")));
                    self.selectCode(parameter.reasonCD);   

                });
            }
            else {
                self.selectCode(self.deviationTimeList[0]);
            }
            dfd.resolve();

            return dfd.promise();
        }

        cancel_Dialog(): any {
            DeviationTime
            nts.uk.ui.windows.close();
        }

        submit() {

            var self = this;
            var selectDeviationTime = _.find(self.deviationTimeList(), ['divergenceReasonCode', self.selectCode()]);
            if (selectDeviationTime !== undefined) {
                nts.uk.ui.windows.setShared("ReturnData", selectDeviationTime.divergenceReasonCode);
            }
            else {
                nts.uk.ui.windows.setShared("ReturnData", null, true);
            }
            self.cancel_Dialog();
            

        }

    }
    class DeviationTime {
        divergenceReasonCode: string;
        reason: string;

        constructor(divergenceReasonCode: string, reason: string) {
            this.divergenceReasonCode = divergenceReasonCode;
            this.reason = reason;

        }
    }
}