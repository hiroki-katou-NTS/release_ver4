import { Vue } from '@app/provider';
import { component } from '@app/core/component';
import { KdlS35Component } from '../s35/';

@component({
    name: 'kdltests35',
    route: '/kdl/tests35',
    style: require('./style.scss'),
    template: require('./index.vue'),
    components: {
        'kdls35': KdlS35Component
    },
    resource: require('./resources.json'),
    validations: {
        dateValue: {
            required: true,
            dateRange: true,
        }
    },
    constraints: []
})
export class KdlTests35Component extends Vue {
    public title: string = 'KdlTests35';
    public employeeId: string;
    public dateValue: { start: Date | null, end: Date | null } = { start: null, end: null };
    public daysUnit: number = 0.5;
    public targetSelectionAtr: number = 0;
    public managementData: any[] = [];
    public beforeCreate() {
        const vm = this;

        vm.$auth.user.then((user: any) => {
            vm.employeeId = user.employeeId;
        });
    }

    public created() {
        const vm = this;

    }

    public openKDLS35() {
        const vm = this;

        if (vm.dateValue.start == null || vm.dateValue.end == null ) {
            vm.$validate();

            return ;
        }

        const params: any = {
            // 社員ID
            employeeId: vm.employeeId,

            // 申請期間
            period: {
                startDate: vm.dateValue.start,
                endDate: vm.dateValue.end,
            },

            // 日数単位（1.0 / 0.5）
            daysUnit: vm.daysUnit,

            // 対象選択区分（自動 / 申請 / 手動
            targetSelectionAtr: vm.targetSelectionAtr,

            // List<表示する実績内容>
            actualContentDisplayList: [],

            // List<振出振休紐付け管理>
            managementData: vm.managementData,
        };

        vm.$modal('kdls35', params)
        .then((f: any) => {
            if (f) {
                console.log(f);
                vm.managementData = f.mngDisp;
            }
        });
    }
}