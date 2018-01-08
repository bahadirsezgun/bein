ptBossApp.controller('CreateFastUserController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.maskGsm="(999) 999-9999";
	
	$scope.userId=0;
	$scope.userGsm;
	$scope.userGender="1";
	
	
	$scope.initCFUC=function(){
		
	}
	
	
	$scope.newMemberCreate=function(){
		$scope.userId=0;
		$scope.userGsm="";
		$scope.userGender="1";
		$scope.userSurname="";
		$scope.userName="";
	}
	
	$scope.createMember =function(){
		   
		  var frmDatum = {"userName":$scope.userName,
			"userSurname":$scope.userSurname,
			"userGsm":$scope.userGsm,
			"userType":globals.USER_TYPE_MEMBER,
			"userGender":$scope.userGender,
			"userId":$scope.userId,
			"userBirthdayStr":"",
			"userEmail":"",
			"userSsn":"",
			"userAddress":"",
			"userPhone":"",
			"userComment":""
			}; 
		  
		   $.ajax({
			  type:'POST',
			  url: "../pt/ptusers/create",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu=="1"){
					$scope.userId=parseInt(res.resultMessage);
					toastr.success($translate.instant("success"));
					$scope.$apply();
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				
			}).fail  (function(jqXHR, textStatus, errorThrown) 
			{ 
			  if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/beincloud/lock.html");
			})
			
			
		
	};
	
	
	
});