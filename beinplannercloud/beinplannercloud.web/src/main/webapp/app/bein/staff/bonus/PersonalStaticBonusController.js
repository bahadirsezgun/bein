ptBossApp.controller('PersonalStaticBonusController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.defBonus=new Object();
	$scope.defBonus.bonusValue;
	$scope.defBonus.bonusCount;
	$scope.defBonus.bonusId=0;
	$scope.defBonus.bonusType=1;
	$scope.defBonus.bonusIsType=2;
	$scope.defBonus.bonusProgId="0";
	$scope.defBonus.userId=$scope.userId;
	
	
	$scope.defBonuses;
	$scope.programPersonals;
	$scope.noProgram;
	
	$scope.initPStaticBonus=function(){
		$("[data-toggle=popover]").popover();
		findPrograms();
		findUserPersonelStaticBonus();
		
		
		
	};
	
	
	function findUserPersonelStaticBonus(){
		   $http({
				  method:'POST',
				  url: "/bein/staff/bonus/findPersonalStaticBonus/"+$scope.userId,
				}).then(function successCallback(response) {
					$scope.defBonuses=response.data;
				}, function errorCallback(response) {
					$location.path("/login");
				});
	}
	
	
	$scope.deletePersonalStaticBonus=function(bonusId){
		$http({
			  method:'POST',
			  url: "/bein/staff/bonus/delete/"+bonusId,
			}).then(function successCallback(response) {
				var res=response.data;
				
				if(res.resultStatu=="success"){
					findUserPersonelStaticBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
		
	}
	
	$scope.addPersonalStaticBonus=function(bonusId){
		$http({
			  method:'POST',
			  url: "/bein/staff/bonus/create",
			  data: angular.toJson($scope.defBonus),
		   }).then(function successCallback(response) {
				
				var res=response.data;
				
				if(res.resultStatu=="success"){
					findUserPersonelStaticBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.newPersonalStaticBonus=function(bonusId){
		$scope.bonusValue="";
		$scope.bonusProgId="0";
		$scope.bonusId=0;
		$scope.$apply();
	}
	
	
	$scope.updatePersonalStaticBonus=function(defBonus){
		$scope.defBonus=new Object();
		$scope.defBonus.bonusValue;
		$scope.defBonus.bonusCount;
		$scope.defBonus.bonusId=0;
		$scope.defBonus.bonusType=1;
		$scope.defBonus.bonusIsType=2;
		$scope.defBonus.bonusProgId="0";
		$scope.defBonus.userId=$scope.userId;
	}
	
	
	function findPrograms(){
		$http({
			  method: 'POST',
			  url: "/bein/program/findPersonalPrograms"
			}).then(function successCallback(response) {
				$scope.programPersonals=response.data.resultObj;
				if($scope.programPersonals.length!=0){
					$scope.defBonus.bonusProgId="0";
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