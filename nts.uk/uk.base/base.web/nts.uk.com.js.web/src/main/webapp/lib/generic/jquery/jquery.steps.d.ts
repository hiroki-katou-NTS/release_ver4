interface JQueryStepsLabelsOptions {
    next?: string;
    previous?: string;
    finish?: string;
    cancel?: string;
}

interface JQueryStepsOptions {
    headerTag?: string;
    bodyTag?: string;
    transitionEffect?: string;
    stepsOrientation?: string;
    titleTemplate?: string;
    enablePagination?: boolean;
    enableFinishButton?: boolean;
    autoFocus?: boolean;
    labels?: JQueryStepsLabelsOptions;

    onInit?: (event: any, currentIndex: number) => void;
    onStepChanging?: (event: any, currentIndex: number, newIndex: number) => void;
    onStepChanged?: (event: any, currentIndex: number, priorIndex: number) => boolean;
    onCanceled?: (event: any) => void;
    onFinishing?: (event: any, currentIndex: number) => boolean;
    onFinished?: (event: any, currentIndex: number) => void;
}

interface JQuerySteps {
    (options: JQueryStepsOptions): JQuery;
    (methodName: string, ...params: any[]): JQuery;
}

interface JQuery {
    steps(options: JQueryStepsOptions);
    setStep(step: number);
}
