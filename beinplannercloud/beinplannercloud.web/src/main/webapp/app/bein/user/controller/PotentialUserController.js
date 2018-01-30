ptBossApp.controller('PotentialUserController', function($scope,$http,$routeParams,$translate,parameterService,$location,homerService,commonService,globals) {

    $scope.maskGsm="(999) 999-9999";
    $scope.statuFixed=false;
    
    $scope.potential=new Object();
	$scope.potential.userId=0;
	$scope.potential.userGsm;
	$scope.potential.userGender="1";
	$scope.potential.pStatu="0";
	$scope.potential.pComment="";
	$scope.potential.userEmail="";
	$scope.potential.staffId="0";
	$scope.potential.userName="";
	$scope.potential.userSurname="";
	
	$scope.initPUC=function(){
		
		findAllStaff().then(function(){
			if($routeParams.userId!=null){
				$scope.potential.userId=$routeParams.userId;
				findById();
			}
		});
		
		
	}
	
	function findById(){
		$http({
			  method:'POST',
			  url: "/bein/potential/findById/"+$scope.potential.userId,
			}).then(function successCallback(response) {
				$scope.potential=response.data;
				$scope.potential.pStatu=""+$scope.potential.pStatu;
				$scope.potential.userGender=""+$scope.potential.userGender;
				$scope.potential.staffId=""+$scope.potential.staffId;
				
				if($scope.potential.pStatu=="3"){
					$scope.statuFixed=true;
				}
				
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.turnBackToSearch=function(){
		$location.path("/member/potential");
	}
	
	$scope.createMember =function(){
		$http({
			  method:'POST',
			  url: "/bein/potential/convertToMember",
			  data: JSON.stringify($scope.potential),
			}).then(function successCallback(response) {
				var res=response.data;
				
				if(res.resultStatu=="success"){
					toastr.success($translate.instant("success"));
					$scope.statuFixed=true;
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
			
		
	}
	
	$scope.createPotentialMember =function(){
		   $http({
			  method:'POST',
			  url: "/bein/potential/create",
			  data: JSON.stringify($scope.potential),
			}).then(function successCallback(response) {
				var res=response.data;
				if(res.resultStatu=="success"){
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
			
			
		
	};
	
	
	function findAllStaff(){
		return  $http({
			  method:'POST',
			  url: "/bein/staff/findAllStaff",
			}).then(function successCallback(response) {
				$scope.staffs=response.data.resultObj;
			}, function errorCallback(response) {
				$location.path("/login");
			});
		}
	
});