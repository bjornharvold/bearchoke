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
      "../bearchoke-spa-shared/vendor/js/mixpanel-2/mixpanel.js",
      "../bearchoke-spa-shared/vendor/js/jquery-2.1.4/jquery-2.1.4.js",
      "../bearchoke-spa-shared/vendor/js/angular-1.4.7/angular.js",
      "../bearchoke-spa-shared/vendor/js/lodash-3.1.0/lodash.js",
      "../bearchoke-spa-shared/vendor/js/stomp-1.7.1/stomp.js",
      "../bearchoke-spa-shared/vendor/js/messageformat-0.1.7/messageformat.js",
      "../bearchoke-spa-shared/vendor/js/angular-translate-2.6.0/angular-translate.js",
      "../bearchoke-spa-shared/vendor/js/sweet-alert-0.4.3/sweet-alert.js",
      "../bearchoke-spa-shared/vendor/js/slick-1.5.6/slick.js",
      "../bearchoke-spa-shared/vendor/js/**/*.js"
      "vendor/js/**/*.js"
    ],
    app: [
      "app/js/app.js",
      "../bearchoke-spa-shared/app/js/**/*.js",
      "app/js/**/*.js"
    ]

# for some reason this is not working. had to put it in application.coffee instead
# doesn't like parent directories
  webfonts:
    vendor: "vendor/fonts/**/*.*"
    root: "fonts"

  img:
    app: "app/img/**/*.*"
    vendor: "vendor/img/**/*.*"
    root: "img"

  css:
    vendor: [
      "../bearchoke-spa-shared/vendor/css/**/*.css",
      "vendor/css/**/*.css"
    ]
    app: "app/css/**/*.css"
    concatenated: "generated/css/app.css"
    minified: "dist/css/app.css"
    minifiedWebRelative: "css/app.css"

  less:
    vendor: [
      "../bearchoke-spa-shared/vendor/css/**/*.less",
      "vendor/css/**/*.less"
    ]
    main: "app/css/main.less"
    app: "app/css/**/*.less"
    generated: "generated/css/app.less.css"
