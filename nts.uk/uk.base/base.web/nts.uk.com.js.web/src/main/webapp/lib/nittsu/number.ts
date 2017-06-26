module nts.uk.ntsNumber {

    function isInteger(value: any, option?: any) {
        if (option !== undefined && option.groupseperator() !== undefined) {
            value = isInteger(value) ? value : text.replaceAll(value.toString(), option.groupseperator(), '');
        }
        return !isNaN(value) && parseInt(value) == value && !isNaN(parseInt(value, 10));
    }

    function isDecimal(value: any, option?: any) {
        if (option !== undefined) {
            var seperator = typeof option.groupseperator === 'function' ? option.groupseperator() : option.groupseperator;
            value = isDecimal(value) || seperator === undefined ? value : text.replaceAll(value.toString(), seperator, '');
        }
        return !isNaN(value) && parseFloat(value) == value && !isNaN(parseFloat(value));
    } 

    export function isNumber(value: any, isDecimalValue?: boolean, option?: any, message?: any) {
        if (isDecimalValue) {
            if (message !== undefined) message.id = 'FND_E_REALNUMBER';
            return isDecimal(value, option);
        } else {
            if (message !== undefined) message.id = 'FND_E_INTEGER';
            return isInteger(value, option);
        }
    }
    
    export function isHalfInt(value: any, message?: any) {
        var val = parseFloat(value);
        if (message !== undefined) message.id = 'FND_E_HALFINT';
        if (val !== NaN && (val * 2) % 1 === 0) return true;
        return false;
    }

    /*similar with Math.trunc, get integer value from decimal*/
    export var trunc: (value: number) => number;
    trunc = (typeof (<any>Math).trunc === 'function') ? (<any>Math).trunc : value => value > 0 ? Math.floor(value) : Math.ceil(value);
    
    export function getDecimal(value: any, scale: number){
        var scaleX = Math.pow(10, scale);
        return trunc(value*scaleX)/scaleX;
    }

    export function formatNumber(value: any, formatOption: any) {
        if (value === undefined || value === null || value.toString().trim().lenth <= 0) {
            return value;
        }
        var groupSeperator = formatOption.groupseperator ? formatOption.groupseperator : ',';
        var groupLength = formatOption.grouplength ? formatOption.grouplength : 0;
        var decimalSeperator = formatOption.decimalseperator ? formatOption.decimalseperator : ".";
        var decimalLength = formatOption.decimallength ? formatOption.decimallength : 0;
        var formattedValue = "";
        var stringValue = text.replaceAll(value.toString(), groupSeperator, '');
        var isMinus = stringValue.charAt(0) === '-';
        var values = isMinus ? stringValue.split('-')[1].split(decimalSeperator) : stringValue.split(decimalSeperator);
        if (groupLength > 0) {
            var x = values[0].split('').reverse().join('');
            for (var i = 0; i < x.length;) {
                formattedValue += x.substr(i, groupLength) + (x.length > i + groupLength ? groupSeperator : "");
                i += groupLength;
            }
            formattedValue = formattedValue.split('').reverse().join('');
        } else {
            formattedValue = values[0];
        }
        if (values[1] === undefined || decimalLength > values[1].length) {
            values[1] = text.padRight(values[1] ? values[1] : "", '0', values[1] ? decimalLength : decimalLength + 1);
        } else {
            values[1] = values[1].substr(0, decimalLength);
        }
        values[1] = text.splitOrPadRight(values[1], decimalLength, '0');

        return (isMinus ? '-' : '') + formattedValue + (decimalLength <= 0 ? '' : decimalSeperator + values[1]);
    }
}