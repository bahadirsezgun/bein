ptBossApp.controller('DefinitionSportDeviceController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.defSportDevice=new Object();
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
		$scope.defSportDevice.spId="0";
		findAllDefSport();
		
    };
    
    $scope.addNewDefDeviceSport =function(){
		
    	if($scope.defSportDevice.spId!="0"){
    		
    		angular.forEach($scope.defSports,function(obj,data){
    			if($scope.defSportDevice.spId==obj.spId){
    				$scope.defSportDevice.spName=obj.spName;
    			}
    		})
    		
    		$scope.defSportDevice.spdName="";
    		$scope.defSportDevice.spdId="0";
    		
		$scope.willDefSportDeviceCreate=true;
    	}else{
			toastr.error($translate.instant("chooseDefSport"));
			$scope.defSportsDevice=new Array();
		}
	};
    
	
	
	
	$scope.deleteDefDeviceSport =function(defSport){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSportDevice/delete",
			  data:angular.toJson(defSport)
			}).then(function successCallback(response) {
				toastr.success($translate.instant(response.data.resultMessage))
				findAllDefSportDevice($scope.defSportDevice.spId);
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	
	$scope.createDefDeviceSport =function(){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSportDevice/create",
			  data:angular.toJson($scope.defSportDevice)
			}).then(function successCallback(response) {
				$scope.defSportDevice=response.data.resultObj;
				$scope.defSportDevice.spId=""+$scope.defSportDevice.spId;
				
				
				angular.forEach($scope.defSports,function(obj,data){
	    			if($scope.defSportDevice.spId==obj.spId){
	    				$scope.defSportDevice.spName=obj.spName;
	    			}
	    		})
				
				$scope.willDefSportDeviceCreate=true;
				findAllDefSportDevice($scope.defSportDevice.spId);
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
		
	
	
	$scope.showDefSportDevice =function(spId){
		
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSportDevice/findById/"+spId
			}).then(function successCallback(response) {
				$scope.defSportDevice=response.data;
				$scope.defSportDevice.spId=""+$scope.defSportDevice.spId;
				angular.forEach($scope.defSports,function(obj,data){
	    			if($scope.defSportDevice.spId==obj.spId){
	    				$scope.defSportDevice.spName=obj.spName;
	    			}
	    		})
				
				$scope.willDefSportDeviceCreate=true;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
		
	};
	
	$scope.defSportChange =function(){
		if($scope.defSportDevice.spId!="0"){
			findAllDefSportDevice($scope.defSportDevice.spId);
		}else{
			toastr.error($translate.instant("chooseDefSport"));
			$scope.defSportsDevice=new Array();
		}
		
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