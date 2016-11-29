var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ntsNumber;
        (function (ntsNumber) {
            function isInteger(value, option) {
                if (option !== undefined && option.groupseperator() !== undefined) {
                    value = isInteger(value) ? value : uk.text.replaceAll(value.toString(), option.groupseperator(), '');
                }
                return !isNaN(value) && parseInt(value) == value && !isNaN(parseInt(value, 10));
            }
            function isDecimal(value, option) {
                if (option !== undefined && option.groupseperator() !== undefined) {
                    value = isDecimal(value) ? value : uk.text.replaceAll(value.toString(), option.groupseperator(), '');
                }
                return !isNaN(value) && parseFloat(value) == value && !isNaN(parseFloat(value));
            }
            function isNumber(value, isDecimalValue, option) {
                if (isDecimalValue) {
                    return isDecimal(value, option);
                }
                else {
                    return isInteger(value, option);
                }
            }
            ntsNumber.isNumber = isNumber;
            ntsNumber.trunc = (typeof Math.trunc === 'function') ? Math.trunc : function (value) { return value > 0 ? Math.floor(value) : Math.ceil(value); };
            function getDecimal(value, scale) {
                var scaleX = Math.pow(10, scale);
                return ntsNumber.trunc(value * scaleX) / scaleX;
            }
            ntsNumber.getDecimal = getDecimal;
            function formatNumber(value, formatOption) {
                if (value === undefined || value === null || value.toString().trim().lenth <= 0) {
                    return value;
                }
                var groupseperator = formatOption.groupseperator() ? formatOption.groupseperator() : ',';
                var grouplength = formatOption.grouplength() ? formatOption.grouplength() : 0;
                var decimalseperator = formatOption.decimalseperator() ? formatOption.decimalseperator() : ".";
                var decimallength = formatOption.decimallength() ? formatOption.decimallength() : 0;
                var formattedValue = "";
                var stringValue = uk.text.replaceAll(value.toString(), groupseperator, '');
                var isMinus = stringValue.charAt(0) === '-';
                var values = isMinus ? stringValue.split('-')[1].split(decimalseperator) : stringValue.split(decimalseperator);
                if (grouplength > 0) {
                    var x = values[0].split('').reverse().join('');
                    for (var i = 0; i < x.length;) {
                        formattedValue += x.substr(i, grouplength) + (x.length > i + grouplength ? groupseperator : "");
                        i += grouplength;
                    }
                    formattedValue = formattedValue.split('').reverse().join('');
                }
                else {
                    formattedValue = values[0];
                }
                if (values[1] === undefined || decimallength > values[1].length) {
                    values[1] = uk.text.padRight(values[1] ? values[1] : "", '0', values[1] ? decimallength : decimallength + 1);
                }
                else {
                    values[1] = values[1].substr(0, decimallength);
                }
                return (isMinus ? '-' : '') + formattedValue + (decimallength <= 0 ? '' : decimalseperator + values[1]);
            }
            ntsNumber.formatNumber = formatNumber;
        })(ntsNumber = uk.ntsNumber || (uk.ntsNumber = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
