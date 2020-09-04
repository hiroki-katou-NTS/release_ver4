module nts.uk.devices {
    const WS_URI = "ws://127.0.0.1:18080/pasori/";

    let instance: Felica | null = null;
    let callback: CALL_BACK | null = null;

    export type COMMAND = 'open' | 'close' | 'status' | 'connect' | 'disconnect' | 'read';
    export type CALL_BACK = (command: COMMAND, status: boolean | undefined, cardNo: string | undefined) => void;

    type W_COMMAND = 'S' | 'C' | 'D' | 'R';

    interface SocketData {
        Category: string;
        Command: W_COMMAND;
        CardNo: string;
        ReaderConnected: boolean;
    }

    class Felica {
        socket!: WebSocket;

        constructor() {
            const fc = this;

            // create socket for connect to c# app
            fc.socket = new WebSocket(WS_URI);

            fc.socket.onopen = function $open(evt: Event) {
                if (callback) {
                    callback('open', undefined, undefined);
                }
            };

            fc.socket.onclose = function $close(evt: Event) {
                if (callback) {
                    callback('close', undefined, undefined);
                }
            };

            fc.socket.onmessage = function $message(evt: MessageEvent) {
                const json: SocketData = JSON.parse(evt.data);

                if (!callback || json.Category.toUpperCase() !== "FELICA") {
                    return;
                }

                // if message pass (send from felica app)
                switch (json.Command) {
                    case 'S':
                        callback('status', json.ReaderConnected, undefined);
                        break;
                    case 'C':
                        callback('connect', undefined, undefined);
                        break;
                    case 'D':
                        callback('disconnect', undefined, undefined);
                        break;
                    case 'R':
                        callback('read', undefined, json.CardNo);
                        break;
                }
            };
        }
    }

    // export only create method for Felica class
    export function felica(cb: CALL_BACK) {
        // if reconnect, close old connect
        if (instance && instance.socket.OPEN) {
            instance.socket.close();
        }

        // register callback function
        callback = cb;

        // create new instance (and new socket connection)
        return instance = new Felica();
    }
}