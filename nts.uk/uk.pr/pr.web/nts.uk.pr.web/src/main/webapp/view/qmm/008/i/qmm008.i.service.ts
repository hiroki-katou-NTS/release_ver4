module nts.uk.pr.view.qmm008.i {
    export module service {

        /**
         *  Service paths
         */
        var paths: any = {
            updatePensionAvgearn: "ctx/pr/core/insurance/social/pensionavgearn/update",
            findPensionAvgearn: "ctx/pr/core/insurance/social/pensionavgearn/find",
            recalculatePensionAvgearn: "ctx/pr/core/insurance/social/pensionavgearn/recalculate"
        };

        /**
         *  Save list pensionAvgearn
         */
        export function updatePensionAvgearn(list: model.ListPensionAvgearnDto, officeCode: string): JQueryPromise<any> {
            var data = {
                listPensionAvgearnDto: list.listPensionAvgearnDto,
                historyId: list.historyId,
                officeCode: officeCode
            };
            return nts.uk.request.ajax(paths.updatePensionAvgearn, data);
        }

        export function recalculatePensionAvgearn(historyId: string): JQueryPromise<model.ListPensionAvgearnDto> {
            var data = {
                historyId: historyId,
            };

            return nts.uk.request.ajax(paths.recalculatePensionAvgearn, data);
        }

        /**
        *  Find list PensionAvgearn by historyId
        */
        export function findPensionAvgearn(id: string): JQueryPromise<model.ListPensionAvgearnDto> {
            return nts.uk.request.ajax(paths.findPensionAvgearn + '/' + id);
        }

        /**
        * Model namespace.
        */
        export module model {

            export interface PensionAvgearnDto {
                grade: number;
                avgEarn: number;
                upperLimit: number;
                companyFund: PensionAvgearnValue;
                companyFundExemption: PensionAvgearnValue;
                companyPension: PensionAvgearnValue;
                personalFund: PensionAvgearnValue;
                personalFundExemption: PensionAvgearnValue;
                personalPension: PensionAvgearnValue;
                childContributionAmount: number;
            }

            export interface ListPensionAvgearnDto {
                listPensionAvgearnDto: Array<PensionAvgearnDto>;
                historyId: string;
            }

            export interface PensionAvgearnValue {
                maleAmount: number;
                femaleAmount: number;
                unknownAmount: number;
            }
        }
    }

}
