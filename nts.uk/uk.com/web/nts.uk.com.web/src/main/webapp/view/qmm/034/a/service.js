var qmm034;
(function (qmm034) {
    var a;
    (function (a) {
        var service;
        (function (service) {
            var paths = {
                getAllEras: "ctx/basic/era/finderas",
                //        getEraDetail: "ctx/basic/era/find/{0}",
                deleteEra: "ctx/basic/era/deleteData",
                updateEra: "ctx/basic/era/updateData",
                addEra: "ctx/basic/era/addData",
                getFixAttribute: "ctx/basic/era/getFixAttribute/{0}",
                checkStartDate: "ctx/basic/era/checkStartDate/{0}/"
            };
            /**
             * get list era
             */
            function getAllEras() {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.getAllEras).done(function (res) {
                    var data = _.map(res, function (era) {
                        return new a.viewmodel.EraModel(era.eraName, era.eraMark, era.startDate, era.fixAttribute, era.eraHist, era.endDate);
                    });
                    dfd.resolve(data);
                }).fail(function (err) {
                    dfd.reject(err);
                });
                return dfd.promise();
            }
            service.getAllEras = getAllEras;
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
            function getFixAttribute(eraHist) {
                var dfd = $.Deferred();
                var self = this;
                var _path = nts.uk.text.format(paths.getFixAttribute, eraHist);
                nts.uk.request.ajax(_path).done(function (res) {
                    dfd.resolve(res);
                }).fail(function (err) {
                    dfd.reject(err);
                });
                return dfd.promise();
            }
            service.getFixAttribute = getFixAttribute;
            function addData(isUpdate, command) {
                var dfd = $.Deferred();
                var path = isUpdate ? paths.updateEra : paths.addEra;
                var options = {
                    dataType: 'text',
                    contentType: 'text/plain'
                };
                nts.uk.request.ajax(path, command)
                    .done(function (res) {
                    dfd.resolve(res);
                }).fail(function (err) {
                    dfd.reject(err);
                });
                return dfd.promise();
            }
            service.addData = addData;
            function deleteData(command) {
                var dfd = $.Deferred();
                //var dateObject = ko.mapping.toJS(command);
                nts.uk.request.ajax(paths.deleteEra, command)
                    .done(function (res) {
                    dfd.resolve(res);
                }).fail(function (err) {
                    dfd.reject(err);
                });
                return dfd.promise();
            }
            service.deleteData = deleteData;
            function checkStartDate(startDate) {
                var dfd = $.Deferred();
                var self = this;
                var _path = nts.uk.text.format(paths.checkStartDate, startDate);
                nts.uk.request.ajax(_path)
                    .done(function (res) {
                    dfd.resolve(res);
                });
                return dfd.promise();
            }
            service.checkStartDate = checkStartDate;
            var model;
            (function (model) {
                var EraDto = (function () {
                    function EraDto(eraName, eraMark, startDate, endDate, fixAttribute, eraHist) {
                        this.eraName = eraName;
                        this.eraMark = eraMark;
                        this.startDate = startDate;
                        this.endDate = endDate;
                        this.fixAttribute = fixAttribute;
                        this.eraHist = eraHist;
                    }
                    return EraDto;
                }());
                model.EraDto = EraDto;
            })(model = service.model || (service.model = {}));
            var model;
            (function (model) {
                var EraDtoDelete = (function () {
                    function EraDtoDelete(eraHist) {
                        this.eraHist = eraHist;
                    }
                    return EraDtoDelete;
                }());
                model.EraDtoDelete = EraDtoDelete;
            })(model = service.model || (service.model = {}));
        })(service = a.service || (a.service = {}));
    })(a = qmm034.a || (qmm034.a = {}));
})(qmm034 || (qmm034 = {}));
//# sourceMappingURL=service.js.map