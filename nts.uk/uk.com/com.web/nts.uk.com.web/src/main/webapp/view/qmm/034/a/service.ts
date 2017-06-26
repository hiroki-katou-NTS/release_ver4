module qmm034.a.service {
    var paths = {
        getAllEras: "ctx/basic/era/finderas",
//        getEraDetail: "ctx/basic/era/find/{0}",
        deleteEra: "ctx/basic/era/deleteData",
        updateEra: "ctx/basic/era/updateData",
        addEra: "ctx/basic/era/addData",
        getFixAttribute: "ctx/basic/era/getFixAttribute/{0}",
        checkStartDate: "ctx/basic/era/checkStartDate/{0}/"
    }
    /**
     * get list era
     */
    export function getAllEras(): JQueryPromise<Array<viewmodel.EraModel>> {
        let dfd = $.Deferred<Array<viewmodel.EraModel>>();
        nts.uk.request.ajax(paths.getAllEras).done(function(res: Array<model.EraDto>) {
            var data = _.map(res, function(era) {
                return new viewmodel.EraModel(era.eraName, era.eraMark, era.startDate, era.fixAttribute, era.eraHist, era.endDate);
            });
            dfd.resolve(data);
        }).fail(function(err: any) {
            dfd.reject(err);
        })
        return dfd.promise();

    }
    /**
     * get a company 
     */
//    export function getEraDetail(eraHist: string): JQueryPromise<model.EraDto> {
//        let dfd = $.Deferred<model.EraDto>();
//        let self = this;
//        let _path = nts.uk.text.format(paths.getEraDetail, eraHist);
//        nts.uk.request.ajax(_path).done(function(res: model.EraDto) {
//            dfd.resolve(res);
//        }).fail(function(res) {
//            dfd.reject(res);
//        })
//        return dfd.promise();
//
//    }

    /**
     * get a company 
     */
    export function getFixAttribute(eraHist: string): JQueryPromise<any> {
        let dfd = $.Deferred<any>();
        let self = this;
        let _path = nts.uk.text.format(paths.getFixAttribute, eraHist);
        nts.uk.request.ajax(_path).done(function(res: number) {
            dfd.resolve(res);
        }).fail(function(err: any) {
            dfd.reject(err);
        });
        return dfd.promise();

    }

    export function addData(isUpdate: boolean, command: any): JQueryPromise<any> {
        let dfd = $.Deferred<any>();
        let path = isUpdate ? paths.updateEra : paths.addEra;
        var options = {
            dataType: 'text',
            contentType: 'text/plain'
        };
        nts.uk.request.ajax(path, command)
            .done(function(res: model.EraDto) {
                dfd.resolve(res);
            }).fail(function(err: any) {
                dfd.reject(err);
            });
        return dfd.promise();
    }
    export function deleteData(command: model.EraDtoDelete): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        //var dateObject = ko.mapping.toJS(command);
        nts.uk.request.ajax(paths.deleteEra, command)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            }).fail(function(err: any) {
                dfd.reject(err);
            });
        return dfd.promise();
    }
    export function checkStartDate(startDate: Date): JQueryPromise<any> {
        let dfd = $.Deferred<any>();
        let self = this;
        let _path = nts.uk.text.format(paths.checkStartDate, startDate);
        nts.uk.request.ajax(_path)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
        })
        return dfd.promise();
    }

    export module model {
        export class EraDto {
            eraName: string;
            eraMark: string;
            startDate: Date;
            endDate: Date;
            fixAttribute: number;
            eraHist: string;

            constructor(eraName: string, eraMark: string, startDate: Date, endDate: Date, fixAttribute: number, eraHist: string) {
                this.eraName = eraName;
                this.eraMark = eraMark;
                this.startDate = startDate;
                this.endDate = endDate;
                this.fixAttribute = fixAttribute;
                this.eraHist = eraHist;
            }
        }
    }

    export module model {
        export class EraDtoDelete {
            eraHist: string;

            constructor(eraHist: string) {
                this.eraHist = eraHist;

            }
        }
    }

}