module nts.uk.pr.view.qmm008.h {
    export module service {

        /**
         *  Service paths
         */
        var paths: any = {
            updateHealthInsuranceAvgearn: "ctx/pr/core/insurance/social/healthavgearn/update",
            findHealthInsuranceAvgEarn: "ctx/pr/core/insurance/social/healthavgearn/find",
            recalculateHealthInsuranceAvgearn: "ctx/pr/core/insurance/social/healthavgearn/recalculate"
        };

        /**
         *  Save List Health Insurance Average Earn
         */
        export function updateHealthInsuranceAvgearn(list: model.ListHealthInsuranceAvgEarnDto, officeCode: string): JQueryPromise<any> {
            var data = {
                listHealthInsuranceAvgearnDto: list.listHealthInsuranceAvgearnDto,
                historyId: list.historyId,
                officeCode: officeCode
            };
            return nts.uk.request.ajax(paths.updateHealthInsuranceAvgearn, data);
        }

        // Re-calculate values
        export function recalculateHealthInsuranceAvgearn(historyId: string): JQueryPromise<model.ListHealthInsuranceAvgEarnDto> {
            var data = {
                historyId: historyId,
            };

            return nts.uk.request.ajax(paths.recalculateHealthInsuranceAvgearn, data);
        }

        /**
         *  Find list HealthInsuranceAvgEarn by historyId
         */
        export function findHealthInsuranceAvgEarn(historyId: string): JQueryPromise<model.ListHealthInsuranceAvgEarnDto> {
            return nts.uk.request.ajax(paths.findHealthInsuranceAvgEarn + '/' + historyId);
        }

        /**
        * Model namespace.
        */
        export module model {

            export interface HealthInsuranceAvgEarnValue {
                healthGeneralMny: number;
                healthNursingMny: number;
                healthBasicMny: number;
                healthSpecificMny: number;
            }

            export interface HealthInsuranceAvgEarnDto {
                grade: number;
                avgEarn: number;
                upperLimit: number;
                companyAvg: HealthInsuranceAvgEarnValue;
                personalAvg: HealthInsuranceAvgEarnValue;
            }

            export interface ListHealthInsuranceAvgEarnDto {
                historyId: string;
                listHealthInsuranceAvgearnDto: HealthInsuranceAvgEarnDto[];
            }

        }
    }
}