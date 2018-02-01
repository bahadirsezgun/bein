ptBossApp.controller('SpecialDatesUserFindController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.users;
	$scope.mailSend=false;
	$scope.user;
	$scope.sendMailPerson;
	$scope.dateFormat;
	
	$scope.init=function(){
		 
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			$http({
				  method:'POST',
				  url: "/bein/dashboard/specialDates"
				}).then(function successCallback(response) {
					
						$scope.users=response.data.resultObj;
					
				}, function errorCallback(response) {
					$location.path("/login");
				});
		});
		
		
	}
	
	
	$scope.closeMail=function(){
		$scope.mailSend=false;
	}
	
	
	$scope.openMail=function(pu){
		if(pu.gender==1){
			$scope.sendMailPerson=$translate.instant("wTitle")+" "+pu.userName+" "+pu.userSurname;
		}else{
			$scope.sendMailPerson=$translate.instant("mTitle")+" "+pu.userName+" "+pu.userSurname;
		}
		$scope.user=pu;
		$scope.mailSend=true;
	}
	
	$scope.sendMail=function(){
		
		var frmDatum={"mailSubject":$translate.instant("birthdateSubject")
				      ,"mailContent":$scope.sendMailPerson+", "+$scope.mailContent
				      ,"userEmail":$scope.user.userEmail};
		
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/mail/sendMailForSpecialDates",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$scope.mailSend=false;
				toastr.success($translate.instant("mailSended"));
				$scope.$apply();
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  console.log(textStatus);
				  console.log(errorThrown);
			});
		
		
	}
	
	
	
	
	
});