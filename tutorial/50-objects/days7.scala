
val monthLength = Array(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
val weekday = Array("Monday", "Tuesday", "Wednesday", "Thursday",
		    "Friday", "Saturday", "Sunday")
val monthname = Array("January", "February", "March",
		      "April", "May", "June",
		      "July", "August", "September",
		      "October", "November", "December")

class Date(val year: Int, val month: Int, val day: Int) {
  require(1901 <= year && year <= 2099)
  require(1 <= month && month <= 12)
  require(1 <= day && day <= monthLength(month - 1))
  require(month != 2 || day <= 28 || (year % 4) == 0)

  // returns the number of days since 1901/01/01 (day 0)
  def dayIndex: Int = {
    val fourY = (365 + 365 + 365 + 366)
    val yearn = year - 1901
    var total = 0
    total += fourY * (yearn / 4)
    total += 365 * (yearn % 4)
    for (m <- 0 until month - 1)
      total += monthLength(m)
    total += day - 1
    if (year % 4 != 0 && month > 2)
      total -= 1
    total
  }

  def dayOfWeek: String = weekday((dayIndex + 1) % 7)

  override def toString: String = 
    "%s, %s %d, %d".format(dayOfWeek,
			   monthname(month-1),
			   day, 
			   year)

  def num2date(n: Int): Date = {
    val fourY = (365 + 365 + 365 + 366)
    var year = 1901 + (n / fourY) * 4
    var day = n % fourY 
    if (day >= 365 + 365 + 365 + 59) {
      year += 3
      day -= 365 + 365 + 365
    } else {
      year += (day / 365)
      day = day % 365
      if (day >= 59)
	day += 1
    }
    var month = 1
    while (day >= monthLength(month-1)) {
      day -= monthLength(month-1)
      month += 1
    }
    new Date(year, month, day+1)
  }

  def -(rhs: Date) = dayIndex - rhs.dayIndex

  def +(n: Int): Date = num2date(dayIndex + n)
  def -(n: Int): Date = num2date(dayIndex - n)
}

def testDays() {
  val maxDate = new Date(2099, 12, 31)
  val maxDay = maxDate.dayIndex
  var s = scala.collection.mutable.Set.empty[Date]
  for (i <- 0 to maxDay) {
    val d = maxDate.num2date(i)
    s += d
    assert(i == d.dayIndex, i)
  }
  println(s.size)
}
