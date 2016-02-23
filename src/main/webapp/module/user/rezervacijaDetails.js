app.controller('rezervacijaDetails', function($scope, $mdDialog, rezervacija, reservationService, POSETA_STATUS){
	
	$scope.init = function(){
		
		$scope.date = new Date();
		$scope.rezervacija = rezervacija;
		rezervacija.datetime = new Date(rezervacija.vreme)
		
		$scope.posetaStatusi = POSETA_STATUS;
		
		reservationService.getRezervacijaPoste($scope.rezervacija.id, function(response){
			$scope.rezervacija.posete = response.data;
			console.log(rezervacija.posete);
		})
	}
	
	$scope.cancel = function(){
		$mdDialog.hide();
	}
	
	$scope.deleteRez = function(){
		reservationService.deleteReservation($scope.rezervacija.id, function(response){
			$mdDialog.hide();
		})
	}
});