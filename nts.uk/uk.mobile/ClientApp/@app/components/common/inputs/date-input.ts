import { _, Vue, moment } from '@app/provider';
import { input, InputComponent } from './input';

@input()
class DateComponent extends InputComponent {
    public type: string = 'string';

    public editable: boolean = false;

    public mounted() {
        this.icons.after = 'far fa-calendar-alt';

        Object.assign(window, { moment });
    }

    get date() {
        return moment(this.value || new Date()).date();
    }

    get month() {
        return moment(this.value || new Date()).month() + 1;
    }

    get year() {
        return moment(this.value || new Date()).year();
    }

    get rawValue() {
        if (_.isNil(this.value)) {
            return '';
        }

        return moment(this.value).format('YYYY-MM-DD');
    }

    private titlePicker(selecteds: { year: number; month: number; date: number; }) {
        return this.$i18n('nts_date_format', [selecteds.year.toString(), selecteds.month.toString(), selecteds.date.toString()]);
    }

    public click() {
        let self = this,
            { year, month, date } = self,
            daysInMonth = moment.utc(Date.UTC(year, month - 1)).daysInMonth();

        self.$picker({
            year,
            month,
            date
        }, {
                year: _.range(1900, 10000, 1).map((value: number) => ({ text: value.toString(), value })),
                month: _.range(1, 13, 1).map((value: number) => ({ text: _.padStart(`${value}`, 2, '0'), value })),
                date: _.range(1, daysInMonth + 1, 1).map((value) => ({ text: _.padStart(`${value}`, 2, '0'), value }))
            }, (select: ISelected, pkr: IPicker) => {
                if (_.isEmpty(pkr.selects)) {
                    return;
                }

                let daysInMonth = moment.utc(Date.UTC(select.year, select.month - 1)).daysInMonth();

                if (pkr.dataSources.date.length !== daysInMonth) {
                    pkr.dataSources.date = _.range(1, daysInMonth + 1, 1).map((value) => ({ text: _.padStart(`${value}`, 2, '0'), value }));
                }

                if (pkr.selects.date > daysInMonth) {
                    pkr.selects.date = daysInMonth;
                }

                let { year, month, date } = pkr.selects;

                pkr.title = self.titlePicker({ year, month, date });
            }, {
                title: self.titlePicker({ year, month, date }),
                required: self.constraints && self.constraints.required
            }).then((select: { year: number; month: number; date: number; }) => {
                if (select === undefined) {

                } else if (select === null) {
                    self.$emit('input', null);
                } else {
                    let $utc = Date.UTC(select.year, select.month - 1, select.date);

                    self.$emit('input', moment.utc($utc).toDate());
                }
            });

    }
}

Vue.component('nts-date-input', DateComponent);

interface ISelected {
    [key: string]: any;
}

interface IDataSource {
    [key: string]: any[];
}

interface IPicker {
    title: string;
    dataSources: IDataSource;
    selects: ISelected;
}