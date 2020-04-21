enablePlugins(GatlingPlugin)

name := "test"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.3.1" % "test,it"

libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "3.3.1" % "test,it"