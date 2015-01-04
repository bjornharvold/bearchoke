module.exports = (grunt) ->
  grunt.initConfig(JSON.parse(grunt.option("config" || {})))