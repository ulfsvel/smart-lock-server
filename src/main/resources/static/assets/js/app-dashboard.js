const subscribeUrl = '/user/devicesData/influx';
const wsUrl = '/ws';
const infoUrl = '/api/deviceDetails/';
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

    constructor(infoUrl) {
        this.infoUrl = infoUrl;
        this.card = null;
    }

    setMessage(message) {
        this.message = message;
        if (this.message.actionType === 'new') {
            const baseClass = this;
            $.get({
                url: this.infoUrl + baseClass.message.deviceId,
                data: {},
                dataType: 'json',
            }).done(function (data) {
                $(".js-devices-container").append(data.card);
                baseClass.card = $('[data-device-name="' + data.deviceId + '"]');
                baseClass.processAction();
            });

        }else{
            this.card = $('[data-device-name="' + this.message.deviceId + '"]');
            this.processAction();
        }
        console.log(message);
    }

    processAction() {
        let type, text;
        switch (this.message.actionType) {
            case 'CONNECT' :
                type = 'success';
                text = `connected!`;
                Action.setConnectIcon(this.card);
                break;
            case 'OPEN' :
                type = 'info';
                text = `opened!`;
                Action.setOpenIcon(this.card);
                break;
            case 'CLOSE' :
                type = 'info';
                text = `closed!`;
                Action.setLockedIcon(this.card);
                break;
            case 'ERROR' :
                type = 'danger';
                text = `was unable to perform an action!`;
                Action.setErrorIcon(this.card);
                break;
            case 'DISCONNECT':
                type = 'danger';
                text = `disconnected!`;
                Action.setDisconnectIcon(this.card);
                break;
            case 'OPEN_REQUEST':
                type = 'info';
                text = `received an OPEN request!`;
                Action.setLoadingIcon(this.card);
                break;
            case 'CLOSE_REQUEST':
                type = 'info';
                text = `received an CLOSE request!`;
                Action.setLoadingIcon(this.card);
                break;
        }
        const deviceName = this.card.find('.js-device-name').html();
        text = `Device with name [${deviceName}] ` + text;
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
        $('.content').on('click','.js-device-action',function () {
            Action.onClickAction($(this),appDashboard);
        });
    }

    static onClickAction(element, appDashboard) {
        if(element.attr('data-action-pending') === 'true'){
            return;
        }
        appDashboard.sendData({
            deviceId : element.closest('.js-device-card').attr('data-device-name'),
            action : Action.getOppositeState(element.attr('data-current-state'))
        });
        if(Action.getOppositeState(element.attr('data-current-state')) === 'OPEN_REQUEST'){
            Action.setOpenRequstIcon(element);
        }else if(Action.getOppositeState(element.attr('data-current-state')) === 'CLOSE_REQUEST'){
            Action.setCloseRequstIcon(element);
        }

    }

    static setOpenRequstIcon(element) {
        element.html('autorenew');
        element.attr('data-action-pending',true);
        element.attr('data-current-state','OPEN_REQUEST');
    }

    static setCloseRequstIcon(element) {
        element.html('autorenew');
        element.attr('data-action-pending',true);
        element.attr('data-current-state','CLOSE_REQUEST');
    }

    static setOpenIcon(element) {
        element.html('lock_open');
        element.attr('data-action-pending',false);
        element.attr('data-current-state','OPEN');
    }

    static setLockedIcon(element) {
        element.html('lock');
        element.attr('data-action-pending',false);
        element.attr('data-current-state','CLOSE');
    }

    static setErrorIcon(element) {
        element.html('error');
        element.attr('data-action-pending',true);
        element.attr('data-current-state','ERROR');
    }

    static setDisconnectIcon(element) {
        element.html('mobile_off');
        element.attr('data-action-pending',true);
        element.attr('data-current-state','ERROR');
    }

    static setConnectIcon(element) {
        element.html('mobile_friendly');
        element.attr('data-action-pending',true);
        element.attr('data-current-state','ERROR');
    }

    static getOppositeState(state){
        if(state === 'OPEN'){
            return 'CLOSE_REQUEST';
        }else if(state === 'CLOSE') {
            return 'OPEN_REQUEST';
        }
        return null;
    }
}

const appDashboard = new AppDashboard(Stomp, wsUrl, subscribeUrl, sendUrl, new RenderAppDashboardMessage(infoUrl));
const action = new Action(appDashboard);