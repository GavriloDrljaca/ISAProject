app.service('restaurantService', function($http){
	return {
		getRestaurants: function(onSuccess, onError){
			$http.get('/restaurants').then(onSuccess, onError);
		},
		updateRestaurant: function(restaurant, onSuccess, onError){
			$http.put('restaurants/'+restaurant.id, restaurant).then(onSuccess, onError);
		},
		saveRestaurant: function(restaurant, onSuccess, onError){
			$http.post('restaurants', restaurant).then(onSuccess, onError);
		},
		deleteJelo: function(jeloId, onSuccess ,onError){
			$http.delete('restaurants/jelo/'+jeloId).then(onSuccess, onError);
		},
		createTables: function(restoranId,stolovi, onSuccess, onError){
			$http.post("restaurants/createTables/"+ restoranId, stolovi).then(onSuccess, onError);
		}
		
		
		
	}
	
});