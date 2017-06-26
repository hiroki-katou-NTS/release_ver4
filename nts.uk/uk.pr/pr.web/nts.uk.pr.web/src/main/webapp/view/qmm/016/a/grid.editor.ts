(<any>$.ig).NtsNumberEditor = (<any>$.ig).EditorProvider.extend({
    /**
     * Create editor.
     */
    createEditor: function(callbacks: {textChanged: (...args:any[]) => void}, key: string, editorOptions: Object, tabIndex: Object, format: Object, element: Object): JQuery {
        var randomId = nts.uk.util.randomId();
        var editorWrapper = $('<div id="edtiorWrapper"><input id="' + randomId + '"/></div>');
        this.editor = {};
        this.editor.element = editorWrapper;
        this.editor.koObject = ko.observable(undefined);
        ko.applyBindingsToNode($('input', editorWrapper).get(0), {
            ntsNumberEditor: $.extend({
                value: this.editor.koObject,
                }, editorOptions)
        });

        // Must call text change to enable ok button on grid.
        $('input', editorWrapper).on('input',(evt) => {
            callbacks.textChanged(evt, {}, key);
        });

        this._super(callbacks, key, editorOptions, tabIndex, format, element);
        return editorWrapper;
    },

    /**
     * No need to impl?
     */
    attachErrorEvents: function (errorShowing: Object, errorShown: Object, errorHidden: Object): void {},

   /**
     * Get value function.
     * Get current object value.
     */
    getValue: function(): Object {
        return this.editor.koObject();
    },

    /**
     * Set value function.
     * Set current object value.
     */
    setValue: function(val: Object): void  {
        this.editor.koObject(val);
    },

    /**
     * Set size for control.
     */
    setSize: function(width: number, height: number): void  {
        $('input', this.editor.element).width(width - 22);
        $('input', this.editor.element).height(height - 10);
    },

    /**
     * Destroy.
     */
    destroy: function(): void {
        // Need destroy.
    },

    /**
     * Call validate function.
     */
    validate: function() : boolean{
        $('input', this.editor.element).ntsEditor('validate');
        return !$('span', this.editor.element).hasClass('error');
    },

    /**
     * Is valid method
     */
    isValid: function(): boolean {
        return !$('span', this.editor.element).hasClass('error');
    }
})
