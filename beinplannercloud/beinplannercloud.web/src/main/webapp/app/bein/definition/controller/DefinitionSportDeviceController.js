ptBossApp.controller('DefinitionSportDeviceController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.defSportDevice;
	$scope.defSportsDevice;
	
	
	toastr.options = {
	        "debug": false,
	        "newestOnTop": false,
	        "positionClass": "toast-top-center",
	        "closeButton": true,
	        "toastClass": "animated fadeInDown",
	    };
	
	$scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("SportTitle");
		commonService.pageComment=$translate.instant("defSportDefinitionComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		findAllDefDeviceSport();
		
    };
    
    $scope.addNewDefDeviceSport =function(){
		$scope.defSportDevice=new Object();
		$scope.defSportDevice.spdId="0";
		$scope.defSportDevice.spId="0";
		$scope.defSportDevice.spdName="";
		
		$scope.willDefSportDeviceCreate=true;
	};
    
	
	
	
	$scope.deleteDefDeviceSport =function(defSport){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSportDevice/delete",
			  data:angular.toJson(defSport)
			}).then(function successCallback(response) {
				toastr.success($translate.instant(response.data.resultMessage))
				findAllDefSport();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	
	$scope.createDefSport =function(){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSportDevice/create",
			  data:angular.toJson($scope.defSportDevice)
			}).then(function successCallback(response) {
				$scope.defSport=response.data.resultObj;
				
				$scope.willDefSportDeviceCreate=true;
				findAllDefSportDevice();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
		
	
	
	$scope.showDefSport =function(spId){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSportDevice/findById/"+spId
			}).then(function successCallback(response) {
				$scope.defSportDevice=response.data.resultObj;
				$scope.willDefSportDeviceCreate=true;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	};
	
    function findAllDefSport(){
		$http({
		  method: 'POST',
		  url: "/bein/definition/defSportDevice/findAll"
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