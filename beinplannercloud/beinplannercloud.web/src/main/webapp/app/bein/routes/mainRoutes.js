	ptBossApp.config(['$routeProvider',
	 function($routeProvider) {
     $routeProvider.
	     when('/member', {
	         templateUrl: '/bein/member/profile.html'
	     }).
	     when('/member/find', {
	         templateUrl: '/bein/member/find.html'
	     }).
	     when('/member/findAll', {
	         templateUrl: '/bein/member/findAll.html'
	     }).
	     when('/member/list', {
	         templateUrl: '/bein/member/list.html'
	     }).
	     when('/staff/sign', {
	         templateUrl: '/bein/sign/staffTracking.html'
	     }).
	     when('/member/profile/:userId', {
	         templateUrl: '/bein/member/profile.html'
	     }).
	     when('/member/create', {
	         templateUrl: '/bein/member/profile.html'
	     }).
	     when('/member/potential/create', {
	         templateUrl: '/bein/member/potentialuser.html'
	     }).
	     when('/member/potential/edit/:userId', {
	         templateUrl: '/bein/member/potentialuser.html'
	     })
	     .when('/member/special/dates', {
	         templateUrl: '/bein/member/specialDates.html'
	     }).
	     when('/member/potential', {
	         templateUrl: '/bein/member/potentialuserfind.html'
	     }).
	     when('/staff/list', {
	         templateUrl: '/bein/staff/list.html'
	     }).
	     when('/staff/profile/:userId', {
	         templateUrl: '/bein/staff/profile.html'
	     }).
	     when('/staff/profile', {
	         templateUrl: '/bein/staff/profile.html'
	     }).
	     when('/staff/plan', {
	         templateUrl: '/bein/staff/staffplan.html'
	     }).
	     when('/myprofile', {
	         templateUrl: '/bein/staff/myprofile.html'
	     }).
	     when('/forgot', {
	         templateUrl: '/bein/staff/forgot.html'
	     }).
	     when('/changePassword', {
	         templateUrl: '/bein/staff/change.html'
	     }).
	     when('/member/upload', {
             templateUrl: '/bein/upload/upload.html'
         }).
         when('/dashboard', {
             templateUrl: '/bein/dashboard/index.html'
         }).
         when('/dashboard/finance', {
             templateUrl: '/bein/dashboard/dash_finance.html'
         }).
         when('/dashboard/special', {
             templateUrl: '/bein/dashboard/dash_special.html'
         }).
         when('/dashboard/classes', {
             templateUrl: '/bein/dashboard/dash_classes.html'
         }).
         when('/firm', {
             templateUrl: '/bein/definitions/firm.html'
         }).
         when('/rolemenu', {
             templateUrl: '/bein/definitions/rolemenu.html'
         }).
         when('/calDefTimes', {
             templateUrl: '/bein/definitions/calendartimes.html'
         }).
         when('/program/pprogram', {
             templateUrl: '/bein/programs/personalprog.html'
         }).
         when('/program/cprogram', {
             templateUrl: '/bein/programs/classprog.html'
         }).
         when('/program/mprogram', {
             templateUrl: '/bein/programs/membershipprog.html'
         }).
         when('/definition/levelInfo', {
	         templateUrl: '/bein/definitions/levelInfo.html'
	     }).
	     when('/settings/rules', {
             templateUrl: '/bein/settings/rules.html'
         }).
         when('/settings/global', {
             templateUrl: '/bein/settings/globals.html'
         }).
         when('/settings/database', {
             templateUrl: '/bein/settings/dbhosting.html'
         }).
         when('/settings/mail', {
             templateUrl: '/bein/settings/mailhosting.html'
         }).
        when('/packetsale/sale', {
             templateUrl: '/bein/packetsale/new_sale.html'
         }).
         when('/packetsale/saletouser/:userId', {
             templateUrl: '/bein/packetsale/new_saletouser.html'
         }).
          when('/packetpayment/confirm', {
             templateUrl: '/bein/packetpayment/payment_confirm.html'
         }).
         when('/packetpayment/leftPayment', {
             templateUrl: '/bein/packetpayment/leftPayments.html'
         }).
         when('/packetpayment/payment/find', {
             templateUrl: '/bein/packetpayment/paymentFindMember.html'
         }).
         when('/packetpayment/payment/list', {
             templateUrl: '/bein/packetpayment/paymentListMember.html'
         }).
         when('/packetpayment/payment', {
             templateUrl: '/bein/packetpayment/payment.html'
         }).
         when('/schedule/membership', {
             templateUrl: '/bein/schedule/membership/booking.html'
         }).
         when('/schedule/search', {
             templateUrl: '/bein/schedule/plan/list/listsearch.html'
         }).
         when('/schedule/choose', {
             templateUrl: '/bein/schedule/plan/choose.html'
         }).
         when('/schedule/deleteAll', {
             templateUrl: '/bein/schedule/plan/util/resultAllDelete.html'
         }).
         when('/schedule/personal', {
             templateUrl: '/bein/schedule/plan/personal/booking.html'
         }).
         when('/schedule/class', {
             templateUrl: '/bein/schedule/plan/class/booking.html'
         }).
         when('/schedule/plan/calendar', {
             templateUrl: '/bein/booking/calendar/booking.html'
         }).
         /*
         when('/schedule/plan/class/result', {
             templateUrl: '/bein/schedule/plan/class/result/result.html'
         }).
         when('/schedule/plan/personal/result', {
             templateUrl: '/bein/schedule/plan/personal/result/result.html'
         }).
         when('/schedule/plan/calendar', {
             templateUrl: '/bein/schedule/plan/calendar/ptcalendar.html'
         }).
         when('/schedule/plan/calendar/all', {
             templateUrl: '/bein/schedule/plan/calendar/ptcalendar_allweek.html'
         }).
         when('/schedule/plan/calendar/dash', {
             templateUrl: '/bein/schedule/plan/calendar/ptcalendardash.html'
         }).
         */
         when('/income', {
             templateUrl: '/bein/income/pastIncome.html'
         }).
         when('/expense', {
             templateUrl: '/bein/income/ptexpenses.html'
         }).
         when('/bonus', {
             templateUrl: '/bein/bonus/choose.html'
         }).
         when('/bonus/lock', {
             templateUrl: '/bein/bonus/lock/bonuslock.html'
         }).
         when('/bonus/personal', {
             templateUrl: '/bein/bonus/personal/pbonus.html'
         }).
         when('/bonus/personal/payment/detail', {
             templateUrl: '/bein/bonus/personal/pbonusdetail'
         }).
         when('/bonus/class', {
             templateUrl: '/bein/bonus/class/cbonus.html'
         }).
         otherwise({
         redirectTo: '/dashboard'
         });
     
     
     
      }]);	