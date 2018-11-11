

var stompClient = null;
connect();

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}
        , function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/user/devicesData/influx', function(message) {
                showData(JSON.parse(message.body));
            });
        });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendData() {
    stompClient.send("/app/sendToUsers", {
    }, JSON.stringify({
        'email': email,
        'deviceName' : $("#name").val(),
        'actionType' : $("#action-type").val(),
        'actionContent' : $("#action-content").val()
    }));
}

function showData(message) {
    console.log(message);
    if(message.actionType === 'connect'){
        const deviceTemplate = `
              <div data-device-name="${message.deviceId}" class="card">
                <div class="card-header card-header-primary">
                  <h4 class="card-title">${message.deviceId}</h4>
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
    }
    processAction(message);
}

function showNotification(type, message) {
    $.notify({
        icon: "add_alert",
        message: message

    }, {
        type: type,
        timer: 3000,
        placement: {
            from: 'top',
            align: 'right'
        }
    });
}

function setStatus(message) {
    const card = $('[data-device-name="'+message.deviceId+'"]');
    card.find('.js-device-header').text(message.actionType.toUpperCase());
    const text = `<tr><td>${message.time}</td><td>${message.actionType.toUpperCase()}</td><td>${message.actionContent}</td></tr>`;
    let log = card.find('.js-device-body');
    let logEntries = log.find('tr');
    if(logEntries.length === 5){
        logEntries.last().remove();
    }
    log.prepend(text);
}

function processAction(message){
    let type,text;
    switch (message.actionType) {
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
    text = `Device with name [${message.deviceId}] ` + text;
    showNotification(type,text);
    setStatus(message);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendData(); });
});
