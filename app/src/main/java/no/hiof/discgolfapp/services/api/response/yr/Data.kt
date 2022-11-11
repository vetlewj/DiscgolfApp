package no.hiof.discgolfapp.services.api.response.yr

data class Data(
    val instant: Instant,
    val next_12_hours: Next12Hours?,
    val next_1_hours: Next1Hours?,
    val next_6_hours: Next6Hours?
)