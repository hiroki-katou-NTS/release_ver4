import { _ } from '@app/provider';
import { IRule } from 'declarations';

const required = function (value: any, checkOr: boolean, rule: IRule) {
    let msg = false;

    if (Array.isArray(value)) {
        msg = value.length === 0;
    } else if (value === undefined || value === null) {
        msg = true;
    } else if (value === false) {
        msg = false;
    } else if (value instanceof Date) {
        // invalid date won't pass
        msg = isNaN(value.getTime());
    } else if (typeof value === 'object') {
        switch (rule.valueType) {
            case 'TimeRange':
                msg = _.isNil(value.start) || _.isNil(value.end);
                break;
            default:
                msg = Object.keys(value).length === 0;
                break;
        }
    } else {
        msg = String(value).length === 0;
    }

    return !msg ? null : 'MsgB_1';
};

export { required };