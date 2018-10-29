

var stompClient = null;
var email = 'alexbotici@gmail.com';
var password = 'test';


function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({
            username: email,
            password: password
        }
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
    if(message.actionType === 'connect'){
        const deviceTemplate = `
<div data-device="${message.deviceName}" class="col-lg-3 col-md-6 col-sm-6">
    <div class="card card-stats">
        <div class="card-header card-header-warning card-header-icon">
            <div class="card-icon">
                <i class="material-icons">content_copy</i>
            </div>
            <p class="card-category">State</p>
            <h3 class="card-title">Open</h3>
        </div>
        <div class="card-footer">
            <div class="stats">
                <i class="material-icons text-danger">warning</i>
                <a href="#pablo">Lock is open</a>
            </div>
        </div>
    </div>
</div>`;
        $(".js-devices-container").append(deviceTemplate);
    }else{
        console.log(message);
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendData(); });
});
