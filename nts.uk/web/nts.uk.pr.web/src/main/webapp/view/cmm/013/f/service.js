var cmm013;
(function (cmm013) {
    var f;
    (function (f) {
        var service;
        (function (service) {
            var paths = {
                findAllPosition: "basic/position/findallposition/",
                addPosition: "basic/position/addPosition",
                deletePosition: "basic/position/deletePosition",
                updatePosition: "basic/position/updatePosition",
                getAllHistory: "basic/position/getallhist"
            };
            function findAllPosition(historyId) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("com", paths.findAllPosition + historyId)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.findAllPosition = findAllPosition;
            function addPosition(position) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("com", paths.addPosition, position)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.addPosition = addPosition;
            function updatePosition(position) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("com", paths.updatePosition, position).done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.updatePosition = updatePosition;
            function deletePosition(position) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("com", paths.deletePosition, position).done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.deletePosition = deletePosition;
            function getAllHistory() {
                var dfd = $.Deferred();
                nts.uk.request.ajax("com", paths.getAllHistory)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getAllHistory = getAllHistory;
        })(service = f.service || (f.service = {}));
    })(f = cmm013.f || (cmm013.f = {}));
})(cmm013 || (cmm013 = {}));
