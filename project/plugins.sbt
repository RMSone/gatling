resolvers += Resolver.url("gatling", url("http://dl.bintray.com/content/gatling/sbt-plugins/"))(Resolver.ivyStylePatterns)
resolvers += "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com"

addSbtPlugin("io.gatling" % "gatling-build-plugin" % "2.0.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.6.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.8.2")

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")

addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.25")

addSbtPlugin("ohnosequences" % "sbt-s3-resolver" % "0.16.0")

addMavenResolverPlugin
