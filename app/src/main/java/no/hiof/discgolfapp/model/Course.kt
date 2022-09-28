package no.hiof.discgolfapp.model

data class Course(
    val name: String,
    val holes: Int?,
    val rating: Double?,
    val area: String?,
    val city: String?,
    val location: String?,
    val latitude: Double?,
    val longitude: Double?
) {
    //TODO: Finish class

    companion object {
        fun getCourses(): List<Course> {
            // Hentet data rett fra API
            return listOf(
                Course("Muselunden", null, null, "Oslo", "Oslo", null, 59.939369, 10.785842),
                Course("Ekeberg skole", null, null, "Oslo", "Oslo", null, 59.895178, 10.787161),
                Course("Frogner", null, null, "", null, null, null, null)
                // TODO: Add more sample data
            )
        }
    }
}
