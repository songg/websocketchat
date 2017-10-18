(function() {
    var webSocketService = function(userInfoModel) {
    
    
        var socket = ws = new WebSocket("ws://172.17.137.163/ws.do");
    	var stompClient = Stomp.over(socket);
    	stompClient.connect({}, 
    		function (frame) {
//    			setConnected(true);
    			console.log('Connected: ' + frame);
    		},
    		function(message) {
    			// check message for disconnect
    			console.log("message:" + message);
			}
		);
	
		return {
			stompClient: stompClient
		};
		
        // var socket = new WebSocket('ws://localhost:8080');
        // socket.onopen = function () {
        // };

        // return {
        //    socket: socket,
        //    send: function(msg) {
        //        socket.send(msg);
        //    },
        //    onmessage: function(message) {
        //        socket.onmessage = message;
        //    },
        //    sendAPI: function(type, data, noToken) {
        //        if(!noToken || noToken == undefined) {
        //            data.id = userInfoModel.id;
        //            data.token = userInfoModel.token;
        //        }
        //        var obj = {
        //            type: type,
        //            data: data
        //        };
        //        socket.send(JSON.stringify(obj));
        //    }
        //};

    };
    angular.module('lyingman').service('webSocketService', webSocketService);
})();
