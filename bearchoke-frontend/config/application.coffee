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
    "grunt-angular-templates"
    "grunt-replace"
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
      "jshint",
      "handlebars",
      "jst",
      "replace:development",
      "concat_sourcemap",
      "copy:dev",
      "images:dev",
      "webfonts:dev",
      "pages:dev"
    ],
    dist: [
      "replace:dist",
      "uglify",
      "cssmin",
      "copy:dist",
      "images:dist",
      "webfonts:dist",
      "pages:dist"
    ]

  removeTasks:
    common: ["handlebars", "jst"]

  appendTasks:
    dev: [
      "replace:development"
    ]

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
            cwd: "vendor/"
            src: "fonts/**/*.*"
            dest: "generated/"
          },
          {
            expand: true
            cwd: "app/"
            src: "fonts/**/*.*"
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
            cwd: "vendor/"
            src: "fonts/**/*.*"
            dest: "dist/"
          },
          {
            expand: true
            cwd: "app/"
            src: "fonts/**/*.*"
            dest: "dist/"
          }
        ]

  replace:
    development:
      options:
        patterns: [json: lineman.grunt.file.readJSON("./config/environments/development.json")]

      files: [
        expand: true
        flatten: true
        src: ["./config/environment_constants.js"]
        dest: "app/js/constants/"
      ]
    dist:
      options:
        patterns: [json: lineman.grunt.file.readJSON("./config/environments/cloudfoundry.json")]

      files: [
        expand: true
        flatten: true
        src: ["./config/environment_constants.js"]
        dest: "app/js/constants/"
      ]

  ngtemplates:
    app:
      cwd: "app/templates"
      src: "**/*.html"
      dest: "<%= files.ngtemplates.dest %>"

  watch:
    fonts:
      files: ["app/fonts/**/*.*", "vendor/fonts/**/*.*"]
      tasks: "copy:dev"

    json:
      files: "app/json/**/*.*"
      tasks: "copy:dev"

    environment:
      files: ["./config/environment_constants.js", "./config/environments/development.json"]
      tasks: "replace:development"
