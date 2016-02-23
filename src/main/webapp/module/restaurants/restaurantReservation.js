app.controller('restaurantReservation', function($scope, $mdDialog, $state, loginService, restaurantService, reservationService){
	$scope.init = function(){
		
		$scope.date = new Date();
		$scope.chooseTable = false;
		$scope.setTableConfiguration = false;
		$scope.brojRedova =1;
		$scope.brojKolona =1;
		$scope.createdTables = true;
		$scope.successfulReservation = false;
		$scope.nonSuccessfulReservation = false;
		$scope.rezervacija = {};
		$scope.rezervacija.trajanje = 0.5;
		$scope.rezervacija.vreme = "";
		//inicijalizuj rezervaciju
		
		angular.forEach($scope.restaurant.stolovi, function(sto){
			sto.rezervisi = false;
			sto.zauzet = false;
		})
		
		loginService.getProfile(function(response){
			$scope.user = response.data;
			$scope.user = response.data;
			if($scope.user.role === 'MENADZER_RESTORANA'){
				if($scope.user.restoran.id == $scope.restaurant.id){
					$scope.isManager = true;
				}else{
					$scope.isManager = false;
				}
			}else{
				$scope.isManager = false;
			}
			
			//SEE IF STOLOVI SETUP IS POSSIBLE
			if($scope.restaurant.stolovi[0]){
				if($scope.restaurant.stolovi[0].id){
					$scope.setTableConfiguration = false;
				}else{
					$scope.setTableConfiguration = true;
				}
			}else{
				if($scope.isManager){
					$scope.setTableConfiguration = true;
				}
			}
			
		}, function(response){
			loginService.logout();
		})
		
	}
	
	$scope.proceed = function(){
		var selectedDate = new Date($scope.rezervacija.vreme)
		if(!selectedDate || selectedDate<$scope.date){
			alert("Izabrali nevalidan datum!");
		}else{
			reservationService.getRezervisaniStolovi($scope.rezervacija.trajanje, selectedDate, function(response){
				$scope.rezervisaniStolovi = response.data;
				oznaciZauzeteStolove();
			})
			$scope.chooseTable = true;
		}
	}
	
	function oznaciZauzeteStolove(){
		var ii =0;
		var jj =0;
		for(ii; ii<$scope.restaurant.stolovi.length; ii++){
			var jj =0;
			for(jj; jj<$scope.rezervisaniStolovi.length; jj++){
				if($scope.rezervisaniStolovi[jj].id == $scope.restaurant.stolovi[ii].id){
					$scope.restaurant.stolovi[ii].zauzet = true;
				}
			}
		}
	}
	$scope.nazadNaIzborVremena = function(){
		$scope.chooseTable = false;
	}
	
	$scope.rezervisi = function(){
		
		var selectedDate = new Date($scope.rezervacija.vreme)
		if(!selectedDate || selectedDate<$scope.date){
			alert("Izabrali nevalidan datum!");
		}else{
			var rezervisaniStolovi = [];
			
			var i =0;
			console.log($scope.restaurant.stolovi);
			for(i; i<$scope.restaurant.stolovi.length; i++){
				if($scope.restaurant.stolovi[i].rezervisi){
					rezervisaniStolovi.push($scope.restaurant.stolovi[i]);
				}
			}
			$scope.rezervacija.vreme = selectedDate;
			$scope.rezervacija.stolovi = rezervisaniStolovi;
			$scope.rezervacija.user = $scope.user;
			$scope.rezervacija.restoran = $scope.restaurant;
			console.log($scope.rezervacija)
			reservationService.createReservation($scope.user.id, $scope.restaurant.id, $scope.rezervacija, function(response){
				$scope.successfulReservation = true;
				cleanTableReservations();
			},function(response){
					$scope.nonSuccessfulReservation = true;
					cleanTableReservations();	
				
			});
		
		}
		
		
	}
	
	function cleanTableReservations(){
		angular.forEach($scope.restaurant.stolovi, function(sto){
			sto.rezervisi = false;
			sto.zauzet = false;
		});
	}
	$scope.novaRezervacija = function(){
		$scope.successfulReservation = false;
		$scope.nonSuccessfulReservation = false;
	}
	
	$scope.createTables = function(ev){
		
		$scope.restaurant.brojRedova = $scope.brojRedova;
		$scope.restaurant.brojKolona = $scope.brojKolona;
		
		var ukupno = $scope.brojKolona*$scope.brojRedova;
		var i = 0;
		$scope.restaurant.stolovi = [];
		var sto = {}
		sto.brojStolova = 2;
		sto.pusacki = true;
		sto.dostupan = true;
		for(i; i<ukupno; i++){
			sto.redniBroj = i+1;
			$scope.restaurant.stolovi.push(sto);
		}
		console.log($scope.restaurant);
		console.log(ukupno);
		
		restaurantService.createTables($scope.restaurant.id, $scope.restaurant.stolovi, function(response){
			restaurantService.getRestaurant($scope.restaurant.id, function(response){
				$scope.restaurant.stolovi = response.data.stolovi;
				$scope.createdTables = true;
			});
		})
		
	}
	
	$scope.tableDetails = function(){
	}
	$scope.getNumber = function(num) {
	    var i =1;
	    ret = [];
	    for(i; i<=num; i++){
	    	ret.push(i);
	    }
	    
	    return ret;
	}
});