import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import akka.http.scaladsl.server.Directives


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val employeeNameFormat = jsonFormat2(EmployeeName)
  implicit val employeeTypeFormat = jsonFormat1(EmployeeType)
  implicit val employeeFormat = jsonFormat3(Employee)
}

object WebServer extends Directives with JsonSupport {
  def main(args: Array[String]) {

    val employeeDAO: EmployeeDAO = EmployeeDAO()

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    // TODO add exception handler
    val route =
      concat(
        get {
          pathPrefix("employees" / IntNumber) { id =>
            // TODO handle exceptions/possible  404 if employee does not exist
            // TODO return employee
            employeeDAO.get(id)
            complete(StatusCodes.OK)
          }
        } ~
        post {
          path("employee") {
            entity(as[Employee]) { Employee =>
              employeeDAO.create(Employee)
              complete(StatusCodes.OK)
            }
          }
        } ~
        delete {
          path("employees" / IntNumber) { id =>
            // TODO handle exceptions
            employeeDAO.delete(id)
            complete(StatusCodes.OK)
          }
        }
      )
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
