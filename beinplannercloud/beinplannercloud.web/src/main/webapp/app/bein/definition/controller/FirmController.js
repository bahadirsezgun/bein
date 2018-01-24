ptBossApp.controller('FirmController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {
	
	
	$scope.defFirm=new Object();
	
	$scope.defFirm.firmName="";
	$scope.defFirm.firmPhone="";
	$scope.defFirm.firmAddress="";
	$scope.defFirm.firmEmail="";
	
	$scope.defFirm.firmCityName="";
	$scope.defFirm.firmStateName="";
	
	$scope.maskGsm="(999) 999-9999";
	
	$scope.langs=new Array();
	
	
	$scope.init = function(){
		
		    findFirm();
		    commonService.pageName=$translate.instant("definition_firm");
			commonService.pageComment=$translate.instant("firmDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
		   
	}
	
	function findFirm(){
		$http({
			  method:'POST',
			  url: "/bein/definition/firm/find",
			}).then(function successCallback(response) {
				$scope.defFirm=response.data;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	
	$scope.createFirm =function(){
		   $http({
			  method:'POST',
			  url: "/bein/definition/firm/create",
			  data: angular.toJson($scope.defFirm),
			}).then(function successCallback(response) {
				var res=response.data;
				if(res!=null){
					if(res.resultStatu=="success"){
						toastr.success($translate.instant(res.resultMessage.trim()));
					}else{
						toastr.error($translate.instant(res.resultMessage.trim()));
					}
				}
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
	};
	
});