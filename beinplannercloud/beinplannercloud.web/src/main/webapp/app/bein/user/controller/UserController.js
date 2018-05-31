ptBossApp.controller('UserController', function($scope,$translate,parameterService,$location,homerService,commonService,globals,$routeParams,$http) {
	
	$scope.member;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	$scope.ptCurrency;
	$scope.userTestPage="";
	
	$scope.staffs;
	$scope.avatars;
	
	$scope.profileUrl="profile.png";
	
	$scope.packetPaymentPage=""
	$scope.makePayment=false;
	$scope.paymentProfileShow=false;
	
	$scope.saleToUser =function(){
		if($scope.member.userId!=null)
			$location.path('/packetsale/saletouser/'+$scope.member.userId);
		else
			$location.path('/packetsale/sale');
	}
	
	
	
	$scope.memberReport =function(){
		if($scope.member.userId!=null){
			$location.path('/reports/member/'+$scope.member.userId);
	   }
	}
	
	
	$scope.init = function(){
		commonService.pageName=$translate.instant("memberCreatePage");
		commonService.pageComment=$translate.instant("memberCreatePageComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		findInstructors().then(function(instructor){
			$scope.staffs=instructor;
			
			$scope.userId=$routeParams.userId;
			if($scope.userId!=null){
				findUserById($scope.userId).then(function(){
					$scope.avatars=avatarFemale;
					setMember();
				});
			}else{
				$scope.member=new Object();
				$scope.member.userGender="0";
				$scope.member.createTime=new Date();
				$scope.member.userBirthday=new Date();
				$scope.member.staffId="0";
				$scope.member.userType=globals.USER_TYPE_MEMBER;
			}
		});
		
		
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
		});
	}
	
	$scope.newMember =function(){
		$scope.member=new Object();
		$scope.member.userGender="0";
		$scope.member.createTime=new Date();
		$scope.member.userBirthday=new Date();
		$scope.member.staffId="0";
		$scope.member.userType=globals.USER_TYPE_MEMBER;
	}
	
	$scope.createMember =function(){
		if(controlElements()){
		   $http({
			  method:'POST',
			  url: "/bein/member/create",
			  data: angular.toJson($scope.member),
			}).then(function successCallback(response) {
				var res=response.data;
				if(res.resultStatu=="success"){
					$scope.member=res.resultObj;
					setMember();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
		}
	};
	
	
	var setMember=function(){
		$scope.member.userGender=""+$scope.member.userGender;
		$scope.member.createTime=new Date($scope.member.createTime);
		$scope.member.userBirthday=new Date($scope.member.userBirthday);
		$scope.member.staffId=""+$scope.member.staffId;
		$scope.member.userType=""+$scope.member.userType;
		$scope.member.userGender=""+$scope.member.userGender;
		$scope.member.staffId=""+$scope.member.staffId;
		$scope.member.staffStatu=""+$scope.member.staffStatu;
		$scope.member.createTime=new Date($scope.member.createTime);
		
		var defaultUrlPath=$scope.member.profileUrl;
		if($scope.member.profileUrl==null){
			if($scope.member.userGender==0){
				defaultUrlPath="profilem.png";
			}else{
				defaultUrlPath="profile.png";
			}
		}
		
		$scope.profileUrl=defaultUrlPath+"?"+new Date();
		
		if($scope.member.userGender=="1"){
			$scope.avatars=avatarFemale;
		}else{
			$scope.avatars=avatarMale;
		}
		setTimeout(function(){
			$('.i-checks').iCheck({
		        checkboxClass: 'icheckbox_square-green',
		        radioClass: 'iradio_square-green'
		    });
			createAvatarUrlEvent();
		},1000);
	}
	
	function findInstructors(){
		   return  $http({
			  method: 'POST',
			  url: "/bein/staff/findAllSchedulerStaff"
			}).then(function successCallback(response) {
				return response.data.resultObj;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	  }
	
	
	
	var findUserById=function(){
		
		return $http({
			method:'POST',
			  url: "/bein/member/findById/"+$scope.userId,
			}).then(function successCallback(response) {
				if(response!=null){
					$scope.member=response.data.resultObj;
					
				}
			});
	}
	
	function controlElements(){
		if($scope.member.userEmail==""){
			toastr.error($translate.instant('userEmailNotFound'));
			return false;
		}
		if($scope.member.userName==""){
			toastr.error($translate.instant('userNameNotFound'));
			return false;
		}
		if($scope.member.userSurname==""){
			toastr.error($translate.instant('userSurnameNotFound'));
			return false;
		}
		if($scope.member.userGender=="0"){
			toastr.error($translate.instant('userGenderNotSelected'));
			return false;
		}
		
		if($scope.member.userBirthdayStr==""){
			toastr.error($translate.instant('userBirtdateNotFound'));
			return false;
		}
		
		
		
		if($scope.member.userGsm==""){
			toastr.error($translate.instant('userGsmNotFound'));
			return false;
		}
		return true;
	}
	
	
	
	
	
	$scope.maskWorkPhone="(999) 999-9999 ext. 9999";
	$scope.maskGsm="(999) 999-9999";
	
	function createAvatarUrlEvent(){
		$('.i-checks').on('ifChanged', function(event) {
			if(event.target.checked){
				$scope.member.profileUrl=event.target.value;
				$scope.createMember();
			   
			}
		});
	}
	
	
	$scope.genderChange=function(){
		if($scope.member.userGender=="1"){
			$scope.avatars=avatarFemale;
		}else{
			$scope.avatars=avatarMale;
		}
		setTimeout(function(){
			$('.i-checks').iCheck({
		        checkboxClass: 'icheckbox_square-green',
		        radioClass: 'iradio_square-green'
		    });
			createAvatarUrlEvent();
		},1000);
	}
	
	
	var avatarMale=[{"url":"/homerlib/images/a1.PNG","value":"a1.PNG"},
        {"url":"/homerlib/images/a11.PNG","value":"a11.PNG"},
        {"url":"/homerlib/images/a12.PNG","value":"a12.PNG"},
        {"url":"/homerlib/images/a13.PNG","value":"a13.PNG"},
        {"url":"/homerlib/images/a14.PNG","value":"a14.PNG"},
        {"url":"/homerlib/images/a15.PNG","value":"a15.PNG"},
        {"url":"/homerlib/images/a16.PNG","value":"a16.PNG"},
        {"url":"/homerlib/images/a18.PNG","value":"a18.PNG"},
        {"url":"/homerlib/images/a26.PNG","value":"a26.PNG"},
        {"url":"/homerlib/images/a27.PNG","value":"a27.PNG"},
        {"url":"/homerlib/images/a28.PNG","value":"a28.PNG"},
        {"url":"/homerlib/images/a29.PNG","value":"a29.PNG"},
        {"url":"/homerlib/images/a30.PNG","value":"a30.PNG"},
        {"url":"/homerlib/images/a31.PNG","value":"a31.PNG"},
        {"url":"/homerlib/images/a32.PNG","value":"a32.PNG"},
        {"url":"/homerlib/images/m1.jpg","value":"m1.jpg"},
        {"url":"/homerlib/images/m2.jpg","value":"m2.jpg"},
        {"url":"/homerlib/images/m3.jpg","value":"m3.jpg"}];


	var avatarFemale=[{"url":"/homerlib/images/a2.PNG","value":"a2.PNG"},
	        {"url":"/homerlib/images/a3.PNG","value":"a3.PNG"},
	        {"url":"/homerlib/images/a4.PNG","value":"a4.PNG"},
	        {"url":"/homerlib/images/a5.PNG","value":"a5.PNG"},
	        {"url":"/homerlib/images/a6.PNG","value":"a6.PNG"},
	        {"url":"/homerlib/images/a7.PNG","value":"a7.PNG"},
	        {"url":"/homerlib/images/a8.PNG","value":"a8.PNG"},
	        {"url":"/homerlib/images/a9.PNG","value":"a9.PNG"},
	        {"url":"/homerlib/images/a10.PNG","value":"a10.PNG"},
	        {"url":"/homerlib/images/a17.PNG","value":"a17.PNG"},
	        {"url":"/homerlib/images/a19.PNG","value":"a19.PNG"},
	        {"url":"/homerlib/images/a20.PNG","value":"a20.PNG"},
	        {"url":"/homerlib/images/a21.PNG","value":"a21.PNG"},
	        {"url":"/homerlib/images/a22.PNG","value":"a22.PNG"},
	        {"url":"/homerlib/images/a23.PNG","value":"a23.PNG"},
	        {"url":"/homerlib/images/a24.PNG","value":"a24.PNG"},
	        {"url":"/homerlib/images/a25.PNG","value":"a25.PNG"},
	        {"url":"/homerlib/images/f1.jpg","value":"f1.jpg"},
	        {"url":"/homerlib/images/f2.jpg","value":"f2.jpg"},
	        {"url":"/homerlib/images/f3.jpg","value":"f3.jpg"},
	        {"url":"/homerlib/images/f4.jpg","value":"f4.jpg"},
	        {"url":"/homerlib/images/f5.jpg","value":"f5.jpg"},
	        {"url":"/homerlib/images/f6.jpg","value":"f6.jpg"},
	        {"url":"/homerlib/images/f7.jpg","value":"f7.jpg"}];
	
	
	
	
	$scope.getTestPage=function(){
		$scope.userTestPage="/bein/test/usertest.html";
	}
	

	$scope.getMeasurementPage=function(){
		$scope.userMeasurementPage="/bein/measurement/meas.html";
	}
	
});
