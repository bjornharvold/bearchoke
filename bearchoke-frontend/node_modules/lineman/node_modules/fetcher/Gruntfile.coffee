#global module:false

module.exports = (grunt) ->
  grunt.loadNpmTasks('grunt-jasmine-bundle')
  grunt.registerTask("default", ["spec"])
