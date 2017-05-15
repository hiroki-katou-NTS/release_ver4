var qpp014;
(function (qpp014) {
    var g;
    (function (g) {
        var service;
        (function (service) {
            var paths = {
                findAllLineBank: "basic/system/bank/linebank/findAll",
                findAllBankBranch: "basic/system/bank/find/all",
            };
            function findAllLineBank() {
                return nts.uk.request.ajax("com", paths.findAllLineBank);
            }
            service.findAllLineBank = findAllLineBank;
            function findAllBankBranch() {
                return nts.uk.request.ajax("com", paths.findAllBankBranch);
            }
            service.findAllBankBranch = findAllBankBranch;
        })(service = g.service || (g.service = {}));
    })(g = qpp014.g || (qpp014.g = {}));
})(qpp014 || (qpp014 = {}));
//# sourceMappingURL=qpp014.g.service.js.map