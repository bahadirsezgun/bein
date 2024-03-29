ptBossApp.controller('SpecialDatesUserFindController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.users;
	$scope.mailSend=false;
	$scope.user;
	$scope.sendMailPerson;
	$scope.dateFormat;
	
	$scope.init=function(){
		 
		$('.summernote').summernote({height: 350,focus: true,disableDragAndDrop: false});
		
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
		
		 var sHTML = $('.summernote').code();
		
		var frmDatum={"mailSubject":$translate.instant("birthdateSubject")
				      ,"mailContent":$scope.sendMailPerson+", "+$scope.mailContent
				      ,"userEmail":$scope.user.userEmail};
		
		
		var mailObj=new Object();
		mailObj.toPerson=$scope.user.userEmail;
		mailObj.subject=$translate.instant("birthdateSubject");
		mailObj.content=" ";//$scope.sendMailPerson+", "+$scope.mailContent;
		mailObj.htmlContent=sHTML;
		
		
		$http({
			method:'POST',
			  url: "/bein/mail/sendBirthdayMail",
			  data:angular.toJson(mailObj),
			}).then(function successCallback(response) {
				toastr.success($translate.instant("success"));
				
		}, function errorCallback(response) {
			toastr.error($translate.instant("error"));
		});
		
		
	}
	
	
	
	
	
	
	
});