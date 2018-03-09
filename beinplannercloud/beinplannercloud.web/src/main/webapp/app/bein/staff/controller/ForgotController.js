ptBossApp.controller('ForgotController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.email="";
	
	$scope.sendPassword=function(){
		
		
		if($scope.email=="" | $scope.email.match(/@/g).length==0){
			toastr.error($translate.instant("emailRequired"));
			return;
		}

		
		
		var frmDatum=new Object();
		frmDatum.content=$translate.instant("yourPasswordContent");
		frmDatum.subject=$translate.instant("yourPasswordSubject");
		frmDatum.toPerson=$scope.email;
		
		
		$http({
  		  method:'POST',
  		  url: "/bein/staff/forgotPassword/",
  		  contentType: "application/json; charset=utf-8",				    
  		  data:angular.toJson(frmDatum),
  		}).then(function(response) {
  			var res=response.data;
  			if(res.resultStatu=="success"){
  				toastr.success($translate.instant("passwordSendToYourMail"));
  			}else{
  				toastr.error($translate.instant(res.resultMessage));
  			}
  			
  			
  			
  		});
		
	}
	
	
});