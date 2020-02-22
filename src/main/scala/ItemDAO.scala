import scala.collection.mutable

sealed trait BaseRepository {
  def create(empl: Employee): Unit
  def get(id: Int): Employee
  def update(newEmpl: Employee): Unit
  def delete(id: Int): Unit
}

final case class EmployeeName(firstName: String, lastName: String)
final case class EmployeeType(employeeType: Int)
final case class Employee(id: Int, name: EmployeeName, employeeType: EmployeeType)

class EmployeeDAO extends BaseRepository {

  val items : mutable.Map[Int, Employee] =  mutable.Map()

  def get(id: Int): Employee = {
    if(!items.contains(id)) {
      throw new EmployeeNotFoundException
    }
    items(id)
  }

  def create(item: Employee): Unit = {
    if(items.contains(item.id)) {
      throw new Exception
    }
    items.put(item.id, item)
  }

  def update(newEmpl: Employee): Unit = {
    items.put(newEmpl.id, newEmpl)
  }

  def delete(id: Int): Unit = {
    items.remove(id)
  }
}

object EmployeeDAO {
  def apply(): EmployeeDAO = new EmployeeDAO()
}

final case class EmployeeNotFoundException() extends Exception
