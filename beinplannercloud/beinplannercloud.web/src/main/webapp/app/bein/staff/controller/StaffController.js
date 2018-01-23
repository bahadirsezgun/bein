ptBossApp.controller('StaffController', function($scope,$http,$routeParams,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.staff;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	$scope.ptCurrency;
	
	$scope.userId;
	$scope.profileUrl="profile.png";
	
	
	
	
	$scope.bonusTypeP="0";
	$scope.bonusTypeC="0";
	
	
	
	
	
	$scope.init = function(){
		
		
		commonService.pageName=$translate.instant("staffCreatePage");
		commonService.pageComment=$translate.instant("staffCreatePageComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		$scope.userId=$routeParams.userId;
		if($scope.userId!=null){
			findUserById($scope.userId).then(function(){
				$scope.avatars=avatarFemale;
				setStaff();
			});
		}else{
			$scope.staff=new Object();
			$scope.staff.userGender="0";
			$scope.staff.createTime=new Date();
			$scope.staff.userBirthday=new Date();
			$scope.staff.staffId="0";
			$scope.staff.userType="3";
			$scope.staff.staffStatu="0"
			$scope.staff.bonusTypeP="0";
			$scope.staff.bonusTypeC="0";
		}
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
		});
		
	}
	
	
	$scope.bonusPage="";
	$scope.bonusClassPage="";
	
	$scope.bonusCTypeChange=function(){
		
		if($scope.bonusTypeC!="0"){
			
			
			swal({
	            title: $translate.instant("areYouSureToChange"),
	            text: $translate.instant("changeBonusStateComment"),
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: $translate.instant("yes"),
	            cancelButtonText: $translate.instant("no"),
	            closeOnConfirm: true,
	            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	      $http({
							  method:'POST',
							  url: "/bein/staff/setClassBonusType/"+$scope.userId+"/"+$scope.bonusTypeC,
							}).then(function successCallback(response) {
								var res=response.data;
								
								if(res.resultStatu=="success"){
									if($scope.bonusTypeC=="1"){
										$scope.bonusClassPage="/bein/staff/bonus/classRateBonus.html";
									}else if($scope.bonusTypeC=="2"){
										$scope.bonusClassPage="/bein/staff/bonus/classStaticBonus.html";
									}else if($scope.bonusTypeC=="3"){
										$scope.bonusClassPage="/bein/staff/bonus/classStaticRateBonus.html";
									}
									toastr.success($translate.instant("success"));
									$scope.staff.bonusTypeC=$scope.bonusTypeC;
								}else{
									toastr.error($translate.instant(res.resultMessage));
								}
								
								
							}, function errorCallback(response) {
								$location.path("/login");
							});
	            }else {
	            	$scope.bonusTypeC=$scope.staff.bonusTypeC;
	               // swal($translate.instant("changeCanceled"), $translate.instant("changeBonusTypeCanceled"));
	            }
	            });
			}
	}
	
	$scope.bonusCTapShow=function(){
	    if($scope.bonusTypeC=="1"){
			$scope.bonusClassPage="/bein/staff/bonus/classRateBonus.html";
		}else if($scope.bonusTypeC=="2"){
			$scope.bonusClassPage="/bein/staff/bonus/classStaticBonus.html";
		}else if($scope.bonusTypeC=="3"){
			$scope.bonusClassPage="/bein/staff/bonus/classStaticRateBonus.html";
		}
	};
	
	
	$scope.bonusPTapShow=function(){
	    if($scope.bonusTypeP=="1"){
			$scope.bonusPage="/bein/staff/bonus/personalRateBonus.html";
		}else if($scope.bonusTypeP=="2"){
			$scope.bonusPage="/bein/staff/bonus/personalStaticBonus.html";
		}else if($scope.bonusTypeP=="3"){
			$scope.bonusPage="/bein/staff/bonus/personalStaticRateBonus.html";
		}
	};
	
	
	
	$scope.bonusPTypeChange=function(){
		
		
		if($scope.bonusTypeP!="0"){
		
		
		swal({
            title: $translate.instant("areYouSureToChange"),
            text: $translate.instant("changeBonusStateComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yes"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
            
					  
					   $http({
						  method:'POST',
						  url: "/bein/staff/setPersonalBonusType/"+$scope.userId+"/"+$scope.bonusTypeP,
						}).then(function successCallback(response) {
							var res=response.data;
							
							if(res.resultStatu=="success"){
								if($scope.bonusTypeP=="1"){
									$scope.bonusPage="/bein/staff/bonus/personalRateBonus.html";
								}else if($scope.bonusTypeP=="2"){
									$scope.bonusPage="/bein/staff/bonus/personalStaticBonus.html";
								}else if($scope.bonusTypeP=="3"){
									$scope.bonusPage="/bein/staff/bonus/personalStaticRateBonus.html";
								}
								toastr.success($translate.instant("success"));
								
								$scope.staff.bonusTypeP=$scope.bonusTypeP;
								
							}else{
								toastr.error($translate.instant(res.resultMessage));
							}
							
							
						}, function errorCallback(response) {
							$location.path("/login");
						});
            	
            	
            	
            	
            	
            }else {
            	$scope.bonusTypeP=$scope.staff.bonusTypeP;
               // swal($translate.instant("changeCanceled"), $translate.instant("changeBonusTypeCanceled"));
            }
            });
		
		
		}
	}

	
	
	$scope.genderChange=function(){
		if($scope.staff.userGender=="1"){
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
	
	
	
	
	var findUserById=function(){
		
		return $http({
			method:'POST',
			  url: "/bein/staff/findById/"+$scope.userId,
			}).then(function successCallback(response) {
				if(response!=null){
					$scope.staff=response.data.resultObj;
					
					$scope.bonusTypeC=""+$scope.staff.bonusTypeC;
					$scope.bonusTypeP=""+$scope.staff.bonusTypeP;
					
					
					if($scope.staff.userType=="3"){
						$scope.showBonus=1;
					}else{
						$scope.showBonus=0;
					}
					
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	
	var setStaff=function(){
		$scope.staff.userGender=""+$scope.staff.userGender;
		$scope.staff.createTime=new Date($scope.staff.createTime);
		$scope.staff.userBirthday=new Date($scope.staff.userBirthday);
		$scope.staff.staffId=""+$scope.staff.staffId;
		$scope.staff.userType=""+$scope.staff.userType;
		$scope.staff.userGender=""+$scope.staff.userGender;
		$scope.staff.staffId=""+$scope.staff.staffId;
		$scope.staff.staffStatu=""+$scope.staff.staffStatu;
		$scope.staff.createTime=new Date($scope.staff.createTime);
		
		$scope.staff.bonusTypeP=""+$scope.staff.bonusTypeP;
		$scope.staff.bonusTypeC=""+$scope.staff.bonusTypeC;
		
		
		var defaultUrlPath=$scope.staff.profileUrl;
		if($scope.staff.profileUrl==null){
			if($scope.staff.userGender==0){
				defaultUrlPath="profilem.png";
			}else{
				defaultUrlPath="profile.png";
			}
		}
		
		$scope.profileUrl=defaultUrlPath;
		
		if($scope.staff.userGender=="1"){
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
	
	
	
	$scope.userTypeChange = function(){
		if($scope.staff.userType=="3"){
			$scope.showBonus=1;
		}else{
			$scope.showBonus=0;
		}
	};
	
	
	
	
	
	function controlElements(){
		if($scope.staff.userEmail==""){
			toastr.error($translate.instant('userEmailNotFound'));
			return false;
		}
		if($scope.staff.userName==""){
			toastr.error($translate.instant('userNameNotFound'));
			return false;
		}
		if($scope.staff.userSurname==""){
			toastr.error($translate.instant('userSurnameNotFound'));
			return false;
		}
		if($scope.staff.userGender=="0"){
			toastr.error($translate.instant('userGenderNotSelected'));
			return false;
		}
		
		if($scope.staff.userBirthday==""){
			toastr.error($translate.instant('userBirtdateNotFound'));
			return false;
		}
		
		
		
		if($scope.staff.userGsm==""){
			toastr.error($translate.instant('userGsmNotFound'));
			return false;
		}
		return true;
	}
	
	
	$scope.statuChange=function(){
		if($scope.userId!=0){
		 var statuUrl="/bein/staff/";
		 if($scope.staffStatu=="1"){
			 statuUrl+="setStaffToPassiveMode/"+$scope.userId;
		 }else{
			 statuUrl+="setStaffToActiveMode/"+$scope.userId;
		 }
		
		 $http({
			  method:'POST',
			  url: statuUrl,
			}).then(function successCallback(response) {
				var res=response.data;
				if(res.resultStatu=="1"){
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
				
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
		}
		
	}
	
	
	$scope.maskWorkPhone="(999) 999-9999 ext. 9999";
	$scope.maskGsm="(999) 999-9999";
	
	$scope.createStaff =function(){
		
		   if(controlElements()){
			   $http({
				  method:'POST',
				  url: "/bein/staff/create",
				  data: angular.toJson($scope.staff),
				}).then(function successCallback(response) {
					var res=response.data;
					if(res.resultStatu=="success"){
						$scope.staff=res.resultObj;
						setStaff();
						toastr.success($translate.instant("success"));
					}else{
						toastr.error($translate.instant(res.resultMessage));
					}
				}, function errorCallback(response) {
					$location.path("/login");
				});
			}
	};
	
	
	/***********************************************************************************/
	
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
		        {"url":"/homerlib/images/a23.PNG","value":"23.PNG"},
		        {"url":"/homerlib/images/a24.PNG","value":"a24.PNG"},
		        {"url":"/homerlib/images/a25.PNG","value":"a25.PNG"},
		        {"url":"/homerlib/images/f1.jpg","value":"f1.jpg"},
		        {"url":"/homerlib/images/f2.jpg","value":"f2.jpg"},
		        {"url":"/homerlib/images/f3.jpg","value":"f3.jpg"},
		        {"url":"/homerlib/images/f4.jpg","value":"f4.jpg"},
		        {"url":"/homerlib/images/f5.jpg","value":"f5.jpg"},
		        {"url":"/homerlib/images/f6.jpg","value":"f6.jpg"},
		        {"url":"/homerlib/images/f7.jpg","value":"f7.jpg"}];
		
		function createAvatarUrlEvent(){
		$('.i-checks').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.staff.profileUrl=event.target.value;
			$scope.createStaff();
		   
		}
		});
		}
		
		
		$scope.genderChange=function(){
		if($scope.staff.userGender=="1"){
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
	
	
});
