package com.example.scheduleapp.data

data class Date (
    var year : Int?   = null,
    var month : Int?   = null,
    var day : Int?   = null
)

data class Schedule (
    var lessonNum       : Int?    = null,
    var discipline : String? = null,
    var cabinet    : String?    = null,
    var teacher    : String? = null
)