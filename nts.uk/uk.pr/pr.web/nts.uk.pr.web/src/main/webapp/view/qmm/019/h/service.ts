module qmm019.h.service {
    var paths = {
        getAll: "pr/proto/personalwage/findalls/{0}",

    }

    export function getPersonalWageNames(categoryAtr : number): JQueryPromise<Array<model.PersonalWageNameDto>> {
        var dfd = $.Deferred<Array<any>>();
        var _path = nts.uk.text.format(paths.getAll, categoryAtr);
        nts.uk.request.ajax(_path)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export module model {
        export interface LayoutMasterModel {
            personalWageName: String;
            companyCode: String;
        }
    }

    export module model {
        // layout
        export class PersonalWageNameDto {
            companyCode: string;
            categoryAtr: number;
            personalWageCode: string;
            personalWageName: string;
        }

    }
}