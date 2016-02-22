app.service('userService', function($http){
	return{
		getNonFriends: function(userId, onSuccess, onError){
			$http.get("users/nonFriends/"+userId).then(onSuccess, onError);
		},
		addFriend: function (userId, friendId, onSuccess, onError){
			$http.post("users/addFriend/"+userId+"/"+ friendId).then(onSuccess, onError);
		},
		getUser: function(userId, onSuccess, onError){
			$http.get("users/"+userId).then(onSuccess, onError);
		},
		getFriends: function(userId, onSuccess, onError){
			$http.get("users/friends/"+userId).then(onSuccess, onError);
		},
		deleteFriend: function(userId, friendId, onSuccess, onError){
			$http.delete("users/deleteFriend/"+userId+"/"+friendId).then(onSuccess, onError);
		},
		updateUser: function(user, onSuccess, onError){
			$http.put("users/"+user.id, user).then(onSuccess, onError);
		},
		getUserVisits: function(userId, onSuccess, onError){
			$http.get('users/posete/'+userId).then(onSuccess, onError);
		},
		getUserReservations: function(userId, onSuccess, onError){
			$http.get('users/rezervacije/'+userId).then(onSuccess, onError);
		}
	}
	
});