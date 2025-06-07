package $package$

import cats.effect.Sync
import cats.syntax.all.*
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.StaticFile
import org.http4s.CacheDirective.`no-cache`
import org.http4s.headers.`Cache-Control`
import cats.data.NonEmptyList

object $name;format="Camel"$Routes:

  def indexRoutes[F[_]: Sync]: HttpRoutes[F] =
    val dsl = new Http4sDsl[F]{}
    import dsl.*
    HttpRoutes.of[F] {
      case req @ GET -> Root =>
        StaticFile
         .fromResource(s"/pages/index.html", Some(req))
         .map(_.putHeaders())
         .map(_.putHeaders(`Cache-Control`(NonEmptyList.of(`no-cache`()))))
         .getOrElseF(NotFound())
    }

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F]{}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F]{}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
