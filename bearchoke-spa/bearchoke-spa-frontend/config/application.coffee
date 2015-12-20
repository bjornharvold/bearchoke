# Exports a function which returns an object that overrides the default &
#    plugin grunt configuration object.
# 
#  You can familiarize yourself with Lineman's defaults by checking out:
# 
#    - https://github.com/linemanjs/lineman/blob/master/config/application.coffee
#    - https://github.com/linemanjs/lineman/blob/master/config/plugins
# 
#  You can also ask Lineman's about config from the command line:
# 
#    $ lineman config #=> to print the entire config
#    $ lineman config concat_sourcemap.js #=> to see the JS config for the concat task.
# 
module.exports = (lineman) ->

# configure lineman to load additional angular related npm tasks
  loadNpmTasks: [
    "grunt-contrib-less"
    "grunt-contrib-uglify"
    "grunt-angular-templates"
    "grunt-ng-annotate"
  ]

# we don't use the lineman default handlebars, and jst tasks by default
# html5push state simulation
  server:
    pushState: true

# have to copy over environment config file before actually copying resources
  appTasks:
    common: [
      "coffee",
      "jshint"
    ],
    dev: [
      "concat_sourcemap",
      "copy:dev",
      "images:dev",
      "webfonts:dev",
      "pages:dev",
      "server",
      "watch"
    ],
    dist: [
      "concat_sourcemap",
      "uglify",
      "cssmin",
      "copy:dist",
      "images:dist",
      "webfonts:dist",
      "pages:dist"
    ]

  removeTasks:
    common: ["handlebars", "jst"]

  copy:
    dev:
      files: [
        {
          expand: true
          cwd: "app/"
          src: "json/**/*.*"
          dest: "generated/"
        },
        {
          expand: true
          cwd: "../bearchoke-spa-shared/vendor/"
          src: "fonts/**/*.*"
          dest: "generated/"
        },
        {
          expand: true
          cwd: "../bearchoke-spa-shared/vendor/"
          src: "img/**/*.*"
          dest: "generated/"
        },
        {
          expand: true
          cwd: "../bearchoke-spa-shared/app/"
          src: "img/**/*.*"
          dest: "generated/"
        }
      ]

    dist:
      files: [
        {
          expand: true
          cwd: "app/"
          src: "json/**/*.*"
          dest: "dist/"
        },
        {
          expand: true
          cwd: "../bearchoke-spa-shared/vendor/"
          src: "fonts/**/*.*"
          dest: "dist/"
        },
        {
          expand: true
          cwd: "../bearchoke-spa-shared/vendor/"
          src: "img/**/*.*"
          dest: "dist/"
        },
        {
          expand: true
          cwd: "../bearchoke-spa-shared/app/"
          src: "img/**/*.*"
          dest: "dist/"
        }
      ]

  ngtemplates:
    app:
      cwd: "app/templates"
      src: "**/*.html"
      dest: "<%= files.ngtemplates.dest %>"

  watch:
    fonts:
      files: [
        "../bearchoke-spa-shared/vendor/fonts/**/*.*",
        "vendor/fonts/**/*.*"
      ]
      tasks: "copy:dev"

    images:
      files: [
        "../bearchoke-spa-shared/vendor/img/**/*.*",
        "../bearchoke-spa-shared/app/img/**/*.*",
        "app/img/**/*.*",
        "vendor/img/**/*.*"
      ]
      tasks: "copy:dev"

    json:
      files: "app/json/**/*.*"
      tasks: "copy:dev"

  uglify:
    options:
      mangle: false

  jshint:
    options:
      curly: true
      eqeqeq: true
      latedef: true
      newcap: false
      noarg: true
      boss: true
      eqnull: true
      sub: true
      browser: true
