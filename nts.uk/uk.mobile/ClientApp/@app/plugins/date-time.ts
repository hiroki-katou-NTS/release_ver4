import { _, Vue, VueConstructor, moment } from '@app/provider';
import { TimeWithDay, TimePoint, TimeDuration } from '@app/utils/time';

const datetime = {
    install(vue: VueConstructor<Vue>) {

        vue.mixin({
            beforeCreate() {
                let self = this;

                Object.assign(self, {
                    $dt(d: Date, format: string) {
                        return moment(d).format(format || 'YYYY/MM/DD');
                    }
                });

                Object.assign(self.$dt, {
                    date(d: Date, format: string) {
                        return moment(d).format(format || 'YYYY/MM/DD');
                    },
                    timewd(value: number) {
                        return TimeWithDay.toString(value);
                    },
                    timept(value: number) {
                        return TimePoint.toString(value);
                    },
                    timedr(value: number) {
                        return TimeDuration.toString(value);
                    },
                    yearmonth(d: number, format?: string) {
                        if (_.isNumber(d) && !_.isNaN(d)) {
                            let year: number = Math.floor(d / 100),
                                month: number = Math.floor(d % 100);

                            if (year && month) {
                                return moment.utc(Date.UTC(year, month)).format(format || 'YYYY/MM');
                            }
                        }

                        return d;
                    }
                });
            }
        });
    }
};

Vue.use(datetime);