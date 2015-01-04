module.exports = (lineman) ->

  config:
    loadNpmTasks: lineman.config.application.loadNpmTasks.concat("grunt-ng-annotate")

    prependTasks:
      dist: lineman.config.application.prependTasks.dist.concat("ngAnnotate")

    ngAnnotate:
      js:
        src: "<%= files.js.concatenated %>"
        dest: "<%= files.js.concatenated %>"
