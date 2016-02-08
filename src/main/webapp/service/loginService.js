app.service('loginService', function($http){
	return{
		login: function(token, onSuccess, onError){
			$http.post('login/loginUser', 'gdrljaca', 'gdrljaca').then(onSuccess, onError);
		}
	}
});