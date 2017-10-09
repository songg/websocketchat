var stompClient = null;
var roomId = "";

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

function quitRoomStatus(status) {
	$("#quit").prop("disabled", status);
}

function connect() {
    var socket = ws = new WebSocket("ws://chat.meizu.com/ws.do");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, 
    function (frame) {setConnected(true);console.log('Connected: ' + frame);}, 
    function(message) {
    	// check message for disconnect
    	console.log("message:" + message);
	}
	);
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
		  		roomId = data.roomId;
				quitRoomStatus(false);		  		
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
    quitRoomStatus(true);		  	
    console.log("Disconnected");
}

function quitRoom() {
	var dest = "/room/" + roomId;
	stompClient.unsubscribe(dest);
	stompClient.send("/app/quit/" + roomId + "/" + $("#name").val(), {});
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#matching" ).click(function() { matching($("#name").val()); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#quit" ).click(function() { quitRoom(); });
});