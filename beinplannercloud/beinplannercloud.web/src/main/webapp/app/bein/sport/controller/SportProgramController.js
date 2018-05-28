ptBossApp.controller('SportProgramController', function($rootScope,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.defSports;
	$scope.defSportsDevice;
	
	$scope.addNewProgram=false;
	$scope.sportPrograms=true;
	
	$scope.usps=new Array();
	$scope.usp=new Object();
	$scope.usp.applyDate="0";
	$scope.usp.spId="0";
	$scope.usp.spdId="0";
	
	$scope.applyDates=[{day:"2",dayName:"monday"},
					   {day:"3",dayName:"tuesday"},
					   {day:"4",dayName:"wednesday"},
					   {day:"5",dayName:"thursday"},
					   {day:"6",dayName:"friday"},
					   {day:"7",dayName:"saturday"},
					   {day:"1",dayName:"sunday"},
					   ]
	
	$scope.initSPC = function(){
		
		findAllDefSport();
		findUserSportBySaleId();
	};
	
	
	$scope.addDay=function(){
		$scope.addNewProgram=true;
		
	};
	
	
	$scope.createSportProgram=function(){
		$scope.usp.userId=$scope.userId;
		$scope.usp.saleType=$scope.packetSale.progType;
		$scope.usp.saleId=$scope.packetSale.saleId;
		
		$http({
			  method: 'POST',
			  url: "/bein/sport/create",
			  data:angular.toJson($scope.usp)
			}).then(function successCallback(response) {
				
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					findUserSportBySaleId();
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	$scope.deleteSportProgram=function(usp){
			
		$http({
			  method: 'POST',
			  url: "/bein/sport/create",
			  data:angular.toJson(usp)
			}).then(function successCallback(response) {
				findUserSportBySaleId();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	function findUserSportBySaleId(){
		if($scope.packetSale.saleId!=0){
		
		$http({
			  method: 'POST',
			  url: "/bein/sport/findBySaleIdAndSaleTypeAndUserId/"+$scope.packetSale.saleId+"/"+$scope.packetSale.progType+"/"+$scope.userId,
			}).then(function successCallback(response) {
				$scope.usps=response.data;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		}
	}
	
	
	$scope.defSportChange=function(){
		findAllDefSportDevice($scope.usp.spId);
	}
	
	function findAllDefSport(){
		$http({
		  method: 'POST',
		  url: "/bein/definition/defSport/findAll"
		}).then(function successCallback(response) {
			$scope.defSports=response.data;
			if($scope.defSports.length!=0){
				$scope.noProgram=false;
			}else{
				$scope.noProgram=true;
			}
			
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});
    }
	
	function findAllDefSportDevice(spId){
		$http({
		  method: 'POST',
		  url: "/bein/definition/defSportDevice/findBySp/"+spId
		}).then(function successCallback(response) {
			$scope.defSportsDevice=response.data;
		
			if($scope.defSportsDevice.length!=0){
				$scope.noProgram=false;
			}else{
				$scope.noProgram=true;
			}
			
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});
     }
	
	
});