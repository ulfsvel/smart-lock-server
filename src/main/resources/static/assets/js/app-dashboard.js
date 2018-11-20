const subscribeUrl = '/user/devicesData/influx';
const wsUrl = '/ws';
const infoUrl = '/api/DeviceDetails/';
const sendUrl = 'app/sendToDevices';

class AppDashboard {

    constructor(Stomp,wsUrl, subscribeUrl, sendUrl, render) {
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

class renderAppDashboardMessage {

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
                const deviceTemplate = `
              <div data-device-name="${data.deviceId}" class="card">
                <div class="card-header card-header-primary">
                  <h4 class="card-title js-device-name">${data.deviceName}</h4>
                  <p class="card-category js-device-header">Syncing</p>
                </div>
                <div class="card-body">
                  <table class="table table-hover">
                    <thead class="text-warning">
                      <tr>
                      <th>Time</th>
                      <th>Action</th>
                      <th>Description</th>
                    </tr>
                    </thead>
                    <tbody class="js-device-body">
                     
                    </tbody>
                  </table>
                </div>
              </div>`;
                $(".js-devices-container").append(deviceTemplate);
                baseClass.card = $('[data-device-name="' + data.deviceId + '"]');
                baseClass.processAction();
            });

        }
        this.card = $('[data-device-name="' + this.message.deviceId + '"]');
        this.processAction();
        console.log(message);
    }

    processAction() {
        let type, text;
        switch (this.message.actionType) {
            case 'connect' :
                type = 'success';
                text = `connected!`;
                break;
            case 'open' :
                type = 'info';
                text = `opened!`;
                break;
            case 'close' :
                type = 'info';
                text = `closed!`;
                break;
            case 'disconnect':
                type = 'danger';
                text = `disconnected!`;
                break;
        }
        const deviceName = this.card.find('js-device-name').html();
        text = `Device with name [${deviceName}] ` + text;
        renderAppDashboardMessage.showNotification(type, text);
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
        this.card.find('.js-device-header').text(this.message.actionType.toUpperCase());
        const text = `<tr><td>${this.message.time}</td><td>${this.message.actionType.toUpperCase()}</td><td>${this.message.actionContent}</td></tr>`;
        let log = this.card.find('.js-device-body');
        let logEntries = log.find('tr');
        if (logEntries.length === 5) {
            logEntries.last().remove();
        }
        log.prepend(text);
    }

}

const dashboard = new AppDashboard(Stomp,wsUrl, subscribeUrl, sendUrl, new renderAppDashboardMessage(infoUrl));