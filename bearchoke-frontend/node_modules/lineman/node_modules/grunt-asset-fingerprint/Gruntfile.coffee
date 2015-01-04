"use strict"
module.exports = (grunt) ->

  grunt.loadNpmTasks "grunt-jasmine-bundle"

  grunt.initConfig
    spec:
      e2e:
        minijasminenode:
          showColors: true
  
  grunt.registerTask "test", ["spec"]
  grunt.registerTask "default", ["test"]
