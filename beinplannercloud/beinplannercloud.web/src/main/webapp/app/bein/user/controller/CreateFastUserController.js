ptBossApp.controller('CreateFastUserController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.maskGsm="(999) 999-9999";
	
	$scope.user=new Object();
	$scope.user.userId=0;
	$scope.user.userGsm;
	$scope.user.userGender="1";
	
	
	$scope.initCFUC=function(){
		
	}
	
	
	$scope.newMemberCreate=function(){
		$scope.user.userId=0;
		$scope.user.userGsm="";
		$scope.user.userGender="1";
		$scope.user.userSurname="";
		$scope.user.userName="";
	}
	
	$scope.createMember =function(){
		 $http({
			  method:'POST',
			  url: "/bein/member/create",
			  data: angular.toJson($scope.user),
			}).then(function successCallback(response) {
				var res=response.data;
				if(res.resultStatu=="success"){
					$scope.user=res.resultObj;
					$scope.user.userGender=""+$scope.user.userGender;
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	};
	
	
	
});