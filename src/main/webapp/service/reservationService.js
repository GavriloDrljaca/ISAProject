app.service('reservationService', function($http){
	return{
		createReservation: function(userId, restaurantId, reservation, onSuccess, onError){
			$http.post('rezervacije/'+userId+"/"+restaurantId, reservation).then(onSuccess, onError);
		},
		deleteReservation: function(reservationId, onSuccess, onError){
			$http.delete('rezervacije/'+reservationId).then(onSuccess, onError);
		},
		getRezervisaniStolovi: function(trajanje, vreme, onSuccess, onError){
			$http.get('rezervacije/zauzetiStolovi/'+trajanje+'/'+vreme).then(onSuccess, onError);
		},
		getRezervacijaPoste: function(rezervacijaId, onSuccess, onError){
			$http.get("rezervacije/getPosete/" + rezervacijaId).then(onSuccess, onError);
		}
		
	}
	
});