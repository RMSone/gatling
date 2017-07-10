import io.gatling.build.LicenseKeys._
import io.gatling.build.MavenPublishKeys._
import io.gatling.build.license._

import com.typesafe.sbt.SbtSite.site
import com.typesafe.sbt.site.SphinxSupport.Sphinx
import sbt.Keys._
import sbt._
import sbtunidoc.Plugin.UnidocKeys._
import sbtunidoc.Plugin.{ScalaUnidoc, unidocSettings}

object BuildSettings {

  lazy val basicSettings = Seq(
    license := ApacheV2,
    githubPath := "gatling/gatling",
    projectDevelopers := developers
    // [fl]
    //
    //
    //
    // [fl]
  )

  lazy val gatlingModuleSettings =
    basicSettings ++ scaladocSettings ++ publishSettings

  lazy val noArtifactToPublish =
    publishArtifact in Compile := false

  lazy val publishSettings = {
    import ohnosequences.sbt.SbtS3Resolver.autoImport._

    val repoSuffix = "mvn-repo.miuinsights.com"
    val releaseRepo: s3 = s3(s"releases.$repoSuffix")
    val snapshotRepo: s3 = s3(s"snapshots.$repoSuffix")

    Seq(
      resolvers ++= {
        val releases = s3resolver.value("Releases resolver", releaseRepo).withIvyPatterns
        val snapshots = s3resolver.value("Snapshots resolver", snapshotRepo).withIvyPatterns
        Seq(releases, snapshots)
      },
      publishMavenStyle := false,
      publishTo := {
        val repo = if (isSnapshot.value) snapshotRepo else releaseRepo
        Some(s3resolver.value(s"$repo S3 bucket", repo).withIvyPatterns)
      },
      publishArtifact in(Compile, packageDoc) := false
    )
  }

  // [fl]
  //
  //
  //
  //
  //
  // [fl]

  val developers = Seq(
    GatlingDeveloper("slandelle@gatling.io", "Stephane Landelle", isGatlingCorp = true),
    GatlingDeveloper("gcorre@gatling.io", "Guillaume Corré", isGatlingCorp = true),
    GatlingDeveloper("tgrenier@gatling.io", "Thomas Grenier", isGatlingCorp = true),
    GatlingDeveloper("ccousseran@gatling.io", "Cédric Cousseran", isGatlingCorp = true),
    GatlingDeveloper("achaouat@gatling.io", "Alexandre Chaouat", isGatlingCorp = true)
  )

  /****************************/
  /** Documentation settings **/
  /****************************/

  lazy val scaladocSettings = Seq(
    autoAPIMappings := true
  )

  def docSettings(excludedProjects: ProjectReference*) = unidocSettings ++ site.settings ++ site.sphinxSupport() ++ Seq(
    unmanagedSourceDirectories in Test := ((sourceDirectory in Sphinx).value ** "code").get,
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(excludedProjects: _*)
  ) ++ scaladocSettings

  /**************************************/
  /** gatling-charts specific settings **/
  /**************************************/

  lazy val chartTestsSettings = Seq(
    fork := true,
    javaOptions in Test := Seq("-DGATLING_HOME=gatling-charts") // Allows FileDataReaderSpec to run
  )

  lazy val excludeDummyComponentLibrary = Seq(
    mappings in (Compile, packageBin) := {
      val compiledClassesMappings = (mappings in (Compile, packageBin)).value
      compiledClassesMappings.filter { case (file, path) => !path.contains("io/gatling/charts/component/impl") }
    }
  )
}
