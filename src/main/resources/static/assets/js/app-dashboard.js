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
        switch (this.message.actionType.toLowerCase()) {
            case 'connect' :
                type = 'success';
                text = `connected!`;
                break;
            case 'open' :
                type = 'info';
                text = `opened!`;
                Action.setOpenIcon(this.card);
                break;
            case 'close' :
                type = 'info';
                text = `closed!`;
                Action.setLockedIcon(this.card);
                break;
            case 'error' :
                type = 'danger';
                text = `was unable to perform an action!`;
                Action.setErrorIcon(this.card);
                break;
            case 'disconnect':
                type = 'danger';
                text = `disconnected!`;
                Action.setErrorIcon(this.card);
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
        $('.content').on('click','.js-device-card',function () {
            Action.onClickAction($(this),appDashboard);
        });
    }

    static onClickAction(element, appDashboard) {
        if(element.attr('data-action-pending') === true){
            return;
        }
        Action.setLoadingIcon(element);
        appDashboard.sendData({
            deviceId : element.attr('data-device-name'),
            action : Action.getOppositeState(element.data('currentState'))
        });
    }

    static setLoadingIcon(element) {
        element.find('.js-device-action').html('autorenew');
        element.attr('data-action-pending',true);
    }

    static setOpenIcon(element) {
        element.find('.js-device-action').html('lock_open');
        element.attr('data-action-pending',false);
    }

    static setLockedIcon(element) {
        element.find('.js-device-action').html('lock');
        element.attr('data-action-pending',false);
    }

    static setErrorIcon(element) {
        element.find('.js-device-action').html('error');
        element.attr('data-action-pending',false);
    }

    static getOppositeState(state){
        if(state === 'open'){
            return 'close';
        }else {
            return 'open';
        }
    }
}

const appDashboard = new AppDashboard(Stomp, wsUrl, subscribeUrl, sendUrl, new RenderAppDashboardMessage(infoUrl));
const action = new Action(appDashboard);