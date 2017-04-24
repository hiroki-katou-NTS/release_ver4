declare var __viewContext: ViewContext;

interface ViewContext {
    rootPath: string;
    primitiveValueConstraints: { [key: string]: any };
    codeNames: { [key: string]: string };
    messages: { [key: string]: string };
    
    title: string;
    transferred: nts.uk.util.optional.Optional<any>;
    
    ready: (callback: () => void) => void;
    bind: (viewModel: any) => void;
}