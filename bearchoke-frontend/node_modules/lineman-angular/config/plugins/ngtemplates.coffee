module.exports = (lineman) ->

  config:
    loadNpmTasks: lineman.config.application.loadNpmTasks.concat("grunt-angular-templates")

    removeTasks:
      common: lineman.config.application.removeTasks.common.concat("handlebars", "jst")

    prependTasks:
      common: lineman.config.application.prependTasks.common.concat("ngtemplates")

    ngtemplates:
      app:
        options:
          base: "app/templates"
        src: "app/templates/**/*.html"
        dest: "<%= files.ngtemplates.dest %>"

    watch:
      ngtemplates:
        files: "app/templates/**/*.html",
        tasks: ["ngtemplates", "concat_sourcemap:js"]

  files:
    ngtemplates:
      dest: "generated/angular/template-cache.js"

    template:
      generated: "<%= files.ngtemplates.dest %>"
