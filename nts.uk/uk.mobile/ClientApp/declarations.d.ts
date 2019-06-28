import Vue, { ComponentOptions } from "vue";
import { ErrorHandler } from "vue-router/types/router";

declare interface Window {
    Reflect: any;
}

interface IFetchOption {
    url: string;
    type?: 'url' | 'form' | 'json';
    method: 'get' | 'post' | 'push' | 'patch' | 'delete';
    data?: any;
    headers?: any;
}

declare interface IRule {
    loop?: boolean;
    required?: boolean;
    min?: number | Date;
    max?: number | Date;
    mantissaMaxLength?: number;
    minLength?: number;
    maxLength?: number;
    timeRange?: boolean;
    dateRange?: boolean;
    constraint?: string;
    charType?: 'Any' | 'Kana' | 'AnyHalfWidth' | 'AlphaNumeric' | 'Numeric';
    valueType?: 'String' | 'Decimal' | 'Integer' | 'HalfInt' | 'Date' | 'Time' | 'Clock' | 'Duration' | 'TimePoint' | 'TimeRange' | 'DateRange';
    [rule: string]: Array<Date | number | string> | Date | number | boolean | IRule | string | Function | {
        test: RegExp | Function;
        message: string;
    } | {
        test: RegExp | Function;
        messageId: string;
    };
}

declare interface IModalOptions {
    type?: 'modal' | 'popup' | 'info' | 'dropback';
    size?: 'lg' | 'md' | 'sm' | 'xs';
    title?: string;
    style?: string;
    animate?: 'up' | 'right' | 'down' | 'left';
    opacity?: number;
}

declare interface IValidations {
    [name: string]: IValidations | IRule
}

declare module "vue/types/options" {
    interface ComponentOptions<V extends Vue> {
        route?: string | {
            url: string;
            parent?: string;
        };
        style?: string;
        resource?: {
            [lang: string]: {
                [resources: string]: string;
            }
        };
        validations?: {
            [name: string]: IRule | IValidations;
        },
        markdown?: string | {
            [lang: string]: string;
        };
        constraints?: Array<string>;
        enums?: Array<string>;
    }
}

declare module "vue/types/vue" {
    interface Vue {
        pgName: string;
        readonly $valid: boolean;
        $http: {
            get(url: string): Promise<{}>;
            get(pg: 'at' | 'pr' | 'hr' | 'com', url: string): Promise<{}>;
            post(url: string, data?: any): Promise<{}>;
            post(pg: 'at' | 'pr' | 'hr' | 'com', url: string, data?: any): Promise<{}>;
            file: {
                live: (fileId: string) => string;
                upload: (form: FormData) => Promise<{}>;
                download: (fileId: string) => Promise<{}>;
            },
            async: {
                info: (taskId: string) => Promise<{}>;
                cancel: (taskId: string) => Promise<{}>;
            },
            enum(enumNames?: Array<String>): Promise<{}>;
            readonly resources: Promise<{}>;
        };
        $auth: {
            login: (data: any) => Promise<{}>;
            logout: () => Promise<{}>;
            readonly user: Promise<null | {
                employee: boolean;
                companyId: string;
                employeeId: string;
                companyCode: string;
                employeeCode: string;
                constractCode: string;
                role: {
                    payroll: string;
                    personnel: string;
                    attendance: string;
                    systemAdmin: string;
                    companyAdmin: string;
                    personalInfo: string;
                    officeHelper: string;
                    groupCompanyAdmin: string;
                }
            }>;
            readonly token: Promise<string>;
            readonly contract: Promise<null | { code: string; password: string }>;
        };
        $i18n: {
            (resr: string): string;
            (resr: string, param: string): string;
            (resr: string, param: string[]): string;
            (resr: string, param: { [key: string]: string }): string;
        };
        $close: {
            (): void;
            (data: any): void;
        };
        $mask: {
            (act: 'hide'): void;
            (act: 'show', opacity?: number): {
                on: (click: () => void, hide?: () => void) => void;
            };
            (act: 'show', options: { opacity?: number; message?: boolean | string; }): {
                on: (click: () => void, hide?: () => void) => void;
            };
        };
        $goto: {
            (name: string): void;
            (name: string, params: { [key: string]: any; }): void;
            (name: string, params: { [key: string]: any; }, onComplete: Function): void;
            (name: string, params: { [key: string]: any; }, onComplete: Function, onAbort: ErrorHandler): void;
            (location: { name: string, params?: { [key: string]: any; } }, onComplete?: Function, onAbort?: ErrorHandler): void;
            home(): void;
            home(params: any): void;
            login(): void;
            login(params: any): void;
            password: {
                change(): void;
                change(params: any): void;
                forget(): void;
                forget(params: any): void;
                reset(): void;
                reset(params: any): void;
                mail(): void;
                mail(params: any): void;
            }
        };
        $modal: {
            (name: string): Promise<{}>;
            (name: string, params: any): Promise<{}>;
            (name: string, params: any, options: IModalOptions): Promise<{}>;
            (component: VueConstructor<Vue>): Promise<{}>;
            (component: VueConstructor<Vue>, params: any): Promise<{}>;
            (component: VueConstructor<Vue>, params: any, options: IModalOptions): Promise<{}>;
            (component: ComponentOptions<Vue>): Promise<{}>;
            (component: ComponentOptions<Vue>, params: any): Promise<{}>;
            (component: ComponentOptions<Vue>, params: any, options: IModalOptions): Promise<{}>;
            warn: {
                (msg: string): Promise<{}>;
                (resource: { messageId: string, messageParams?: string[] | { [key: string]: string } }): Promise<{}>;
            };
            info: {
                (msg: string): Promise<{}>;
                (resource: { messageId: string, messageParams?: string[] | { [key: string]: string } }): Promise<{}>;
            };
            error: {
                (msg: string): Promise<{}>;
                (resource: { messageId: string, messageParams?: string[] | { [key: string]: string } }): Promise<{}>;
            };
            confirm: {
                (msg: string, style?: 'normal' | 'process' | 'danger'): Promise<'yes' | 'no'>;
                (resource: { messageId: string, messageParams?: string[] | { [key: string]: string } }, style?: 'normal' | 'process' | 'danger'): Promise<'yes' | 'no'>;
            }
        };
        $picker: {
            (value: { [key: string]: any },
                dataSources: { [key: string]: any[] },
                options?: {
                    text?: string;
                    value?: string;
                    title?: string;
                    required?: boolean;
                    className?: 'clock' | 'time' | 'time-day';
                    onSelect?: (selects: { [key: string]: any }, pkr: { title: string; dataSources: { [key: string]: any[] } }) => void;
                }): Promise<{}>;
            (value: { [key: string]: any },
                dataSources: { [key: string]: any[] },
                onSelect: (selects: { [key: string]: any }, pkr: { title: string; dataSources: { [key: string]: any[] } }) => void,
                options?: {
                    text?: string;
                    value?: string;
                    title?: string;
                    required?: boolean;
                    className?: 'clock' | 'time' | 'time-day';
                }): Promise<{}>;
        };
        $errors: {
            [name: string]: {
                [rule: string]: string;
            }
        };
        $validate: {
            (): void;
            (act: 'clear'): void;
            (name: string): boolean;
        };
        $updateValidator: {
            (rule: IRule): void;
            (name: string, rule: IRule): void;
        };
        validations: {
            [name: string]: IRule;
        };
        toJS: (value: any) => any;
    }

    export interface VueConstructor<V extends Vue = Vue> {
        util: {
            defineReactive: (obj: any, key: string, val: any) => void;
            extend: (to: any, from: any) => any;
        };
        toJS: (value: any) => any;
        vmOf: (el: HTMLElement) => { [key: string]: any; };
    }
}

declare module "vue-router/types/router" {
    export interface VueRouter {
        goto: {
            (location: { name: string; params: { [key: string]: any } }): void;
            (location: { name: string; params: { [key: string]: any } }, onComplete: Function): void;
            (location: { name: string; params: { [key: string]: any } }, onComplete: Function, onAbort: ErrorHandler): void;
            home(): void;
            home(params: any): void;
            login(): void;
            login(params: any): void;
            password: {
                change(): void;
                change(params: any): void;
                forget(): void;
                forget(params: any): void;
                reset(): void;
                reset(params: any): void;
                mail(): void;
                mail(params: any): void;
            }
        }
    }
}

export declare type VueClass<V> = {
    new(...args: any[]): V & Vue;
} & typeof Vue;