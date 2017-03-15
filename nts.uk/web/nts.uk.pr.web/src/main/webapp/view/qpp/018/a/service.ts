module nts.uk.pr.view.qpp018.a {
    export module service {

        // Service paths.
        var servicePath = {
            findAllInsuranceOffice: "screen/pr/QPP018/findAllOffice",
            saveAsPdf: "screen/pr/QPP018/saveAsPdf"
        };
        
        /**
         * find All Insurance Office
         */
        export function findAllInsuranceOffice(): JQueryPromise<model.InsuranceOffice[]> {
            var dfd = $.Deferred();
            nts.uk.request.ajax(servicePath.findAllInsuranceOffice).done(function (res: any) {
                var list = _.map(res, function (item: any) {
                    return new model.InsuranceOffice(item.officeCode, item.officeName);
                });
                dfd.resolve(list);
            });
            return dfd.promise();
        }
        
        export function saveAsPdf(command: any): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.saveAsPdf, command);
        }
        
        /**
         * insurance office
         */
        export module model {
            
            export class InsuranceOffice {
                
                code: string;
                name: string;
                
                constructor(code: string, name: string) {
                    let self = this;
                    self.code = code;
                    self.name = name;
                }
            }
        }
    }
}
