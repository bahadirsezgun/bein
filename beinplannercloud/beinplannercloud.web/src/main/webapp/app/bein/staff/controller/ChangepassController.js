ptBossApp.controller('ChangepassController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	
	$scope.oldPassword;
	$scope.passwordControl;
	$scope.password;
	$scope.userEmail;
	
	$scope.init=function(){
		
	}
	
	$scope.changePassword=function(){
		
      if($scope.password!=$scope.passwordControl)	{
    	  toastr.error($translate.instant("passwordDifferent"));
    	  return;
      }else if($scope.password==""){
    	  toastr.error($translate.instant("passwordMustNotBeEmpty"));
    	  return;
      }	
		
		
	var user=new Object();
		user.oldPassword=$scope.oldPassword;
		user.newPassword=$scope.password;
		user.userEmail=$scope.userEmail;
		
		
		
	$http({
		  method:'POST',
		  url: "/bein/staff/changePassword/",
		  data:angular.toJson(user),
		}).then(function(response) {
			var res=response.data;
			if(res.resultStatu=="success"){
				toastr.success($translate.instant(res.resultMessage));
				$(location).href("attr","/");
			}else{
				 toastr.error($translate.instant(res.resultMessage));  
			  }
			
		});
		
	}
	
	
	
});