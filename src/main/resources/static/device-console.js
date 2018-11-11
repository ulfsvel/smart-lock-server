var stompClient = null;
var email = '';

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    email = document.getElementById("login").value;
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({
            username: document.getElementById("login").value,
            password: document.getElementById("password").value
        }

    , function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/usersData/influx', function(greeting) {
                                                            showGreeting(JSON.parse(greeting.body).content);
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
        'userId': $("#user").val(),
        'deviceId' : $("#device").val(),
        'actionType' : $("#action-type").val(),
        'actionContent' : $("#action-content").val()
    }));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendData(); });
});
