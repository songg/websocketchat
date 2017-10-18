var stompClient = null;
var roomId = "";
var roomPrivateKey = "";
var userPrivateKey = "";
var userName = "";

//监听dest;
var wolfsubscription  = null;
var roomsubscription = null;
var seersubscription = null;
var timelinsubscription = null;
var roomusersubscription = null;

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

//退出房间按钮状态
function quitRoomStatus(status) {
	$("#quit").prop("disabled", status);
}

function connect() {
    var socket = ws = new WebSocket("ws://172.17.137.163/ws.do");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, 
    function (frame) {
    	setConnected(true);
    	console.log('Connected: ' + frame);
    },
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
		  		roomPrivateKey = data.roomPrivateKey;
		  		userPrivateKey = data.user.privateKey;
		  		userName = data.user.name;
		  		
		  		//订阅房间广播消息，包括玩家加入，退出等
		  		var dest = "/room/" + data.roomPrivateKey + "/" + data.roomId;
		  		roomsubscription = stompClient.subscribe(dest, function (msg) {
		  			joinRoom(JSON.parse(msg.body));
		  		});
		  		
		  		
		  		//订阅私人消息
		  		var userDest = "/room/" + data.roomPrivateKey + "/" + data.roomId + "/user/" + data.user.privateKey + "/" + data.user.index;
		  		roomusersubscription = stompClient.subscribe(userDest, function(msg) {
		  			rolePick(JSON.parse(msg.body));
		  		}); 
		  		
		  		stompClient.send("/app/join/" + data.roomId + "/" + data.user.name, {}, JSON.stringify({'roomId': data.roomId, 'userPrivateKey' : data.user.privateKey}));
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
			alert("你的角色是:狼人");
			wolfsubscription = stompClient.subscribe();
  			break;
		case 2:
  			alert("你的角色是:村民");
  			break;
		case 3:
			alert("你的角色是:守卫");
			break;
		case 4:
			alert("你的角色是:预言家");
			var seerDest = "/room/" + roomPrivateKey + "/" + roomId + "/seer/" + userPrivateKey
			seersubscription = stompClient.subscribe(seerDest, function(msg) {
				identifyRole(JSON.parse(mgs.body));
			});
			break;
	}
	
	var timelineDest = "/room/" + roomPrivateKey + "/" + roomId + "/timeline";
	timelinsubscription = stompClient.subscribe(timelineDest, function(msg) {
		  			timeline(JSON.parse(msg.body));
		  		});
	sleep(5000);
	
	var triggerDest = "/app/trigger/" + roomId;
	stompClient.send(triggerDest, {}, {});
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    quitRoomStatus(true);		  	
}

function identifyRole(roleint) {
		switch(roleint) {
		case 1:
			alert("他的身份是:狼人");
  			break;
		default:
  			alert("他的身份是:好人");
	}
}

function timeline(timelineMsg) {
	console.log("30秒钟时间");
	sleep(30);
}

function quitRoom() {
	$("#players").html("");
	$("#roomId").html("");
	stompClient.send("/app/quit/" + roomId + "/" + $("#name").val(), {});
	
	unsubscribeAll();
}

function unsubscribeAll() {
	if(wolfsubscription != null) {
		wolfsubscription.unsubscribe();
	}
	
	if(roomsubscription != null) {
		roomsubscription.unsubscribe();
	}
	
	if(seersubscription != null) {
		seersubscription.unsubscribe();
	}
	
	if(timelinsubscription != null) {
		timelinsubscription.unsubscribe();
	}
	
	if(roomusersubscription != null) {
		roomusersubscription.unsubscribe();
	}
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