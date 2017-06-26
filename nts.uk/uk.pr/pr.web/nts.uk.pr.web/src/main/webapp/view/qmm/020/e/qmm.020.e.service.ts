module qmm020.e.service {
    var paths = {
        getAllClassificationAllotSetting: "pr/core/allot/getallclassificationallotsetting",
        getAllClassificationAllotSettingHeader: "pr/core/allot/getallclassificationallotsettingheader",
        updateClassificationAllotSettingHeader: "pr/core/allot/updateclassificationallotsettingheader",
    }

    /**
     * Get list history
     */
    export function getAllClassificationAllotSettingHeader(): JQueryPromise<Array<viewmodel.ClassificationAllotSettingHeaderDto>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getAllClassificationAllotSettingHeader, {companyCode: "0001"})
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    
    /**
     * Get list history
     */
    export function updateClassificationAllotSettingHeader(data: viewmodel.ClassificationAllotSettingHeaderDto): JQueryPromise<any> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.updateClassificationAllotSettingHeader, data)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
   
    }
    
    
    /**
     * Get gridlist 
     getAllClassificationAllotSetting
    */
    export function getAllClassificationAllotSetting(historyId: string): JQueryPromise<Array<viewmodel.ClassificationAllotSettingDto>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getAllClassificationAllotSetting, {historyId: historyId})
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    
    export class ClassificationAllotSettingHeader{
        companyCode : string;
        historyId : string;
        startDateYM : number;
        endDateYM : number;
        
          constructor(companyCode: string, historyId: string, startDateYM: number, endDateYM: number) {
            var self = this;
            this.companyCode = companyCode;
            this.historyId = historyId;
            this.startDateYM = startDateYM;
            this.endDateYM = endDateYM;
             }
    }
}