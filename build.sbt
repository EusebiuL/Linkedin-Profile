import sbt._

val Http4sVersion = "0.18.14"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"

lazy val linkedinintegration = (project in file("."))
  .settings(commonSettings)
  .aggregate(server)

lazy val server = project
  .settings(commonSettings)
  .settings(mainClass := Option("lk.server.LinkedInServerApp"))
  .dependsOn(`lk-core`, `lk-json`, `lk-algebra`, `lk-service`, `lk-config`)

lazy val `lk-core` = project.settings(commonSettings)

lazy val `lk-algebra` = project.settings(commonSettings).dependsOn(`lk-config`, `lk-core`).aggregate(`lk-config`, `lk-core`)

lazy val `lk-json` = project.settings(commonSettings).dependsOn(`lk-algebra`)

lazy val `lk-service` = project.settings(commonSettings).dependsOn(`lk-json`, `lk-algebra`).aggregate(`lk-json`, `lk-algebra`)

lazy val `lk-config` = project.settings(commonSettings)

def commonSettings: Seq[Setting[_]] = Seq(
  scalaVersion := "2.12.6",
  libraryDependencies ++= Seq(
    http4sBlazeServer,
    http4sBlazeClient,
    http4sCirce,
    http4sDSL,
    specs2,
    logbackClassic,
    circeGeneric,
    circeLiteral,
    pureConfig,
    bmCore,
    bmcJson,
    linebacker,
    monix,
    log4cats
  )
)

def sbtAssemblySettings: Seq[Setting[_]] = {
  import sbtassembly.MergeStrategy
  import sbtassembly.PathList

  baseAssemblySettings ++
    Seq(
      // Skip tests during while running the assembly task
      test in assembly := {},
      assemblyMergeStrategy in assembly := {
        case PathList("application.conf", _ @_*) => MergeStrategy.concat
        case "application.conf" => MergeStrategy.concat
        case PathList("reference.conf", _ @_*) => MergeStrategy.concat
        case "reference.conf" => MergeStrategy.concat
        case x                => (assemblyMergeStrategy in assembly).value(x)
      },
      //this is to avoid propagation of the assembly task to all subprojects.
      //changing this makes assembly incredibly slow
      aggregate in assembly := false
    )
}

lazy val http4sBlazeServer : ModuleID = "org.http4s" %% "http4s-blaze-server" % Http4sVersion withSources ()
lazy val http4sBlazeClient : ModuleID = "org.http4s" %% "http4s-blaze-client" % Http4sVersion withSources()
lazy val http4sCirce : ModuleID = "org.http4s" %% "http4s-circe" % Http4sVersion withSources ()
lazy val http4sDSL : ModuleID = "org.http4s" %% "http4s-dsl" % Http4sVersion withSources ()

lazy val specs2 :ModuleID = "org.specs2" %% "specs2-core" % Specs2Version % "test" withSources()

lazy val logbackClassic: ModuleID = "ch.qos.logback" % "logback-classic" % LogbackVersion withSources()

lazy val circeGeneric : ModuleID = "io.circe" %% "circe-generic" % "0.9.3" withSources()
lazy val circeLiteral: ModuleID = "io.circe" %% "circe-literal" % "0.9.3" withSources()

lazy val pureConfig : ModuleID = "com.github.pureconfig" %% "pureconfig" % "0.9.1" withSources ()

lazy val bmCore : ModuleID = "com.busymachines" %% "busymachines-commons-core" % "0.3.0-RC8" withSources()

def bmCommons(m: String): ModuleID = "com.busymachines" %% s"busymachines-commons-$m" % "0.3.0-RC8"

lazy val bmcJson:          ModuleID = bmCommons("json")              withSources ()

//https://github.com/ChristopherDavenport/linebacker
lazy val linebacker: ModuleID = "io.chrisdavenport" % "linebacker_2.12" % "0.1.0" withSources ()

//https://github.com/monix/monix
lazy val monix: ModuleID = "io.monix" %% "monix" % "3.0.0-RC1" withSources ()

//https://github.com/ChristopherDavenport/log4cats
lazy val log4cats = "io.chrisdavenport" %% "log4cats-slf4j" % "0.0.6" withSources ()