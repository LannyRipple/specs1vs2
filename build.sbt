name := "eg"

organization := "me.ljr"

version := "1.0-SNAPSHOT"

scalaVersion := "2.9.2"

scalacOptions ++= Seq("-deprecation", "-unchecked")

resolvers ++= Seq(
  "Cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= {
  object V {
    val hadoop = "0.20.2-cdh3u3"
  }
  Seq(
    "org.apache.hadoop" % "hadoop-core" % V.hadoop,
    "org.apache.hadoop" % "hadoop-test" % V.hadoop % "test",
    "junit" % "junit" % "4.10" % "test",
    "org.specs2" %% "specs2" % "1.12-SNAPSHOT" % "test"
  )
}
