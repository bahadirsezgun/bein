ptBossApp.controller('ClassStaticRateBonusController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	
	
	$scope.defBonus=new Object();
	$scope.defBonus.bonusValue;
	$scope.defBonus.bonusCount;
	$scope.defBonus.bonusId=0;
	$scope.defBonus.bonusType=2;
	$scope.defBonus.bonusIsType=3;
	$scope.defBonus.bonusProgId="0";
	$scope.defBonus.userId=$scope.userId;
	
	
	$scope.defBonuses;
	$scope.programClasses;
	$scope.noProgram;
	
	$scope.initCStaticRateBonus=function(){
		$("[data-toggle=popover]").popover();
		findClassPrograms();
		findUserClassStaticRateBonus();
	};
	
	
	function findUserClassStaticRateBonus(){
			   $http({
					  method:'POST',
					  url: "/bein/staff/bonus/findClassStaticRateBonus/"+$scope.userId,
					}).then(function successCallback(response) {
						$scope.defBonuses=response.data;
					}, function errorCallback(response) {
						$location.path("/login");
					});
	}
	
	
	$scope.deleteClassStaticRateBonus=function(bonusId){
		  
		    
		    
		    $http({
				  method:'POST',
				  url: "/bein/staff/bonus/delete/"+bonusId,
				}).then(function successCallback(response) {
					var res=response.data;
					
					if(res.resultStatu=="success"){
						findUserClassStaticRateBonus();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				}, function errorCallback(response) {
					$location.path("/login");
				});
		
	}
	
	$scope.addClassStaticRateBonus=function(bonusId){
		  
		   $http({
				  method:'POST',
				  url: "/bein/staff/bonus/create",
				  data: angular.toJson($scope.defBonus),
			   }).then(function successCallback(response) {
					
					var res=response.data;
					
					if(res.resultStatu=="success"){
						findUserClassStaticRateBonus();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				}, function errorCallback(response) {
					$location.path("/login");
				});
		   
	}
	
	$scope.newClassStaticRateBonus=function(bonusId){
		$scope.defBonus=new Object();
		$scope.defBonus.bonusValue;
		$scope.defBonus.bonusCount;
		$scope.defBonus.bonusId=0;
		$scope.defBonus.bonusType=2;
		$scope.defBonus.bonusIsType=3;
		$scope.defBonus.bonusProgId="0";
		$scope.defBonus.userId=$scope.userId;
	}
	
	
	
	
	
	function findClassPrograms(){
		$http({
		  method: 'POST',
		  url: "/bein/program/findClassPrograms"
		}).then(function successCallback(response) {
			$scope.programClasses=response.data.resultObj;
			if($scope.programClasses.length!=0){
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