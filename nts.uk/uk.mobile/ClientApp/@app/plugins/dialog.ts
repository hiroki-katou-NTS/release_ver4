import { obj } from "@app/utils";
import { IModalOptions } from 'declarations';
import { Vue, VueConstructor, ComponentOptions } from '@app/provider';

const $dialog = () => ({
    props: ['params'],
    template: `<div>
        <div class="text-justify">{{ $i18n(params.message || params.messageId, params.messageParams) }}</div>
        <div class="text-right mt-2" v-if="params.messageId">{{ params.messageId }}</div>
        <div data-msg="No messageId" v-else></div>
        <div class="modal-footer text-right">
            <template v-if="['info', 'error', 'warn'].indexOf(params.type) > -1">
                <button class="btn btn-link" v-on:click="$close('close')">{{'close' | i18n}}</button>
                <!--<button class="btn btn-link" v-on:click="$close('cancel')">{{'cancel' | i18n}}</button>-->
            </template>
            <template v-else>
                <button class="btn" v-bind:class="{
                    'btn-link': normal,
                    'btn-danger': danger,
                    'btn-primary': primary
                }" v-on:click="$close('yes')">{{'yes' | i18n}}</button>
                <button class="btn" v-bind:class="{
                    'btn-link': normal,
                    'btn-secondary': !normal
                }" v-on:click="$close('no')">{{'no' | i18n}}</button>
            </template>
        </div>
     </div>`,
    computed: {
        normal() {
            return this.params.style === 'normal';
        },
        danger() {
            return this.params.style === 'danger';
        },
        primary() {
            return this.params.style === 'process';
        }
    }
}) as ComponentOptions<Vue>,
    dialog = {
        install(vue: VueConstructor<Vue>) {
            vue.mixin({
                beforeCreate() {
                    let self = this,
                        $dlg: { [key: string]: any } = {};

                    ['warn', 'info', 'error', 'confirm']
                        .forEach($type => {
                            $dlg[$type] = function (msg: string | { messageId: string, messageParams: string[] | { [key: string]: string } }, style: 'normal' | 'process' | 'danger' = 'normal') { //'normal' | 'process' | 'danger'
                                let params: { [key: string]: any } = {},
                                    option: IModalOptions = {
                                        title: $type,
                                        type: 'popup',
                                        animate: {
                                            hide: 'fadeOut',
                                            show: 'fadeIn'
                                        }
                                    };

                                if (typeof msg !== 'string') {
                                    params = msg;
                                } else {
                                    params.message = msg;
                                }

                                obj.extend(params, { type: $type, style: style });

                                return (<Vue>this).$modal($dialog(), params, option);
                            }.bind(self);
                        });

                    obj.extend(self.$modal, $dlg);
                }
            })
        }
    };

export { dialog, $dialog };