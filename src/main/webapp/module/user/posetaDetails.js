app.controller('posetaDetails', function($scope, $mdDialog, $state, poseta, userService, POSETA_STATUS){

	$scope.init = function(){
		var today = new Date();
		if(poseta.rezervacija.datetime<today){
			$scope.isPoseta = true;
			$scope.staJe = "Poseta";
		}else{
			$scope.isPoseta = false;
			$scope.staJe = "Pozivnica";
		}
		
		$scope.posetaStatusi = POSETA_STATUS;
		
		$scope.poseta = poseta;
	}
	
	$scope.cancel = function(){
		$mdDialog.hide();
	}
	
	$scope.save = function(){
		userService.updatePoseta($scope.poseta, function(response){
			$mdDialog.hide();
		})
	}
})
