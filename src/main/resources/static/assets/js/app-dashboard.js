const subscribeUrl = '/user/devicesData/influx';
const wsUrl = '/ws';
const sendUrl = '/app/sendToDevices';

class AppDashboard {

    constructor(Stomp, wsUrl, subscribeUrl, sendUrl, render) {
        const parent = this;
        this.sendUrl = sendUrl;
        this.socket = new SockJS('/ws');
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.connect(
            {},
            function (frame) {
                console.log('Connected: ' + frame);
                parent.stompClient.subscribe(subscribeUrl, function (message) {
                    render.setMessage(JSON.parse(message.body));
                });
            });
    }

    sendData(jsonData) {
        this.stompClient.send(
            this.sendUrl,
            {},
            JSON.stringify(jsonData)
        );
    }

}

class RenderAppDashboardMessage {

    constructor() {
        this.card = null;
    }

    setMessage(message) {
        this.message = message;
        if (this.message.actionType === 'NEW') {
            const device = `<div data-device-name="${this.message.deviceId}" class="card js-device-card">
    <div class="d-flex justify-content-between card-header card-header-primary">
        <div>
            <h4 class="card-title js-device-name">${this.message.deviceName}</h4>
            <p class="card-category js-device-header">NEW</p>
        </div>
        <a href="#">
            <i data-action-pending="true" data-current-state="ERROR" style="color: #FFF" class="display-2 material-icons js-device-action">fiber_new</i>
        </a>
    </div>
    <div class="card-body">
        <table class="table table-hover">
            <thead class="text-warning">
            <tr>
                <th class="w-20 text-justify">Time</th>
                <th class="w-30 text-justify">Action</th>
                <th class="w-50 text-justify">Description</th>
            </tr>
            </thead>
            <tbody class="js-device-body">
            </tbody>
        </table>
    </div>
</div>`;
            $('.js-no-devices').remove();
            $('.js-devices-container').append(device);
        }
        this.card = $('[data-device-name="' + this.message.deviceId + '"]');
        this.processAction();

        console.log(message);
    }

    processAction() {
        let type, text;
        const button = this.card.find('.js-device-action');
        switch (this.message.actionType) {
            case 'NEW' :
                type = 'success';
                text = `created`;
                break;
            case 'CONNECT' :
                type = 'success';
                text = `connected!`;
                Action.setConnectIcon(button);
                break;
            case 'OPEN' :
                type = 'info';
                text = `opened!`;
                Action.setOpenIcon(button);
                break;
            case 'CLOSE' :
                type = 'info';
                text = `closed!`;
                Action.setLockedIcon(button);
                break;
            case 'ERROR' :
                type = 'danger';
                text = `was unable to perform an action!`;
                Action.setErrorIcon(button);
                break;
            case 'DISCONNECT':
                type = 'danger';
                text = `disconnected!`;
                Action.setDisconnectIcon(button);
                break;
            case 'OPEN_REQUEST':
                type = 'info';
                text = `received an OPEN request!`;
                Action.setOpenRequestIcon(button);
                break;
            case 'CLOSE_REQUEST':
                type = 'info';
                text = `received an CLOSE request!`;
                Action.setCloseRequestIcon(button);
                break;
        }
        text = `Device with name [${this.message.deviceName}] ` + text;
        RenderAppDashboardMessage.showNotification(type, text);
        this.setStatus();
    }

    static showNotification(type, text) {
        $.notify({
            icon: "add_alert",
            message: text

        }, {
            type: type,
            timer: 3000,
            placement: {
                from: 'top',
                align: 'right'
            }
        });
    }

    setStatus() {
        this.card.find('.js-device-header').text(this.message.actionType);
        const text = `<tr><td>${this.message.time}</td><td>${this.message.actionType}</td><td>${this.message.actionContent}</td></tr>`;
        let log = this.card.find('.js-device-body');
        let logEntries = log.find('tr');
        if (logEntries.length === 5) {
            logEntries.last().remove();
        }
        log.prepend(text);
    }

}

class Action {

    constructor(appDashboard) {
        $('.content').on('click', '.js-device-action', function () {
            Action.onClickAction($(this), appDashboard);
        });
    }

    static onClickAction(element, appDashboard) {
        if (element.attr('data-action-pending') === 'true') {
            return;
        }
        appDashboard.sendData({
            deviceId: element.closest('.js-device-card').attr('data-device-name'),
            action: Action.getOppositeState(element.attr('data-current-state'))
        });
        if (Action.getOppositeState(element.attr('data-current-state')) === 'OPEN_REQUEST') {
            Action.setOpenRequestIcon(element);
        } else if (Action.getOppositeState(element.attr('data-current-state')) === 'CLOSE_REQUEST') {
            Action.setCloseRequestIcon(element);
        }

    }

    static setOpenRequestIcon(element) {
        element.html('autorenew');
        element.attr('data-action-pending', true);
        element.attr('data-current-state', 'OPEN_REQUEST');
    }

    static setCloseRequestIcon(element) {
        element.html('autorenew');
        element.attr('data-action-pending', true);
        element.attr('data-current-state', 'CLOSE_REQUEST');
    }

    static setOpenIcon(element) {
        element.html('lock_open');
        element.attr('data-action-pending', false);
        element.attr('data-current-state', 'OPEN');
    }

    static setLockedIcon(element) {
        element.html('lock');
        element.attr('data-action-pending', false);
        element.attr('data-current-state', 'CLOSE');
    }

    static setErrorIcon(element) {
        element.html('error');
        element.attr('data-action-pending', true);
        element.attr('data-current-state', 'ERROR');
    }

    static setDisconnectIcon(element) {
        element.html('mobile_off');
        element.attr('data-action-pending', true);
        element.attr('data-current-state', 'ERROR');
    }

    static setConnectIcon(element) {
        element.html('mobile_friendly');
        element.attr('data-action-pending', true);
        element.attr('data-current-state', 'ERROR');
    }

    static getOppositeState(state) {
        if (state === 'OPEN') {
            return 'CLOSE_REQUEST';
        } else if (state === 'CLOSE') {
            return 'OPEN_REQUEST';
        }
        return null;
    }
}

const appDashboard = new AppDashboard(Stomp, wsUrl, subscribeUrl, sendUrl, new RenderAppDashboardMessage());
const action = new Action(appDashboard);