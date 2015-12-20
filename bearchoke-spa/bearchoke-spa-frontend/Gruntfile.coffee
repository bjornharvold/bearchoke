#global module:false
module.exports = (grunt) ->
  require("./config/lineman").config.grunt.run grunt
  return