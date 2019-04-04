import { obj } from '@app/utils';
import { Vue } from '@app/provider';
import { IRule } from 'declarations';
import { component, Prop, Watch } from '@app/core/component';
import { DatePickerComponent, TimeWDPickerComponent, TimePointPickerComponent, TimeDurationPickerComponent } from '@app/components';

export const input = (tagName: 'input' | 'textarea' | 'select' = 'input') => component({
    template: `<div class="form-group row">
        <template v-if="showTitle && showTitle !== 'false'">
            <div v-bind:class="columns.title">
                <nts-label v-bind:constraint="constraints" v-bind:class="{ 'control-label-inline': inlineTitle }">{{ name | i18n }}</nts-label>
            </div>
        </template>
        <div v-bind:class="columns.input">
            <div class="input-group input-group-transparent">                
                <template v-if="icons.before">
                    <div class="input-group-prepend">
                        <span class="input-group-text" v-bind:class="iconsClass.before">{{ !iconsClass.before ? icons.before : '' }}</span>
                    </div>
                </template>
                <template v-if="icons.after">
                    <div class="input-group-append">
                        <span class="input-group-text" v-bind:class="iconsClass.after">{{ !iconsClass.after ? icons.after : ''}}</span>
                    </div>
                </template>                
                ${
        tagName === 'select' ?
            `<select class="form-control" 
                            ref="input"
                            v-validate="{
                                always: !!errorsAlways,
                                errors: ($errors || errorsAlways || {})
                            }"
                            v-bind:disabled="disabled"
                            v-bind:value="rawValue"
                            v-on:change="input()">
                        <slot />
                    </select>`
            :
            `<${tagName} class="form-control"
                        ref="input"
                        v-bind:type="type"
                        v-validate="{
                            always: !!errorsAlways,
                            errors: ($errors || errorsAlways || {})
                        }"
                        v-bind:rows="rows"
                        v-bind:disabled="disabled"
                        v-bind:readonly="!editable"
                        v-bind:value="rawValue"
                        v-on:click="click()"
                        v-on:keydown.13="click()"
                        v-on:input="input()"
                    />`
        }
                <v-errors v-for="(error, k) in ($errors || errorsAlways || {})" v-bind:key="k" v-bind:data="error" v-bind:name="name" />
            </div>
        </div>
    </div>`,
    components: {
        'datepicker': DatePickerComponent,
        'time-point-picker': TimePointPickerComponent,
        'time-duration-picker': TimeDurationPickerComponent,
        'time-with-day-picker': TimeWDPickerComponent
    }
});

export class InputComponent extends Vue {
    click() { }

    type: string = '';
    rows: number | null = null;

    editable: boolean = true;

    @Prop({ default: () => '' })
    readonly name: string;

    @Prop({ default: () => '' })
    readonly value: any;

    @Prop({ default: () => false })
    readonly disabled?: boolean;

    @Prop({ default: () => null })
    readonly errors!: any;

    @Prop({ default: () => null })
    readonly errorsAlways!: any;

    @Prop({ default: () => ({}) })
    readonly constraint!: IRule;

    @Prop({ default: () => true })
    readonly showTitle!: 'true' | 'false' | boolean;

    @Prop({ default: () => false })
    readonly inlineTitle!: boolean;

    @Prop({ default: () => ({ before: '', after: '' }) })
    readonly icons!: { before: string; after: string }

    @Prop({ default: () => ({ title: 'col-md-12', input: 'col-md-12' }) })
    readonly columns!: { title: string; input: string };

    get iconsClass() {
        let self = this,
            classess = ['fa', 'fas', 'fab'],
            isClass = (icon: string) => {
                return !!classess.filter(f => icon.indexOf(f) > -1).length;
            };

        return {
            before: isClass(self.icons.before) ? self.icons.before : '',
            after: isClass(self.icons.after) ? self.icons.after : ''
        };
    }

    readonly $errors: any = {};
    readonly constraints: any = {};

    @Watch('errors', { deep: true })
    wSErrs(newErrs: any) {
        let self = this;

        Vue.set(self, '$errors', newErrs);
    }

    @Watch('errorsAlways', { deep: true })
    wSErrAs(newErrs: any) {
        let self = this;

        if (!obj.isBoolean(newErrs)) {
            Vue.set(self, '$errors', newErrs);
        }
    }

    @Watch('$parent.$errors', { deep: true })
    wErrs(newErrs: any) {
        let self = this,
            exprs = (((<any>self.$vnode.data) || { model: { expression: '' } }).model || { expression: '' }).expression,
            props = (self.$vnode.componentOptions.propsData);

        if (obj.has(self.$parent, exprs) && !obj.has(props, 'errors') &&
            (obj.isBoolean(self.errorsAlways) || self.errorsAlways == undefined)) {
            Vue.set(self, '$errors', obj.get(newErrs, exprs));
        }
    }

    @Watch('constraint', { immediate: true, deep: true })
    wSConsts(newValidts: any) {
        let self = this;

        Vue.set(self, 'constraints', newValidts);
    }

    @Watch('$parent.validations', { immediate: true, deep: true })
    wConsts(newValidts: any) {
        let self = this,
            exprs = (((<any>self.$vnode.data) || { model: { expression: '' } }).model || { expression: '' }).expression,
            props = (self.$vnode.componentOptions.propsData);

        if (obj.has(self.$parent, exprs) && !obj.has(props, 'constraint')) {
            Vue.set(self, 'constraints', obj.get(newValidts, exprs));
        }
    }
}