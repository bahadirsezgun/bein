ptBossApp.controller('ClassRateBonusController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.defBonus=new Object();
	$scope.defBonus.bonusValue;
	$scope.defBonus.bonusCount;
	$scope.defBonus.bonusId=0;
	$scope.defBonus.bonusType=2;
	$scope.defBonus.bonusIsType=1;
	$scope.defBonus.userId=$scope.userId;
	
	$scope.defBonuses;
	
	
	
	$scope.initCRateBonus=function(){
		$("[data-toggle=popover]").popover();
		findUserClassRateBonus();
	};
	
	
	function findUserClassRateBonus(){
			   $http({
				  method:'POST',
				  url: "/bein/staff/bonus/findClassRateBonus/"+$scope.userId,
				}).then(function successCallback(response) {
					$scope.defBonuses=response.data;
				}, function errorCallback(response) {
					$location.path("/login");
				});
	}
	
	
	$scope.deleteClassRateBonus=function(bonusId){
			$http({
				  method:'POST',
				  url: "/bein/staff/bonus/delete/"+bonusId,
				}).then(function successCallback(response) {
					var res=response.data;
					
					if(res.resultStatu=="success"){
						findUserClassRateBonus();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				}, function errorCallback(response) {
					$location.path("/login");
				});
		
	}
	
	$scope.addClassRateBonus=function(bonusId){
		  
		  
		   $http({
			  method:'POST',
			  url: "/bein/staff/bonus/create",
			  data: angular.toJson($scope.defBonus),
		   }).then(function successCallback(response) {
				
				var res=response.data;
				
				if(res.resultStatu=="success"){
					findUserClassRateBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.newClassRateBonus=function(bonusId){
		$scope.defBonus.bonusValue="";
		$scope.defBonus.bonusCount="";
		$scope.defBonus.bonusId=0;
	}
});