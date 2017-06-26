declare var __viewContext: ViewContext;
declare var names: Names;
declare var messages: Messages;
declare var systemLanguage: string;

interface ViewContext {
    rootPath: string;
    primitiveValueConstraints: { [key: string]: any };
    codeNames: { [key: string]: string };
    messages: { [key: string]: string };
    systemLanguage: string;

    title: string;
    transferred: nts.uk.util.optional.Optional<any>;

    ready: (callback: () => void) => void;
    bind: (viewModel: any) => void;
}
interface Names {
}
interface Messages { }