var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#matching").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = ws = new WebSocket("ws://chat.meizu.com/ws.do");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function matching(name) {
	$.ajax(
		{ 
			url: "/matching.do",
			type: "POST",
			dataType: "json", 
			data: {name : name},
		  	success: function(data){
		  		var dest = "/room/" + data.roomId;
		  		console.log("dest:" + dest)
		  		stompClient.subscribe(dest, function (username) {
		  			joinRoom(JSON.parse(username.body));
		  		});
		  		
		  		stompClient.send("/app/join/" + data.roomId + "/" + data.user, {}, JSON.stringify({'roomId': data}));
      		},
            error: function(err) {}
      	});
}

function joinRoom(user) {
	$("#players").html("");
	$("#roomId").html("");
	$.each(user, function(i, item) {
			$("#players").append("<tr><td>" + item + "</td></tr>");
			});
	
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#matching" ).click(function() { matching($("#name").val()); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});