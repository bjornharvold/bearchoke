# Exports a function which returns an object that overrides the default &
#    plugin file patterns (used widely through the app configuration)
# 
#  To see the default definitions for Lineman's file paths and globs, see:
# 
#    - https://github.com/linemanjs/lineman/blob/master/config/files.coffee
# 
module.exports = (lineman) ->

  #Override file patterns here
  js: 
    vendor: [
      "vendor/js/jquery-2.1.3/jquery-2.1.3.js",
      "vendor/js/angular-1.3.4/angular.js",
      "vendor/js/stomp-1.7.1/stomp.js",
      "vendor/js/**/messageformat.js",
      "vendor/js/angular-translate-2.2.0/angular-translate.min.js",
      "vendor/js/sweet-alert-0.4.3/sweet-alert.js",
      "vendor/js/**/*.js"
    ],
	  app: [
      "app/js/app.js",
      "app/js/**/*.js"
	  ]
