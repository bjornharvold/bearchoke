angular.module('app').factory('HelloWorldFactory', function ($timeout) {

	var hws = new com_bearchoke_platform_webservice_HelloWorldService();
	hws.url = 'http://dev.bearchoke.com:8080/services/HelloWorldService';

	//public methods
	var self = {

		hello: function ( success, error, name ) {
			$timeout(function(){
				var wrapperObj = new com_bearchoke_platform_webservice_hello();
				wrapperObj.setName(name);
				hws.hello(success, error, wrapperObj);
			}, 200);
		}

	};
	
	return self;
});

