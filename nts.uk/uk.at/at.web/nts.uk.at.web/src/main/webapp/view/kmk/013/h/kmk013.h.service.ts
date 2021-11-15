module nts.uk.at.view.kmk013.h {
    export module service {
        let paths = {
            findByCId : "at/record/calculation/findByCode",
            findUsageData: "at/record/calculation/findUsageData",
            add:"at/record/calculation/add",
            update:"at/record/calculation/update",
            loadTmpWorkMng: "at/record/workrecord/temporarywork/find",
            loadGoOutManage: "at/shared/workrecord/goout/find",
            regTmpWorkMng: "at/record/workrecord/temporarywork/save",
            regGoOutManage: "at/shared/workrecord/goout/save",
        };
        
        export function findByCompanyId(): JQueryPromise<any> {
            return nts.uk.request.ajax("at", paths.findByCId);
        }

        export function findUsageData(): JQueryPromise<any> {
          return nts.uk.request.ajax(paths.findUsageData);
        }
        
        export function save(obj: any): JQueryPromise<any> {
            const dfd = $.Deferred();
            findByCompanyId().done(data => {
                if (data)
                    nts.uk.request.ajax("at", paths.update, obj).done(() => {
                        dfd.resolve();
                    }).fail((error: any) => {
                        dfd.reject(error);
                    });
                else
                    return nts.uk.request.ajax("at", paths.add,obj).done(() => {
                        dfd.resolve();
                    }).fail((error: any) => {
                        dfd.reject(error);
                    });
            }).fail((error: any) => {
                dfd.reject(error);
            });
            return dfd.promise();
        }

      export function saveTempWorkManage(data: any) {
        return nts.uk.request.ajax(paths.regTmpWorkMng, data);
      }

      export function saveGoOutManage(data: any) {
        return nts.uk.request.ajax(paths.regGoOutManage, data);
      }
    }
}