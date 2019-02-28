object Main {
  def main(args: Array[String]): Unit = {
    // Parse args
    val scenario = args.sliding(2, 1).toList.foldLeft(Scenario.empty) {
      case (acc, cur) => cur match {
        case Array("-scenario", s) => acc.copy(name = s)
        case Array("-interval", i) => acc.copy(interval = i.toInt)
        case Array("-endpoint", e) => acc.copy(endpoint = e)
        case _ => acc // TODO: unknown arg -> print usage
      }
    }

    scenario.start()
  }
}
