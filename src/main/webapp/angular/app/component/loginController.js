(function() {
	var loginController = function($scope, webSocketService, userInfoModel, $location) {
		
		function joinRoom(msg) {
			$(".player-name-section").html("");
			$(".player-number-section").html("");
			var index = 1;
			$.each(msg.players, function(i, item) {
				var innerHtml = "<div class=\"player-name-${index}\">" + "<p>" + item.name + "</p></div>"
				$(".player-name-section").append(innerHtml.replace("${index}", index));
				
				var innerHtmlIndex = "<p class=\"player-number-${index}\">" + item.index + "</p>"
				$(".player-number-section").append(innerHtmlIndex.replace("${index}", index));
				
				console.log(innerHtmlIndex.replace("${index}", index))
				index++;

//				changeRoomHolder(item.name, item.index, room.holderIndex);
			});

		}

		// 匹配房间
		var matching = function() {
			$location.path("/room");
			$.ajax({
				url : "/matching.do",
				type : "POST",
				dataType : "json",
				data : {
					name : $scope.name
				},
				success : function(data) {
					roomPrivateKey = data.roomPrivateKey;
					userPrivateKey = data.user.privateKey;
					userName = data.user.name;

					// 订阅房间广播消息，包括玩家加入，退出等
					var dest = "/room/" + data.roomPrivateKey + "/"
							+ data.roomId;
					roomsubscription = webSocketService.stompClient.subscribe(
							dest, function(msg) {
								roomController.joinRoom(JSON.parse(msg.body));
							});

					// 订阅私人消息
					var userDest = "/room/" + data.roomPrivateKey + "/"
							+ data.roomId + "/user/" + data.user.privateKey
							+ "/" + data.user.index;
					roomusersubscription = webSocketService.stompClient
							.subscribe(userDest, function(msg) {
								rolePick(JSON.parse(msg.body));
							});

					webSocketService.stompClient.send("/app/join/"
							+ data.roomId + "/" + data.user.name, {}, JSON
							.stringify({
								'roomId' : data.roomId,
								'userPrivateKey' : data.user.privateKey
							}));
					roomId = data.roomId;
				},
				error : function(err) {
				}
			});
		}

		$scope.name = '';
		var signIn = function(mode) {
			// var data = {
			// name: $scope.name
			// };
			//			
			// console.log($scope.name);
			userInfoModel.mode = mode;

			matching();
		};
		$scope.signIn = signIn;

		// webSocketService.onmessage(function(msg) {
		// var obj = JSON.parse(msg.data);
		// console.log(obj);
		// if(obj.type != undefined) {
		// var data = obj.data;
		// switch (obj.type) {
		// case 'login_succ':
		// var data = obj.data;
		// userInfoModel.name = data.name;
		// userInfoModel.id = data.id;
		// userInfoModel.token = data.token;
		// console.log(userInfoModel.token);
		// $location.path('/joinroom');
		// $scope.$apply();
		// break;
		// default:
		//
		// }
		// }
		// });

	};
	angular.module('lyingman').controller('loginController', loginController);
})();
