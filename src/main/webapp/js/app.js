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
	
	timeline();
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
		  		stompClient.subscribe(dest, function (msg) {
		  			joinRoom(JSON.parse(msg.body));
		  		});
		  		
		  		var userDest = "/room/" + data.roomId + "/user/" + data.user.index;
		  		stompClient.subscribe(userDest, function(msg) {
		  			rolePick(JSON.parse(msg.body));
		  		}); 
		  		
		  		stompClient.send("/app/join/" + data.roomId + "/" + data.user.name, {}, JSON.stringify({'roomId': data}));
		  		roomId = data.roomId;
				quitRoomStatus(false);		  		
      		},
            error: function(err) {}
      	});
}

function joinRoom(room) {
	$("#players").html("");
	$("#roomId").html("");
	$.each(room.players, function(i, item) {
				$("#players").append("<tr><td>" + item.name + "</td></tr>");
			
				changeRoomHolder(item.name, item.index, room.holderIndex);
			}
	);
	
}

function changeRoomHolder(username, userindex, holderIndex) {
	if($("#name").val() == username) {
		if(userindex == holderIndex) {
			$("#start").prop("disabled", false);
		}else {
			$("#start").prop("disabled", true);
		}
	}
}

function startGame() {
	var dest = "/app/start/" + roomId;
	stompClient.send(dest, {});
}

function rolePick(roleint) {
	switch(roleint) {
		case 1:
			alert("你的角色是:狼人")
  			break;
		case 2:
  			alert("你的角色是:村民")
  			break;
		case 3:
			alert("你的角色是:守卫")
			break;
		case 4:
			alert("你的角色是:预言家")
			break;
	}
	
	var dest = "/room/" + roomId + "/timeline";
	stompClient.subscribe(userDest, function(msg) {
		  			timeline(JSON.parse(msg.body));
		  		})
	sleep(5000);
	
	var triggerDest = "/app/trigger/" + roomId;
	stompClient.send(triggerDest, {});
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    quitRoomStatus(true);		  	
}

function timeline(timelineMsg) {
	
}

function quitRoom() {
	stompClient.send("/app/quit/" + roomId + "/" + $("#name").val(), {});
}

function sleep(numberMillis) { 
	var now = new Date(); 
	var exitTime = now.getTime() + numberMillis; 
	while (true) { 
		now = new Date(); 
		if (now.getTime() > exitTime) 
			return; 
	} 
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
    $( "#start" ).click(function() { startGame(); });
});