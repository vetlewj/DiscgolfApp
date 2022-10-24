package no.hiof.discgolfapp.model

data class Course(
    val uid: Int,
    val name: String,
    val holes: List<Hole?>?,
    val rating: Double?,
    val area: String?,
    val city: String?,
    val location: String?,
    val latitude: Float?,
    val longitude: Float?
) {
    //TODO: Finish class

    companion object {
        fun getCourses(): List<Course> {
            // Hentet data rett fra API
            return listOf(
                Course(0, "Muselunden", Hole.getHoles(), null, "Oslo", "Oslo", null, 59.939369F, 10.785842F),
                Course(1, "Ekeberg skole", Hole.getHoles(), null, "Oslo", "Oslo", null, 59.895178F, 10.787161F),
                Course(2, "Frogner", Hole.getHoles(), null, "", null, null, null, null)

            )
        }
    }
}
