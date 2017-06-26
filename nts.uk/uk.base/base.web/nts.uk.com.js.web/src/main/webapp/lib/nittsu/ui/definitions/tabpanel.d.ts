declare module nts.uk.ui {
    export interface NtsTabPanelModel {
        id: string;
        title: string;
        content: string;
        visible: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;
    }
}