package no.hiof.discgolfapp.model

data class Course(
    val uid: Int,
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
                Course(0, "Muselunden", null, null, "Oslo", "Oslo", null, 59.939369, 10.785842),
                Course(1, "Ekeberg skole", null, null, "Oslo", "Oslo", null, 59.895178, 10.787161),
                Course(2, "Frogner", null, null, "", null, null, null, null)
                // TODO: Add more sample data
            )
        }
    }
}
