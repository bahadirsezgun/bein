ptBossApp.controller('PotentialUserController', function($scope,$http,$routeParams,$translate,parameterService,$location,homerService,commonService,globals) {

    $scope.maskGsm="(999) 999-9999";
	
    
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
		
	}
	
	$scope.createMember =function(){
		
		
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
		 $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/findAll/"+$scope.firmId+"/0",
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.staffs=res;
				$scope.$apply();
				
				if(parameterService.param1!=""){
					var potential=parameterService.param1;
					$scope.userId=potential.userId;
					$scope.firmId=""+potential.firmId;
					$scope.userName=potential.userName;
					$scope.userSurname=potential.userSurname;
					$scope.userGsm=potential.userGsm;
					$scope.userGender=""+potential.userGender;
					$scope.pStatu=""+potential.pStatu;
					$scope.pComment=potential.pComment;
					$scope.userEmail=""+potential.userEmail;
					$scope.staffId=""+potential.staffId;
					$scope.$apply();
				}
				
			});
		}
	
});