ptBossApp.controller('DefinitionSportController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.defSport;
	$scope.defSports;
	
	
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
		findAllDefSport();
		
    };
    
    $scope.addNewDefSport =function(){
		$scope.defSport=new Object();
		$scope.defSport.spId="0";
		$scope.defSport.spName="";
		
		$scope.defSportDevice.spName=
		
		$scope.willDefSportCreate=true;
	};
    
	
	
	
	$scope.deleteDefSport =function(defSport){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSport/delete",
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
			  url: "/bein/definition/defSport/create",
			  data:angular.toJson($scope.defSport)
			}).then(function successCallback(response) {
				$scope.defSport=response.data.resultObj;
				
				$scope.willDefSportCreate=true;
				findAllDefSport();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
		
	
	
	$scope.showDefSport =function(spId){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defSport/findById/"+spId
			}).then(function successCallback(response) {
				$scope.defSport=response.data;
				$scope.willDefSportCreate=true;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	};
	
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
	
});