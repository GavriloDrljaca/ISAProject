app.service('loginService', function($http){
	return{
		login: function(username, password, onSuccess, onError){
			$http.post('login/loginUser/'+ username+"/"+ password).then(onSuccess, onError);
		},
		registerUser: function(user, onSuccess, onError){
			$http.post('login/registerUser/',user).then(onSuccess, onError);
		}
	}
});