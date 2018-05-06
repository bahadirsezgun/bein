ptBossApp.controller('MailController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.subject="";
	
	$scope.init=function(){
		$('.summernote').summernote({height: 350,focus: true,disableDragAndDrop: false});

       
	}
	
	$scope.save=function(){
		 var sHTML = $('.summernote').code();
		 	var mailObj=new Object();
			mailObj.toPerson="all";
			mailObj.subject=$scope.subject;
			mailObj.content=" ";//$scope.sendMailPerson+", "+$scope.mailContent;
			mailObj.htmlContent=sHTML;
			
			
			$http({
				method:'POST',
				  url: "/bein/mail/sendMarketingMail",
				  data:angular.toJson(mailObj),
				}).then(function successCallback(response) {
					toastr.success($translate.instant("success"));
					
			}, function errorCallback(response) {
				toastr.error($translate.instant("error"));
			});
	}
	
	$scope.sendSelf=function(){
		 var sHTML = $('.summernote').code();
		 	var mailObj=new Object();
		 	
			mailObj.toPerson="self";
			mailObj.subject=$scope.subject;
			mailObj.content=" ";//$scope.sendMailPerson+", "+$scope.mailContent;
			mailObj.htmlContent=sHTML;
			
			
			$http({
				method:'POST',
				  url: "/bein/mail/sendMarketingMail",
				  data:angular.toJson(mailObj),
				}).then(function successCallback(response) {
					toastr.success($translate.instant("success"));
					
			}, function errorCallback(response) {
				toastr.error($translate.instant("error"));
			});
	}
	
});