
class Filters @Inject() (corsFilter: CORSFilter)
  extends DefaultHttpFilters(corsFilter)

