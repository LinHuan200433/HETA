// 项目信息
organization := "xxx"
version := "0.0.1"
name := "CGRA-MG"

// 设置固定的 Scala 版本
scalaVersion := "2.13.14"

// 编译器选项
scalacOptions ++= Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
    "-Ymacro-annotations",
    // "-Xsource:2.11" // 根据需要保留或移除
)

// Java 编译器选项
javacOptions ++= Seq(
  "-source", "21",
  "-target", "21"
)

// 依赖管理
val chiselVersion = "6.5.0"       // 请根据需要确认最新版本
val testersVersion = "6.0.0"      // 请根据需要确认最新版本
val jacksonVersion = "2.18.1"
libraryDependencies ++= Seq(
  "org.chipsalliance" %% "chisel" % chiselVersion,
  "edu.berkeley.cs" %% "chiseltest" % testersVersion % Test,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
)
dependencyOverrides += "org.scala-lang" % "scala-library" % "2.13.14"
addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full)
// 仓库设置
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

// 测试设置
logBuffered in Test := false
parallelExecution in Test := false

// 其他设置
trapExit := false
